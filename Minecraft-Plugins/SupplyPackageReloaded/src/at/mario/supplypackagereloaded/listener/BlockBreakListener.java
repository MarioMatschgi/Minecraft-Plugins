package at.mario.supplypackagereloaded.listener;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import at.mario.supplypackagereloaded.Main;

public class BlockBreakListener implements Listener {
	
	@EventHandler
    public void on(BlockBreakEvent e) {
		Block block = e.getBlock();
		
		if (Main.blocks.containsKey(block)) {
        	e.setCancelled(true);
		}
    }
}
