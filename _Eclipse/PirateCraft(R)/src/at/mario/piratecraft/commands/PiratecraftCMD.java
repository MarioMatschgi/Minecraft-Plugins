package at.mario.piratecraft.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import at.mario.piratecraft.inventories.SetupInventory;
import at.mario.piratecraft.manager.ConfigManagers.DataManager;
import at.mario.piratecraft.manager.ConfigManagers.MessagesManager;

public class PiratecraftCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				CMDInfo(p);
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("arena")) {
					ArenaCMDInfo(p);
				} else {
					CMDInfo(p);
				}
			} else {
				if (args[0].equalsIgnoreCase("arena")) {
					if (args[1].equalsIgnoreCase("info")) {
						if (args.length >= 3) {
							ArenaInfo(args[2], p);
						}
					} else if (args[1].equalsIgnoreCase("list")) {
						if (args.length >= 2) {
							if (p.hasPermission("piratecraft.admin") || p.hasPermission("piratecraft.arena.list") || p.isOp()) {
								p.sendMessage("--------§bPirate§3Craft§f: §aArenas§f-----------------------------");
								if (dm.getData().contains("Data.arenas")) {
									ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
									for(String key : configSection.getKeys(false)) {
										if ( (dm.getData().contains("Data.arenas." + key + ".lobbyspawn")) && (dm.getData().contains("Data.arenas." + key + ".spawn")) && (dm.getData().contains("Data.arenas." + key + ".bounds.loc1")) && 
												(dm.getData().contains("Data.arenas." + key + ".bounds.loc2")) ) {
											
											p.sendMessage(mm.getMessages().getString("Messages.command.arena.list.format").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", 
													key).replace("%worldname%", dm.getData().getString("Data.arenas." + key + ".world")));
										} else {
											p.sendMessage(mm.getMessages().getString("Messages.command.arena.list.arenaNotComplete").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", key));
										}
									}
								} else {
									p.sendMessage(mm.getMessages().getString("Messages.command.arena.list.noArenas"));
								}
								p.sendMessage("§f------------------------------------------------------");
							} else {
								mm.sendMessage("Messages.noPermission", p);
							}
						}
					} else if (args[1].equalsIgnoreCase("create")) {
						if (p.hasPermission("piratecraft.admin") || p.hasPermission("piratecraft.arena.create") || p.isOp()) {
							if (args.length >= 3) {
								if (dm.getData().contains("Data.arenas." + args[2])) {
									p.sendMessage(mm.getMessages().getString("Messages.command.arena.alreadyCreated").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
								} else {
									dm.getData().createSection("Data.arenas." + args[2]);
									
									dm.saveData();
									p.sendMessage(mm.getMessages().getString("Messages.command.arena.created").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
								}
							} else {
								ArenaCMDInfo(p);
							}
						} else {
							mm.sendMessage("Messages.noPermission", p);
						}
					} else if (args[1].equalsIgnoreCase("remove")) {
						if (p.hasPermission("piratecraft.admin") || p.hasPermission("piratecraft.arena.remove") || p.isOp()) {
							if (args.length >= 3) {
								if (dm.getData().contains("Data.arenas." + args[2])) {
									dm.getData().set("Data.arenas." + args[2], null);
									
									dm.saveData();
									p.sendMessage(mm.getMessages().getString("Messages.command.arena.removed").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
								} else {
									p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
								}
							} else {
								ArenaCMDInfo(p);
							}
						} else {
							mm.sendMessage("Messages.noPermission", p);
						}
					} else if (args[1].equalsIgnoreCase("setup")) {
						if (p.hasPermission("piratecraft.admin") || p.hasPermission("piratecraft.arena.setup") || p.isOp()) {
							if (args.length >= 3) {
								if (dm.getData().contains("Data.arenas." + args[2])) {
									SetupInventory.getInstance().newInventory(args[2], p);
								} else {
									p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
								}
							} else {
								ArenaCMDInfo(p);
							}
						}
					}
					
					else {
						ArenaInfo(args[1], p);
					}
				}
			}
		} else {
			sender.sendMessage("§cYou have to be a player");
		}
		
		return false;
	}
	
	public void ArenaCMDInfo(Player p) {
		p.sendMessage("--------§bPirate§3Craft§f--(/§apiratecraft arena§f)----------------");
		p.sendMessage("§6/piratecraft arena §f| §eThe main subcommand");
		p.sendMessage(" ");
		p.sendMessage("§6/piratecraft arena list §f| §eLists all arenas");
		p.sendMessage("§6/piratecraft arena info {arenaName} §f| §eGives you the info of the arena {arenaName}");
		p.sendMessage("§6/piratecraft arena create {arenaName} §f| §eCreates an arena named {arenaName}");
		p.sendMessage("§6/piratecraft arena remove {arenaName} §f| §eRemoves the arena named {arenaName}");
		p.sendMessage("§6/piratecraft arena setup {arenaName} §f| §eOpens the setup menue for the arena {arenaName}");
		p.sendMessage("§f------------------------------------------------------");
	}

	public static void CMDInfo(Player p) {
		p.sendMessage("§f--------§bPirate§3Craft§f----------------------------------------");
		p.sendMessage("§6/piratecraft §f| §eThe main command");
		p.sendMessage("§6Aliasses§f: §epc§f, §epcr§f, §epiratecraftreloaded");
		p.sendMessage(" ");
		p.sendMessage("§6/piratecraft help §f| §eShows you this info");
		p.sendMessage("§6/piratecraft arena help§f| §eShows you the info of the \"/arena\" subcommand");
		p.sendMessage("§f------------------------------------------------------");
	}

	public void ArenaInfo(String arenaName, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		
		String lobbyspawn = " ";
		if (dm.getData().contains("Data.arenas." + arenaName + ".lobbyspawn")) {
			
			lobbyspawn = "  X: " + dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.x") + " Y: " + dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.y") + " Z: " + 
					dm.getData().getDouble("Data.arenas." + arenaName + ".lobbyspawn.z") + "§nl  Yaw: " + (int) ((float) dm.getData().get("Data.arenas." + arenaName + ".lobbyspawn.yaw")) + "§nl  Pitch: " + 
					(int) ((float) dm.getData().get("Data.arenas." + arenaName + ".lobbyspawn.pitch"));
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName));
			return;
		}
		
		String bounds = "  Position 1: ";
		Location pos1 = p.getLocation();
		
		if (dm.getData().contains("Data.arenas." + arenaName + ".bounds.loc1")) {
			pos1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc1.world")));
			pos1.setPitch((float) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.pitch"));
			pos1.setYaw((float) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc1.yaw"));
			pos1.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.x"));
			pos1.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.y"));
			pos1.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc1.z"));
			
			bounds = "  Position 1: " + "§nl    X: " + (int) pos1.getX() + " Y: " + (int) pos1.getY() + " Z: " + (int) pos1.getZ() + "§nl    Yaw: " + (int) pos1.getYaw() + "§nl    Pitch: " + (int) pos1.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName));
			return;
		}
		Location pos2 = p.getLocation();
		if (dm.getData().contains("Data.arenas." + arenaName + ".bounds.loc2")) {
			pos2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".bounds.loc2.world")));
			pos2.setPitch((float) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.pitch"));
			pos2.setYaw((float) dm.getData().get("Data.arenas." + arenaName + ".bounds.loc2.yaw"));
			pos2.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.x"));
			pos2.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.y"));
			pos2.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".bounds.loc2.z"));
			
			bounds = bounds + "§nl  Position 2: " + "§nl    X: " + (int) pos2.getX() + " Y: " + (int) pos2.getY() + " Z: " + (int) pos2.getZ() + "§nl    Yaw: " + (int) pos2.getYaw() + "§nl    Pitch: " + (int) pos2.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName));
			return;
		}
		
		Location spawnloc = p.getLocation();
		String spawn = "";
		if (dm.getData().contains("Data.arenas." + arenaName + ".spawn")) {
			spawnloc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arenaName + ".spawn.world")));
			spawnloc.setPitch((float) dm.getData().get("Data.arenas." + arenaName + ".spawn.pitch"));
			spawnloc.setYaw((float) dm.getData().get("Data.arenas." + arenaName + ".spawn.yaw"));
			spawnloc.setX(dm.getData().getDouble("Data.arenas." + arenaName + ".spawn.x"));
			spawnloc.setY(dm.getData().getDouble("Data.arenas." + arenaName + ".spawn.y"));
			spawnloc.setZ(dm.getData().getDouble("Data.arenas." + arenaName + ".spawn.z"));
			
			spawn = "  X: " + (int) spawnloc.getX() + " Y: " + (int) spawnloc.getY() + " Z: " + (int) spawnloc.getZ() + "§nl  Yaw: " + (int) spawnloc.getYaw() + "§nl  Pitch: " + (int) spawnloc.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName));
			return;
		}

		p.sendMessage("--------§bPirate§3Craft§f: §aArena§f------------------------------");
		List<String> list = mm.getMessages().getStringList("Messages.command.arena.info");
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);
			
			p.sendMessage(string.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName).replace("%worldname%", 
					dm.getData().getString("Data.arenas." + arenaName + ".world")).replace("%lobbyspawn%", lobbyspawn.replace("§nl", "\n")).replace("%bounds%", bounds.replace("§nl", "\n")).replace("%spawn%", 
							spawn.replace("§nl", "\n")));
		}
		p.sendMessage("§f------------------------------------------------------");
	}
}
