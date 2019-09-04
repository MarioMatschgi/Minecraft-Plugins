package at.mario.hidenseek.inventories;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;

public class SetupInventory {

	public SetupInventory() { }
	
	private static SetupInventory instance = new SetupInventory();
	
	public static SetupInventory getInstance() {
		return instance;
	}
	
	public void newInventory(String arena, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		Location ploc = p.getLocation();
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 54, mm.getMessages().getString("Messages.gui.setup.title").replace("%arena%", arena));

		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);
		
		ItemStack info = new ItemStack(Material.SIGN);
		ItemMeta infoMeta = info.getItemMeta();
		infoMeta.setDisplayName(mm.getMessages().getString("Messages.gui.setup.info"));
		ArrayList<String> infolist = new ArrayList<String>();
		infolist.add(arena);
		infoMeta.setLore(infolist);
		info.setItemMeta(infoMeta);
		
		ItemStack setLobbySpawn = new ItemStack(Material.BEACON);
		ItemMeta setLobbySpawnMeta = setLobbySpawn.getItemMeta();
		setLobbySpawnMeta.setDisplayName(mm.getMessages().getString("Messages.gui.setup.setLobbySpawn"));
		ArrayList<String> setLobbySpawnlist = new ArrayList<String>();
		setLobbySpawnlist.add("§b" + ploc.getWorld().getName());
		setLobbySpawnlist.add(" ");
		setLobbySpawnlist.add("§3X§f: §6" + (int) ploc.getX() + " §3Y§f: §6" + (int) ploc.getY() + " §3Z§f: §6" + (int) ploc.getZ());
		setLobbySpawnlist.add(" ");
		setLobbySpawnlist.add("§bYaw§f: §6" + (int) ploc.getYaw());
		setLobbySpawnlist.add("§bPitch§f: §6" + (int) ploc.getPitch());
		setLobbySpawnMeta.setLore(setLobbySpawnlist);
		setLobbySpawn.setItemMeta(setLobbySpawnMeta);
		
		ItemStack setSpawn = new ItemStack(Material.BED);
		ItemMeta setSpawnMeta = setSpawn.getItemMeta();
		setSpawnMeta.setDisplayName(mm.getMessages().getString("Messages.gui.setup.setSpawn"));
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
		setBound1Meta.setDisplayName(mm.getMessages().getString("Messages.gui.setup.setBound1"));
		ArrayList<String> setBound1list = new ArrayList<String>();
		setBound1list.add("§b" + ploc.getWorld().getName());
		setBound1list.add(" ");
		setBound1list.add("§3X§f: §6" + (int) ploc.getX() + " §3Y§f: §6" + (int) ploc.getY() + " §3Z§f: §6" + (int) ploc.getZ());
		setBound1list.add(" ");
		setBound1list.add("§bYaw§f: §6" + (int) ploc.getYaw());
		setBound1list.add("§bPitch§f: §6" + (int) ploc.getPitch());
		setBound1Meta.setLore(setBound1list);
		setBound1.setItemMeta(setBound1Meta);
		
		ItemStack setBound2 = new ItemStack(Material.SPRUCE_FENCE);
		ItemMeta setBound2Meta = setBound2.getItemMeta();
		setBound2Meta.setDisplayName(mm.getMessages().getString("Messages.gui.setup.setBound2"));
		ArrayList<String> setBound2list = new ArrayList<String>();
		setBound2list.add("§b" + ploc.getWorld().getName());
		setBound2list.add(" ");
		setBound2list.add("§3X§f: §6" + (int) ploc.getX() + " §3Y§f: §6" + (int) ploc.getY() + " §3Z§f: §6" + (int) ploc.getZ());
		setBound2list.add(" ");
		setBound2list.add("§bYaw§f: §6" + (int) ploc.getYaw());
		setBound2list.add("§bPitch§f: §6" + (int) ploc.getPitch());
		setBound2Meta.setLore(setBound2list);
		setBound2.setItemMeta(setBound2Meta);

		
		ItemStack minPlayerPlus = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		ItemMeta minPlayerPlusMeta = minPlayerPlus.getItemMeta();
		minPlayerPlusMeta.setDisplayName(mm.getMessages().getString("Messages.gui.setup.minPlayerPlus"));
		minPlayerPlus.setItemMeta(minPlayerPlusMeta);
		
		int minplayer = 2;
		if (dm.getData().contains("Data.arenas." + arena + ".minplayers")) {
			minplayer = dm.getData().getInt("Data.arenas." + arena + ".minplayers");
		} else {
			dm.getData().set("Data.arenas." + arena + ".minplayers", 2);
			dm.saveData();
		}
		ItemStack minPlayerInfo = new ItemStack(Material.PAPER);
		ItemMeta minPlayerInfoMeta = minPlayerInfo.getItemMeta();
		minPlayerInfo.setAmount(minplayer);
		minPlayerInfoMeta.setDisplayName(mm.getMessages().getString("Messages.gui.setup.minPlayerInfo").replace("%minplayers%", minplayer+""));
		minPlayerInfo.setItemMeta(minPlayerInfoMeta);
		
		ItemStack minPlayerNeg = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
		ItemMeta minPlayerNegMeta = minPlayerNeg.getItemMeta();
		minPlayerNegMeta.setDisplayName(mm.getMessages().getString("Messages.gui.setup.minPlayerNeg"));
		minPlayerNeg.setItemMeta(minPlayerNegMeta);

		
		ItemStack maxPlayerPlus = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		ItemMeta maxPlayerPlusMeta = maxPlayerPlus.getItemMeta();
		maxPlayerPlusMeta.setDisplayName(mm.getMessages().getString("Messages.gui.setup.maxPlayerPlus"));
		maxPlayerPlus.setItemMeta(maxPlayerPlusMeta);

		int maxplayer = 5;
		if (dm.getData().contains("Data.arenas." + arena + ".maxplayers")) {
			maxplayer = dm.getData().getInt("Data.arenas." + arena + ".maxplayers");
		} else {
			dm.getData().set("Data.arenas." + arena + ".maxplayers", 5);
			dm.saveData();
		}
		ItemStack maxPlayerInfo = new ItemStack(Material.PAPER);
		ItemMeta maxPlayerInfoMeta = maxPlayerInfo.getItemMeta();
		maxPlayerInfo.setAmount(maxplayer);
		maxPlayerInfoMeta.setDisplayName(mm.getMessages().getString("Messages.gui.setup.maxPlayerInfo").replace("%maxplayers%", maxplayer+""));
		maxPlayerInfo.setItemMeta(maxPlayerInfoMeta);
		
		ItemStack maxPlayerNeg = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
		ItemMeta maxPlayerNegMeta = maxPlayerNeg.getItemMeta();
		maxPlayerNegMeta.setDisplayName(mm.getMessages().getString("Messages.gui.setup.maxPlayerNeg"));
		maxPlayerNeg.setItemMeta(maxPlayerNegMeta);
		
		
		inv.setItem(4, info);
		inv.setItem(19, setSpawn);
		inv.setItem(25, setLobbySpawn);
		inv.setItem(37, setBound1);
		inv.setItem(43, setBound2);
		
		inv.setItem(21, minPlayerPlus);
		inv.setItem(30, minPlayerInfo);
		inv.setItem(39, minPlayerNeg);
		
		inv.setItem(23, maxPlayerPlus);
		inv.setItem(32, maxPlayerInfo);
		inv.setItem(41, maxPlayerNeg);

		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null) {
				inv.setItem(i, nothing);
			}
		}
		
		p.openInventory(inv);
	}
}
