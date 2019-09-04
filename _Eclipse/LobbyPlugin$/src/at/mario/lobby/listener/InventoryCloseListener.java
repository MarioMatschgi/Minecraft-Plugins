package at.mario.lobby.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class InventoryCloseListener implements Listener {
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		Inventory inv = e.getInventory();
		
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		
		if (inv.getName() == mm.getMessages().getString("Messages.gui.teleportSort.title")) {
			if (dm.getData().contains("Data.temp.index2")) {
				for (int i = 0; i < dm.getData().getInt("Data.temp.index2"); i++) {
					if (i > 8) {
						p.getInventory().setItem(i, null);
					}
				}
				
			}
		} else if (inv.getName() == mm.getMessages().getString("Messages.gui.particle.title") || inv.getName() == mm.getMessages().getString("Messages.gui.pet.title") || inv.getName() == 
				mm.getMessages().getString("Messages.gui.armor.title") || inv.getName() == mm.getMessages().getString("Messages.gui.gadgets.title")) {
			
			Inventory pInv = p.getInventory();
			
			for (int i = 0; i < 27; i++) {
				pInv.setItem(i + 9, null);
			}
		}
		
		dm.getData().set("Data.temp.index2", 0);
		dm.getData();
	}
}
