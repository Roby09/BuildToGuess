package me.quin.buildtoguess.game;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import me.quin.buildtoguess.BuildToGuess;
import me.quin.buildtoguess.game.event.BuildListener;
import me.quin.buildtoguess.game.task.BuilderCountdown;
import me.quin.buildtoguess.game.task.BuildCountdown;
import me.quin.buildtoguess.game.task.EndGameCountdown;
import me.quin.buildtoguess.profile.Profile;
import me.quin.buildtoguess.profile.ProfileSaver;
import me.quin.buildtoguess.scoreboard.GameScoreboard;

public class Game {
	
	private BuildToGuess b2g;
	private String mapName;
	private Location spawn;
	private Location builderSpawn;
	private Player builder;
	private int round;
	private int builderId;
	private HashMap<Player, Integer> points;
	private String word;
	private int roundsUntilEnd;
	private int reward;
	private BuildCountdown buildCountdown;
	
	public Game(BuildToGuess b2g, String mapName)
	{
		this.b2g = b2g;
		this.mapName = mapName;
		getSpawnPoints();
		builderId = 0;
		points = new HashMap<Player, Integer>();
		round = 1;
		buildCountdown = new BuildCountdown(b2g, this);
	}
	
	public void teleportToGame()
	{
		for(Player player : Bukkit.getServer().getOnlinePlayers())
		{
			player.teleport(getSpawn());
			player.sendMessage(b2g.sendMessage("The game has now started.."));
		}
	}
	
	public void startGame()
	{
		teleportToGame();
		chooseNewBuilder();
		roundsUntilEnd = Bukkit.getServer().getOnlinePlayers().size();
		for(Player player : Bukkit.getServer().getOnlinePlayers())
		{
			setupScoreboard(player);
			points.put(player, 0);
		}
	}
	
	public void stopGame()
	{
		b2g.setGameState(GameState.LOBBY);
		for(Player player : Bukkit.getServer().getOnlinePlayers())
		{
			player.setGameMode(GameMode.SURVIVAL);
			player.teleport(b2g.getLobby().getSpawn());
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			Profile profile = b2g.getProfile(player);
			profile.setCoins(profile.getCoins()+getPoints(player));
			new ProfileSaver(profile);
			points.clear();
		}
		if(Bukkit.getServer().getOnlinePlayers().size() >= b2g.getConf().getInt("Lobby.minPlayers"))
			b2g.getLobby().startCountdown();
		b2g.setGameState(GameState.LOBBY);
	}
	
	public void setupScoreboard(Player player)
	{
		GameScoreboard board = new GameScoreboard("Build2Guess");
		board.addLine(ChatColor.GREEN + "" + ChatColor.BOLD + "Builder");
		board.addLine(ChatColor.GRAY + "No Builder");
		board.addBlankLine();
		board.addLine(ChatColor.RED + "" + ChatColor.BOLD + "Word");
		board.addLine(ChatColor.GRAY + "No Word");
		board.addBlankLine();
		board.addLine(ChatColor.AQUA + "" + ChatColor.BOLD + "Round");
		board.addLine(ChatColor.GRAY + "1/" + getRoundsUntilEnd());
		board.addBlankLine();
		board.addLine(ChatColor.AQUA + "" + ChatColor.BOLD + "Time");
		board.addLine(ChatColor.GRAY + "No Time");
		board.addBlankLine();
		board.addLine(ChatColor.AQUA + "" + ChatColor.BOLD + "Points");
		board.addLine(ChatColor.GRAY + "0");
		b2g.getScoreboardManager().setScoreboard(player, board);
	}
	
	public void chooseNewBuilder()
	{
		new BuilderCountdown(b2g, this);
		b2g.setGameState(GameState.NEWBUILDER);
		Bukkit.broadcastMessage(b2g.sendMessage("A new builder is being chosen/!"));
		BuildListener.restoreBlocks();
		setReward(10);
	}
	
