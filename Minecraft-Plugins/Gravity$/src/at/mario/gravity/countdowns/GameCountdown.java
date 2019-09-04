package at.mario.gravity.countdowns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import at.mario.gravity.Main;
import at.mario.gravity.gamestates.GameState;
import at.mario.gravity.gamestates.IngameState;
import at.mario.gravity.listener.SignChangeListener;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.scoreboards.FinishedScoreboard;
import at.mario.gravity.scoreboards.GameScoreboard;
import at.mario.gravity.utils.ItemsUtil;
import at.mario.gravity.utils.PlayerUtil;

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
		DataManager dm = new DataManager();
		
		IngameState.gameStarts.put(arenaName, System.currentTimeMillis());
		
		List<ConfigurationSection> usedMaps = IngameState.ArenaUsedMaps.get(arenaName);

		for (ConfigurationSection configurationSection : usedMaps) {
			for (int x = smallerX(configurationSection.getName()).intValue(); x <= biggerX(configurationSection.getName()).intValue(); x++) {
			    for (int y = smallerY(configurationSection.getName()).intValue(); y <= biggerY(configurationSection.getName()).intValue(); y++) {
			        for (int z = smallerZ(configurationSection.getName()).intValue(); z <= biggerZ(configurationSection.getName()).intValue(); z++) {
			            Location loc = new Location(Bukkit.getWorld(dm.getData().getString("Data.maps." + configurationSection.getName() + ".world")), x, y, z);
			            loc.getBlock().setType(Material.AIR);
			        }
			    }
			}
		}
		
		
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
				
				List<Player> fallers = new ArrayList<Player>();
				for (int i = 0; i < players.size(); i++) {
					Player player = players.get(i);
					if (!IngameState.ArenaFinishedPlayers.containsKey(arenaName) || IngameState.ArenaFinishedPlayers.get(arenaName) == null || !IngameState.ArenaFinishedPlayers.get(arenaName).containsKey(player)) {
						fallers.add(player);
					}
				}
				
				if (players.size() == 0 || fallers.size() == 0 || seconds.get(arenaName) == 0) {
					// Game over
					
					cancel(arenaName);
					Main.getInstance().getGameStateManager().setGameState(GameState.ENDING_STATE, arenaName);
					SignChangeListener.updateSigns(arenaName);
					return;
				}
				
				for (int i = 0; i < players.size(); i++) {
					Player player = players.get(i);

					if (IngameState.ArenaFinishedPlayers.containsKey(arenaName) && IngameState.ArenaFinishedPlayers.get(arenaName) != null && IngameState.ArenaFinishedPlayers.get(arenaName).containsKey(player) && 
							IngameState.ArenaFinishedPlayers.get(arenaName).get(player) != null) {
						// Spieler fertig
						
						FinishedScoreboard.setScoreboard(arenaName, player);
					} else {
						// Spieler nicht fertig
						
						GameScoreboard.setScoreboard(arenaName, player);
					}
					PlayerUtil.sendIngameActionbar(arenaName, player);
				}
				
				seconds.put(arenaName, seconds.get(arenaName) - 1);
			}
		}, 0, 1 * 20));
	}

	@Override
	public void cancel(String arenaName) {
		DataManager dm = new DataManager();
		
		isRunning.put(arenaName, false);
		
		Bukkit.getScheduler().cancelTask(taskIDs.get(arenaName));
		
		List<ConfigurationSection> usedMaps = IngameState.ArenaUsedMaps.get(arenaName);

		for (ConfigurationSection configurationSection : usedMaps) {
			for (int x = smallerX(configurationSection.getName()).intValue(); x <= biggerX(configurationSection.getName()).intValue(); x++) {
			    for (int y = smallerY(configurationSection.getName()).intValue(); y <= biggerY(configurationSection.getName()).intValue(); y++) {
			        for (int z = smallerZ(configurationSection.getName()).intValue(); z <= biggerZ(configurationSection.getName()).intValue(); z++) {
			            Location loc = new Location(Bukkit.getWorld(dm.getData().getString("Data.maps." + configurationSection.getName() + ".world")), x, y, z);
			            loc.getBlock().setType(Material.GLASS);
			        }
			    }
			}
		}
		
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
	
	public static Double smallerX(String mapName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.z"));

		if (loc1.getX() < loc2.getX()) {
			return loc1.getX();
		} else {
			return loc2.getX();
		}
	}
	
	public static Double smallerY(String mapName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.z"));
		
		if (loc1.getY() < loc2.getY()) {
			return loc1.getY();
		} else {
			return loc2.getY();
		}
	}
	
	public static Double smallerZ(String mapName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.z"));
		
		if (loc1.getZ() < loc2.getZ()) {
			return loc1.getZ();
		} else {
			return loc2.getZ();
		}
	}
	
	public static Double biggerX(String mapName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.z"));
		
		if (loc1.getX() > loc2.getX()) {
			return loc1.getX();
		} else {
			return loc2.getX();
		}
	}
	
	public static Double biggerY(String mapName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.z"));
		
		if (loc1.getY() > loc2.getY()) {
			return loc1.getY();
		} else {
			return loc2.getY();
		}
	}
	
	public static Double biggerZ(String mapName) {
		DataManager dm = new DataManager();
		
		Location loc1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc1.world")));
		loc1.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.pitch")));
		loc1.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc1.yaw")));
		loc1.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.x"));
		loc1.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.y"));
		loc1.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc1.z"));

		Location loc2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		loc2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + mapName + ".bounds.loc2.world")));
		loc2.setPitch((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.pitch")));
		loc2.setYaw((float) ((double) dm.getData().get("Data.maps." + mapName + ".bounds.loc2.yaw")));
		loc2.setX(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.x"));
		loc2.setY(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.y"));
		loc2.setZ(dm.getData().getDouble("Data.maps." + mapName + ".bounds.loc2.z"));
		
		if (loc1.getZ() > loc2.getZ()) {
			return loc1.getZ();
		} else {
			return loc2.getZ();
		}
	}
}
