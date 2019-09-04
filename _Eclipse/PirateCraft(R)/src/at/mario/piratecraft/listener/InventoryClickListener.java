package at.mario.piratecraft.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.inventories.EmtyInventory;
import at.mario.piratecraft.inventories.SetupInventory;
import at.mario.piratecraft.inventories.ShopInventory;
import at.mario.piratecraft.manager.ConfigManagers.DataManager;
import at.mario.piratecraft.manager.ConfigManagers.MessagesManager;

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
			return;
		}
		/*
		if (item == null || !item.hasItemMeta()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					ItemStack item1 = item;
					ItemStack item2 = open.getItem(e.getSlot());
					
					if (item2 != null && item2.hasItemMeta()) {
						for (int i = 0; i < notMoveableItemsPatpiratecraft.size(); i++) {
							if (item2.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages." + notMoveableItemsPatpiratecraft.get(i) + ".name"))) {
								int slot = Main.getInstance().getConfig().getInt("Config." + notMoveableItemsPatpiratecraft.get(i) + ".slot");
								if (notMoveableItemsPatpiratecraft.get(i).equals("ingameitems.visibilityItem.all") || notMoveableItemsPatpiratecraft.get(i).equals("ingameitems.visibilityItem.invisible")) {
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
				for (int i = 0; i < notMoveableItemsPatpiratecraft.size(); i++) {
					if (p.getInventory().getItem(e.getSlot()).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages." + notMoveableItemsPatpiratecraft.get(i) + ".name"))) {
						e.setCancelled(true);
						
						break;
					}
				}
			}
		}
		*/
			
		if (open.getName() == mm.getMessages().getString("Messages.gui.arenasetup.title")) {
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
			
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.setup.setLobbySpawn"))) {
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.x", p.getLocation().getX());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.y", p.getLocation().getY());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setLobbySpawn").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.setup.setSpawn"))) {
				dm.getData().set("Data.arenas." + arenaName + ".spawn.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.arenas." + arenaName + ".spawn.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.arenas." + arenaName + ".spawn.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.arenas." + arenaName + ".spawn.x", p.getLocation().getX());
				dm.getData().set("Data.arenas." + arenaName + ".spawn.y", p.getLocation().getY());
				dm.getData().set("Data.arenas." + arenaName + ".spawn.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setSpawn").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.setup.setBound1"))) {
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.x", p.getLocation().getX());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.y", p.getLocation().getY());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setBound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName).replace("%bound%", "1"));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.setup.setBound2"))) {
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.x", p.getLocation().getX());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.y", p.getLocation().getY());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setBound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName).replace("%bound%", "2"));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.setup.minPlayerPlus"))) {
				if (dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") + 1 <= dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers")) {
					dm.getData().set("Data.arenas." + arenaName + ".minplayers", dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") + 1);
					dm.saveData();
					
					SetupInventory.getInstance().newInventory(arenaName, p);
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.setup.minPlayerNeg"))) {
				if (dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") - 1 >= 2) {
					dm.getData().set("Data.arenas." + arenaName + ".minplayers", dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") - 1);
					dm.saveData();
					
					SetupInventory.getInstance().newInventory(arenaName, p);
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.setup.maxPlayerPlus"))) {
				dm.getData().set("Data.arenas." + arenaName + ".maxplayers", dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers") + 1);
				dm.saveData();
				
				SetupInventory.getInstance().newInventory(arenaName, p);
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.setup.maxPlayerNeg"))) {
				if (dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers") - 1 >= dm.getData().getInt("Data.arenas." + arenaName + ".minplayers")) {
					dm.getData().set("Data.arenas." + arenaName + ".maxplayers", dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers") - 1);
					dm.saveData();
					
					SetupInventory.getInstance().newInventory(arenaName, p);
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
			}
		}
	}
}
