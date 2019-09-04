package at.mario.lieferservice;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import at.mario.lieferservice.commands.OrderCMD;
import at.mario.lieferservice.commands.OrderTabComleter;
import at.mario.lieferservice.commands.RequestCMD;
import at.mario.lieferservice.listener.InventoryClickListener;
import at.mario.lieferservice.listener.PlayerJoinListener;
import at.mario.lieferservice.manager.ConfigManagers.DataManager;

public class Main extends JavaPlugin implements Listener {

	private static Main instance = null;
	
	public static Main getInstance() {
		return instance;
	}
	public static Main getPlugin() {
		return instance;
	}

	public static ArrayList<Request> requests;

	public File DataConfigFile = new File(this.getDataFolder(), "data.yml");
	public FileConfiguration DataConfig = YamlConfiguration.loadConfiguration(DataConfigFile);
	
	public File MessagesEnglishfile = new File(this.getDataFolder(), "messagesEnglish.yml");
	public FileConfiguration messagesEnglish = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	public File MessagesGermanfile = new File(this.getDataFolder(), "messagesGerman.yml");
	public FileConfiguration messagesGerman = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	
	@SuppressWarnings("unchecked")
	public void onEnable() {
		instance = this;
		
		ConfigurationSerialization.registerClass(Request.class, "Request");
		
        setUpConfigs();
        
		getCommand("order").setExecutor(new OrderCMD());
		getCommand("order").setTabCompleter(new OrderTabComleter());
		getCommand("requests").setExecutor(new RequestCMD());

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InventoryClickListener(), this);
		pm.registerEvents(new PlayerJoinListener(), this);
		
		DataManager dm = new DataManager();
		
		requests = new ArrayList<Request>();
		if (dm.getData().contains("Data.requests")) {
			requests = (ArrayList<Request>) dm.getData().get("Data.requests");
		} else {
			dm.getData().set("Data.requests", requests);
			dm.saveData();
		}
		
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eLieferservice reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eLieferservice reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §4deaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	
	public static Integer parseInt(String string) {
		if (isInteger(string)) {
			int i = Integer.parseInt(string);
			return i;
		}
		return null;
	}
	public static boolean isInteger(String string) {
	    try { 
	        Integer.parseInt(string); 
	    } catch (NumberFormatException e) { 
	        return false; 
	    } catch (NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	public void setUpConfigs() {
		saveDefaultConfig();

		if (!new File(getDataFolder(), "data.yml").exists()) {
			saveResource("data.yml", false);
		}
		DataConfig = YamlConfiguration.loadConfiguration(DataConfigFile);
		
		if (!new File(getDataFolder(), "messagesEnglish.yml").exists()) {
			saveResource("messagesEnglish.yml", false);			
		}
		messagesEnglish = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
		if (!new File(getDataFolder(), "messagesGerman.yml").exists()) {
			saveResource("messagesGerman.yml", false);			
		}
		messagesGerman = YamlConfiguration.loadConfiguration(MessagesGermanfile);
	}
	public FileConfiguration getDataConfig() {
		return DataConfig;
	}

	public File getDataFile() {
		File DataConfigFile = new File(this.getDataFolder(), "data.yml");
		return DataConfigFile;
	}
	public FileConfiguration getMessageConfig() {
		if (Main.getInstance().getConfig().getString("Config.language").equalsIgnoreCase("english")) {
			return messagesEnglish;
		} else if (Main.getInstance().getConfig().getString("Config.language").equalsIgnoreCase("deutsch") || Main.getInstance().getConfig().getString("Config.language").equalsIgnoreCase("German")) {
			return messagesGerman;
		}
		return messagesEnglish;
	}
	public FileConfiguration getPluginConfig() {
		return getConfig();
	}
}
