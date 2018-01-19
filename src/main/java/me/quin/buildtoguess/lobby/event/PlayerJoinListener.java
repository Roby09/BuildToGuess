package me.quin.buildtoguess.lobby.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.quin.buildtoguess.BuildToGuess;
import me.quin.buildtoguess.game.GameState;
import me.quin.buildtoguess.profile.Profile;
import me.quin.buildtoguess.profile.ProfileLoader;

public class PlayerJoinListener implements Listener {
	
	private BuildToGuess b2g;
	
	public PlayerJoinListener(BuildToGuess b2g)
	{
		this.b2g = b2g;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(b2g.getGameState() != GameState.LOBBY)
		{
			event.getPlayer().kickPlayer(ChatColor.RED + "De game is still running.");
			event.setJoinMessage("");
			return;
		}
		if(b2g.getConf().getInt("Lobby.maxPlayers")-1 == Bukkit.getServer().getOnlinePlayers().size())
		{
			event.getPlayer().kickPlayer(ChatColor.RED + "De game is helaas al vol.");
			event.setJoinMessage("");
			return;
		}
		int maxPlayers = b2g.getConf().getInt("Lobby.maxPlayers");
		event.setJoinMessage(ChatColor.GREEN + "+ " + event.getPlayer().getName() + ChatColor.GRAY +  " (" + Bukkit.getServer().getOnlinePlayers().size() + "/" + maxPlayers + ")");
		event.getPlayer().teleport(b2g.getLobby().getSpawn());
		event.getPlayer().setFoodLevel(20);
		event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
		event.getPlayer().getInventory().clear();
		final Profile profile = new Profile(b2g, event.getPlayer());
        b2g.getProfiles().put(event.getPlayer().getUniqueId(), profile);
        new ProfileLoader(profile).runTaskAsynchronously(b2g);
		if(b2g.getConf().getInt("Lobby.minPlayers") == Bukkit.getServer().getOnlinePlayers().size())
			b2g.getLobby().startCountdown();
	}

}
