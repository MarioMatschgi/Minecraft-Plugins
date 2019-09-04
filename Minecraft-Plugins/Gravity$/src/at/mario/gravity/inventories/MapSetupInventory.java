package at.mario.gravity.inventories;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.gravity.Main;
import at.mario.gravity.manager.ConfigManagers.DataManager;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;

public class MapSetupInventory {

	public MapSetupInventory() { }
	
	private static MapSetupInventory instance = new MapSetupInventory();
	
	public static MapSetupInventory getInstance() {
		return instance;
	}
	
	public void newInventory(String arena, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		Location ploc = p.getLocation();
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 36, mm.getMessages().getString("Messages.gui.mapsetup.title").replace("%map%", arena));

		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);
		
		ItemStack info = new ItemStack(Material.SIGN);
		ItemMeta infoMeta = info.getItemMeta();
		infoMeta.setDisplayName(mm.getMessages().getString("Messages.gui.mapsetup.info"));
		ArrayList<String> infolist = new ArrayList<String>();
		infolist.add(arena);
		infoMeta.setLore(infolist);
		info.setItemMeta(infoMeta);
		
		ItemStack setSpawn = new ItemStack(Material.BED);
		ItemMeta setSpawnMeta = setSpawn.getItemMeta();
		setSpawnMeta.setDisplayName(mm.getMessages().getString("Messages.gui.mapsetup.setSpawn"));
		ArrayList<String> setSpawnlist = new ArrayList<String>();
		setSpawnlist.add("§b" + ploc.getWorld().getName());
		setSpawnlist.add(" ");
		setSpawnlist.add("§3X§f: §6" + (int) ploc.getX() + " §3Y§f: §6" + (int) ploc.getY() + " §3Z§f: §6" + (int) ploc.getZ());
		setSpawnlist.add(" ");
		setSpawnlist.add("§bYaw§f: §6" + (int) ploc.getYaw());
		setSpawnlist.add("§bPitch§f: §6" + (int) ploc.getPitch());
		setSpawnMeta.setLore(setSpawnlist);
		setSpawn.setItemMeta(setSpawnMeta);
		
		ItemStack setBound1 = new ItemStack(Material.FENCE);
		ItemMeta setBound1Meta = setBound1.getItemMeta();
		setBound1Meta.setDisplayName(mm.getMessages().getString("Messages.gui.mapsetup.setGlasspos1"));
		ArrayList<String> setBound1list = new ArrayList<String>();
		setBound1list.add("§b" + ploc.getWorld().getName());
		setBound1list.add(" ");
		setBound1list.add("§3X§f: §6" + (int) ploc.getX() + " §3Y§f: §6" + (int) ploc.getY() + "-1 §3Z§f: §6" + (int) ploc.getZ());
		setBound1Meta.setLore(setBound1list);
		setBound1.setItemMeta(setBound1Meta);
		
		ItemStack setBound2 = new ItemStack(Material.SPRUCE_FENCE);
		ItemMeta setBound2Meta = setBound2.getItemMeta();
		setBound2Meta.setDisplayName(mm.getMessages().getString("Messages.gui.mapsetup.setGlasspos2"));
		ArrayList<String> setBound2list = new ArrayList<String>();
		setBound2list.add("§b" + ploc.getWorld().getName());
		setBound2list.add(" ");
		setBound2list.add("§3X§f: §6" + (int) ploc.getX() + " §3Y§f: §6" + (int) ploc.getY() + "-1 §3Z§f: §6" + (int) ploc.getZ());
		setBound2Meta.setLore(setBound2list);
		setBound2.setItemMeta(setBound2Meta);
		
		if (!dm.getData().contains("Data.maps." + arena + ".difficulty")) {
			dm.getData().set("Data.maps." + arena + ".difficulty", 1);
			dm.saveData();
		}
		int difficulty = dm.getData().getInt("Data.maps." + arena + ".difficulty");
		int difficultycolor = 0;
		if (difficulty == 1) {
			difficultycolor = 10;
		} else if (difficulty == 2) {
			difficultycolor = 14;
		} else if (difficulty == 3) {
			difficultycolor = 1;
		}
		
		ItemStack setDifficulty = new ItemStack(Material.INK_SACK, 1, (short) difficultycolor);
		ItemMeta setDifficultyMeta = setDifficulty.getItemMeta();
		setDifficultyMeta.setDisplayName(mm.getMessages().getString("Messages.gui.mapsetup.setDifficulty"));
		ArrayList<String> setDifficultylist = new ArrayList<String>();
		for (int i = 0; i < mm.getMessages().getStringList("Messages.gui.mapsetup.setDifficultyLore").size(); i++) {
			String difficultyStr = "";
			if (difficulty == 1) {
				difficultyStr = mm.getMessages().getString("Messages.gui.mapsetup.difficulties.easy");
			} else if (difficulty == 2) {
				difficultyStr = mm.getMessages().getString("Messages.gui.mapsetup.difficulties.medium");
			} else if (difficulty == 3) {
				difficultyStr = mm.getMessages().getString("Messages.gui.mapsetup.difficulties.hard");
			}
			String str = mm.getMessages().getStringList("Messages.gui.mapsetup.setDifficultyLore").get(i).replace("%diffyculty%", difficultyStr);
			setDifficultylist.add(str);
		}
		setDifficultyMeta.setLore(setDifficultylist);
		setDifficulty.setItemMeta(setDifficultyMeta);
		
		
		inv.setItem(4, info);
		inv.setItem(19, setBound1);
		inv.setItem(21, setSpawn);
		inv.setItem(23, setDifficulty);
		inv.setItem(25, setBound2);

		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null) {
				inv.setItem(i, nothing);
			}
		}
		
		p.openInventory(inv);
	}
}
