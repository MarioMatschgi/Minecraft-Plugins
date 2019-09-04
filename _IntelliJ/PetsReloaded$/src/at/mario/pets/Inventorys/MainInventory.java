package at.mario.pets.Inventorys;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.pets.Main;

public class MainInventory {
	
	private MainInventory() { }
	
	private static MainInventory instance = new MainInventory();
	
	public static MainInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, "MainInv");
		
		ItemStack pets = new ItemStack(Material.BONE);
		ItemMeta petsMeta = pets.getItemMeta();
		petsMeta.setDisplayName("Haustiere");
		pets.setItemMeta(petsMeta);
		
		inv.setItem(13, pets);
		
		p.openInventory(inv);
	}
}