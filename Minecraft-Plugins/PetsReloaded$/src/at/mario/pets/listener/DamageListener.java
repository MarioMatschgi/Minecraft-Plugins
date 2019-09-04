package at.mario.pets.listener;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {
	
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		Entity entity = (Entity) e.getEntity();
		if (InventoryClick.pets.containsValue(entity)) {
			e.setCancelled(true);
		}
		if (e.getDamager() != null) {
			Entity damager = (Entity) e.getDamager();
			if (InventoryClick.pets.containsValue(damager)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBDmg(EntityDamageEvent e) {
		Entity entity = (Entity) e.getEntity();
		if (InventoryClick.pets.containsValue(entity)) {
			e.setCancelled(true);
		}
	}
}
