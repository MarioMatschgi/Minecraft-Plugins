package at.mario.lobby.inventories;

import org.bukkit.entity.Player;

import at.mario.lobby.manager.ConfigManagers.DataManager;

public class MainInventory {

	public MainInventory() { }
	
	private static MainInventory instance = new MainInventory();
	
	public static MainInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		// MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		// ConfigManager cm = new ConfigManager();
		
		if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".inventory")) {
			if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".inventory").equalsIgnoreCase("armorInventory")) {
				ArmorInventory.getInstance().newInventory(p);
			} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".inventory").equalsIgnoreCase("particleInventory")) {
				ParticleInventory.getInstance().newInventory(p);
			} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".inventory").equalsIgnoreCase("petInventory")) {
				PetInventory.getInstance().newInventory(p);
			} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".inventory").equalsIgnoreCase("gadgetsInventory")) {
				GadgetsInventory.getInstance().newInventory(p);
			} else {
				ParticleInventory.getInstance().newInventory(p);
			}
		} else {
			ParticleInventory.getInstance().newInventory(p);
			
			dm.getData().set("Data." + p.getName().toLowerCase() + ".inventory", "particleInventory");
			dm.saveData();
		}
	}
}
/*
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 45, mm.getMessages().getString("Messages.gui.main.title"));
		
		ItemStack pets = new ItemStack(Material.BONE);
		ItemMeta petsMeta = pets.getItemMeta();
		petsMeta.setDisplayName((String) mm.getMessages().getString("Messages.gui.main.pets"));
		pets.setItemMeta(petsMeta);
		
		ItemStack particle = new ItemStack(Material.BLAZE_POWDER);
		ItemMeta particleMeta = particle.getItemMeta();
		particleMeta.setDisplayName((String) mm.getMessages().getString("Messages.gui.main.particle"));
		particle.setItemMeta(particleMeta);
		
		ItemStack visibility = new ItemStack(Material.INK_SACK, 1 , (short) 10);
		ItemMeta visibilityMeta = visibility.getItemMeta();
		visibilityMeta.setDisplayName((String) mm.getMessages().getString("Messages.gui.main.visibility"));
		visibility.setItemMeta(visibilityMeta);
		
		ItemStack armor = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemMeta armorMeta = armor.getItemMeta();
		armorMeta.setDisplayName((String) mm.getMessages().getString("Messages.gui.main.armor"));
		// armorMeta.setDisplayName((String) mm.getMessages().getString("Messages.gui.main.visibility"));
		armor.setItemMeta(armorMeta);
		
		inv.setItem(6, pets);
		inv.setItem(22, particle);
		inv.setItem(40, visibility);
		inv.setItem(2, armor);
		

		p.openInventory(inv);
*/