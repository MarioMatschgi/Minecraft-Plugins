package at.mario.lobby.commands;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lobby.Main;
import at.mario.lobby.inventories.MainInventory;
import at.mario.lobby.inventories.TeleportEditInventory;
import at.mario.lobby.inventories.TeleportSortInventory;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;
import at.mario.lobby.manager.ConfigManagers.ScoreboardCFGManager;
import at.mario.lobby.other.autoMessage.broadcasts.Broadcast;
import at.mario.lobby.other.autoMessage.util.IgnoreManager;
import at.mario.lobby.other.autoMessage.util.MessageManager;
import net.minecraft.server.v1_8_R3.EntityLiving;

public class LobbyCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				if (dm.getData().contains("Data.lobby.spawn")) {
					Main.giveLobbyItems(p);
					
					Location loc = (Location) Main.getInstance().getDataConfig().get("Data.lobby.spawn", p.getLocation());
					p.teleport(loc);
				} else {
					mm.sendMessage("Messages.spawnNotSet", p);
				}
			} else {
				if (args[0].equalsIgnoreCase("setspawn")) {
					if (p.hasPermission("lobby.admin") || p.isOp()) {
						Main.getInstance().getDataConfig().set("Data.lobby.spawn", p.getLocation());
						dm.saveData();
						
						mm.sendMessage("Messages.spawnSet", p);
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (args[0].equalsIgnoreCase("inv") || args[0].equalsIgnoreCase("inventory")) {
					MainInventory.getInstance().newInventory(p);
				} else if (args[0].equalsIgnoreCase("setloc1")) {
					if (p.hasPermission("lobby.admin") || p.isOp()) {
						dm.getData().set("Data.lobby.location.loc1.x", p.getLocation().getBlockX());
						dm.getData().set("Data.lobby.location.loc1.y", p.getLocation().getBlockY());
						dm.getData().set("Data.lobby.location.loc1.z", p.getLocation().getBlockZ());
						dm.getData().set("Data.lobby.location.loc1.world", p.getLocation().getWorld().getName());
//						dm.getData().set("Data.lobby.location.loc1", p.getLocation());
						dm.saveData();
						
						mm.sendReplacedMessage("%prefix% §aSuccesfully set the second position of the lobby boundaries!", p);
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (args[0].equalsIgnoreCase("setloc2")) {
					if (p.hasPermission("lobby.admin") || p.isOp()) {
						dm.getData().set("Data.lobby.location.loc2.x", p.getLocation().getBlockX());
						dm.getData().set("Data.lobby.location.loc2.y", p.getLocation().getBlockY());
						dm.getData().set("Data.lobby.location.loc2.z", p.getLocation().getBlockZ());
						dm.getData().set("Data.lobby.location.loc2.world", p.getLocation().getWorld().getName());
//						dm.getData().set("Data.lobby.location.loc2", p.getLocation());
						dm.saveData();

						mm.sendReplacedMessage("%prefix% §aSuccesfully set the first position of the lobby boundaries!", p);
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (args[0].equalsIgnoreCase("wand")) {
					if (p.hasPermission("lobby.admin") || p.isOp()) {
						ItemStack wand = new ItemStack(Material.BLAZE_ROD);
						ItemMeta wandMeta = wand.getItemMeta();
						wandMeta.setDisplayName(Main.wandName);
						ArrayList<String> wandList = new ArrayList<String>();
						wandList.add("§2Leftclick to set the first position");
						wandList.add("§aRightclick to set the second position");
						wandMeta.setLore(wandList);
						wand.setItemMeta(wandMeta);
						
						p.getInventory().addItem(wand);
						mm.sendReplacedMessage("%prefix% §aSuccesfully given the lobby wand to you", p);
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (args[0].equalsIgnoreCase("sorttp")) {
					if (p.hasPermission("lobby.admin") || p.isOp()) {
						TeleportSortInventory.getInstance().newInventory(false, p);
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (args[0].equalsIgnoreCase("addtp")) {
					if (p.hasPermission("lobby.admin") || p.isOp()) {
						if (args.length > 1) {
							String name = "";
							for (int i = 1; i < (args.length); i++) {
								name = name + " " + args[i];
							}
							dm.getData().set("temp.name", name);
							dm.saveData();
							
							TeleportEditInventory.getInstance().newInventory(p);
						} else {
							mm.sendMessage("Messages.tp.falseLenght", p);
						}
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (args[0].equalsIgnoreCase("removetp")) {
					if (p.hasPermission("lobby.admin") || p.isOp()) {
						if (args.length > 1) {
							String name = "";
							for (int i = 1; i < (args.length); i++) {
								name = name + " " + args[i];
							}
							
							for (int i = 0; i <= (dm.getData().getInt("Data.teleporter.index") - 1); i++) {
								if (dm.getData().get("Data.teleporter." + i + ".item") != null) {
									if (dm.getData().get("Data.teleporter." + i + ".name").equals(name)) {
										dm.getData().set("Data.teleporter." + i, null);
										dm.saveData();
										p.sendMessage(mm.getMessages().getString("Messages.tp.removetp.succeed").replace("%tpname%", name).replace("%prefix%", mm.getMessages().getString("Messages.prefix").replace("%player%", p.getName())));
										
										return true;
									}
								}
							}
							p.sendMessage(mm.getMessages().getString("Messages.tp.removetp.couldNotFind").replace("%tpname%", name).replace("%prefix%", mm.getMessages().getString("Messages.prefix").replace("%player%", p.getName())));
						} else {
							mm.sendMessage("Messages.tp.falseLenght", p);
						}
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (p.hasPermission("lobby.admin") || p.isOp()) {
						ScoreboardCFGManager sm = new ScoreboardCFGManager();
						Broadcast broadcast = new Broadcast();
						IgnoreManager ignoreManager = new IgnoreManager();
						MessageManager messageManager = new MessageManager();
						
						dm.reloadData();
						mm.reloadMessages();
						sm.reloadScoreboard();
						
						Main.getInstance().reloadConfig();

						/* Loads chat and boss bar messages and permissions as well as chat prefix and suffix. */
						messageManager.loadAll();
						/* Loads chat and boss bar ignore list. */
						ignoreManager.loadChatIgnoreList();
						ignoreManager.loadBossBarIgnoreList();
						/* Starts broadcast(s) after checking their status. */
						broadcast.broadcast();

						
						if (Main.getInstance().setupEconomy()) {
							// Bukkit.getConsoleSender().sendMessage("§aEco + Vault!");
						} else {
							// Bukkit.getConsoleSender().sendMessage("§cEco + Vault! Error!");
						}
						Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
				        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eLobbyReloaded");
				        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
				        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
				        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §breloaded");
				        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
				        
				        mm.sendReplacedMessage("%prefix% §bLobby§3Reloaded §ahas been sucessfully reloaded", p);
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (args[0].equalsIgnoreCase("setDailyreward")) {
					if (p.hasPermission("lobby.admin") || p.isOp()) {
						Entity entity = p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
						
						entity.setCustomName(mm.getMessages().getString("Messages.dailyReward.dailyRewardVillagerName"));
						entity.setCustomNameVisible(true);
						
						Main.setNoAI(entity);
						
						Villager villager = (Villager) entity;
						villager.setAdult();
						villager.setCanPickupItems(false);
						villager.setProfession(Profession.FARMER);
						
						mm.sendMessage("Messages.dailyReward.setDailyReward", p);
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (args[0].equalsIgnoreCase("removeDailyreward")) {
					if (p.hasPermission("lobby.admin") || p.isOp()) {
						int number = 0;
						
						for (int i = 0; i < Bukkit.getWorlds().size(); i++) {
							String worldname = (Bukkit.getWorlds().get(i) ).getName();
							
							for (Entity entity : Bukkit.getWorld(worldname).getEntities()) {
								String name = entity.getCustomName();
								if (entity.isCustomNameVisible()) {
									if (name.equals(mm.getMessages().getString("Messages.dailyReward.dailyRewardVillagerName")) ) {
										entity.remove();
										number ++;
										p.getWorld().playSound(p.getLocation(), Sound.WITHER_DEATH, 3.0F, 0.533F);
									}
								}
							}
						}
						p.sendMessage(mm.getMessages().getString("Messages.dailyReward.removeDailyReward").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%number%", number+""));
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else {
					cmdInfo(p);
				}
			}
		} else if (args[0].equalsIgnoreCase("reload")) {
			ScoreboardCFGManager sm = new ScoreboardCFGManager();
			Broadcast broadcast = new Broadcast();
			IgnoreManager ignoreManager = new IgnoreManager();
			MessageManager messageManager = new MessageManager();
			
			dm.reloadData();
			mm.reloadMessages();
			sm.reloadScoreboard();
			
			Main.getInstance().reloadConfig();

			/* Loads chat and boss bar messages and permissions as well as chat prefix and suffix. */
			messageManager.loadAll();
			/* Loads chat and boss bar ignore list. */
			ignoreManager.loadChatIgnoreList();
			ignoreManager.loadBossBarIgnoreList();
			/* Starts broadcast(s) after checking their status. */
			broadcast.broadcast();

			
			if (Main.getInstance().setupEconomy()) {
				// Bukkit.getConsoleSender().sendMessage("§aEco + Vault!");
			} else {
				// Bukkit.getConsoleSender().sendMessage("§cEco + Vault! Error!");
			}
			Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	        Bukkit.getConsoleSender().sendMessage("§cPlugin: §eLobbyReloaded");
	        Bukkit.getConsoleSender().sendMessage("§cPlugin version: " + Main.getPlugin().getDescription().getVersion());
	        Bukkit.getConsoleSender().sendMessage("§cPlugin author: §eMario_Matschgi");
	        Bukkit.getConsoleSender().sendMessage("§cPlugin status: §breloaded");
	        Bukkit.getConsoleSender().sendMessage("§7-------------==+==-------------");
	        
	        String msg = "%prefix% §bLobby§3Reloaded §ahas been sucessfully reloaded";
	        sender.sendMessage(msg.replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
		} else {
			sender.sendMessage(ChatColor.RED + "You have to be a Player!");
		}
		return true;
	}

	public static void cmdInfo(Player p) {
		p.sendMessage("-------§3Lobby§bReloaded§f----------------------------------");
		p.sendMessage("§6/lobby §f| §eMain command");
		p.sendMessage("§6Aliases: lb, hub, spawn, l");
		p.sendMessage("  ");
		p.sendMessage("§b/lobby §f| §3Teleports you to the lobby");
		p.sendMessage("§b/lobby help §f| §3Shows this info");
		p.sendMessage("  ");
		p.sendMessage("§c/lobby (inv/inventory) §f| §4opens the profile inventory");
		if (p.hasPermission("lobby.admin") || p.isOp()) {
			p.sendMessage("§c/lobby setspawn §f| §4Sets the spawn of the lobby");
			p.sendMessage("§c/lobby setloc1 §f| §4Sets the first bound of the lobby");
			p.sendMessage("§c/lobby setloc2 §f| §4Sets the second bound of the lobby");
			p.sendMessage("§c/lobby wand §f| §4Gives you the wand to set the lobby bounds");
			p.sendMessage("§c/lobby sorttp §f| §4Opens an Inventory to sort the tp inventory");
			p.sendMessage("§c/lobby addtp {name} §f| §4Adds a tp with name {name}");
			p.sendMessage("§c/lobby removetp {name} §f| §4Removes a tp with name {name}");
			p.sendMessage("§c/lobby reload §f| §4Reloads the plugin");
			p.sendMessage("  ");
			p.sendMessage("§d/motd §f| §5Sets the MOTD of the server");
		}
		if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.broadcast") || p.isOp()) {
			p.sendMessage("§d/broadcast help §f| §5For broadcast help");
		}
		if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.vip") || p.hasPermission("lobby.builder") || p.hasPermission("lobby.vanish") || p.isOp()) {
			p.sendMessage("  ");
			p.sendMessage("§a/vanish §f| §2Makes you invisible");
		}
		if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.builder") || p.isOp()) {
			p.sendMessage("  ");
			p.sendMessage("§a/build §f| §2You can build blocks in the lobby");
		}
		if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.builder") || p.hasPermission("lobby.fly") || p.hasPermission("lobby.vip") || p.isOp()) {
			p.sendMessage("  ");
			p.sendMessage("§a/fly §f| §2You can fly");
		}
		p.sendMessage("  ");
		p.sendMessage("§a/visibility §f| §2Openes the gui to togggle visibility");
		if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.vip") || p.hasPermission("lobby.builder") || p.hasPermission("lobby.silenthub") || p.isOp()) {
			p.sendMessage("  ");
			p.sendMessage("§a/silenthub §f| §2Toggles silenthub");
		}
		p.sendMessage("§f------------------------------------------------------");
	}
	
	
	
	
	
	public static void cmdInfo2(Player p) {
		if (p.hasPermission("lobby.admin")) {
			p.sendMessage("----§3Lobby§bReloaded§f--------------------------------------");
			p.sendMessage("§6/lobby §f| §eMain command");
			p.sendMessage("§6Aliases: lb, hub, spawn, l");
			p.sendMessage("  ");
			p.sendMessage("§b/lobby §f| §3Teleports you to the lobby");
			p.sendMessage("§b/lobby help §f| §3Shows this info");
			p.sendMessage("  ");
			p.sendMessage("§c/lobby setspawn §f| §4Sets the spawn of the lobby");
			p.sendMessage("§c/lobby setloc1 §f| §4Sets the first bound of the lobby");
			p.sendMessage("§c/lobby setloc2 §f| §4Sets the second bound of the lobby");
			p.sendMessage("§c/lobby wand §f| §4Gives you the wand to set the lobby bounds");
			p.sendMessage("§c/lobby (inv/inventory) §f| §4opens the profile inventory");
			p.sendMessage("§c/lobby sorttp §f| §4Opens an Inventory to sort the tp inventory");
			p.sendMessage("§c/lobby addtp {name} §f| §4Adds a tp with name {name}");
			p.sendMessage("§c/lobby removetp {name} §f| §4Removes a tp with name {name}");
			p.sendMessage("§c/lobby reload §f| §4Reloads the plugin");
			p.sendMessage("  ");
			p.sendMessage("§d/motd §f| §5Sets the MOTD of the server");
			p.sendMessage("§d/broadcast help §f| §5For broadcast help");
			p.sendMessage("  ");
			p.sendMessage("§a/vanish §f| §2Makes you invisible");
			p.sendMessage("§a/build §f| §2You can build blocks in the lobby");
			p.sendMessage("§a/fly §f| §2You can fly");
			p.sendMessage("§a/visibility §f| §2Openes the gui to togggle visibility");
			p.sendMessage("§a/silenthub §f| §2Toggles silenthub");
			p.sendMessage("§f------------------------------------------------------");
		} else if (p.hasPermission("lobby.builder")) {
			p.sendMessage("----§3Lobby§bReloaded§f--------------------------------------");
			p.sendMessage("§6/lobby §f| §eMain command");
			p.sendMessage("§6Aliases: lb, hub, spawn, l");
			p.sendMessage("  ");
			p.sendMessage("§b/lobby §f| §3Teleports you to the lobby");
			p.sendMessage("§b/lobby help §f| §3Shows this info");
			p.sendMessage("§c/lobby (inv/inventory) §f| §4opens the profile inventory");
			p.sendMessage("  ");
			p.sendMessage("§a/vanish §f| §2Makes you invisible");
			p.sendMessage("§a/build §f| §2You can build blocks in the lobby");
			p.sendMessage("§a/fly §f| §2You can fly");
			p.sendMessage("§a/visibility §f| §2Openes the gui to togggle visibility");
			p.sendMessage("§a/silenthub §f| §2Toggles silenthub");
			p.sendMessage("§f------------------------------------------------------");
		} else if (p.hasPermission("lobby.vip")) {
			p.sendMessage("----§3Lobby§bReloaded§f--------------------------------------");
			p.sendMessage("§6/lobby §f| §eMain command");
			p.sendMessage("§6Aliases: lb, hub, spawn, l");
			p.sendMessage("  ");
			p.sendMessage("§b/lobby §f| §3Teleports you to the lobby");
			p.sendMessage("§b/lobby help §f| §3Shows this info");
			p.sendMessage("§c/lobby (inv/inventory) §f| §4opens the profile inventory");
			p.sendMessage("  ");
			p.sendMessage("§a/vanish §f| §2Makes you invisible");
			p.sendMessage("§a/build §f| §2You can build blocks in the lobby");
			p.sendMessage("§a/fly §f| §2You can fly");
			p.sendMessage("§a/visibility §f| §2Openes the gui to togggle visibility");
			p.sendMessage("§a/silenthub §f| §2Toggles silenthub");
			p.sendMessage("§f------------------------------------------------------");
		} else {
			p.sendMessage("----§3Lobby§bReloaded§f--------------------------------------");
			p.sendMessage("§b/lobby §f| §3Teleports you to the lobby");
			p.sendMessage("§b/lobby help §f| §3Shows this info");
			p.sendMessage("§c/lobby (inv/inventory) §f| §4opens the profile inventory");
			p.sendMessage("§f-------------------------------------------------------");
		}
	}
	
	public static void setNoAITag(EntityLiving ent , boolean noAI) {
		try {
			String pack = Bukkit.getServer().getClass().getPackage().getName();
			String version = pack.substring(pack.lastIndexOf(".") + 1);
			
			Method k = Class.forName("net.minecraft.server." + version + ".EntityInsentient").getMethod("k", boolean.class);
			Object entity = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftEntity").getMethod("getHandle").invoke(ent);
			
			k.invoke(entity, noAI);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
