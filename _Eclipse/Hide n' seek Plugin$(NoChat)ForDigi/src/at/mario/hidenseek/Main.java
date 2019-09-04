package at.mario.hidenseek;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
import at.mario.hidenseek.listener.FoodLevelChangeListener;
import at.mario.hidenseek.listener.GamemodeListener;
import at.mario.hidenseek.listener.InventoryClickListener;
import at.mario.hidenseek.listener.InventoryCloseListener;
import at.mario.hidenseek.listener.ItemDropListener;
import at.mario.hidenseek.listener.JoinListener;
import at.mario.hidenseek.listener.MapInitializeListener;
import at.mario.hidenseek.listener.MoveListener;
import at.mario.hidenseek.listener.PlayerChatListener;
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
	public static HashMap<String, List<Player>> SpectateArenaPlayer;
	
	private static HashMap<Player, Integer> specTaskIDs;
	

	public static HashMap<String, HashMap<Player, Integer> > arenaHiderFoundAfterSeconds;
	public static HashMap<String, HashMap<Player, Integer> > arenaSeekerFoundPlayer;
	public static HashMap<String, Player> arenaHiderLeft;
	public static HashMap<String, Player> arenaLastHider;
	public static HashMap<String, Player> arenaFistSeeker;
	
	
	public void onEnable() {
		instance = this;
		
		pictureUtil = new PictureUtil(this);
		
        setUpConfigs();
        
		gameStateManager = new GameStateManager();
        
        ArenaPlayer = new HashMap<String, List<Player> >();
        specTaskIDs = new HashMap<Player, Integer>();
        arenaLastHider = new HashMap<String, Player>();
        SpectateArenaPlayer = new HashMap<String, List<Player> >();
        
        arenaHiderFoundAfterSeconds = new HashMap<String, HashMap<Player, Integer> >();
        arenaSeekerFoundPlayer = new HashMap<String, HashMap<Player, Integer> >();
        arenaHiderLeft = new HashMap<String, Player>();
        arenaFistSeeker = new HashMap<String, Player>();
        
		getCommand("hidenseek").setExecutor(new HideNSeekCMD());
		getCommand("hidenseek").setTabCompleter(new HideNSeekTabComleter());
		
		//PluginManager pm = Bukkit.getPluginManager();
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
		Bukkit.getPluginManager().registerEvents(new Quitlistener(), this);
		Bukkit.getPluginManager().registerEvents(new JoinListener(getPlugin()), this);
		Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
		Bukkit.getPluginManager().registerEvents(new MoveListener(), this);
		Bukkit.getPluginManager().registerEvents(new SneakListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
		Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
		Bukkit.getPluginManager().registerEvents(new ItemDropListener(), this);
		Bukkit.getPluginManager().registerEvents(new GamemodeListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
		Bukkit.getPluginManager().registerEvents(new ToggleFlightListener(), this);
		Bukkit.getPluginManager().registerEvents(new SignChangeListener(), this);
		Bukkit.getPluginManager().registerEvents(new PreCommandListener(), this);
		Bukkit.getPluginManager().registerEvents(new TabCompleteListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new MapInitializeListener(), this);
		
		if (setupEconomy() == true) {
			Bukkit.getConsoleSender().sendMessage("[Hide n' seek reloaded] [Vault] §aLinked Vault to Hide n' seek reloaded");
		} else {
			Bukkit.getConsoleSender().sendMessage("[Hide n' seek reloaded] §cCould not link to Vault... Is it installed?");
		}

		if (getServer().getPluginManager().getPlugin("PermissionsEx") == null) {
			Bukkit.getConsoleSender().sendMessage("[Hide n' seek reloaded] §cCould not link to PermissionsEx... Is it installed?");
		} else {
			Bukkit.getConsoleSender().sendMessage("[Hide n' seek reloaded] [PermissionsEx] §aLinked PermissionsEx to Hide n' seek reloaded");
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
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eHide n' seek reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	
	public static void giveStartItem(Player p) {
		MessagesManager mm = new MessagesManager();
		
		String path = "lobbyitems";
		
		
		ItemStack start = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".start.material")));
		ItemMeta startMeta = start.getItemMeta();
		startMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".start.name"));
		List<String> startList = mm.getMessages().getStringList("Messages." + path + ".start.lore");
		for (int i = 0; i < startList.size(); i++)
			startList.set(i, startList.get(i));
		startMeta.setLore(startList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".start.enchantedGlow")) {
			startMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			startMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		start.setItemMeta(startMeta);
		start.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".start.amount"));
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config." + path + ".start.slot"), start);
	}
	public static void giveLobbyItems(Player p) {
		MessagesManager mm = new MessagesManager();
		
		String path = "lobbyitems";
		
		
		ItemStack shop = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".shop.material")));
		ItemMeta shopMeta = shop.getItemMeta();
		shopMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".shop.name"));
		List<String> shopList = mm.getMessages().getStringList("Messages." + path + ".shop.lore");
		for (int i = 0; i < shopList.size(); i++)
			shopList.set(i, shopList.get(i));
		shopMeta.setLore(shopList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".shop.enchantedGlow")) {
			shopMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			shopMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		shop.setItemMeta(shopMeta);
		shop.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".shop.amount"));
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config." + path + ".shop.slot"), shop);
		
		
		ItemStack pass = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".pass.material")));
		ItemMeta passMeta = pass.getItemMeta();
		passMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".pass.name"));
		List<String> passList = mm.getMessages().getStringList("Messages." + path + ".pass.lore");
		for (int i = 0; i < passList.size(); i++)
			passList.set(i, passList.get(i));
		passMeta.setLore(passList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".pass.enchantedGlow")) {
			passMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			passMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		pass.setItemMeta(passMeta);
		pass.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".pass.amount"));
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config." + path + ".pass.slot"), pass);
		
		
		ItemStack leave = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".leave.material")));
		ItemMeta leaveMeta = leave.getItemMeta();
		leaveMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".leave.name"));
		List<String> leaveList = mm.getMessages().getStringList("Messages." + path + ".leave.lore");
		for (int i = 0; i < leaveList.size(); i++)
			leaveList.set(i, leaveList.get(i));
		leaveMeta.setLore(leaveList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".leave.enchantedGlow")) {
			leaveMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			leaveMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		leave.setItemMeta(leaveMeta);
		leave.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".leave.amount"));
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config." + path + ".leave.slot"), leave);
	}
	public static void giveSpectatorItems(Player p) {
		MessagesManager mm = new MessagesManager();
		
		String path = "lobbyitems";
		

		ItemStack shop = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".shop.material")));
		ItemMeta shopMeta = shop.getItemMeta();
		shopMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".shop.name"));
		List<String> shopList = mm.getMessages().getStringList("Messages." + path + ".shop.lore");
		for (int i = 0; i < shopList.size(); i++)
			shopList.set(i, shopList.get(i));
		shopMeta.setLore(shopList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".shop.enchantedGlow")) {
			shopMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			shopMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		shop.setItemMeta(shopMeta);
		shop.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".shop.amount"));
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config." + path + ".shop.slot"), shop);
		
		
		ItemStack leave = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".leave.material")));
		ItemMeta leaveMeta = leave.getItemMeta();
		leaveMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".leave.name"));
		List<String> leaveList = mm.getMessages().getStringList("Messages." + path + ".leave.lore");
		for (int i = 0; i < leaveList.size(); i++)
			leaveList.set(i, leaveList.get(i));
		leaveMeta.setLore(leaveList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".leave.enchantedGlow")) {
			leaveMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			leaveMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		leave.setItemMeta(leaveMeta);
		leave.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".leave.amount"));
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config." + path + ".leave.slot"), leave);
	}
	public static void giveIngameItems(Player p) {
		MessagesManager mm = new MessagesManager();

		String path = "ingameitems";
		if (Roles.roles.get(p) == Roles.SEEKER) {
			
			ItemStack seekerStick = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".seekerStick.material")));
			ItemMeta seekerStickMeta = seekerStick.getItemMeta();
			seekerStickMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".seekerStick.name"));
			List<String> seekerStickList = mm.getMessages().getStringList("Messages." + path + ".seekerStick.lore");
			for (int i = 0; i < seekerStickList.size(); i++)
				seekerStickList.set(i, seekerStickList.get(i));
			seekerStickMeta.setLore(seekerStickList);
			if (Main.getInstance().getConfig().getBoolean("Config." + path + ".seekerStick.enchantedGlow")) {
				seekerStickMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				seekerStickMeta.addEnchant(Enchantment.DURABILITY, 0, true);
			}
			seekerStick.setItemMeta(seekerStickMeta);
			seekerStick.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".seekerStick.amount"));
			p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config." + path + ".seekerStick.slot"), seekerStick);
			
			
			ItemStack seekerHelmet = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".seekerHelmet.material")));
			ItemMeta seekerHelmetMeta = seekerHelmet.getItemMeta();
			seekerHelmetMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".seekerHelmet.name"));
			List<String> seekerHelmetList = mm.getMessages().getStringList("Messages." + path + ".seekerHelmet.lore");
			for (int i = 0; i < seekerHelmetList.size(); i++)
				seekerHelmetList.set(i, seekerHelmetList.get(i));
			seekerStickMeta.setLore(seekerHelmetList);
			if (Main.getInstance().getConfig().getBoolean("Config." + path + ".seekerHelmet.enchantedGlow")) {
				seekerHelmetMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				seekerHelmetMeta.addEnchant(Enchantment.DURABILITY, 0, true);
			}
			seekerHelmet.setItemMeta(seekerHelmetMeta);
			seekerHelmet.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".seekerHelmet.amount"));
			p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config." + path + ".seekerHelmet.slot"), seekerHelmet);
			
			
			ItemStack seekerChestplate = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".seekerChestplate.material")));
			ItemMeta seekerChestplateMeta = seekerChestplate.getItemMeta();
			seekerChestplateMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".seekerChestplate.name"));
			List<String> seekerChestplateList = mm.getMessages().getStringList("Messages." + path + ".seekerChestplate.lore");
			for (int i = 0; i < seekerChestplateList.size(); i++)
				seekerChestplateList.set(i, seekerChestplateList.get(i));
			seekerStickMeta.setLore(seekerChestplateList);
			if (Main.getInstance().getConfig().getBoolean("Config." + path + ".seekerChestplate.enchantedGlow")) {
				seekerChestplateMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				seekerChestplateMeta.addEnchant(Enchantment.DURABILITY, 0, true);
			}
			seekerChestplate.setItemMeta(seekerChestplateMeta);
			seekerChestplate.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".seekerChestplate.amount"));
			p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config." + path + ".seekerChestplate.slot"), seekerChestplate);
			
			
			ItemStack seekerLeggings = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".seekerLeggings.material")));
			ItemMeta seekerLeggingsMeta = seekerLeggings.getItemMeta();
			seekerLeggingsMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".seekerLeggings.name"));
			List<String> seekerLeggingsList = mm.getMessages().getStringList("Messages." + path + ".seekerLeggings.lore");
			for (int i = 0; i < seekerLeggingsList.size(); i++)
				seekerLeggingsList.set(i, seekerLeggingsList.get(i));
			seekerStickMeta.setLore(seekerLeggingsList);
			if (Main.getInstance().getConfig().getBoolean("Config." + path + ".seekerLeggings.enchantedGlow")) {
				seekerLeggingsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				seekerLeggingsMeta.addEnchant(Enchantment.DURABILITY, 0, true);
			}
			seekerLeggings.setItemMeta(seekerLeggingsMeta);
			seekerLeggings.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".seekerLeggings.amount"));
			p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config." + path + ".seekerLeggings.slot"), seekerLeggings);
			
			
			ItemStack seekerBoots = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".seekerBoots.material")));
			ItemMeta seekerBootsMeta = seekerBoots.getItemMeta();
			seekerBootsMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".seekerBoots.name"));
			List<String> seekerBootsList = mm.getMessages().getStringList("Messages." + path + ".seekerBoots.lore");
			for (int i = 0; i < seekerBootsList.size(); i++)
				seekerBootsList.set(i, seekerBootsList.get(i));
			seekerBootsMeta.setLore(seekerBootsList);
			if (Main.getInstance().getConfig().getBoolean("Config." + path + ".seekerBoots.enchantedGlow")) {
				seekerBootsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				seekerBootsMeta.addEnchant(Enchantment.DURABILITY, 0, true);
			}
			seekerBoots.setItemMeta(seekerBootsMeta);
			seekerBoots.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".seekerBoots.amount"));
			p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config." + path + ".seekerBoots.slot"), seekerBoots);
		}
	}
	public static void removeItems(Player p) {
		MessagesManager mm = new MessagesManager();
		
		// Ingameitems
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerStick.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerStick.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerStick.slot")).getItemMeta().getDisplayName().
						equals(mm.getMessages().getString("Messages.ingameitems.seekerStick.name"))) {
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.ingameitems.seekerStick.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerHelmet.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerHelmet.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerHelmet.slot")).getItemMeta().getDisplayName().
						equals(mm.getMessages().getString("Messages.ingameitems.seekerHelmet.name"))) {
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.ingameitems.seekerHelmet.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerChestplate.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerChestplate.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerChestplate.slot")).getItemMeta().getDisplayName().
						equals(mm.getMessages().getString("Messages.ingameitems.seekerChestplate.name"))) {
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.ingameitems.seekerChestplate.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerLeggings.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerLeggings.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerLeggings.slot")).getItemMeta().getDisplayName().
						equals(mm.getMessages().getString("Messages.ingameitems.seekerLeggings.name"))) {
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.ingameitems.seekerLeggings.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerBoots.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerBoots.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.ingameitems.seekerBoots.slot")).getItemMeta().getDisplayName().
						equals(mm.getMessages().getString("Messages.ingameitems.seekerBoots.name"))) {
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.ingameitems.seekerBoots.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		
		
		// Lobbyitems
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.leave.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.leave.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.leave.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.leave.name"))) {
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.lobbyitems.leave.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.shop.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.shop.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.shop.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.shop.name"))) {
					
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.lobbyitems.shop.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.pass.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.pass.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.pass.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.pass.name"))) {
					
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.lobbyitems.pass.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.start.slot")) != null) {
			if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.start.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(getInstance().getConfig().getInt("Config.lobbyitems.start.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.start.name"))) {
					
					p.getInventory().setItem(getInstance().getConfig().getInt("Config.lobbyitems.start.slot"), new ItemStack(Material.AIR));
				}
			}
		}
	}
	
	public static void rewardPlayer(String arena, Player p) {
		MessagesManager mm = new MessagesManager();
		
		int amount = 0;
		
		int moneyPerMinuteOfSurviving = 0;
		int moneyIfSurvivedTillEnd = 0;
		int moneyPerFoundPlayer = 0;
		int moneyIfWinGame = 0;
		
		if (Roles.roles.get(p) == Roles.HIDER || (Main.arenaLastHider.containsKey(arena) && Main.arenaLastHider.get(arena).equals(p))) {
			if (Main.arenaHiderFoundAfterSeconds.containsKey(arena) && Main.arenaHiderFoundAfterSeconds.get(arena) != null) {
				if (Main.arenaHiderFoundAfterSeconds.get(arena).containsKey(p) && Main.arenaHiderFoundAfterSeconds.get(arena).get(p) != null) {
					moneyPerMinuteOfSurviving = (int) (Math.floor(Main.arenaHiderFoundAfterSeconds.get(arena).get(p) / (float) 60) * 
							Main.getInstance().getConfig().getInt("Config.coins.hiders.moneyPerMinuteOfSurviving"));
				}
			}
			if (Main.arenaHiderLeft.containsValue(p)) {
				moneyIfSurvivedTillEnd = Main.getInstance().getConfig().getInt("Config.coins.hiders.moneyIfSurvivedTillEnd");
			}
		} else {
			if (Main.arenaSeekerFoundPlayer.containsKey(arena) && Main.arenaSeekerFoundPlayer.get(arena) != null) {
				if (Main.arenaSeekerFoundPlayer.get(arena).containsKey(p) && Main.arenaSeekerFoundPlayer.get(arena).get(p) != null) {
					moneyPerFoundPlayer = Main.getInstance().getConfig().getInt("Config.coins.seekers.moneyPerFoundPlayer") * Main.arenaSeekerFoundPlayer.get(arena).get(p);
				}
			}
			if (Main.arenaFistSeeker.containsValue(p)) {
				if (Main.arenaHiderLeft.size() == 0) {
					moneyIfWinGame = Main.getInstance().getConfig().getInt("Config.coins.seekers.moneyIfWinGame");
				}
			}
		}
		
		amount += moneyPerMinuteOfSurviving;
		amount += moneyIfSurvivedTillEnd;
		amount += moneyPerFoundPlayer;
		amount += moneyIfWinGame;
		
		eco.depositPlayer(p, amount);

		List<String> list = mm.getMessages().getStringList("Messages.rewards.format");
		
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);

			p.sendMessage(string.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
					replace("%moneyPerMinuteOfSurviving%", moneyPerMinuteOfSurviving+"").
					replace("%moneyIfSurvivedTillEnd%", moneyIfSurvivedTillEnd+"").
					replace("%moneyPerFoundPlayer%", moneyPerFoundPlayer+"").
					replace("%moneyIfWinGame%", moneyIfWinGame+"").
					replace("%total%", amount+""));
		}
	}
	
	public static Boolean isinLobby(Location loc, String arenaName) {
		MoveListener ml = new MoveListener();
		// DataManager dm = new DataManager();

		if ( ( (loc.getX() >= ml.smallerX(arenaName)) && (loc.getY() >= ml.smallerY(arenaName)) && (loc.getZ() >= ml.smallerZ(arenaName)) ) && 
				( (loc.getX() <= ml.biggerX(arenaName)) && (loc.getY() <= ml.biggerY(arenaName)) && (loc.getZ() <= ml.biggerZ(arenaName)) ) ) {
			return true;
		} else {
			return false;
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
	/*
	public static void setNoAI(Entity bukkitEntity) {
	    net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
	    
	    NBTTagCompound tag = nmsEntity.getNBTTag();
	    if (tag == null) {
	        tag = new NBTTagCompound();
	    }
	    nmsEntity.c(tag);
	    tag.setInt("NoAI", 1);
	    nmsEntity.f(tag);
	}*/
	
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eHide n' seek reloaded");
        Bukkit.getConsoleSender().sendMessage("§cPlugin version: §e0.1");
        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §4deaktivated");
        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
	
	public static String getArenaOfPlayer(Player p) {
		DataManager dm = new DataManager();
		
		String arenaName = "";
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		if (configSection != null) {
			for (String key : configSection.getKeys(false)) {
				if (Main.ArenaPlayer.containsKey(key)) {
					if (Main.ArenaPlayer.get(key).contains(p)) {
						arenaName = key;
						break;
					}
				}
			}
		}
		
		return arenaName;
	}
	
	public static void joinGame(String arenaName, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof LobbyState) {
			int currentPlayers = 0;

			if (Main.ArenaPlayer.get(arenaName) != null) {
				if (Main.ArenaPlayer.containsKey(arenaName)) {
					currentPlayers = Main.ArenaPlayer.get(arenaName).size();
				}
			}
			
			if (currentPlayers <= dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers")) {
				List<Player> players = new ArrayList<Player>();
				if (Main.ArenaPlayer.containsKey(arenaName) && Main.ArenaPlayer.get(arenaName) != null) {
					players = Main.ArenaPlayer.get(arenaName);
				}
				players.add(p);
				Main.ArenaPlayer.put(arenaName, players);
				SignChangeListener.updateSigns(arenaName);

				giveLobbyItems(p);
			
				int missingPlayers = LobbyState.minPlayers.get(arenaName) - currentPlayers;
				
				if (Main.ArenaPlayer.get(arenaName) != null) {
					if (Main.ArenaPlayer.containsKey(arenaName)) {
						currentPlayers = Main.ArenaPlayer.get(arenaName).size();
					}
				}
				
				LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(arenaName);
				Main.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.lobbyJoinMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).
						replace("%arena%", arenaName).replace("%players%", currentPlayers+"").replace("%maxplayers%", LobbyState.maxPlayers.get(arenaName)+""));
				if (missingPlayers != 0) {
					missingPlayers = LobbyState.minPlayers.get(arenaName) - currentPlayers;
					Main.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.missingPlayersToStart").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%missingplayers%", missingPlayers+""));
				}
				
				Location loc = p.getLocation();
				loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".lobbyspawn.world")));
				loc.setPitch((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".lobbyspawn.pitch")));
				loc.setYaw((float) ((double) dm.getData().get("Data.arenas." + arenaName + ".lobbyspawn.yaw")));
				loc.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.x"));
				loc.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.y"));
				loc.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.z"));
				p.teleport(loc);
				
				dm.getData().set("Data." + p.getName() + ".gamemode", p.getGameMode().toString());
				dm.saveData();
				
				if (getInstance().getConfig().get("Config.gamemode.lobby") instanceof String) {
					if (getInstance().getConfig().getString("Config.gamemode.lobby").equalsIgnoreCase("survival")) {
						p.setGameMode(GameMode.SURVIVAL);
					} else if (getInstance().getConfig().getString("Config.gamemode.lobby").equalsIgnoreCase("creative")) {
						p.setGameMode(GameMode.CREATIVE);
					} else if (getInstance().getConfig().getString("Config.gamemode.lobby").equalsIgnoreCase("adventure")) {
						p.setGameMode(GameMode.ADVENTURE);
					} else if (getInstance().getConfig().getString("Config.gamemode.lobby").equalsIgnoreCase("spectator")) {
						p.setGameMode(GameMode.SPECTATOR);
					}
				} else {
					if (getInstance().getConfig().getInt("Config.gamemode.lobby") == 0) {
						p.setGameMode(GameMode.SURVIVAL);
					} else if (getInstance().getConfig().getInt("Config.gamemode.lobby") == 1) {
						p.setGameMode(GameMode.CREATIVE);
					} else if (getInstance().getConfig().getInt("Config.gamemode.lobby") == 2) {
						p.setGameMode(GameMode.ADVENTURE);
					} else if (getInstance().getConfig().getInt("Config.gamemode.lobby") == 3) {
						p.setGameMode(GameMode.SPECTATOR);
					}
				}
				
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
				
				PlayerInteractListener.taskIDs.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
					
					@Override
					public void run() {
						if (Main.ArenaPlayer.get(arenaName) != null && Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof LobbyState && Main.ArenaPlayer.get(arenaName).contains(p)) {
							LobbyScoreboard.setScoreboard(arenaName, p);
						} else {
							try {  Bukkit.getScheduler().cancelTask(PlayerInteractListener.taskIDs.get(p));  } catch (Exception e2) {		}
							PlayerInteractListener.taskIDs.remove(p);
							LobbyScoreboard.removeScoreboard(p);
							return;
						}
					}
				}, 0, 1 * 20));
			} else {
				if (p.hasPermission("hs.premiumJoin") || p.isOp()) {
					List<Player> players = new ArrayList<Player>();
					if (Main.ArenaPlayer.containsKey(arenaName) && Main.ArenaPlayer.get(arenaName) != null) {
						players = Main.ArenaPlayer.get(arenaName);
					}
					Player playerToKick = null;
					for (int i = players.size() - 1; i >= 0; i--) {
						Player player = players.get(i);
						
						if (!p.hasPermission("hs.premiumJoin") && !player.isOp()) {
							playerToKick = player;
							break;
						}
					}
					if (playerToKick == null) {
						playerToKick = players.get(players.size() - 1);
					}
					
					Main.leaveGame(arenaName, playerToKick);
					Main.joinGame(arenaName, p);
				} else {
					p.sendMessage(mm.getMessages().getString("Messages.noPremiumJoin").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				}
			}
		} else if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof IngameState) {
			joinGameAsSpectator(arenaName, p);
		}
	}
	
	public static void joinGameAsSpectator(String arenaName, Player p) {
		DataManager dm = new DataManager();
		
		if (Main.getInstance().getGameStateManager().getCurrentGameState(arenaName) instanceof IngameState) {
			List<Player> spectatorPlayers = new ArrayList<Player>();
			if (Main.SpectateArenaPlayer.containsKey(arenaName) && Main.SpectateArenaPlayer.get(arenaName) != null) {
				spectatorPlayers = Main.SpectateArenaPlayer.get(arenaName);
			}
			spectatorPlayers.add(p);
			Main.SpectateArenaPlayer.put(arenaName, spectatorPlayers);
			SignChangeListener.updateSigns(arenaName);

			Location loc = p.getLocation();
			Double pitch = (Double) dm.getData().get("Data.arenas." + arenaName + ".lobbyspawn.pitch");
			Double yaw = (Double) dm.getData().get("Data.arenas." + arenaName + ".lobbyspawn.yaw");
			loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".lobbyspawn.world")));
			loc.setPitch(pitch.floatValue());
			loc.setYaw(yaw.floatValue());
			loc.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.x"));
			loc.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.y"));
			loc.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.z"));
			p.teleport(loc);
			
			dm.getData().set("Data." + p.getName() + ".gamemode", p.getGameMode().toString());
			dm.saveData();
			
			if (getInstance().getConfig().get("Config.gamemode.spectator") instanceof String) {
				if (getInstance().getConfig().getString("Config.gamemode.spectator").equalsIgnoreCase("survival")) {
					p.setGameMode(GameMode.SURVIVAL);
				} else if (getInstance().getConfig().getString("Config.gamemode.spectator").equalsIgnoreCase("creative")) {
					p.setGameMode(GameMode.CREATIVE);
				} else if (getInstance().getConfig().getString("Config.gamemode.spectator").equalsIgnoreCase("adventure")) {
					p.setGameMode(GameMode.ADVENTURE);
				} else if (getInstance().getConfig().getString("Config.gamemode.spectator").equalsIgnoreCase("spectator")) {
					p.setGameMode(GameMode.SPECTATOR);
				}
			} else {
				if (getInstance().getConfig().getInt("Config.gamemode.spectator") == 0) {
					p.setGameMode(GameMode.SURVIVAL);
				} else if (getInstance().getConfig().getInt("Config.gamemode.spectator") == 1) {
					p.setGameMode(GameMode.CREATIVE);
				} else if (getInstance().getConfig().getInt("Config.gamemode.spectator") == 2) {
					p.setGameMode(GameMode.ADVENTURE);
				} else if (getInstance().getConfig().getInt("Config.gamemode.spectator") == 3) {
					p.setGameMode(GameMode.SPECTATOR);
				}
			}
			p.setAllowFlight(true);
			
			Main.removeItems(p);
			Main.giveSpectatorItems(p);
			
			
			specTaskIDs.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					try {
						if (Main.SpectateArenaPlayer.get(arenaName).contains(p)) {
							GameScoreboard.setSpectatorScoreboard(arenaName, p);
						} else {
							GameScoreboard.removeScoreboard(p);
							Bukkit.getScheduler().cancelTask(specTaskIDs.get(p));
						}
					} catch (Exception e) {	}
				}
			}, 0, 1 * 20));
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

		try {
			if (Main.SpectateArenaPlayer.get(arenaName).contains(p)) {
				List<Player> spectatorPlayers = new ArrayList<Player>();
				spectatorPlayers = Main.SpectateArenaPlayer.get(arenaName);
				spectatorPlayers.remove(p);
				Main.SpectateArenaPlayer.put(arenaName, spectatorPlayers);
			}
		} catch (Exception e) {	}
		
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
		removeItems(p);
		
		p.setGameMode(GameMode.valueOf(dm.getData().getString("Data." + p.getName() + ".gamemode")));
		if (p.getGameMode() != GameMode.CREATIVE && p.getGameMode() != GameMode.SPECTATOR) {
			p.setAllowFlight(false);
		}
		
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
		
		currentPlayers = 0;
		if (Main.ArenaPlayer.containsKey(arenaName)) {
			currentPlayers = Main.ArenaPlayer.get(arenaName).size();
		}
		
		Main.sendToArenaOnly(arenaName, mm.getMessages().getString("Messages.lobbyQuitMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()).replace("%arena%", arenaName)
				.replace("%players%", currentPlayers+"").replace("%maxplayers%", LobbyState.maxPlayers.get(arenaName)+""));
		LobbyScoreboard.removeScoreboard(p);
		GameScoreboard.removeScoreboard(p);
		p.removePotionEffect(PotionEffectType.BLINDNESS);
		p.removePotionEffect(PotionEffectType.GLOWING);
		p.removePotionEffect(PotionEffectType.JUMP);
		p.removePotionEffect(PotionEffectType.SLOW);
		p.removePotionEffect(PotionEffectType.SPEED);

		
		if (getInstance().getConfig().getBoolean("Config.reAddSeekerPassOnLeaveInLobby") && IngameState.seekerPassPlayer.containsKey(p)) {
			dm.getData().set("Data." + p.getName() + ".seekerPasses", dm.getData().getInt("Data." + p.getName() + ".seekerPasses") + 1);
			dm.saveData();
		}
		if (getInstance().getConfig().getBoolean("Config.reAddJoinmePassOnLeaveInLobby") && LobbyState.joinmePassPlayer.containsKey(p)) {
			dm.getData().set("Data." + p.getName() + ".joinmes", dm.getData().getInt("Data." + p.getName() + ".joinmes") + 1);
			dm.saveData();
		}
		
		Location loc = p.getLocation();
		if (getInstance().getConfig().getBoolean("Config.teleportToMainLobbySpawn")) {
			if (dm.getData().getString("Data.mainlobbyspawn.world") == null || dm.getData().get("Data.mainlobbyspawn.pitch") == null || dm.getData().get("Data.mainlobbyspawn.yaw") == null || dm.getData().get("Data.mainlobbyspawn.x") == null 
					|| dm.getData().get("Data.mainlobbyspawn.y") == null || dm.getData().get("Data.mainlobbyspawn.z") == null) {
				p.sendMessage(mm.getMessages().getString("Messages.noMainLobbySpawn").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				
				loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data." + p.getName() + ".position.world")));
				loc.setPitch((float) dm.getData().get("Data." + p.getName() + ".position.pitch"));
				loc.setYaw((float) dm.getData().get("Data." + p.getName() + ".position.yaw"));
				loc.setX(dm.getData().getDouble("Data." + p.getName() + ".position.x"));
				loc.setY(dm.getData().getDouble("Data." + p.getName() + ".position.y"));
				loc.setZ(dm.getData().getDouble("Data." + p.getName() + ".position.z"));
				
				p.teleport(loc);
			} else {
				loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.mainlobbyspawn.world")));
				loc.setPitch((float) ((double) dm.getData().get("Data.mainlobbyspawn.pitch")));
				loc.setYaw((float) ((double) dm.getData().get("Data.mainlobbyspawn.yaw")));
				loc.setX(dm.getData().getDouble("Data.mainlobbyspawn.x"));
				loc.setY(dm.getData().getDouble("Data.mainlobbyspawn.y"));
				loc.setZ(dm.getData().getDouble("Data.mainlobbyspawn.z")); // /tellraw @a {'text':'Congratulations','color': 'black', 'text':'!','color': 'white'}
				p.teleport(loc);
			}
		} else {
			loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data." + p.getName() + ".position.world")));
			loc.setPitch((float) dm.getData().get("Data." + p.getName() + ".position.pitch"));
			loc.setYaw((float) dm.getData().get("Data." + p.getName() + ".position.yaw"));
			loc.setX(dm.getData().getDouble("Data." + p.getName() + ".position.x"));
			loc.setY(dm.getData().getDouble("Data." + p.getName() + ".position.y"));
			loc.setZ(dm.getData().getDouble("Data." + p.getName() + ".position.z"));
			
			p.teleport(loc);
		}
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
