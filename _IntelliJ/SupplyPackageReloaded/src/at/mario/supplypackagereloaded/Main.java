package at.mario.supplypackagereloaded;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import at.mario.supplypackagereloaded.commands.SupplypackageCMD;
import at.mario.supplypackagereloaded.commands.SupplypackageTabCompleter;
import at.mario.supplypackagereloaded.listener.BlockBreakListener;
import at.mario.supplypackagereloaded.listener.EntityChangeBlockListener;
import at.mario.supplypackagereloaded.listener.ItemSpawnListener;
import at.mario.supplypackagereloaded.listener.PlayerInteractListener;
import at.mario.supplypackagereloaded.manager.ConfigManagers.DataManager;

public class Main extends JavaPlugin implements Listener {

	private static Main instance = null;
	
	public static Main getInstance() {
		return instance;
	}
	public static Main getPlugin() {
		return instance;
	}

	public static HashMap<Block, Player> blocks;
	public static HashMap<FallingBlock, Player> fallingBlocks;
	public static HashMap<FallingBlock, Integer> fallingBlockIDs;
	
	public File DataConfigFile = new File(this.getDataFolder(), "data.yml");
	public FileConfiguration DataConfig = YamlConfiguration.loadConfiguration(DataConfigFile);
	
	public File MessagesEnglishfile = new File(this.getDataFolder(), "messagesEnglish.yml");
	public FileConfiguration messagesEnglish = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	public File MessagesGermanfile = new File(this.getDataFolder(), "messagesGerman.yml");
	public FileConfiguration messagesGerman = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	
	public void onEnable() {
		instance = this;
		
        setUpConfigs();
        
		getCommand("supplypackage").setExecutor(new SupplypackageCMD());
			getCommand("supplypackage").setTabCompleter(new SupplypackageTabCompleter());
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new EntityChangeBlockListener(), this);
		pm.registerEvents(new ItemSpawnListener(), this);
		pm.registerEvents(new PlayerInteractListener(), this);
		pm.registerEvents(new BlockBreakListener(), this);
		
		blocks = new HashMap<Block, Player>();
		fallingBlocks = new HashMap<FallingBlock, Player>();
		fallingBlockIDs = new HashMap<FallingBlock, Integer>();
		
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eSupplypackage reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eSupplypackage reloaded");
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
	
	public static void setSupplies(HashMap<ItemStack, Integer> supplies) {
		DataManager dm = new DataManager();
		
		LinkedHashMap<ItemStack, Integer> lmap = new LinkedHashMap<ItemStack, Integer>(supplies);
		int i = 0;
		for (ItemStack item : lmap.keySet()) {
			dm.getData().set("Data.supplies.item_" + i, item);
			dm.getData().set("Data.supplies.probability_" + i, lmap.get(item));
			
			i++;
		}
		dm.saveData();
	}
	public static HashMap<ItemStack, Integer> getSupplies() {
		DataManager dm = new DataManager();
		
		LinkedHashMap<ItemStack, Integer> map = new LinkedHashMap<>();
		ConfigurationSection section = dm.getData().getConfigurationSection("Data.supplies");
		if (section != null) {
			ItemStack item = null;
			Integer probability = null;
			
			for (String key : section.getKeys(false)) {
				if (key.split("_")[0].equals("item")) 
					item = dm.getData().getItemStack("Data.supplies." + key);
				else if (key.split("_")[0].equals("probability")) 
					probability = dm.getData().getInt("Data.supplies." + key);
				
				map.put(item, probability);
			}
		}
		
		return map;
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
		/*
		if (!new File(getDataFolder(), "messagesGerman.yml").exists()) {
			saveResource("messagesGerman.yml", false);			
		}
		messagesGerman = YamlConfiguration.loadConfiguration(MessagesGermanfile);
		*/
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
