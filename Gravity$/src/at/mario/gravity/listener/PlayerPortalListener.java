package at.mario.gravity.listener;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import at.mario.gravity.Main;
import at.mario.gravity.gamestates.IngameState;
import at.mario.gravity.manager.ConfigManagers.DataManager;

public class PlayerPortalListener implements Listener {
	
	@EventHandler
	public void onTeleport(PlayerPortalEvent e) {
		DataManager dm = new DataManager();
		
		Player p = e.getPlayer();
		

		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		if (configSection != null) {
			for (String key : configSection.getKeys(false)) {
				if (Main.ArenaPlayer.containsKey(key)) {
					if (Main.ArenaPlayer.get(key).contains(p)) {
						if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState) {
							if (p.getLocation().getBlock().getType().equals(Material.PORTAL)) {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
}
