package at.mario.hidenseek.listener;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.IngameState;
import at.mario.hidenseek.inventories.EmtyInventory;
import at.mario.hidenseek.inventories.SetupInventory;
import at.mario.hidenseek.inventories.ShopInventory;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;

public class InventoryClickListener implements Listener {
	
	@SuppressWarnings("unused")
	@EventHandler
	public void InvenClick(InventoryClickEvent e) {
		MessagesManager mm = new MessagesManager();
		
		DataManager dm = new DataManager();
		Player p = (Player) e.getWhoClicked();
		
		//ClickType click = e.getClick();
		Inventory open = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		ItemStack itemClick = e.getCursor();
		
		
		if (open == null) {
			return;
		}
		
		if (item == null || !item.hasItemMeta()) {
			return;
		}
		
		
		if (open == p.getInventory()) {
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.lobbyitems.leaveItem.name")) || item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.lobbyitems.shopItem.name")) || 
					item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.lobbyitems.passItem.name")) || 
					item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.lobbyitems.startItem.name"))) {
				e.setCancelled(true);
			}
		}
		if (open.getName() == mm.getMessages().getString("Messages.gui.pass.title")) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.pass.usePass.name"))) {
				if (!IngameState.seekerPassPlayer.containsKey(p)) {
					if (dm.getData().getInt("Data." + p.getName() + ".seekerPasses") - 1 >= 0) {
						IngameState.seekerPassPlayer.put(p, "pass1");
						dm.getData().set("Data." + p.getName() + ".seekerPasses", dm.getData().getInt("Data." + p.getName() + ".seekerPasses") - 1);
						dm.saveData();
						p.closeInventory();
						p.sendMessage("Used Pass1");
					} else {
						p.sendMessage("Not enough passes!");
					}
				} else {
					p.sendMessage("Already used Pass1");
				}
			}
		}
		if (open.getName() == mm.getMessages().getString("Messages.gui.setup.title")) {
			e.setCancelled(true);
			
			String arenaName = open.getItem(4).getItemMeta().getLore().get(0);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (!dm.getData().contains("Data.arenas." + arenaName + ".signs")) {
				ArrayList<Location> signlist = new ArrayList<Location>();
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
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn", p.getLocation());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setLobbySpawn").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.setup.setSpawn"))) {
				dm.getData().set("Data.arenas." + arenaName + ".spawn", p.getLocation());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setSpawn").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.setup.setBound1"))) {
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1", p.getLocation());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setBound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName).replace("%bound%", "1"));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.setup.setBound2"))) {
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2", p.getLocation());
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
		}
		if (open.getName() == mm.getMessages().getString("Messages.gui.shop.title")) {
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
	}
}
