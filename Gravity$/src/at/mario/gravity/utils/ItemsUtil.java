package at.mario.gravity.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.gravity.Main;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;

public class ItemsUtil {

	public static void giveLobbyItems(Player p) {
		MessagesManager mm = new MessagesManager();

		removeItems(p);
		
		ItemStack leave = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.lobbyitems.leaveItem.material")), Main.getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.amount"), (short) 
				Main.getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.damage"));
		ItemMeta leaveMeta = leave.getItemMeta();
		leaveMeta.setDisplayName(mm.getMessages().getString("Messages.lobbyitems.leaveItem.name"));
		leaveMeta.setLore(mm.getMessages().getStringList("Messages.lobbyitems.leaveItem.lore"));
		leave.setItemMeta(leaveMeta);
		
		ItemStack shop = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.lobbyitems.shopItem.material")), Main.getInstance().getConfig().getInt("Config.lobbyitems.shopItem.amount"), (short) 
				Main.getInstance().getConfig().getInt("Config.lobbyitems.shopItem.damage"));
		ItemMeta shopMeta = shop.getItemMeta();
		shopMeta.setDisplayName(mm.getMessages().getString("Messages.lobbyitems.shopItem.name"));
		shopMeta.setLore(mm.getMessages().getStringList("Messages.lobbyitems.shopItem.lore"));
		shop.setItemMeta(shopMeta);
		
		
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.slot"), leave);
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.shopItem.slot"), shop);
	}

	public static void giveSpectatorItems(Player p) {
		MessagesManager mm = new MessagesManager();

		removeItems(p);
		
		ItemStack leave = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.lobbyitems.leaveItem.material")), Main.getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.amount"), (short) 
				Main.getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.damage"));
		ItemMeta leaveMeta = leave.getItemMeta();
		leaveMeta.setDisplayName(mm.getMessages().getString("Messages.lobbyitems.leaveItem.name"));
		leaveMeta.setLore(mm.getMessages().getStringList("Messages.lobbyitems.leaveItem.lore"));
		leave.setItemMeta(leaveMeta);
		
		ItemStack shop = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.lobbyitems.shopItem.material")), Main.getInstance().getConfig().getInt("Config.lobbyitems.shopItem.amount"), (short) 
				Main.getInstance().getConfig().getInt("Config.lobbyitems.shopItem.damage"));
		ItemMeta shopMeta = shop.getItemMeta();
		shopMeta.setDisplayName(mm.getMessages().getString("Messages.lobbyitems.shopItem.name"));
		shopMeta.setLore(mm.getMessages().getStringList("Messages.lobbyitems.shopItem.lore"));
		shop.setItemMeta(shopMeta);
		
		
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.slot"), leave);
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.shopItem.slot"), shop);
	}

	public static void giveIngameItems(Boolean atGamestart, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();

		removeItems(p);
		
		ItemStack reset = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.ingameitems.resetItem.material")), Main.getInstance().getConfig().getInt("Config.ingameitems.resetItem.amount"), 
				(short) Main.getInstance().getConfig().getInt("Config.ingameitems.resetItem.damage"));
		ItemMeta resetMeta = reset.getItemMeta();
		resetMeta.setDisplayName(mm.getMessages().getString("Messages.ingameitems.resetItem.name"));
		resetMeta.setLore(mm.getMessages().getStringList("Messages.ingameitems.resetItem.lore"));
		reset.setItemMeta(resetMeta);
		
		
		if (atGamestart) {
			dm.getData().set("Data." + p.getName() + ".visibility", "all");
			dm.saveData();
		}
		
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.ingameitems.visibilityItem.slot"), getVisibilityItem(p));
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.ingameitems.resetItem.slot"), reset);
	}

	public static void giveFinisherItems(Player p) {
		MessagesManager mm = new MessagesManager();
		
		removeItems(p);
		
		ItemStack leave = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.finishedItems.leaveItem.material")), Main.getInstance().getConfig().getInt("Config.finishedItems.leaveItem.amount"), 
				(short) Main.getInstance().getConfig().getInt("Config.finishedItems.leaveItem.damage"));
		ItemMeta leaveMeta = leave.getItemMeta();
		leaveMeta.setDisplayName(mm.getMessages().getString("Messages.finishedItems.leaveItem.name"));
		leaveMeta.setLore(mm.getMessages().getStringList("Messages.finishedItems.leaveItem.lore"));
		leave.setItemMeta(leaveMeta);
		
		ItemStack teleporter = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.finishedItems.teleporterItem.material")), 
				Main.getInstance().getConfig().getInt("Config.finishedItems.teleporterItem.amount"), (short) Main.getInstance().getConfig().getInt("Config.finishedItems.teleporterItem.damage"));
		ItemMeta teleporterMeta = teleporter.getItemMeta();
		teleporterMeta.setDisplayName(mm.getMessages().getString("Messages.finishedItems.teleporterItem.name"));
		teleporterMeta.setLore(mm.getMessages().getStringList("Messages.finishedItems.teleporterItem.lore"));
		teleporter.setItemMeta(teleporterMeta);
		
		
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.finishedItems.leaveItem.slot"), leave);
		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.finishedItems.teleporterItem.slot"), teleporter);
	}
	
	public static void giveSkipItem(Player p) {
		MessagesManager mm = new MessagesManager();

		removeItems(p);
		giveIngameItems(false, p);

		ItemStack skip = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.ingameitems.skipItem.material")), Main.getInstance().getConfig().getInt("Config.ingameitems.skipItem.amount"), (short) 
				Main.getInstance().getConfig().getInt("Config.ingameitems.skipItem.damage"));
		ItemMeta skipMeta = skip.getItemMeta();
		skipMeta.setDisplayName(mm.getMessages().getString("Messages.ingameitems.skipItem.name"));
		skipMeta.setLore(mm.getMessages().getStringList("Messages.ingameitems.skipItem.lore"));
		skip.setItemMeta(skipMeta);

		p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.ingameitems.skipItem.slot"), skip);
	}
	
	public static ItemStack getVisibilityItem(Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();

		removeItems(p);
		
		ItemStack visibility = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.ingameitems.visibilityItem." + dm.getData().getString("Data." + p.getName() + ".visibility") + ".material")), 
				Main.getInstance().getConfig().getInt("Config.ingameitems.visibilityItem." + dm.getData().getString("Data." + p.getName() + ".visibility") + ".amount"), 
				(short) Main.getInstance().getConfig().getInt("Config.ingameitems.visibilityItem." + dm.getData().getString("Data." + p.getName() + ".visibility") + ".damage"));
		ItemMeta visibilityMeta = visibility.getItemMeta();
		visibilityMeta.setDisplayName(mm.getMessages().getString("Messages.ingameitems.visibilityItem." + dm.getData().getString("Data." + p.getName() + ".visibility") + ".name"));
		visibilityMeta.setLore(mm.getMessages().getStringList("Messages.ingameitems.visibilityItem." + dm.getData().getString("Data." + p.getName() + ".visibility") + ".lore"));
		visibility.setItemMeta(visibilityMeta);
		
		return visibility;
	}
	

	public static void removeItems(Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.slot")) != null) {
			if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.leaveItem.name"))) {
					p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.leaveItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.shopItem.slot")) != null) {
			if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.shopItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.shopItem.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.shopItem.name"))) {
					
					p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.shopItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.startItem.slot")) != null) {
			if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.startItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.startItem.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.startItem.name"))) {
					
					p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.lobbyitems.startItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.ingameitems.resetItem.slot")) != null) {
			if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.ingameitems.resetItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.ingameitems.resetItem.slot")).getItemMeta().getDisplayName().
						equals(mm.getMessages().getString("Messages.ingameitems.resetItem.name"))) {
					
					p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.ingameitems.resetItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.ingameitems.visibilityItem.slot")) != null) {
			if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.ingameitems.visibilityItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.ingameitems.visibilityItem.slot")).getItemMeta().getDisplayName().
						equals(mm.getMessages().getString("Messages.ingameitems.visibilityItem." + dm.getData().getString("Data." + p.getName() + ".visibility") + ".name"))) {
					
					p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.ingameitems.visibilityItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.ingameitems.skipItem.slot")) != null) {
			if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.ingameitems.skipItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.ingameitems.skipItem.slot")).getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.ingameitems.skipItem.name"))) {
					
					p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.ingameitems.skipItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.finishedItems.leaveItem.slot")) != null) {
			if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.finishedItems.leaveItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.finishedItems.leaveItem.slot")).getItemMeta().getDisplayName().
						equals(mm.getMessages().getString("Messages.finishedItems.leaveItem.name"))) {
					
					p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.finishedItems.leaveItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
		if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.finishedItems.teleporterItem.slot")) != null) {
			if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.finishedItems.teleporterItem.slot")).hasItemMeta()) {
				if (p.getInventory().getItem(Main.getInstance().getConfig().getInt("Config.finishedItems.teleporterItem.slot")).getItemMeta().getDisplayName().
						equals(mm.getMessages().getString("Messages.finishedItems.teleporterItem.name"))) {
					
					p.getInventory().setItem(Main.getInstance().getConfig().getInt("Config.finishedItems.teleporterItem.slot"), new ItemStack(Material.AIR));
				}
			}
		}
	}
}
