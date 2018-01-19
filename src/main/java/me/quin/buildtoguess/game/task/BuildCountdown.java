package me.quin.buildtoguess.game.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.quin.buildtoguess.BuildToGuess;
import me.quin.buildtoguess.game.Game;
import me.quin.buildtoguess.scoreboard.GameScoreboard;

public class BuildCountdown extends BukkitRunnable{
	
	private BuildToGuess b2g;
	private Game game;
	private int remaining;
	private int countdownId;
	
	public BuildCountdown(BuildToGuess b2g, Game game)
	{
		this.b2g = b2g;
		this.game = game;
	}
	
	public void start()
	{
		remaining = 120;
		runTaskTimer(b2g, 20l, 20l);
	}
	
	public void stop()
	{
		Bukkit.getServer().getScheduler().cancelTask(countdownId);	
	}

	@Override
	public void run() {
		remaining--;
		for(Player player : Bukkit.getServer().getOnlinePlayers())
		{
			GameScoreboard scoreboard = b2g.getScoreboardManager().getScoreboard(player);
			scoreboard.editLine(10, ChatColor.GRAY + String.valueOf(remaining) + "s");
		}
		if(remaining == 60)
		{
			game.setReward(5);
		}
		if(remaining == 30)
		{
			game.setReward(3);
		}
		if(remaining == 0)
		{
			Bukkit.broadcastMessage(b2g.sendMessage("The word hasn't been guessed.!"));
			Bukkit.broadcastMessage(b2g.sendMessage("The word was: " + game.getWord()));
			game.setRound(game.getRound()+1);
		}
	}

}
