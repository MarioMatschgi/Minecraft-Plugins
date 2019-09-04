package at.mario.hidenseek.gamestates;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.Roles;
import at.mario.hidenseek.listener.SignChangeListener;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;
import at.mario.hidenseek.manager.ConfigManagers.StatsManager;
import at.mario.hidenseek.scoreboards.GameScoreboard;
import at.mario.hidenseek.scoreboards.LobbyScoreboard;

public class EndingState extends GameState {
	
	private HashMap<String, Integer> seconds = new HashMap<String, Integer>();
	private HashMap<String, Integer> taskIDs = new HashMap<String, Integer>();
	
	@Override
	public void start(String arena) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		List<Player> players = Main.ArenaPlayer.get(arena);
		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);
			
			p.removePotionEffect(PotionEffectType.BLINDNESS);
			p.removePotionEffect(PotionEffectType.JUMP);
			p.removePotionEffect(PotionEffectType.SLOW);
			
			if (Roles.roles.get(p) == Roles.HIDER) {
				Main.arenaHiderLeft.put(arena, p);
			}
			
			Main.rewardPlayer(arena, p);
			
			Main.removeIngameItems(p);
			
			if (Main.getInstance().getConfig().getBoolean("Config.gameOverSound.enabled")) {
				p.getWorld().playSound(p.getLocation(), Sound.valueOf(Main.getInstance().getConfig().getString("Config.gameOverSound.sound")), 1, 1);
			}

			if (Main.getInstance().getConfig().get("Config.gamemode.ending") instanceof String) {
				if (Main.getInstance().getConfig().getString("Config.gamemode.ending").equalsIgnoreCase("survival")) {
					p.setGameMode(GameMode.SURVIVAL);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.ending").equalsIgnoreCase("creative")) {
					p.setGameMode(GameMode.CREATIVE);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.ending").equalsIgnoreCase("adventure")) {
					p.setGameMode(GameMode.ADVENTURE);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.ending").equalsIgnoreCase("spectator")) {
					p.setGameMode(GameMode.SPECTATOR);
				}
			} else {
				if (Main.getInstance().getConfig().getInt("Config.gamemode.ending") == 0) {
					p.setGameMode(GameMode.SURVIVAL);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.ending") == 1) {
					p.setGameMode(GameMode.CREATIVE);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.ending") == 2) {
					p.setGameMode(GameMode.ADVENTURE);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.ending") == 3) {
					p.setGameMode(GameMode.SPECTATOR);
				}
			}
		}
		
		if (!seconds.containsKey(arena)) {
			seconds.put(arena, Main.getInstance().getConfig().getInt("Config.gameOverDelay"));
		}
		taskIDs.put(arena, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				if (Main.getInstance().getConfig().getIntegerList("Config.gameOverBroadcastAt").contains(seconds.get(arena))) {
					for (int i = 0; i < players.size(); i++) {
						Player p = players.get(i);
						
						p.sendMessage(mm.getMessages().getString("Messages.gameOverBroadcast").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seconds%", seconds.get(arena)+""));
					}
				}
				if (seconds.get(arena) == 0) {
					Main.ArenaPlayer.remove(arena);
					Main.arenaFistSeeker.remove(arena);
					Main.arenaHiderFoundAfterSeconds.remove(arena);
					Main.arenaHiderLeft.remove(arena);
					Main.arenaLastHider.remove(arena);
					Main.arenaSeekerFoundPlayer.remove(arena);
					
					for (int i = 0; i < players.size(); i++) {
						Player p = players.get(i);
						
						LobbyScoreboard.removeScoreboard(p);
						GameScoreboard.removeScoreboard(p);

						if (Main.getInstance().getConfig().getBoolean("Config.sendStatsOnGameend")) {
							StatsManager.sendStats(p.getName(), p);
						}
						Location loc = p.getLocation();
						loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.mainlobbyspawn.world")));
						loc.setPitch((float) ((double) dm.getData().get("Data.mainlobbyspawn.pitch")));
						loc.setYaw((float) ((double) dm.getData().get("Data.mainlobbyspawn.yaw")));
						loc.setX(dm.getData().getDouble("Data.mainlobbyspawn.x"));
						loc.setY(dm.getData().getDouble("Data.mainlobbyspawn.y"));
						loc.setZ(dm.getData().getDouble("Data.mainlobbyspawn.z"));
						
						p.teleport(loc);

						p.setGameMode(GameMode.valueOf(dm.getData().getString("Data." + p.getName() + ".gamemode")));
						if (p.getGameMode() != GameMode.CREATIVE) {
							p.setAllowFlight(false);
						}
					}
					Main.getInstance().getGameStateManager().setGameState(GameState.LOBBY_STATE, arena);
					SignChangeListener.updateSigns(arena);
					
					Bukkit.getScheduler().cancelTask(taskIDs.get(arena));
					return;
				}
				if (Main.ArenaPlayer.get(arena).size() == 0) {
					Main.getInstance().getGameStateManager().setGameState(GameState.LOBBY_STATE, arena);
					SignChangeListener.updateSigns(arena);
					
					Bukkit.getScheduler().cancelTask(taskIDs.get(arena));
					return;
				}
				seconds.put(arena, seconds.get(arena) - 1);
			}
		}, 0, 1 * 20));
	}

	@Override
	public void stop(String arena) {
		
	}
}
