package at.mario.hidenseek.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.inventories.SetupInventory;
import at.mario.hidenseek.manager.PackageSender;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;
import at.mario.hidenseek.manager.ConfigManagers.StatsManager;
import at.mario.hidenseek.pictureLogin.com.bobacadodl.imgmessage.ImageMessage;

public class HideNSeekCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				CMDInfo(p);
			} else {
				if (args[0].equalsIgnoreCase("arena")) {
					if (args.length == 1) {
						CMDInfo(p);
					} else {
						if (args[1].equalsIgnoreCase("list")) {
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
						} else if (args[1].equalsIgnoreCase("create")) {
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
						} else if (args[1].equalsIgnoreCase("remove")) {
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
						} else if (args[1].equalsIgnoreCase("setup")) {
							if (args.length == 3) {
								if (dm.getData().contains("Data.arenas." + args[2])) {
									SetupInventory.getInstance().newInventory(args[2], p);
								} else {
									p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
								}
							} else {
								ArenaCMDInfo(p);
							}
						} else {
 							 if (dm.getData().contains("Data.arenas." + args[1])) {
 								 ArenaInfo(args, p);
							} else {
								ArenaCMDInfo(p);
							}
						}
					}
				} else if (args[0].equalsIgnoreCase("join")) {
					if (args.length == 2) {
						if (dm.getData().contains("Data.arenas." + args[1])) {
							if (Main.getInstance().getGameStateManager().getCurrentGameState(args[1]) instanceof LobbyState) {
								boolean join = true;
								
								for (String key : Main.ArenaPlayer.keySet()) {
									List<Player> players = Main.ArenaPlayer.get(key);
										
									if (players.contains(p)) {
										if (key.equals(args[1])) {
											join = false;
											p.sendMessage(mm.getMessages().getString("Messages.command.arena.join.sameArena").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
										} else {
											Main.leaveGame(key, p);
											Main.joinGame(args[1], p);
											join = false;
										}
									}
								}
								if (join) {
									Main.joinGame(args[1], p);
								}
							} else {
								p.sendMessage(mm.getMessages().getString("Messages.command.arena.join.cantJoin").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
							}
						} else {
							p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[2]));
						}
					} else {
						ArenaCMDInfo(p);
					}
				} else if (args[0].equalsIgnoreCase("joinme")) {
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
									
									if ((dm.getData().getInt("Data." + p.getName() + ".joinmes") - 1) >= 0) {
										String msg = "";
										for (String line : ImageMessage.getLines()) {
											msg = msg + "\n" + line.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", key).replace("%player%", p.getName()).replace("%players%", currentPlayers+"").replace("%maxplayers%", 
													LobbyState.maxPlayers.get(key)+"").replace("%minplayers%", dm.getData().getInt("Data.arenas." + key + ".minplayers")+"");
								        }
										for (Player player : Bukkit.getOnlinePlayers()) {
											PackageSender.sendMessageWithChatHover(player, msg, mm.getMessages().getString("Messages.joinme.hover"), "/hs join " + key);
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
				} else if (args[0].equalsIgnoreCase("setmainlobby")) {
					dm.getData().set("Data.mainlobbyspawn", p.getLocation());
					dm.saveData();

					p.sendMessage(mm.getMessages().getString("Messages.command.setMainLobby").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
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
		p.sendMessage("--------§2Hide n' seek§f------------------------------------");
		p.sendMessage("§6/hideAndSeek §f| §eThe main command");
		p.sendMessage("§6Aliasses§f: §ehs§f, §ehns§f, §ehidenseek");
		p.sendMessage(" ");
		p.sendMessage("§6/hideAndSeek help §f| §eShows you this info");
		p.sendMessage("§6/hideAndSeek arena §f| §eShows you the info of the \"/arena\" subcommand");
		p.sendMessage("§6/hideAndSeek setmainlobby §f| §eSets the mainlobby spawn");
		p.sendMessage("§f------------------------------------------------------");
	}

	public void ArenaInfo(String[] args, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
			
		Location lobbyLoc = null;
		String lobbyspawn = " ";
		if (dm.getData().contains("Data.arenas." + args[1] + ".lobbyspawn")) {
			lobbyLoc = (Location) dm.getData().get("Data.arenas." + args[1] + ".lobbyspawn");
			lobbyspawn = "  X: " + (int) lobbyLoc.getX() + " Y: " + (int) lobbyLoc.getY() + " Z: " + (int) lobbyLoc.getZ() + "§nl  Yaw: " + (int) lobbyLoc.getYaw() + "§nl  Pitch: " + (int) lobbyLoc.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		
		String bounds = "  Position 1: ";
		Location pos1 = null;
		if (dm.getData().contains("Data.arenas." + args[1] + ".bounds.loc1")) {
			pos1 = (Location) dm.getData().get("Data.arenas." + args[1] + ".bounds.loc1");
			bounds = "  Position 1: " + "§nl    X: " + (int) pos1.getX() + " Y: " + (int) pos1.getY() + " Z: " + (int) pos1.getZ() + "§nl    Yaw: " + (int) pos1.getYaw() + "§nl    Pitch: " + (int) pos1.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		Location pos2 = null;
		if (dm.getData().contains("Data.arenas." + args[1] + ".bounds.loc1")) {
			pos2 = (Location) dm.getData().get("Data.arenas." + args[1] + ".bounds.loc2");
			bounds = bounds + "§nl  Position 2: " + "§nl    X: " + (int) pos2.getX() + " Y: " + (int) pos2.getY() + " Z: " + (int) pos2.getZ() + "§nl    Yaw: " + (int) pos2.getYaw() + "§nl    Pitch: " + (int) pos2.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		
		Location spawnloc = null;
		String spawn = "";
		if (dm.getData().contains("Data.arenas." + args[1] + ".spawn")) {
			spawnloc = (Location) dm.getData().get("Data.arenas." + args[1] + ".spawn");
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
