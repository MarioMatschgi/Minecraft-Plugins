package at.mario.gravity.listener;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import at.mario.gravity.Main;
import at.mario.gravity.gamestates.EndingState;
import at.mario.gravity.gamestates.IngameState;
import at.mario.gravity.gamestates.LobbyState;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.utils.PlayerUtil;

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
					if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState || 
							Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
							}
						}
					} else if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState) {
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								if ((p.getHealth() - e.getDamage()) <= 0) {
									e.setCancelled(true);
									
									
									HashMap<Player, Integer> ArenaPlayerREALFailsMap = IngameState.ArenaPlayerREALFails.get(key);
									ArenaPlayerREALFailsMap.put(p, ArenaPlayerREALFailsMap.get(p) + 1);
									IngameState.ArenaPlayerREALFails.put(key, ArenaPlayerREALFailsMap);
									PlayerUtil.restartStage(p, key);
								}
							}
						}
					}
				}
			}
		}
	}
}
