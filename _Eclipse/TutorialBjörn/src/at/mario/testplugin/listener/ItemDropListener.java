package at.mario.testplugin.listener;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import at.mario.testplugin.Main;

public class ItemDropListener implements Listener {
	
	@EventHandler
	public void itemDrop(PlayerDropItemEvent e) {
		Item item = e.getItemDrop();
		
		// Testen, ob es überhaupt ein item ist
		if (item == null || item.getItemStack() == null || !item.getItemStack().hasItemMeta() || !item.getItemStack().getType().equals(Material.AIR)) {
			// Wenn es KEIN Item oder Luft ist, abbrechen
			
			return;
		}
		
		// Wenn es ein Item ist, abfragen, ob der Name des Items dem Namen in der "itemNames" Liste enthalten ist
		if (Main.itemNames.contains(item.getItemStack().getItemMeta().getDisplayName())) {
			// Wenn es in der Liste enthalten ist, das Event abbrechen, um zu verhindern, dass es gedroppt wird.
			
			e.setCancelled(true);
			return;
		}
	}
}
