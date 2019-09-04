package at.mario.hidenseek.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.EndingState;
import at.mario.hidenseek.gamestates.IngameState;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.manager.PackageSender;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;
import at.mario.hidenseek.manager.ConfigManagers.StatsManager;
import at.mario.hidenseek.pictureLogin.com.bobacadodl.imgmessage.ImageMessage;
import at.mario.hidenseek.pictureLogin.me.itsnathang.picturelogin.util.PictureWrapper;

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
						Main.leaveGame(key, p);
						
						e.setCancelled(true);
					}
				}
				if (e.getMessage().toLowerCase().startsWith("/start")) {
					if (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.start") || p.isOp()) {
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
					if (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.joinme") || p.isOp()) {
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
										PackageSender.sendMessageWithChatHover(player, msg, mm.getMessages().getString("Messages.joinme.hover"), "/hs join " + key);
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
