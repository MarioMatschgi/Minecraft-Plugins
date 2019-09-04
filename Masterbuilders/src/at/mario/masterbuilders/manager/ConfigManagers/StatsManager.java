package at.mario.masterbuilders.manager.ConfigManagers;

import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import at.mario.masterbuilders.Main;

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
	
	public static void setStats(Player p, int wins, int loses, int wasSeeker, int wasHider, int gotFounded, int playersFound) {
		getStats().set("Stats." + p.getName() + ".wins", wins);
		getStats().set("Stats." + p.getName() + ".loses", loses);
		getStats().set("Stats." + p.getName() + ".wasHider", wasHider);
		getStats().set("Stats." + p.getName() + ".wasSeeker", wasSeeker);
		getStats().set("Stats." + p.getName() + ".gotFounded", gotFounded);
		getStats().set("Stats." + p.getName() + ".playersFound", playersFound);
		
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
			int wasHider = getStats().getInt("Stats." + p.getName() + ".wasHider");
			int wasSeeker = getStats().getInt("Stats." + p.getName() + ".wasSeeker");
			int gotFounded = getStats().getInt("Stats." + p.getName() + ".gotFounded");
			int playersFound = getStats().getInt("Stats." + p.getName() + ".playersFound");
			
			p.sendMessage(string.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%statsOf%", statsOf).replace("%wins%", wins+"").replace("%loses%", loses+"").replace("%washider%", wasHider+"").replace("%wasseeker%", wasSeeker+"")
					.replace("%gotfounded%", gotFounded+"").replace("%playersfound%", playersFound+""));
		}
	}
}
