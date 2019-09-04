package at.mario.hidenseek.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;

public class PlayerInteractListener implements Listener {
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		DataManager dm = new DataManager();
		// MessagesManager mm = new MessagesManager();
		
		Block block = e.getClickedBlock();
		Player p = e.getPlayer();

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
				ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
				for (String key : configSection.getKeys(false)) {
					if (dm.getData().contains("Data.arenas." + key + ".signs")) {
						ArrayList<Location> signlist = (ArrayList<Location>) dm.getData().get("Data.arenas." + key + ".signs");
						if (signlist.contains(block.getLocation())) {
							if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
								p.sendMessage("Ja: Arena: " + key);
								
								List<Player> players = new ArrayList<Player>();
								if (Main.ArenaPlayer.containsKey(key)) {
									players = Main.ArenaPlayer.get(key);
								}
								players.add(p);
								Main.ArenaPlayer.put(key, players);
								SignChangeListener.updateSigns(key);
							
							
							
								int currentPlayers = Main.ArenaPlayer.get(key).size();
							
							// Main.getInstance().getGameStateManager().setGameState(GameState.LOBBY_STATE, key);
							
							

								LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(key);
								Main.sendToArenaOnly(key, "§6" + p.getName() + " §ahat das Spiel: " + key + " betreten. §7(" + currentPlayers + "/" + LobbyState.maxPlayers.get(key) + ")");
								/*for (Entry<String, Player> entry : Main.ArenaPlayer.entrySet()) {
							        if (Objects.equals(key, entry.getKey())) {
							        	Player player = entry.getValue();
										player.sendMessage("§6" + p.getName() + " §ahat das Spiel: " + key + " betreten. §7(" + currentPlayers + "/" + LobbyState.MAX_PLAYERS + ")");
							        }
							    }*/
								
								if (currentPlayers >= LobbyState.minPlayers.get(key)) {
									// LobbyCountdown starten...
									if (!lobbyState.getLobbycountdown().isRunning(key)) {
										if (lobbyState.getLobbycountdown().isIdling(key)) {
											lobbyState.getLobbycountdown().cancelIdleing(key);
										}
										lobbyState.getLobbycountdown().run(key);
									}
								} else {
									lobbyState.getLobbycountdown().idle(key);
								}
								Location loc = (Location) dm.getData().get("Data.arenas." + key + ".lobbyspawn");
								p.teleport(loc);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e) {
		
	}
}
