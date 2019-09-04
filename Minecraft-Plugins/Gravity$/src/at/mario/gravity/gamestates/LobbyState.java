package at.mario.gravity.gamestates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import at.mario.gravity.Main;
import at.mario.gravity.countdowns.LobbyCountdown;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;

public class LobbyState extends GameState {
	
	public static HashMap<String, Integer> minPlayers;
	public static HashMap<String, Integer> maxPlayers;
	public static HashMap<String, HashMap<Integer, List<ConfigurationSection> > > ArenaMaps = new HashMap<String, HashMap<Integer, List<ConfigurationSection> > >();
	public static HashMap<String, HashMap<List<ConfigurationSection>, Integer> > ArenaVotes = new HashMap<String, HashMap<List<ConfigurationSection>, Integer> >();
	public static HashMap<String, HashMap<Player, List<ConfigurationSection>> > PlayerVotes = new HashMap<String, HashMap<Player, List<ConfigurationSection>> >();
	
	
	private LobbyCountdown lobbycountdown;

	@Override
	public void start(String arena) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		Main.getInstance().getGameStateManager().setIsJoinme(arena, false);
		lobbycountdown = new LobbyCountdown();
		// lobbycountdown.idle(arena);
		
		
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.maps");
		List<ConfigurationSection> allMaps = new ArrayList<ConfigurationSection>();
		if (configSection != null) {
			for (String key : configSection.getKeys(false)) {
				allMaps.add(dm.getData().getConfigurationSection("Data.maps." + key));
			}
		}

		HashMap<Integer, List<ConfigurationSection> > usedMaps = new HashMap<Integer, List<ConfigurationSection> >();
		if (allMaps.size() != 0) {
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < Main.getInstance().getConfig().getInt("Config.levelAmount"); j++) {
					if (usedMaps.get(i) != null) {
						if (usedMaps.get(i).size() == allMaps.size()) {
							int rand = new Random().nextInt(allMaps.size());
							List<ConfigurationSection> maps = new ArrayList<ConfigurationSection>();
							if (usedMaps.get(i) != null) {
								maps = usedMaps.get(i);
							}
							maps.add(allMaps.get(rand));
							usedMaps.put(i, maps);
						}
					}
					int rand = new Random().nextInt(allMaps.size());
					if (usedMaps.get(i) == null) {
						// Map noch nicht benutzt
						
						List<ConfigurationSection> maps = new ArrayList<ConfigurationSection>();
						maps.add(allMaps.get(rand));
						usedMaps.put(i, maps);
					} else {
						do {
							rand = new Random().nextInt(allMaps.size());
						} while (usedMaps.get(i).contains(allMaps.get(rand)));
						
						List<ConfigurationSection> maps = usedMaps.get(i);
						maps.add(allMaps.get(rand));
						usedMaps.put(i, maps);
					}
				}
			}
		} else {
			Bukkit.broadcastMessage(mm.getMessages().getString("Messages.notEnoughMaps").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%maps%", allMaps.size()+"").replace("%minmaps%", 
					Main.getInstance().getConfig().getInt("Config.levelAmount")+""));
			return;
		}
		ArenaMaps.put(arena, usedMaps);
		
		HashMap<List<ConfigurationSection>, Integer> ArenaVotesMap = new HashMap<List<ConfigurationSection>, Integer>();
		for (List<ConfigurationSection> maps : usedMaps.values()) {
			ArenaVotesMap.put(maps, 0);
		}
		ArenaVotes.put(arena, ArenaVotesMap);
	}

	@Override
	public void stop(String arena) {
		if (lobbycountdown.isIdling(arena)) {
			lobbycountdown.cancelIdleing(arena);
		}
		if (lobbycountdown.isRunning(arena)) {
			lobbycountdown.cancel(arena);
		}
		Main.getInstance().getGameStateManager().setIsJoinme(arena, false);
	}
	
	public LobbyCountdown getLobbycountdown() {
		return lobbycountdown;
	}
}
