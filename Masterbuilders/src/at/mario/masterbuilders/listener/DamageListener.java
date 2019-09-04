package at.mario.masterbuilders.listener;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import at.mario.masterbuilders.Main;
import at.mario.masterbuilders.gamestates.EndingState;
import at.mario.masterbuilders.gamestates.IngameState;
import at.mario.masterbuilders.gamestates.LobbyState;
import at.mario.masterbuilders.manager.ConfigManagers.DataManager;

public class DamageListener implements Listener {
	
	@EventHandler
	public void onDmgByEnt(EntityDamageByEntityEvent e) {
		DataManager dm = new DataManager();
		
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		if (configSection != null) {
			for (String key : configSection.getKeys(false)) {
				if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState || Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState || 
						Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
					if (e.getEntity() instanceof Player) {
						Player p = (Player) e.getEntity();
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDmgByBlock(EntityDamageByBlockEvent e) {
		DataManager dm = new DataManager();
		
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
			if (configSection != null) {
				for (String key : configSection.getKeys(false)) {
					if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState || Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState || 
							Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDmg(EntityDamageEvent e) {
		DataManager dm = new DataManager();
		
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
			if (configSection != null) {
				for (String key : configSection.getKeys(false)) {
					if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState || Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState || 
							Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
}
