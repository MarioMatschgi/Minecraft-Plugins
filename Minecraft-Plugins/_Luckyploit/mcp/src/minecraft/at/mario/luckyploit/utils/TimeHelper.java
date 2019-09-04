package at.mario.luckyploit.utils;

public class TimeHelper {
	
	public static long lastMS = 0L;
	
	public boolean isDelayComplete(float f) {
		return (System.currentTimeMillis() - this.lastMS >= f);
	}
	
	public static long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}
	
	public void setLastMS(long lastMS) {
		this.lastMS = System.currentTimeMillis();
	}
	
	public int convertToMS(int perSecond) {
		return 1000 / perSecond;
	}
	
	public static boolean hasReached(long milliseonds) {
		return getCurrentMS() - lastMS >= milliseonds;
	}
	
	public static void reset() {
		lastMS = getCurrentMS(); 
	}
}