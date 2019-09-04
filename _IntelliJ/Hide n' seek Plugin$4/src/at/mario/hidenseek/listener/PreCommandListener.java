package at.mario.hidenseek.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.scoreboards.LobbyScoreboard;

public class PreCommandListener implements Listener {

	@EventHandler
	public void preCommand(PlayerCommandPreprocessEvent e) {
		DataManager dm = new DataManager();
		
		Player p = e.getPlayer();

		for (String key : Main.ArenaPlayer.keySet()) {
			List<Player> players = Main.ArenaPlayer.get(key);
			
			if (players.contains(p)) {
				if (e.getMessage().equalsIgnoreCase("/l") || e.getMessage().equalsIgnoreCase("/lobby") || e.getMessage().equalsIgnoreCase("/lb") || e.getMessage().equalsIgnoreCase("/spawn") || e.getMessage().equalsIgnoreCase("/hub")) {
					String arena = key;

					int currentPlayers = Main.ArenaPlayer.get(key).size();
					
					if (Main.getInstance().getGameStateManager().getCurrentGameState(arena) instanceof LobbyState) {
						LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(arena);
						
						if (currentPlayers < LobbyState.minPlayers.get(arena)) {
							if (currentPlayers == 0) {
								lobbyState.getLobbycountdown().cancelIdleing(arena);
							} else {
								// LobbyCountdown stoppen...
								if (lobbyState.getLobbycountdown().isRunning(arena)) {
									lobbyState.getLobbycountdown().cancel(arena);
									if (!lobbyState.getLobbycountdown().isIdling(arena)) {
										lobbyState.getLobbycountdown().idle(arena);
									}
								}
							}
						}
					}
					players.remove(p);
					Main.ArenaPlayer.put(key, players);
					SignChangeListener.updateSigns(arena);
				
					LobbyScoreboard.removeScoreboard(p);
				
					Location loc = (Location) dm.getData().get("Data.mainlobbyspawn");
					p.teleport(loc);
				
					e.setCancelled(true);
				} else if (e.getMessage().equalsIgnoreCase("/start")) {
					if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
						LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(key);
						
						if (lobbyState.getLobbycountdown().isRunning(key)) {
							if (!(lobbyState.getLobbycountdown().getSeconds(key) <= 3) ) {
								lobbyState.getLobbycountdown().setSeconds(3, key);
								e.setCancelled(true);
							} else {
								e.setCancelled(false);
							}
						} else {
							e.setCancelled(false);
						}
					} else {
						e.setCancelled(false);
					}
				} else {
					e.setCancelled(false);
				}
			} else {
				e.setCancelled(false);
			}
		}
	}
}
