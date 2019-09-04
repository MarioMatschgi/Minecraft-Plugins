package at.mario.masterbuilders.countdowns;

import java.util.HashMap;

import org.bukkit.Bukkit;

import at.mario.masterbuilders.Main;

public class SeekerReleaseCountdown extends Countdown {
	
	private HashMap<String, Integer> taskIDs;
	private HashMap<String, Integer> seconds;
	
	public SeekerReleaseCountdown() {
		taskIDs = new HashMap<String, Integer>();
		seconds = new HashMap<String, Integer>();
	}
	
	@Override
	public void run(String arenaName) {
		seconds.put(arenaName, Main.getInstance().getConfig().getInt("Config.seekerWaitDuration"));
		
		taskIDs.put(arenaName, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				seconds.put(arenaName, seconds.get(arenaName) - 1);
				if (seconds.get(arenaName) <= 0) {
					cancel(arenaName);
				}
			}
			
		}, 0, 1 * 20));
	}

	@Override
	public void cancel(String arenaName) {
		Bukkit.getScheduler().cancelTask(taskIDs.get(arenaName));
		
		seconds.put(arenaName, Main.getInstance().getConfig().getInt("Config.seekerWaitDuration"));
	}
}
