package at.mario.hidenseek.listener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.IngameState;
import at.mario.hidenseek.inventories.EmtyInventory;
import at.mario.hidenseek.inventories.PassShopInventory;
import at.mario.hidenseek.inventories.PowerUpShopInventory;
import at.mario.hidenseek.inventories.SetupInventory;
import at.mario.hidenseek.inventories.ShopInventory;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;

public class InventoryClickListener implements Listener {
	
	@SuppressWarnings({ "unused", "unchecked" })
	@EventHandler
	public void InvenClick(InventoryClickEvent e) {
		MessagesManager mm = new MessagesManager();
		
		DataManager dm = new DataManager();
		Player p = (Player) e.getWhoClicked();
		
		//ClickType click = e.getClick();
		Inventory open = e.getInventory();
		ItemStack item = e.getCurrentItem();
		ItemStack itemClick = e.getCursor();
		
		//p.sendMessage("SLOT: " + e.getSlot());
		
		if (item == null || !item.hasItemMeta()) {
			return;
		}
		
		if (open.getName() == "container.crafting") {
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.lobbyitems.leave.name")) || 
					item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.lobbyitems.shop.name")) || 
					item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.lobbyitems.pass.name")) || 
					item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.lobbyitems.start.name")) || 
					item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.ingameitems.seekerStick.name")) || 
					item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.ingameitems.seekerHelmet.name")) || 
					item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.ingameitems.seekerChestplate.name")) || 
					item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.ingameitems.seekerLeggings.name")) || 
					item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.ingameitems.seekerBoots.name"))) {
				
				e.setCancelled(true);
			}
		}

		if (open == null) {
			return;
		}
		
