package at.mario.piratecraft.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.manager.ConfigManagers.MessagesManager;

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
		
	}
	

	public static void removeItems(Player p) {
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
	}
}
