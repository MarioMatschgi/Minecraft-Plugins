package at.mario.pets.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import at.mario.pets.Pets;
import at.mario.pets.manager.ConfigManagers.DataManager;

public class SneakListener implements Listener {
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		Player p = (Player) e.getPlayer();
		Pets pets = new Pets();
		DataManager dm = new DataManager();

		if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".ride") == "true") {
			e.setCancelled(true);
			pets.sittOnPet(false, p);
		}
	}
}
