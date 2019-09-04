package at.mario.piratecraft.inventories;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.manager.ConfigManagers.DataManager;
import at.mario.piratecraft.manager.ConfigManagers.MessagesManager;

public class ShopInventory {

	public ShopInventory() { }
	
	private static ShopInventory instance = new ShopInventory();
	
	public static ShopInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages.gui.shop.title"));

		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);
		
		int joinmes = dm.getData().getInt("Data." + p.getName() + ".joinmes");
		ItemStack buyJoinmePass = new ItemStack(Material.PAPER);
		ItemMeta buyJoinmePassMeta = buyJoinmePass.getItemMeta();
		buyJoinmePassMeta.setDisplayName(mm.getMessages().getString("Messages.gui.shop.joinme.name").replace("%joinmes%", joinmes+""));
		List<String> joinmesList = mm.getMessages().getStringList("Messages.gui.shop.joinme.lore");
		for (int i = 0; i < joinmesList.size(); i++) {
			String string = joinmesList.get(i);
			joinmesList.set(i, string.replace("%joinmes%", joinmes+""));
		}
		buyJoinmePassMeta.setLore(joinmesList);
		buyJoinmePass.setItemMeta(buyJoinmePassMeta);
		
		
		inv.setItem(13, buyJoinmePass);
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR) {
				inv.setItem(i, nothing);
			}
		}
		
		p.openInventory(inv);
	}
}
