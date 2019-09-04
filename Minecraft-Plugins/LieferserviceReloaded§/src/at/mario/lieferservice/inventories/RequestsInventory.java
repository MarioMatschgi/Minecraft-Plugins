package at.mario.lieferservice.inventories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lieferservice.Main;
import at.mario.lieferservice.Request;
import at.mario.lieferservice.manager.ConfigManagers.MessagesManager;

public class RequestsInventory {

	public RequestsInventory() { }
	
	private static RequestsInventory instance = new RequestsInventory();
	
	public static RequestsInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		MessagesManager mm = new MessagesManager();
		//DataManager dm = new DataManager();
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 6 * 9, mm.getMessages().getString("Messages.gui.requestsInventory.title"));
		
		
		for (int i = 0; i < Main.requests.size(); i++) {
			Request request = Main.requests.get(i);
			
			ItemStack item = request.requestedItem.clone();
			item.setAmount(1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(request.requester.getName() + " #" + request.requestNumber);
			item.setItemMeta(meta);
			
			inv.setItem(i, item);
		}
		
		p.openInventory(inv);
	}
}
