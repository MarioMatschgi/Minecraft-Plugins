package at.mario.hidenseek.listener;

import java.util.ArrayList;

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
import at.mario.hidenseek.gamestates.GameState;
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
							p.sendMessage("Ja: Arena: " + key);
							
							Main.ArenaPlayer.put(key, p);
							SignChangeListener.updateSigns(key);
							
							
							
							int currentPlayers = 0;
							for (String key2 : Main.ArenaPlayer.keySet()) {
								if (key2 == key) {
									currentPlayers++;
								}
							}
							
							Main.getInstance().getGameStateManager().setGameState(GameState.LOBBY_STATE);
							
							

							if (Main.getInstance().getGameStateManager().getCurrentGameState() instanceof LobbyState) {
								LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState();
								
								Main.sendToArenaOnly(key, "§6" + p.getName() + " §ahat das Spiel: " + key + " betreten. §7(" + currentPlayers + "/" + LobbyState.MAX_PLAYERS + ")");
								/*for (Entry<String, Player> entry : Main.ArenaPlayer.entrySet()) {
							        if (Objects.equals(key, entry.getKey())) {
							        	Player player = entry.getValue();
										player.sendMessage("§6" + p.getName() + " §ahat das Spiel: " + key + " betreten. §7(" + currentPlayers + "/" + LobbyState.MAX_PLAYERS + ")");
							        }
							    }*/
								
								if (currentPlayers >= LobbyState.MIN_PLAYERS) {
									// LobbyCountdown starten...
									if (!lobbyState.getLobbycountdown().isRunning()) {
										if (lobbyState.getLobbycountdown().isIdling()) {
											lobbyState.getLobbycountdown().cancelIdleing();
										}
										lobbyState.getLobbycountdown().run();
									}
								}
							}
							
							
							
							
							Location loc = (Location) dm.getData().get("Data.arenas." + key + ".lobbyspawn");
							p.teleport(loc);
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
