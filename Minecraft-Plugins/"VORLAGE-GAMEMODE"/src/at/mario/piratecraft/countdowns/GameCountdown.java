package at.mario.piratecraft.countdowns;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.gamestates.GameState;
import at.mario.piratecraft.gamestates.IngameState;
import at.mario.piratecraft.listener.SignChangeListener;
import at.mario.piratecraft.scoreboards.GameScoreboard;
import at.mario.piratecraft.utils.ItemsUtil;
import at.mario.piratecraft.utils.PlayerUtil;

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
		IngameState.gameStarts.put(arenaName, System.currentTimeMillis());
		
		if (!seconds.containsKey(arenaName)) {
			seconds.put(arenaName, Main.getInstance().getConfig().getInt("Config.maxGameLenght") * 60);
		}

		List<Player> players = Main.ArenaPlayer.get(arenaName);
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			ItemsUtil.giveIngameItems(true, player);
		}
		isRunning.put(arenaName, true);
		
		taskIDs.put(arenaName, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				List<Player> players = Main.ArenaPlayer.get(arenaName);
				
				if (players.size() == 0 || seconds.get(arenaName) == 0) {
					// Game over
					
					cancel(arenaName);
					Main.getInstance().getGameStateManager().setGameState(GameState.ENDING_STATE, arenaName);
					SignChangeListener.updateSigns(arenaName);
					return;
				}
				
				for (int i = 0; i < players.size(); i++) {
					Player player = players.get(i);

					GameScoreboard.setScoreboard(arenaName, player);
					PlayerUtil.sendIngameActionbar(arenaName, player);
				}
				
				seconds.put(arenaName, seconds.get(arenaName) - 1);
			}
		}, 0, 1 * 20));
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
	public static void setSeconds(HashMap<String, Integer> seconds) {
		GameCountdown.seconds = seconds;
	}
}
