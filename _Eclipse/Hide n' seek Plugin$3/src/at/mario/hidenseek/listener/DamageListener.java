package at.mario.hidenseek.listener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;

public class DamageListener implements Listener {
	
	@EventHandler
	public void onDmgByEnt(EntityDamageByEntityEvent e) {
		DataManager dm = new DataManager();
		
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
			for (String key : configSection.getKeys(false)) {
				Bukkit.broadcastMessage("§cKEY: §a"+key);
				if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
					Bukkit.broadcastMessage("§cKEY2: §a"+key);
					if (Main.ArenaPlayer.containsKey(key)) {
						Bukkit.broadcastMessage("§cKEY3: §a"+key);
						if (Main.ArenaPlayer.get(key).contains(p)) {
							Bukkit.broadcastMessage("§cKEY4: §a"+key);
							e.setCancelled(true);
							return;
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
			for (String key : configSection.getKeys(false)) {
				if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
					if (Main.ArenaPlayer.containsKey(key)) {
						if (Main.ArenaPlayer.get(key).contains(p)) {
							e.setCancelled(true);
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
			for (String key : configSection.getKeys(false)) {
				if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
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
