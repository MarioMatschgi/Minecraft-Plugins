package at.mario.gravity.gamestates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import at.mario.gravity.Main;
import at.mario.gravity.Roles;
import at.mario.gravity.countdowns.GameCountdown;
import at.mario.gravity.countdowns.IngameLobbyCountdown;
import at.mario.gravity.listener.PlayerInteractListener;
import at.mario.gravity.manager.StatsManager;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.utils.ItemsUtil;

public class IngameState extends GameState {
	
	private IngameLobbyCountdown ingameLobbyCountdown;
	public static HashMap<String, Long> gameStarts = new HashMap<String, Long>();
	public static HashMap<String, HashMap<Player, Long> > ArenaPlayerLevelStart = new HashMap<String, HashMap<Player, Long> >();
	public static HashMap<String, HashMap<Player, Integer> > ArenaPlayerLevels = new HashMap<String, HashMap<Player, Integer> >();
	public static HashMap<String, HashMap<Player, Integer> > ArenaFinishedPlayerLevels = new HashMap<String, HashMap<Player, Integer> >();
	public static HashMap<String, HashMap<Player, Long> > ArenaFinishedPlayers = new HashMap<String, HashMap<Player, Long> >();
	public static HashMap<String, HashMap<Player, Integer> > ArenaPlayerFails = new HashMap<String, HashMap<Player, Integer> >();
	public static HashMap<String, HashMap<Player, Integer> > ArenaPlayerREALFails = new HashMap<String, HashMap<Player, Integer> >();
	public static HashMap<String, List<ConfigurationSection> > ArenaUsedMaps = new HashMap<String,List<ConfigurationSection> >();
	
