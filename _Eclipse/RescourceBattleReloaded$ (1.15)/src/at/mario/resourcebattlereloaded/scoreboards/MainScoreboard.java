package at.mario.resourcebattlereloaded.scoreboards;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import at.mario.resourcebattlereloaded.Main;
import at.mario.resourcebattlereloaded.countdowns.MainCountdown;
import at.mario.resourcebattlereloaded.manager.ConfigManagers.ScoreboardCFGManager;

public class MainScoreboard {
	
	@SuppressWarnings("deprecation")
	public static void setScoreboard(Player p) {
		List<String> list = ScoreboardCFGManager.getScoreboardCFG().getStringList("Scoreboard.mainScoreboard.lines");
		
		Scoreboard board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("lobby", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ScoreboardCFGManager.getScoreboardCFG().getString("Scoreboard.mainScoreboard.title"));
		
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);
			
			obj.getScore(string.replace("%timeleft%", (MainCountdown.ticks / 20)+""))
			.setScore((i * -1) + list.size());
		}
		int idx = list.size();
		for (Player player : Bukkit.getOnlinePlayers()) {
			obj.getScore(ScoreboardCFGManager.getScoreboardCFG().getString("Scoreboard.mainScoreboard.scoreFormat").replace("%player%", player.getDisplayName())
					.replace("%score%", Main.playerScores.get(player)+""))
			.setScore((idx * -1) + list.size());
			
			idx++;
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
