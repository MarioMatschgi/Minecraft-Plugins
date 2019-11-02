package at.mario.resourcebattlereloaded.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.mario.resourcebattlereloaded.Main;
import at.mario.resourcebattlereloaded.scoreboards.MainScoreboard;

public class MainCountdown {
	
	public static boolean isRunning;
	
	private static int taskID;
	public static int seconds;
	
	public static void start(int minutes) {
		//MessagesManager mm = new MessagesManager();
		
		seconds = minutes * 60;
		isRunning = true;
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				for (Player player: Bukkit.getOnlinePlayers()) {
					Main.UpdateScore(player);
					MainScoreboard.setScoreboard(player);
				}
				
				
				seconds--;
				if (seconds < 0)
					cancel();
			}
			
		}, 0, 1 * 20);
	}

	public static void cancel() {
		Bukkit.getScheduler().cancelTask(taskID);

		for (Player player: Bukkit.getOnlinePlayers())
			MainScoreboard.removeScoreboard(player);
		
		isRunning = false;
	}
}
