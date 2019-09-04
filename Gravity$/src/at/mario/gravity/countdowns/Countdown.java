package at.mario.gravity.countdowns;

public abstract class Countdown {
	
	protected int taskID;
	
	public abstract void run(String arenaName);
	public abstract void cancel(String arenaName);
}
