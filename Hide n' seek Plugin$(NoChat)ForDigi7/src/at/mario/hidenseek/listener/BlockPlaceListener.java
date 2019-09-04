package at.mario.hidenseek.listener;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.EndingState;
import at.mario.hidenseek.gamestates.IngameState;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;

public class BlockPlaceListener implements Listener {
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		DataManager dm = new DataManager();

		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		if (configSection != null) {
			for (String key : configSection.getKeys(false)) {
				if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState || Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState || 
						Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
					
					if (Main.ArenaPlayer.containsKey(key)) {
						if (Main.ArenaPlayer.get(key).contains(e.getPlayer())) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
}
