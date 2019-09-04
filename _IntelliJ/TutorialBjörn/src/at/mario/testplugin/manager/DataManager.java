package at.mario.testplugin.manager;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import at.mario.testplugin.Main;

public class DataManager {

	public static FileConfiguration getData() {
		return Main.getInstance().getDataConfig();
	}

	public static void saveData() {
		try {
			getData().save(Main.getInstance().getDataFile());
		} catch (IOException e) { 		}
	}

	public static void reloadData() {
		try {
			getData().save(Main.getInstance().getDataFile());
		} catch (IOException e) { 		}
	}
}
