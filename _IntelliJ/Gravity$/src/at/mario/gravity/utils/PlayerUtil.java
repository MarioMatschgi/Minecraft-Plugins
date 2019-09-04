package at.mario.gravity.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.mario.gravity.Main;
import at.mario.gravity.countdowns.GameCountdown;
import at.mario.gravity.gamestates.EndingState;
import at.mario.gravity.gamestates.IngameState;
import at.mario.gravity.gamestates.LobbyState;
import at.mario.gravity.listener.PlayerInteractListener;
import at.mario.gravity.listener.SignChangeListener;
import at.mario.gravity.manager.PackageSender;
import at.mario.gravity.manager.StatsManager;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;
import at.mario.gravity.scoreboards.GameScoreboard;
import at.mario.gravity.scoreboards.LobbyScoreboard;

public class PlayerUtil {
	
	public static HashMap<Player, Integer> specTaskIDs = new HashMap<Player, Integer>();
	public static HashMap<String, Boolean> hasPlayerFinished = new HashMap<String, Boolean>();

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
				if (p.hasPermission("hs.premiumJoin") || p.isOp()) {
					List<Player> players = new ArrayList<Player>();
					if (Main.ArenaPlayer.containsKey(arenaName) && Main.ArenaPlayer.get(arenaName) != null) {
						players = Main.ArenaPlayer.get(arenaName);
					}
					Player playerToKick = null;
					for (int i = players.size() - 1; i >= 0; i--) {
						Player player = players.get(i);
						
						if (!p.hasPermission("hs.premiumJoin") && !player.isOp()) {
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

			for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
				List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
				Player player = onlinePlayers.get(i);
				
				if (isPlayerInArena(player)) {
					if (!getArenaOfPlayer(player).equals(arenaName)) {
						player.hidePlayer(p);
						p.hidePlayer(player);
					}
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
		
		sendVoteInfo(arenaName, p);
		
		SignChangeListener.updateSigns(arenaName);
	
		int missingPlayers = LobbyState.minPlayers.get(arenaName) - currentPlayers;
		
		if (Main.ArenaPlayer.get(arenaName) != null) {
			if (Main.ArenaPlayer.containsKey(arenaName)) {
				currentPlayers = Main.ArenaPlayer.get(arenaName).size();
			}
		}
		
		LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(arenaName);
		ChatUtil.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.lobbyJoinMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).replace("%arena%", arenaName)
				.replace("%players%", currentPlayers+"").replace("%maxplayers%", LobbyState.maxPlayers.get(arenaName)+""));
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
		
		if (Main.getInstance().getConfig().get("Config.gamemode.lobby") instanceof String) {
			if (Main.getInstance().getConfig().getString("Config.gamemode.lobby").equalsIgnoreCase("survival")) {
				p.setGameMode(GameMode.SURVIVAL);
			} else if (Main.getInstance().getConfig().getString("Config.gamemode.lobby").equalsIgnoreCase("creative")) {
				p.setGameMode(GameMode.CREATIVE);
			} else if (Main.getInstance().getConfig().getString("Config.gamemode.lobby").equalsIgnoreCase("adventure")) {
				p.setGameMode(GameMode.ADVENTURE);
			} else if (Main.getInstance().getConfig().getString("Config.gamemode.lobby").equalsIgnoreCase("spectator")) {
				p.setGameMode(GameMode.SPECTATOR);
			}
		} else {
			if (Main.getInstance().getConfig().getInt("Config.gamemode.lobby") == 0) {
				p.setGameMode(GameMode.SURVIVAL);
			} else if (Main.getInstance().getConfig().getInt("Config.gamemode.lobby") == 1) {
				p.setGameMode(GameMode.CREATIVE);
			} else if (Main.getInstance().getConfig().getInt("Config.gamemode.lobby") == 2) {
				p.setGameMode(GameMode.ADVENTURE);
			} else if (Main.getInstance().getConfig().getInt("Config.gamemode.lobby") == 3) {
				p.setGameMode(GameMode.SPECTATOR);
			}
		}
		
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
			
			if (Main.getInstance().getConfig().get("Config.gamemode.spectator") instanceof String) {
				if (Main.getInstance().getConfig().getString("Config.gamemode.spectator").equalsIgnoreCase("survival")) {
					p.setGameMode(GameMode.SURVIVAL);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.spectator").equalsIgnoreCase("creative")) {
					p.setGameMode(GameMode.CREATIVE);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.spectator").equalsIgnoreCase("adventure")) {
					p.setGameMode(GameMode.ADVENTURE);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.spectator").equalsIgnoreCase("spectator")) {
					p.setGameMode(GameMode.SPECTATOR);
				}
			} else {
				if (Main.getInstance().getConfig().getInt("Config.gamemode.spectator") == 0) {
					p.setGameMode(GameMode.SURVIVAL);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.spectator") == 1) {
					p.setGameMode(GameMode.CREATIVE);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.spectator") == 2) {
					p.setGameMode(GameMode.ADVENTURE);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.spectator") == 3) {
					p.setGameMode(GameMode.SPECTATOR);
				}
			}
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
		
		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
			List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
			Player player = onlinePlayers.get(i);
			
			if (isPlayerInArena(player)) {
				if (!getArenaOfPlayer(player).equals(arenaName)) {
					player.showPlayer(p);
					p.showPlayer(player);
				}
			}
		}

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
		ItemsUtil.removeItems(p);
		p.setGameMode(GameMode.valueOf(dm.getData().getString("Data." + p.getName() + ".gamemode")));
		if (p.getGameMode() != GameMode.CREATIVE) {
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
			StatsManager.setStat(p, "fails", IngameState.ArenaPlayerFails.get(arenaName).get(p));
			
			
			for (int i = 0; i < players.size(); i++) {
				Player player = players.get(i);
				
				player.showPlayer(p);
				 // Finisher sichtbar machen
				p.showPlayer(player);
				 // Wenn KEIN finisher alle Finisher sichtbar machen
			}
		} else if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof EndingState) {
			for (int i = 0; i < players.size(); i++) {
				Player player = players.get(i);
				
				player.showPlayer(p);
				 // Finisher sichtbar machen
				p.showPlayer(player);
				 // Wenn KEIN finisher alle Finisher sichtbar machen
			}
		}
		
