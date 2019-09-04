package at.mario.piratecraft.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.gamestates.IngameState;
import at.mario.piratecraft.gamestates.LobbyState;
import at.mario.piratecraft.listener.PlayerInteractListener;
import at.mario.piratecraft.listener.SignChangeListener;
import at.mario.piratecraft.manager.StatsManager;
import at.mario.piratecraft.manager.ConfigManagers.DataManager;
import at.mario.piratecraft.manager.ConfigManagers.MessagesManager;
import at.mario.piratecraft.scoreboards.GameScoreboard;
import at.mario.piratecraft.scoreboards.LobbyScoreboard;

public class PlayerUtil {
	
	public static HashMap<Player, Integer> specTaskIDs = new HashMap<Player, Integer>();

	public static void joinGame(String arenaName, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof LobbyState) {
			int currentPlayers = 0;

			if (Main.ArenaPlayer.get(arenaName) != null) {
				if (Main.ArenaPlayer.containsKey(arenaName)) {
					currentPlayers = Main.ArenaPlayer.get(arenaName).size();
				}
			}
			
			if (currentPlayers <= dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers")) {
				joinGameF(arenaName, p);
			} else {
				if (p.hasPermission("piratecraft.premiumJoin") || p.isOp()) {
					List<Player> players = new ArrayList<Player>();
					if (Main.ArenaPlayer.containsKey(arenaName) && Main.ArenaPlayer.get(arenaName) != null) {
						players = Main.ArenaPlayer.get(arenaName);
					}
					Player playerToKick = null;
					for (int i = players.size() - 1; i >= 0; i--) {
						Player player = players.get(i);
						
						if (!p.hasPermission("piratecraft.premiumJoin") && !player.isOp()) {
							playerToKick = player;
							break;
						}
					}
					if (playerToKick == null) {
						playerToKick = players.get(players.size() - 1);
					}
					
					leaveGame(false, arenaName, playerToKick);
					joinGameF(arenaName, p);
				} else {
					p.sendMessage(mm.getMessages().getString("Messages.noPremiumJoin").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				}
			}
		} else if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof IngameState) {
			joinGameAsSpectator(arenaName, p);
		}
	}
	private static void joinGameF(String arenaName, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		int currentPlayers = 0;

		if (Main.ArenaPlayer.get(arenaName) != null) {
			if (Main.ArenaPlayer.containsKey(arenaName)) {
				currentPlayers = Main.ArenaPlayer.get(arenaName).size();
			}
		}
		List<Player> players = new ArrayList<Player>();
		if (Main.ArenaPlayer.containsKey(arenaName) && Main.ArenaPlayer.get(arenaName) != null) {
			players = Main.ArenaPlayer.get(arenaName);
		}
		players.add(p);
		Main.ArenaPlayer.put(arenaName, players);
		p.setAllowFlight(false);
		
		SignChangeListener.updateSigns(arenaName);
	
		int missingPlayers = LobbyState.minPlayers.get(arenaName) - currentPlayers;
		
		if (Main.ArenaPlayer.get(arenaName) != null) {
			if (Main.ArenaPlayer.containsKey(arenaName)) {
				currentPlayers = Main.ArenaPlayer.get(arenaName).size();
			}
		}
		
		LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(arenaName);
		ChatUtil.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.lobbyJoinMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).replace("%arena%", 
				arenaName).replace("%players%", currentPlayers+"").replace("%maxplayers%", LobbyState.maxPlayers.get(arenaName)+""));
		if (missingPlayers != 0) {
			missingPlayers = LobbyState.minPlayers.get(arenaName) - currentPlayers;
			ChatUtil.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.missingPlayersToStart").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%missingplayers%", missingPlayers+""));
		}
		
		Location loc = p.getLocation();
		loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".lobbyspawn.world")));
		loc.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".lobbyspawn.pitch")));
		loc.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".lobbyspawn.yaw")));
		loc.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.x"));
		loc.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.y"));
		loc.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.z"));
		p.teleport(loc);
		
		dm.getData().set("Data." + p.getName() + ".gamemode", p.getGameMode().toString());
		dm.saveData();
		
		setGamemode(p, "lobby");
		
		if (currentPlayers >= LobbyState.minPlayers.get(arenaName)) {
			// LobbyCountdown starten...
			if (!lobbyState.getLobbycountdown().isRunning(arenaName)) {
				if (lobbyState.getLobbycountdown().isIdling(arenaName)) {
					lobbyState.getLobbycountdown().cancelIdleing(arenaName);
				}
				lobbyState.getLobbycountdown().run(arenaName);
			}
		} else {
			lobbyState.getLobbycountdown().idle(arenaName);
		}
		
		ItemsUtil.giveLobbyItems(p);
		
		PlayerInteractListener.taskIDs.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				if (Main.ArenaPlayer.get(arenaName) != null && Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof LobbyState && Main.ArenaPlayer.get(arenaName).contains(p)) {
					LobbyScoreboard.setScoreboard(arenaName, p);
				} else {
					try {  Bukkit.getScheduler().cancelTask(PlayerInteractListener.taskIDs.get(p));  } catch (Exception e2) {		}
					PlayerInteractListener.taskIDs.remove(p);
					LobbyScoreboard.removeScoreboard(p);
					return;
				}
			}
		}, 0, 1 * 20));
	}
	
	public static void joinGameAsSpectator(String arenaName, Player p) {
		DataManager dm = new DataManager();
		
		if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof IngameState) {
			List<Player> spectatorPlayers = new ArrayList<Player>();
			if (Main.SpectateArenaPlayer.containsKey(arenaName) && Main.SpectateArenaPlayer.get(arenaName) != null) {
				spectatorPlayers = Main.SpectateArenaPlayer.get(arenaName);
			}
			spectatorPlayers.add(p);
			Main.SpectateArenaPlayer.put(arenaName, spectatorPlayers);
			SignChangeListener.updateSigns(arenaName);

			Location loc = p.getLocation();
			loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".lobbyspawn.world")));
			loc.setPitch((float) dm.getData().get("Data.arenas." + arenaName + ".lobbyspawn.pitch"));
			loc.setYaw((float) dm.getData().get("Data.arenas." + arenaName + ".lobbyspawn.yaw"));
			loc.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.x"));
			loc.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.y"));
			loc.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.z"));
			p.teleport(loc);
			
			dm.getData().set("Data." + p.getName() + ".gamemode", p.getGameMode().toString());
			dm.saveData();
			
			setGamemode(p, "spectator");
			p.setAllowFlight(true);
			
			ItemsUtil.removeItems(p);
			ItemsUtil.giveSpectatorItems(p);
			
			specTaskIDs.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					try {
						if (Main.SpectateArenaPlayer.get(arenaName).contains(p)) {
							GameScoreboard.setSpectatorScoreboard(arenaName, p);
						} else {
							GameScoreboard.removeScoreboard(p);
							Bukkit.getScheduler().cancelTask(specTaskIDs.get(p));
							specTaskIDs.remove(p);
						}
					} catch (Exception e) {	}
				}
			}, 0, 1 * 20));
		}
	}
	
	public static void leaveGame(Boolean didPlayerLeave, String arenaName, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		p.setAllowFlight(false);

		List<Player> players = Main.ArenaPlayer.get(arenaName);
		
		int currentPlayers = 0;
		if (Main.ArenaPlayer.containsKey(arenaName)) {
			currentPlayers = Main.ArenaPlayer.get(arenaName).size();
		}

		try {
			if (Main.SpectateArenaPlayer.get(arenaName).contains(p)) {
				List<Player> spectatorPlayers = new ArrayList<Player>();
				spectatorPlayers = Main.SpectateArenaPlayer.get(arenaName);
				spectatorPlayers.remove(p);
				Main.SpectateArenaPlayer.put(arenaName, spectatorPlayers);
			}
		} catch (Exception e) {	}
		
		if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof LobbyState) {
			LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(arenaName);
			
			if (currentPlayers < LobbyState.minPlayers.get(arenaName)) {
				if (currentPlayers == 1) {
					Main.getInstance().getGameStateManager().setIsJoinme(arenaName, false);
					if (lobbyState.getLobbycountdown().isRunning(arenaName)) {
						lobbyState.getLobbycountdown().cancel(arenaName);
					}
					if (lobbyState.getLobbycountdown().isIdling(arenaName)) {
						lobbyState.getLobbycountdown().cancelIdleing(arenaName);
					}
				} else {
					// LobbyCountdown stoppen...
					if (lobbyState.getLobbycountdown().isRunning(arenaName)) {
						lobbyState.getLobbycountdown().cancel(arenaName);
						if (!lobbyState.getLobbycountdown().isIdling(arenaName)) {
							lobbyState.getLobbycountdown().idle(arenaName);
						}
					}
				}
			}
		}
		if (players != null && players.contains(p)) {
			players.remove(p);
		}
		Main.ArenaPlayer.put(arenaName, players);
		
		ItemsUtil.removeItems(p);
		p.setGameMode(GameMode.valueOf(dm.getData().getString("Data." + p.getName() + ".gamemode")));
		if (p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR) {
			p.setAllowFlight(false);
		}
		
		if (Main.getInstance().getConfig().getBoolean("Config.sendStatsOnLeave")) {
			StatsManager.sendStats(p.getName(), p);
		}

		if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof IngameState) {
			if (didPlayerLeave) {
				if (Main.getInstance().getConfig().getBoolean("Config.add1LoseAtLeavingGame")) {
					StatsManager.setStat(p, "loses", StatsManager.getStats(p).getInt("loses") + 1);
				}
			} else {
				StatsManager.setStat(p, "loses", StatsManager.getStats(p).getInt("loses") + 1);
			}
		}
		SignChangeListener.updateSigns(arenaName);
		
		currentPlayers = 0;
		if (Main.ArenaPlayer.containsKey(arenaName)) {
			currentPlayers = Main.ArenaPlayer.get(arenaName).size();
		}
		
		ChatUtil.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.lobbyQuitMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).replace("%arena%", arenaName)
				.replace("%players%", currentPlayers+"").replace("%maxplayers%", LobbyState.maxPlayers.get(arenaName)+""));
		LobbyScoreboard.removeScoreboard(p);
		GameScoreboard.removeScoreboard(p);
		
		Location loc = p.getLocation();
		if (Main.getInstance().getConfig().getBoolean("Config.teleportToMainLobbySpawn")) {
			if (dm.getData().getString("Data.mainlobbyspawn.world") == null || dm.getData().get("Data.mainlobbyspawn.pitch") == null || dm.getData().get("Data.mainlobbyspawn.yaw") == null || dm.getData().get("Data.mainlobbyspawn.x") == null 
					|| dm.getData().get("Data.mainlobbyspawn.y") == null || dm.getData().get("Data.mainlobbyspawn.z") == null) {
				p.sendMessage(mm.getMessages().getString("Messages.noMainLobbySpawn").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				
				loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data." + p.getName() + ".position.world")));
				loc.setPitch((float) dm.getData().get("Data." + p.getName() + ".position.pitch"));
				loc.setYaw((float) dm.getData().get("Data." + p.getName() + ".position.yaw"));
				loc.setX(dm.getData().getDouble("Data." + p.getName() + ".position.x"));
				loc.setY(dm.getData().getDouble("Data." + p.getName() + ".position.y"));
				loc.setZ(dm.getData().getDouble("Data." + p.getName() + ".position.z"));
			} else {
				loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.mainlobbyspawn.world")));
				loc.setPitch((float) ((double) dm.getData().get("Data.mainlobbyspawn.pitch")));
				loc.setYaw((float) ((double) dm.getData().get("Data.mainlobbyspawn.yaw")));
				loc.setX(dm.getData().getDouble("Data.mainlobbyspawn.x"));
				loc.setY(dm.getData().getDouble("Data.mainlobbyspawn.y"));
				loc.setZ(dm.getData().getDouble("Data.mainlobbyspawn.z"));
			}
		} else {
			loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data." + p.getName() + ".position.world")));
			loc.setPitch((float) dm.getData().get("Data." + p.getName() + ".position.pitch"));
			loc.setYaw((float) dm.getData().get("Data." + p.getName() + ".position.yaw"));
			loc.setX(dm.getData().getDouble("Data." + p.getName() + ".position.x"));
			loc.setY(dm.getData().getDouble("Data." + p.getName() + ".position.y"));
			loc.setZ(dm.getData().getDouble("Data." + p.getName() + ".position.z"));
		}
		p.teleport(loc);
	}
	
	public static void setGamemode(Player p, String gamestate) {
		if (Main.getInstance().getConfig().get("Config.gamemode." + gamestate) instanceof String) {
			if (Main.getInstance().getConfig().getString("Config.gamemode." + gamestate).equalsIgnoreCase("survival")) {
				p.setGameMode(GameMode.SURVIVAL);
			} else if (Main.getInstance().getConfig().getString("Config.gamemode." + gamestate).equalsIgnoreCase("creative")) {
				p.setGameMode(GameMode.CREATIVE);
			} else if (Main.getInstance().getConfig().getString("Config.gamemode." + gamestate).equalsIgnoreCase("adventure")) {
				p.setGameMode(GameMode.ADVENTURE);
			} else if (Main.getInstance().getConfig().getString("Config.gamemode." + gamestate).equalsIgnoreCase("spectator")) {
				p.setGameMode(GameMode.SPECTATOR);
			}
		} else {
			if (Main.getInstance().getConfig().getInt("Config.gamemode." + gamestate) == 0) {
				p.setGameMode(GameMode.SURVIVAL);
			} else if (Main.getInstance().getConfig().getInt("Config.gamemode." + gamestate) == 1) {
				p.setGameMode(GameMode.CREATIVE);
			} else if (Main.getInstance().getConfig().getInt("Config.gamemode." + gamestate) == 2) {
				p.setGameMode(GameMode.ADVENTURE);
			} else if (Main.getInstance().getConfig().getInt("Config.gamemode." + gamestate) == 3) {
				p.setGameMode(GameMode.SPECTATOR);
			}
		}
	}
	
	public static void sendIngameActionbar(String arenaName, Player p) {
		
	}

	public static String getArenaOfPlayer(Player p) {
		DataManager dm = new DataManager();
		
		String arenaName = "";
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		if (configSection != null) {
			for (String key : configSection.getKeys(false)) {
				if (Main.ArenaPlayer.containsKey(key)) {
					if (Main.ArenaPlayer.get(key).contains(p)) {
						arenaName = key;
						break;
					}
				}
			}
		}
		
		return arenaName;
	}
	public static boolean isPlayerInArena(Player p) {
		DataManager dm = new DataManager();
		
		String arenaName = "";
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		if (configSection != null) {
			for (String key : configSection.getKeys(false)) {
				if (Main.ArenaPlayer.containsKey(key)) {
					if (Main.ArenaPlayer.get(key).contains(p)) {
						arenaName = key;
						break;
					}
				}
			}
		}
		
		if (arenaName == "" && arenaName.equals("")) {
			return false;
		}
		
		return true;
	}
}
