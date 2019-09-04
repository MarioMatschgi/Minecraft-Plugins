package at.mario.lobby.listener;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class ItemDropListener implements Listener {
	
	@EventHandler
	public void itemDrop(PlayerDropItemEvent e) {
		MessagesManager mm = new MessagesManager();
		Item item = e.getItemDrop();
		
		if (item == null) {
			return;
		}
		
		if (item.getItemStack().getItemMeta().getDisplayName() == mm.getMessages().get("Messages.inventory.teleporter") || item.getItemStack().getItemMeta().getDisplayName() == mm.getMessages().get("Messages.inventory.visibility") 
				|| item.getItemStack().getItemMeta().getDisplayName() == mm.getMessages().get("Messages.inventory.profiles") || item.getItemStack().getItemMeta().getDisplayName() == 
						mm.getMessages().getString("Messages.inventory.silenthub")) {
			
			e.setCancelled(true);
		}
	}
}
