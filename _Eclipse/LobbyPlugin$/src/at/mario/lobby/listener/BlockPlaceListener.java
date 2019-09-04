package at.mario.lobby.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class BlockPlaceListener implements Listener {
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		Player p = e.getPlayer();
		Block block = e.getBlock();
		
		
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".build") == false) {
			if (Main.isinLobby(block.getLocation())) {
				e.setCancelled(true);
			}
		}
		if (p.getItemInHand().getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.profiles")) {
			e.setCancelled(true);
		}
		if (p.getItemInHand().getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.silenthub")) {
			e.setCancelled(true);
		}
	}
}
