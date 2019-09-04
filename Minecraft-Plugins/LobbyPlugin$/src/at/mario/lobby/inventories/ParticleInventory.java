package at.mario.lobby.inventories;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lobby.Main;
import at.mario.lobby.Effects.ParticleEffect;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class ParticleInventory {

	public int taskID;
	public static HashMap<String, Integer> taskIDs = new HashMap<String, Integer>();
	
	public ParticleInventory() { }
	
	private static ParticleInventory instance = new ParticleInventory();
	
	public static ParticleInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		p.updateInventory();
		
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		
		String buy = mm.getMessages().getString("Messages.gui.pet.buy");
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 54, mm.getMessages().getString("Messages.gui.particle.title"));
		
		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);

		ItemStack info = new ItemStack(Material.SIGN);
		ItemMeta infoMeta = info.getItemMeta();
		infoMeta.setDisplayName("Info");
		ArrayList<String> infoList = new ArrayList<String>();
		infoList.add(mm.getMessages().getString("Messages.gui.particle.info"));
		infoMeta.setLore(infoList);
		info.setItemMeta(infoMeta);

		ItemStack notSelected = new ItemStack(Material.BARRIER);
		ItemMeta notSelectedMeta = notSelected.getItemMeta();
		notSelectedMeta.setDisplayName(mm.getMessages().getString("Messages.gui.nothingSelected"));
		notSelected.setItemMeta(notSelectedMeta);
		
		ItemStack dripLava = new ItemStack(Material.LAVA_BUCKET, 1);
		ItemMeta dripLavaMeta = dripLava.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.dripLava") == true) {
			dripLavaMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.dripLava"));
		} else {
			dripLavaMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.dripLava"));
			ArrayList<String> dripLavaList = new ArrayList<String>();
			dripLavaList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.dripLava"));
			dripLavaMeta.setLore(dripLavaList);
		}
		dripLava.setItemMeta(dripLavaMeta);

		ItemStack dripWater = new ItemStack(Material.WATER_BUCKET, 1);
		ItemMeta dripWaterMeta = dripWater.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.dripWater") == true) {
			dripWaterMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.dripWater"));
		} else {
			dripWaterMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.dripWater"));
			ArrayList<String> dripWaterList = new ArrayList<String>();
			dripWaterList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.dripWater"));
			dripWaterMeta.setLore(dripWaterList);
		}
		dripWater.setItemMeta(dripWaterMeta);

		ItemStack heart = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemMeta heartMeta = heart.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.heart") == true) {
			heartMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.heart"));
		} else {
			heartMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.heart"));
			ArrayList<String> dripWaterList = new ArrayList<String>();
			dripWaterList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.heart"));
			heartMeta.setLore(dripWaterList);
		}
		heart.setItemMeta(heartMeta);

		ItemStack lava = new ItemStack(Material.FIREBALL, 1);
		ItemMeta lavaMeta = lava.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.lava") == true) {
			lavaMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.lava"));
		} else {
			lavaMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.lava"));
			ArrayList<String> lavaList = new ArrayList<String>();
			lavaList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.lava"));
			lavaMeta.setLore(lavaList);
		}
		lava.setItemMeta(lavaMeta);

		ItemStack note = new ItemStack(Material.NOTE_BLOCK, 1);
		ItemMeta noteMeta = note.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.note") == true) {
			noteMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.note"));
		} else {
			noteMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.note"));
			ArrayList<String> dripLavaList = new ArrayList<String>();
			dripLavaList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.note"));
			noteMeta.setLore(dripLavaList);
		}
		note.setItemMeta(noteMeta);

		ItemStack redstone = new ItemStack(Material.REDSTONE, 1);
		ItemMeta redstoneMeta = redstone.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.redstone") == true) {
			redstoneMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.redstone"));
		} else {
			redstoneMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.redstone"));
			ArrayList<String> redstoneList = new ArrayList<String>();
			redstoneList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.redstone"));
			redstoneMeta.setLore(redstoneList);
		}
		redstone.setItemMeta(redstoneMeta);

		ItemStack slime = new ItemStack(Material.SLIME_BALL, 1);
		ItemMeta slimeMeta = slime.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.slime") == true) {
			slimeMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.slime"));
		} else {
			slimeMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.slime"));
			ArrayList<String> slimeList = new ArrayList<String>();
			slimeList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.slime"));
			slimeMeta.setLore(slimeList);
		}
		slime.setItemMeta(slimeMeta);

		ItemStack snowball = new ItemStack(Material.SNOW_BALL, 1);
		ItemMeta snowballMeta = snowball.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.snowball") == true) {
			snowballMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.snowball"));
		} else {
			snowballMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.snowball"));
			ArrayList<String> snowballList = new ArrayList<String>();
			snowballList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.snowball"));
			snowballMeta.setLore(snowballList);
		}
		snowball.setItemMeta(snowballMeta);

		ItemStack spell = new ItemStack(Material.STICK, 1);
		ItemMeta spellMeta = spell.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.spell") == true) {
			spellMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.spell"));
		} else {
			spellMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.spell"));
			ArrayList<String> spellList = new ArrayList<String>();
			spellList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.spell"));
			spellMeta.setLore(spellList);
		}
		spell.setItemMeta(spellMeta);

		ItemStack townaura = new ItemStack(Material.WOOL, 1, (short) 15);
		ItemMeta townauraMeta = townaura.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.townaura") == true) {
			townauraMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.townaura"));
		} else {
			townauraMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.townaura"));
			ArrayList<String> townauraList = new ArrayList<String>();
			townauraList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.townaura"));
			townauraMeta.setLore(townauraList);
		}
		townaura.setItemMeta(townauraMeta);

		ItemStack villagerAngry = new ItemStack(Material.MONSTER_EGG, 1, (short) 120);
		ItemMeta villagerAngryMeta = villagerAngry.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.villagerAngry") == true) {
			villagerAngryMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.villagerAngry"));
		} else {
			villagerAngryMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.villagerAngry"));
			ArrayList<String> villagerAngryList = new ArrayList<String>();
			villagerAngryList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.villagerAngry"));
			villagerAngryMeta.setLore(villagerAngryList);
		}
		villagerAngry.setItemMeta(villagerAngryMeta);

		ItemStack villagerHappy = new ItemStack(Material.WOOL, 1, (short) 5);
		ItemMeta villagerHappyMeta = villagerHappy.getItemMeta();
		if (dm.getData().getBoolean("Data." + p.getName().toLowerCase() + ".bought.villagerHappy") == true) {
			villagerHappyMeta.setDisplayName(mm.getMessages().getString("Messages.gui.particle.villagerHappy"));
		} else {
			villagerHappyMeta.setDisplayName(buy + " " + mm.getMessages().getString("Messages.gui.particle.villagerHappy"));
			ArrayList<String> villagerHappyList = new ArrayList<String>();
			villagerHappyList.add("§ePreis §5" + Main.getInstance().getConfig().getString("Config.particle.price.villagerHappy"));
			villagerHappyMeta.setLore(villagerHappyList);
		}
		villagerHappy.setItemMeta(villagerHappyMeta);
		
		
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
		inv.setItem(10, dripLava);
		inv.setItem(11, nothing);
		inv.setItem(12, dripWater);
		inv.setItem(13, nothing);
		inv.setItem(14, villagerHappy);
		inv.setItem(15, nothing);
		inv.setItem(16, lava);
		inv.setItem(17, nothing);

		inv.setItem(18, nothing);
		inv.setItem(19, heart);
		inv.setItem(20, nothing);
		inv.setItem(21, note);
		inv.setItem(22, nothing);
		inv.setItem(23, redstone);
		inv.setItem(24, nothing);
		inv.setItem(25, slime);
		inv.setItem(26, nothing);

		inv.setItem(27, nothing);
		inv.setItem(28, snowball);
		inv.setItem(29, nothing);
		inv.setItem(30, spell);
		inv.setItem(31, nothing);
		inv.setItem(32, townaura);
		inv.setItem(33, nothing);
		inv.setItem(34, villagerAngry);
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

		if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "dripLava") {
			inv.setItem(49, dripLava);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "dripWater") {
			inv.setItem(49, dripWater);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "villagerHappy") {
			inv.setItem(49, villagerHappy);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "lava") {
			inv.setItem(49, lava);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "heart") {
			inv.setItem(49, heart);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "note") {
			inv.setItem(49, note);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "redstone") {
			inv.setItem(49, redstone);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "slime") {
			inv.setItem(49, slime);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "snowball") {
			inv.setItem(49, snowball);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "spell") {
			inv.setItem(49, spell);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "townaura") {
			inv.setItem(49, townaura);
		} else if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "villagerAngry") {
			inv.setItem(49, villagerAngry);
		} else {
			inv.setItem(49, notSelected);
		}
		
		inv.setItem(50, nothing);
		inv.setItem(51, nothing);
		inv.setItem(52, nothing);
		inv.setItem(53, nothing);


		dm.getData().set("Data." + p.getName().toLowerCase() + ".inventory", "particleInventory");
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
	
	public void summonParticle(ParticleEffect effect, Player p) {
		DataManager dm = new DataManager();
		// PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particleType, true, x, y, z, offsetX, offsetY, offsetZ, speed, amount);

		taskIDs.put(p.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				
				if (p.isOnGround() || p.isFlying()) {
					particles(effect, p);
				}
				if (dm.getData().getString("Data." + p.getName().toLowerCase() + ".particle") == "null" || p.isOnline() == false) {
					// dm.getData().set("Data." + p.getName().toLowerCase() + ".particle", "null");
					// dm.saveData();
					Bukkit.getScheduler().cancelTask(taskIDs.get(p.getName()));
				}
			}
		}, 0, 2) );
		
        
	}
	
	private void particles(ParticleEffect effect, Player p) {
		// ParticleEffect.HEART.display((float) 0.2, 0, (float) 0.2, 10, 10, loc, 15);
		
		Location loc = p.getLocation().subtract(0.0, 0.4, 0.0);
		
		if (effect == ParticleEffect.DRIP_LAVA) {
			loc = loc.add(0.0, 0.45, 0.0);
			ParticleEffect.DRIP_LAVA.display((float) 0.2, 0, (float) 0.2, 10, 100, loc, 15);
		} else if (effect == ParticleEffect.DRIP_WATER) {
			loc = loc.add(0.0, 0.45, 0.0);
			ParticleEffect.DRIP_WATER.display((float) 0.2, 0, (float) 0.2, 10, 100, loc, 15);
		} else if (effect == ParticleEffect.HEART) {
			ParticleEffect.HEART.display((float) 0.2, 0, (float) 0.2, 10, 10, loc, 15);
		} else if (effect == ParticleEffect.LAVA) {
			ParticleEffect.LAVA.display((float) 0.2, 0, (float) 0.2, 10, 10, loc, 15);
		} else if (effect == ParticleEffect.NOTE) {
			ParticleEffect.NOTE.display((float) 0.3, 0, (float) 0.3, 5, 20, loc, 15);
		} else if (effect == ParticleEffect.REDSTONE) {
			loc = loc.add(0.0, 0.5, 0.0);
			ParticleEffect.REDSTONE.display((float) 0.2, 0, (float) 0.2, 10, 20, loc, 15);
		} else if (effect == ParticleEffect.SLIME) {
			loc = loc.add(0.0, 0.5, 0.0);
			ParticleEffect.SLIME.display((float) 0.2, 0, (float) 0.2, 10, 30, loc, 15);
		} else if (effect == ParticleEffect.SNOWBALL) {
			loc = loc.add(0.0, 0.5, 0.0);
			ParticleEffect.SNOWBALL.display((float) 0.2, 0, (float) 0.2, 10, 20, loc, 15);
		} else if (effect == ParticleEffect.SPELL) {
			loc = loc.add(0.0, 0.5, 0.0);
			ParticleEffect.SPELL.display((float) 0.2, 0, (float) 0.2, 10, 10, loc, 15);
		} else if (effect == ParticleEffect.TOWN_AURA) {
			loc = loc.add(0.0, 0.5, 0.0);
			ParticleEffect.TOWN_AURA.display((float) 0.3, 0, (float) 0.3, 10, 50, loc, 15);
		} else if (effect == ParticleEffect.VILLAGER_ANGRY) {
			loc = loc.subtract(0.0, 0.2, 0.0);
			ParticleEffect.VILLAGER_ANGRY.display((float) 0.2, 0, (float) 0.2, 10, 7, loc, 15);
		} else if (effect == ParticleEffect.VILLAGER_HAPPY) {
			loc = loc.add(0.0, 0.5, 0.0);
			ParticleEffect.VILLAGER_HAPPY.display((float) 0.2, 0, (float) 0.2, 10, 10, loc, 15);
		} else {
			
		}
	}
}
