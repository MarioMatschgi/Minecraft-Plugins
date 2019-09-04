package at.mario.utilreloaded;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import at.mario.utilreloaded.commands.DayCMD;
import at.mario.utilreloaded.commands.DupeItemCMD;
import at.mario.utilreloaded.commands.DupeItemTabComleter;
import at.mario.utilreloaded.commands.EnderchestCMD;
import at.mario.utilreloaded.commands.FlyCMD;
import at.mario.utilreloaded.commands.FlyTabComleter;
import at.mario.utilreloaded.commands.GamemodeCMD;
import at.mario.utilreloaded.commands.GamemodeTabComleter;
import at.mario.utilreloaded.commands.NightCMD;
import at.mario.utilreloaded.commands.NightvisionCMD;
import at.mario.utilreloaded.commands.NightvisionTabComleter;
import at.mario.utilreloaded.commands.PingCMD;
import at.mario.utilreloaded.commands.RepairCMD;
import at.mario.utilreloaded.commands.VanishCMD;
import at.mario.utilreloaded.listener.InventoryCloseListener;
import at.mario.utilreloaded.listener.JoinListener;
import at.mario.utilreloaded.listener.QuitListener;

public class Main extends JavaPlugin implements Listener {

	private static Main instance = null;
	
	public static Main getInstance() {
		return instance;
	}
	public static Main getPlugin() {
		return instance;
	}

	public static HashMap<Player, Boolean> flyData;

	public File DataConfigFile = new File(this.getDataFolder(), "data.yml");
	public FileConfiguration DataConfig = YamlConfiguration.loadConfiguration(DataConfigFile);
	
	public File MessagesEnglishfile = new File(this.getDataFolder(), "messagesEnglish.yml");
	public FileConfiguration messagesEnglish = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	public File MessagesGermanfile = new File(this.getDataFolder(), "messagesGerman.yml");
	public FileConfiguration messagesGerman = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	
	public void onEnable() {
		instance = this;
		
        setUpConfigs();
        
		getCommand("gm").setExecutor(new GamemodeCMD());
		getCommand("gm").setTabCompleter(new GamemodeTabComleter());
		getCommand("fly").setExecutor(new FlyCMD());
		getCommand("fly").setTabCompleter(new FlyTabComleter());
		getCommand("dupeitem").setExecutor(new DupeItemCMD());
		getCommand("dupeitem").setTabCompleter(new DupeItemTabComleter());
		getCommand("vanish").setExecutor(new VanishCMD());
		getCommand("enderchest").setExecutor(new EnderchestCMD());
		getCommand("nightvision").setExecutor(new NightvisionCMD());
		getCommand("nightvision").setTabCompleter(new NightvisionTabComleter());
		getCommand("repair").setExecutor(new RepairCMD());
		getCommand("day").setExecutor(new DayCMD());
		getCommand("night").setExecutor(new NightCMD());
		getCommand("ping").setExecutor(new PingCMD());

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new JoinListener(), this);
		pm.registerEvents(new QuitListener(), this);
		pm.registerEvents(new InventoryCloseListener(), this);
		
		flyData = new HashMap<Player, Boolean>();
		
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eUtil reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eUtil reloaded");
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