		Main.ArenaPlayer.put(arenaName, players);
		SignChangeListener.updateSigns(arenaName);
		
		currentPlayers = 0;
		if (Main.ArenaPlayer.containsKey(arenaName)) {
			currentPlayers = Main.ArenaPlayer.get(arenaName).size();
		}
		
		ChatUtil.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.lobbyQuitMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).replace("%arena%", arenaName)
				.replace("%players%", currentPlayers+"").replace("%maxplayers%", LobbyState.maxPlayers.get(arenaName)+""));
		LobbyScoreboard.removeScoreboard(p);
		GameScoreboard.removeScoreboard(p);
		p.removePotionEffect(PotionEffectType.BLINDNESS);
		p.removePotionEffect(PotionEffectType.JUMP);
		p.removePotionEffect(PotionEffectType.SLOW);

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
				
				p.teleport(loc);
			} else {
				loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.mainlobbyspawn.world")));
				loc.setPitch((float) ((double) dm.getData().get("Data.mainlobbyspawn.pitch")));
				loc.setYaw((float) ((double) dm.getData().get("Data.mainlobbyspawn.yaw")));
				loc.setX(dm.getData().getDouble("Data.mainlobbyspawn.x"));
				loc.setY(dm.getData().getDouble("Data.mainlobbyspawn.y"));
				loc.setZ(dm.getData().getDouble("Data.mainlobbyspawn.z"));
				p.teleport(loc);
			}
		} else {
			loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data." + p.getName() + ".position.world")));
			loc.setPitch((float) dm.getData().get("Data." + p.getName() + ".position.pitch"));
			loc.setYaw((float) dm.getData().get("Data." + p.getName() + ".position.yaw"));
			loc.setX(dm.getData().getDouble("Data." + p.getName() + ".position.x"));
			loc.setY(dm.getData().getDouble("Data." + p.getName() + ".position.y"));
			loc.setZ(dm.getData().getDouble("Data." + p.getName() + ".position.z"));
			
			p.teleport(loc);
		}
	}
	
	public static void nextStage(String arenaName, Player p) {
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		
		
		p.setHealth(p.getMaxHealth());
		p.setFoodLevel(20);
		
		if (IngameState.ArenaFinishedPlayers.containsKey(arenaName) && IngameState.ArenaFinishedPlayers.get(arenaName) != null && IngameState.ArenaFinishedPlayers.get(arenaName).containsKey(p)) {
			if (Main.getInstance().getConfig().getBoolean("Config.nextStageForFinishedPlayer")) {
				HashMap<Player, Integer> ArenaFinishedPlayerLevelsMap = IngameState.ArenaFinishedPlayerLevels.get(PlayerUtil.getArenaOfPlayer(p));
				
				if (ArenaFinishedPlayerLevelsMap.get(p) < Main.getInstance().getConfig().getInt("Config.levelAmount")) {
					
					Location loc = new Location(Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0, 0.0F, 0.0F);
					loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(ArenaFinishedPlayerLevelsMap.get(p)).getName() + ".spawn.world")));
					loc.setPitch((float) ((double) dm.getData().get("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(ArenaFinishedPlayerLevelsMap.get(p)).getName() + ".spawn.pitch")));
					loc.setYaw((float) ((double) dm.getData().get("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(ArenaFinishedPlayerLevelsMap.get(p)).getName() + ".spawn.yaw")));
					loc.setX(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(ArenaFinishedPlayerLevelsMap.get(p)).getName() + ".spawn.x"));
					loc.setY(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(ArenaFinishedPlayerLevelsMap.get(p)).getName() + ".spawn.y"));
					loc.setZ(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(ArenaFinishedPlayerLevelsMap.get(p)).getName() + ".spawn.z"));
					
					p.teleport(loc);

					ArenaFinishedPlayerLevelsMap.put(p, ArenaFinishedPlayerLevelsMap.get(p) + 1);
					IngameState.ArenaFinishedPlayerLevels.put(PlayerUtil.getArenaOfPlayer(p), ArenaFinishedPlayerLevelsMap);
				}
			}
		} else {
			if (IngameState.ArenaPlayerLevels.containsKey(arenaName) && IngameState.ArenaPlayerLevels.get(arenaName).containsKey(p) && IngameState.ArenaPlayerLevels.get(arenaName) != null && 
					IngameState.ArenaPlayerLevels.get(arenaName).get(p) != null) {
				if (IngameState.ArenaPlayerLevels.get(arenaName) != null && IngameState.ArenaPlayerLevels.get(arenaName).get(p) == Main.getInstance().getConfig().getInt("Config.levelAmount")) {
					// Spieler fertig
					
					if (IngameState.ArenaFinishedPlayers == null || IngameState.ArenaFinishedPlayers.isEmpty()) {
						setupPlayerFinish(arenaName, p);
					} else if (!IngameState.ArenaFinishedPlayers.get(arenaName).containsKey(p)) {
						setupPlayerFinish(arenaName, p);
					}
				} else {
					// Spieler nicht fertig
					
					HashMap<Player, Integer> ArenaPlayerLevelsMap = IngameState.ArenaPlayerLevels.get(arenaName);
					ArenaPlayerLevelsMap.put(p, IngameState.ArenaPlayerLevels.get(arenaName).get(p) + 1);
					IngameState.ArenaPlayerLevels.put(arenaName, ArenaPlayerLevelsMap);
					
					Location loc = new Location(Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0, 0.0F, 0.0F);
					loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + IngameState.ArenaUsedMaps.get(arenaName).get(IngameState.ArenaPlayerLevels.get(arenaName).get(p) - 1).getName() + ".spawn.world")));
					loc.setPitch((float) ((double) dm.getData().get("Data.maps." + IngameState.ArenaUsedMaps.get(arenaName).get(IngameState.ArenaPlayerLevels.get(arenaName).get(p) - 1).getName() + ".spawn.pitch")));
					loc.setYaw((float) ((double) dm.getData().get("Data.maps." + IngameState.ArenaUsedMaps.get(arenaName).get(IngameState.ArenaPlayerLevels.get(arenaName).get(p) - 1).getName() + ".spawn.yaw")));
					loc.setX(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(arenaName).get(IngameState.ArenaPlayerLevels.get(arenaName).get(p) - 1).getName() + ".spawn.x"));
					loc.setY(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(arenaName).get(IngameState.ArenaPlayerLevels.get(arenaName).get(p) - 1).getName() + ".spawn.y"));
					loc.setZ(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(arenaName).get(IngameState.ArenaPlayerLevels.get(arenaName).get(p) - 1).getName() + ".spawn.z"));

					p.teleport(loc);
					
					String mapName = IngameState.ArenaUsedMaps.get(arenaName).get(IngameState.ArenaPlayerLevels.get(arenaName).get(p) - 1).getName();
					
					String difficultyColor = mm.getMessages().getString("Messages.difficultyColors.hard");
					if (dm.getData().getInt("Data.maps." + mapName + ".difficulty") == 1) {
						difficultyColor = mm.getMessages().getString("Messages.difficultyColors.easy");
					} else if (dm.getData().getInt("Data.maps." + mapName + ".difficulty") == 2) {
						difficultyColor = mm.getMessages().getString("Messages.difficultyColors.medium");
					}
					String difficultyString = mm.getMessages().getString("Messages.gui.mapsetup.difficulties.hard");
					if (dm.getData().getInt("Data.maps." + mapName + ".difficulty") == 1) {
						difficultyString = mm.getMessages().getString("Messages.gui.mapsetup.difficulties.easy");
					} else if (dm.getData().getInt("Data.maps." + mapName + ".difficulty") == 2) {
						difficultyString = mm.getMessages().getString("Messages.gui.mapsetup.difficulties.medium");
					}
					
					if (Main.getInstance().getConfig().getBoolean("Config.finish.stage.other.title.enabled")) {
						PackageSender.sendTitle(
								p, 
								mm.getMessages().getString("Messages.finish.stage.other.title").
									replace("%player%", p.getName()).
									replace("%stagenumber%", (IngameState.ArenaPlayerLevels.get(arenaName).get(p))+"").replace("%mapname%", mapName).replace("%difficultycolor%", difficultyColor).replace("%difficulty%", 
											difficultyString), 
								mm.getMessages().getString("Messages.finish.stage.other.subtitle").
									replace("%player%", p.getName()).
									replace("%stagenumber%", (IngameState.ArenaPlayerLevels.get(arenaName).get(p))+"").replace("%mapname%", mapName).replace("%difficultycolor%", difficultyColor).replace("%difficulty%", 
											difficultyString), 
								Main.getInstance().getConfig().getInt("Config.finish.stage.other.title.fadeIn"), 
								Main.getInstance().getConfig().getInt("Config.finish.stage.other.title.stay"), 
								Main.getInstance().getConfig().getInt("Config.finish.stage.other.title.fadeOut"));
					}
					if (Main.getInstance().getConfig().getBoolean("Config.finish.stage.other.chat.enabled")) {
						ChatUtil.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.finish.stage.other.chat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).
							replace("%stagenumber%", (IngameState.ArenaPlayerLevels.get(arenaName).get(p) - 1) +"").replace("%seconds%", 
									((System.currentTimeMillis() - IngameState.ArenaPlayerLevelStart.get(arenaName).get(p)) / 1000)+"").replace("%mapname%", mapName).replace("%difficulty%", difficultyString).
							replace("/nl", "\n"));
					}
					if (Main.getInstance().getConfig().getBoolean("Config.finish.stage.player.title.enabled")) {
						PackageSender.sendTitle(
								p, 
								mm.getMessages().getString("Messages.finish.stage.player.title").
									replace("%stagenumber%", (IngameState.ArenaPlayerLevels.get(arenaName).get(p))+"").replace("%mapname%", mapName).replace("%difficultycolor%", difficultyColor).replace("%difficulty%", 
											difficultyString), 
								mm.getMessages().getString("Messages.finish.stage.player.subtitle").
									replace("%stagenumber%", (IngameState.ArenaPlayerLevels.get(arenaName).get(p))+"").replace("%mapname%", mapName).replace("%difficultycolor%", difficultyColor).replace("%difficulty%", 
											difficultyString), 
								Main.getInstance().getConfig().getInt("Config.finish.stage.player.title.fadeIn"), 
								Main.getInstance().getConfig().getInt("Config.finish.stage.player.title.stay"), 
								Main.getInstance().getConfig().getInt("Config.finish.stage.player.title.fadeOut"));
					}
					if (Main.getInstance().getConfig().getBoolean("Config.finish.stage.player.chat.enabled")) {
						p.sendMessage(mm.getMessages().getString("Messages.finish.stage.player.chat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).
							replace("%stagenumber%", (IngameState.ArenaPlayerLevels.get(arenaName).get(p) - 1)+"").replace("%seconds%", 
									((System.currentTimeMillis() - IngameState.ArenaPlayerLevelStart.get(arenaName).get(p)) / 1000)+"").replace("%mapname%", mapName).replace("%difficulty%", difficultyString).
							replace("/nl", "\n"));
					}

					HashMap<Player, Long> ArenaPlayerLevelStartMap = new HashMap<Player, Long>();
					if (IngameState.ArenaPlayerLevelStart.containsKey(arenaName) && IngameState.ArenaPlayerLevelStart.get(arenaName) != null) {
						ArenaPlayerLevelStartMap = IngameState.ArenaPlayerLevelStart.get(arenaName);
					}
					ArenaPlayerLevelStartMap.put(p, System.currentTimeMillis());
					IngameState.ArenaPlayerLevelStart.put(arenaName, ArenaPlayerLevelStartMap);

					HashMap<Player, Integer> ArenaPlayerREALFailsMap = IngameState.ArenaPlayerREALFails.get(arenaName);
					ArenaPlayerREALFailsMap.put(p, 0);
					IngameState.ArenaPlayerREALFails.put(arenaName, ArenaPlayerREALFailsMap);
					GameScoreboard.setScoreboard(arenaName, p);
					
					GameScoreboard.setScoreboard(arenaName, p);
				}
				sendIngameActionbar(arenaName, p);
			}
		}
	}
	public static void restartStage(Player p, String arena) {
		DataManager dm = new DataManager();
		
		HashMap<Player, Integer> ArenaPlayerFailsMap = IngameState.ArenaPlayerFails.get(arena);
		ArenaPlayerFailsMap.put(p, ArenaPlayerFailsMap.get(p) + 1);
		IngameState.ArenaPlayerFails.put(arena, ArenaPlayerFailsMap);
		
		Location loc = new Location(Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0, 0.0F, 0.0F);
		loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + IngameState.ArenaUsedMaps.get(arena).get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName() + ".spawn.world")));
		loc.setPitch((float) ((double) dm.getData().get("Data.maps." + IngameState.ArenaUsedMaps.get(arena).get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName() + ".spawn.pitch")));
		loc.setYaw((float) ((double) dm.getData().get("Data.maps." + IngameState.ArenaUsedMaps.get(arena).get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName() + ".spawn.yaw")));
		loc.setX(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(arena).get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName() + ".spawn.x"));
		loc.setY(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(arena).get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName() + ".spawn.y"));
		loc.setZ(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(arena).get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName() + ".spawn.z"));
		
		p.teleport(loc);
		
		PlayerUtil.sendIngameActionbar(arena, p);
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 99999, true, false));
		
		if (Main.getInstance().getConfig().getBoolean("Config.skip.enabled")) {
			int rand = Main.getInstance().getConfig().getInt("Config.skip.minimumFailsToSkip") + new Random().nextInt(Main.getInstance().getConfig().getInt("Config.skip.maximumFailsToSkip"));
			
			if (IngameState.ArenaPlayerREALFails.get(arena).get(p) == Main.getInstance().getConfig().getInt("Config.skip.forceskipWithFails") || IngameState.ArenaPlayerREALFails.get(arena).get(p) == rand) {
				ItemsUtil.giveSkipItem(p);
			}
		}
	}
	
	public static void sendVoteInfo(String arenaName, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();

		List<Player> players = new ArrayList<Player>();
		if (Main.ArenaPlayer.containsKey(arenaName) && Main.ArenaPlayer.get(arenaName) != null) {
			players = Main.ArenaPlayer.get(arenaName);
		}
		for (int i = 0; i < mm.getMessages().getStringList("Messages.voteInfo.info").size(); i++) {
			String msg = mm.getMessages().getStringList("Messages.voteInfo.info").get(i).replace("%prefix%", mm.getMessages().getString("Messages.prefix"));
			ChatUtil.sendToArenaOnly(arenaName, msg);
		}
		HashMap<Integer, List<ConfigurationSection> > usedMaps = LobbyState.ArenaMaps.get(arenaName);
		for (int i = 0; i < mm.getMessages().getStringList("Messages.voteInfo.format").size(); i++) {
			List<ConfigurationSection> maps = usedMaps.get(i);
			
			HashMap<List<ConfigurationSection>, Integer> votesMap = new HashMap<List<ConfigurationSection>, Integer>();
			if (LobbyState.ArenaVotes.containsKey(arenaName) && LobbyState.ArenaVotes.get(arenaName) != null) {
				// Bukkit.broadcastMessage("Jup1: ");
				votesMap = LobbyState.ArenaVotes.get(arenaName);
			} else {
				// Bukkit.broadcastMessage("NOPE1: ");
			}
			if (votesMap.isEmpty()) {
				// Bukkit.broadcastMessage("Jup2: ");
				for (List<ConfigurationSection> configurationSection : usedMaps.values()) {
					votesMap.put(configurationSection, 0);
				}
				
				//votesMap.put(maps, 1);
			}
			
			int[] votes = new int[Main.getInstance().getConfig().getInt("Config.levelAmount")];
			if (LobbyState.ArenaVotes.containsKey(arenaName) && LobbyState.ArenaVotes.get(arenaName) != null) {
				// Bukkit.broadcastMessage("Jup4: ");
				votesMap = LobbyState.ArenaVotes.get(arenaName);
			} else {
				// Bukkit.broadcastMessage("Nope4: ");
			}
			
			for (int j = 0; j < 5; j++) {
				List<ConfigurationSection> votesmaps = new ArrayList<ConfigurationSection>();
				if (LobbyState.ArenaMaps.containsKey(arenaName) && LobbyState.ArenaMaps.get(arenaName) != null) {
					// Bukkit.broadcastMessage("Jup5: ");
					if (LobbyState.ArenaMaps.get(arenaName).containsKey(j) && LobbyState.ArenaMaps.get(arenaName).get(j) != null) {
						// Bukkit.broadcastMessage("Jup6: ");
						votesmaps = LobbyState.ArenaMaps.get(arenaName).get(j);
					} else {
						// Bukkit.broadcastMessage("NOPE6: ");
					}
				} else {
					// Bukkit.broadcastMessage("NOPE5: ");
				}
				if (votesMap.isEmpty() || votesMap == null || votesMap.get(votesmaps) == null) {
					// Bukkit.broadcastMessage("Jup7: ");
					votes[j] = 0;
				} else {
					// Bukkit.broadcastMessage("NOPE7: ");
					votes[j] = votesMap.get(votesmaps);
				}
			}
			//System.out.println(votesMap);
			String msg = (mm.getMessages().getStringList("Messages.voteInfo.format").get(i)).replace("%prefix%", mm.getMessages().getString("Messages.prefix"));
			for (int j = 0; j < votes.length; j++) {
				// Bukkit.broadcastMessage("J: " + j);
				// Bukkit.broadcastMessage("votes[j]: " + votes[j]);
				// Bukkit.broadcastMessage("msg: " + msg);
				msg = msg.replace("%votes" + (j + 1) + "%", votes[j]+"");
				// Bukkit.broadcastMessage("msg: " + msg);
			}
			for (int j = 0; j < 5; j++) {
				for (int h = 0; h < Main.getInstance().getConfig().getInt("Config.levelAmount"); h++) {
					String color = mm.getMessages().getString("Messages.difficultyColors.hard");
					if (dm.getData().getInt("Data.maps." + usedMaps.get(j).get(h).getName() + ".difficulty") == 1) {
						color = mm.getMessages().getString("Messages.difficultyColors.easy");
					} else if (dm.getData().getInt("Data.maps." + usedMaps.get(j).get(h).getName() + ".difficulty") == 2) {
						color = mm.getMessages().getString("Messages.difficultyColors.medium");
					}
					msg = msg.replace("%color" + (j + 1) + (h + 1) + "%", color);
				}
			}
			
			String hoverInfo = mm.getMessages().getString("Messages.voteInfo.hoverInfoFormat").replace("%prefix%", mm.getMessages().getString("Messages.prefix"));
			for (int j = 0; j < votes.length; j++) {
				String color = mm.getMessages().getString("Messages.difficultyColors.hard");
				if (dm.getData().getInt("Data.maps." + maps.get(j).getName() + ".difficulty") == 1) {
					color = mm.getMessages().getString("Messages.difficultyColors.easy");
				} else if (dm.getData().getInt("Data.maps." + maps.get(j).getName() + ".difficulty") == 2) {
					color = mm.getMessages().getString("Messages.difficultyColors.medium");
				}
				hoverInfo = hoverInfo.replace("%votes" + (j + 1) + "%", votes[j]+"").replace("%color" + (j + 1) + "%", color).replace("%map" + (j + 1) + "%", maps.get(j).getName());
			}
			
			for (int i2 = 0; i2 < players.size(); i2++) {
				Player player = players.get(i2);
				PackageSender.sendMessageWithChatHover(player, msg, hoverInfo, "/vote " + (i + 1));
			}
		}
	}
	public static void sendIngameActionbar(String arenaName, Player p) {
		MessagesManager mm = new MessagesManager();
		
		HashMap<Player, Long> finishedPlayers = new HashMap<Player, Long>();
		if (IngameState.ArenaFinishedPlayers.containsKey(arenaName) && IngameState.ArenaFinishedPlayers.get(arenaName) != null) {
			finishedPlayers = IngameState.ArenaFinishedPlayers.get(arenaName);
		}
		
		List<ConfigurationSection> usedMaps = IngameState.ArenaUsedMaps.get(arenaName);
		String map1 = usedMaps.get(0).getName();
		String color1 = "§f";
		if (IngameState.ArenaPlayerLevels.containsKey(arenaName) && IngameState.ArenaPlayerLevels.get(arenaName).containsKey(p) && IngameState.ArenaPlayerLevels.get(arenaName) != null && IngameState.ArenaPlayerLevels.get(arenaName).get(p) != null) {
			if (IngameState.ArenaPlayerLevels.get(arenaName).get(p) > 1 || finishedPlayers.containsKey(p)) {
				color1 = "§a";
			} else if (IngameState.ArenaPlayerLevels.get(arenaName).get(p) == 1) {
				color1 = "§e";
			}
		}
		String map2 = usedMaps.get(1).getName();
		String color2 = "§f";
		if (IngameState.ArenaPlayerLevels.containsKey(arenaName) && IngameState.ArenaPlayerLevels.get(arenaName).containsKey(p) && IngameState.ArenaPlayerLevels.get(arenaName) != null && IngameState.ArenaPlayerLevels.get(arenaName).get(p) != null) {
			if (IngameState.ArenaPlayerLevels.get(arenaName).get(p) > 2 || finishedPlayers.containsKey(p)) {
				color2 = "§a";
			} else if (IngameState.ArenaPlayerLevels.get(arenaName).get(p) == 2) {
				color2 = "§e";
			}
		}
		String map3 = usedMaps.get(2).getName();
		String color3 = "§f";
		if (IngameState.ArenaPlayerLevels.containsKey(arenaName) && IngameState.ArenaPlayerLevels.get(arenaName).containsKey(p) && IngameState.ArenaPlayerLevels.get(arenaName) != null && IngameState.ArenaPlayerLevels.get(arenaName).get(p) != null) {
			if (IngameState.ArenaPlayerLevels.get(arenaName).get(p) > 3 || finishedPlayers.containsKey(p)) {
				color3 = "§a";
			} else if (IngameState.ArenaPlayerLevels.get(arenaName).get(p) == 3) {
				color3 = "§e";
			}
		}
		String map4 = usedMaps.get(3).getName();
		String color4 = "§f";
		if (IngameState.ArenaPlayerLevels.containsKey(arenaName) && IngameState.ArenaPlayerLevels.get(arenaName).containsKey(p) && IngameState.ArenaPlayerLevels.get(arenaName) != null && IngameState.ArenaPlayerLevels.get(arenaName).get(p) != null) {
			if (IngameState.ArenaPlayerLevels.get(arenaName).get(p) > 4 || finishedPlayers.containsKey(p)) {
				color4 = "§a";
			} else if (IngameState.ArenaPlayerLevels.get(arenaName).get(p) == 4) {
				color4 = "§e";
			}
		}
		String map5 = usedMaps.get(4).getName();
		String color5 = "§f";
		if (IngameState.ArenaPlayerLevels.containsKey(arenaName) && IngameState.ArenaPlayerLevels.get(arenaName).containsKey(p) && IngameState.ArenaPlayerLevels.get(arenaName) != null && IngameState.ArenaPlayerLevels.get(arenaName).get(p) != null) {
			if (IngameState.ArenaPlayerLevels.get(arenaName).get(p) > 5 || finishedPlayers.containsKey(p)) {
				color5 = "§a";
			} else if (IngameState.ArenaPlayerLevels.get(arenaName).get(p) == 5) {
				color5 = "§e";
			}
		}
		PackageSender.sendActionbar(p, mm.getMessages().getString("Messages.actionbar.ingame").replace("%fails%", IngameState.ArenaPlayerFails.get(arenaName).get(p)+"").replace("%map1%", map1).
				replace("%map2%", map2).replace("%map3%", map3).replace("%map4%", map4).replace("%map5%", map5).replace("%color1%", color1).replace("%color2%", color2).replace("%color3%", color3).replace("%color4%", color4).
				replace("%color5%", color5));
	}
	
	public static void setupPlayerFinish(String arena, Player p) {
		MessagesManager mm = new MessagesManager();

		
		List<Player> players = Main.ArenaPlayer.get(arena);
		
		HashMap<Player, Long> playerTimes = new HashMap<Player, Long>();
		if (IngameState.ArenaFinishedPlayers.containsKey(arena) && IngameState.ArenaFinishedPlayers.get(arena) != null) {
			playerTimes = IngameState.ArenaFinishedPlayers.get(arena);
		}
		playerTimes.put(p, System.currentTimeMillis() - IngameState.gameStarts.get(arena));
		IngameState.ArenaFinishedPlayers.put(arena, playerTimes);
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			GameScoreboard.setScoreboard(arena, player);
			player.hidePlayer(p);
			 // Finisher unsichtbar machen
		}
		 
		HashMap<List<ConfigurationSection>, Integer> allMaps = LobbyState.ArenaVotes.get(arena);
		int[] votes = {0, 0, 0, 0, 0};
		int highestVote = 0;
			
		int temp = 0;
		for (Entry<List<ConfigurationSection>, Integer> entry : allMaps.entrySet()) {
		    // List<ConfigurationSection> key = entry.getKey();
		    Integer value = entry.getValue();
		    
		    votes[temp] = value;
		    temp++;
		}
		
		for (int counter = 1; counter < votes.length; counter++) {
			if (votes[counter] > highestVote) {
				highestVote = votes[counter];
			}
		}
		List<ConfigurationSection> votedMaps = new ArrayList<ConfigurationSection>();
		for (Entry<List<ConfigurationSection>, Integer> entry : allMaps.entrySet()) {
		    List<ConfigurationSection> key2 = entry.getKey();
		    Integer value = entry.getValue();
		    
		    if (value == highestVote) {
		    	votedMaps = key2;
		    }
		}
		
		// Sendet an alle
		if (Main.getInstance().getConfig().getBoolean("Config.finish.final.other.chat.enabled")) {
			ChatUtil.sendToArenaOnly(arena, mm.getMessages().getString("Messages.finish.final.other.chat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).replace("%placecolor%", 
					mm.getMessages().getString("Messages.finish.final.placeColors." + getPlaceOfPlayerString(arena, p))).replace("%place%", getPlaceOfPlayer(arena, p)+"").replace("%mapname%", 
							votedMaps.get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName()).replace("/nl", "\n"));
		}

		if (hasPlayerFinished == null || !hasPlayerFinished.containsKey(arena) || !hasPlayerFinished.get(arena)) {
			if (Main.getInstance().getConfig().getBoolean("Config.reduceTimeOnFirstFinish.enabled")) {
				if (Main.getInstance().getConfig().getString("Config.reduceTimeOnFirstFinish.method").equalsIgnoreCase("reduce")) {
					HashMap<String, Integer> cdSecondsMap = GameCountdown.getSeconds();
					cdSecondsMap.put(arena, GameCountdown.getSeconds().get(arena) - Main.getInstance().getConfig().getInt("Config.reduceTimeOnFirstFinish.value"));
					GameCountdown.setSeconds(cdSecondsMap);
				} else {
					HashMap<String, Integer> cdSecondsMap = GameCountdown.getSeconds();
					cdSecondsMap.put(arena, Main.getInstance().getConfig().getInt("Config.reduceTimeOnFirstFinish.value"));
					GameCountdown.setSeconds(cdSecondsMap);
				}
			}
		}

		HashMap<String, Integer> secondsMap = GameCountdown.getSeconds();
		int seconds = Main.getInstance().getConfig().getInt("Config.maxGameLenght");
		if (secondsMap.containsKey(arena)) {
			seconds = secondsMap.get(arena);
		}
		
		int minutes = seconds / 60;
		 seconds = seconds - (minutes * 60);
		
		
		if (Main.getInstance().getConfig().getBoolean("Config.finish.final.other.title.enabled") && (hasPlayerFinished == null || !hasPlayerFinished.containsKey(arena) || !hasPlayerFinished.get(arena))) {
			StatsManager.setStat(p, "wins", StatsManager.getStats(p).getInt("wins") + 1);
			
			for (int i = 0; i < players.size(); i++) {
				Player player = players.get(i);
				
				PackageSender.sendTitle(
						player, 
						mm.getMessages().getString("Messages.finish.final.other.title").
							replace("%player%", p.getName()).replace("%seconds%", String.format("%02d", seconds)+"").replace("%minutes%", minutes+"").replace("%timeLeft%", minutes + ":" + String.format("%02d", seconds)).
							replace("%placecolor%", mm.getMessages().getString("Messages.finish.final.placeColors." + getPlaceOfPlayerString(arena, p))).replace("%place%", getPlaceOfPlayer(arena, p)+"").replace("%mapname%", 
									votedMaps.get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName()).replace("/nl", "\n"), 
						mm.getMessages().getString("Messages.finish.final.other.subtitle").
							replace("%player%", p.getName()).replace("%seconds%", String.format("%02d", seconds)+"").replace("%minutes%", minutes+"").replace("%timeLeft%", minutes + ":" + String.format("%02d", seconds)).
							replace("%placecolor%", mm.getMessages().getString("Messages.finish.final.placeColors." + getPlaceOfPlayerString(arena, p))).replace("%place%", getPlaceOfPlayer(arena, p)+"").replace("%mapname%", 
									votedMaps.get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName()).replace("/nl", "\n"), 
						Main.getInstance().getConfig().getInt("Config.finish.final.other.title.fadeIn"), 
						Main.getInstance().getConfig().getInt("Config.finish.final.other.title.stay"), 
						Main.getInstance().getConfig().getInt("Config.finish.final.other.title.fadeOut"));
			}
			
		}
		// Sendet NUR an den Spieler
		if (Main.getInstance().getConfig().getBoolean("Config.finish.final.player.chat.enabled")) {
			p.sendMessage(mm.getMessages().getString("Messages.finish.final.player.chat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).replace("%placecolor%", 
					mm.getMessages().getString("Messages.finish.final.placeColors." + getPlaceOfPlayerString(arena, p))).replace("%place%", getPlaceOfPlayer(arena, p)+"").replace("%mapname%", 
						votedMaps.get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName()).replace("/nl", "\n"));
		}
		if (Main.getInstance().getConfig().getBoolean("Config.finish.final.player.title.enabled")) {
			PackageSender.sendTitle(
					p, 
					mm.getMessages().getString("Messages.finish.final.player.title").
						replace("%player%", p.getName()).replace("%seconds%", String.format("%02d", seconds)+"").replace("%minutes%", minutes+"").replace("%timeLeft%", minutes + ":" + String.format("%02d", seconds)).
						replace("%placecolor%", mm.getMessages().getString("Messages.finish.final.placeColors." + getPlaceOfPlayerString(arena, p))).replace("%place%", getPlaceOfPlayer(arena, p)+"").replace("%mapname%", 
								votedMaps.get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName()).replace("/nl", "\n"), 
					mm.getMessages().getString("Messages.finish.final.player.subtitle").
						replace("%player%", p.getName()).replace("%seconds%", String.format("%02d", seconds)+"").replace("%minutes%", minutes+"").replace("%timeLeft%", minutes + ":" + String.format("%02d", seconds)).
						replace("%placecolor%", mm.getMessages().getString("Messages.finish.final.placeColors." + getPlaceOfPlayerString(arena, p))).replace("%place%", getPlaceOfPlayer(arena, p)+"").replace("%mapname%", 
								votedMaps.get(IngameState.ArenaPlayerLevels.get(arena).get(p) - 1).getName()).replace("/nl", "\n"), 
					Main.getInstance().getConfig().getInt("Config.finish.final.player.title.fadeIn"), 
					Main.getInstance().getConfig().getInt("Config.finish.final.player.title.stay"), 
					Main.getInstance().getConfig().getInt("Config.finish.final.player.title.fadeOut"));
		}

		p.setAllowFlight(true);
		ItemsUtil.removeItems(p);
		ItemsUtil.giveFinisherItems(p);
		
		hasPlayerFinished.put(arena, true);
		
		
		HashMap<Player, Integer> ArenaFinishedPlayerLevelsMap = new HashMap<Player, Integer>();
		if (IngameState.ArenaFinishedPlayerLevels.containsKey(arena) && IngameState.ArenaFinishedPlayerLevels.get(arena) != null) {
			ArenaFinishedPlayerLevelsMap = IngameState.ArenaFinishedPlayerLevels.get(arena);
		}
		ArenaFinishedPlayerLevelsMap.put(p, Main.getInstance().getConfig().getInt("Config.levelAmount"));
		IngameState.ArenaFinishedPlayerLevels.put(arena, ArenaFinishedPlayerLevelsMap);
	}
	
	public static int getPlaceOfPlayer(String arena, Player p) {
		int index = 0;
		
		for (Player key : IngameState.ArenaFinishedPlayers.get(arena).keySet()) {
			++index;
			
			if (key.getName().equals(p.getName())) {
				break;
			}
		}
		
		return index;
	}
	private static String getPlaceOfPlayerString(String arena, Player p) {
		MessagesManager mm = new MessagesManager();
		
		String place = "else";
		HashMap<Player, Long> playersMap = new HashMap<Player, Long>();
		if (IngameState.ArenaFinishedPlayers.containsKey(arena) && IngameState.ArenaFinishedPlayers.get(arena) != null) {
			playersMap = IngameState.ArenaFinishedPlayers.get(arena);
		}
		playersMap = Main.sortHashMapByValues(playersMap);
		
		List<Player> playersPlace = new ArrayList<Player>();
		for (Player player : playersMap.keySet()) {
			playersPlace.add(player);
		}
		
		String first = mm.getMessages().getString("Messages.scoreboard.noPlayer");
		if (playersPlace != null && !playersPlace.isEmpty()) {
			if (playersPlace.size() >= 1) {
				first = "§9" + (playersPlace.get(0).getName());
			}
		}
		String second = mm.getMessages().getString("Messages.scoreboard.noPlayer");
		if (playersPlace != null && !playersPlace.isEmpty()) {
			if (playersPlace.size() >= 2) {
				second = "§9" + (playersPlace.get(1).getName());
			}
		}
		String third = mm.getMessages().getString("Messages.scoreboard.noPlayer");
		if (playersPlace != null && !playersPlace.isEmpty()) {
			if (playersPlace.size() >= 3) {
				third = "§9" + (playersPlace.get(2).getName());
			}
		}
		if (first.equals("§9" + p.getName())) {
			place = "first";
		} else if (second.equals("§9" + p.getName())) {
			place = "second";
		} else if (third.equals("§9" + p.getName())) {
			place = "third";
		}
		
		return place;
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
