package at.mario.gravity.gamestates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import at.mario.gravity.Main;
import at.mario.gravity.listener.SignChangeListener;
import at.mario.gravity.manager.StatsManager;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;
import at.mario.gravity.scoreboards.GameScoreboard;
import at.mario.gravity.scoreboards.LobbyScoreboard;
import at.mario.gravity.utils.ItemsUtil;
import at.mario.gravity.utils.PlayerUtil;

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
			if (Main.getInstance().getConfig().getBoolean("Config.gameOverSound.enabled")) {
				p.getWorld().playSound(p.getLocation(), Sound.valueOf(Main.getInstance().getConfig().getString("Config.gameOverSound.sound")), 1, 1);
			}
			for (int i2 = 0; i2 < Bukkit.getOnlinePlayers().size(); i2++) {
				List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
				Player player = onlinePlayers.get(i2);
				
				if (PlayerUtil.isPlayerInArena(player)) {
					if (!PlayerUtil.getArenaOfPlayer(player).equals(arena)) {
						player.showPlayer(p);
						p.showPlayer(player);
					}
				}
			}
		}
		
		//if (!seconds.containsKey(arena)) {
			seconds.put(arena, Main.getInstance().getConfig().getInt("Config.gameOverDelay"));
		//}
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
					IngameState.ArenaUsedMaps.remove(arena);
					IngameState.ArenaFinishedPlayers.remove(arena);
					IngameState.ArenaPlayerFails.remove(arena);
					IngameState.ArenaPlayerLevels.remove(arena);
					PlayerUtil.hasPlayerFinished.remove(arena);
					
					for (int i = 0; i < players.size(); i++) {
						Player p = players.get(i);
						
						LobbyScoreboard.removeScoreboard(p);
						GameScoreboard.removeScoreboard(p);
						ItemsUtil.removeItems(p);

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
