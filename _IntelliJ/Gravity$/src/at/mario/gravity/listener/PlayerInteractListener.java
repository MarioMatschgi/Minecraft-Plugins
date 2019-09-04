package at.mario.gravity.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import at.mario.gravity.Main;
import at.mario.gravity.gamestates.IngameState;
import at.mario.gravity.gamestates.LobbyState;
import at.mario.gravity.inventories.PassInventory;
import at.mario.gravity.inventories.ShopInventory;
import at.mario.gravity.inventories.TeleporterInventory;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;
import at.mario.gravity.utils.ItemsUtil;
import at.mario.gravity.utils.PlayerUtil;

public class PlayerInteractListener implements Listener {

	public static List<Player> hiddenPlayers = new ArrayList<Player>();
	public static HashMap<Player, Integer> taskIDs = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> visibilityTaskIDs = new HashMap<Player, Integer>();
	public static HashMap<Player, Integer> playerCanUseVisibility = new HashMap<Player, Integer>();
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		Block block = e.getClickedBlock();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			if (item != null && item.hasItemMeta()) {
				if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.profiles")) {
					ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
					if (configSection != null) {
						for (String key : configSection.getKeys(false)) {
							if (Main.ArenaPlayer.containsKey(key)) {
								if (Main.ArenaPlayer.get(key).contains(p)) {
									PlayerUtil.restartStage(p, key);
								}
							}
						}
					}
				}
			}
			
			// SIGN check
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
					ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
					if (configSection != null) {
						for (String key : configSection.getKeys(false)) {
							if (dm.getData().contains("Data.arenas." + key + ".signs")) {
								ArrayList<LinkedHashMap<String, Object>> signlist = (ArrayList<LinkedHashMap<String, Object>>) dm.getData().get("Data.arenas." + key + ".signs");
								for (int i = 0; i < signlist.size(); i++) {
									LinkedHashMap<String, Object> section = signlist.get(i);
									
									if (section.get("world").equals(block.getWorld().getName()) && (double) section.get("x") == (double) block.getX() && (double) section.get("y") == (double) block.getY() && (double) section.get("z") == 
											(double) block.getZ() ) {
										PlayerUtil.joinGame(key, p);
										return;
									}
								}
							}
						}
					}
				}
			}
			if (item != null && item.hasItemMeta() && item.getType() != Material.AIR) {
				for (String key : Main.ArenaPlayer.keySet()) {
					List<Player> players = Main.ArenaPlayer.get(key);
					
					if (players.contains(p)) {
						// leave check
						if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.leaveItem.name"))) {
							PlayerUtil.leaveGame(true, key, p);
							e.setCancelled(true);
							return;
						}
						// leave check (FinisherItem)
						if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.finishedItems.leaveItem.name"))) {
							PlayerUtil.leaveGame(true, key, p);
							e.setCancelled(true);
							return;
						}
						
						// SHOP check
						else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.shopItem.name"))) {
							ShopInventory.getInstance().newInventory(p);
							e.setCancelled(true);
							return;
						}
						// PASS check
						else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.passItem.name"))) {
							PassInventory.getInstance().newInventory(p);
							e.setCancelled(true);
							return;
						}
						// START check
						else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.startItem.name"))) {
							if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
								LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(key);
								
								if (lobbyState.getLobbycountdown().isRunning(key)) {
									if (!(lobbyState.getLobbycountdown().getSeconds(key) <= 3) ) {
										lobbyState.getLobbycountdown().setSeconds(3, key);
										e.setCancelled(true);
									}
								}
							}
							e.setCancelled(true);
							return;
						}
						// RESTART check
						else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.ingameitems.resetItem.name")) {
							if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState) {
								PlayerUtil.restartStage(p, key);
								
								e.setCancelled(true);
								return;
							}
						}
						
						// VISIBILITY check
						else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.ingameitems.visibilityItem.all.name")) {
							if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState) {
								if (playerCanUseVisibility.get(p) == 0) {
									e.setCancelled(true);
									
									for (Player player : Bukkit.getServer().getOnlinePlayers()) {
										if (!IngameState.ArenaFinishedPlayers.containsKey(key) || IngameState.ArenaFinishedPlayers.get(key) == null || !IngameState.ArenaFinishedPlayers.get(key).containsKey(player)) {
											p.hidePlayer(player);
											hiddenPlayers.add(player);
										}
									}
									
									dm.getData().set("Data." + p.getName() + ".visibility", "invisible");
									dm.saveData();
									ItemsUtil.giveIngameItems(false, p);
									// p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.ingameitems.visibilityItem.slot"), ItemsManager.getVisibilityItem(p));
								} else {
									p.sendMessage(mm.getMessages().getString("Messages.clickedVisibilityWhileCooldown").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seconds%", 
											playerCanUseVisibility.get(p)+""));
									return;
								}

								if (Main.getInstance().getConfig().getInt("Config.changeVisibilityCooldown") > 0) {
									playerCanUseVisibility.put(p, Main.getInstance().getConfig().getInt("Config.changeVisibilityCooldown"));
									
									visibilityTaskIDs.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
										
										@Override
										public void run() {
											playerCanUseVisibility.put(p, playerCanUseVisibility.get(p) - 1);
											
											if (playerCanUseVisibility.get(p) <= 0) {
												Bukkit.getScheduler().cancelTask(visibilityTaskIDs.get(p));
											}
										}
									}, 0, 1 * 20));
								}
								
								return;
							}
						}
						// INVISIBILITY check
						else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.ingameitems.visibilityItem.invisible.name")) {
							if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState) {
								if (playerCanUseVisibility.get(p) == 0) {
									e.setCancelled(true);
									
									for (Player player : Bukkit.getServer().getOnlinePlayers()) {
										if (!IngameState.ArenaFinishedPlayers.containsKey(key) || IngameState.ArenaFinishedPlayers.get(key) == null || !IngameState.ArenaFinishedPlayers.get(key).containsKey(player)) {
											p.showPlayer(player);
									    	hiddenPlayers.remove(player);
										}
									}
									

									dm.getData().set("Data." + p.getName() + ".visibility", "all");
									dm.saveData();
									ItemsUtil.giveIngameItems(false, p);
									// p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.ingameitems.visibilityItem.slot"), ItemsManager.getVisibilityItem(p));
								} else {
									p.sendMessage(mm.getMessages().getString("Messages.clickedVisibilityWhileCooldown").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seconds%", 
											playerCanUseVisibility.get(p)+""));
									return;
								}

								if (Main.getInstance().getConfig().getInt("Config.changeVisibilityCooldown") > 0) {
									playerCanUseVisibility.put(p, Main.getInstance().getConfig().getInt("Config.changeVisibilityCooldown"));
									
									visibilityTaskIDs.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
										
										@Override
										public void run() {
											playerCanUseVisibility.put(p, playerCanUseVisibility.get(p) - 1);
											
											if (playerCanUseVisibility.get(p) <= 0) {
												Bukkit.getScheduler().cancelTask(visibilityTaskIDs.get(p));
											}
										}
									}, 0, 1 * 20));
								}
								
								return;
							}
						}
						// SKIP check
						else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.ingameitems.skipItem.name")) {
							if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState) {
								PlayerUtil.nextStage(key, p);
								
								p.getInventory().setItemInHand(new ItemStack(Material.AIR));
								e.setCancelled(true);
								return;
							}
						}
						// TELEPORTER check
						else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.finishedItems.teleporterItem.name")) {
							if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState) {
								TeleporterInventory.getInstance().newInventory(key, p);
								
								e.setCancelled(true);
								return;
							}
						}
						
						
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e) {
		
	}
}
