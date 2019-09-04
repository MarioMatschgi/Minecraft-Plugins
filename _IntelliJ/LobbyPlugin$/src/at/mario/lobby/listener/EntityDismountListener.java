package at.mario.lobby.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

import at.mario.lobby.manager.ConfigManagers.DataManager;

public class EntityDismountListener implements Listener {
	
	@EventHandler
	public void onDismount(EntityDismountEvent e) {
		DataManager dm = new DataManager();

		Entity entity = e.getDismounted();
		
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			if (InventoryClick.pets.containsValue(entity)) {
				dm.getData().set("Data." + p.getName().toLowerCase() + ".ride", "false");
				dm.saveData();
			}
		}
	}
}
