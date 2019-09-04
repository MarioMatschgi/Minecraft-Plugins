package at.mario.economy.scoreboards;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import at.mario.economy.Main;
import at.mario.economy.Manager.ConfigManagers.MessagesManager;

public class MainScoreboard {
	
	private static MessagesManager mm;
	
	public static void setScoreboard(Player p) {
		mm = new MessagesManager();
		
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("abcd", "abcd");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(mm.getMessages().getString("Messages.scoreboard.title"));
		
		//obj.getScore(mm.getMessages().getString("Messages.scoreboard.line0").replace("%minutes%", Countdown.minutes + "").replace("%seconds%", Countdown.seconds + "")).setScore(5);
		//obj.getScore(mm.getMessages().getString("Messages.scoreboard.line1").replace("%minutes%", Countdown.minutes + "").replace("%seconds%", Countdown.seconds + "")).setScore(4);
		//obj.getScore(mm.getMessages().getString("Messages.scoreboard.line2").replace("%minutes%", Countdown.minutes + "").replace("%seconds%", Countdown.seconds + "")).setScore(3);
		//obj.getScore(mm.getMessages().getString("Messages.scoreboard.line3").replace("%minutes%", Countdown.minutes + "").replace("%seconds%", Countdown.seconds + "")).setScore(2);
		//obj.getScore(mm.getMessages().getString("Messages.scoreboard.line4").replace("%minutes%", Countdown.minutes + "").replace("%seconds%", Countdown.seconds + "")).setScore(1);
		//obj.getScore(mm.getMessages().getString("Messages.scoreboard.line5").replace("%minutes%", Countdown.minutes + "").replace("%seconds%", Countdown.seconds + "")).setScore(0);
		
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