package me.quin.buildtoguess.game.event;

import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.quin.buildtoguess.BuildToGuess;
import me.quin.buildtoguess.game.Game;
import me.quin.buildtoguess.game.GameState;

public class BuildListener implements Listener {
	
	private BuildToGuess b2g;
	private Game game;
	private static ArrayList<Block> placedBlocks;
	
	public BuildListener(Game game, BuildToGuess b2g)
	{
		this.game = game;
		this.b2g = b2g;
		placedBlocks = new ArrayList<Block>();
	}
	
	@EventHandler
	public void onBuild(BlockPlaceEvent event)
	{
		if(b2g.getGameState() != GameState.GAME)
		{
			event.setCancelled(true);
			return;
		}
		if(event.getPlayer() != game.getBuilder())
		{
			event.setCancelled(true);
			return;
		}
		placedBlocks.add(event.getBlock());
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event)
	{
		if(b2g.getGameState() != GameState.GAME)
		{
			event.setCancelled(true);
			return;
		}
		if(event.getPlayer() != game.getBuilder())
		{
			event.setCancelled(true);
			return;
		}
		if(!placedBlocks.contains(event.getBlock()))
		{
			event.setCancelled(true);
			return;
		}
	}
	
	public static void restoreBlocks()
	{
		for(Block block : placedBlocks)
		{
			block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
			block.setType(Material.AIR);
		}
		placedBlocks.clear();
	}
}
