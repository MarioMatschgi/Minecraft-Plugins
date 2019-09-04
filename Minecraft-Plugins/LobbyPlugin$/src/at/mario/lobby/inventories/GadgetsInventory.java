package at.mario.lobby.inventories;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class GadgetsInventory {

	public GadgetsInventory() { }
	
	private static GadgetsInventory instance = new GadgetsInventory();
	
	public static GadgetsInventory getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public void newInventory(Player p) {
		p.updateInventory();
		
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 54, mm.getMessages().getString("Messages.gui.gadgets.title"));

		
		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);
		
		ItemStack doubleJump = new ItemStack(Material.FEATHER);
		ItemMeta doubleJumpMeta = doubleJump.getItemMeta();
		doubleJumpMeta.setDisplayName(mm.getMessages().getString("Messages.gui.gadgets.doubleJump"));
		ArrayList<String> doubleJumpList1 = new ArrayList<String>();
		doubleJumpList1 = (ArrayList<String>) mm.getMessages().get("Messages.gui.gadgets.doubleJumpDescription");
		ArrayList<String> doubleJumpList2 = new ArrayList<String>();
		for(int i = 0; i < doubleJumpList1 .size(); i++) {
			if(doubleJumpList1.get(i).contains("%status%")) {
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".doubleJump").equalsIgnoreCase("enabled")) {
					doubleJumpList2.add(doubleJumpList1.get(i).replace("%status%", mm.getMessages().getString("Messages.gui.gadgets.doubleJumpStatus.enabled")));
				} else {
					doubleJumpList2.add(doubleJumpList1.get(i).replace("%status%", mm.getMessages().getString("Messages.gui.gadgets.doubleJumpStatus.disabled")));
				}
			} else {
				doubleJumpList2.add(doubleJumpList1.get(i));
			}
		}
		doubleJumpMeta.setLore(doubleJumpList2);
		doubleJump.setItemMeta(doubleJumpMeta);
		
		
		inv.setItem(0, nothing);
		inv.setItem(1, nothing);
		inv.setItem(2, nothing);
		inv.setItem(3, nothing);
		inv.setItem(4, nothing);
		inv.setItem(5, nothing);
		inv.setItem(6, nothing);
		inv.setItem(7, nothing);
		inv.setItem(8, nothing);
		
		inv.setItem(9, nothing);		
		inv.setItem(10, nothing);
		inv.setItem(11, nothing);
		inv.setItem(12, nothing);
		inv.setItem(13, doubleJump);
		inv.setItem(14, nothing);
		inv.setItem(15, nothing);
		inv.setItem(16, nothing);
		inv.setItem(17, nothing);
		
		inv.setItem(18, nothing);		
		inv.setItem(19, nothing);
		inv.setItem(20, nothing);
		inv.setItem(21, nothing);
		inv.setItem(22, nothing);
		inv.setItem(23, nothing);
		inv.setItem(24, nothing);
		inv.setItem(25, nothing);
		inv.setItem(26, nothing);
		
		inv.setItem(27, nothing);		
		inv.setItem(28, nothing);
		inv.setItem(29, nothing);
		inv.setItem(30, nothing);
		inv.setItem(31, nothing);
		inv.setItem(32, nothing);
		inv.setItem(33, nothing);
		inv.setItem(34, nothing);
		inv.setItem(35, nothing);
		
		inv.setItem(36, nothing);		
		inv.setItem(37, nothing);
		inv.setItem(38, nothing);
		inv.setItem(39, nothing);
		inv.setItem(40, nothing);
		inv.setItem(41, nothing);
		inv.setItem(42, nothing);
		inv.setItem(43, nothing);
		inv.setItem(44, nothing);
		
		inv.setItem(45, nothing);		
		inv.setItem(46, nothing);
		inv.setItem(47, nothing);
		inv.setItem(48, nothing);
		inv.setItem(49, nothing);
		inv.setItem(50, nothing);
		inv.setItem(51, nothing);
		inv.setItem(52, nothing);
		inv.setItem(53, nothing);

		dm.getData().set("Data." + p.getName().toLowerCase() + ".inventory", "gadgetsInventory");
		dm.saveData();


		Inventory pInv = p.getInventory();
		for (int i = 0; i < 27; i++) {
			pInv.setItem(i + 9, nothing);
		}

		ItemStack GadgetInventory = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		ItemMeta GadgetInventoryInventoryMeta = GadgetInventory.getItemMeta();
		GadgetInventoryInventoryMeta.setDisplayName(mm.getMessages().getString("Messages.gui.main.gadget"));
		GadgetInventory.setItemMeta(GadgetInventoryInventoryMeta);
		
		ItemStack particleInventory = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		ItemMeta particleInventoryMeta = particleInventory.getItemMeta();
		particleInventoryMeta.setDisplayName(mm.getMessages().getString("Messages.gui.main.particle"));
		particleInventory.setItemMeta(particleInventoryMeta);

		ItemStack petInventory = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		ItemMeta petInventoryMeta = petInventory.getItemMeta();
		petInventoryMeta.setDisplayName(mm.getMessages().getString("Messages.gui.main.pets"));
		petInventory.setItemMeta(petInventoryMeta);

		ItemStack armorInventory = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		ItemMeta armorInventoryMeta = armorInventory.getItemMeta();
		armorInventoryMeta.setDisplayName(mm.getMessages().getString("Messages.gui.main.armor"));
		armorInventory.setItemMeta(armorInventoryMeta);

		pInv.setItem(25, GadgetInventory);
		pInv.setItem(23, petInventory);
		pInv.setItem(21, particleInventory);
		pInv.setItem(19, armorInventory);
		
		
		
		p.openInventory(inv);
		p.updateInventory();
	}
}
