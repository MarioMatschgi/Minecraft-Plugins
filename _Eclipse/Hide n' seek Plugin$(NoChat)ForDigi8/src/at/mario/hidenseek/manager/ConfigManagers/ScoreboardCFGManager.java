package at.mario.hidenseek.manager.ConfigManagers;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import at.mario.hidenseek.Main;

public class ScoreboardCFGManager {
	
	public static FileConfiguration getScoreboardCFG() {
		return Main.getInstance().getScoreboardConfig();
	}
	
	public void reloadScoreboard() {
		Main.getInstance().ScoreboardConfig = YamlConfiguration.loadConfiguration(Main.getInstance().ScoreboardConfigFile);
	}

	public void saveScoreboardCFG() {
		try {
			getScoreboardCFG().save(Main.getInstance().getScoreboardFile());
		} catch (IOException e) { 		}
	}
}
