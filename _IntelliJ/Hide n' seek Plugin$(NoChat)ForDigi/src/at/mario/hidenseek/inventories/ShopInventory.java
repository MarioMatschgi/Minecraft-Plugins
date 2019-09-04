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

		String path = "inventory.shop";
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages." + path + ".title"));
		
		
		// Item erstellen
		ItemStack buyPassesMenue = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".buyPassesMenue.material")));
		ItemMeta buyPassesMenueMeta = buyPassesMenue.getItemMeta();
		buyPassesMenueMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".buyPassesMenue.name"));
		List<String> buyPassesMenueList = mm.getMessages().getStringList("Messages." + path + ".buyPassesMenue.lore");
		for (int i = 0; i < buyPassesMenueList.size(); i++)
			buyPassesMenueList.set(i, buyPassesMenueList.get(i));
		buyPassesMenueMeta.setLore(buyPassesMenueList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".buyPassesMenue.enchantedGlow")) {
			buyPassesMenueMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			buyPassesMenueMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		buyPassesMenue.setItemMeta(buyPassesMenueMeta);
		buyPassesMenue.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".buyPassesMenue.amount"));
		inv.setItem(Main.getInstance().getConfig().getInt("Config." + path + ".buyPassesMenue.slot"), buyPassesMenue);
		
		
		// Item erstellen
		ItemStack buyPowerupsMenue = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".buyPowerupsMenue.material")));
		ItemMeta buyPowerupsMenueMeta = buyPowerupsMenue.getItemMeta();
		buyPowerupsMenueMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".buyPowerupsMenue.name"));
		List<String> buyPowerupsMenueList = mm.getMessages().getStringList("Messages." + path + ".buyPowerupsMenue.lore");
		for (int i = 0; i < buyPowerupsMenueList.size(); i++) {
			String string = buyPowerupsMenueList.get(i);
			buyPowerupsMenueList.set(i, string);
		}
		buyPowerupsMenueMeta.setLore(buyPowerupsMenueList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".buyPowerupsMenue.enchantedGlow")) {
			buyPowerupsMenueMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			buyPowerupsMenueMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		buyPowerupsMenue.setItemMeta(buyPowerupsMenueMeta);
		buyPowerupsMenue.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".buyPowerupsMenue.amount"));
		inv.setItem(Main.getInstance().getConfig().getInt("Config." + path + ".buyPowerupsMenue.slot"), buyPowerupsMenue);
		

		// nothing setzen
		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);
		for (int i = 0; i < inv.getSize(); i++)
			if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR)
				inv.setItem(i, nothing);
		
		p.openInventory(inv);
	}
}
