package at.mario.economy.Manager.ConfigManagers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import at.mario.economy.Main;
import at.mario.economy.Manager.CFGM;

public class DataManager {
	private Main plugin = Main.getPlugin(Main.class);
	private CFGM cfgm;

	public String[] Datato = {};
	public String[] Datadata = {};
	
	public boolean DataExists = false;
	
	public static FileConfiguration datacfg;
	public static File datafile;
	
	public void setupData() {
		if (plugin.getDataFolder().exists() == false) {
			plugin.getDataFolder().mkdir();
		}
		datafile = new File(plugin.getDataFolder(), "data.yml");
		if (!datafile.exists()) {
			try {
				datafile.createNewFile();
				
				datacfg = YamlConfiguration.loadConfiguration(datafile);
				cfgm = new CFGM();
				
				for (int i = 0; i < Datadata.length; i++) {
					cfgm.DefaultConfig(Datato[i] + "", Datadata[i] + "");
				}
				DataExists = true;
				Bukkit.getServer().getConsoleSender().sendMessage("§cData status: §aThe data.yml file has been created");
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage("§cData status: Could not create the data.yml file");
			}
		}
		datacfg = YamlConfiguration.loadConfiguration(datafile);
	}

	public FileConfiguration getData() {
		return datacfg;
	}

	public void saveData() {
		try {
			datacfg.save(datafile);
			Bukkit.getServer().getConsoleSender().sendMessage("§cData status: §aThe data.yml file has been saved");
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage("§cData status: Could not save the data.yml file");
		}
	}

	public void reloadData() {
		datacfg = YamlConfiguration.loadConfiguration(datafile);
		Bukkit.getServer().getConsoleSender().sendMessage("§cData status: §aThe data.yml file has been reload");
	}
}
