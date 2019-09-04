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

public class PowerUpShopInventory {

	public PowerUpShopInventory() { }
	
	private static PowerUpShopInventory instance = new PowerUpShopInventory();
	
	public static PowerUpShopInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		//DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();

		String path = "inventory.powerupShop";
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages." + path + ".title"));
		

		// Item erstellen
		ItemStack buyjumpBoost = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".buyjumpBoost.material")));
		ItemMeta buyjumpBoostMeta = buyjumpBoost.getItemMeta();
		buyjumpBoostMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".buyjumpBoost.name"));
		List<String> buyjumpBoostList = mm.getMessages().getStringList("Messages." + path + ".buyjumpBoost.lore");
		for (int i = 0; i < buyjumpBoostList.size(); i++)
			buyjumpBoostList.set(i, buyjumpBoostList.get(i).replace("%lvl%", Main.getInstance().getConfig().getInt("Config.powerups.jumpBoost.amplifier")+"").
					replace("%duration%", Main.getInstance().getConfig().getInt("Config.powerups.jumpBoost.duration")+"").
					replace("%price%", Main.getInstance().getConfig().getInt("Config.prices.powerups.jumpboost")+""));
		buyjumpBoostMeta.setLore(buyjumpBoostList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".buyjumpBoost.enchantedGlow")) {
			buyjumpBoostMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			buyjumpBoostMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		buyjumpBoost.setItemMeta(buyjumpBoostMeta);
		buyjumpBoost.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".buyjumpBoost.amount"));
		inv.setItem(Main.getInstance().getConfig().getInt("Config." + path + ".buyjumpBoost.slot"), buyjumpBoost);

		// Item erstellen
		ItemStack buyKnockback = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".buyKnockback.material")));
		ItemMeta buyKnockbackMeta = buyKnockback.getItemMeta();
		buyKnockbackMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".buyKnockback.name"));
		List<String> buyKnockbackList = mm.getMessages().getStringList("Messages." + path + ".buyKnockback.lore");
		for (int i = 0; i < buyKnockbackList.size(); i++)
			buyKnockbackList.set(i, buyKnockbackList.get(i).replace("%lvl%", Main.getInstance().getConfig().getInt("Config.powerups.knockback.amplifier")+"").
					replace("%hits%", Main.getInstance().getConfig().getInt("Config.powerups.knockback.hits")+"").
					replace("%price%", Main.getInstance().getConfig().getInt("Config.prices.powerups.knockback")+""));
		buyKnockbackMeta.setLore(buyKnockbackList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".buyKnockback.enchantedGlow")) {
			buyKnockbackMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			buyKnockbackMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		buyKnockback.setItemMeta(buyKnockbackMeta);
		buyKnockback.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".buyKnockback.amount"));
		inv.setItem(Main.getInstance().getConfig().getInt("Config." + path + ".buyKnockback.slot"), buyKnockback);

		// Item erstellen
		ItemStack buyInvisibility = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".buyInvisibility.material")));
		ItemMeta buyInvisibilityMeta = buyInvisibility.getItemMeta();
		buyInvisibilityMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".buyInvisibility.name"));
		List<String> buyInvisibilityList = mm.getMessages().getStringList("Messages." + path + ".buyInvisibility.lore");
		for (int i = 0; i < buyInvisibilityList.size(); i++)
			buyInvisibilityList.set(i, buyInvisibilityList.get(i).replace("%lvl%", Main.getInstance().getConfig().getInt("Config.powerups.invisibility.amplifier")+"").
					replace("%duration%", Main.getInstance().getConfig().getInt("Config.powerups.invisibility.duration")+"").
					replace("%price%", Main.getInstance().getConfig().getInt("Config.prices.powerups.invisibility")+""));
		buyInvisibilityMeta.setLore(buyInvisibilityList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".buyInvisibility.enchantedGlow")) {
			buyInvisibilityMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			buyInvisibilityMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		buyInvisibility.setItemMeta(buyInvisibilityMeta);
		buyInvisibility.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".buyInvisibility.amount"));
		inv.setItem(Main.getInstance().getConfig().getInt("Config." + path + ".buyInvisibility.slot"), buyInvisibility);

		// Item erstellen
		ItemStack buySpeed = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".buySpeed.material")));
		ItemMeta buySpeedMeta = buySpeed.getItemMeta();
		buySpeedMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".buySpeed.name"));
		List<String> buySpeedList = mm.getMessages().getStringList("Messages." + path + ".buySpeed.lore");
		for (int i = 0; i < buySpeedList.size(); i++)
			buySpeedList.set(i, buySpeedList.get(i).replace("%lvl%", Main.getInstance().getConfig().getInt("Config.powerups.speed.amplifier")+"").
					replace("%duration%", Main.getInstance().getConfig().getInt("Config.powerups.speed.duration")+"").
					replace("%price%", Main.getInstance().getConfig().getInt("Config.prices.powerups.speed")+""));
		buySpeedMeta.setLore(buySpeedList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".buySpeed.enchantedGlow")) {
			buySpeedMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			buySpeedMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		buySpeed.setItemMeta(buySpeedMeta);
		buySpeed.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".buySpeed.amount"));
		inv.setItem(Main.getInstance().getConfig().getInt("Config." + path + ".buySpeed.slot"), buySpeed);

		
		// Item erstellen
		ItemStack back = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".back.material")));
		ItemMeta backMeta = back.getItemMeta();
		backMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".back.name"));
		List<String> backList = mm.getMessages().getStringList("Messages." + path + ".back.lore");
		for (int i = 0; i < backList.size(); i++)
			backList.set(i, backList.get(i).replace("%lvl%", Main.getInstance().getConfig().getInt("Config.powerups.back.amplifier")+"").
					replace("%goTo%", mm.getMessages().getString("Messages.inventory.shop.title")));
		backMeta.setLore(backList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".back.enchantedGlow")) {
			backMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			backMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		back.setItemMeta(backMeta);
		back.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".back.amount"));
		inv.setItem(Main.getInstance().getConfig().getInt("Config." + path + ".back.slot"), back);

		
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
