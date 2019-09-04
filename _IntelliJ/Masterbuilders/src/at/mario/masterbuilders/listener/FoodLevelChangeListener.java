package at.mario.masterbuilders.listener;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import at.mario.masterbuilders.Main;
import at.mario.masterbuilders.manager.ConfigManagers.DataManager;

public class FoodLevelChangeListener implements Listener {
	
	@EventHandler
	public void foodLevelChange(FoodLevelChangeEvent e) {
		DataManager dm = new DataManager();
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
			if (configSection != null) {
				for (String key : configSection.getKeys(false)) {
					if (Main.ArenaPlayer.containsKey(key)) {
						if (Main.ArenaPlayer.get(key).contains(p)) {
							// ist Spieler
							e.setCancelled(true);
							break;
						}
					} else if (Main.SpectateArenaPlayer.containsKey(key)) {
						if (Main.SpectateArenaPlayer.get(key).contains(p)) {
							// ist Spectator
							e.setCancelled(true);
							break;
						}
					}
				}
			}
		}
	}
}
