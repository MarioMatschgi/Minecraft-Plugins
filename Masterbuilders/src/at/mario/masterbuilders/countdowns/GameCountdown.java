package at.mario.masterbuilders.countdowns;

import java.util.HashMap;

import org.bukkit.Bukkit;

import at.mario.masterbuilders.Main;

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
		if (!seconds.containsKey(arenaName)) {
			seconds.put(arenaName, Main.getInstance().getConfig().getInt("Config.maxGameLenght") * 60);
		}
		
		isRunning.put(arenaName, true);
		
		taskIDs.put(arenaName, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				seconds.put(arenaName, seconds.get(arenaName) - 1);
			}
		}, 0, 20));
	}

	@Override
	public void cancel(String arenaName) {
		isRunning.put(arenaName, false);
		
		Bukkit.getScheduler().cancelTask(taskIDs.get(arenaName));
		
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
