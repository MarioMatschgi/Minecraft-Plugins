package at.mario.piratecraft.listener;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.gamestates.EndingState;
import at.mario.piratecraft.gamestates.IngameState;
import at.mario.piratecraft.gamestates.LobbyState;
import at.mario.piratecraft.manager.ConfigManagers.DataManager;

public class BlockPlaceListener implements Listener {
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		DataManager dm = new DataManager();

		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		if (configSection != null) {
			for (String key : configSection.getKeys(false)) {
				if (Main.ArenaPlayer.containsKey(key)) {
					if (Main.ArenaPlayer.get(key).contains(e.getPlayer())) {
						if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState || Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState || 
								Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
					
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
}
