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
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;

public class PassShopInventory {

	public PassShopInventory() { }
	
	private static PassShopInventory instance = new PassShopInventory();
	
	public static PassShopInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();

		String path = "inventory.passShop";
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages." + path + ".title"));
		
		
		int joinmes = (Main.getInstance().getConfig().getBoolean("Config.unlimitedJoinmePassesWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infiniteJoinmePasses") || p.isOp()) )
				? 99999 : dm.getData().getInt("Data." + p.getName() + ".joinmes");
		// Item erstellen
		ItemStack buyJoinmePasses = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".buyJoinmePasses.material")));
		ItemMeta buyJoinmePassesMeta = buyJoinmePasses.getItemMeta();
		buyJoinmePassesMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".buyJoinmePasses.name"));
		List<String> buyJoinmePassesList = mm.getMessages().getStringList("Messages." + path + ".buyJoinmePasses.lore");
		for (int i = 0; i < buyJoinmePassesList.size(); i++)
			buyJoinmePassesList.set(i, buyJoinmePassesList.get(i).replace("%joinmes%", joinmes+"").
					replace("%price%", Main.getInstance().getConfig().getInt("Config.prices.passes.joinmePass")+""));
		buyJoinmePassesMeta.setLore(buyJoinmePassesList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".buyJoinmePasses.enchantedGlow")) {
			buyJoinmePassesMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			buyJoinmePassesMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		buyJoinmePasses.setItemMeta(buyJoinmePassesMeta);
		buyJoinmePasses.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".buyJoinmePasses.amount"));
		inv.setItem(Main.getInstance().getConfig().getInt("Config." + path + ".buyJoinmePasses.slot"), buyJoinmePasses);

		
		int seekerPasses = (Main.getInstance().getConfig().getBoolean("Config.unlimitedSeekerPassesWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infiniteSeekerPasses") || p.isOp()) )
				? 99999 : dm.getData().getInt("Data." + p.getName() + ".seekerPasses");
		// Item erstellen
		ItemStack buySeekerPasses = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".buySeekerPasses.material")));
		ItemMeta buySeekerPassesMeta = buySeekerPasses.getItemMeta();
		buySeekerPassesMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".buySeekerPasses.name"));
		List<String> buySeekerPassesList = mm.getMessages().getStringList("Messages." + path + ".buySeekerPasses.lore");
		for (int i = 0; i < buySeekerPassesList.size(); i++)
			buySeekerPassesList.set(i, buySeekerPassesList.get(i).replace("%seekerpasses%", seekerPasses+"").
					replace("%price%", Main.getInstance().getConfig().getInt("Config.prices.passes.seekerPass")+""));
		buySeekerPassesMeta.setLore(buySeekerPassesList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".buySeekerPasses.enchantedGlow")) {
			buySeekerPassesMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			buySeekerPassesMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		buySeekerPasses.setItemMeta(buySeekerPassesMeta);
		buySeekerPasses.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".buySeekerPasses.amount"));
		inv.setItem(Main.getInstance().getConfig().getInt("Config." + path + ".buySeekerPasses.slot"), buySeekerPasses);

		
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
