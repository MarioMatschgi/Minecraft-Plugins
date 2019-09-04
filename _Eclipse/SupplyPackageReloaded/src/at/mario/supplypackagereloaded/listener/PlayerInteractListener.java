package at.mario.supplypackagereloaded.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import at.mario.supplypackagereloaded.Main;
import at.mario.supplypackagereloaded.commands.SupplypackageCMD;

public class PlayerInteractListener implements Listener {
	
	@EventHandler
    public void on(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Block block = e.getClickedBlock();
		
		if (Main.blocks.containsKey(block)) {
        	e.setCancelled(true);

    		if (Main.blocks.containsValue(p) || Main.blocks.get(block) == null) {
	        	block.setType(Material.AIR);
	        	Main.blocks.remove(block);
	        	
	        	SupplypackageCMD.giveSupplies(p);
    		}
		}
    }
}
