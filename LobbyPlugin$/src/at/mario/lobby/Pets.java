package at.mario.lobby;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;

import at.mario.lobby.entities.RideableChicken;
import at.mario.lobby.entities.RideableCow;
import at.mario.lobby.entities.RideableHorse;
import at.mario.lobby.entities.RideableMushroomCow;
import at.mario.lobby.entities.RideableOcelot;
import at.mario.lobby.entities.RideablePig;
import at.mario.lobby.entities.RideableRabbit;
import at.mario.lobby.entities.RideableSheep;
import at.mario.lobby.entities.RideableSilverfish;
import at.mario.lobby.entities.RideableVillager;
import at.mario.lobby.entities.RideableWolf;
import at.mario.lobby.listener.InventoryClick;
import at.mario.lobby.manager.ConfigManagers.DataManager;

public class Pets {
	public HashMap<String, Integer> taskIDs = new HashMap<String, Integer>();
	
	
	public Pets() {
		
	}
	
	public void createPet(Player p, String type) {
		DataManager dm = new DataManager();
		
		if (type == "WOLF") {
			Wolf wolf = RideableWolf.spawn(p.getLocation());
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".baby") == "true") {
				wolf.setBaby();
			} else {
				wolf.setAdult();
			}
			
			InventoryClick.pets.put(p.getName(), wolf);
		} else if (type == "SHEEP") {
			Sheep sheep = RideableSheep.spawn(p.getLocation());
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".baby") == "true") {
				sheep.setBaby();
			} else {
				sheep.setAdult();
			}
		
			InventoryClick.pets.put(p.getName(), sheep);
		} else if (type == "CHICKEN") {
			Chicken chicken = RideableChicken.spawn(p.getLocation());
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".baby") == "true") {
				chicken.setBaby();
			} else {
				chicken.setAdult();
			}
		
			InventoryClick.pets.put(p.getName(), chicken);
		} else if (type == "HORSE") {
			Horse horse = RideableHorse.spawn(p.getLocation());
			horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".baby") == "true") {
				horse.setBaby();
			} else {
				horse.setAdult();
			}
		
			InventoryClick.pets.put(p.getName(), horse);
		} else if (type == "PIG") {
			Pig pig = RideablePig.spawn(p.getLocation());
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".baby") == "true") {
				pig.setBaby();
			} else {
				pig.setAdult();
			}
		
			InventoryClick.pets.put(p.getName(), pig);
		} else if (type == "COW") {
			Cow cow = RideableCow.spawn(p.getLocation());
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".baby") == "true") {
				cow.setBaby();
			} else {
				cow.setAdult();
			}
		
			InventoryClick.pets.put(p.getName(), cow);
		} else if (type == "MUSHROOM_COW") {
			MushroomCow mushroomCow = RideableMushroomCow.spawn(p.getLocation());
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".baby") == "true") {
				mushroomCow.setBaby();
			} else {
				mushroomCow.setAdult();
			}
		
			InventoryClick.pets.put(p.getName(), mushroomCow);
		} else if (type == "OCELOT") {
			Ocelot ocelot = RideableOcelot.spawn(p.getLocation());
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".baby") == "true") {
				ocelot.setBaby();
			} else {
				ocelot.setAdult();
			}
		
			InventoryClick.pets.put(p.getName(), ocelot);
		} else if (type == "RABBIT") {
			Rabbit rabbit = RideableRabbit.spawn(p.getLocation());
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".baby") == "true") {
				rabbit.setBaby();
			} else {
				rabbit.setAdult();
			}
		
			InventoryClick.pets.put(p.getName(), rabbit);
		} else if (type == "VILLAGER") {
			Villager villager = RideableVillager.spawn(p.getLocation());
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".baby") == "true") {
				villager.setBaby();
			} else {
				villager.setAdult();
			}
		
			InventoryClick.pets.put(p.getName(), villager);
		} else if (type == "SQUID") {
			Squid squid = (Squid) p.getWorld().spawnEntity(p.getLocation(), EntityType.SQUID);
		
			InventoryClick.pets.put(p.getName(), squid);
		} else if (type == "SILVERFISH") {
			Silverfish silverfish = RideableSilverfish.spawn(p.getLocation());
		
			InventoryClick.pets.put(p.getName(), silverfish);
		} else {
			
		}

		Entity entity = InventoryClick.pets.get(p.getName());
		if (entity != null) {
	        float Speed = 2.0f;
	        taskIDs.put(p.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
	        	@SuppressWarnings("deprecation")
	        	public void run() {
	            	double distance = entity.getLocation().distance(p.getLocation());
	            	if (distance < 11) {
	            		float speed = Speed;
	            		if (distance < 3) {
	            			speed = 0;
	            		}
	            		if (entity instanceof Squid) {
	            			try {
		            			Bukkit.getScheduler().cancelTask(taskIDs.get(p.getName()));
	            			} catch (Exception exe) {	            			}
	            		} else {
		            		((CraftCreature) entity).getHandle().getNavigation().a(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), speed);
	            		}
	            	} else {
	            		if (p.isOnGround()) {
	            			entity.teleport(p);
	            		}
	            	}
	            }
	        }, 0, 1));
	        if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".petName")) {
				entity.setCustomName(dm.getData().getString("Data." + p.getName().toLowerCase() + ".petName"));
			} else {
				entity.setCustomName(p.getName());
			}
			entity.setCustomNameVisible(true);
		}
	}
	
	public void removePet(Player p) {
		if (InventoryClick.pets != null) {
			if (InventoryClick.pets.containsKey(p.getName())) {
				InventoryClick.pets.get(p.getName()).remove();
			}
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
	
	public void renamePet(Player p, String name) {
		Entity entity = InventoryClick.pets.get(p.getName());
		if (entity != null) {
			entity.setCustomName(name);
		}
	}
}
