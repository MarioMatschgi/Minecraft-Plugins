package at.mario.supplypackagereloaded.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import at.mario.supplypackagereloaded.Main;
import at.mario.supplypackagereloaded.manager.ConfigManagers.MessagesManager;

public class EntityChangeBlockListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onBlockFall(EntityChangeBlockEvent e) {
		MessagesManager mm = new MessagesManager();
		
        if ((e.getEntityType() == EntityType.FALLING_BLOCK)) {
        	Block block = e.getBlock();
        	
        	Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					Player p = Main.fallingBlocks.get(e.getEntity());
					Location loc = block.getLocation();
					
		        	block.setType(Material.valueOf(Main.getInstance().getConfig().getString("Config.landedBlockMaterial")));
		        	
					Main.fallingBlocks.remove(e.getEntity());
					Main.blocks.put(block.getLocation().getWorld().getBlockAt(loc), p);
					
					p.sendMessage(mm.getMessages().getString("Messages.successfullyLanded").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
							replace("%x%", loc.getBlockX()+"").replace("%y%", loc.getBlockY()+"").replace("%z%", loc.getBlockZ()+""));
				}
			}, 2);
        }
    }
}
