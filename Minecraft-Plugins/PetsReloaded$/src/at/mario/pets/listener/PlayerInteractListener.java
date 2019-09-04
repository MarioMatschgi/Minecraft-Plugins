package at.mario.pets.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractListener implements Listener {
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e) {
		Entity entity = e.getRightClicked();
	  
		if (!(entity instanceof NPC)) {
	      return;
		}
		if (entity instanceof Villager) {
			if (InventoryClick.pets.containsValue(entity)) {
				e.setCancelled(true);
			}
		}
	}
}
