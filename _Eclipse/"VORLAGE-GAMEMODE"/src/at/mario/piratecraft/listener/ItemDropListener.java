package at.mario.piratecraft.listener;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import at.mario.piratecraft.manager.ConfigManagers.MessagesManager;

public class ItemDropListener implements Listener {
	
	@EventHandler
	public void itemDrop(PlayerDropItemEvent e) {
		MessagesManager mm = new MessagesManager();
		Item item = e.getItemDrop();
		
		if (item == null || item.getItemStack() == null) {
			return;
		} else if (!item.getItemStack().hasItemMeta()) {
			return;
		}
		
		if (item.getItemStack().getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.lobbyitems.leaveItem.name")) || 
				item.getItemStack().getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.lobbyitems.shopItem.name")) || 
				item.getItemStack().getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.lobbyitems.startItem.name"))) {

			e.setCancelled(true);
		}
	}
}
