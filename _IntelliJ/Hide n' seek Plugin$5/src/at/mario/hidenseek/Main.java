package at.mario.hidenseek;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import at.mario.hidenseek.commands.HideNSeekCMD;
import at.mario.hidenseek.commands.HideNSeekTabComleter;
import at.mario.hidenseek.gamestates.GameState;
import at.mario.hidenseek.gamestates.IngameState;
import at.mario.hidenseek.gamestates.LobbyState;
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
import at.mario.hidenseek.listener.TabCompleteListener;
import at.mario.hidenseek.listener.ToggleFlightListener;
import at.mario.hidenseek.manager.GameStateManager;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;
import at.mario.hidenseek.manager.ConfigManagers.StatsManager;
import at.mario.hidenseek.pictureLogin.com.bobacadodl.imgmessage.ImageChar;
import at.mario.hidenseek.pictureLogin.com.bobacadodl.imgmessage.ImageMessage;
import at.mario.hidenseek.pictureLogin.me.itsnathang.picturelogin.util.PictureUtil;
import at.mario.hidenseek.scoreboards.GameScoreboard;
import at.mario.hidenseek.scoreboards.LobbyScoreboard;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Main extends JavaPlugin implements Listener {

	private static Main instance = null;
	
	public static Main getInstance() {
		return instance;
	}
	
	private PictureUtil pictureUtil;
	
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

	public File StatsConfigFile = new File(this.getDataFolder(), "stats.yml");
	public FileConfiguration StatsConfig = YamlConfiguration.loadConfiguration(StatsConfigFile);
	
	public static HashMap<String, List<Player>> ArenaPlayer;
	
	public void onEnable() {
		instance = this;
		
		pictureUtil = new PictureUtil(this);
		
        setUpConfigs();
        
		gameStateManager = new GameStateManager();
        
        ArenaPlayer = new HashMap<String, List<Player> >();
		getCommand("hidenseek").setExecutor(new HideNSeekCMD());
		getCommand("hidenseek").setTabCompleter(new HideNSeekTabComleter());
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new InventoryClickListener(), this);
		pm.registerEvents(new Quitlistener(), this);
		pm.registerEvents(new JoinListener(getPlugin()), this);
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
		pm.registerEvents(new TabCompleteListener(), this);
		
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
		
		try {
			updateAllSigns();
		} catch (Exception e) {   }
		
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eHide n' seek reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	
	public static void giveLobbyItems(Player p) {
		MessagesManager mm = new MessagesManager();
		
		ItemStack leave = new ItemStack(Material.valueOf(getInstance().getConfig().getString("Config.lobbyitems.leaveItem.material")), getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.amount"), (short) 
				getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.damage"));
		ItemMeta leaveMeta = leave.getItemMeta();
		leaveMeta.setDisplayName(mm.getMessages().getString("Messages.lobbyitems.leaveItem.name"));
		leaveMeta.setLore(mm.getMessages().getStringList("Messages.lobbyitems.leaveItem.lore"));
		leave.setItemMeta(leaveMeta);
		
		ItemStack shop = new ItemStack(Material.valueOf(getInstance().getConfig().getString("Config.lobbyitems.shopItem.material")), getInstance().getConfig().getInt("Config.lobbyitems.shopItem.amount"), (short) 
				getInstance().getConfig().getInt("Config.lobbyitems.shopItem.damage"));
		ItemMeta shopMeta = shop.getItemMeta();
		shopMeta.setDisplayName(mm.getMessages().getString("Messages.lobbyitems.shopItem.name"));
		shopMeta.setLore(mm.getMessages().getStringList("Messages.lobbyitems.shopItem.lore"));
		shop.setItemMeta(shopMeta);
		
		ItemStack pass = new ItemStack(Material.valueOf(getInstance().getConfig().getString("Config.lobbyitems.passItem.material")), getInstance().getConfig().getInt("Config.lobbyitems.passItem.amount"), (short) 
				getInstance().getConfig().getInt("Config.lobbyitems.passItem.damage"));
		ItemMeta passMeta = pass.getItemMeta();
		passMeta.setDisplayName(mm.getMessages().getString("Messages.lobbyitems.passItem.name"));
		passMeta.setLore(mm.getMessages().getStringList("Messages.lobbyitems.passItem.lore"));
		pass.setItemMeta(passMeta);
		
		
		p.getInventory().setItem(getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.slot"), leave);
		p.getInventory().setItem(getInstance().getConfig().getInt("Config.lobbyitems.shopItem.slot"), shop);
		p.getInventory().setItem(getInstance().getConfig().getInt("Config.lobbyitems.passItem.slot"), pass);
	}
	public static void removeLobbyItems(Player p) {
		MessagesManager mm = new MessagesManager();
		
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.leaveItem.name"))) {
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.shopItem.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.shopItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.shopItem.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.shopItem.name"))) {
					
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.lobbyitems.shopItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.passItem.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.passItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.passItem.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.passItem.name"))) {
					
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.lobbyitems.passItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.startItem.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.startItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.startItem.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.startItem.name"))) {
					
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.lobbyitems.startItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
	}
	
	public static void sendToArenaOnly(String arenaname, String message) {
		List<Player> players = Main.ArenaPlayer.get(arenaname);
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			player.sendMessage(message);
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
	
	private void updateAllSigns() {
		DataManager dm = new DataManager();
		
		LobbyState.minPlayers = new HashMap<String, Integer>();
		LobbyState.maxPlayers = new HashMap<String, Integer>();
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
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
	
	public void setUpConfigs() {
		saveDefaultConfig();
		
		if (!new File(getDataFolder(), "scoreboard.yml").exists()) {
			saveResource("scoreboard.yml", false);			
		}
		ScoreboardConfig = YamlConfiguration.loadConfiguration(ScoreboardConfigFile);

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

	public static void joinGame(String arenaName, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof LobbyState) {
			if (dm.getData().getInt("Data.arenas." + arenaName + ".maxplayer") < LobbyState.maxPlayers.get(arenaName)) {
				List<Player> players = new ArrayList<Player>();
				if (Main.ArenaPlayer.containsKey(arenaName) && Main.ArenaPlayer.get(arenaName) != null) {
					players = Main.ArenaPlayer.get(arenaName);
				}
				players.add(p);
				Main.ArenaPlayer.put(arenaName, players);
				SignChangeListener.updateSigns(arenaName);
				
				int currentPlayers = Main.ArenaPlayer.get(arenaName).size();
				
				if (Main.ArenaPlayer.containsKey(arenaName)) {
					currentPlayers = Main.ArenaPlayer.get(arenaName).size();
				}
			
				int missingPlayers = LobbyState.minPlayers.get(arenaName) - currentPlayers;
				
				LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(arenaName);
				Main.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.lobbyJoinMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).replace("%arena%", arenaName)
						.replace("%players%", currentPlayers+"").replace("%maxplayers%", LobbyState.maxPlayers.get(arenaName)+""));
				if (missingPlayers != 0) {
					Main.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.missingPlayersToStart").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%missingplayers%", missingPlayers+""));
				}

				Location loc = (Location) dm.getData().get("Data.arenas." + arenaName + ".lobbyspawn");
				p.teleport(loc);
				if (currentPlayers >= LobbyState.minPlayers.get(arenaName)) {
					// LobbyCountdown starten...
					if (!lobbyState.getLobbycountdown().isRunning(arenaName)) {
						if (lobbyState.getLobbycountdown().isIdling(arenaName)) {
							lobbyState.getLobbycountdown().cancelIdleing(arenaName);
						}
						lobbyState.getLobbycountdown().run(arenaName);
					}
				} else {
					lobbyState.getLobbycountdown().idle(arenaName);
				}
				giveLobbyItems(p);
				
				PlayerInteractListener.taskIDs.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
					
					@Override
					public void run() {
						if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof LobbyState && Main.ArenaPlayer.get(arenaName).contains(p)) {
							LobbyScoreboard.setScoreboard(arenaName, p);
						} else {
							try {  Bukkit.getScheduler().cancelTask(PlayerInteractListener.taskIDs.get(p));  } catch (Exception e2) {		}
							PlayerInteractListener.taskIDs.remove(p);
							LobbyScoreboard.removeScoreboard(p);
							return;
						}
					}
				}, 0, 1 * 20));
			}
		}
	}
	
	public static void leaveGame(String arenaName, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();

		List<Player> players = Main.ArenaPlayer.get(arenaName);
		
		int currentPlayers = 0;
		if (Main.ArenaPlayer.containsKey(arenaName)) {
			currentPlayers = Main.ArenaPlayer.get(arenaName).size();
		}
		
		if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof LobbyState) {
			LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(arenaName);
			
			if (currentPlayers < LobbyState.minPlayers.get(arenaName)) {
				if (currentPlayers == 1) {
					Main.getInstance().getGameStateManager().setIsJoinme(arenaName, false);
					if (lobbyState.getLobbycountdown().isRunning(arenaName)) {
						lobbyState.getLobbycountdown().cancel(arenaName);
					}
					if (lobbyState.getLobbycountdown().isIdling(arenaName)) {
						lobbyState.getLobbycountdown().cancelIdleing(arenaName);
					}
				} else {
					// LobbyCountdown stoppen...
					if (lobbyState.getLobbycountdown().isRunning(arenaName)) {
						lobbyState.getLobbycountdown().cancel(arenaName);
						if (!lobbyState.getLobbycountdown().isIdling(arenaName)) {
							lobbyState.getLobbycountdown().idle(arenaName);
						}
					}
				}
			}
		}
		if (players != null && players.contains(p)) {
			players.remove(p);
		}
		removeLobbyItems(p);
		
		if (getInstance().getConfig().getBoolean("Config.sendStatsOnLeave")) {
			StatsManager.sendStats(p.getName(), p);
		}

		if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof IngameState) {
			if (getInstance().getConfig().getBoolean("Config.add1LoseAtLeavingGame")) {
				StatsManager.setStat(p, "loses", StatsManager.getStats(p).getInt("loses") + 1);
			}
		}
		
		Main.ArenaPlayer.put(arenaName, players);
		SignChangeListener.updateSigns(arenaName);
		
		Main.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.lobbyQuitMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).replace("%arena%", arenaName)
				.replace("%players%", currentPlayers+"").replace("%maxplayers%", LobbyState.maxPlayers.get(arenaName)+""));
		LobbyScoreboard.removeScoreboard(p);
		GameScoreboard.removeScoreboard(p);
		p.removePotionEffect(PotionEffectType.BLINDNESS);
		p.removePotionEffect(PotionEffectType.JUMP);
		p.removePotionEffect(PotionEffectType.SLOW);
		
		Location loc = (Location) dm.getData().get("Data.mainlobbyspawn");
		p.teleport(loc);
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
}
