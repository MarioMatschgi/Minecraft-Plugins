package at.mario.pets.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import at.mario.pets.manager.ConfigManagers.DataManager;

public class MoveListener implements Listener {
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		DataManager dm = new DataManager();
		
		Player p = e.getPlayer();
		//Entity entity = InventoryClick.pets.get(p.getName());
		if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".ride") == "true") {
		}
	}
}
