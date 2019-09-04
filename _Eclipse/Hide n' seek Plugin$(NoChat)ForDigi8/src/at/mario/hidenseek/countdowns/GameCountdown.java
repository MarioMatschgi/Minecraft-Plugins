package at.mario.hidenseek.countdowns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.Roles;
import at.mario.hidenseek.gamestates.GameState;
import at.mario.hidenseek.listener.SignChangeListener;
import at.mario.hidenseek.manager.PackageSender;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;
import at.mario.hidenseek.manager.ConfigManagers.StatsManager;
import at.mario.hidenseek.scoreboards.GameScoreboard;

public class GameCountdown extends Countdown {
	
	private HashMap<String, Boolean> isRunning;
	private HashMap<String, Integer> taskIDs;
	private static HashMap<String, Integer> seconds;
	
	public GameCountdown() {
		isRunning = new HashMap<String, Boolean>();
		taskIDs = new HashMap<String, Integer>();
		seconds = new HashMap<String, Integer>();
	}
	
	@Override
	public void run(String arenaName) {
		MessagesManager mm = new MessagesManager();
		
		if (!seconds.containsKey(arenaName)) {
			seconds.put(arenaName, Main.getInstance().getConfig().getInt("Config.maxGameLenght") * 60);
		}
		
		isRunning.put(arenaName, true);
		
		taskIDs.put(arenaName, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				List<Player> players = Main.ArenaPlayer.get(arenaName);
				
				List<Player> hiders = new ArrayList<Player>();
				List<Player> seekers = new ArrayList<Player>();
				for (int i = 0; i < players.size(); i++) {
					Player player = players.get(i);
					if (Roles.roles.get(player) == Roles.HIDER) {
						hiders.add(player);
					}
				}
				for (int i = 0; i < players.size(); i++) {
					Player player = players.get(i);
					if (Roles.roles.get(player) == Roles.HIDER) {
						seekers.add(player);
					}
				}
				
				int currentPlayers = Main.ArenaPlayer.get(arenaName).size();
					
				if (seconds.get(arenaName) == 30) {
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);
						
						if (Roles.roles.get(player) == Roles.HIDER) {
							player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 99999, 255, false, false));
							// PackageSender.sendTitle(player, mm.getMessages().getString("Messages.win.hidersTitle"), mm.getMessages().getString("Messages.win.hidersSubtitle"), 1, 5, 1);
						}
					}
				}
				
				if (currentPlayers == 1) {
					cancel(arenaName);
					Main.getInstance().getGameStateManager().setGameState(GameState.ENDING_STATE, arenaName);
					SignChangeListener.updateSigns(arenaName);
					
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);

						StatsManager.setStat(player, "wins", StatsManager.getStats(player).getInt("wins") + 1);
						if (Roles.roles.get(player) == Roles.SEEKER) {
							player.sendMessage(mm.getMessages().getString("Messages.win.seekersChat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							PackageSender.sendTitle(player, mm.getMessages().getString("Messages.win.seekersTitle"), mm.getMessages().getString("Messages.win.seekersSubtitle"), 1, 5, 1);
						} else if (Roles.roles.get(player) == Roles.HIDER) {
							player.sendMessage(mm.getMessages().getString("Messages.win.hidersChat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							PackageSender.sendTitle(player, mm.getMessages().getString("Messages.win.hidersTitle"), mm.getMessages().getString("Messages.win.hidersSubtitle"), 1, 5, 1);
						}
						GameScoreboard.removeScoreboard(player);
					}
					return;
				} else if (currentPlayers == 0) {
					cancel(arenaName);
					Main.getInstance().getGameStateManager().setGameState(GameState.ENDING_STATE, arenaName);
					SignChangeListener.updateSigns(arenaName);
					return;
				} else if (hiders.size() == 0) {
					// Seeker won
					cancel(arenaName);
					Main.getInstance().getGameStateManager().setGameState(GameState.ENDING_STATE, arenaName);
					SignChangeListener.updateSigns(arenaName);
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);
						
						if (Roles.roles.get(player) == Roles.SEEKER) {
							StatsManager.setStat(player, "wins", StatsManager.getStats(player).getInt("wins") + 1);
							player.sendMessage(mm.getMessages().getString("Messages.win.seekersChat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							PackageSender.sendTitle(player, mm.getMessages().getString("Messages.win.seekersTitle"), mm.getMessages().getString("Messages.win.seekersSubtitle"), 1, 5, 1);
						} else if (Roles.roles.get(player) == Roles.HIDER) {
							StatsManager.setStat(player, "loses", StatsManager.getStats(player).getInt("loses") + 1);
							player.sendMessage(mm.getMessages().getString("Messages.lose.hidersChat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							PackageSender.sendTitle(player, mm.getMessages().getString("Messages.lose.hidersTitle"), mm.getMessages().getString("Messages.lose.hidersSubtitle"), 1, 5, 1);
						}
						GameScoreboard.removeScoreboard(player);
					}
					return;
				} else if (seekers.size() == 0 || seconds.get(arenaName) == 0) {
					// Hiders won
					cancel(arenaName);
					Main.getInstance().getGameStateManager().setGameState(GameState.ENDING_STATE, arenaName);
					SignChangeListener.updateSigns(arenaName);
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);
						
						if (Roles.roles.get(player) == Roles.SEEKER) {
							StatsManager.setStat(player, "loses", StatsManager.getStats(player).getInt("loses") + 1);
							player.sendMessage(mm.getMessages().getString("Messages.lose.seekersChat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							PackageSender.sendTitle(player, mm.getMessages().getString("Messages.lose.seekersTitle"), mm.getMessages().getString("Messages.lose.seekersSubtitle"), 1, 5, 1);
						} else if (Roles.roles.get(player) == Roles.HIDER) {
							StatsManager.setStat(player, "wins", StatsManager.getStats(player).getInt("wins") + 1);
							player.sendMessage(mm.getMessages().getString("Messages.win.hidersChat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							PackageSender.sendTitle(player, mm.getMessages().getString("Messages.win.hidersTitle"), mm.getMessages().getString("Messages.win.hidersSubtitle"), 1, 5, 1);
						}
						GameScoreboard.removeScoreboard(player);
					}
					return;
				}
				
				for (int i = 0; i < players.size(); i++) {
					Player player = players.get(i);
					
					GameScoreboard.setScoreboard(arenaName, player);
				}
				seconds.put(arenaName, seconds.get(arenaName) - 1);
			}
		}, 0, 20));
	}

	@Override
	public void cancel(String arenaName) {
		isRunning.put(arenaName, false);
		
		Bukkit.getScheduler().cancelTask(taskIDs.get(arenaName));
		
		List<Player> players = Main.ArenaPlayer.get(arenaName);
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			if (Roles.roles.get(player) == Roles.HIDER) {
				player.removePotionEffect(PotionEffectType.GLOWING);
			}
		}
		
		seconds.put(arenaName, (Main.getInstance().getConfig().getInt("Config.maxGameLenght") * 60) + 1);
	}
	
	public boolean isRunning(String arenaName) {
		boolean isrunning = false;
		if (isRunning.containsKey(arenaName)) {
			isrunning = isRunning.get(arenaName);
		}
		return isrunning;
	}
	
	public static HashMap<String, Integer> getSeconds() {
		return seconds;
	}
}
