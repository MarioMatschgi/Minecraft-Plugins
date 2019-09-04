package at.mario.hidenseek.countdowns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.GameState;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.listener.SignChangeListener;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;
import at.mario.hidenseek.scoreboards.LobbyScoreboard;

public class LobbyCountdown extends Countdown {
	
	private HashMap<String, Boolean> isRunning;
	private HashMap<String, Integer> taskIDs;
	private static HashMap<String, Integer> seconds;
	
	
	private HashMap<String, Integer> idleIDs;
	private HashMap<String, Boolean> isIdling;
	
	public LobbyCountdown() {
		isIdling = new HashMap<String, Boolean>();
		isRunning = new HashMap<String, Boolean>();
		idleIDs = new HashMap<String, Integer>();
		taskIDs = new HashMap<String, Integer>();
		
		seconds = new HashMap<String, Integer>();
	}
	
	@Override
	public void run(String arenaName) {
		MessagesManager mm = new MessagesManager();
		
		isRunning.put(arenaName, true);
		
		if (!seconds.containsKey(arenaName)) {
			seconds.put(arenaName, Main.getInstance().getConfig().getInt("Config.lobbyCountdownLenght"));
		}
		 
		taskIDs.put(arenaName, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
				
			@Override
			public void run() {
				List<Integer> list = Main.getInstance().getConfig().getIntegerList("Config.countdownBroadcastAt");
				int currentPlayers = Main.ArenaPlayer.get(arenaName).size();
					
				if (currentPlayers < LobbyState.minPlayers.get(arenaName)) {
					cancel(arenaName);
				}
				
				List<Player> players = new ArrayList<Player>();
				
				if (Main.ArenaPlayer.containsKey(arenaName)) {
					players = Main.ArenaPlayer.get(arenaName);
				}
				
				for (int i = 0; i < players.size(); i++) {
					Player player = players.get(i);
					
					ItemStack start = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.lobbyitems.startItem.material")), Main.getInstance().getConfig().getInt("Config.lobbyitems.startItem.amount"), (short) 
							Main.getInstance().getConfig().getInt("Config.lobbyitems.startItem.damage"));
					ItemMeta startMeta = start.getItemMeta();
					startMeta.setDisplayName(mm.getMessages().getString("Messages.lobbyitems.startItem.name"));
					startMeta.setLore(mm.getMessages().getStringList("Messages.lobbyitems.startItem.lore"));
					start.setItemMeta(startMeta);
					player.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.startItem.slot"), start);
				}
				
				if (seconds.get(arenaName) == 0) {
					cancel(arenaName);
					Main.getInstance().getGameStateManager().setGameState(GameState.INGAME_STATE, arenaName);
					SignChangeListener.updateSigns(arenaName);
					return;
				}
				if (list.contains(seconds.get(arenaName))) {
					Main.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.countdownBroadcast").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seconds%", seconds.get(arenaName)+""));
				}
				seconds.put(arenaName, seconds.get(arenaName) - 1);
				for (int i = 0; i < players.size(); i++) {
					Player player = players.get(i);
					
					LobbyScoreboard.setScoreboard(arenaName, player);
				}
				
				if (currentPlayers < LobbyState.minPlayers.get(arenaName)) {
					cancel(arenaName);
				}
			}
		}, 0, 1 * 20));
	}

	@Override
	public void cancel(String arenaName) {
		isRunning.put(arenaName, false);
		
		Bukkit.getScheduler().cancelTask(taskIDs.get(arenaName));
		
		seconds.put(arenaName,  Main.getInstance().getConfig().getInt("Config.lobbyCountdownLenght") + 1);
		Main.getInstance().getGameStateManager().setIsJoinme(arenaName, false);
	}
	
	public void idle(String arenaName) {
		MessagesManager mm = new MessagesManager();
		
		isIdling.put(arenaName, true);
		
		idleIDs.put(arenaName, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				int currentPlayers = Main.ArenaPlayer.get(arenaName).size();
				int missingPlayers = LobbyState.minPlayers.get(arenaName) - currentPlayers;

				Main.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.missingPlayersToStart").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%missingplayers%", missingPlayers+""));
			}
		}, Main.getInstance().getConfig().getInt("Config.lobbyIdleCountdownLenght") * 20, Main.getInstance().getConfig().getInt("Config.lobbyIdleCountdownLenght") * 20));
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
	
	public int getSeconds(String arenaName) {
		return seconds.get(arenaName);
	}
	
	public static HashMap<String, Integer> getSecondsMap() {
		return seconds;
	}
	
	public void setSeconds(int seconds, String arenaName) {
		LobbyCountdown.seconds.put(arenaName,  seconds);
	}
}
