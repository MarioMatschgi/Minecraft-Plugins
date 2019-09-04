package at.mario.lobby.listener;

import java.text.NumberFormat;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import at.mario.lobby.Main;
import at.mario.lobby.Pets;
import at.mario.lobby.Effects.ParticleEffect;
import at.mario.lobby.inventories.ParticleInventory;
import at.mario.lobby.manager.PackageSender;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;
import at.mario.lobby.manager.ConfigManagers.TablistCFGManager;
import at.mario.lobby.other.autoMessage.broadcasts.Broadcast;
import at.mario.lobby.other.autoMessage.broadcasts.BroadcastStatus;
import at.mario.lobby.scoreboards.MainScoreboard;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class JoinListener implements Listener {

	private Broadcast broadcast = new Broadcast();

	public static HashMap<String, Integer> taskIDs = new HashMap<String, Integer>();
	static int index = 1;
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		Pets pets = new Pets();
		
		Player p = e.getPlayer();

		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.world", p.getLocation().getWorld().getName());
		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.pitch", p.getLocation().getPitch());
		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.yaw", p.getLocation().getYaw());
		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.x", p.getLocation().getX());
		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.y", p.getLocation().getY());
		dm.getData().set("Data." + p.getName().toLowerCase() + ".position.z", p.getLocation().getZ());
		dm.saveData();
		
		
		dm.getData().set("Data." + p.getName().toLowerCase() + ".ride", "false");
		dm.getData().set("Data." + p.getName().toLowerCase() + ".hat", "false");
		if (!dm.getData().contains("Data." + p.getName().toLowerCase() + ".particle")) {
			dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "null");
		}
		if (!dm.getData().contains("Data." + p.getName().toLowerCase() + ".baby")) {
			dm.getData().set("Data." + p.getName().toLowerCase() + ".baby", "false");
		}

		if (p.hasPermission("PetsReloaded.pets.all")) {
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.wolf", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.sheep", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.chicken", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.horse", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.pig", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.cow", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.mooshroom", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.ocelot", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.rabbit", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.villager", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.squid", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.silverfish", true);
		} else {
			if (!dm.getData().contains("Data." + p.getName().toLowerCase() + ".bought")) {
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.wolf", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.sheep", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.chicken", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.horse", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.pig", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.cow", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.mooshroom", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.ocelot", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.rabbit", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.villager", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.squid", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.silverfish", false);
			}
		}
		
		if (p.hasPermission("PetsReloaded.particle.all")) {
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.dripLava", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.dripWater", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.heart", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.lava", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.note", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.redstone", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.slime", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.snowball", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.spell", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.townaura", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.villagerAngry", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.villagerHappy", true);
		} else {
			if (!dm.getData().contains("Data." + p.getName().toLowerCase() + ".bought")) {
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.dripLava", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.dripWater", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.heart", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.lava", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.note", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.redstone", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.slime", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.snowball", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.spell", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.townaura", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.villagerAngry", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.villagerHappy", false);
			}
		}
		
		if (p.hasPermission("PetsReloaded.armor.all")) {
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.helmet", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.chestplate", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.leggins", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.boots", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.helmet", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.chestplate", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.leggins", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.boots", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.helmet", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.chestplate", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.leggins", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.boots", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.helmet", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.chestplate", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.leggins", true);
			dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.boots", true);
		} else {
			if (!dm.getData().contains("Data." + p.getName().toLowerCase() + ".bought")) {
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.helmet", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.chestplate", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.leggins", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.boots", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.helmet", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.chestplate", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.leggins", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.boots", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.helmet", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.chestplate", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.leggins", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.boots", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.helmet", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.chestplate", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.leggins", false);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.boots", false);
			}
		}
		
		if (dm.getData().contains("Data.lobby.spawn")) {
			if (Main.getInstance().getConfig().getBoolean("Config.spawnAtLobbyspawn") == true) {
				Location loc = (Location) Main.getInstance().getDataConfig().get("Data.lobby.spawn");
				
				p.teleport(loc);
				Main.giveLobbyItems(p);
			}
		}	
		InventoryClick.hiddenPlayers.add(p);
		dm.saveData();
		
		new BukkitRunnable() {
			@Override
			public void run() {
				MainScoreboard.setScoreboard(p);
				
				if (Main.getInstance().getConfig().getBoolean("Config.spawnPetAtSpawn") == true) {
					if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") != null) {
						pets.createPet(p, dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet").toUpperCase());
					}
				}
				

				if (Main.getInstance().getConfig().getBoolean("Config.spawnParticleAtSpawn") == true) {
					setParticle(p);
				}
				
				if (Main.getInstance().getConfig().getBoolean("Config.joinTitle") == true) {
					PackageSender.sendTitle(p, mm.getMessages().getString("Messages.joinTitle").replace("%player%", p.getName()).replace("&", "§"), mm.getMessages().getString("Messages.joinSubtitle").replace("%player%", p.getName()).replace("&", "§"), 1, 3, 1);
				}
				setTablist(p);
				
				if (!dm.getData().contains("Data.lobby.location")) {
					if (p.isOp()) {
						mm.sendMessage("Messages.lobbyBoundsNotSet", p);
					}
				}
				if (!dm.getData().contains("Data.lobby.spawn")) {
					if (p.isOp()) {
						mm.sendMessage("Messages.spawnNotSet", p);
					}
				}
			}
		}.runTaskLater(Main.getPlugin(), 10L);
		
		
		/* Checks if chat broadcast status is not disabled. */
		if (Broadcast.getChatBroadcastStatus() == BroadcastStatus.DISABLED) {
			return;
		}
		/* TODO Comment */
		if (Broadcast.getChatBroadcastStatus() == BroadcastStatus.WAITING && Broadcast.getChatBroadcastStatus() != BroadcastStatus.DISABLED) {
			broadcast.broadcast();
		}

		if (Main.getInstance().getConfig().getBoolean("Config.customJoinMessage") == true) {
			e.setJoinMessage(mm.getMessages().getString("Messages.joinMessage").replace("%player%", p.getName()).replace("&", "§"));
		}
		
		
		if (!dm.getData().contains("Data." + p.getName().toLowerCase() + ".pet")) {
			dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
		}
		if (p.getGameMode() == GameMode.CREATIVE) {
			p.setAllowFlight(true);
		} else {
			p.setAllowFlight(false);
		}
		dm.getData().set("Data." + p.getName().toLowerCase() + ".fly", false);
		dm.saveData();
	}
	
	public void setParticle(Player p) {
		DataManager dm = new DataManager();
		
		try {
			Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
		} catch (Exception exe) { }
		
		if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") != null) {
			String str = dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle").toUpperCase();
			
			if (!str.equals("null")) {
				ParticleInventory.getInstance().summonParticle(ParticleEffect.valueOf(str), p);
			}
		}
		
		
	}
	
	public void setTablist(Player p) {
		DataManager dm = new DataManager();
		TablistCFGManager tm = new TablistCFGManager();

		if (tm.getTablistCFG().getBoolean("Tablist.enabled") == true) {
			taskIDs.put(p.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if (tm.getTablistCFG().getBoolean("Tablist.enabled") == false || p.isOnline() == false) {
						try {
							Bukkit.getScheduler().cancelTask(taskIDs.get(p.getName()));
						} catch (Exception exeption) {					}

						return;
					}
					
					if (tm.getTablistCFG().getString("Tablist.region").equalsIgnoreCase("lobby")) {
						if (!Main.isinLobby(p.getLocation())) {
							try {
								Bukkit.getScheduler().cancelTask(taskIDs.get(p.getName()));
							} catch (Exception exeption) {					}

							return;
						}
					} else if (tm.getTablistCFG().getString("Tablist.region").equalsIgnoreCase("world")) {
						Location location = (Location) dm.getData().get("Data.lobby.location.loc1");
						if (!p.getLocation().getWorld().equals(location.getWorld())) {
							try {
								Bukkit.getScheduler().cancelTask(taskIDs.get(p.getName()));
							} catch (Exception exeption) {					}

							return;
						}
					}
					
					if (!tm.getTablistCFG().contains("Tablist.lines.header." + index)) {
						index = 1;
					}
					if (!tm.getTablistCFG().contains("Tablist.lines.footer." + index)) {
						index = 1;
					}
					String prefix = PermissionsEx.getUser(p).getGroups()[0].getPrefix();
					
					Double moNey = Main.eco.getBalance(p);
					int Money = moNey.intValue();
					String money = NumberFormat.getInstance().format(Money);
					
					
					PackageSender.sendHeaderAndFooter( p, getHeader().replace("§nl", "\n").replace("%online%", Bukkit.getOnlinePlayers().size()+"").replace("%maxplayers%", Bukkit.getMaxPlayers()+
							"").replace("%player%", p.getName()).replace("%money%", money+"").replace("%rank%", prefix.replace("&", "§")), getFooter().replace("§nl", "\n").replace("%online%", Bukkit.getOnlinePlayers().size()+
									"").replace("%maxplayers%", Bukkit.getMaxPlayers()+"").replace("%player%", p.getName()).replace("%money%", money+"").replace("%rank%", prefix.replace("&", "§")) );
					index++;
				}
			}, 0, tm.getTablistCFG().getInt("Tablist.time")) );
		}
	}
	
	public void removeTablist(Player p) {
		try {
			Bukkit.getScheduler().cancelTask(taskIDs.get(p.getName()));
		} catch (Exception exeption) {					}
		PackageSender.sendHeaderAndFooter(p, "", "");
	}
	
	public String getHeader() {
		TablistCFGManager tm = new TablistCFGManager();
		
		String name = "";

		for (int i = 0; i < tm.getTablistCFG().getList("Tablist.lines.header." + index).size(); i++) {
			name = name + (String) tm.getTablistCFG().getList("Tablist.lines.header." + index).get(i) + "§nl";
		}
		
		return name;
	}
	
	public String getFooter() {
		TablistCFGManager tm = new TablistCFGManager();
		
		String name = "";

		for (int i = 0; i < tm.getTablistCFG().getList("Tablist.lines.footer." + index).size(); i++) {
			name = name + (String) tm.getTablistCFG().getList("Tablist.lines.footer." + index).get(i) + "§nl";
		}
		
		return name;
	}
}
