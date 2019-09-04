package at.mario.hidenseek.inventories;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;

public class ShopInventory {

	public ShopInventory() { }
	
	private static ShopInventory instance = new ShopInventory();
	
	public static ShopInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		MessagesManager mm = new MessagesManager();
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages.gui.shop.title"));

		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);
		
		ItemStack buyPassesMenue = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.inventory.shop.buyPassesMenueItem.material")));
		ItemMeta buyPassesMenueMeta = buyPassesMenue.getItemMeta();
		buyPassesMenueMeta.setDisplayName(mm.getMessages().getString("Messages.gui.shop.buyPassesMenue.name"));
		List<String> buyPassesMenueList = mm.getMessages().getStringList("Messages.gui.shop.buyPassesMenue.lore");
		for (int i = 0; i < buyPassesMenueList.size(); i++) {
			String string = buyPassesMenueList.get(i);
			buyPassesMenueList.set(i, string);
		}
		buyPassesMenueMeta.setLore(buyPassesMenueList);
		if (Main.getInstance().getConfig().getBoolean("Config.inventory.shop.buyPowerupsMenueItem.enchantedGlow")) {
			buyPassesMenueMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			buyPassesMenueMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		buyPassesMenue.setItemMeta(buyPassesMenueMeta);
		buyPassesMenue.setAmount(Main.getInstance().getConfig().getInt("Config.inventory.shop.buyPassesMenueItem.amount"));
		
		
		ItemStack buyPowerupsMenue = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.inventory.shop.buyPowerupsMenueItem.material")));
		ItemMeta buyPowerupsMenueMeta = buyPowerupsMenue.getItemMeta();
		buyPowerupsMenueMeta.setDisplayName(mm.getMessages().getString("Messages.gui.shop.buyPowerupsMenue.name"));
		List<String> buyPowerupsMenueList = mm.getMessages().getStringList("Messages.gui.shop.buyPowerupsMenue.lore");
		for (int i = 0; i < buyPowerupsMenueList.size(); i++) {
			String string = buyPowerupsMenueList.get(i);
			buyPowerupsMenueList.set(i, string);
		}
		buyPowerupsMenueMeta.setLore(buyPowerupsMenueList);
		if (Main.getInstance().getConfig().getBoolean("Config.inventory.shop.buyPowerupsMenueItem.enchantedGlow")) {
			buyPowerupsMenueMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			buyPowerupsMenueMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		buyPowerupsMenue.setItemMeta(buyPowerupsMenueMeta);
		buyPowerupsMenue.setAmount(Main.getInstance().getConfig().getInt("Config.inventory.shop.buyPassesMenueItem.amount"));
		
		
		inv.setItem(Main.getInstance().getConfig().getInt("Config.inventory.shop.buyPassesMenueItem.slot"), buyPassesMenue);
		inv.setItem(Main.getInstance().getConfig().getInt("Config.inventory.shop.buyPowerupsMenueItem.slot"), buyPowerupsMenue);
		
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR) {
				inv.setItem(i, nothing);
			}
		}
		
		p.openInventory(inv);
	}
}
