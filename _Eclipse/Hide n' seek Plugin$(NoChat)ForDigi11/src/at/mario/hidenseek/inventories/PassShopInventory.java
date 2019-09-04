package at.mario.hidenseek.inventories;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 27, mm.getMessages().getString("Messages.gui.passShop.title"));
		
		
		int joinmes = dm.getData().getInt("Data." + p.getName() + ".joinmes");
		ItemStack buyJoinmePass = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.inventory.pass.buySeekerPassItem.material")));
		ItemMeta buyJoinmePassMeta = buyJoinmePass.getItemMeta();
		buyJoinmePassMeta.setDisplayName(mm.getMessages().getString("Messages.gui.passShop.joinme.name").replace("%joinmes%", joinmes+""));
		List<String> joinmesList = mm.getMessages().getStringList("Messages.gui.passShop.joinme.lore");
		for (int i = 0; i < joinmesList.size(); i++) {
			String string = joinmesList.get(i);
			joinmesList.set(i, string.replace("%joinmes%", joinmes+""));
		}
		buyJoinmePassMeta.setLore(joinmesList);
		buyJoinmePass.setItemMeta(buyJoinmePassMeta);

		int seekerPasses = (Main.getInstance().getConfig().getBoolean("Config.unlimitedSeekerPassesWithPermission") && (p.hasPermission("hs.admin") || p.hasPermission("hs.arena.infiniteSeekerPasses") || p.isOp()) )
				? 99999 : dm.getData().getInt("Data." + p.getName() + ".seekerPasses");
		ItemStack buySeekerPass = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("Config.inventory.pass.buyPowerupsMenueItem.material")));
		ItemMeta buySeekerPassMeta = buySeekerPass.getItemMeta();
		buySeekerPassMeta.setDisplayName(mm.getMessages().getString("Messages.gui.passShop.seeker.name"));
		List<String> seekerPassesList = mm.getMessages().getStringList("Messages.gui.passShop.seeker.lore");
		for (int i = 0; i < seekerPassesList.size(); i++) {
			String string = seekerPassesList.get(i);
			seekerPassesList.set(i, string.replace("%seekerpasses%", seekerPasses+""));
		}
		buySeekerPassMeta.setLore(seekerPassesList);
		buySeekerPass.setItemMeta(buySeekerPassMeta);
		
		
		inv.setItem(11, buyJoinmePass);
		inv.setItem(15, buySeekerPass);

		
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
