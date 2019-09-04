package at.mario.lieferservice.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.mario.lieferservice.Main;
import at.mario.lieferservice.Request;
import at.mario.lieferservice.inventories.OptionsInventory;
import at.mario.lieferservice.inventories.RequestsInventory;
import at.mario.lieferservice.manager.ConfigManagers.DataManager;
import at.mario.lieferservice.manager.ConfigManagers.MessagesManager;

public class InventoryClickListener implements Listener {
	
	@SuppressWarnings({ "unused" })
	@EventHandler
	public void InvenClick(InventoryClickEvent e) {
		MessagesManager mm = new MessagesManager();
		
		DataManager dm = new DataManager();
		Player p = (Player) e.getWhoClicked();
		
		//ClickType click = e.getClick();
		Inventory open = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		ItemStack itemClick = e.getCursor();
		int slot = e.getSlot();
		
		
		if (open == null) {
			return;
		}
		
		if (item == null || !item.hasItemMeta()) {
			return;
		}
		
		if (open.getName().equals(mm.getMessages().getString("Messages.gui.requestsInventory.title"))) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			
			Request request = Main.requests.get(slot);
			OptionsInventory.getInstance().newInventory(p, request);
		}
		else if (open.getName().equals(mm.getMessages().getString("Messages.gui.optionsInventory.title"))) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			
			int requestID = Main.parseInt(open.getContents()[0].getItemMeta().getDisplayName().substring(4));
			Request request = Main.requests.get(requestID - 1);
			ItemStack requestedItem = request.requestedItem;

			String stacksStr = "";
			int amount = requestedItem.getAmount();
			if (amount % requestedItem.getMaxStackSize() == 0) {
				stacksStr = " stacks";
				
				amount /= requestedItem.getMaxStackSize();
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.optionsInventory.deny"))) {
				Main.requests.remove(request);
				Request.updateIds();
				dm.getData().set("Data.requests", Main.requests);
				dm.saveData();
				
				((Player) request.requester).sendMessage(mm.getMessages().getString("Messages.command.requests.other.deny").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
						replace("%amount%", amount+"").replace("%stacks%", stacksStr).replace("%item%", requestedItem.getItemMeta().getDisplayName()));
				p.sendMessage(mm.getMessages().getString("Messages.command.requests.self.deny").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
						replace("%id%", requestID+"").replace("%player%", request.requester.getName()));
				
				p.closeInventory();
			}
			else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.optionsInventory.finish"))) {
				Main.requests.remove(request);
				Request.updateIds();
				dm.getData().set("Data.requests", Main.requests);
				dm.saveData();
				
				((Player) request.requester).sendMessage(mm.getMessages().getString("Messages.command.requests.other.finished").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
						replace("%amount%", amount+"").replace("%stacks%", stacksStr).replace("%item%", requestedItem.getItemMeta().getDisplayName()));
				p.sendMessage(mm.getMessages().getString("Messages.command.requests.self.finished").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
						replace("%id%", requestID+"").replace("%player%", request.requester.getName()));
				
				p.closeInventory();
			}
			else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.optionsInventory.back"))) {
				p.closeInventory();
				RequestsInventory.getInstance().newInventory(p);
			}
		}
	}
}
