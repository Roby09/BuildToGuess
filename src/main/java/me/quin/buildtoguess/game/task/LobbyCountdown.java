package me.quin.buildtoguess.game.task;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.quin.buildtoguess.BuildToGuess;

public class LobbyCountdown extends BukkitRunnable{
	
	private BuildToGuess b2g;
	private int remaining;
	private int countdownId;
	
	public LobbyCountdown(BuildToGuess b2g)
	{
		this.b2g = b2g;
		remaining = 30;
		runTaskTimer(b2g, 20l, 20l);
	}
	
	public void setTime(int remaining)
	{
		this.remaining = remaining;
	}

	public void cancelTask()
	{
		Bukkit.getServer().getScheduler().cancelTask(countdownId);	
	}

	@Override
	public void run() {
		for(Player player : Bukkit.getServer().getOnlinePlayers())
		{
			if(remaining == 30 || remaining == 25 || remaining == 20 || remaining == 15 || remaining == 10 || remaining <= 5)
			{
				player.sendMessage(b2g.sendMessage(remaining + " seconds until the game begins!"));
				player.getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
			}
		}
		remaining--;
		if(remaining == 0)
		{
			Bukkit.getScheduler().cancelTask(countdownId);	
			b2g.getGame().startGame();
		}
	}

}
