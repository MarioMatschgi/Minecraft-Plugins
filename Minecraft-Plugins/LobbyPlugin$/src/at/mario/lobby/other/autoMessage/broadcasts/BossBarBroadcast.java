package at.mario.lobby.other.autoMessage.broadcasts;

import org.bukkit.Bukkit;

import at.mario.lobby.Main;

public class BossBarBroadcast {
	
	/**
	 * Repeating scheduler.
	 */
	private static int schedulerTask;
	
	/**
	 * TODO
	 * 
	 * @param reduceHealthBar
	 */
	public void broadcast(boolean reduceHealthBar) {
		setSchedulerTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				// TODO
			}
		}, 0L, Main.getInstance().getConfig().getInt("chat.delay") * 20L));
	}
	
	/**
	 * TODO
	 * 
	 * @param reduceHealthBar
	 */
	public void randomBroadcast(boolean reduceHealthBar) {
		setSchedulerTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				// TODO
			}
		}, 0L, Main.getInstance().getConfig().getInt("chat.delay") * 20L));
	}
	
	/**
	 * Returns scheduler task.
	 * 
	 * @return the schedulerTask
	 */
	public static int getSchedulerTask() {
		return schedulerTask;
	}
	
	/**
	 * Sets scheduler task.
	 * 
	 * @param schedulerTask the schedulerTask to set
	 */
	public void setSchedulerTask(int schedulerTask) {
		BossBarBroadcast.schedulerTask = schedulerTask;
	}
}