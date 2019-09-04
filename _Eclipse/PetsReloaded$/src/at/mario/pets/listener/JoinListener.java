package at.mario.pets.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import at.mario.pets.manager.ConfigManagers.DataManager;

public class JoinListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		DataManager dm = new DataManager();
		
		dm.getData().set("Data." + e.getPlayer().getName().toLowerCase() + ".pet", "null");
		dm.getData().set("Data." + e.getPlayer().getName().toLowerCase() + ".ride", "false");
		dm.saveData();
	}
}
