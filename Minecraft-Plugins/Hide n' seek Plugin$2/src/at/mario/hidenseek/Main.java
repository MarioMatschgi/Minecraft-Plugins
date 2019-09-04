package at.mario.hidenseek;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import at.mario.hidenseek.commands.HideNSeekCMD;
import at.mario.hidenseek.commands.HideNSeekTabComleter;
import at.mario.hidenseek.listener.BlockBreakListener;
import at.mario.hidenseek.listener.BlockPlaceListener;
import at.mario.hidenseek.listener.DamageListener;
import at.mario.hidenseek.listener.EntityDismountListener;
import at.mario.hidenseek.listener.FoodLevelChangeListener;
import at.mario.hidenseek.listener.GamemodeListener;
import at.mario.hidenseek.listener.InventoryClickListener;
import at.mario.hidenseek.listener.InventoryCloseListener;
import at.mario.hidenseek.listener.ItemDropListener;
import at.mario.hidenseek.listener.JoinListener;
import at.mario.hidenseek.listener.MoveListener;
import at.mario.hidenseek.listener.PlayerInteractListener;
import at.mario.hidenseek.listener.PreCommandListener;
import at.mario.hidenseek.listener.Quitlistener;
import at.mario.hidenseek.listener.SignChangeListener;
import at.mario.hidenseek.listener.SneakListener;
import at.mario.hidenseek.listener.ToggleFlightListener;
import at.mario.hidenseek.manager.GameStateManager;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Main extends JavaPlugin implements Listener {

	private static Main instance = null;
	
	public static Main getInstance() {
		return instance;
	}
	
	public static Economy eco = null;
	
	private GameStateManager gameStateManager;

	public File DataConfigFile = new File(this.getDataFolder(), "data.yml");
	public FileConfiguration DataConfig = YamlConfiguration.loadConfiguration(DataConfigFile);

	public File ScoreboardConfigFile = new File(this.getDataFolder(), "scoreboard.yml");
	public FileConfiguration ScoreboardConfig = YamlConfiguration.loadConfiguration(ScoreboardConfigFile);

	public File tablistConfigfile = new File(this.getDataFolder(), "tablist.yml");
	public FileConfiguration tablistConfig = YamlConfiguration.loadConfiguration(tablistConfigfile);

	public File MessagesEnglishfile = new File(this.getDataFolder(), "messagesEnglish.yml");
	public FileConfiguration messagesEnglish = YamlConfiguration.loadConfiguration(MessagesEnglishfile);

	public File MessagesGermanfile = new File(this.getDataFolder(), "messagesGerman.yml");
	public FileConfiguration messagesGerman = YamlConfiguration.loadConfiguration(MessagesEnglishfile);
	
	public static HashMap<String, Player> ArenaPlayer = new HashMap<String, Player>();
	
	public void onEnable() {
		instance = this;
		
		gameStateManager = new GameStateManager();
		
        setUpConfigs();
		
		getCommand("hidenseek").setExecutor(new HideNSeekCMD());
		getCommand("hidenseek").setTabCompleter(new HideNSeekTabComleter());
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new InventoryClickListener(), this);
		pm.registerEvents(new Quitlistener(), this);
		pm.registerEvents(new JoinListener(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new PlayerInteractListener(), this);
		pm.registerEvents(new MoveListener(), this);
		pm.registerEvents(new SneakListener(), this);
		pm.registerEvents(new BlockBreakListener(), this);
		pm.registerEvents(new BlockPlaceListener(), this);
		pm.registerEvents(new FoodLevelChangeListener(), this);
		pm.registerEvents(new ItemDropListener(), this);
		pm.registerEvents(new GamemodeListener(), this);
		pm.registerEvents(new InventoryCloseListener(), this);
		pm.registerEvents(new ToggleFlightListener(), this);
		pm.registerEvents(new EntityDismountListener(), this);
		pm.registerEvents(new SignChangeListener(), this);
		pm.registerEvents(new PreCommandListener(), this);
		
		if (setupEconomy() == false) {
			Bukkit.getConsoleSender().sendMessage("[Hide n' seek reloaded] §cCould not link to Vault... Is it installed?");
		} else {
			Bukkit.getConsoleSender().sendMessage("[Hide n' seek reloaded] [Vault] §aLinked Vault to Hide n' seek reloaded");
		}

		if (getServer().getPluginManager().getPlugin("PermissionsEx") == null) {
			Bukkit.getConsoleSender().sendMessage("[Hide n' seek reloaded] §cCould not link to PermissionsEx... Is it installed?");
		} else {
			Bukkit.getConsoleSender().sendMessage("[Hide n' seek reloaded] [PermissionsEx] §aLinked PermissionsEx to Hide n' seek reloaded");
		}
		
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eHide n' seek reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	
	public static void sendToArenaOnly(String arenaname, String message) {
		for (Entry<String, Player> entry : Main.ArenaPlayer.entrySet()) {
	        if (Objects.equals(arenaname, entry.getKey())) {
	        	Player player = entry.getValue();
				player.sendMessage(message);
	        }
	    }
	}
	
	public static Main getPlugin() {
		return instance;
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
	/*
	public static Boolean isinLobby(Location loc) {
		MoveListener ml = new MoveListener();
		DataManager dm = new DataManager();

		if (dm.getData().contains("Data.lobby")) {
			if ( ( (loc.getX() >= ml.smallerX()) && (loc.getY() >= ml.smallerY()) && (loc.getZ() >= ml.smallerZ()) ) && 
					( (loc.getX() <= ml.biggerX()) && (loc.getY() <= ml.biggerY()) && (loc.getZ() <= ml.biggerZ()) ) ) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	*/
	
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
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}

	
	public void setUpConfigs() {
		saveDefaultConfig();
		/*
		if (!new File(getDataFolder(), "scoreboard.yml").exists()) {
			saveResource("scoreboard.yml", false);			
		}
		ScoreboardConfig = YamlConfiguration.loadConfiguration(ScoreboardConfigFile);
		
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
	public FileConfiguration getScoreboardConfig() {
		return ScoreboardConfig;
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
	
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eHide n' seek reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: §e0.1");
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §4deaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
}
