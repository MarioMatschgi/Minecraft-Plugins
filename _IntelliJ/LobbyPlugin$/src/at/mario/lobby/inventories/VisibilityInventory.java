package at.mario.lobby.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class VisibilityInventory {

	public VisibilityInventory() { }
	
	private static VisibilityInventory instance = new VisibilityInventory();
	
	public static VisibilityInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		MessagesManager mm = new MessagesManager();
		//DataManager dm = new DataManager();
		//ConfigManager cm = new ConfigManager();
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages.gui.visibility.title"));
		
		ItemStack all = new ItemStack(Material.INK_SACK, 1, (short) 10);
		ItemMeta allMeta = all.getItemMeta();
		allMeta.setDisplayName(mm.getMessages().getString("Messages.gui.visibility.all"));
		all.setItemMeta(allMeta);
		
		ItemStack VIP = new ItemStack(Material.INK_SACK, 1, (short) 5);
		ItemMeta VIPMeta = VIP.getItemMeta();
		VIPMeta.setDisplayName(mm.getMessages().getString("Messages.gui.visibility.VIP"));
		VIP.setItemMeta(VIPMeta);

		ItemStack nobody = new ItemStack(Material.INK_SACK, 1, (short) 8);
		ItemMeta nobodyMeta = nobody.getItemMeta();
		nobodyMeta.setDisplayName(mm.getMessages().getString("Messages.gui.visibility.nobody"));
		nobody.setItemMeta(nobodyMeta);
		

		inv.setItem(11, all);
		inv.setItem(13, VIP);
		inv.setItem(15, nobody);
		
		p.openInventory(inv);
	}
}
