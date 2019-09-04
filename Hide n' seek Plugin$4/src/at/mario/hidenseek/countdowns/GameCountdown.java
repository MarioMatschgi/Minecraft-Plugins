package at.mario.hidenseek.countdowns;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.GameState;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.listener.SignChangeListener;
import at.mario.hidenseek.scoreboards.GameScoreboard;

public class GameCountdown extends Countdown {
	
	private HashMap<String, Boolean> isRunning;
	private HashMap<String, Integer> taskIDs;
	private HashMap<String, Integer> seconds;
	
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
				List<Player> players = Main.ArenaPlayer.get(arenaName);
				int currentPlayers = Main.ArenaPlayer.get(arenaName).size();
					
				if (currentPlayers < LobbyState.minPlayers.get(arenaName) || seconds.get(arenaName) == 0) {
					Bukkit.broadcastMessage("§6ToDo§f: §ccurrentPlayers <= 1  NICHT < LobbyState.minPlayers.get(arenaName)  !!!");
					cancel(arenaName);
					Main.getInstance().getGameStateManager().setGameState(GameState.ENDING_STATE, arenaName);
					SignChangeListener.updateSigns(arenaName);
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);
						
						GameScoreboard.removeScoreboard(player);
					}
					return;
				}
				for (int i = 0; i < players.size(); i++) {
					Player player = players.get(i);
					
					GameScoreboard.setScoreboard(arenaName, player);
				}
				
				seconds.put(arenaName, seconds.get(arenaName) - 1);
			}
			
		}, 0, 15));
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
}
