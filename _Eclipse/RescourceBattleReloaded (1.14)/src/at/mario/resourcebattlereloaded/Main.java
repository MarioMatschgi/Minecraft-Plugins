package at.mario.resourcebattlereloaded;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import at.mario.resourcebattlereloaded.commands.ResourceBattleCMD;
import at.mario.resourcebattlereloaded.commands.ResourceBattleTabComleter;
import at.mario.resourcebattlereloaded.manager.ConfigManagers.DataManager;

public class Main extends JavaPlugin implements Listener {

	private static Main instance = null;
	
	public static Main getInstance() {
		return instance;
	}
	public static Main getPlugin() {
		return instance;
	}

	public static HashMap<Player, Integer> playerScores;

	public File DataConfigFile = new File(this.getDataFolder(), "data.yml");
	public FileConfiguration DataConfig = YamlConfiguration.loadConfiguration(DataConfigFile);

	public File ScoreboardConfigFile = new File(this.getDataFolder(), "scoreboard.yml");
	public FileConfiguration ScoreboardConfig = YamlConfiguration.loadConfiguration(ScoreboardConfigFile);
	
	public File MessagesEnglishfile = new File(this.getDataFolder(), "messagesEnglish.yml");
	public FileConfiguration messagesEnglish = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	public File MessagesGermanfile = new File(this.getDataFolder(), "messagesGerman.yml");
	public FileConfiguration messagesGerman = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	
	public void onEnable() {
		instance = this;
		
        setUpConfigs();
        
		getCommand("resourcebattle").setExecutor(new ResourceBattleCMD());
		getCommand("resourcebattle").setTabCompleter(new ResourceBattleTabComleter());

		//PluginManager pm = Bukkit.getPluginManager();
		//pm.registerEvents(new JoinListener(), this);
		
		playerScores = new HashMap<Player, Integer>();
		
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eResourcebattle reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eResourcebattle reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §4deaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	
	
	
	public static void UpdateScore(Player player) {
		int total_score = 0;
		
		Inventory inv = player.getInventory();
		HashMap<Material, Integer> items = GetItemHashMap();
		for (ItemStack itemStack : inv) {
			if (items.containsKey(itemStack.getType())) {
				total_score += itemStack.getAmount() * items.get(itemStack.getType());
			}
		}
		
		playerScores.put(player, total_score);
	}
	
	
	
	public static void AddItem(ItemStack item, int points) {
		DataManager dm = new DataManager();
		
		dm.getData().get("Data.items");
		
		HashMap<Material, Integer> items = GetItemHashMap();
		items.put(item.getType(), points);
		SetItemHashMap(items);
	}
	public static void RemoveItem(ItemStack item) {
		DataManager dm = new DataManager();
		
		dm.getData().get("Data.items");
		
		HashMap<Material, Integer> items = GetItemHashMap();
		items.remove(item.getType());
		SetItemHashMap(items);
	}
	@SuppressWarnings("unchecked")
	public static HashMap<Material, Integer> GetItemHashMap() {
		DataManager dm = new DataManager();
		
		HashMap<Material, Integer> items = new HashMap<Material, Integer>();
		if (dm.getData().get("Data.items.keys") != null && dm.getData().get("Data.items.values") != null) {
			List<Material> materials = (List<Material>) dm.getData().get("Data.items.keys");
			List<Integer> points = (List<Integer>) dm.getData().get("Data.items.values");
			
			for (int i = 0; i < Math.min(materials.size(), points.size()); i++)
				items.put(materials.get(i), points.get(i));
		}
		
		return items;
	}
	public static void SetItemHashMap(HashMap<Material, Integer> _items) {
		DataManager dm = new DataManager();

		Bukkit.broadcastMessage("SAVE");
		List<Material> materials = new ArrayList<Material>();
		List<Integer> points = new ArrayList<Integer>();
		for (Entry<Material, Integer> entry : _items.entrySet()) {
			Material key = entry.getKey();
		    Integer value = entry.getValue();
		    
		    if (key == null || value == null)
		    	continue;
			
			materials.add(key);
			points.add(value);
		}
		
		dm.getData().set("Data.items.keys", materials);
		dm.getData().set("Data.items.values", points);
		
		dm.saveData();
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
		
		if (!new File(getDataFolder(), "scoreboard.yml").exists()) {
			saveResource("scoreboard.yml", false);			
		}
		ScoreboardConfig = YamlConfiguration.loadConfiguration(ScoreboardConfigFile);

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
	public FileConfiguration getScoreboardConfig() {
		return ScoreboardConfig;
	}
	public File getScoreboardFile() {
		File ScoreboardConfigFile = new File(this.getDataFolder(), "scoreboard.yml");
		return ScoreboardConfigFile;
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
