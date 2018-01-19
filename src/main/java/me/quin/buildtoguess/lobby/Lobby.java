package me.quin.buildtoguess.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;

import me.quin.buildtoguess.BuildToGuess;
import me.quin.buildtoguess.game.task.LobbyCountdown;

public class Lobby {
	
	private BuildToGuess b2g;
	private Location spawn;
	private LobbyCountdown countdown;
	
	public Lobby(BuildToGuess b2g, String worldName)
	{
		this.b2g = b2g;
		Bukkit.createWorld(new WorldCreator(worldName));
		double x = b2g.getConf().getDouble("Locations.lobby.X");
		double y = b2g.getConf().getDouble("Locations.lobby.Y");
		double z = b2g.getConf().getDouble("Locations.lobby.Z");
		spawn = new Location(Bukkit.getWorld(worldName), x,y,z);
	}
	
	public void startCountdown()
	{
		countdown = new LobbyCountdown(b2g);
	}
	
	public Location getSpawn()
	{
		return spawn;
	}
		
	public LobbyCountdown getCountdown()
	{
		return countdown;
	}
}
