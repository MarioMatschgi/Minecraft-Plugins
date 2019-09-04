package at.mario.gravity.manager;

import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import at.mario.gravity.Main;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;

public class StatsManager {
	public boolean DataExists = false;

	public static FileConfiguration getStats() {
		return Main.getInstance().getStatsConfig();
	}

	public static void saveStats() {
		try {
			getStats().save(Main.getInstance().getStatsFile());
		} catch (IOException e) { 		}
	}

	public static void reloadStats() {
		try {
			getStats().save(Main.getInstance().getStatsFile());
		} catch (IOException e) { 		}
	}
	
	public static ConfigurationSection getStats(Player p) {
		return getStats().getConfigurationSection("Stats." + p.getName());
	}
	
	public static Boolean hasStats(Player p) {
		Boolean hasStats = true;
		try {
			if (getStats(p) == null) {
				hasStats = false;
			}
		} catch (Exception e) {
			hasStats = false;
		}
		return hasStats;
	}
	
	public static void setStats(Player p, int wins, int loses, int gamesPlayed, int fails) {
		getStats().set("Stats." + p.getName() + ".wins", wins);
		getStats().set("Stats." + p.getName() + ".loses", loses);
		getStats().set("Stats." + p.getName() + ".gamesPlayed", gamesPlayed);
		getStats().set("Stats." + p.getName() + ".fails", fails);
		
		saveStats();
	}
	
	public static void setStat(Player p, String path, Object stat) {
		getStats().set("Stats." + p.getName() + "." + path, stat);
		
		saveStats();
	}
	
	public static void sendStats(String statsOf, Player p) {
		MessagesManager mm = new MessagesManager();
		
		List<String> list = mm.getMessages().getStringList("Messages.stats.format");
		
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);
			
			int wins = getStats().getInt("Stats." + p.getName() + ".wins");
			int loses = getStats().getInt("Stats." + p.getName() + ".loses");
			int gamesPlayed = getStats().getInt("Stats." + p.getName() + ".gamesPlayed");
			int fails = getStats().getInt("Stats." + p.getName() + ".fails");
			
			p.sendMessage(string.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%statsOf%", statsOf).replace("%wins%", wins+"").replace("%loses%", loses+"").replace("%gamesplayed%", gamesPlayed+"").
					replace("%fails%", fails+""));
		}
	}
}
