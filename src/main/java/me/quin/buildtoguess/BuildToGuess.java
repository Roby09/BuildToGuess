package me.quin.buildtoguess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.quin.buildtoguess.database.Database;
import me.quin.buildtoguess.game.Game;
import me.quin.buildtoguess.game.GameState;
import me.quin.buildtoguess.game.event.BuildListener;
import me.quin.buildtoguess.game.event.ChatListener;
import me.quin.buildtoguess.lobby.Lobby;
import me.quin.buildtoguess.lobby.event.FoodListener;
import me.quin.buildtoguess.lobby.event.NoDamageListener;
import me.quin.buildtoguess.lobby.event.PlayerJoinListener;
import me.quin.buildtoguess.lobby.event.PlayerQuitListener;
import me.quin.buildtoguess.profile.Profile;
import me.quin.buildtoguess.scoreboard.ScoreboardManager;
import me.quin.buildtoguess.util.Files;

public class BuildToGuess extends JavaPlugin {
	
	/**
	 * @author Inferides / Q de Preter
	 */
	
	private Game game = new Game(this, "world");;
	private GameState gameState = GameState.LOBBY;;
	private Lobby lobby = new Lobby(this, "lobby");;
	private ScoreboardManager scoreboardManager = new ScoreboardManager();
	private Files config;
	private Files listConfig;
	private Database database = new Database();
    private HashMap<UUID, Profile> profiles = new HashMap<>();
		
	@Override
	public void onEnable()
	{
		config = new Files(getDataFolder(), "config");
		listConfig = new Files(getDataFolder(), "wordlist");
		if(!(config.fileExists()))
		{
			System.out.println("[B2G] Config not found. Making a new one.");
			createConfig(config);
		}
		if(!(listConfig.fileExists()))
		{
			System.out.println("[B2G] Wordlist not found. Making a new one.");
			createListConfig(listConfig);
		}
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
		Bukkit.getPluginManager().registerEvents(new ChatListener(game, this), this);
		Bukkit.getPluginManager().registerEvents(new BuildListener(game, this), this);
		Bukkit.getPluginManager().registerEvents(new NoDamageListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
		Bukkit.getPluginManager().registerEvents(new FoodListener(), this);
		config.loadFile();
		listConfig.loadFile();
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	public void createConfig(Files config)
	{
		System.out.println("[B2G] Confing is being made.");
		if(config == null)
		{
			return;
		}
		config.createFile();
		config.set("Locations.lobby.X", 0);
		config.set("Locations.lobby.Y", 0);
		config.set("Locations.lobby.Z", 0); 
		config.set("Lobby.maxPlayers", 10);
		config.set("Lobby.minPlayers", 5);
		config.saveFile();
	}
	
	public void createListConfig(Files config)
	{
		System.out.println("[B2G] Wordlist is being made.");
		if(config == null)
		{
			return;
		}
		config.createFile();
		config.set("Wordlist", new ArrayList<String>());
		ArrayList<String> list = (ArrayList<String>) config.getStringList("Wordlist");
		list.add("word");
		config.set("Wordlist", list);
		config.saveFile();
	}
	
	public Game getGame()
	{
		return game;
	}

	public Lobby getLobby()
	{
		return lobby;
	}
	
	public void setGameState(GameState gameState)
	{
		this.gameState = gameState;
	}
	
	public GameState getGameState()
	{
		return gameState;
	}
	
	public String sendMessage(String msg)
	{
		return ChatColor.GRAY + "[" + ChatColor.AQUA + "B2G" + ChatColor.GRAY + "]" + ChatColor.WHITE + " " + msg;
	}
	
	public Files getConf()
	{
		return config;
	}
	
	public Files getListConfig()
	{
		return listConfig;
	}
	
	public ScoreboardManager getScoreboardManager()
	{
		return scoreboardManager;
	}
	
	public Database getDatabas()
	{
		return database;
	}
	
	public HashMap<UUID, Profile> getProfiles()
	{
		return profiles;
	}
	
	public Profile getProfile(Player player)
	{
		return profiles.get(player);
	}

}
