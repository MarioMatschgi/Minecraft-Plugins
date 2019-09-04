package at.mario.gravity.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import at.mario.gravity.Main;
import at.mario.gravity.gamestates.LobbyState;
import at.mario.gravity.inventories.ArenaSetupInventory;
import at.mario.gravity.inventories.MapSetupInventory;
import at.mario.gravity.manager.PackageSender;
import at.mario.gravity.manager.StatsManager;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;
import at.mario.gravity.pictureLogin.com.bobacadodl.imgmessage.ImageMessage;
import at.mario.gravity.utils.PlayerUtil;

public class GravityCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				CMDInfo(p);
			} else {
				if (args[0].equalsIgnoreCase("maps")) {
					if (args.length == 1) {
						mapCMDInfo(p);
					} else {
						if (args[1].equalsIgnoreCase("list")) {
							if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.maps.list") || p.isOp()) {
								p.sendMessage("--------§2Gravity§f: §aMaps§f---------------------------------");
								if (dm.getData().contains("Data.maps")) {
									ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.maps");
									for(String key : configSection.getKeys(false)) {
										if ( (dm.getData().contains("Data.maps." + key + ".spawn")) && (dm.getData().contains("Data.maps." + key + ".bounds.loc1")) && 
												(dm.getData().contains("Data.maps." + key + ".bounds.loc2")) ) {
											
											p.sendMessage(mm.getMessages().getString("Messages.command.arena.list.format").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", 
													key).replace("%worldname%", dm.getData().getString("Data.maps." + key + ".world")));
										} else {
											p.sendMessage(mm.getMessages().getString("Messages.command.arena.list.arenaNotComplete").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", key));
										}
									}
								} else {
									p.sendMessage(mm.getMessages().getString("Messages.command.maps.list.noMaps"));
								}
								p.sendMessage("§f------------------------------------------------------");
							} else {
								mm.sendMessage("Messages.noPermission", p);
							}
						} else if (args[1].equalsIgnoreCase("create")) {
							if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.maps.create") || p.isOp()) {
								if (args.length == 3) {
									if (dm.getData().contains("Data.maps." + args[2])) {
										p.sendMessage(mm.getMessages().getString("Messages.command.arena.alreadyCreated").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
									} else {
										dm.getData().createSection("Data.maps." + args[2]);
										
										dm.saveData();
										p.sendMessage(mm.getMessages().getString("Messages.command.arena.created").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
									}
								} else {
									mapCMDInfo(p);
								}
							} else {
								mm.sendMessage("Messages.noPermission", p);
							}
						} else if (args[1].equalsIgnoreCase("remove")) {
							if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.maps.remove") || p.isOp()) {
								if (args.length == 3) {
									if (dm.getData().contains("Data.maps." + args[2])) {
										dm.getData().set("Data.maps." + args[2], null);
										
										dm.saveData();
										p.sendMessage(mm.getMessages().getString("Messages.command.arena.removed").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
									} else {
										p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
									}
								} else {
									mapCMDInfo(p);
								}
							} else {
								mm.sendMessage("Messages.noPermission", p);
							}
						} else if (args[1].equalsIgnoreCase("setup")) {
							if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.maps.setup") || p.isOp()) {
								if (args.length == 3) {
									if (dm.getData().contains("Data.maps." + args[2])) {
										MapSetupInventory.getInstance().newInventory(args[2], p);
									} else {
										p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
									}
								} else {
									mapCMDInfo(p);
								}
							} else {
								mm.sendMessage("Messages.noPermission", p);
							}
						} else {
							if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.maps.list") || p.isOp()) {
	 							if (dm.getData().contains("Data.maps." + args[1])) {
	 								mapInfo(args, p);
								} else {
									mapCMDInfo(p);
								}
							} else {
								mm.sendMessage("Messages.noPermission", p);
							}
						}
					}
				} else if (args[0].equalsIgnoreCase("arena")) {
					if (args.length == 1) {
						CMDInfo(p);
					} else {
						if (args[1].equalsIgnoreCase("list")) {
							if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.arena.list") || p.isOp()) {
								p.sendMessage("--------§2Hide n' seek§f: §aArenas§f-----------------------------");
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
						} else if (args[1].equalsIgnoreCase("create")) {
							if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.arena.create") || p.isOp()) {
								if (args.length == 3) {
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
							if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.arena.remove") || p.isOp()) {
								if (args.length == 3) {
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
							if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.arena.setup") || p.isOp()) {
								if (args.length == 3) {
									if (dm.getData().contains("Data.arenas." + args[2])) {
										ArenaSetupInventory.getInstance().newInventory(args[2], p);
									} else {
										p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
									}
								} else {
									ArenaCMDInfo(p);
								}
							} else {
								mm.sendMessage("Messages.noPermission", p);
							}
						} else {
							if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.arena.list") || p.isOp()) {
	 							if (dm.getData().contains("Data.arenas." + args[1])) {
	 								ArenaInfo(args, p);
								} else {
									ArenaCMDInfo(p);
								}
							} else {
								mm.sendMessage("Messages.noPermission", p);
							}
						}
					}
				} else if (args[0].equalsIgnoreCase("join")) {
					if (args.length == 2) {
						if (dm.getData().contains("Data.maps." + args[1])) {
							if (Main.getInstance().getGameStateManager().getCurrentGameState(args[1]) instanceof LobbyState) {
								boolean join = true;
								
								for (String key : Main.ArenaPlayer.keySet()) {
									List<Player> players = Main.ArenaPlayer.get(key);
										
									if (players.contains(p)) {
										if (key.equals(args[1])) {
											join = false;
											p.sendMessage(mm.getMessages().getString("Messages.command.arena.join.sameArena").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
										} else {
											PlayerUtil.leaveGame(false, key, p);
											PlayerUtil.joinGame(args[1], p);
											join = false;
										}
									}
								}
								if (join) {
									PlayerUtil.joinGame(args[1], p);
								}
							} else {
								p.sendMessage(mm.getMessages().getString("Messages.command.arena.join.cantJoin").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
							}
						} else {
							p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
						}
					} else {
						mapCMDInfo(p);
					}
				} else if (args[0].equalsIgnoreCase("joinme")) {
					if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.joinme") || p.isOp()) {
						Boolean joinme = false;
						
						for (String key : Main.ArenaPlayer.keySet()) {
							List<Player> players = Main.ArenaPlayer.get(key);
							
							if (players.contains(p)) {
								joinme = true;
								
								if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
									int currentPlayers = Main.ArenaPlayer.get(key).size();

									if (Main.getInstance().getGameStateManager().getIsJoinme(key) == null) {
										Main.getInstance().getGameStateManager().setIsJoinme(key, false);
									}
									if (!Main.getInstance().getGameStateManager().getIsJoinme(key)) {
										Main.getInstance().getGameStateManager().setIsJoinme(key, true);
										
										if ((dm.getData().getDouble("Data." + p.getName() + ".joinmes") - 1) >= 0) {
											String msg = "";
											for (String line : ImageMessage.getLines()) {
												msg = msg + "\n" + line.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", key).replace("%player%", p.getName()).replace("%players%", currentPlayers+"").replace("%maxplayers%", 
														LobbyState.maxPlayers.get(key)+"").replace("%minplayers%", dm.getData().getDouble("Data.maps." + key + ".minplayers")+"");
									        }
											for (Player player : Bukkit.getOnlinePlayers()) {
												PackageSender.sendMessageWithChatHover(player, msg, mm.getMessages().getString("Messages.joinme.hover"), "/gravity join " + key);
											}
											break;
										} else {
											p.sendMessage(mm.getMessages().getString("Messages.joinme.notEnoughJoinmePasses").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
										}
									} else {
										p.sendMessage(mm.getMessages().getString("Messages.joinme.alreadyAJoinme").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
									}
								} else {
									joinme = false;
								}
							} else {
								joinme = false;
							}
						}
						if (joinme == false) {
							p.sendMessage(mm.getMessages().getString("Messages.joinme.cantCreateJoinme").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
						}
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (args[0].equalsIgnoreCase("setmainlobby")) {
					if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.setmainlobby") || p.isOp()) {
						dm.getData().set("Data.mainlobbyspawn.world", p.getLocation().getWorld().getName());
						dm.getData().set("Data.mainlobbyspawn.x", p.getLocation().getX());
						dm.getData().set("Data.mainlobbyspawn.y", p.getLocation().getY());
						dm.getData().set("Data.mainlobbyspawn.z", p.getLocation().getZ());
						dm.getData().set("Data.mainlobbyspawn.yaw", p.getLocation().getYaw());
						dm.getData().set("Data.mainlobbyspawn.pitch", p.getLocation().getPitch());
						dm.saveData();

						p.sendMessage(mm.getMessages().getString("Messages.command.setMainLobby").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (args[0].equalsIgnoreCase("stats")) {
					if (args.length == 1) {
						StatsManager.sendStats(p.getName(), p);
					} else {
						if (StatsManager.hasStats(Bukkit.getPlayer(args[1]))) {
							StatsManager.sendStats(args[1], Bukkit.getPlayer(args[1]));
						} else {
							p.sendMessage(mm.getMessages().getString("Messages.playerNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", args[1]));
						}
					}
				} else {
					CMDInfo(p);
				}
			}
		} else {
			sender.sendMessage("§cYou have to be a player");
		}
		
		return false;
	}
	
	public void mapCMDInfo(Player p) {
		p.sendMessage("§f--------§2Gravity§f----(/§agravity§f)----------------------------");
		p.sendMessage("§6/gravity maps §f| §eThe main subcommand");
		p.sendMessage(" ");
		p.sendMessage("§6/gravity maps §f| §eShows you this info");
		p.sendMessage("§6/gravity maps list §f| §eLists all maps");
		p.sendMessage("§6/gravity maps create {map} §f| §eCreates an maps {map}");
		p.sendMessage("§6/gravity maps remove {map} §f| §eRemoves an maps {map}");
		p.sendMessage("§6/gravity maps setup {map} §f| §eOpens the setup menue for the map {map}");
		p.sendMessage("§f------------------------------------------------------");
	}
	
	public void ArenaCMDInfo(Player p) {
		p.sendMessage("--------§2Hide n' seek§f--(/§ahideAndSeek arena§f)----------------");
		p.sendMessage("§6/hideAndSeek arena §f| §eThe main subcommand");
		p.sendMessage(" ");
		p.sendMessage("§6/hideAndSeek arena §f| §eShows you this info");
		p.sendMessage("§6/hideAndSeek arena list §f| §eLists all arenas");
		p.sendMessage("§6/hideAndSeek arena create {arena} §f| §eCreates an arena {arena}");
		p.sendMessage("§6/hideAndSeek arena remove {arena} §f| §eRemoves an arena {arena}");
		p.sendMessage("§6/hideAndSeek arena setup {arena} §f| §eOpens the setup menue for the arena {arena}");
		p.sendMessage("§f------------------------------------------------------");
	}

	public static void CMDInfo(Player p) {
		p.sendMessage("§f--------§2Gravity§f----------------------------------------");
		p.sendMessage("§6/gravity §f| §eThe main command");
		p.sendMessage("§6Aliasses§f: §egrav§f, §egr§f, §egravityreloaded");
		p.sendMessage(" ");
		p.sendMessage("§6/gravity help §f| §eShows you this info");
		p.sendMessage("§6/gravity maps help§f| §eShows you the info of the \"/arena\" subcommand");
		p.sendMessage("§6/gravity setmainlobby §f| §eSets the mainlobby spawn");
		p.sendMessage("§f------------------------------------------------------");
	}

	public void mapInfo(String[] args, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		
		String bounds = "  Position 1: ";
		Location pos1 = p.getLocation();
		
		if (dm.getData().contains("Data.maps." + args[1] + ".bounds.loc1")) {
			pos1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + args[1] + ".bounds.loc1.world")));
			pos1.setPitch((float) 0.0);
			pos1.setYaw((float) 0.0);
			pos1.setX(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc1.x"));
			pos1.setY(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc1.y"));
			pos1.setZ(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc1.z"));
			
			bounds = "  Position 1: " + "§nl    X: " + (int) pos1.getX() + " Y: " + (int) pos1.getY() + " Z: " + (int) pos1.getZ();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		Location pos2 = p.getLocation();
		if (dm.getData().contains("Data.maps." + args[1] + ".bounds.loc2")) {
			pos2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + args[1] + ".bounds.loc2.world")));
			pos2.setPitch((float) 0.0);
			pos2.setYaw((float) 0.0);
			pos2.setX(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc2.x"));
			pos2.setY(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc2.y"));
			pos2.setZ(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc2.z"));
			
			bounds = bounds + "§nl  Position 2: " + "§nl    X: " + (int) pos2.getX() + " Y: " + (int) pos2.getY() + " Z: " + (int) pos2.getZ();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		
		Location spawnloc = p.getLocation();
		String spawn = "";
		if (dm.getData().contains("Data.maps." + args[1] + ".spawn")) {
			spawnloc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + args[1] + ".spawn.world")));
			spawnloc.setPitch(((Number) dm.getData().get("Data.maps." + args[1] + ".spawn.pitch")).floatValue());
			spawnloc.setYaw(((Number) dm.getData().get("Data.maps." + args[1] + ".spawn.yaw")).floatValue());
			spawnloc.setX(dm.getData().getDouble("Data.maps." + args[1] + ".spawn.x"));
			spawnloc.setY(dm.getData().getDouble("Data.maps." + args[1] + ".spawn.y"));
			spawnloc.setZ(dm.getData().getDouble("Data.maps." + args[1] + ".spawn.z"));
			
			spawn = "  X: " + (int) spawnloc.getX() + " Y: " + (int) spawnloc.getY() + " Z: " + (int) spawnloc.getZ() + "§nl  Yaw: " + (int) spawnloc.getYaw() + "§nl  Pitch: " + (int) spawnloc.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}

		p.sendMessage("--------§2Gravity§f: §aArena§f-----------------------------------");
		List<String> list = mm.getMessages().getStringList("Messages.command.maps.info");
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);
			
			p.sendMessage(string.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%map%", args[1]).replace("%worldname%", 
					dm.getData().getString("Data.maps." + args[1] + ".world")).replace("%bounds%", bounds.replace("§nl", "\n")).replace("%spawn%", 
							spawn.replace("§nl", "\n")));
		}
		p.sendMessage("§f------------------------------------------------------");
	}

	public void ArenaInfo(String[] args, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		
		String lobbyspawn = " ";
		if (dm.getData().contains("Data.arenas." + args[1] + ".lobbyspawn")) {
			
			lobbyspawn = "  X: " + dm.getData().getDouble("Data.arenas." + args[1] + ".lobbyspawn.x") + " Y: " + dm.getData().getDouble("Data.arenas." + args[1] + ".lobbyspawn.y") + " Z: " + dm.getData().getDouble("Data.arenas." + args[1] + 
					".lobbyspawn.z") + "§nl  Yaw: " + (int) ((float) dm.getData().get("Data.arenas." + args[1] + ".lobbyspawn.yaw")) + "§nl  Pitch: " + (int) ((float) dm.getData().get("Data.arenas." + args[1] + ".lobbyspawn.pitch"));
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		
		String bounds = "  Position 1: ";
		Location pos1 = p.getLocation();
		
		if (dm.getData().contains("Data.arenas." + args[1] + ".bounds.loc1")) {
			pos1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + args[1] + ".bounds.loc1.world")));
			pos1.setPitch((float) dm.getData().get("Data.arenas." + args[1] + ".bounds.loc1.pitch"));
			pos1.setYaw((float) dm.getData().get("Data.arenas." + args[1] + ".bounds.loc1.yaw"));
			pos1.setX(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc1.x"));
			pos1.setY(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc1.y"));
			pos1.setZ(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc1.z"));
			
			bounds = "  Position 1: " + "§nl    X: " + (int) pos1.getX() + " Y: " + (int) pos1.getY() + " Z: " + (int) pos1.getZ() + "§nl    Yaw: " + (int) pos1.getYaw() + "§nl    Pitch: " + (int) pos1.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		Location pos2 = p.getLocation();
		if (dm.getData().contains("Data.arenas." + args[1] + ".bounds.loc2")) {
			pos2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + args[1] + ".bounds.loc2.world")));
			pos2.setPitch((float) dm.getData().get("Data.arenas." + args[1] + ".bounds.loc2.pitch"));
			pos2.setYaw((float) dm.getData().get("Data.arenas." + args[1] + ".bounds.loc2.yaw"));
			pos2.setX(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc2.x"));
			pos2.setY(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc2.y"));
			pos2.setZ(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc2.z"));
			
			bounds = bounds + "§nl  Position 2: " + "§nl    X: " + (int) pos2.getX() + " Y: " + (int) pos2.getY() + " Z: " + (int) pos2.getZ() + "§nl    Yaw: " + (int) pos2.getYaw() + "§nl    Pitch: " + (int) pos2.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		
		Location spawnloc = p.getLocation();
		String spawn = "";
		if (dm.getData().contains("Data.arenas." + args[1] + ".spawn")) {
			spawnloc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + args[1] + ".spawn.world")));
			spawnloc.setPitch((float) dm.getData().get("Data.arenas." + args[1] + ".spawn.pitch"));
			spawnloc.setYaw((float) dm.getData().get("Data.arenas." + args[1] + ".spawn.yaw"));
			spawnloc.setX(dm.getData().getDouble("Data.arenas." + args[1] + ".spawn.x"));
			spawnloc.setY(dm.getData().getDouble("Data.arenas." + args[1] + ".spawn.y"));
			spawnloc.setZ(dm.getData().getDouble("Data.arenas." + args[1] + ".spawn.z"));
			
			spawn = "  X: " + (int) spawnloc.getX() + " Y: " + (int) spawnloc.getY() + " Z: " + (int) spawnloc.getZ() + "§nl  Yaw: " + (int) spawnloc.getYaw() + "§nl  Pitch: " + (int) spawnloc.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}

		p.sendMessage("--------§2Hide n' seek§f: §aArena§f------------------------------");
		List<String> list = mm.getMessages().getStringList("Messages.command.arena.info");
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);
			
			p.sendMessage(string.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]).replace("%worldname%", 
					dm.getData().getString("Data.arenas." + args[1] + ".world")).replace("%lobbyspawn%", lobbyspawn.replace("§nl", "\n")).replace("%bounds%", bounds.replace("§nl", "\n")).replace("%spawn%", 
							spawn.replace("§nl", "\n")));
		}
		p.sendMessage("§f------------------------------------------------------");
	}
}
