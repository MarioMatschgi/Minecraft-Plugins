package at.mario.masterbuilders.listener;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import at.mario.masterbuilders.Main;
import at.mario.masterbuilders.gamestates.EndingState;
import at.mario.masterbuilders.gamestates.IngameState;
import at.mario.masterbuilders.gamestates.LobbyState;
import at.mario.masterbuilders.manager.ConfigManagers.DataManager;

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
