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
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages.inventory.usePass.title"));

		int seekerPasses = dm.getData().getInt("Data." + p.getName() + ".seekerPasses");
		// UseSeekerPass item
		ItemStack useSeekerPass = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.inventory.usePass.useSeekerPass.material")));
		ItemMeta useSeekerPassMeta = useSeekerPass.getItemMeta();
		useSeekerPassMeta.setDisplayName(mm.getMessages().getString("Messages.inventory.usePass.useSeekerPass.name"));
		List<String> seekerPassesList = mm.getMessages().getStringList("Messages.inventory.usePass.useSeekerPass.lore");
		for (int i = 0; i < seekerPassesList.size(); i++) {
			String string = seekerPassesList.get(i);
			seekerPassesList.set(i, string.replace("%seekerpasses%", seekerPasses+""));
		}
		useSeekerPassMeta.setLore(seekerPassesList);
		if (Main.getInstance().getConfig().getBoolean("Config.inventory.usePass.useSeekerPass.enchantedGlow")) {
			useSeekerPassMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			useSeekerPassMeta.addEnchant(Enchantment.DURABILITY, 0, true);
		}
		useSeekerPass.setItemMeta(useSeekerPassMeta);
		useSeekerPass.setAmount(Main.getInstance().getConfig().getInt("Config.inventory.usePass.useSeekerPass.amount"));
		inv.setItem(Main.getInstance().getConfig().getInt("Config.inventory.usePass.useSeekerPass.slot"), useSeekerPass);
		
		
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
