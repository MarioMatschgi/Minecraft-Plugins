package at.mario.hidenseek.countdowns;

import java.util.HashMap;

import org.bukkit.Bukkit;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.GameState;
import at.mario.hidenseek.gamestates.LobbyState;

public class LobbyCountdown extends Countdown {
	
	private HashMap<String, Boolean> isRunning;
	private HashMap<String, Integer> taskIDs;
	private HashMap<String, Integer> seconds;
	
	
	private HashMap<String, Integer> idleIDs;
	private HashMap<String, Boolean> isIdling;
	private int idleSeconds = 20;
	
	public LobbyCountdown() {
		isIdling = new HashMap<String, Boolean>();
		isRunning = new HashMap<String, Boolean>();
		idleIDs = new HashMap<String, Integer>();
		taskIDs = new HashMap<String, Integer>();
		
		seconds = new HashMap<String, Integer>();
	}
	
	@Override
	public void run(String arenaName) {
		if (!seconds.containsKey(arenaName)) {
			seconds.put(arenaName, 15);
		}
		
		isRunning.put(arenaName, true);
		 
		taskIDs.put(arenaName, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
				
			@Override
			public void run() {
				int currentPlayers = Main.ArenaPlayer.get(arenaName).size();
					
				if (currentPlayers < LobbyState.minPlayers.get(arenaName)) {
					cancel(arenaName);
				}
					
				switch (seconds.get(arenaName)) {
					case 60: case 30: case 15: case 10: case 9: case 8: case 7: case 6: case 5: case 4: case 3: case 2: case 1:
						Main.sendToArenaOnly(arenaName, "§aNoch " + seconds.get(arenaName) + " Sekunde(n)");
						break;
					case 0:
						Main.getInstance().getGameStateManager().setGameState(GameState.INGAME_STATE, arenaName);
						break;
					default:
						break;
				}
					
				seconds.put(arenaName, seconds.get(arenaName) - 1);
			}
		}, 0, 1 * 20));
	}

	@Override
	public void cancel(String arenaName) {
		isRunning.put(arenaName, false);
		
		Bukkit.getScheduler().cancelTask(taskIDs.get(arenaName));
		
		seconds.put(arenaName, 16);
	}
	
	public void idle(String arenaName) {
		isIdling.put(arenaName, true);
		
		idleIDs.put(arenaName, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				int currentPlayers = Main.ArenaPlayer.get(arenaName).size();
				int missingPlayers = LobbyState.minPlayers.get(arenaName) - currentPlayers;

				Main.sendToArenaOnly(arenaName, "§aEs fehlen noch " + missingPlayers + " Spieler");
			}
		}, idleSeconds * 20, idleSeconds * 20));
	}
	
	public void cancelIdleing(String arenaName) {
		isIdling.put(arenaName, false);
		
		Bukkit.getScheduler().cancelTask(idleIDs.get(arenaName));
	}
	
	
	public boolean isRunning(String arenaName) {
		boolean isrunning = false;
		if (isRunning.containsKey(arenaName)) {
			isrunning = isRunning.get(arenaName);
		}
		return isrunning;
	}
	
	public boolean isIdling(String arenaName) {
		boolean isidling = false;
		if (isIdling.containsKey(arenaName)) {
			isidling = isIdling.get(arenaName);
		}
		return isidling;
	}

	public int getIdleID(String arenaName) {
		return idleIDs.get(arenaName);
	}
}
