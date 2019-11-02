package at.mario.utilreloaded.listener;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import at.mario.utilreloaded.manager.ConfigManagers.DataManager;
import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class InventoryCloseListener implements Listener {
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		Inventory inv = e.getInventory();
		InventoryView view = e.getView();
		
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		
		if (view.getTitle().equals(mm.getMessages().getString("Messages.ecTitle"))) {
			ItemStack[] content = inv.getContents();
			ArrayList<ItemStack> data = new ArrayList<ItemStack>();
			
			for (int i = 0; i < content.length; i++) {
				data.add(content[i]);
			}
			
			dm.getData().set("Data." + p.getName() + ".privateenderchest", data);
			dm.saveData();

			p.sendMessage(mm.getMessages().getString("Messages.command.enderchest.saved").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
		}
	}
}
