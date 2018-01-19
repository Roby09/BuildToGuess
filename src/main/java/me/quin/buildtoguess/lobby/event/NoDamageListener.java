package me.quin.buildtoguess.lobby.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class NoDamageListener implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageEvent event)
	{
		if(event.getEntity() instanceof Player)
			event.setCancelled(true);
	}

}
