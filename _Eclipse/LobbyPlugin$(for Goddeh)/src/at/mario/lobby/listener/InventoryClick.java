package at.mario.lobby.listener;

import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lobby.AnvilGUI;
import at.mario.lobby.Main;
import at.mario.lobby.Pets;
import at.mario.lobby.Effects.ParticleEffect;
import at.mario.lobby.inventories.ArmorInventory;
import at.mario.lobby.inventories.EmtyInventory;
import at.mario.lobby.inventories.GadgetsInventory;
import at.mario.lobby.inventories.ParticleInventory;
import at.mario.lobby.inventories.PetInventory;
import at.mario.lobby.inventories.TeleportSortInventory;
import at.mario.lobby.inventories.VisibilityInventory;
import at.mario.lobby.manager.MoneyManager;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class InventoryClick implements Listener{

	public static List<Player> hiddenPlayers = new ArrayList<Player>();
	 
	//private Plugin plugin = Main.getPlugin();
	public static HashMap<String, Entity> pets = new HashMap<String, Entity>();
	
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
			
			if (item.getItemMeta().getDisplayName() == mm.getMessages().get("Messages.inventory.teleporter") || item.getItemMeta().getDisplayName() == mm.getMessages().get("Messages.inventory.visibility") 
					|| item.getItemMeta().getDisplayName() == mm.getMessages().get("Messages.inventory.profiles") || item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.silenthub") 
					
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.boots"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.boots"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.boots"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"))
					|| item.getItemMeta().getDisplayName().equalsIgnoreCase(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.boots")) ) {

				e.setCancelled(true);
			}
		}
		
		if (open.getName() == p.getInventory().getName()) {
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.main.particle"))) {
				e.setCancelled(true);
				
				EmtyInventory.getInstance().newInventory(p);
				ParticleInventory.getInstance().newInventory(p);
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.main.pets"))) {
				e.setCancelled(true);

				EmtyInventory.getInstance().newInventory(p);
				PetInventory.getInstance().newInventory(p);
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.main.armor"))) {
				e.setCancelled(true);

				EmtyInventory.getInstance().newInventory(p);
				ArmorInventory.getInstance().newInventory(p);
			} else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.main.gadget"))) {
				e.setCancelled(true);

				EmtyInventory.getInstance().newInventory(p);
				GadgetsInventory.getInstance().newInventory(p);
			} else if (item.getItemMeta().getDisplayName().equals(" ")) {
				e.setCancelled(true);
			}
		}

		if (open.getName() == mm.getMessages().getString("Messages.gui.DailyReward.title")) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			
			String date = format.format(now);
			int day = Integer.parseInt(date.substring(0, 2));
			int month = Integer.parseInt(date.substring(3, 5));
			int year = Integer.parseInt(date.substring(6, 10));
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.DailyReward.collectReward").replace("%type%", "VIP"))) {
				dm.getData().set("Data." + p.getName().toLowerCase() + ".lastVIPDailyreward.day", day);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".lastVIPDailyreward.month", month);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".lastVIPDailyreward.year", year);
				dm.saveData();
				Main.eco.depositPlayer(p, Main.getInstance().getConfig().getInt("Config.dailyRewards.VIPReward"));
				p.sendMessage(mm.getMessages().getString("Messages.dailyReward.getDailyReward").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%earned%", 
						Main.getInstance().getConfig().getInt("Config.dailyRewards.VIPReward")+"").replace("%money%", NumberFormat.getInstance().format(Main.eco.getBalance(p))+""));
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.DailyReward.collectReward").replace("%type%", "MVIP/VIPPlus"))) {
				dm.getData().set("Data." + p.getName().toLowerCase() + ".lastVIPPlusDailyreward.day", day);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".lastVIPPlusDailyreward.month", month);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".lastVIPPlusDailyreward.year", year);
				dm.saveData();
				Main.eco.depositPlayer(p, Main.getInstance().getConfig().getInt("Config.dailyRewards.MVIPReward"));
				p.sendMessage(mm.getMessages().getString("Messages.dailyReward.getDailyReward").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%earned%", 
						Main.getInstance().getConfig().getInt("Config.dailyRewards.MVIPReward")+"").replace("%money%", NumberFormat.getInstance().format(Main.eco.getBalance(p))+""));
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.DailyReward.collectReward").replace("%type%", ""))) {
				dm.getData().set("Data." + p.getName().toLowerCase() + ".lastDailyreward.day", day);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".lastDailyreward.month", month);
				dm.getData().set("Data." + p.getName().toLowerCase() + ".lastDailyreward.year", year);
				dm.saveData();
				Main.eco.depositPlayer(p, Main.getInstance().getConfig().getInt("Config.dailyRewards.freeReward"));
				p.sendMessage(mm.getMessages().getString("Messages.dailyReward.getDailyReward").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%earned%", 
						Main.getInstance().getConfig().getInt("Config.dailyRewards.freeReward")+"").replace("%money%", NumberFormat.getInstance().format(Main.eco.getBalance(p))+""));
				p.closeInventory();
			}
		}

		if (open.getName() == mm.getMessages().getString("Messages.gui.gadgets.title")) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.gadgets.doubleJump")) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".doubleJump").equalsIgnoreCase("enabled")) {
					dm.getData().set("Data." + p.getName().toLowerCase() + ".doubleJump", "disabled");
					dm.saveData();
				} else {
					dm.getData().set("Data." + p.getName().toLowerCase() + ".doubleJump", "enabled");
					dm.saveData();
				}
				p.closeInventory();
			}
		}
		
		if (open.getName() == mm.getMessages().getString("Messages.gui.visibility.title")) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.visibility.all")) {
				p.closeInventory();
				
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				     p.showPlayer(player);
				     hiddenPlayers.remove(player);
				}
				mm.sendMessage("Messages.visibility.all", p);
			}
			
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.visibility.VIP")) {
				p.closeInventory();
				
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				     p.showPlayer(player);
				     hiddenPlayers.remove(player);
				}
				
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (!player.hasPermission("lobby.vip")) {
						p.hidePlayer(player);
						hiddenPlayers.add(player);
					}
				}
				mm.sendMessage("Messages.visibility.vip", p);
			}
			
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.visibility.nobody")) {
				p.closeInventory();
				
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					p.hidePlayer(player);
					hiddenPlayers.add(player);
				}
				mm.sendMessage("Messages.visibility.nobody", p);
			}
		}

		if (open.getName() == mm.getMessages().getString("Messages.gui.teleport.title")) {
			e.setCancelled(true);
			if (item == null || !item.hasItemMeta() || item.getType() == Material.AIR) {
				return;
			}

			for (int i = 0; i <= (dm.getData().getInt("Data.teleporter.index") - 1); i++) {
				if (dm.getData().get("Data.teleporter." + i + ".location") != null && dm.getData().get("Data.teleporter." + i + ".item") != null) {
					if (dm.getData().get("Data.teleporter." + i + ".item").equals(item)) {
						Location loc = (Location) dm.getData().get("Data.teleporter." + i + ".location");
						
						p.teleport(loc);
					}
				}
			}
		}

		if (open.getName() == mm.getMessages().getString("Messages.gui.teleportSort.title")) {
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.teleportSort.cancel")) {
				e.setCancelled(true);
				p.closeInventory();
				return;
			}
			
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.teleportSort.save")) {
				Main.getInstance().getConfig().set("Config.teleportInventorySize", dm.getData().getInt("Data.temp.slots"));
				
				for (int i = 0; i < ( (Main.getInstance().getConfig().getInt("Config.teleportInventorySize") - 1) ); i++) {
					if (open.getItem(i) != null && open.getItem(i).getItemMeta() != null && open.getItem(i).getType() != Material.AIR) {
						ItemStack item2 = open.getItem(i);
						
						if (item2.getType() != Material.STAINED_GLASS_PANE) {
							for (int i2 = 0; i2 <= (dm.getData().getInt("Data.teleporter.index") - 1); i2++) {
								if (dm.getData().get("Data.teleporter." + i2 + ".item") != null) {
									if (dm.getData().get("Data.teleporter." + i2 + ".item").equals(item2)) {
										
										dm.getData().set("Data.teleporter." + i2 + ".slot", i);
										dm.saveData();
										// p.sendMessage("I: " + i + " " + "I2: " + i2);
									}
								}
							}
						}
					}
				}
				e.setCancelled(true);
				p.closeInventory();
				
				return;
			}
			
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.teleportSort.slotNeg")) {
				e.setCancelled(true);
				if (dm.getData().getInt("Data.temp.slots") > 0 && dm.getData().getInt("Data.temp.slots") <= 54) {
					dm.getData().set("Data.temp.slots", dm.getData().getInt("Data.temp.slots") - 9);
				}
				TeleportSortInventory.getInstance().newInventory(true, p);
				
				return;
			}
			

			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.teleportSort.slotInfo")) {
				e.setCancelled(true);
			}
			if (item.getItemMeta().getDisplayName().equals(" ")) {
				e.setCancelled(true);
			}
			
			
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.teleportSort.slotPos")) {
				e.setCancelled(true);
				if (dm.getData().getInt("Data.temp.slots") >= 0 && dm.getData().getInt("Data.temp.slots") < 54) {
					dm.getData().set("Data.temp.slots", dm.getData().getInt("Data.temp.slots") + 9);
				}
				TeleportSortInventory.getInstance().newInventory(true, p);
				
				return;
			}
		}
		
		if (open.getName() == mm.getMessages().getString("Messages.gui.teleportEdit.title")) {
			e.setCancelled(true);
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.teleportEdit.select")) {
				if (itemClick == null || itemClick == null || itemClick.getType() == Material.AIR) {
					p.sendMessage("Nope");
				} else {
					if (dm.getData().get("Data.teleporter.index") == null) {
						dm.getData().set("Data.teleporter.index", 1);
					}
					dm.getData().set("Data.teleporter." + dm.getData().getInt("Data.teleporter.index") + ".name", dm.getData().get("temp.name"));
					
					if (itemClick.getItemMeta().getDisplayName() == null || itemClick.getItemMeta().getDisplayName() == "" || itemClick.getItemMeta().getDisplayName() == " ") {
						ItemMeta itemClickMeta = itemClick.getItemMeta();
						itemClickMeta.setDisplayName(dm.getData().getString("Data.teleporter." + dm.getData().getInt("Data.teleporter.index") + ".name") );
						itemClick.setItemMeta(itemClickMeta);
					}
					dm.getData().set("Data.teleporter." + dm.getData().getInt("Data.teleporter.index") + ".name", itemClick.getItemMeta().getDisplayName());
					dm.getData().set("Data.teleporter." + dm.getData().getInt("Data.teleporter.index") + ".item", itemClick);
					dm.getData().set("Data.teleporter." + dm.getData().getInt("Data.teleporter.index") + ".location", p.getLocation());
					
					dm.getData().set("Data.teleporter.index", dm.getData().getInt("Data.teleporter.index") + 1);
					
					dm.getData().set("temp.name", " ");
					
					dm.saveData();
					
					p.closeInventory();
					return;
				}
				return;
			}
			
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.teleportEdit.cancel")) {
				p.closeInventory();
				return;
			}
		}
		
		if (open.getName() == mm.getMessages().getString("Messages.gui.main.title")) {
			e.setCancelled(true);
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.main.pets")) {
				p.closeInventory();
				
				PetInventory.getInstance().newInventory(p);
			}
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.main.particle")) {
				p.closeInventory();
				
				ParticleInventory.getInstance().newInventory(p);
			}
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.main.visibility")) {
				p.closeInventory();
				
				VisibilityInventory.getInstance().newInventory(p);
			}
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.gui.main.armor")) {
				p.closeInventory();
				
				ArmorInventory.getInstance().newInventory(p);
			}
		}


		if (open.getName() == mm.getMessages().getString("Messages.gui.particle.title")) {
			e.setCancelled(true);
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.dripLava"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "drip_Lava") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }
					
					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "drip_Lava");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.DRIP_LAVA, p);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("dripLava"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.dripLava");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.dripLava", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.dripWater"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "drip_Water") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }
					
					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "drip_Water");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.DRIP_WATER, p);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("dripWater"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.dripWater");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.dripWater", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.note"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "note") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }
					
					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "note");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.NOTE, p);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("note"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.note");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.note", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.heart"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "heart") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }

					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "heart");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.HEART, p);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("heart"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.heart");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.heart", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.lava"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "lava") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }

					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "lava");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.LAVA, p);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("lava"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.lava");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.lava", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.redstone"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "redstone") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }

					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "redstone");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.REDSTONE, p);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("redstone"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.redstone");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.redstone", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.slime"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "slime") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }

					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "slime");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.SLIME, p);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("slime"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.slime");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.slime", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.snowball"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "snowball") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }

					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "snowball");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.SNOWBALL, p);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("snowball"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.snowball");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.snowball", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.spell"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "spell") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }
					
					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "spell");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.SPELL, p);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("spell"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.spell");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.spell", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.townaura"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "town_aura") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }
					
					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "town_aura");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.TOWN_AURA, p);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("townaura"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.townaura");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.townaura", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.villagerAngry"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "villager_Angry") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }

					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "villager_Angry");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.VILLAGER_ANGRY, p);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("villagerAngry"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.villagerAngry");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.villagerAngry", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.particle.villagerHappy"))) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "villager_Happy") {
					try { Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName())); } catch (Exception exe) { }
				} else {
					try {
						Bukkit.getScheduler().cancelTask(ParticleInventory.taskIDs.get(p.getName()));
					} catch (Exception exe) { }

					dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "villager_Happy");
					dm.saveData();
					ParticleInventory.getInstance().summonParticle(ParticleEffect.VILLAGER_HAPPY, p);
				}
				
				p.closeInventory();
			}

			if (item.getItemMeta().getDisplayName().equals(buyNameParticle("villagerHappy"))) {
				String price = Main.getInstance().getConfig().getString("Config.particle.price.villagerHappy");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.villagerHappy", true);
					dm.saveData();
					p.closeInventory();
					ParticleInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
		}
		
		
		if (open.getName() == mm.getMessages().getString("Messages.gui.pet.title")) {
			Pets pets = new Pets();
			
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			
			// Hier wenn Wolf geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.wolf"))) {
				p.closeInventory();

				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "WOLF") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "WOLF");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "WOLF");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("wolf"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.wolf");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.wolf", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			// Hier wenn Schaf geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.sheep"))) {
				p.closeInventory();
				
				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "SHEEP") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "SHEEP");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "SHEEP");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("sheep"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.sheep");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.sheep", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			// Hier wenn Huhn geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.chicken"))) {
				p.closeInventory();

				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "CHICKEN") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "CHICKEN");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "CHICKEN");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("chicken"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.chicken");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.chicken", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			// Hier wenn Pferd geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.horse"))) {
				p.closeInventory();

				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "HORSE") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "HORSE");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "HORSE");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("horse"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.horse");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.horse", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			// Hier wenn Schwein geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.pig"))) {
				p.closeInventory();

				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "PIG") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "PIG");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "PIG");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("pig"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.pig");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.pig", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			// Hier wenn Kuh geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.cow"))) {
				p.closeInventory();

				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "COW") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "COW");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "COW");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("cow"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.cow");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.cow", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			// Hier wenn Pilzkuh geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.mooshroom"))) {
				p.closeInventory();

				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "MUSHROOM_COW") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "MUSHROOM_COW");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "MUSHROOM_COW");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("mooshroom"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.mooshroom");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.mooshroom", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			// Hier wenn Katze geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.ocelot"))) {
				p.closeInventory();

				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "OCELOT") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "OCELOT");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "OCELOT");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("ocelot"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.ocelot");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.ocelot", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			// Hier wenn Hase geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.rabbit"))) {
				p.closeInventory();

				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "RABBIT") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "RABBIT");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "RABBIT");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("rabbit"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.rabbit");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.rabbit", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			// Hier wenn Dorfbewohner geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.villager"))) {
				p.closeInventory();

				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "VILLAGER") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "VILLAGER");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "VILLAGER");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("villager"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.villager");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.villager", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			// Hier wenn Tintenfisch geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.squid"))) {
				p.closeInventory();

				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "SQUID") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "SQUID");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "SQUID");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("squid"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.squid");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.squid", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			// Hier wenn Silberfisch geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.pet.silverfish"))) {
				p.closeInventory();
				
				if (dm.getData().get("Data." + p.getName().toLowerCase() + ".pet") == "SILVERFISH") {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", null);
					dm.saveData();
				} else {
					pets.removePet(p);
					pets.createPet(p, "SILVERFISH");
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "SILVERFISH");
					dm.saveData();
				}
			}
			if (item.getItemMeta().getDisplayName().equals(buyName("silverfish"))) {
				String price = Main.getInstance().getConfig().getString("Config.pet.price.silverfish");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.silverfish", true);
					dm.saveData();
					p.closeInventory();
					PetInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			

			// Hier wenn Reiten geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.pet.ride"))) {
				p.closeInventory();
				if (InventoryClick.pets.containsKey(p.getName())) {
					if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".ride") == "false") {
						if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".hat") == "true") {
							p.eject();
							dm.getData().set("Data." + p.getName().toLowerCase() + ".hat", "false");
							dm.saveData();
						}
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
							
							@Override
							public void run() {
								pets.sittOnPet(true, p);
							}
						}, 5L);
					} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".ride") == "true") {
						pets.sittOnPet(false, p);
					}
				} else {
					mm.sendMessage("Messages.pet.noPet", p);
				}
			}
			// Hier wenn Umbenennen geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.pet.rename"))) {
				p.closeInventory();
				
		        final AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
		        	public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
		            event.setWillClose(false);
		            event.setWillDestroy(false);
		            
		            if (event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT) {
			            if (!event.getName().equalsIgnoreCase("")) {
			            	dm.getData().set("Data." + p.getName().toLowerCase() + ".petName", event.getName());
			            	p.sendMessage(mm.getMessages().getString("Messages.pet.renamed").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%name%", event.getName()));
					        
							Entity ent = InventoryClick.pets.get(p.getName());
							pets.removePet(p);
							pets.createPet(p, ent.getType().toString().toUpperCase());
							
			            	event.setWillClose(true);
			            	event.setWillDestroy(true);
			            }
		            } else {
		            	event.setWillClose(false);
		            	event.setWillDestroy(false);
		            }
		          }
		        });
		        ItemStack nametag = new ItemStack(Material.NAME_TAG);
		        ItemMeta nametagmeta = nametag.getItemMeta();
		        nametagmeta.setDisplayName(mm.getMessages().getString("Messages.gui.pet.anvilNametagName"));
		        nametag.setItemMeta(nametagmeta);
		        
		        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, nametag);
		        
		        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable()
		        {
		          public void run()
		          {
		            try
		            {
		              gui.open();
		            }
		            catch (IllegalAccessException e)
		            {
		              e.printStackTrace();
		            }
		            catch (InvocationTargetException e)
		            {
		              e.printStackTrace();
		            }
		            catch (InstantiationException e)
		            {
		              e.printStackTrace();
		            }
		          }
		        }, 3L);
			}
			// Hier wenn Entfernen geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.pet.remove"))) {
				p.closeInventory();
				if (InventoryClick.pets.containsKey(p.getName())) {
					pets.removePet(p);
					dm.getData().set("Data." + p.getName().toLowerCase() + ".pet", "null");
					dm.saveData();
				} else {
					mm.sendMessage("Messages.pet.noPet", p);
				}
			}
			// Hier wenn Aufsetzten geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.pet.hat"))) {
				p.closeInventory();
				if (InventoryClick.pets.containsKey(p.getName())) {
					Entity entity = InventoryClick.pets.get(p.getName());
					if (entity != null) {
						if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".hat") == "false") {
							if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".ride") == "true") {
								pets.sittOnPet(false, p);
							}
							Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
								
								@Override
								public void run() {
									p.setPassenger(entity);
								}
							}, 5L);
							dm.getData().set("Data." + p.getName().toLowerCase() + ".hat", "true");
						} else {
							p.eject();
							dm.getData().set("Data." + p.getName().toLowerCase() + ".hat", "false");
						}
						dm.saveData();
					}
				} else {
					mm.sendMessage("Messages.pet.noPet", p);
				}
			}
			// Hier wenn Baby geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.pet.baby"))) {
				if (InventoryClick.pets.containsKey(p.getName())) {
					Entity ent = InventoryClick.pets.get(p.getName());
					pets.removePet(p);
					
					dm.getData().set("Data." + p.getName().toLowerCase() + ".baby", "false");
					dm.saveData();
					
					pets.createPet(p, ent.getType().toString().toUpperCase());
				} else {
					mm.sendMessage("Messages.pet.noPet", p);
				}
				p.closeInventory();
			}
			// Hier wenn Erwachsen geklickt
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.pet.adult"))) {
				if (InventoryClick.pets.containsKey(p.getName())) {
					Entity ent = InventoryClick.pets.get(p.getName());
					pets.removePet(p);
					
					dm.getData().set("Data." + p.getName().toLowerCase() + ".baby", "true");
					dm.saveData();
					
					pets.createPet(p, ent.getType().toString().toUpperCase());
				} else {
					mm.sendMessage("Messages.pet.noPet", p);
				}
				p.closeInventory();
			}
		}

		if (open.getName() == mm.getMessages().getString("Messages.gui.armor.title")) {
			e.setCancelled(true);
			
			if (item == null || !item.hasItemMeta()) {
				return;
			}
			
			// DIAMOND
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"))) {
				ItemStack setitem = new ItemStack(Material.DIAMOND_HELMET);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
				setitem.setItemMeta(setitemMeta);
				
				if (!(p.getInventory().getHelmet() == null || p.getInventory().getHelmet().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getHelmet().getItemMeta().getDisplayName())) {
						p.getInventory().setHelmet(null);
					} else {
						p.getInventory().setHelmet(setitem);
					}
				} else {
					p.getInventory().setHelmet(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("diamond", "helmet"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.diamond");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.helmet", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"))) {
				ItemStack setitem = new ItemStack(Material.DIAMOND_CHESTPLATE);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
				setitem.setItemMeta(setitemMeta);
				
				if (!(p.getInventory().getChestplate() == null || p.getInventory().getChestplate().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getChestplate().getItemMeta().getDisplayName())) {
						p.getInventory().setChestplate(null);
					} else {
						p.getInventory().setChestplate(setitem);
					}
				} else {
					p.getInventory().setChestplate(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("diamond", "chestplate"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.diamond");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.chestplate", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"))) {
				ItemStack setitem = new ItemStack(Material.DIAMOND_LEGGINGS);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
				setitem.setItemMeta(setitemMeta);
				
				if (!(p.getInventory().getLeggings() == null || p.getInventory().getLeggings().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getLeggings().getItemMeta().getDisplayName())) {
						p.getInventory().setLeggings(null);
					} else {
						p.getInventory().setLeggings(setitem);
					}
				} else {
					p.getInventory().setLeggings(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("diamond", "leggins"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.diamond");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.leggins", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.boots"))) {
				ItemStack setitem = new ItemStack(Material.DIAMOND_BOOTS);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
				setitem.setItemMeta(setitemMeta);
				
				if (!(p.getInventory().getBoots() == null || p.getInventory().getBoots().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getBoots().getItemMeta().getDisplayName())) {
						p.getInventory().setBoots(null);
					} else {
						p.getInventory().setBoots(setitem);
					}
				} else {
					p.getInventory().setBoots(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("diamond", "boots"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.diamond");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.boots", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			// GOLD
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"))) {
				ItemStack setitem = new ItemStack(Material.GOLD_HELMET);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
				setitem.setItemMeta(setitemMeta);
				
				if (!(p.getInventory().getHelmet() == null || p.getInventory().getHelmet().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getHelmet().getItemMeta().getDisplayName())) {
						p.getInventory().setHelmet(null);
					} else {
						p.getInventory().setHelmet(setitem);
					}
				} else {
					p.getInventory().setHelmet(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("gold", "helmet"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.gold");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.helmet", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"))) {
				ItemStack setitem = new ItemStack(Material.GOLD_CHESTPLATE);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
				setitem.setItemMeta(setitemMeta);
				
				if (!(p.getInventory().getChestplate() == null || p.getInventory().getChestplate().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getChestplate().getItemMeta().getDisplayName())) {
						p.getInventory().setChestplate(null);
					} else {
						p.getInventory().setChestplate(setitem);
					}
				} else {
					p.getInventory().setChestplate(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("gold", "chestplate"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.gold");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.chestplate", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"))) {
				ItemStack setitem = new ItemStack(Material.GOLD_LEGGINGS);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
				setitem.setItemMeta(setitemMeta);
				
				if (!(p.getInventory().getLeggings() == null || p.getInventory().getLeggings().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getLeggings().getItemMeta().getDisplayName())) {
						p.getInventory().setLeggings(null);
					} else {
						p.getInventory().setLeggings(setitem);
					}
				} else {
					p.getInventory().setLeggings(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("gold", "leggins"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.gold");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.leggins", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.boots"))) {
				ItemStack setitem = new ItemStack(Material.GOLD_BOOTS);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
				setitem.setItemMeta(setitemMeta);
				
				if (!(p.getInventory().getBoots() == null || p.getInventory().getBoots().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getBoots().getItemMeta().getDisplayName())) {
						p.getInventory().setBoots(null);
					} else {
						p.getInventory().setBoots(setitem);
					}
				} else {
					p.getInventory().setBoots(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("gold", "boots"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.gold");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.gold.boots", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			// IRON
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"))) {
				ItemStack setitem = new ItemStack(Material.IRON_HELMET);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
				setitem.setItemMeta(setitemMeta);
				
				if (!(p.getInventory().getHelmet() == null || p.getInventory().getHelmet().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getHelmet().getItemMeta().getDisplayName())) {
						p.getInventory().setHelmet(null);
					} else {
						p.getInventory().setHelmet(setitem);
					}
				} else {
					p.getInventory().setHelmet(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("iron", "helmet"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.iron");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.helmet", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"))) {
				ItemStack setitem = new ItemStack(Material.IRON_CHESTPLATE);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
				setitem.setItemMeta(setitemMeta);
				
				if (!(p.getInventory().getChestplate() == null || p.getInventory().getChestplate().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getChestplate().getItemMeta().getDisplayName())) {
						p.getInventory().setChestplate(null);
					} else {
						p.getInventory().setChestplate(setitem);
					}
				} else {
					p.getInventory().setChestplate(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("iron", "chestplate"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.iron");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.chestplate", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"))) {
				ItemStack setitem = new ItemStack(Material.IRON_LEGGINGS);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
				setitem.setItemMeta(setitemMeta);
				if (!(p.getInventory().getLeggings() == null || p.getInventory().getLeggings().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getLeggings().getItemMeta().getDisplayName())) {
						p.getInventory().setLeggings(null);
					} else {
						p.getInventory().setLeggings(setitem);
					}
				} else {
					p.getInventory().setLeggings(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("iron", "leggins"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.iron");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.leggins", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.boots"))) {
				ItemStack setitem = new ItemStack(Material.IRON_BOOTS);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
				setitem.setItemMeta(setitemMeta);
				if (!(p.getInventory().getBoots() == null || p.getInventory().getBoots().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getBoots().getItemMeta().getDisplayName())) {
						p.getInventory().setBoots(null);
					} else {
						p.getInventory().setBoots(setitem);
					}
				} else {
					p.getInventory().setBoots(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("iron", "boots"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.iron");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.iron.boots", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			
			// LEATHER
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"))) {
				ItemStack setitem = new ItemStack(Material.LEATHER_HELMET);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
				setitem.setItemMeta(setitemMeta);
				if (!(p.getInventory().getHelmet() == null || p.getInventory().getHelmet().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getHelmet().getItemMeta().getDisplayName())) {
						p.getInventory().setHelmet(null);
					} else {
						p.getInventory().setHelmet(setitem);
					}
				} else {
					p.getInventory().setHelmet(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("leather", "helmet"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.leather");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.helmet", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"))) {
				ItemStack setitem = new ItemStack(Material.LEATHER_CHESTPLATE);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
				setitem.setItemMeta(setitemMeta);
				if (!(p.getInventory().getChestplate() == null || p.getInventory().getChestplate().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getChestplate().getItemMeta().getDisplayName())) {
						p.getInventory().setChestplate(null);
					} else {
						p.getInventory().setChestplate(setitem);
					}
				} else {
					p.getInventory().setChestplate(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("leather", "chestplate"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.leather");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.chestplate", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"))) {
				ItemStack setitem = new ItemStack(Material.LEATHER_LEGGINGS);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
				setitem.setItemMeta(setitemMeta);
				if (!(p.getInventory().getLeggings() == null || p.getInventory().getLeggings().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getLeggings().getItemMeta().getDisplayName())) {
						p.getInventory().setLeggings(null);
					} else {
						p.getInventory().setLeggings(setitem);
					}
				} else {
					p.getInventory().setLeggings(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("leather", "leggins"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.leather");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.leggins", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}

			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.boots"))) {
				ItemStack setitem = new ItemStack(Material.LEATHER_BOOTS);
				ItemMeta setitemMeta = setitem.getItemMeta();
				setitemMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
				setitem.setItemMeta(setitemMeta);
				if (!(p.getInventory().getBoots() == null || p.getInventory().getBoots().getItemMeta().getDisplayName() == null)) {
					if (item.getItemMeta().getDisplayName().equals(p.getInventory().getBoots().getItemMeta().getDisplayName())) {
						p.getInventory().setBoots(null);
					} else {
						p.getInventory().setBoots(setitem);
					}
				} else {
					p.getInventory().setBoots(setitem);
				}
				
				p.closeInventory();
			}
			if (item.getItemMeta().getDisplayName().equals(buyNameArmor("leather", "boots"))) {
				String price = Main.getInstance().getConfig().getString("Config.armor.price.leather");
				if (MoneyManager.getMoney(p) >= Integer.parseInt(price)) {
					Main.eco.withdrawPlayer(p, Double.parseDouble(price));
					dm.getData().set("Data." + p.getName().toLowerCase() + ".bought.armor.leather.boots", true);
					dm.saveData();
					p.closeInventory();
					ArmorInventory.getInstance().newInventory(p);
				} else {
					mm.sendMessage("Messages.economy.notEnoughMoney", p);
				}
			}
			if (item.getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.gui.armor.removeAll"))) {
				p.getInventory().setHelmet(null);
				p.getInventory().setChestplate(null);
				p.getInventory().setLeggings(null);
				p.getInventory().setBoots(null);
				
				p.closeInventory();
			}
		}
	}
	
	public String buyName(String name) {
		MessagesManager mm = new MessagesManager();
		
		return (mm.getMessages().getString("Messages.gui.pet.buy")+" " + mm.getMessages().getString("Messages.gui.pet." + name) );
	}
	public String buyNameParticle(String name) {
		MessagesManager mm = new MessagesManager();
		
		return (mm.getMessages().getString("Messages.gui.pet.buy")+" " + mm.getMessages().getString("Messages.gui.particle." + name) );
	}
	public String buyNameArmor(String material, String type) {
		MessagesManager mm = new MessagesManager();
		
		return (mm.getMessages().getString("Messages.gui.pet.buy")+" " + mm.getMessages().getString("Messages.gui.armor." + material)+" " + mm.getMessages().getString("Messages.gui.armor." + type) );
	}
}
