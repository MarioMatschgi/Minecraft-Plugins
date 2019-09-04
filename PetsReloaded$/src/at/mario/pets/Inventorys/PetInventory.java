package at.mario.pets.Inventorys;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.pets.Main;

public class PetInventory {

	public PetInventory() { }
	
	private static PetInventory instance = new PetInventory();
	
	public static PetInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 45, "PetInv");
		// Wolf
		ItemStack wolf = new ItemStack(Material.BONE);
		ItemMeta wolfMeta = wolf.getItemMeta();
		wolfMeta.setDisplayName("Wolf");
		wolf.setItemMeta(wolfMeta);
		// Schaf
		ItemStack sheep = new ItemStack(Material.WOOL);
		ItemMeta sheepMeta = sheep.getItemMeta();
		sheepMeta.setDisplayName("Schaf");
		sheep.setItemMeta(sheepMeta);
		// Huhn
		ItemStack chicken = new ItemStack(Material.SEEDS);
		ItemMeta chickenMeta = chicken.getItemMeta();
		chickenMeta.setDisplayName("Huhn");
		chicken.setItemMeta(chickenMeta);
		// Pferd
		ItemStack horse = new ItemStack(Material.SADDLE);
		ItemMeta horseMeta = horse.getItemMeta();
		horseMeta.setDisplayName("Pferd");
		horse.setItemMeta(horseMeta);
		// Schwein
		ItemStack pig = new ItemStack(Material.CARROT_STICK);
		ItemMeta pigMeta = pig.getItemMeta();
		pigMeta.setDisplayName("Schwein");
		pig.setItemMeta(pigMeta);
		// Kuh
		ItemStack cow = new ItemStack(Material.MILK_BUCKET);
		ItemMeta cowMeta = cow.getItemMeta();
		cowMeta.setDisplayName("Kuh");
		cow.setItemMeta(cowMeta);
		// Pilzkuh
		ItemStack mooshroom = new ItemStack(Material.RED_MUSHROOM);
		ItemMeta mooshroomMeta = mooshroom.getItemMeta();
		mooshroomMeta.setDisplayName("Pilzkuh");
		mooshroom.setItemMeta(mooshroomMeta);
		// Katze
		ItemStack cat = new ItemStack(Material.RAW_FISH);
		ItemMeta catMeta = cat.getItemMeta();
		catMeta.setDisplayName("Katze");
		cat.setItemMeta(catMeta);
		// Hase
		ItemStack rabbit = new ItemStack(Material.RABBIT_HIDE);
		ItemMeta rabbitMeta = rabbit.getItemMeta();
		rabbitMeta.setDisplayName("Hase");
		rabbit.setItemMeta(rabbitMeta);
		// Dorfbewohner
		ItemStack villager = new ItemStack(Material.WOOD_DOOR);
		ItemMeta villagerMeta = villager.getItemMeta();
		villagerMeta.setDisplayName("Dorfbewohner");
		villager.setItemMeta(villagerMeta);
		
		// Sitting
		ItemStack sitting = new ItemStack(Material.SADDLE);
		ItemMeta sittingMeta = sitting.getItemMeta();
		sittingMeta.setDisplayName("Reiten");
		sitting.setItemMeta(sittingMeta);
		
		inv.setItem(1, wolf);
		inv.setItem(3, sheep);
		inv.setItem(5, chicken);
		inv.setItem(7, horse);
		
		inv.setItem(10, pig);
		inv.setItem(12, cow);
		inv.setItem(14, mooshroom);
		inv.setItem(16, cat);
		
		inv.setItem(19, rabbit);
		inv.setItem(21, villager);
		

		inv.setItem(39, sitting);
		
		p.openInventory(inv);
	}
}
