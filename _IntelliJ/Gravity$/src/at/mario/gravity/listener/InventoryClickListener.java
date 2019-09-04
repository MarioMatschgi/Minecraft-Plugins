package at.mario.gravity.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import at.mario.gravity.Main;
import at.mario.gravity.gamestates.IngameState;
import at.mario.gravity.inventories.ArenaSetupInventory;
import at.mario.gravity.inventories.EmtyInventory;
import at.mario.gravity.inventories.MapSetupInventory;
import at.mario.gravity.inventories.ShopInventory;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;
import at.mario.gravity.utils.PlayerUtil;

public class InventoryClickListener implements Listener {
	
	String[] notMoveableItemsPathsArray = {"lobbyitems.leaveItem", "lobbyitems.shopItem", "lobbyitems.passItem", "lobbyitems.startItem", "ingameitems.resetItem", "ingameitems.visibilityItem.all", "ingameitems.visibilityItem.invisible", 
											"ingameitems.skipItem", "finishedItems.leaveItem", "finishedItems.teleporterItem"};
	List<String> notMoveableItemsPaths = new ArrayList<>(Arrays.asList(notMoveableItemsPathsArray));
	
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void InvenClick(InventoryClickEvent e) {
		MessagesManager mm = new MessagesManager();
		
		DataManager dm = new DataManager();
		Player p = (Player) e.getWhoClicked();
		
		//ClickType clickType = e.getClick();
		Inventory open = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		//ItemStack itemClicked = e.getCursor();
		
		
		if (open == null) {
			return;
		}
		
		if (item == null || !item.hasItemMeta()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					ItemStack item1 = item;
					ItemStack item2 = open.getItem(e.getSlot());
					
					if (item2 != null && item2.hasItemMeta()) {
						for (int i = 0; i < notMoveableItemsPaths.size(); i++) {
							if (item2.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages." + notMoveableItemsPaths.get(i) + ".name"))) {
								int slot = Main.getInstance().getConfig().getInt("Config." + notMoveableItemsPaths.get(i) + ".slot");
								if (notMoveableItemsPaths.get(i).equals("ingameitems.visibilityItem.all") || notMoveableItemsPaths.get(i).equals("ingameitems.visibilityItem.invisible")) {
									slot = Main.getInstance().getConfig().getInt("Config.ingameitems.visibilityItem.slot");
								}
								p.getInventory().setItem(slot, item2);
								
								open.setItem(e.getSlot(), item1);
								
								break;
							}
						}
					}
				}
			}, 1);
			
			return;
		}
		
		if (e.getSlot() < 40) {
			if (p.getInventory().getItem(e.getSlot()) != null && p.getInventory().getItem(e.getSlot()).hasItemMeta()) {
				for (int i = 0; i < notMoveableItemsPaths.size(); i++) {
					if (p.getInventory().getItem(e.getSlot()).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages." + notMoveableItemsPaths.get(i) + ".name"))) {
						e.setCancelled(true);
						
						break;
					}
				}
			}
		}
			
		if (open.getName() == mm.getMessages().getString("Messages.gui.mapsetup.title")) {
			e.setCancelled(true);
			
			String mapName = open.getItem(4).getItemMeta().getLore().get(0);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (!dm.getData().contains("Data.maps." + mapName + ".world")) {
				dm.getData().set("Data.maps." + mapName + ".world", p.getLocation().getWorld().getName());
				dm.saveData();
			}
			
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.mapsetup.setSpawn"))) {
				dm.getData().set("Data.maps." + mapName + ".spawn.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.maps." + mapName + ".spawn.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.maps." + mapName + ".spawn.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.maps." + mapName + ".spawn.x", p.getLocation().getX());
				dm.getData().set("Data.maps." + mapName + ".spawn.y", p.getLocation().getY());
				dm.getData().set("Data.maps." + mapName + ".spawn.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.maps.setSpawn").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%map%", mapName));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.mapsetup.setGlasspos1"))) {
				dm.getData().set("Data.maps." + mapName + ".bounds.loc1.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.maps." + mapName + ".bounds.loc1.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.maps." + mapName + ".bounds.loc1.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.maps." + mapName + ".bounds.loc1.x", p.getLocation().getX());
				dm.getData().set("Data.maps." + mapName + ".bounds.loc1.y", p.getLocation().getY() - 1);
				dm.getData().set("Data.maps." + mapName + ".bounds.loc1.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.maps.setGlasspos").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%map%", mapName).replace("%pos%", "1"));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.mapsetup.setGlasspos2"))) {
				dm.getData().set("Data.maps." + mapName + ".bounds.loc2.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.maps." + mapName + ".bounds.loc2.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.maps." + mapName + ".bounds.loc2.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.maps." + mapName + ".bounds.loc2.x", p.getLocation().getX());
				dm.getData().set("Data.maps." + mapName + ".bounds.loc2.y", p.getLocation().getY() - 1);
				dm.getData().set("Data.maps." + mapName + ".bounds.loc2.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.maps.setGlasspos").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%map%", mapName).replace("%pos%", "2"));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.mapsetup.setDifficulty"))) {
				int difficulty = dm.getData().getInt("Data.maps." + mapName + ".difficulty");
				Bukkit.broadcastMessage("Difficulty: " + difficulty);
				if (difficulty == 1) {
					difficulty = 2;
				} else if (difficulty == 2) {
					difficulty = 3;
				} else if (difficulty == 3) {
					difficulty = 1;
				}
				
				dm.getData().set("Data.maps." + mapName + ".difficulty", difficulty);
				dm.saveData();
				EmtyInventory.getInstance().newInventory(p);
				MapSetupInventory.getInstance().newInventory(mapName, p);
			}
		} else if (open.getName() == mm.getMessages().getString("Messages.gui.arenasetup.title")) {
			e.setCancelled(true);
			
			String arenaName = open.getItem(4).getItemMeta().getLore().get(0);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (!dm.getData().contains("Data.arenas." + arenaName + ".signs")) {
				ArrayList<LinkedHashMap<String, Object>> signlist = (ArrayList<LinkedHashMap<String, Object>>) dm.getData().get("Data.arenas." + arenaName + ".signs");
				dm.getData().set("Data.arenas." + arenaName + ".signs", signlist);
				dm.saveData();
			}
			if (!dm.getData().contains("Data.arenas." + arenaName + ".status")) {
				dm.getData().set("Data.arenas." + arenaName + ".status", "stopped");
				dm.saveData();
			}
			if (!dm.getData().contains("Data.arenas." + arenaName + ".world")) {
				dm.getData().set("Data.arenas." + arenaName + ".world", p.getLocation().getWorld().getName());
				dm.saveData();
			}
			
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.arenasetup.setLobbySpawn"))) {
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.x", p.getLocation().getX());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.y", p.getLocation().getY());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setLobbySpawn").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.arenasetup.minPlayerPlus"))) {
				if (dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") + 1 <= dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers")) {
					dm.getData().set("Data.arenas." + arenaName + ".minplayers", dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") + 1);
					dm.saveData();
					
					ArenaSetupInventory.getInstance().newInventory(arenaName, p);
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.arenasetup.minPlayerNeg"))) {
				if (dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") - 1 >= 2) {
					dm.getData().set("Data.arenas." + arenaName + ".minplayers", dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") - 1);
					dm.saveData();
					
					ArenaSetupInventory.getInstance().newInventory(arenaName, p);
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.arenasetup.maxPlayerPlus"))) {
				dm.getData().set("Data.arenas." + arenaName + ".maxplayers", dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers") + 1);
				dm.saveData();
				
				ArenaSetupInventory.getInstance().newInventory(arenaName, p);
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.arenasetup.maxPlayerNeg"))) {
				if (dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers") - 1 >= dm.getData().getInt("Data.arenas." + arenaName + ".minplayers")) {
					dm.getData().set("Data.arenas." + arenaName + ".maxplayers", dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers") - 1);
					dm.saveData();
					
					ArenaSetupInventory.getInstance().newInventory(arenaName, p);
				}
			}
		} else if (open.getName() == mm.getMessages().getString("Messages.gui.shop.title")) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.shop.joinme.name"))) {
				if (Main.eco.getBalance(p) >= Main.getInstance().getConfig().getInt("Config.prices.joinmePass")) {
					Main.eco.withdrawPlayer(p, Main.getInstance().getConfig().getInt("Config.prices.joinmePass"));
					dm.getData().set("Data." + p.getName() + ".joinmes", dm.getData().getInt("Data." + p.getName() + ".joinmes") + 1);
					dm.saveData();
					EmtyInventory.getInstance().newInventory(p);
					ShopInventory.getInstance().newInventory(p);
					p.sendMessage(mm.getMessages().getString("Messages.shop.boughtJoinmePass").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.shop.seeker.name"))) {
				if (Main.getInstance().getConfig().getBoolean("Config.unlimitedSeekerPassesWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infiniteSeekerPasses") || p.isOp()) ) {
					p.sendMessage(mm.getMessages().getString("Messages.seekerPasses.youHaveUnlimited").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				} else {
					if (Main.eco.getBalance(p) >= Main.getInstance().getConfig().getInt("Config.prices.seekerPass")) {
						Main.eco.withdrawPlayer(p, Main.getInstance().getConfig().getInt("Config.prices.seekerPass"));
						dm.getData().set("Data." + p.getName() + ".seekerPasses", dm.getData().getInt("Data." + p.getName() + ".seekerPasses") + 1);
						dm.saveData();
						EmtyInventory.getInstance().newInventory(p);
						ShopInventory.getInstance().newInventory(p);
						p.sendMessage(mm.getMessages().getString("Messages.shop.boughtSeekerPass").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					} else {
						mm.sendMessage("Messages.economy.notEnoughMoney", p);
					}
				}
			}
		} else if (open.getName() == mm.getMessages().getString("Messages.gui.teleporter.title")) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (item.getType().equals(Material.SKULL_ITEM)) {
				Player player = Bukkit.getPlayer(((SkullMeta) item.getItemMeta()).getOwner());
				p.teleport(player.getLocation());

				HashMap<Player, Integer> ArenaFinishedPlayerLevelsMap = IngameState.ArenaFinishedPlayerLevels.get(PlayerUtil.getArenaOfPlayer(p));
				ArenaFinishedPlayerLevelsMap.put(p, IngameState.ArenaPlayerLevels.get(PlayerUtil.getArenaOfPlayer(player)).get(player));
				IngameState.ArenaFinishedPlayerLevels.put(PlayerUtil.getArenaOfPlayer(p), ArenaFinishedPlayerLevelsMap);
				
				p.closeInventory();
			} else if (item.getType().equals(Material.MAP)) {
				Location loc = new Location(Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0, 0.0F, 0.0F);
				loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(e.getSlot()).getName() + ".spawn.world")));
				loc.setPitch((float) ((double) dm.getData().get("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(e.getSlot()).getName() + ".spawn.pitch")));
				loc.setYaw((float) ((double) dm.getData().get("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(e.getSlot()).getName() + ".spawn.yaw")));
				loc.setX(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(e.getSlot()).getName() + ".spawn.x"));
				loc.setY(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(e.getSlot()).getName() + ".spawn.y"));
				loc.setZ(dm.getData().getDouble("Data.maps." + IngameState.ArenaUsedMaps.get(PlayerUtil.getArenaOfPlayer(p)).get(e.getSlot()).getName() + ".spawn.z"));
				
				p.teleport(loc);

				HashMap<Player, Integer> ArenaFinishedPlayerLevelsMap = IngameState.ArenaFinishedPlayerLevels.get(PlayerUtil.getArenaOfPlayer(p));
				ArenaFinishedPlayerLevelsMap.put(p, e.getSlot() + 1);
				IngameState.ArenaFinishedPlayerLevels.put(PlayerUtil.getArenaOfPlayer(p), ArenaFinishedPlayerLevelsMap);
				
				p.closeInventory();
			}
		}
		
	}
}