		if (open.getName() == mm.getMessages().getString("Messages.inventory.usePass.title")) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.usePass.useSeekerPass.name"))) {
				if (!IngameState.seekerPassPlayer.containsKey(p)) {
					if (Main.getInstance().getConfig().getBoolean("Config.unlimitedSeekerPassesWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infiniteSeekerPasses") || p.isOp()) ) {
						IngameState.seekerPassPlayer.put(p, "pass1");
						p.closeInventory();
						//p.sendMessage(mm.getMessages().getString("Messages.seekerPasses.youHaveUnlimited").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
						p.sendMessage(mm.getMessages().getString("Messages.seekerPasses.usedPass1").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					} else {
						if (dm.getData().getInt("Data." + p.getName() + ".seekerPasses") - 1 >= 0) {
							IngameState.seekerPassPlayer.put(p, "pass1");
							dm.getData().set("Data." + p.getName() + ".seekerPasses", dm.getData().getInt("Data." + p.getName() + ".seekerPasses") - 1);
							dm.saveData();
							
							p.sendMessage(mm.getMessages().getString("Messages.seekerPasses.usedPass1").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
							
							p.closeInventory();
						} else {
							p.sendMessage(mm.getMessages().getString("Messages.seekerPasses.notEnoughPasses").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
						}
					}
				} else {
					p.sendMessage(mm.getMessages().getString("Messages.seekerPasses.alreadyUsedPass").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				}
			}
		} else if (open.getName() == mm.getMessages().getString("Messages.inventory.passShop.title")) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.passShop.buyJoinmePasses.name"))) {
				if (Main.getInstance().getConfig().getBoolean("Config.unlimitedJoinmePassesWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infiniteJoinmePasses") || p.isOp()) ) {
					p.sendMessage(mm.getMessages().getString("Messages.shop.youHaveUnlimited").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
							replace("%amount%", 1+"").replace("%item%", "joinme-pass"));
					p.closeInventory();
				} else {
					if (Main.eco.getBalance(p) >= Main.getInstance().getConfig().getInt("Config.prices.passes.joinmePass")) {
						Main.eco.withdrawPlayer(p, Main.getInstance().getConfig().getInt("Config.prices.passes.joinmePass"));
						dm.getData().set("Data." + p.getName() + ".joinmes", dm.getData().getInt("Data." + p.getName() + ".joinmes") + 1);
						dm.saveData();
						EmtyInventory.getInstance().newInventory(p);
						PassShopInventory.getInstance().newInventory(p);
						p.sendMessage(mm.getMessages().getString("Messages.shop.boughtShopItem").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
								replace("%amount%", 1+"").replace("%item%", "joinme-pass"));
					} else {
						mm.sendMessage("Messages.economy.notEnoughMoney", p);
					}
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.passShop.buySeekerPasses.name"))) {
				if (Main.getInstance().getConfig().getBoolean("Config.unlimitedSeekerPassesWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infiniteSeekerPasses") || p.isOp()) ) {
					p.sendMessage(mm.getMessages().getString("Messages.shop.youHaveUnlimited").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
							replace("%amount%", 1+"").replace("%item%", "seeker-pass"));
					p.closeInventory();
				} else {
					if (Main.eco.getBalance(p) >= Main.getInstance().getConfig().getInt("Config.prices.passes.seekerPass")) {
						Main.eco.withdrawPlayer(p, Main.getInstance().getConfig().getInt("Config.prices.passes.seekerPass"));
						dm.getData().set("Data." + p.getName() + ".seekerPasses", dm.getData().getInt("Data." + p.getName() + ".seekerPasses") + 1);
						dm.saveData();
						EmtyInventory.getInstance().newInventory(p);
						PassShopInventory.getInstance().newInventory(p);
						p.sendMessage(mm.getMessages().getString("Messages.shop.boughtShopItem").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
								replace("%amount%", 1+"").replace("%item%", "seeker-pass"));
					} else {
						mm.sendMessage("Messages.economy.notEnoughMoney", p);
					}
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.passShop.back.name"))) {
				ShopInventory.getInstance().newInventory(p);
			}
		} else if (open.getName() == mm.getMessages().getString("Messages.inventory.powerupShop.title")) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.powerupShop.buyjumpBoost.name"))) {
				if (Main.getInstance().getConfig().getBoolean("Config.unlimitedPowerupsWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infinitePowerups") || p.isOp()) ) {
					p.sendMessage(mm.getMessages().getString("Messages.shop.youHaveUnlimited").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
							replace("%amount%", 1+"").replace("%item%", "jumpboost-powerup"));
					p.closeInventory();
				} else {
					if (Main.eco.getBalance(p) >= Main.getInstance().getConfig().getInt("Config.prices.powerups.jumpboost")) {
						Main.eco.withdrawPlayer(p, Main.getInstance().getConfig().getInt("Config.prices.powerups.jumpboost"));
						dm.getData().set("Data." + p.getName() + ".powerups.jumpboost", dm.getData().getInt("Data." + p.getName() + ".powerups.jumpboost") + 1);
						dm.saveData();
						EmtyInventory.getInstance().newInventory(p);
						PassShopInventory.getInstance().newInventory(p);
						p.sendMessage(mm.getMessages().getString("Messages.shop.boughtShopItem").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
								replace("%amount%", 1+"").replace("%item%", "jumpboost-powerup"));
					} else {
						mm.sendMessage("Messages.economy.notEnoughMoney", p);
					}
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.powerupShop.buyKnockback.name"))) {
				if (Main.getInstance().getConfig().getBoolean("Config.unlimitedPowerupsWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infinitePowerups") || p.isOp()) ) {
					p.sendMessage(mm.getMessages().getString("Messages.shop.youHaveUnlimited").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
							replace("%amount%", 1+"").replace("%item%", "knockback-powerup"));
					p.closeInventory();
				} else {
					if (Main.eco.getBalance(p) >= Main.getInstance().getConfig().getInt("Config.prices.powerups.knockback")) {
						Main.eco.withdrawPlayer(p, Main.getInstance().getConfig().getInt("Config.prices.powerups.knockback"));
						dm.getData().set("Data." + p.getName() + ".powerups.knockback", dm.getData().getInt("Data." + p.getName() + ".powerups.knockback") + 1);
						dm.saveData();
						EmtyInventory.getInstance().newInventory(p);
						PassShopInventory.getInstance().newInventory(p);
						p.sendMessage(mm.getMessages().getString("Messages.shop.boughtShopItem").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
								replace("%amount%", 1+"").replace("%item%", "knockback-powerup"));
					} else {
						mm.sendMessage("Messages.economy.notEnoughMoney", p);
					}
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.powerupShop.buyInvisibility.name"))) {
				if (Main.getInstance().getConfig().getBoolean("Config.unlimitedPowerupsWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infinitePowerups") || p.isOp()) ) {
					p.sendMessage(mm.getMessages().getString("Messages.shop.youHaveUnlimited").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
							replace("%amount%", 1+"").replace("%item%", "invisibility-powerup"));
					p.closeInventory();
				} else {
					if (Main.eco.getBalance(p) >= Main.getInstance().getConfig().getInt("Config.prices.powerups.invisibility")) {
						Main.eco.withdrawPlayer(p, Main.getInstance().getConfig().getInt("Config.prices.powerups.invisibility"));
						dm.getData().set("Data." + p.getName() + ".powerups.invisibility", dm.getData().getInt("Data." + p.getName() + ".powerups.invisibility") + 1);
						dm.saveData();
						EmtyInventory.getInstance().newInventory(p);
						PassShopInventory.getInstance().newInventory(p);
						p.sendMessage(mm.getMessages().getString("Messages.shop.boughtShopItem").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
								replace("%amount%", 1+"").replace("%item%", "invisibility-powerup"));
					} else {
						mm.sendMessage("Messages.economy.notEnoughMoney", p);
					}
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.powerupShop.buySpeed.name"))) {
				if (Main.getInstance().getConfig().getBoolean("Config.unlimitedPowerupsWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infinitePowerups") || p.isOp()) ) {
					p.sendMessage(mm.getMessages().getString("Messages.shop.youHaveUnlimited").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
							replace("%item%", "speed-powerup"));
					p.closeInventory();
				} else {
					if (Main.eco.getBalance(p) >= Main.getInstance().getConfig().getInt("Config.prices.powerups.speed")) {
						Main.eco.withdrawPlayer(p, Main.getInstance().getConfig().getInt("Config.prices.powerups.speed"));
						dm.getData().set("Data." + p.getName() + ".powerups.speed", dm.getData().getInt("Data." + p.getName() + ".powerups.speed") + 1);
						dm.saveData();
						EmtyInventory.getInstance().newInventory(p);
						PassShopInventory.getInstance().newInventory(p);
						p.sendMessage(mm.getMessages().getString("Messages.shop.boughtShopItem").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
								replace("%amount%", 1+"").replace("%item%", "speed-powerup"));
					} else {
						mm.sendMessage("Messages.economy.notEnoughMoney", p);
					}
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.powerupShop.back.name"))) {
				ShopInventory.getInstance().newInventory(p);
			}
		} else if (open.getName() == mm.getMessages().getString("Messages.inventory.shop.title")) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.shop.buyPassesMenue.name"))) {
				PassShopInventory.getInstance().newInventory(p);
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.shop.buyPowerupsMenue.name"))) {
				PowerUpShopInventory.getInstance().newInventory(p);
			}
		} else if (open.getName() == mm.getMessages().getString("Messages.inventory.setup.title")) {
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
			
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.setup.setLobbySpawn"))) {
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.x", p.getLocation().getX());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.y", p.getLocation().getY());
				dm.getData().set("Data.arenas." + arenaName + ".lobbyspawn.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setLobbySpawn").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.setup.setSpawn"))) {
				dm.getData().set("Data.arenas." + arenaName + ".spawn.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.arenas." + arenaName + ".spawn.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.arenas." + arenaName + ".spawn.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.arenas." + arenaName + ".spawn.x", p.getLocation().getX());
				dm.getData().set("Data.arenas." + arenaName + ".spawn.y", p.getLocation().getY());
				dm.getData().set("Data.arenas." + arenaName + ".spawn.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setSpawn").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.setup.setBound1"))) {
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.x", p.getLocation().getX());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.y", p.getLocation().getY());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc1.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setBound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName).replace("%bound%", "1"));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.setup.setBound2"))) {
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.pitch", p.getLocation().getPitch());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.yaw", p.getLocation().getYaw());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.x", p.getLocation().getX());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.y", p.getLocation().getY());
				dm.getData().set("Data.arenas." + arenaName + ".bounds.loc2.z", p.getLocation().getZ());
				dm.saveData();
				
				p.sendMessage(mm.getMessages().getString("Messages.command.arena.setBound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", arenaName).replace("%bound%", "2"));
				p.closeInventory();
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.setup.minPlayerPlus"))) {
				if (dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") + 1 <= dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers")) {
					dm.getData().set("Data.arenas." + arenaName + ".minplayers", dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") + 1);
					dm.saveData();
					
					SetupInventory.getInstance().newInventory(arenaName, p);
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.setup.minPlayerNeg"))) {
				if (dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") - 1 >= 2) {
					dm.getData().set("Data.arenas." + arenaName + ".minplayers", dm.getData().getInt("Data.arenas." + arenaName + ".minplayers") - 1);
					dm.saveData();
					
					SetupInventory.getInstance().newInventory(arenaName, p);
				}
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.setup.maxPlayerPlus"))) {
				dm.getData().set("Data.arenas." + arenaName + ".maxplayers", dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers") + 1);
				dm.saveData();
				
				SetupInventory.getInstance().newInventory(arenaName, p);
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.inventory.setup.maxPlayerNeg"))) {
				if (dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers") - 1 >= dm.getData().getInt("Data.arenas." + arenaName + ".minplayers")) {
					dm.getData().set("Data.arenas." + arenaName + ".maxplayers", dm.getData().getInt("Data.arenas." + arenaName + ".maxplayers") - 1);
					dm.saveData();
					
					SetupInventory.getInstance().newInventory(arenaName, p);
				}
			}
		}
	}
}
