package me.quin.buildtoguess.game.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.quin.buildtoguess.BuildToGuess;
import me.quin.buildtoguess.game.Game;

public class BuilderCountdown extends BukkitRunnable{
	
	private BuildToGuess b2g;
	private Game game;
	private int remaining;
	private int countdownId;
	
	public BuilderCountdown(BuildToGuess b2g, Game game)
	{
		this.b2g = b2g;
		this.game = game;
		remaining = 5;
		runTaskTimer(b2g, 20l, 20l);
	}
	
	public void setTime(int remaining)
	{
		this.remaining = remaining;
	}

	@Override
	public void run() {
		remaining--;
		if(remaining == 0)
		{
			List<Player> players = new ArrayList<Player>();
			for(Player player : Bukkit.getServer().getOnlinePlayers())
				players.add(player);
			Player builder = players.get(game.getBuilderId());
			Bukkit.broadcastMessage(b2g.sendMessage("The builder is: " + builder.getName()));
			ArrayList<String> list = (ArrayList<String>) b2g.getListConfig().getStringList("Wordlist");
			String randomWord = list.get(new Random().nextInt(list.size()));
			game.setWord(randomWord);
			game.setBuilder(builder);
			builder.sendMessage(b2g.sendMessage("Your word is: " + randomWord));
			Bukkit.getServer().getScheduler().cancelTask(countdownId);	
		}
	}

}
