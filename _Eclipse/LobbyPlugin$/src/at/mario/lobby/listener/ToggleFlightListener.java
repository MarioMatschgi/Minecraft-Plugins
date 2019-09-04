package at.mario.lobby.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.DataManager;

public class ToggleFlightListener implements Listener {
	
	@EventHandler
	public void onToggleFlight(PlayerToggleFlightEvent e) {
		DataManager dm = new DataManager();
		
		Player p = e.getPlayer();
		
		if (Main.isinLobby(p.getLocation())) {
			if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".doubleJump")) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".doubleJump").equalsIgnoreCase("enabled")) {
						if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".fly")) {
							if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".fly") == false) {
								if (p.getGameMode() == GameMode.CREATIVE) {
									return;
								}
								e.setCancelled(true);
								p.setAllowFlight(false);
								p.setFlying(false);
								p.setVelocity(p.getLocation().getDirection().multiply(1.5).setY(1));
							}
						} else {
							if (p.getGameMode() == GameMode.CREATIVE) {
								return;
							}
							e.setCancelled(true);
							p.setAllowFlight(false);
							p.setFlying(false);
							p.setVelocity(p.getLocation().getDirection().multiply(1.5).setY(1));
						}
					
				}
			}
		}
	}
}
