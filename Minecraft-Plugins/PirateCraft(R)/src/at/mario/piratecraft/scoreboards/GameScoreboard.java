package at.mario.piratecraft.scoreboards;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.countdowns.GameCountdown;
import at.mario.piratecraft.manager.ConfigManagers.ScoreboardCFGManager;

public class GameScoreboard {
	
	public static void setScoreboard(String arenaName, Player p) {
		List<String> list = ScoreboardCFGManager.getScoreboardCFG().getStringList("Scoreboard.gameScoreboard.lines");
		
		Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("game", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ScoreboardCFGManager.getScoreboardCFG().getString("Scoreboard.gameScoreboard.title").replace("%arena%", arenaName));

		int spaceIndex = 0;
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);

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
			
			obj.getScore(string.replace("%money%", money).replace("%arena%", arenaName).replace("%currentplayers%", currentPlayers+"").replace("%seconds%", String.format("%02d", seconds)).replace("%minutes%", minutes+"").
					replace("%timeleft%", minutes + ":" + String.format("%02d", seconds)))
			
			.setScore( (i * -1) + list.size());
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
			
			obj.getScore(string.replace("%money%", money).replace("%arena%", arenaName).replace("%currentplayers%", currentPlayers+"").replace("%seconds%", seconds+"").replace("%minutes%", minutes+""));
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
	
	public static int[] splitToComponentTimes(BigDecimal biggy) {
		long longVal = biggy.longValue();
		int hours = (int) longVal / 3600;
		int remainder = (int) longVal - hours * 3600;
		int mins = remainder / 60;
		remainder = remainder - mins * 60;
		int secs = remainder;

		int[] ints = {mins , secs};
		return ints;
	}
}
