package at.mario.hidenseek.inventories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import at.mario.hidenseek.Main;

public class EmtyInventory {

	public EmtyInventory() { }
	
	private static EmtyInventory instance = new EmtyInventory();
	
	public static EmtyInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 54, " ");
		
		p.openInventory(inv);
	}
}
