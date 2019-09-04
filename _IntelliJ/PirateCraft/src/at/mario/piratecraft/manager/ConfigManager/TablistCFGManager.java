package at.mario.piratecraft.manager.ConfigManager;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import at.mario.piratecraft.Main;

public class TablistCFGManager {
	
	public FileConfiguration getTablistCFG() {
		return Main.getInstance().getTablistConfig();
	}
	
	public void reloadTablistCFG() {
		Main.getInstance().tablistConfig = YamlConfiguration.loadConfiguration(Main.getInstance().tablistConfigfile);
	}

	public void saveTablistCFG() {
		try {
			getTablistCFG().save(Main.getInstance().getTablistFile());
		} catch (IOException e) { 		}
	}
}