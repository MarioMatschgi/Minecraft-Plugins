package at.mario.lobby.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class TeleportInventory {

	public TeleportInventory() { }
	
	private static TeleportInventory instance = new TeleportInventory();
	
	public static TeleportInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		Inventory inv = Main.getPlugin().getServer().createInventory(null, Main.getInstance().getConfig().getInt("Config.teleportInventorySize"), mm.getMessages().getString("Messages.gui.teleport.title"));
		
		
		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);


		if (dm.getData().get("Data.teleporter.index") == null) {
			return;
		}
		
		for (int i = 0; i <= (dm.getData().getInt("Data.teleporter.index") - 1); i++) {
			if (dm.getData().get("Data.teleporter." + i + ".item") != null) {
				if (dm.getData().contains("Data.teleporter." + i + ".slot") ) {
					ItemStack item = dm.getData().getItemStack("Data.teleporter." + i + ".item");
					item.setAmount(1);
					
					ItemStack item2 = inv.getItem(dm.getData().getInt("Data.teleporter." + i + ".slot"));
					
					if (item2 == null || item2.getItemMeta() == null || item2.getType() == Material.AIR) {
						inv.setItem(dm.getData().getInt("Data.teleporter." + i + ".slot"), item);
					} else {
						inv.setItem(dm.getData().getInt("Data.teleporter." + i + ".slot") + 1, item2);
						inv.setItem(dm.getData().getInt("Data.teleporter." + i + ".slot"), item);
					}					
				} else {
					ItemStack item = dm.getData().getItemStack("Data.teleporter." + i + ".item");
					item.setAmount(1);

					inv.setItem(i - 1, item);
				}
			}
		}
		
		for (int i = 0; i <= (inv.getSize() - 1); i++) {
			if (inv.getItem(i) == null) {
				inv.setItem(i, nothing);
			}
		}
		
		
		p.openInventory(inv);
	}
}