	@Override
	public void start(String arena) {
		DataManager dm = new DataManager();
		
		List<Player> players = Main.ArenaPlayer.get(arena);
		
		HashMap<Player, Integer> ArenaPlayerFailsMap = new HashMap<Player, Integer>();
		HashMap<Player, Integer> ArenaPlayerREALFailsMap = new HashMap<Player, Integer>();
		HashMap<Player, Long> ArenaPlayerLevelStartMap = new HashMap<Player, Long>();
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			HashMap<Player, Integer> ArenaPlayerLevelsMap = new HashMap<Player, Integer>();
			if (ArenaPlayerLevels != null && ArenaPlayerLevels.get(arena) != null) {
				ArenaPlayerLevelsMap = ArenaPlayerLevels.get(arena);
			}
			ArenaPlayerLevelsMap.put(player, 1);
			ArenaPlayerLevels.put(arena, ArenaPlayerLevelsMap);
			
			ArenaPlayerFailsMap.put(player, 0);
			ArenaPlayerREALFailsMap.put(player, 0);
			ArenaPlayerLevelStartMap.put(player, System.currentTimeMillis());
			
			if (Main.getInstance().getConfig().get("Config.gamemode.inGame") instanceof String) {
				if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("survival")) {
					player.setGameMode(GameMode.SURVIVAL);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("creative")) {
					player.setGameMode(GameMode.CREATIVE);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("adventure")) {
					player.setGameMode(GameMode.ADVENTURE);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("spectator")) {
					player.setGameMode(GameMode.SPECTATOR);
				}
			} else {
				if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 0) {
					player.setGameMode(GameMode.SURVIVAL);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 1) {
					player.setGameMode(GameMode.CREATIVE);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 2) {
					player.setGameMode(GameMode.ADVENTURE);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 3) {
					player.setGameMode(GameMode.SPECTATOR);
				}
			}

			PlayerInteractListener.playerCanUseVisibility.put(player, 0);
			ItemsUtil.removeItems(player);
		}
		ArenaPlayerFails.put(arena, ArenaPlayerFailsMap);
		ArenaPlayerREALFails.put(arena, ArenaPlayerREALFailsMap);
		ArenaPlayerLevelStart.put(arena, ArenaPlayerLevelStartMap);
		

		List<ConfigurationSection> mapsGetUsed = new ArrayList<ConfigurationSection>();
		
		/*
		HashMap<List<ConfigurationSection>, Integer> allMaps = new HashMap<List<ConfigurationSection>, Integer>();
		if (LobbyState.ArenaVotes.containsKey(arena) && LobbyState.ArenaVotes.get(arena) != null) {
			allMaps = LobbyState.ArenaVotes.get(arena);
		}
		
		int[] votes = new int[Main.getInstance().getConfig().getInt("Config.levelAmount")];
		int highestVote = 0;
		
		int temp = 0;
		for (Entry<List<ConfigurationSection>, Integer> entry : allMaps.entrySet()) {
		    // List<ConfigurationSection> key = entry.getKey();
		    Integer value = entry.getValue();
		    if (temp >= Main.getInstance().getConfig().getInt("Config.levelAmount")) {
		    	break;
		    }
		    votes[temp] = value;
		    if (temp >= Main.getInstance().getConfig().getInt("Config.levelAmount")) {
		    	break;
		    } else {
			    temp++;
		    }
		}
		
		for (int counter = 1; counter < votes.length; counter++) {
			if (votes[counter] > highestVote) {
				highestVote = votes[counter];
			}
		}
		List<ConfigurationSection> votedMaps = new ArrayList<ConfigurationSection>();
		for (Entry<List<ConfigurationSection>, Integer> entry : allMaps.entrySet()) {
		    List<ConfigurationSection> key = entry.getKey();
		    Integer value = entry.getValue();
		    
		    if (value == highestVote) {
		    	votedMaps = key;
		    	break;
		    }
		}*/
		
		
		HashMap<List<ConfigurationSection>, Integer> allMaps = LobbyState.ArenaVotes.get(arena);
		int[] votes = {0, 0, 0, 0, 0};
		int highestVote = 0;
		
		int temp = 0;
		for (Entry<List<ConfigurationSection>, Integer> entry : allMaps.entrySet()) {
		    // List<ConfigurationSection> key = entry.getKey();
		    Integer value = entry.getValue();
		    
		    votes[temp] = value;
		    temp++;
		}
		
		for (int counter = 1; counter < votes.length; counter++) {
			if (votes[counter] > highestVote) {
				highestVote = votes[counter];
			}
		}
		List<ConfigurationSection> votedMaps = new ArrayList<ConfigurationSection>();
		for (Entry<List<ConfigurationSection>, Integer> entry : allMaps.entrySet()) {
		    List<ConfigurationSection> key2 = entry.getKey();
		    Integer value = entry.getValue();
		    
		    if (value == highestVote) {
		    	votedMaps = key2;
		    }
		}
		if (votedMaps != null && !votedMaps.isEmpty()) {
			mapsGetUsed = votedMaps;
		}
		ArenaUsedMaps.put(arena, mapsGetUsed);
		
		Location loc = new Location(Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0, 0.0F, 0.0F);
		loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + ArenaUsedMaps.get(arena).get(0).getName() + ".spawn.world")));
		loc.setPitch((float) ((double) dm.getData().get("Data.maps." + ArenaUsedMaps.get(arena).get(0).getName() + ".spawn.pitch")));
		loc.setYaw((float) ((double) dm.getData().get("Data.maps." + ArenaUsedMaps.get(arena).get(0).getName() + ".spawn.yaw")));
		loc.setX(dm.getData().getDouble("Data.maps." + ArenaUsedMaps.get(arena).get(0).getName() + ".spawn.x"));
		loc.setY(dm.getData().getDouble("Data.maps." + ArenaUsedMaps.get(arena).get(0).getName() + ".spawn.y"));
		loc.setZ(dm.getData().getDouble("Data.maps." + ArenaUsedMaps.get(arena).get(0).getName() + ".spawn.z"));
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			player.teleport(loc);
			Roles.roles.put(player, Roles.FALLER);
			StatsManager.setStat(player, "gamesPlayed", StatsManager.getStats(player).getInt("gamesPlayed") + 1);
		}
		
		ingameLobbyCountdown = new IngameLobbyCountdown();
		ingameLobbyCountdown.run(arena);
	}
	
	@Override
	public void stop(String arena) {
		if (ingameLobbyCountdown.gamecountdown.isRunning(arena)) {
			ingameLobbyCountdown.gamecountdown.cancel(arena);
		}
	}
	
	public GameCountdown getGamecountdown() {
		return ingameLobbyCountdown.gamecountdown;
	}
}
