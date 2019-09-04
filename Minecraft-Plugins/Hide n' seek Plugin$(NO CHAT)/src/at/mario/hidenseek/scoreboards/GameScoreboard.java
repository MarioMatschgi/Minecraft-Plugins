package at.mario.hidenseek.scoreboards;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.Roles;
import at.mario.hidenseek.countdowns.GameCountdown;
import at.mario.hidenseek.manager.ConfigManagers.ScoreboardCFGManager;

public class GameScoreboard {
	
	public static void setScoreboard(String arenaName, Player p) {
		List<String> list = ScoreboardCFGManager.getScoreboardCFG().getStringList("Scoreboard.gameScoreboard.lines");
		
		Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("game", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ScoreboardCFGManager.getScoreboardCFG().getString("Scoreboard.gameScoreboard.title").replace("%arena%", arenaName));
		
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);

			int spaceIndex = 0;
			if (string.equals("") || string == "" || string == null) {
				for (int j = 0; j < spaceIndex; j++) {
					string = string + " ";
				}
				spaceIndex++;
			}
			
			Double moNey = 0.0;
			if (Main.eco != null) {
				if (Main.eco.hasAccount(p)) {
					moNey = Main.eco.getBalance(p);
				}
			}
			int Money = moNey.intValue();
			String money = NumberFormat.getInstance().format(Money);
			
			HashMap<String, Integer> secondsMap = GameCountdown.getSeconds();
			int seconds = Main.getInstance().getConfig().getInt("Config.maxGameLenght");
			if (secondsMap.containsKey(arenaName)) {
				seconds = secondsMap.get(arenaName);
			}
			
			int minutes = seconds / 60;
			 seconds = seconds - (minutes * 60);
			int currentPlayers = Main.ArenaPlayer.get(arenaName).size();
			int hiders = 0;
			int seekers = 0;
			
			for (Object value : Roles.roles.values()) {
				if (value == Roles.HIDER) {
					hiders++;
				} else if (value == Roles.SEEKER) {
					seekers++;
				}
			}
			
			obj.getScore(string.replace("%money%", money).replace("%arena%", arenaName).replace("%currentplayers%", currentPlayers+"").replace("%seconds%", seconds+"").replace("%minutes%", minutes+"").replace("%role%", 
					Roles.roles.get(p)).replace("%hiders%", hiders+"").replace("%seekers%", seekers+"")).setScore( (i * -1) + list.size());
		}
		obj.getScore("                                        ").setScore(list.size() + 1);
		
		p.setScoreboard(board);
	}
	public static void setSpectatorScoreboard(String arenaName, Player p) {
		List<String> list = ScoreboardCFGManager.getScoreboardCFG().getStringList("Scoreboard.gameScoreboard.lines");
		
		Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("game", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ScoreboardCFGManager.getScoreboardCFG().getString("Scoreboard.gameScoreboard.title").replace("%arena%", arenaName));
		
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);

			int spaceIndex = 0;
			if (string.equals("") || string == "" || string == null) {
				for (int j = 0; j < spaceIndex; j++) {
					string = string + " ";
				}
				spaceIndex++;
			}
			Double moNey = Main.eco.getBalance(p);
			int Money = moNey.intValue();
			String money = NumberFormat.getInstance().format(Money);
			
			HashMap<String, Integer> secondsMap = GameCountdown.getSeconds();
			int seconds = Main.getInstance().getConfig().getInt("Config.maxGameLenght");
			if (secondsMap.containsKey(arenaName)) {
				seconds = secondsMap.get(arenaName);
			}
			
			int minutes = seconds / 60;
			 seconds = seconds - (minutes * 60);
			int currentPlayers = Main.ArenaPlayer.get(arenaName).size();
			int hiders = 0;
			int seekers = 0;
			
			for (Object value : Roles.roles.values()) {
				if (value == Roles.HIDER) {
					hiders++;
				} else if (value == Roles.SEEKER) {
					seekers++;
				}
			}
			
			obj.getScore(string.replace("%money%", money).replace("%arena%", arenaName).replace("%currentplayers%", currentPlayers+"").replace("%seconds%", seconds+"").replace("%minutes%", minutes+"").replace("%role%", "SPECTATOR").replace("%hiders%", hiders+"").replace("%seekers%", seekers+"")).setScore( (i * -1) + list.size());
		}
		obj.getScore("                                        ").setScore(list.size() + 1);
		
		p.setScoreboard(board);
	}
	
	public static void removeScoreboard(Player p) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			}
		}, 20L);
	}
}