	public void getSpawnPoints()
	{
		World world = Bukkit.getWorld(getMapName());
		for(Chunk chunk : world.getLoadedChunks())
		{
			for(BlockState state : chunk.getTileEntities())
			{
				if(state instanceof Sign)
				{
					Sign sign = (Sign) state;
					if(sign.getLine(0).equalsIgnoreCase("[spawnpoint]"))
					{
						setSpawn(sign.getLocation());
						//TODO Weghalen
						//sign.getBlock().setType(Material.AIR);
					}
					if(sign.getLine(0).equalsIgnoreCase("[builderspawn]"))
					{
						setBuilderSpawn(sign.getLocation());
						//sign.getBlock().setType(Material.AIR);
					}
				}
			}
		}
	}
	
	public void setMapName(String mapName)
	{
		this.mapName = mapName;
	}
	
	public String getMapName()
	{
		return mapName;
	}
	
	public void setSpawn(Location spawn)
	{
		this.spawn = spawn;
	}
	
	public Location getSpawn()
	{
		return spawn;
	}
	
	public void setBuilderSpawn(Location builderSpawn)
	{
		this.builderSpawn = builderSpawn;
	}
	
	public Location getBuilderSpawn()
	{
		return builderSpawn;
	}
	
	public void setBuilder(Player builder)
	{
		if(getRound() != 1)
		{
			GameScoreboard oldBoard = b2g.getScoreboardManager().getScoreboard(this.builder);
			oldBoard.editLine(16, ChatColor.GRAY + "Geen Woord");
			getBuilder().teleport(getSpawn());
			getBuilder().setGameMode(GameMode.SURVIVAL);
			getBuilder().getInventory().clear();
		}
		this.builder = builder;
		builder.setGameMode(GameMode.CREATIVE);
		builder.teleport(getBuilderSpawn());
		for(Player player : Bukkit.getServer().getOnlinePlayers())
		{
			GameScoreboard board = 	b2g.getScoreboardManager().getScoreboard(player);
			board.editLine(19, ChatColor.GRAY + builder.getName());
		}
		GameScoreboard board = 	b2g.getScoreboardManager().getScoreboard(builder);
		board.editLine(16, ChatColor.GRAY + getWord());
		getBuildCountdown().start();
		builderId++;
		b2g.setGameState(GameState.GAME);
	}
	
	public Player getBuilder()
	{
		return builder;
	}
	
	public void setRound(int round)
	{
		if(getRound() == getRoundsUntilEnd())
		{
			new EndGameCountdown(b2g, this);
			//Bukkit.broadcastMessage(b2g.sendMessage("De game is gewonnen door: " + winner.getName()));
			//TODO Winner uitrekenen
			b2g.setGameState(GameState.ENDING);
			BuildListener.restoreBlocks();
			return;
		}
		this.round = round;
		for(Player player : Bukkit.getServer().getOnlinePlayers())
		{
			GameScoreboard board = 	b2g.getScoreboardManager().getScoreboard(player);
			board.editLine(13, ChatColor.GRAY + String.valueOf(this.round) + "/" + getRoundsUntilEnd());
		}
		chooseNewBuilder();
	}
	
	public int getRound()
	{
		return round;
	}
	
	public int getBuilderId()
	{
		return builderId;
	}
	
	public void setPoints(Player player, int points)
	{
		this.points.put(player, points);
		player.sendMessage(b2g.sendMessage("You now have: " + points + " points!"));
		GameScoreboard scoreboard = b2g.getScoreboardManager().getScoreboard(player);
		scoreboard.editLine(7, ChatColor.GRAY + String.valueOf(getPoints(player)));
	}
	
	public int getPoints(Player player)
	{
		return points.get(player);
	}
	
	public void setWord(String word)
	{
		this.word = word;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public void setRoundUntilEnd(int roundsUntilEnd)
	{
		this.roundsUntilEnd = roundsUntilEnd;
	}
	
	public int getRoundsUntilEnd()
	{
		return roundsUntilEnd;
	}
	
	public void setReward(int reward)
	{
		this.reward = reward;
	}
	
	public int getReward()
	{
		return reward;
	}
	
	public BuildCountdown getBuildCountdown()
	{
		return buildCountdown;
	}
}
