package at.mario.hidenseek.listener;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;

public class ItemDropListener implements Listener {
	
	@EventHandler
	public void itemDrop(PlayerDropItemEvent e) {
		MessagesManager mm = new MessagesManager();
		Item itm = e.getItemDrop();
		
		
		if (itm == null || itm.getItemStack() == null || !itm.getItemStack().hasItemMeta()) { 
			return;
		}
		ItemStack item = itm.getItemStack();
		
		if (item.getItemMeta().getDisplayName() == mm.getMessages().get("Messages.lobbyitems.leaveItem.name") || 
				item.getItemMeta().getDisplayName() == mm.getMessages().get("Messages.lobbyitems.shopItem.name") || 
				item.getItemMeta().getDisplayName() == mm.getMessages().get("Messages.lobbyitems.startItem.name") || 
				item.getItemMeta().getDisplayName() == mm.getMessages().get("Messages.lobbyitems.passItem.name") || 
				item.getItemMeta().getDisplayName() == mm.getMessages().get("Messages.ingameitems.seekerStickItem.name")) {

			e.setCancelled(true);
		}
	}
}
