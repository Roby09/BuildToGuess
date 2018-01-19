package me.quin.buildtoguess.game.task;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.quin.buildtoguess.BuildToGuess;
import me.quin.buildtoguess.game.Game;

public class EndGameCountdown extends BukkitRunnable{
	
	private Game game;
	private int remaining;
	private int countdownId;
	
	public EndGameCountdown(BuildToGuess b2g, Game game)
	{
		this.game = game;
		remaining = 10;
		runTaskTimer(b2g, 20l, 20l);
	}
	
	public void setTime(int remaining)
	{
		this.remaining = remaining;
	}

	@Override
	public void run() {
		for(Player player : Bukkit.getServer().getOnlinePlayers())
		{
			float f = 0 + new Random().nextFloat() * (2.5F - 0);
			player.getWorld().playEffect(player.getLocation(), Effect.FIREWORKS_SPARK, 5);
			player.getWorld().playEffect(player.getLocation(), Effect.FIREWORKS_SPARK, 5);
			player.getWorld().playEffect(player.getLocation(), Effect.FIREWORKS_SPARK, 5);
			player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
			player.getWorld().playSound(player.getLocation(), Sound.FIREWORK_LARGE_BLAST, 2, f);
			player.getWorld().playSound(player.getLocation(), Sound.FIREWORK_BLAST, 2, f);
		}
		remaining--;
		if(remaining == 0)
		{
			Bukkit.getScheduler().cancelTask(countdownId);
			game.stopGame();
		}
	}

}
