package at.mario.xpexchange;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import at.mario.xpexchange.commands.ExchangeCMD;
import at.mario.xpexchange.commands.ExchangeTabComleter;

public class Main extends JavaPlugin implements Listener {

	private static Main instance = null;
	
	public static Main getInstance() {
		return instance;
	}
	public static Main getPlugin() {
		return instance;
	}

	public File MessagesEnglishfile = new File(this.getDataFolder(), "messagesEnglish.yml");
	public FileConfiguration messagesEnglish = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	public File MessagesGermanfile = new File(this.getDataFolder(), "messagesGerman.yml");
	public FileConfiguration messagesGerman = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	
	public void onEnable() {
		instance = this;
		
        setUpConfigs();
        
		getCommand("xpexchange").setExecutor(new ExchangeCMD());
		getCommand("xpexchange").setTabCompleter(new ExchangeTabComleter());
		
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eXp-Exchange reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eXp-Exchange reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: §e0.1");
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

		if (!new File(getDataFolder(), "messagesEnglish.yml").exists()) {
			saveResource("messagesEnglish.yml", false);			
		}
		messagesEnglish = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
		if (!new File(getDataFolder(), "messagesGerman.yml").exists()) {
			saveResource("messagesGerman.yml", false);			
		}
		messagesGerman = YamlConfiguration.loadConfiguration(MessagesGermanfile);
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
