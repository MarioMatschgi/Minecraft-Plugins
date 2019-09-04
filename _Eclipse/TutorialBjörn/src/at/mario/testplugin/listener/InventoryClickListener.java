package at.mario.testplugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.mario.testplugin.Main;
import at.mario.testplugin.manager.DataManager;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		Inventory open = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		Integer slot = e.getSlot();
		
		

		if (open == null) {
			return;
		}
		
		
		
		
		
		
		
		
		
		
		if (item == null || !item.hasItemMeta()) {
			Bukkit.broadcastMessage("JUP!");
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					ItemStack item1 = item;
					ItemStack item2 = open.getItem(e.getSlot());
					
					if (item2 != null && item2.hasItemMeta()) {
						Bukkit.broadcastMessage("JUP1");
						for (int i = 0; i < Main.itemNames.size(); i++) {
							if (item2.getItemMeta().getDisplayName().equals(Main.itemNames.get(i))) {
								Bukkit.broadcastMessage("JUP3");
								
								p.getInventory().setItem(slot, item2);
								
								open.setItem(e.getSlot(), item1);
								
								return;
							} else {
								Bukkit.broadcastMessage("NOPE3");
							}
						}
					} else {
						Bukkit.broadcastMessage("NOPE1");
					}
					
					Bukkit.broadcastMessage("Item1: " + item1);
					Bukkit.broadcastMessage("Item2: " + item2);
				}
			}, 1);
			
			return;
		} else {
			Bukkit.broadcastMessage("NOPE!");
		}
		
		if (slot < 40) {
			if (p.getInventory().getItem(e.getSlot()) != null && p.getInventory().getItem(e.getSlot()).hasItemMeta()) {
				for (int i = 0; i < Main.itemNames.size(); i++) {
					if (p.getInventory().getItem(e.getSlot()).getItemMeta().getDisplayName().equals(Main.itemNames.get(i))) {
						e.setCancelled(true);
						
						break;
					}
				}
			}
		}
		
		
		

		
		if (item == null || !item.hasItemMeta()) {
			return;
		}
		
		
		
		
		if (open.getName().equals("§aClick to §6teleport§f!")) {
			e.setCancelled(true);
			
			if (DataManager.getData().contains("Data.teleports." + item.getItemMeta().getDisplayName())) {
				Location loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
				loc.setWorld(Bukkit.getServer().getWorld(DataManager.getData().getString("Data.teleports." + item.getItemMeta().getDisplayName() + ".world")));
				loc.setX(DataManager.getData().getDouble("Data.teleports." + item.getItemMeta().getDisplayName() + ".x"));
				loc.setY(DataManager.getData().getDouble("Data.teleports." + item.getItemMeta().getDisplayName() + ".y"));
				loc.setZ(DataManager.getData().getDouble("Data.teleports." + item.getItemMeta().getDisplayName() + ".z"));
				loc.setYaw((float) DataManager.getData().get("Data.teleports." + item.getItemMeta().getDisplayName() + ".yaw"));
				loc.setPitch((float) DataManager.getData().get("Data.teleports." + item.getItemMeta().getDisplayName() + ".pitch"));
				
				p.closeInventory();
				p.teleport(loc);
				
				return;
			}
		}
	}
}
