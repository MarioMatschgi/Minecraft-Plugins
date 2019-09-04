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

public class PetInventory {

	public PetInventory() { }
	
	private static PetInventory instance = new PetInventory();
	
	public static PetInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		p.updateInventory();
		
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 54, mm.getMessages().getString("Messages.gui.pet.title"));
		String buy = mm.getMessages().getString("Messages.gui.pet.buy");
		
		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);
		
		ItemStack info = new ItemStack(Material.SIGN);
		ItemMeta infoMeta = info.getItemMeta();
		infoMeta.setDisplayName("Info");
		ArrayList<String> infoList = new ArrayList<String>();
		infoList.add(mm.getMessages().getString("Messages.gui.pet.info"));
		infoMeta.setLore(infoList);
		info.setItemMeta(infoMeta);

		ItemStack notSelected = new ItemStack(Material.BARRIER);
		ItemMeta notSelectedMeta = notSelected.getItemMeta();
		notSelectedMeta.setDisplayName(mm.getMessages().getString("Messages.gui.nothingSelected"));
		notSelected.setItemMeta(notSelectedMeta);
		
		// Wolf
		ItemStack wolf = new ItemStack(Material.BONE);
		ItemMeta wolfMeta = wolf.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.wolf") == true) {
			wolfMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.wolf"));
		} else {
			wolfMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.wolf"));
			ArrayList<String> wolfList = new ArrayList<String>();
			wolfList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.wolf"));
			wolfMeta.setLore(wolfList);
		}
		wolf.setItemMeta(wolfMeta);
		
		// Schaf
		ItemStack sheep = new ItemStack(Material.WOOL);
		ItemMeta sheepMeta = sheep.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.sheep") == true) {
			sheepMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.sheep"));
		} else {
			sheepMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.sheep"));
			ArrayList<String> sheeplist = new ArrayList<String>();
			sheeplist.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.sheep"));
			sheepMeta.setLore(sheeplist);
		}
		sheep.setItemMeta(sheepMeta);
		
		// Huhn
		ItemStack chicken = new ItemStack(Material.SEEDS);
		ItemMeta chickenMeta = chicken.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.chicken") == true) {
			chickenMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.chicken"));
		} else {
			chickenMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.chicken"));
			ArrayList<String> chickenlist = new ArrayList<String>();
			chickenlist.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.chicken"));
			chickenMeta.setLore(chickenlist);
		}
		chicken.setItemMeta(chickenMeta);
		
		// Pferd
		ItemStack horse = new ItemStack(Material.SADDLE);
		ItemMeta horseMeta = horse.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.horse") == true) {
			horseMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.horse"));
		} else {
			horseMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.horse"));
			ArrayList<String> horselist = new ArrayList<String>();
			horselist.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.horse"));
			horseMeta.setLore(horselist);
		}
		horse.setItemMeta(horseMeta);
		
		// Schwein
		ItemStack pig = new ItemStack(Material.CARROT_STICK);
		ItemMeta pigMeta = pig.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.pig") == true) {
			pigMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.pig"));
		} else {
			pigMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.pig"));
			ArrayList<String> piglist = new ArrayList<String>();
			piglist.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.pig"));
			pigMeta.setLore(piglist);
		}
		pig.setItemMeta(pigMeta);
		
		// Kuh
		ItemStack cow = new ItemStack(Material.MILK_BUCKET);
		ItemMeta cowMeta = cow.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.cow") == true) {
			cowMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.cow"));
		} else {
			cowMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.cow"));
			ArrayList<String> cowlist = new ArrayList<String>();
			cowlist.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.cow"));
			cowMeta.setLore(cowlist);
		}
		cow.setItemMeta(cowMeta);
		
		// Pilzkuh
		ItemStack mooshroom = new ItemStack(Material.RED_MUSHROOM);
		ItemMeta mooshroomMeta = mooshroom.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.mooshroom") == true) {
			mooshroomMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.mooshroom"));
		} else {
			mooshroomMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.mooshroom"));
			ArrayList<String> mooshroomlist = new ArrayList<String>();
			mooshroomlist.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.mooshroom"));
			mooshroomMeta.setLore(mooshroomlist);
		}
		mooshroom.setItemMeta(mooshroomMeta);
		
		// Katze
		ItemStack cat = new ItemStack(Material.RAW_FISH);
		ItemMeta catMeta = cat.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.ocelot") == true) {
			catMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.ocelot"));
		} else {
			catMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.ocelot"));
			ArrayList<String> catlist = new ArrayList<String>();
			catlist.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.ocelot"));
			catMeta.setLore(catlist);
		}
		cat.setItemMeta(catMeta);
		
		// Hase
		ItemStack rabbit = new ItemStack(Material.RABBIT_HIDE);
		ItemMeta rabbitMeta = rabbit.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.rabbit") == true) {
			rabbitMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.rabbit"));
		} else {
			rabbitMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.rabbit"));
			ArrayList<String> rabbitlist = new ArrayList<String>();
			rabbitlist.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.rabbit"));
			rabbitMeta.setLore(rabbitlist);
		}
		rabbit.setItemMeta(rabbitMeta);
		
		// Dorfbewohner
		ItemStack villager = new ItemStack(Material.WOOD_DOOR);
		ItemMeta villagerMeta = villager.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.villager") == true) {
			villagerMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.villager"));
		} else {
			villagerMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.villager"));
			ArrayList<String> villagerlist = new ArrayList<String>();
			villagerlist.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.villager"));
			villagerMeta.setLore(villagerlist);
		}
		villager.setItemMeta(villagerMeta);
		
		// Tintenfisch
		ItemStack squid = new ItemStack(Material.INK_SACK);
		ItemMeta squidMeta = squid.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.squid") == true) {
			squidMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.squid"));
		} else {
			squidMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.squid"));
			ArrayList<String> squidlist = new ArrayList<String>();
			squidlist.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.squid"));
			squidMeta.setLore(squidlist);
		}
		squid.setItemMeta(squidMeta);
		
		// Silberfisch
		ItemStack silverfish = new ItemStack(Material.STONE);
		ItemMeta silverfishMeta = silverfish.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.silverfish") == true) {
			silverfishMeta.setDisplayName((String) mm.getMessages().get("Messages.gui.pet.silverfish"));
		} else {
			silverfishMeta.setDisplayName(buy + " " + (String) mm.getMessages().get("Messages.gui.pet.silverfish"));
			ArrayList<String> silverfishlist = new ArrayList<String>();
			silverfishlist.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.pet.price.silverfish"));
			silverfishMeta.setLore(silverfishlist);
		}
		silverfish.setItemMeta(silverfishMeta);
		
		// Sitting
		ItemStack sitting = new ItemStack(Material.SADDLE);
		ItemMeta sittingMeta = sitting.getItemMeta();
		sittingMeta.setDisplayName(mm.getMessages().getString("Messages.gui.pet.ride"));
		sitting.setItemMeta(sittingMeta);
		
		// Name
		ItemStack name = new ItemStack(Material.NAME_TAG);
		ItemMeta nameMeta = name.getItemMeta();
		nameMeta.setDisplayName(mm.getMessages().getString("Messages.gui.pet.rename"));
		name.setItemMeta(nameMeta);
		
		// Remove
		ItemStack remove = new ItemStack(Material.BARRIER);
		ItemMeta removeMeta = remove.getItemMeta();
		removeMeta.setDisplayName(mm.getMessages().getString("Messages.gui.pet.remove"));
		remove.setItemMeta(removeMeta);
		
		// Aufsetzten
		ItemStack Aufsetzten = new ItemStack(Material.LEATHER_HELMET);
		ItemMeta AufsetztenMeta = Aufsetzten.getItemMeta();
		AufsetztenMeta.setDisplayName(mm.getMessages().getString("Messages.gui.pet.hat"));
		Aufsetzten.setItemMeta(AufsetztenMeta);
		// Absetzen
		ItemStack Absetzten = new ItemStack(Material.CHAINMAIL_HELMET);
		ItemMeta AbsetztenMeta = Absetzten.getItemMeta();
		AbsetztenMeta.setDisplayName(mm.getMessages().getString("Messages.gui.pet.hat"));
		Absetzten.setItemMeta(AbsetztenMeta);

		// Baby
		ItemStack baby = new ItemStack(Material.WATCH);
		ItemMeta babyMeta = baby.getItemMeta();
		babyMeta.setDisplayName(mm.getMessages().getString("Messages.gui.pet.baby"));
		baby.setItemMeta(babyMeta);
		// Erwachsen
		ItemStack adult = new ItemStack(Material.WATCH);
		ItemMeta adultMeta = adult.getItemMeta();
		adultMeta.setDisplayName(mm.getMessages().getString("Messages.gui.pet.adult"));
		adult.setItemMeta(adultMeta);

		inv.setItem(0, nothing);
		inv.setItem(1, nothing);
		inv.setItem(2, nothing);
		inv.setItem(3, nothing);
		inv.setItem(4, info);
		inv.setItem(5, nothing);
		inv.setItem(6, nothing);
		inv.setItem(7, nothing);
		inv.setItem(8, nothing);
		
		inv.setItem(9, nothing);
		inv.setItem(10, wolf);
		inv.setItem(11, nothing);
		inv.setItem(12, sheep);
		inv.setItem(13, nothing);
		inv.setItem(14, chicken);
		inv.setItem(15, nothing);
		inv.setItem(16, horse);
		inv.setItem(17, nothing);

		inv.setItem(18, nothing);
		inv.setItem(19, pig);
		inv.setItem(20, nothing);
		inv.setItem(21, cow);
		inv.setItem(22, nothing);
		inv.setItem(23, mooshroom);
		inv.setItem(24, nothing);
		inv.setItem(25, cat);
		inv.setItem(26, nothing);

		inv.setItem(27, nothing);
		inv.setItem(28, rabbit);
		inv.setItem(29, nothing);
		inv.setItem(30, villager);
		inv.setItem(31, nothing);
		inv.setItem(32, squid);
		inv.setItem(33, nothing);
		inv.setItem(34, silverfish);
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

		if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".baby") == "true") {
			inv.setItem(45, baby);
		} else {
			inv.setItem(45, adult);
		}
		inv.setItem(46, nothing);
		inv.setItem(47, sitting);
		inv.setItem(48, nothing);
		if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "WOLF") {
			inv.setItem(49, wolf);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "SHEEP") {
			inv.setItem(49, sheep);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "CHICKEN") {
			inv.setItem(49, chicken);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "HORSE") {
			inv.setItem(49, horse);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "PIG") {
			inv.setItem(49, pig);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "COW") {
			inv.setItem(49, cow);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "MUSHROOM_COW") {
			inv.setItem(49, mooshroom);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "OCELOT") {
			inv.setItem(49, cat);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "RABBIT") {
			inv.setItem(49, rabbit);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "VILLAGER") {
			inv.setItem(49, villager);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "SQUID") {
			inv.setItem(49, squid);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".pet") == "SILVERFISH") {
			inv.setItem(49, silverfish);
		} else {
			inv.setItem(49, notSelected);
		}
		inv.setItem(50, nothing);
		inv.setItem(51, name);
		inv.setItem(52, nothing);
		if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".hat") == "false") {
			inv.setItem(53, Aufsetzten);
		} else {
			inv.setItem(53, Absetzten);
		}

		dm.getData().set("Data." + p.getName().toLowerCase() + ".inventory", "petInventory");
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
