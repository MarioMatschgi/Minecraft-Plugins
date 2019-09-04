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

public class ArmorInventory {

	public ArmorInventory() { }
	
	private static ArmorInventory instance = new ArmorInventory();
	
	public static ArmorInventory getInstance() {
		return instance;
	}

	public void newInventory(Player p) {
		p.updateInventory();
		
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		// ConfigManager cm = new ConfigManager();
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 54, mm.getMessages().getString("Messages.gui.armor.title"));
		String buy = mm.getMessages().getString("Messages.gui.pet.buy");

		
		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);

		ItemStack removeAll = new ItemStack(Material.BARRIER);
		ItemMeta removeAllMeta = removeAll.getItemMeta();
		removeAllMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.removeAll"));
		removeAll.setItemMeta(removeAllMeta);
		
		
		ItemStack Diamondhelmet = new ItemStack(Material.DIAMOND_HELMET, 1);
		ItemMeta DiamondhelmetMeta = Diamondhelmet.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.helmet") == true) {
			DiamondhelmetMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
		} else {
			DiamondhelmetMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.diamond"));
			DiamondhelmetMeta.setLore(Lore);
		}
		Diamondhelmet.setItemMeta(DiamondhelmetMeta);
		
		ItemStack Diamondchestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		ItemMeta DiamondchestplateMeta = Diamondchestplate.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.chestplate") == true) {
			DiamondchestplateMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
		} else {
			DiamondchestplateMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.diamond"));
			DiamondchestplateMeta.setLore(Lore);
		}
		Diamondchestplate.setItemMeta(DiamondchestplateMeta);
		
		ItemStack Diamondleggins = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		ItemMeta DiamondlegginsMeta = Diamondleggins.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.leggins") == true) {
			DiamondlegginsMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
		} else {
			DiamondlegginsMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.diamond"));
			DiamondlegginsMeta.setLore(Lore);
		}
		Diamondleggins.setItemMeta(DiamondlegginsMeta);
		
		ItemStack Diamondboots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		ItemMeta DiamondbootsMeta = Diamondboots.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.diamond.boots") == true) {
			DiamondbootsMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
		} else {
			DiamondbootsMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.diamond") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.diamond"));
			DiamondbootsMeta.setLore(Lore);
		}
		Diamondboots.setItemMeta(DiamondbootsMeta);
		
		
		ItemStack Goldhelmet = new ItemStack(Material.GOLD_HELMET, 1);
		ItemMeta GoldhelmetMeta = Goldhelmet.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.gold.helmet") == true) {
			GoldhelmetMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
		} else {
			GoldhelmetMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.gold"));
			GoldhelmetMeta.setLore(Lore);
		}
		Goldhelmet.setItemMeta(GoldhelmetMeta);
		
		ItemStack Goldchestplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);
		ItemMeta GoldchestplateMeta = Goldchestplate.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.gold.chestplate") == true) {
			GoldchestplateMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
		} else {
			GoldchestplateMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.gold"));
			GoldchestplateMeta.setLore(Lore);
		}
		Goldchestplate.setItemMeta(GoldchestplateMeta);
		
		ItemStack Goldleggins = new ItemStack(Material.GOLD_LEGGINGS, 1);
		ItemMeta GoldlegginsMeta = Goldleggins.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.gold.leggins") == true) {
			GoldlegginsMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
		} else {
			GoldlegginsMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.gold"));
			GoldlegginsMeta.setLore(Lore);
		}
		Goldleggins.setItemMeta(GoldlegginsMeta);
		
		ItemStack Goldboots = new ItemStack(Material.GOLD_BOOTS, 1);
		ItemMeta GoldbootsMeta = Goldboots.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.gold.boots") == true) {
			GoldbootsMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
		} else {
			GoldbootsMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.gold") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.gold"));
			GoldbootsMeta.setLore(Lore);
		}
		Goldboots.setItemMeta(GoldbootsMeta);
		
		
		ItemStack Ironhelmet = new ItemStack(Material.IRON_HELMET, 1);
		ItemMeta IronhelmetMeta = Ironhelmet.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.iron.helmet") == true) {
			IronhelmetMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
		} else {
			IronhelmetMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.iron"));
			IronhelmetMeta.setLore(Lore);
		}
		Ironhelmet.setItemMeta(IronhelmetMeta);
		
		ItemStack Ironchestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
		ItemMeta IronchestplateMeta = Ironchestplate.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.iron.chestplate") == true) {
			IronchestplateMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
		} else {
			IronchestplateMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.iron"));
			IronchestplateMeta.setLore(Lore);
		}
		Ironchestplate.setItemMeta(IronchestplateMeta);
		
		ItemStack Ironleggins = new ItemStack(Material.IRON_LEGGINGS, 1);
		ItemMeta IronlegginsMeta = Ironleggins.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.iron.leggins") == true) {
			IronlegginsMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
		} else {
			IronlegginsMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.iron"));
			IronlegginsMeta.setLore(Lore);
		}
		Ironleggins.setItemMeta(IronlegginsMeta);
		
		ItemStack Ironboots = new ItemStack(Material.IRON_BOOTS, 1);
		ItemMeta IronbootsMeta = Ironboots.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.iron.boots") == true) {
			IronbootsMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
		} else {
			IronbootsMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.iron") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.iron"));
			IronbootsMeta.setLore(Lore);
		}
		Ironboots.setItemMeta(IronbootsMeta);
		
		
		ItemStack Leatherhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
		ItemMeta LeatherhelmetMeta = Leatherhelmet.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.leather.helmet") == true) {
			LeatherhelmetMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
		} else {
			LeatherhelmetMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.helmet"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.leather"));
			LeatherhelmetMeta.setLore(Lore);
		}
		Leatherhelmet.setItemMeta(LeatherhelmetMeta);
		
		ItemStack Leatherchestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		ItemMeta LeatherchestplateMeta = Leatherchestplate.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.leather.chestplate") == true) {
			LeatherchestplateMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
		} else {
			LeatherchestplateMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.chestplate"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.leather"));
			LeatherchestplateMeta.setLore(Lore);
		}
		Leatherchestplate.setItemMeta(LeatherchestplateMeta);
		
		ItemStack Leatherleggins = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		ItemMeta LeatherlegginsMeta = Leatherleggins.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.leather.leggins") == true) {
			LeatherlegginsMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
		} else {
			LeatherlegginsMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.leggins"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.leather"));
			LeatherlegginsMeta.setLore(Lore);
		}
		Leatherleggins.setItemMeta(LeatherlegginsMeta);
		
		ItemStack Leatherboots = new ItemStack(Material.LEATHER_BOOTS, 1);
		ItemMeta LeatherbootsMeta = Leatherboots.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.armor.leather.boots") == true) {
			LeatherbootsMeta.setDisplayName(mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
		} else {
			LeatherbootsMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.armor.leather") + " " + mm.getMessages().getString("Messages.gui.armor.boots"));
			ArrayList<String> Lore = new ArrayList<String>();
			Lore.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.armor.price.leather"));
			LeatherbootsMeta.setLore(Lore);
		}
		Leatherboots.setItemMeta(LeatherbootsMeta);
		
		// Colored Leather
		// TODO
		// 
		
		
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
		inv.setItem(11, Diamondhelmet);
		inv.setItem(12, Goldhelmet);
		inv.setItem(13, Ironhelmet);
		inv.setItem(14, Leatherhelmet);
		inv.setItem(15, nothing);
		inv.setItem(16, nothing);
		inv.setItem(17, nothing);
		
		inv.setItem(18, nothing);		
		inv.setItem(19, nothing);
		inv.setItem(20, Diamondchestplate);
		inv.setItem(21, Goldchestplate);
		inv.setItem(22, Ironchestplate);
		inv.setItem(23, Leatherchestplate);
		inv.setItem(24, nothing);
		inv.setItem(25, nothing);
		inv.setItem(26, nothing);
		
		inv.setItem(27, nothing);		
		inv.setItem(28, nothing);
		inv.setItem(29, Diamondleggins);
		inv.setItem(30, Goldleggins);
		inv.setItem(31, Ironleggins);
		inv.setItem(32, Leatherleggins);
		inv.setItem(33, nothing);
		inv.setItem(34, nothing);
		inv.setItem(35, nothing);
		
		inv.setItem(36, nothing);		
		inv.setItem(37, nothing);
		inv.setItem(38, Diamondboots);
		inv.setItem(39, Goldboots);
		inv.setItem(40, Ironboots);
		inv.setItem(41, Leatherboots);
		inv.setItem(42, nothing);
		inv.setItem(43, nothing);
		inv.setItem(44, nothing);
		
		inv.setItem(45, nothing);		
		inv.setItem(46, nothing);
		inv.setItem(47, nothing);
		inv.setItem(48, nothing);
		inv.setItem(49, removeAll);
		inv.setItem(50, nothing);
		inv.setItem(51, nothing);
		inv.setItem(52, nothing);
		inv.setItem(53, nothing);

		dm.getData().set("Data." + p.getName().toLowerCase() + ".inventory", "armorInventory");
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
