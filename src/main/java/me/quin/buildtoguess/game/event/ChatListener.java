package me.quin.buildtoguess.game.event;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.quin.buildtoguess.BuildToGuess;
import me.quin.buildtoguess.game.Game;
import me.quin.buildtoguess.game.GameState;
import me.quin.buildtoguess.util.InstantFirework;

public class ChatListener implements Listener {
	
	private BuildToGuess b2g;
	private Game game;
	
	public ChatListener(Game game, BuildToGuess b2g)
	{
		this.game = game;
		this.b2g = b2g;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)
	{
		if(b2g.getGameState() != GameState.GAME)
			return;
		Player player = event.getPlayer();
		if(player == game.getBuilder())
		{
			event.setCancelled(true);
			player.sendMessage(b2g.sendMessage("You can not talk while you are a builder."));
			return;
		}
		if(event.getMessage().equalsIgnoreCase(game.getWord()))
		{
			event.setCancelled(true);
			Bukkit.broadcastMessage(b2g.sendMessage(player.getName() + " guessed the word!"));
			Bukkit.broadcastMessage(b2g.sendMessage("The word was: " + game.getWord()));
			game.setPoints(player, game.getPoints(player)+game.getReward());
			game.setPoints(game.getBuilder(), game.getPoints(game.getBuilder())+5);
			game.getBuildCountdown().stop();
			new BukkitRunnable()
			{
				public void run()
				{
					InstantFirework.createFireworkEffect(FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.BALL).withColor(Color.AQUA).build(), game.getBuilderSpawn().clone().add(0,5,0));
					InstantFirework.createFireworkEffect(FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.BLUE).build(), game.getBuilderSpawn().clone().add(0,5,0));
				}
			}.runTaskLater(b2g, 1l);
			new BukkitRunnable()
			{
				public void run()
				{
					game.setRound(game.getRound()+1);
				}
			}.runTaskLater(b2g, 60l);
		}
	}

}
