package at.mario.piratecraft;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import at.mario.piratecraft.commands.PiratecraftCMD;
import at.mario.piratecraft.commands.PiratecraftTabComleter;
import at.mario.piratecraft.gamestates.GameState;
import at.mario.piratecraft.gamestates.LobbyState;
import at.mario.piratecraft.listener.BlockBreakListener;
import at.mario.piratecraft.listener.BlockPlaceListener;
import at.mario.piratecraft.listener.DamageListener;
import at.mario.piratecraft.listener.DeathListener;
import at.mario.piratecraft.listener.EntityDismountListener;
import at.mario.piratecraft.listener.FoodLevelChangeListener;
import at.mario.piratecraft.listener.GamemodeListener;
import at.mario.piratecraft.listener.InventoryClickListener;
import at.mario.piratecraft.listener.InventoryCloseListener;
import at.mario.piratecraft.listener.ItemDropListener;
import at.mario.piratecraft.listener.PlayerJoinListener;
import at.mario.piratecraft.listener.MoveListener;
import at.mario.piratecraft.listener.PlayerChatListener;
import at.mario.piratecraft.listener.PlayerInteractListener;
import at.mario.piratecraft.listener.PlayerPortalListener;
import at.mario.piratecraft.listener.PreCommandListener;
import at.mario.piratecraft.listener.PlayerQuitlistener;
import at.mario.piratecraft.listener.SignChangeListener;
import at.mario.piratecraft.listener.SneakListener;
import at.mario.piratecraft.listener.TabCompleteListener;
import at.mario.piratecraft.listener.ToggleFlightListener;
import at.mario.piratecraft.manager.GameStateManager;
import at.mario.piratecraft.manager.ConfigManagers.DataManager;
import at.mario.piratecraft.pictureLogin.com.bobacadodl.imgmessage.ImageChar;
import at.mario.piratecraft.pictureLogin.com.bobacadodl.imgmessage.ImageMessage;
import at.mario.piratecraft.pictureLogin.me.itsnathang.picturelogin.util.PictureUtil;
import at.mario.piratecraft.utils.PlayerUtil;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Main extends JavaPlugin implements Listener {

	private static Main instance = null;
	
	public static Main getInstance() {
		return instance;
	}
	public static Main getPlugin() {
		return instance;
	}
	
	
	private PictureUtil pictureUtil;
	
	public static Economy eco = null;
	
	private GameStateManager gameStateManager;

	public File DataConfigFile = new File(this.getDataFolder(), "data.yml");
	public FileConfiguration DataConfig = YamlConfiguration.loadConfiguration(DataConfigFile);

	public File ScoreboardEnglishConfigFile = new File(this.getDataFolder(), "scoreboardEnglish.yml");
	public FileConfiguration ScoreboardEnglishConfig = YamlConfiguration.loadConfiguration(ScoreboardEnglishConfigFile);
	public File ScoreboardGermanConfigFile = new File(this.getDataFolder(), "scoreboardGerman.yml");
	public FileConfiguration ScoreboardGermanConfig = YamlConfiguration.loadConfiguration(ScoreboardGermanConfigFile);

	public File tablistConfigfile = new File(this.getDataFolder(), "tablist.yml");
	public FileConfiguration tablistConfig = YamlConfiguration.loadConfiguration(tablistConfigfile);

	public File MessagesEnglishfile = new File(this.getDataFolder(), "messagesEnglish.yml");
	public FileConfiguration messagesEnglish = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	public File MessagesGermanfile = new File(this.getDataFolder(), "messagesGerman.yml");
	public FileConfiguration messagesGerman = YamlConfiguration.loadConfiguration(MessagesEnglishfile);

	public File StatsConfigFile = new File(this.getDataFolder(), "stats.yml");
	public FileConfiguration StatsConfig = YamlConfiguration.loadConfiguration(StatsConfigFile);
	
	public static HashMap<String, List<Player>> ArenaPlayer;
	public static HashMap<String, List<Player>> SpectateArenaPlayer;
	
	public static HashMap<String, List<List<String> > > ArenaMaps;
	
	public void onEnable() {
		instance = this;
		
		pictureUtil = new PictureUtil(this);
		
        setUpConfigs();
        
		gameStateManager = new GameStateManager();
        
        ArenaPlayer = new HashMap<String, List<Player> >();
        ArenaMaps = new HashMap<String, List<List<String> > >();
        PlayerUtil.specTaskIDs = new HashMap<Player, Integer>();
        SpectateArenaPlayer = new HashMap<String, List<Player> >();
        
		getCommand("piratecraft").setExecutor(new PiratecraftCMD());
		getCommand("piratecraft").setTabCompleter(new PiratecraftTabComleter());
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new PlayerQuitlistener(), this);
		pm.registerEvents(new MoveListener(), this);
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new SneakListener(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new ItemDropListener(), this);
		pm.registerEvents(new GamemodeListener(), this);
		pm.registerEvents(new BlockBreakListener(), this);
		pm.registerEvents(new BlockPlaceListener(), this);
		pm.registerEvents(new SignChangeListener(), this);
		pm.registerEvents(new PreCommandListener(), this);
		pm.registerEvents(new PlayerChatListener(), this);
		pm.registerEvents(new TabCompleteListener(), this);
		pm.registerEvents(new PlayerPortalListener(), this);
		pm.registerEvents(new ToggleFlightListener(), this);
		pm.registerEvents(new InventoryClickListener(), this);
		pm.registerEvents(new PlayerInteractListener(), this);
		pm.registerEvents(new InventoryCloseListener(), this);
		pm.registerEvents(new EntityDismountListener(), this);
		pm.registerEvents(new PlayerJoinListener(getPlugin()), this);
		pm.registerEvents(new FoodLevelChangeListener(), this);
		
		
		if (setupEconomy() == true) {
			Bukkit.getConsoleSender().sendMessage("[PirateCraft reloaded] [Vault] §aLinked Vault to PirateCraft reloaded");
		} else {
			Bukkit.getConsoleSender().sendMessage("[PirateCraft reloaded] §cCould not link to Vault... Is it installed?");
		}
		
		if (getServer().getPluginManager().getPlugin("PermissionsEx") == null) {
			Bukkit.getConsoleSender().sendMessage("[PirateCraft reloaded] §cCould not link to PermissionsEx... Is it installed?");
		} else {
			Bukkit.getConsoleSender().sendMessage("[PirateCraft reloaded] [PermissionsEx] §aLinked PermissionsEx to PirateCraft reloaded");
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				//try {
					updateAllSigns();
				//} catch (Exception e) {   }
			}
		}, 20L);
		
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §ePirateCraft reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi + mrBayBayHD");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §ePirateCraft reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: §e0.1");
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi + mrBayBayHD");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §4deaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	
	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}
	
	public Boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		eco = rsp.getProvider();
		return (eco != null);
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
	
	public void updateAllSigns() {
		DataManager dm = new DataManager();
		
		LobbyState.minPlayers = new HashMap<String, Integer>();
		LobbyState.maxPlayers = new HashMap<String, Integer>();
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		if (configSection != null) {
			for (String key : configSection.getKeys(false)) {
				if (dm.getData().contains("Data.arenas." + key + ".minplayers")) {
					LobbyState.minPlayers.put(key, dm.getData().getInt("Data.arenas." + key + ".minplayers"));
				} else {
					LobbyState.minPlayers.put(key, 2);
				}
				if (dm.getData().contains("Data.arenas." + key + ".maxplayers")) {
					LobbyState.maxPlayers.put(key, dm.getData().getInt("Data.arenas." + key + ".maxplayers"));
				} else {
					LobbyState.maxPlayers.put(key, 5);
				}
				getGameStateManager().setGameState(GameState.LOBBY_STATE, key);
				SignChangeListener.updateSigns(key);
			}
		}
	}
	
	public void setUpConfigs() {
		/*
		File reloadedSeriesFolder = new File(getPlugin().getDataFolder().getParentFile() + File.separator + "reloadedPlugins");
		if (!reloadedSeriesFolder.exists()) {
			reloadedSeriesFolder.mkdirs();
		}*/
		
		
		saveDefaultConfig();
		
		if (!new File(getDataFolder(), "scoreboardEnglish.yml").exists()) {
			saveResource("scoreboardEnglish.yml", false);			
		}
		ScoreboardEnglishConfig = YamlConfiguration.loadConfiguration(ScoreboardEnglishConfigFile);
		if (!new File(getDataFolder(), "scoreboardGerman.yml").exists()) {
			saveResource("scoreboardGerman.yml", false);
		}
		ScoreboardGermanConfig = YamlConfiguration.loadConfiguration(ScoreboardGermanConfigFile);

		if (!new File(getDataFolder(), "stats.yml").exists()) {
			saveResource("stats.yml", false);			
		}
		StatsConfig = YamlConfiguration.loadConfiguration(StatsConfigFile);
		
		/*
		if (!new File(getDataFolder(), "tablist.yml").exists()) {
			saveResource("tablist.yml", false);			
		}
		tablistConfig = YamlConfiguration.loadConfiguration(tablistConfigfile);*/
		
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
	public FileConfiguration getStatsConfig() {
		return StatsConfig;
	}
	public File getStatsFile() {
		File DataStatsFile = new File(this.getDataFolder(), "stats.yml");
		return DataStatsFile;
	}
	public FileConfiguration getScoreboardConfig() {
		if (Main.getInstance().getConfig().getString("Config.language").equalsIgnoreCase("english")) {
			return ScoreboardEnglishConfig;
		} else if (Main.getInstance().getConfig().getString("Config.language").equalsIgnoreCase("deutsch") || Main.getInstance().getConfig().getString("Config.language").equalsIgnoreCase("German")) {
			return ScoreboardGermanConfig;
		}
		return ScoreboardEnglishConfig;
	}
	public File getScoreboardFile() {
		File ScoreboardConfigFile = new File(this.getDataFolder(), "scoreboard.yml");
		return ScoreboardConfigFile;
	}	
	public FileConfiguration getTablistConfig() {
		return tablistConfig;
	}
	public File getTablistFile() {
		File tablistConfigfile = new File(this.getDataFolder(), "tablist.yml");
		return tablistConfigfile;
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
	
	public static void setNoAI(Entity bukkitEntity) {
	    net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
	    
	    NBTTagCompound tag = nmsEntity.getNBTTag();
	    if (tag == null) {
	        tag = new NBTTagCompound();
	    }
	    nmsEntity.c(tag);
	    tag.setInt("NoAI", 1);
	    nmsEntity.f(tag);
	}
	
	public PictureUtil getPictureUtil() {
		return pictureUtil;
	}
	public String getURL() {
		return getConfig().getString("Config.joinme.url");
	}
	
	public ImageMessage getMessage(List<String> messages, BufferedImage image) {
		int imageDimensions = 8, count = 0;
		ImageMessage imageMessage = new ImageMessage(image, imageDimensions, getChar());
		String[] msg = new String[imageDimensions];

		for (String message : messages) {
			msg[count++] = message;
		}

		while (count < imageDimensions) {
			msg[count++] = "";
		}

		if (getConfig().getBoolean("center-text", false))
			return imageMessage.appendCenteredText(msg);

		return imageMessage.appendText(msg);
	}
	private char getChar() {
		try {
			return ImageChar.valueOf(getConfig().getString("Config.joinme.character").toUpperCase()).getChar();
		} catch (IllegalArgumentException e) {
			return ImageChar.BLOCK.getChar();
		}
	}

	public static LinkedHashMap<Player, Long> sortHashMapByValues(HashMap<Player, Long> passedMap) {
	    List<Player> mapKeys = new ArrayList<>(passedMap.keySet());
	    List<Long> mapValues = new ArrayList<>(passedMap.values());
	    Collections.sort(mapValues);

	    LinkedHashMap<Player, Long> sortedMap = new LinkedHashMap<>();

	    Iterator<Long> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Long val = valueIt.next();
	        Iterator<Player> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	            Player key = keyIt.next();
	            Long comp1 = passedMap.get(key);
	            Long comp2 = val;

	            if (comp1.equals(comp2)) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}
}
