package at.mario.testplugin;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import at.mario.testplugin.commands.AddtpCMD;
import at.mario.testplugin.commands.HealCMD;
import at.mario.testplugin.commands.InventoryCMD;
import at.mario.testplugin.commands.RemovetpCMD;
import at.mario.testplugin.listener.InventoryClickListener;
import at.mario.testplugin.listener.PlayerJoinListener;

public class Main extends JavaPlugin {

	private static Main instance = null;

	public static Main getInstance() {
		return instance;
	}
	public static Main getPlugin() {
		return instance;
	}
	// asdsad
	/*
	 * Integer int = 1 oder 125348249367
	 * Double = 12345.1343651255465
	 * 
	 * String = "Hiasidjllda"
	 * 
	 * Boolean bool = true oder false
	 * 
	 * 
	 */
	
	public static ArrayList<String> itemNames = new ArrayList<String>();
	

	public File DataConfigFile = new File(this.getDataFolder(), "data.yml");
	public FileConfiguration DataConfig = YamlConfiguration.loadConfiguration(DataConfigFile);
	
	
	
	public String text = "Cool";
	public static final String PREFIX = "§f[§bSystem§f] ";
	
	public void onEnable() {
		instance = this;

		
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new PlayerJoinListener(), this);
		pm.registerEvents(new InventoryClickListener(), this);

		
		getCommand("heal").setExecutor(new HealCMD());
		getCommand("addtp").setExecutor(new AddtpCMD());
		getCommand("removetp").setExecutor(new RemovetpCMD());
		getCommand("inventory").setExecutor(new InventoryCMD());
		
		setupConfigs();
		
		
		// Füge Items hinzu
		// itemNames.add(ITEMNAME);
		itemNames.add("Test");
		itemNames.add("Test2");
		// etc.
		// Items mit diesen Namen können NICHT gedropped und NICHT im Inventar verschoben werden!
		
		
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eTestPlugin");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");

        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage("COOL: " + istCool());
        Bukkit.getConsoleSender().sendMessage(text);
	}
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eTestPlugin");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §cdeactivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	
	public void setupConfigs() {
		if (!new File(getDataFolder(), "data.yml").exists()) {
			saveResource("data.yml", false);
		}
		DataConfig = YamlConfiguration.loadConfiguration(DataConfigFile);
	}
	public FileConfiguration getDataConfig() {
		return DataConfig;
	}
	public File getDataFile() {
		File DataConfigFile = new File(this.getDataFolder(), "data.yml");
		return DataConfigFile;
	}
	
	
	private Boolean istCool() {
		if (text == "Cool") {
			return true;
		} else if (text == "Gar nichts") {
			text = "";
			return false;
		} else {
			return false;
		}
	}
}
