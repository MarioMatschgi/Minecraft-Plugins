package at.mario.hidenseek.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
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
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;
import at.mario.hidenseek.scoreboards.LobbyScoreboard;

public class PlayerInteractListener implements Listener {
	
	private HashMap<Player, Integer> taskIDs = new HashMap<Player, Integer>();
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
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
								if (dm.getData().getInt("Data.arenas." + key + ".maxplayer") < LobbyState.maxPlayers.get(key)) {
									List<Player> players = new ArrayList<Player>();
									if (Main.ArenaPlayer.containsKey(key)) {
										players = Main.ArenaPlayer.get(key);
									}
									players.add(p);
									Main.ArenaPlayer.put(key, players);
									SignChangeListener.updateSigns(key);
									
									int currentPlayers = Main.ArenaPlayer.get(key).size();
									
									if (Main.ArenaPlayer.containsKey(key)) {
										currentPlayers = Main.ArenaPlayer.get(key).size();
									}
								
									int missingPlayers = LobbyState.minPlayers.get(key) - currentPlayers;
									
									LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(key);
									Main.sendToArenaOnly(key, mm.getMessages().getString("Messages.lobbyJoinMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).replace("%arena%", key)
											.replace("%players%", currentPlayers+"").replace("%maxplayers%", LobbyState.maxPlayers.get(key)+""));
									if (missingPlayers != 0) {
										Main.sendToArenaOnly(key, mm.getMessages().getString("Messages.missingPlayersToStart").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%missingplayers%", missingPlayers+""));
									}

									Location loc = (Location) dm.getData().get("Data.arenas." + key + ".lobbyspawn");
									p.teleport(loc);
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
									
									taskIDs.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
										
										@Override
										public void run() {
											if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState && Main.ArenaPlayer.get(key).contains(p)) {
												LobbyScoreboard.setScoreboard(key, p);
											} else {
												try {  Bukkit.getScheduler().cancelTask(taskIDs.get(p));  } catch (Exception e2) {		}
												taskIDs.remove(p);
												LobbyScoreboard.removeScoreboard(p);
												return;
											}
										}
									}, 0, 1 * 20));
								}
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
