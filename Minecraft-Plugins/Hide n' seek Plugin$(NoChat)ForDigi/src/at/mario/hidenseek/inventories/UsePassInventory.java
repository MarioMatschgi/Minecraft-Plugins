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

public class UsePassInventory {

	public UsePassInventory() { }
	
	private static UsePassInventory instance = new UsePassInventory();
	
	public static UsePassInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();

		String path = "inventory.usePass";
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages." + path + ".title"));

		int seekerPasses = (Main.getInstance().getConfig().getBoolean("Config.unlimitedSeekerPassesWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infiniteSeekerPasses") || p.isOp()) )
				? 99999 : dm.getData().getInt("Data." + p.getName() + ".seekerPasses");
		
		// Item erstellen
		ItemStack useSeekerPass = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config." + path + ".useSeekerPass.material")));
		ItemMeta useSeekerPassMeta = useSeekerPass.getItemMeta();
		useSeekerPassMeta.setDisplayName(mm.getMessages().getString("Messages." + path + ".useSeekerPass.name"));
		List<String> seekerPassesList = mm.getMessages().getStringList("Messages." + path + ".useSeekerPass.lore");
		for (int i = 0; i < seekerPassesList.size(); i++)
			seekerPassesList.set(i, seekerPassesList.get(i).replace("%seekerpasses%", seekerPasses+""));
		useSeekerPassMeta.setLore(seekerPassesList);
		if (Main.getInstance().getConfig().getBoolean("Config." + path + ".useSeekerPass.enchantedGlow")) {
			useSeekerPassMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			useSeekerPassMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		useSeekerPass.setItemMeta(useSeekerPassMeta);
		useSeekerPass.setAmount(Main.getInstance().getConfig().getInt("Config." + path + ".useSeekerPass.amount"));
		inv.setItem(Main.getInstance().getConfig().getInt("Config." + path + ".useSeekerPass.slot"), useSeekerPass);
		
		
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
