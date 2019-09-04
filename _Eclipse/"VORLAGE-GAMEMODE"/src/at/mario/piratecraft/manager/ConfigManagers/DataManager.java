package at.mario.piratecraft.manager.ConfigManagers;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import at.mario.piratecraft.Main;

public class DataManager {
	// private Main plugin = Main.getPlugin(Main.class);

	public String[] Datato = {};
	public String[] Datadata = {};
	
	public boolean DataExists = false;
	
	public void setupData() {
	}

	public FileConfiguration getData() {
		return Main.getInstance().getDataConfig();
	}

	public void saveData() {
		try {
			getData().save(Main.getInstance().getDataFile());
		} catch (IOException e) { 		}
	}

	public void reloadData() {
		try {
			getData().save(Main.getInstance().getDataFile());
		} catch (IOException e) { 		}
	}
}
