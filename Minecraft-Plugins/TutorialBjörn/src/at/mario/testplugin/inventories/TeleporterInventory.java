package at.mario.testplugin.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.testplugin.manager.DataManager;

public class TeleporterInventory {
	
	public static void newInventory(Player p) {
		Inventory inv = Bukkit.getServer().createInventory(null, 4 * 9, "§aClick to §6teleport§f!");
		
		int idx = 0;
		for (String string : DataManager.getData().getConfigurationSection("Data.teleports").getKeys(false)) {
			ItemStack tpItem = new ItemStack(Material.STAINED_CLAY, 1, (short) 3);
			ItemMeta tpItemMeta = tpItem.getItemMeta();
			tpItemMeta.setDisplayName(string);
			tpItem.setItemMeta(tpItemMeta);
			
			inv.setItem(idx, tpItem);
			idx++;
		}

		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);

		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null) {
				inv.setItem(i, nothing);
			}
		}
		p.openInventory(inv);
	}
}