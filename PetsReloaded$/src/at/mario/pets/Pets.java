package at.mario.pets;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import at.mario.pets.listener.InventoryClick;
import at.mario.pets.manager.ConfigManagers.DataManager;

public class Pets {
	
	public Pets() {
		
	}
	
	public void createPet(Player p, EntityType type) {
		Entity entity = (Entity) p.getWorld().spawnEntity(p.getLocation(), type);
		entity.setCustomName(p.getName());
		entity.setCustomNameVisible(true);
		
		InventoryClick.pets.put(p.getName(), entity);
	}
	
	public void removePet(Player p) {
		if (InventoryClick.pets.containsKey(p.getName())) {
			InventoryClick.pets.get(p.getName()).remove();
		}
	}
	
	public void sittOnPet(Boolean bool, Player p) {
		DataManager dm = new DataManager();
		Entity entity = InventoryClick.pets.get(p.getName());
		if (bool == true) {
			entity.setPassenger(p);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".ride", "true");
			dm.saveData();
		} else {
			entity.eject();
			dm.getData().set("Data." + p.getName().toLowerCase() + ".ride", "false");
			dm.saveData();
		}
	}
}
