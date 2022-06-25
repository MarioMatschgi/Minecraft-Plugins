package at.mario.gravity.scoreboards;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import at.mario.gravity.Main;
import at.mario.gravity.countdowns.LobbyCountdown;
import at.mario.gravity.gamestates.LobbyState;
import at.mario.gravity.manager.ConfigManagers.ScoreboardCFGManager;

public class LobbyScoreboard {
	
	public static void setScoreboard(String arenaName, Player p) {
		List<String> list = ScoreboardCFGManager.getScoreboardCFG().getStringList("Scoreboard.lobbyScoreboard.lines");
		
		Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("lobby", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ScoreboardCFGManager.getScoreboardCFG().getString("Scoreboard.lobbyScoreboard.title").replace("%arena%", arenaName));
		
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
			
			HashMap<String, Integer> secondsMap = LobbyCountdown.getSecondsMap();
			int seconds = Main.getInstance().getConfig().getInt("Config.lobbyCountdownLenght");
			if (secondsMap.containsKey(arenaName)) {
				seconds = secondsMap.get(arenaName);
			}
			int currentPlayers = Main.ArenaPlayer.get(arenaName).size();
			int missingPlayers = LobbyState.minPlayers.get(arenaName) - currentPlayers;
			
			obj.getScore(string.replace("%money%", money).replace("%arena%", arenaName).replace("%time%", seconds + 1 +"").replace("%missingplayers%", missingPlayers+"").replace("%currentplayers%", currentPlayers+"")).
				setScore((i * -1) + list.size());
		}
		// obj.getScore("                                     ").setScore(list.size() + 1);
		
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
