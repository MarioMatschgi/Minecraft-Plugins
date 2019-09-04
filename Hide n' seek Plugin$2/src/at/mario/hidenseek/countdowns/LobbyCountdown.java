package at.mario.hidenseek.countdowns;

import org.bukkit.Bukkit;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.GameState;
import at.mario.hidenseek.gamestates.LobbyState;

public class LobbyCountdown extends Countdown {
	
	private boolean isRunning = false, isIdling = false;
	
	private int seconds = 15;
	private int resetSeconds = 10;
	
	private int idleID;
	private int idleSeconds = 20;
	
	@Override
	public void run() {
		 isRunning = true;
		 
		 taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				switch (seconds) {
					case 60: case 30: case 15: case 10: case 9: case 8: case 7: case 6: case 5: case 4: case 3: case 2: case 1:
						Bukkit.broadcastMessage("§aNoch " + seconds + "Sekunde(n)");
						Bukkit.broadcastMessage("§a ToDo: NICHT BROADCASTEN");
						break;
					case 0:
						Main.getInstance().getGameStateManager().setGameState(GameState.INGAME_STATE);
						break;
					default:
						break;
				}
				
				seconds--;
			}
		}, 0, 1 * 20);
	}

	@Override
	public void cancel() {
		isRunning = false;
		
		Bukkit.getScheduler().cancelTask(taskID);
		
		seconds = resetSeconds;
	}
	
	public void idle() {
		isIdling = true;
		
		idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				int currentPlayers = 0;
				for (String key2 : Main.ArenaPlayer.keySet()) {
					if (key2 == "Test2") {
						currentPlayers++;
					}
				}
				int missingPlayers = LobbyState.MIN_PLAYERS - currentPlayers;
				
				Bukkit.broadcastMessage("§aEs fehlen noch " + missingPlayers + " Spieler");
				Bukkit.broadcastMessage("§a ToDo: NICHT BROADCASTEN");
			}
		}, idleSeconds * 20, idleSeconds * 20);
	}
	
	public void cancelIdleing() {
		isIdling = false;
		
		Bukkit.getScheduler().cancelTask(idleID);
	}
	
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public boolean isIdling() {
		return isIdling;
	}

	public int getIdleID() {
		return idleID;
	}
}
