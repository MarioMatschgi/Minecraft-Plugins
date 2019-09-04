package at.mario.utilreloaded.inventories;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.mario.utilreloaded.Main;
import at.mario.utilreloaded.manager.ConfigManagers.DataManager;
import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class EnderchestInventory {

	public EnderchestInventory() { }
	
	private static EnderchestInventory instance = new EnderchestInventory();
	
	public static EnderchestInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		p.updateInventory();
		
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 12 * 9, mm.getMessages().getString("Messages.ecTitle"));

		ItemStack[] content = inv.getContents();
		ArrayList<ItemStack> data = new ArrayList<ItemStack>();
		
		for (int i = 0; i < content.length; i++) {
			data.add(content[i]);
		}
		
		dm.getData().set("Data." + p.getName() + ".privateenderchest", data);
		dm.saveData();
	}
}
