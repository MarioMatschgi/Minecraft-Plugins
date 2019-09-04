package at.mario.hidenseek.countdowns;

import java.util.HashMap;

import org.bukkit.Bukkit;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;

public class SeekerReleaseCountdown extends Countdown {
	
	private HashMap<String, Integer> taskIDs;
	private HashMap<String, Integer> seconds;
	
	public SeekerReleaseCountdown() {
		taskIDs = new HashMap<String, Integer>();
		seconds = new HashMap<String, Integer>();
	}
	
	@Override
	public void run(String arenaName) {
		MessagesManager mm = new MessagesManager();
		
		if (!seconds.containsKey(arenaName)) {
			seconds.put(arenaName, Main.getInstance().getConfig().getInt("Config.seekerWaitDuration"));
		}
		
		taskIDs.put(arenaName, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				if (Main.getInstance().getConfig().getIntegerList("Config.seekerWaitBroadcastAt").contains(seconds.get(arenaName))) {
					Main.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.seekerWaitBroadcast").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seconds%", seconds.get(arenaName)+""));
				}
				
				seconds.put(arenaName, seconds.get(arenaName) - 1);
				if (seconds.get(arenaName) <= 0) {
					Main.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.seekerWaitEnd").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					
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
