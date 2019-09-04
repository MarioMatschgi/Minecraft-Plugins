package at.mario.hidenseek.inventories;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages.gui.passShop.title"));

		
		ItemStack buySeekerPass = new ItemStack(Material.PAPER);
		ItemMeta buySeekerPassMeta = buySeekerPass.getItemMeta();
		buySeekerPassMeta.setDisplayName(mm.getMessages().getString("Messages.gui.passShop.seeker.name"));
		List<String> seekerPassesList = mm.getMessages().getStringList("Messages.gui.passShop.seeker.lore");
		for (int i = 0; i < seekerPassesList.size(); i++) {
			String string = seekerPassesList.get(i);
			seekerPassesList.set(i, string);
		}
		buySeekerPassMeta.setLore(seekerPassesList);
		buySeekerPass.setItemMeta(buySeekerPassMeta);
		

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
