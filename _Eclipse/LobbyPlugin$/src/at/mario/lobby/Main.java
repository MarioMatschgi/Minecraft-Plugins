package at.mario.lobby;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import at.mario.lobby.commands.ArmorCMD;
import at.mario.lobby.commands.BuildCMD;
import at.mario.lobby.commands.FlyCMD;
import at.mario.lobby.commands.GamemodeCMD;
import at.mario.lobby.commands.GamemodeTabCompleter;
import at.mario.lobby.commands.LobbyCMD;
import at.mario.lobby.commands.LobbyTabCompleter;
import at.mario.lobby.commands.MsgCMD;
import at.mario.lobby.commands.PetCMD;
import at.mario.lobby.commands.PingCMD;
import at.mario.lobby.commands.PlayerVisibilityCMD;
import at.mario.lobby.commands.SilenthubCMD;
import at.mario.lobby.commands.VanishCMD;
import at.mario.lobby.entities.RideableChicken;
import at.mario.lobby.entities.RideableCow;
import at.mario.lobby.entities.RideableHorse;
import at.mario.lobby.entities.RideableMushroomCow;
import at.mario.lobby.entities.RideableOcelot;
import at.mario.lobby.entities.RideablePig;
import at.mario.lobby.entities.RideableRabbit;
import at.mario.lobby.entities.RideableSheep;
import at.mario.lobby.entities.RideableSilverfish;
import at.mario.lobby.entities.RideableSkeleton;
import at.mario.lobby.entities.RideableSpider;
import at.mario.lobby.entities.RideableVillager;
import at.mario.lobby.entities.RideableWolf;
import at.mario.lobby.entities.RideableZombie;
import at.mario.lobby.listener.BlockBreakListener;
import at.mario.lobby.listener.BlockPlaceListener;
import at.mario.lobby.listener.DamageListener;
import at.mario.lobby.listener.EntityDismountListener;
import at.mario.lobby.listener.FoodLevelChangeListener;
import at.mario.lobby.listener.GamemodeListener;
import at.mario.lobby.listener.InventoryClick;
import at.mario.lobby.listener.InventoryCloseListener;
import at.mario.lobby.listener.ItemDropListener;
import at.mario.lobby.listener.JoinListener;
import at.mario.lobby.listener.MoveListener;
import at.mario.lobby.listener.PlayerInteractListener;
import at.mario.lobby.listener.Quitlistener;
import at.mario.lobby.listener.SneakListener;
import at.mario.lobby.listener.TeleportListener;
import at.mario.lobby.listener.ToggleFlightListener;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;
import at.mario.lobby.other.autoMessage.broadcasts.Broadcast;
import at.mario.lobby.other.autoMessage.commands.BroadcastCMD;
import at.mario.lobby.other.autoMessage.commands.BroadcastTabCompleter;
import at.mario.lobby.other.autoMessage.util.IgnoreManager;
import at.mario.lobby.other.autoMessage.util.MessageManager;
import at.mario.lobby.other.motd.MotdCMD;
import at.mario.lobby.other.motd.PriorityEvent;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class Main extends JavaPlugin implements Listener {

	private static Main instance = null;

	public static Main getInstance() {
		return instance;
	}

	public static String wandName = "§6LobbyWand";

	public static Economy eco = null;

	public File bossBarConfigFile = new File(this.getDataFolder(), "bossbar.yml");
	public FileConfiguration bossBarConfig = YamlConfiguration.loadConfiguration(bossBarConfigFile);

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

	public void onEnable() {
		instance = this;

		setUpConfigs();
		Broadcast broadcast = new Broadcast();
		IgnoreManager ignoreManager = new IgnoreManager();
		MessageManager messageManager = new MessageManager();

		getCommand("Lobby").setExecutor(new LobbyCMD());
		getCommand("Lobby").setTabCompleter(new LobbyTabCompleter());
		getCommand("fly").setExecutor(new FlyCMD());
		getCommand("vanish").setExecutor(new VanishCMD());
		getCommand("visibility").setExecutor(new PlayerVisibilityCMD());
		getCommand("build").setExecutor(new BuildCMD());
		getCommand("pet").setExecutor(new PetCMD());
		getCommand("gm").setExecutor(new GamemodeCMD());
		getCommand("gm").setTabCompleter(new GamemodeTabCompleter());
		getCommand("armor").setExecutor(new ArmorCMD());
		getCommand("silenthub").setExecutor(new SilenthubCMD());
		getCommand("motd").setExecutor(new MotdCMD());
		getCommand("broadcast").setExecutor(new BroadcastCMD());
		getCommand("broadcast").setTabCompleter(new BroadcastTabCompleter());
		getCommand("msg").setExecutor(new MsgCMD());
		getCommand("ping").setExecutor(new PingCMD());

		/*
		 * Loads chat and boss bar messages and permissions as well as chat prefix and
		 * suffix.
		 */
		messageManager.loadAll();
		/* Loads chat and boss bar ignore list. */
		ignoreManager.loadChatIgnoreList();
		ignoreManager.loadBossBarIgnoreList();
		/* Starts broadcast(s) after checking their status. */
		broadcast.broadcast();

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InventoryClick(), this);
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
		pm.registerEvents(new PriorityEvent(), this);
		pm.registerEvents(new GamemodeListener(), this);
		pm.registerEvents(new InventoryCloseListener(), this);
		pm.registerEvents(new ToggleFlightListener(), this);
		pm.registerEvents(new EntityDismountListener(), this);
		pm.registerEvents(new TeleportListener(), this);

		if (setupEconomy()) {
			// Bukkit.getConsoleSender().sendMessage("§aEco + Vault!");
		} else {
			// Bukkit.getConsoleSender().sendMessage("§cEco + Vault! Error!");
		}

		if (getServer().getPluginManager().getPlugin("PermissionsEx") == null) {
			Bukkit.getConsoleSender()
					.sendMessage("[LobbyReloaded] §cCould not link to PermissionsEx... Is it installed?");
		} else {
			Bukkit.getConsoleSender()
					.sendMessage("[LobbyReloaded] [PermissionsEx] §aLinked PermissionsEx to LobbyReloaded");
		}

		registerEntities();

		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
		Bukkit.getConsoleSender().sendMessage("§cPlugin: §eLobbyReloaded");
		Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
		Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
		Bukkit.getConsoleSender().sendMessage("§cPlugin status: §aaktivated");
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}

	@SuppressWarnings("unchecked")
	public void registerEntities() {
		try {
			Field c = EntityTypes.class.getDeclaredField("c");
			Field f = EntityTypes.class.getDeclaredField("f");
			c.setAccessible(true);
			f.setAccessible(true);
			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideablePig.class, "Pig");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideablePig.class, 90);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableWolf.class, "Wolf");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableWolf.class, 95);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableSheep.class, "Sheep");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableSheep.class, 91);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableChicken.class, "Chicken");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableChicken.class, 93);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableHorse.class, "Horse");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableHorse.class, 100);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableCow.class, "Cow");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableCow.class, 92);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableMushroomCow.class,
					"MushroomCow");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableMushroomCow.class, 96);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableOcelot.class, "Ocelot");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableOcelot.class, 98);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableRabbit.class, "Rabbit");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableRabbit.class, 101);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableVillager.class, "Villager");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableVillager.class, 120);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableSilverfish.class, "Silverfish");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableSilverfish.class, 60);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableSpider.class, "Spider");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableSpider.class, 52);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableZombie.class, "Zombie");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableZombie.class, 54);

			((Map<Class<? extends EntityInsentient>, String>) c.get(null)).put(RideableSkeleton.class, "Skeleton");
			((Map<Class<? extends EntityInsentient>, Integer>) f.get(null)).put(RideableSkeleton.class, 51);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Main getPlugin() {
		return instance;
	}

	public static void giveLobbyItems(Player p) {
		if (Main.isinLobby(p.getLocation())) {
			MessagesManager mm = new MessagesManager();

			if (p.getGameMode() == GameMode.CREATIVE) {
				return;
			} else {
				ItemStack teleporter = new ItemStack(Material.COMPASS);
				ItemMeta teleporterMeta = teleporter.getItemMeta();
				teleporterMeta.setDisplayName(mm.getMessages().getString("Messages.inventory.teleporter"));
				ArrayList<String> teleporterList = new ArrayList<String>();
				teleporterList.add(mm.getMessages().getString("Messages.inventory.meta.teleporter"));
				teleporterMeta.setLore(teleporterList);
				teleporter.setItemMeta(teleporterMeta);

				ItemStack profile = new ItemStack(Material.CHEST);
				ItemMeta profileMeta = profile.getItemMeta();
				profileMeta.setDisplayName(mm.getMessages().getString("Messages.inventory.profiles"));
				ArrayList<String> profileList = new ArrayList<String>();
				profileList.add(mm.getMessages().getString("Messages.inventory.meta.profiles"));
				profileMeta.setLore(profileList);
				profile.setItemMeta(profileMeta);

				ItemStack visibility = new ItemStack(Material.INK_SACK, 1, (short) 10);
				ItemMeta visibilityMeta = visibility.getItemMeta();
				visibilityMeta.setDisplayName(mm.getMessages().getString("Messages.inventory.visibility"));
				ArrayList<String> visibilityList = new ArrayList<String>();
				visibilityList.add(mm.getMessages().getString("Messages.inventory.meta.visibility"));
				visibilityMeta.setLore(visibilityList);
				visibility.setItemMeta(visibilityMeta);

				ItemStack silenthub = new ItemStack(Material.TNT);
				ItemMeta silenthubMeta = silenthub.getItemMeta();
				silenthubMeta.setDisplayName(mm.getMessages().getString("Messages.inventory.silenthub"));
				ArrayList<String> silenthubList = new ArrayList<String>();
				silenthubList.add(mm.getMessages().getString("Messages.inventory.meta.silenthub"));
				silenthubMeta.setLore(silenthubList);
				silenthub.setItemMeta(silenthubMeta);

				p.getInventory().setItem(0, teleporter);
				p.getInventory().setItem(2, visibility);
				p.getInventory().setItem(4, profile);
				if (p.hasPermission("lobby.vip") || p.hasPermission("lobby.admin")) {
					p.getInventory().setItem(6, silenthub);
				}
			}
		}
	}

	public static void removeLobbyItems(Player p) {
		MessagesManager mm = new MessagesManager();
		Inventory inv = p.getInventory();

		if (inv.getItem(0) == null || inv.getItem(0).getItemMeta() == null) {
			return;
		}
		try {
			if (inv.getItem(0).getItemMeta().getDisplayName() == mm.getMessages()
					.getString("Messages.inventory.teleporter")) {
				p.getInventory().setItem(0, new ItemStack(Material.AIR));
			}
			if (inv.getItem(4).getItemMeta().getDisplayName() == mm.getMessages()
					.getString("Messages.inventory.profiles")) {
				p.getInventory().setItem(4, new ItemStack(Material.AIR));
			}
			if (inv.getItem(2).getItemMeta().getDisplayName() == mm.getMessages()
					.getString("Messages.inventory.visibility")) {
				p.getInventory().setItem(2, new ItemStack(Material.AIR));
			}
			if (inv.getItem(6).getItemMeta().getDisplayName() == mm.getMessages()
					.getString("Messages.inventory.silenthub")) {
				p.getInventory().setItem(6, new ItemStack(Material.AIR));
			}
		} catch (Exception e) {
		}
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

	public static Boolean isinLobby(Location loc) {
		MoveListener ml = new MoveListener();
		DataManager dm = new DataManager();

		if (dm.getData().contains("Data.lobby")) {
			if (((loc.getX() >= ml.smallerX()) && (loc.getY() >= ml.smallerY()) && (loc.getZ() >= ml.smallerZ()))
					&& ((loc.getX() <= ml.biggerX()) && (loc.getY() <= ml.biggerY()) && (loc.getZ() <= ml.biggerZ()))) {
				return true;
			} else {
				return false;
			}
		}
		return false;
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

		if (!new File(getDataFolder(), "tablist.yml").exists()) {
			saveResource("tablist.yml", false);
		}
		tablistConfig = YamlConfiguration.loadConfiguration(tablistConfigfile);

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

		if (!new File(getDataFolder(), "bossbar.yml").exists()) {
			saveResource("bossbar.yml", false);
		}
		bossBarConfig = YamlConfiguration.loadConfiguration(bossBarConfigFile);
		if (!new File(getDataFolder(), "ignore.yml").exists()) {
			saveResource("ignore.yml", false);
		}
	}

	public MemorySection getIgnoreConfig() {
		File ignoreConfigFile = new File(this.getDataFolder(), "ignore.yml");
		FileConfiguration ignoreConfig = YamlConfiguration.loadConfiguration(ignoreConfigFile);
		return ignoreConfig;
	}

	public FileConfiguration getBossBarConfig() {
		return bossBarConfig;
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
		} else if (Main.getInstance().getConfig().getString("Config.language").equalsIgnoreCase("deutsch")
				|| Main.getInstance().getConfig().getString("Config.language").equalsIgnoreCase("German")) {
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
		Bukkit.getConsoleSender().sendMessage("§cPlugin: §eLobbyReloaded");
		Bukkit.getConsoleSender().sendMessage("§cPlugin version: §e0.1");
		Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
		Bukkit.getConsoleSender().sendMessage("§cPlugin status: §4deaktivated");
		Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	}
}
