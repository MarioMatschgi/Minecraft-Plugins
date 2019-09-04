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

public class PassInventory {

	public PassInventory() { }
	
	private static PassInventory instance = new PassInventory();
	
	public static PassInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages.gui.pass.title"));

		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);

		int seekerPasses = dm.getData().getInt("Data." + p.getName() + ".seekerPasses");
		ItemStack buySeekerPass = new ItemStack(Material.PAPER);
		ItemMeta buySeekerPassMeta = buySeekerPass.getItemMeta();
		buySeekerPassMeta.setDisplayName(mm.getMessages().getString("Messages.gui.pass.usePass.name"));
		List<String> seekerPassesList = mm.getMessages().getStringList("Messages.gui.pass.usePass.lore");
		for (int i = 0; i < seekerPassesList.size(); i++) {
			String string = seekerPassesList.get(i);
			seekerPassesList.set(i, string.replace("%seekerpasses%", seekerPasses+""));
		}
		buySeekerPassMeta.setLore(seekerPassesList);
		buySeekerPass.setItemMeta(buySeekerPassMeta);
		
		
		inv.setItem(13, buySeekerPass);
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR) {
				inv.setItem(i, nothing);
			}
		}
		
		p.openInventory(inv);
	}
}
