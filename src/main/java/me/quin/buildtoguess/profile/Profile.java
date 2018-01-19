package me.quin.buildtoguess.profile;

import java.util.UUID;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.quin.buildtoguess.BuildToGuess;

@Getter
@Setter
 public class Profile {

	private BuildToGuess b2g;
	
    private UUID uuid;
    private String name;
    private boolean loaded;
    private int coins;

    public Profile(BuildToGuess b2g, Player player) 
    {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.b2g = b2g;
        loaded = true;
    }
    
    public UUID getUuid()
    {
    	return uuid;
    }
    
    public String getName()
    {
    	return name;
    }
    
    public void setCoins(int coins)
    {
    	this.coins = coins;
    }
    
    public int getCoins()
    {
    	return coins;
    }
    
    public boolean isLoaded()
    {
    	return loaded;
    }
    
    public BuildToGuess getBuildToGuess()
    {
    	return b2g;
    }

}