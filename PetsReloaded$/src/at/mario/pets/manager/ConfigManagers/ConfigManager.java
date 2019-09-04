package at.mario.pets.manager.ConfigManagers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import at.mario.pets.Main;
import at.mario.pets.manager.CFGM;

public class ConfigManager {
	
	private Main plugin = Main.getPlugin(Main.class);
	private CFGM cfgm;

	public String[] CFGto = {"Config.permissions.balance.add", "Config.permissions.balance.remove", "Config.permissions.balance.set"};
	public String[] CFGdata = {"hs.balance.add", "hs.balance.remove", "hs.balance.set"};
	
	public boolean ConfigExists = false;
	
	public static FileConfiguration configcfg;
	public static File configfile;
	
	public void setupConfig() {
		if (plugin.getDataFolder().exists() == false) {
			plugin.getDataFolder().mkdir();
		}
		configfile = new File(plugin.getDataFolder(), "config.yml");
		if (!configfile.exists()) {
			try {
				configfile.createNewFile();
				
				configcfg = YamlConfiguration.loadConfiguration(configfile);
				cfgm = new CFGM();
				
				for (int i = 0; i < CFGdata.length; i++) {
					cfgm.DefaultConfig(CFGto[i] + "", CFGdata[i] + "");
				}
				ConfigExists = true;
				Bukkit.getServer().getConsoleSender().sendMessage("§cConfig status: §aThe config.yml file has been created");
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage("§cMessage status: Could not create the config.yml file");
			}
		}
		configcfg = YamlConfiguration.loadConfiguration(configfile);
	}

	public FileConfiguration getConfig() {
		return configcfg;
	}

	public void saveConfig() {
		try {
			configcfg.save(configfile);
			Bukkit.getServer().getConsoleSender().sendMessage("§cConfig status: §aThe config.yml file has been saved");
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage("§cConfig status: Could not save the config.yml file");
		}
	}

	public void reloadConfig() {
		configcfg = YamlConfiguration.loadConfiguration(configfile);
		Bukkit.getServer().getConsoleSender().sendMessage("§cConfig status: §aThe config.yml file has been reload");
	}
}