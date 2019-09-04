package at.mario.gravity.listener;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import at.mario.gravity.Main;
import at.mario.gravity.gamestates.EndingState;
import at.mario.gravity.gamestates.IngameState;
import at.mario.gravity.gamestates.LobbyState;
import at.mario.gravity.manager.PackageSender;
import at.mario.gravity.manager.StatsManager;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;
import at.mario.gravity.pictureLogin.com.bobacadodl.imgmessage.ImageMessage;
import at.mario.gravity.pictureLogin.me.itsnathang.picturelogin.util.PictureWrapper;
import at.mario.gravity.utils.PlayerUtil;

public class PreCommandListener implements Listener {
	
	private Main plugin;
	private Player player;
	
	
	@EventHandler
	public void preCommand(PlayerCommandPreprocessEvent e) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		plugin = Main.getPlugin();
		player = e.getPlayer();
		Player p = e.getPlayer();
		
		
		for (String key : Main.ArenaPlayer.keySet()) {
			List<Player> players = Main.ArenaPlayer.get(key);
			List<Player> spectatePlayers = Main.SpectateArenaPlayer.get(key);
			
			if (players != null && players.contains(p)) {
				if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState || Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState || 
						Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
					if (Main.getInstance().getConfig().getBoolean("Config.blockedCommands.useAsWhitelist")) {
						if (!(Main.getInstance().getConfig().getStringList("Config.blockedCommands.commands").contains(e.getMessage().substring(1))) ) {
							p.sendMessage(mm.getMessages().getString("Messages.enteredBlockedCommand").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							e.setCancelled(true);
						}
					} else {
						if (Main.getInstance().getConfig().getStringList("Config.blockedCommands.commands").contains(e.getMessage().substring(1))) {
							p.sendMessage(mm.getMessages().getString("Messages.enteredBlockedCommand").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							e.setCancelled(true);
						}
					}
				}
			} else if (spectatePlayers != null && spectatePlayers.contains(p)) {
				if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState || Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState || 
						Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
					if (Main.getInstance().getConfig().getBoolean("Config.blockedCommandsForSpectator.useAsWhitelist")) {
						if (!(Main.getInstance().getConfig().getStringList("Config.blockedCommandsForSpectator.commands").contains(e.getMessage().substring(1))) ) {
							p.sendMessage(mm.getMessages().getString("Messages.enteredBlockedCommand").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							e.setCancelled(true);
						}
					} else {
						if (Main.getInstance().getConfig().getStringList("Config.blockedCommandsForSpectator.commands").contains(e.getMessage().substring(1))) {
							p.sendMessage(mm.getMessages().getString("Messages.enteredBlockedCommand").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							e.setCancelled(true);
						}
					}
				}
			}
			
			if (players.contains(p)) {
				if (e.getMessage().toLowerCase().startsWith("/l") || e.getMessage().toLowerCase().startsWith("/lobby") || e.getMessage().toLowerCase().startsWith("/lb") || e.getMessage().toLowerCase().startsWith("/spawn") || 
					e.getMessage().toLowerCase().startsWith("/hub")) {
					if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState || Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState || 
							Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
						PlayerUtil.leaveGame(true, key, p);
						
						e.setCancelled(true);
					}
				}
				if (e.getMessage().toLowerCase().startsWith("/v") || e.getMessage().toLowerCase().startsWith("/vote")) {
					if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
						if (Main.isInteger(e.getMessage().split(" ")[1])) {
							if (Main.parseInt(e.getMessage().split(" ")[1]) <= Main.getInstance().getConfig().getInt("Config.levelAmount") && Main.parseInt(e.getMessage().split(" ")[1]) >= 0) {
								int mapInt = Main.parseInt(e.getMessage().split(" ")[1]) - 1;
								
								HashMap<Integer, List<ConfigurationSection> > usedMaps = LobbyState.ArenaMaps.get(key);
								List<ConfigurationSection> maps = usedMaps.get(mapInt);
								
									
								HashMap<List<ConfigurationSection>, Integer> ArenaVotesMap = new HashMap<List<ConfigurationSection>, Integer>();
								if (LobbyState.ArenaVotes.containsKey(key) && LobbyState.ArenaVotes.get(key) != null) {
									ArenaVotesMap = LobbyState.ArenaVotes.get(key);
								}
								if (ArenaVotesMap.isEmpty()) {
									for (List<ConfigurationSection> configurationSection : usedMaps.values()) {
										ArenaVotesMap.put(configurationSection, 0);
									}
									
									ArenaVotesMap.put(maps, 1);
								} else {
									if (LobbyState.PlayerVotes.containsKey(key) && LobbyState.PlayerVotes.get(key) != null) {
										if (LobbyState.PlayerVotes.get(key).containsKey(p) && LobbyState.PlayerVotes.get(key).get(p) != null) {
											List<ConfigurationSection> maps2 = LobbyState.PlayerVotes.get(key).get(p);
											if (ArenaVotesMap.containsKey(maps2)) {
												ArenaVotesMap.put(maps2, ArenaVotesMap.get(maps2) - 1);
											}
										}
									}
									if (ArenaVotesMap.containsKey(maps)) {
										ArenaVotesMap.put(maps, ArenaVotesMap.get(maps) + 1);
									} else {
										ArenaVotesMap.put(maps, 1);
									}
								}
								HashMap<Player, List<ConfigurationSection>> PlayerVotesMap = new HashMap<Player, List<ConfigurationSection>>();
								if (LobbyState.PlayerVotes.containsKey(key) && LobbyState.PlayerVotes.get(key) != null) {
									PlayerVotesMap = LobbyState.PlayerVotes.get(key);
								}
								PlayerVotesMap.put(p, maps);
								LobbyState.PlayerVotes.put(key, PlayerVotesMap);
								LobbyState.ArenaVotes.put(key, ArenaVotesMap);
								
								PlayerUtil.sendVoteInfo(key, p);
							} else {
								p.sendMessage(mm.getMessages().getString("Messages.enterANumberBetween").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%num1%", "1").replace("%num2%", 
										Main.getInstance().getConfig().getInt("Config.levelAmount")+""));
							}
						} else {
							p.sendMessage(mm.getMessages().getString("Messages.enterANumber").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%cmd%", "/vote "));
						}
						
						e.setCancelled(true);
					}
					return;
				}
				if (e.getMessage().toLowerCase().startsWith("/start")) {
					if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.arena.start") || p.isOp()) {
						if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
							LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(key);
						
							if (lobbyState.getLobbycountdown().isRunning(key)) {
								if (!(lobbyState.getLobbycountdown().getSeconds(key) <= 3) ) {
									lobbyState.getLobbycountdown().setSeconds(3, key);
									e.setCancelled(true);
								} else {
									p.sendMessage(mm.getMessages().getString("Messages.command.start.gameAlreadyStarting"));
									e.setCancelled(true);
								}
							} else {
								p.sendMessage(mm.getMessages().getString("Messages.command.start.notEnoughPlayers"));
								e.setCancelled(true);
							}
						}
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (e.getMessage().toLowerCase().startsWith("/joinme")) {
					if (p.hasPermission("gravity.admin") || p.hasPermission("gravity.arena.joinme") || p.isOp()) {
						initialiseIMG();
						Boolean joinme = false;
						
						if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
							joinme = true;
							
							int currentPlayers = Main.ArenaPlayer.get(key).size();
							
							if (Main.getInstance().getGameStateManager().getIsJoinme(key) == null) {
								Main.getInstance().getGameStateManager().setIsJoinme(key, false);
							}
							if (!Main.getInstance().getGameStateManager().getIsJoinme(key)) {
								if ((dm.getData().getInt("Data." + p.getName() + ".joinmes") - 1) >= 0) {
									Main.getInstance().getGameStateManager().setIsJoinme(key, true);
									
									String msg = "";
									for (String line : ImageMessage.getLines()) {
										msg = msg + "\n" + line.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", key).replace("%player%", p.getName()).replace("%players%", currentPlayers+"").replace("%maxplayers%", 
												LobbyState.maxPlayers.get(key)+"").replace("%minplayers%", dm.getData().getInt("Data.arenas." + key + ".minplayers")+"");
									}
									for (Player player : Bukkit.getOnlinePlayers()) {
										PackageSender.sendMessageWithChatHover(player, msg, mm.getMessages().getString("Messages.joinme.hover"), "/gravity join " + key);
									}
									dm.getData().set("Data." + p.getName() + ".joinmes", dm.getData().getInt("Data." + p.getName() + ".joinmes") - 1);
									dm.saveData();
								} else {
									p.sendMessage(mm.getMessages().getString("Messages.joinme.notEnoughJoinmePasses").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
								}
							} else {
								p.sendMessage(mm.getMessages().getString("Messages.joinme.alreadyAJoinme").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							}
							
							e.setCancelled(true);
						}
						if (joinme == false) {
							p.sendMessage(mm.getMessages().getString("Messages.joinme.cantCreateJoinme").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
						}
					} else {
						mm.sendMessage("Messages.noPermission", p);
					}
				} else if (e.getMessage().toLowerCase().startsWith("/stats")) {
					if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState || Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState || 
							Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
						
						if (e.getMessage() == "/stats" || e.getMessage() == "/stats ") {
							StatsManager.sendStats(p.getName(), p);
						} else {
							if (StatsManager.hasStats(Bukkit.getPlayer(e.getMessage().substring(6, e.getMessage().length())))) {
								StatsManager.sendStats(Bukkit.getPlayer(e.getMessage().substring(6, e.getMessage().length())).getName(), Bukkit.getPlayer(e.getMessage().substring(6, e.getMessage().length())));
							} else {
								p.sendMessage(mm.getMessages().getString("Messages.playerNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", e.getMessage().substring(6, e.getMessage().length())));
							}
						}
						e.setCancelled(true);
					}
				}
			}
		}
	}
	  
	private void initialiseIMG() {
		PictureWrapper wrapper = new PictureWrapper(this.plugin, this.player);
		wrapper.sendNOImage();
	}
}
