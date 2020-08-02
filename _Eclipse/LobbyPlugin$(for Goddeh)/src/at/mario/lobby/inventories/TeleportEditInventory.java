package at.mario.lobby.inventories;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class TeleportEditInventory {

	public TeleportEditInventory() { }
	
	private static TeleportEditInventory instance = new TeleportEditInventory();
	
	public static TeleportEditInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages.gui.teleportEdit.title"));

		
		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);

		ItemStack cancel = new ItemStack(Material.SLIME_BALL);
		ItemMeta cancelMeta = cancel.getItemMeta();
		cancelMeta.setDisplayName(mm.getMessages().getString("Messages.gui.teleportEdit.cancel"));
		cancel.setItemMeta(cancelMeta);		

		ItemStack info = new ItemStack(Material.SIGN);
		ItemMeta infoMeta = info.getItemMeta();
		infoMeta.setDisplayName("§6Name:");
		ArrayList<String> infoList = new ArrayList<String>();
		infoList.add(dm.getData().getString("temp.name"));
		infoMeta.setLore(infoList);
		info.setItemMeta(infoMeta);
				
		ItemStack location = new ItemStack(Material.ENDER_PEARL);
		ItemMeta locationMeta = location.getItemMeta();
		locationMeta.setDisplayName("Location");
		ArrayList<String> locationList = new ArrayList<String>();
		locationList.add("§6World§f: §a" + p.getLocation().getWorld().getName());
		locationList.add(" ");
		locationList.add("§2X§f: §a" + p.getLocation().getX());
		locationList.add("§2Y§f: §a" + p.getLocation().getY());
		locationList.add("§2Z§f: §a" + p.getLocation().getZ());
		locationList.add(" ");
		locationList.add("§bYaw§f: §a" + p.getLocation().getYaw());
		locationList.add("§bPitch§f: §a" + p.getLocation().getPitch());
		locationMeta.setLore(locationList);
		location.setItemMeta(locationMeta);
		
		ItemStack select = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		ItemMeta selectMeta = select.getItemMeta();
		selectMeta.setDisplayName(mm.getMessages().getString("Messages.gui.teleportEdit.select"));
		select.setItemMeta(selectMeta);
		
		
		inv.setItem(0, nothing);
		inv.setItem(1, nothing);
		inv.setItem(2, nothing);
		inv.setItem(3, nothing);
		inv.setItem(4, nothing);
		inv.setItem(5, nothing);
		inv.setItem(6, nothing);
		inv.setItem(7, nothing);
		inv.setItem(8, nothing);

		inv.setItem(9, nothing);
		inv.setItem(10, info);
		inv.setItem(11, nothing);
		inv.setItem(12, nothing);
		inv.setItem(13, select);
		inv.setItem(14, nothing);
		inv.setItem(15, location);
		inv.setItem(16, nothing);
		inv.setItem(17, cancel);
		
		inv.setItem(18, nothing);
		inv.setItem(19, nothing);
		inv.setItem(20, nothing);
		inv.setItem(21, nothing);
		inv.setItem(22, nothing);
		inv.setItem(23, nothing);
		inv.setItem(24, nothing);
		inv.setItem(25, nothing);
		inv.setItem(26, nothing);
		
		
		p.openInventory(inv);
	}
}
