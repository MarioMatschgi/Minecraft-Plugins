package at.mario.piratecraft.manager.ConfigManagers;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import at.mario.piratecraft.Main;

public class ScoreboardCFGManager {
	
	public static FileConfiguration getScoreboardCFG() {
		return Main.getInstance().getScoreboardConfig();
	}
	
	public void reloadScoreboard() {
		Main.getInstance().ScoreboardEnglishConfig = YamlConfiguration.loadConfiguration(Main.getInstance().ScoreboardEnglishConfigFile);
	}

	public void saveScoreboardCFG() {
		try {
			getScoreboardCFG().save(Main.getInstance().getScoreboardFile());
		} catch (IOException e) { 		}
	}
}
