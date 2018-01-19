package me.quin.buildtoguess.game.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.quin.buildtoguess.BuildToGuess;
import me.quin.buildtoguess.game.Game;
import me.quin.buildtoguess.game.GameState;
import me.quin.buildtoguess.scoreboard.GameScoreboard;

public class LeaveListener implements Listener {
	
	private BuildToGuess b2g;
	private Game game;
	
	public LeaveListener(Game game, BuildToGuess b2g)
	{
		this.game = game;
		this.b2g = b2g;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event)
	{
		if(b2g.getGameState() != GameState.GAME || b2g.getGameState() != GameState.NEWBUILDER)
			return;
		game.setRoundUntilEnd(game.getRoundsUntilEnd()-1);
		for(Player player : Bukkit.getServer().getOnlinePlayers())
		{
			GameScoreboard board = 	b2g.getScoreboardManager().getScoreboard(player);
			board.editLine(13, ChatColor.GRAY + String.valueOf(game.getRound()) + "/" + game.getRoundsUntilEnd());
		}
		if(event.getPlayer() == game.getBuilder())
		{
			game.setRound(game.getRound()+1);
			Bukkit.broadcastMessage(b2g.sendMessage("The builder has left the game."));
		}
	}
}
