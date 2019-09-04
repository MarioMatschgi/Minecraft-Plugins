package at.mario.lobby.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.DataManager;

public class BlockBreakListener implements Listener {
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		DataManager dm = new DataManager();
		Player p = e.getPlayer();	
		Block block = e.getBlock();
		ItemStack item = p.getItemInHand();

		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".build") == false) {
			if (Main.isinLobby(block.getLocation())) {
				e.setCancelled(true);
			}
		}
		
		if (item == null || item.getItemMeta() == null) {
			return;
		}
		
		if (item.getItemMeta().getDisplayName() == Main.wandName) {
			e.setCancelled(true);
		}
	}
}
