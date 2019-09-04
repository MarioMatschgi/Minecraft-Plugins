package at.mario.pets.listener;

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.mario.pets.Pets;
import at.mario.pets.Inventorys.PetInventory;
import at.mario.pets.manager.ConfigManagers.DataManager;

public class InventoryClick implements Listener{
	
	//private Plugin plugin = Main.getPlugin();
	public static HashMap<String, Entity> pets = new HashMap<String, Entity>();
	
	@EventHandler
	public void InvenClick(InventoryClickEvent e) {
		DataManager dm = new DataManager();
		Player p = (Player) e.getWhoClicked();
		
		//ClickType click = e.getClick();
		Inventory open = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		
		if (open == null) {
			return;
		}

		
		// Wenn MainInv
		// -------------------------------------------
		if (open.getName() == "MainInv") {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			// Hier wenn Item geclickt
			if (item.getItemMeta().getDisplayName().equals("Haustiere")) {
				PetInventory.getInstance().newInventory(p);
			}
		}
		
		// Wenn PetInv
		// -------------------------------------------
		if (open.getName() == "PetInv") {
			Pets pets = new Pets();
			
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			
			// Hier wenn Wolf geklickt
			if (item.getItemMeta().getDisplayName().equals("Wolf")) {
				p.closeInventory();

				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "wolf") {
					pets.removePet(p);
				} else {
					pets.removePet(p);
					pets.createPet(p, EntityType.WOLF);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "wolf");
					dm.saveData();
				}
			}
			
			// Hier wenn Schaf geklickt
			if (item.getItemMeta().getDisplayName().equals("Schaf")) {
				p.closeInventory();
				
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "sheep") {
					pets.removePet(p);
				} else {
					pets.removePet(p);
					pets.createPet(p, EntityType.SHEEP);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "sheep");
					dm.saveData();
				}
			}
			// Hier wenn Huhn geklickt
			if (item.getItemMeta().getDisplayName().equals("Huhn")) {
				p.closeInventory();

				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "chicken") {
					pets.removePet(p);
				} else {
					pets.removePet(p);
					pets.createPet(p, EntityType.CHICKEN);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "chicken");
					dm.saveData();
				}
			}
			// Hier wenn Pferd geklickt
			if (item.getItemMeta().getDisplayName().equals("Pferd")) {
				p.closeInventory();

				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "horse") {
					pets.removePet(p);
				} else {
					pets.removePet(p);
					pets.createPet(p, EntityType.HORSE);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "horse");
					dm.saveData();
				}
			}
			// Hier wenn Schwein geklickt
			if (item.getItemMeta().getDisplayName().equals("Schwein")) {
				p.closeInventory();

				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "pig") {
					pets.removePet(p);
				} else {
					pets.removePet(p);
					pets.createPet(p, EntityType.PIG);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "pig");
					dm.saveData();
				}
			}
			// Hier wenn Kuh geklickt
			if (item.getItemMeta().getDisplayName().equals("Kuh")) {
				p.closeInventory();

				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "cow") {
					pets.removePet(p);
				} else {
					pets.removePet(p);
					pets.createPet(p, EntityType.COW);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "cow");
					dm.saveData();
				}
			}
			// Hier wenn Pilzkuh geklickt
			if (item.getItemMeta().getDisplayName().equals("Pilzkuh")) {
				p.closeInventory();

				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "mooshroom") {
					pets.removePet(p);
				} else {
					pets.removePet(p);
					pets.createPet(p, EntityType.MUSHROOM_COW);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "mooshroom");
					dm.saveData();
				}
			}
			// Hier wenn Katze geklickt
			if (item.getItemMeta().getDisplayName().equals("Katze")) {
				p.closeInventory();

				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "ocelot") {
					pets.removePet(p);
				} else {
					pets.removePet(p);
					pets.createPet(p, EntityType.OCELOT);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "ocelot");
					dm.saveData();
				}
			}
			// Hier wenn Hase geklickt
			if (item.getItemMeta().getDisplayName().equals("Hase")) {
				p.closeInventory();

				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "rabbit") {
					pets.removePet(p);
				} else {
					pets.removePet(p);
					pets.createPet(p, EntityType.RABBIT);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "rabbit");
					dm.saveData();
				}
			}
			// Hier wenn Dorfbewohner geklickt
			if (item.getItemMeta().getDisplayName().equals("Dorfbewohner")) {
				p.closeInventory();

				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "villager") {
					pets.removePet(p);
				} else {
					pets.removePet(p);
					pets.createPet(p, EntityType.VILLAGER);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "villager");
					dm.saveData();
				}
			}
			

			// Hier wenn Reiten geklickt
			if (item.getItemMeta().getDisplayName().equals("Reiten")) {
				p.closeInventory();
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".ride") == "false") {
					pets.sittOnPet(true, p);
				} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".ride") == "true") {
					pets.sittOnPet(false, p);
				}
			}
		}
	}
}
