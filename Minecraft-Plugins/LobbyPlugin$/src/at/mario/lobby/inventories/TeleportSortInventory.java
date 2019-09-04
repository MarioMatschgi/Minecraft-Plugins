package at.mario.lobby.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class TeleportSortInventory {

	public TeleportSortInventory() { }
	
	private static TeleportSortInventory instance = new TeleportSortInventory();
	
	public static TeleportSortInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Boolean DataConfig, Player p) {
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 63, mm.getMessages().getString("Messages.gui.teleportSort.title"));
		
		
		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);

		ItemStack cancel = new ItemStack(Material.SLIME_BALL);
		ItemMeta cancelMeta = cancel.getItemMeta();
		cancelMeta.setDisplayName(mm.getMessages().getString("Messages.gui.teleportSort.cancel"));
		cancel.setItemMeta(cancelMeta);

		ItemStack save = new ItemStack(Material.EMERALD);
		ItemMeta saveMeta = save.getItemMeta();
		saveMeta.setDisplayName(mm.getMessages().getString("Messages.gui.teleportSort.save"));
		save.setItemMeta(saveMeta);

		ItemStack slotNeg = new ItemStack(Material.STONE_BUTTON);
		ItemMeta slotNegMeta = slotNeg.getItemMeta();
		slotNegMeta.setDisplayName(mm.getMessages().getString("Messages.gui.teleportSort.slotNeg"));
		slotNeg.setItemMeta(slotNegMeta);

		ItemStack slotInfo = new ItemStack(Material.PAPER);
		if (DataConfig == true) {
			slotInfo.setAmount(dm.getData().getInt("Data.temp.slots"));
		} else {
			slotInfo.setAmount(Main.getInstance().getConfig().getInt("Config.teleportInventorySize"));
		}
		ItemMeta slotInfoMeta = slotInfo.getItemMeta();
		slotInfoMeta.setDisplayName(mm.getMessages().getString("Messages.gui.teleportSort.slotInfo"));
		slotInfo.setItemMeta(slotInfoMeta);

		ItemStack slotPos = new ItemStack(Material.WOOD_BUTTON);
		ItemMeta slotPosMeta = slotPos.getItemMeta();
		slotPosMeta.setDisplayName(mm.getMessages().getString("Messages.gui.teleportSort.slotPos"));
		slotPos.setItemMeta(slotPosMeta);


		if (DataConfig == true) {
			for (int i = 1; i < ( 63 - (dm.getData().getInt("Data.temp.slots") - 1) ); i++) {
				inv.setItem(i + (dm.getData().getInt("Data.temp.slots") - 1), nothing);
			}
		} else {
			for (int i = 1; i < ( 63 - (Main.getInstance().getConfig().getInt("Config.teleportInventorySize") - 1) ); i++) {
				inv.setItem(i + (Main.getInstance().getConfig().getInt("Config.teleportInventorySize") - 1), nothing);
			}
		}

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
		
		inv.setItem(54, null);
		inv.setItem(55, save);
		inv.setItem(56, null);
		inv.setItem(57, slotNeg);
		inv.setItem(58, slotInfo);
		inv.setItem(59, slotPos);
		inv.setItem(60, null);
		inv.setItem(61, cancel);
		inv.setItem(62, null);
		
		
		p.openInventory(inv);
	}
	/*
	int i2 = 9;
	
	for (int i = 0; i <= (dm.getData().getInt("Data.teleporter.index") - 1); i++) {
		if (dm.getData().get("Data.teleporter." + i + ".item") != null) {
			ItemStack item = dm.getData().getItemStack("Data.teleporter." + i + ".item");
			item.setAmount(1);
			
			if (i > (Main.getInstance().getConfig().getInt("Config.teleportInventorySize")) ) {
				Inventory pInv = p.getInventory();
				
				pInv.setItem(i2, item);
				i2 = i2 + 1;
				
				dm.getData().set("Data.temp.index2", i2);
				dm.getData();
			} else {
				inv.setItem(i - 1, item);
			}
		}
	}
	*/
}
