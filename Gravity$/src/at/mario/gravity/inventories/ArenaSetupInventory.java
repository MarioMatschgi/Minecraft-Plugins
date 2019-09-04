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

public class ArenaSetupInventory {

	public ArenaSetupInventory() { }
	
	private static ArenaSetupInventory instance = new ArenaSetupInventory();
	
	public static ArenaSetupInventory getInstance() {
		return instance;
	}
	
	public void newInventory(String arena, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		Location ploc = p.getLocation();
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 54, mm.getMessages().getString("Messages.gui.arenasetup.title").replace("%arena%", arena));

		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);
		
		ItemStack info = new ItemStack(Material.SIGN);
		ItemMeta infoMeta = info.getItemMeta();
		infoMeta.setDisplayName(mm.getMessages().getString("Messages.gui.arenasetup.info"));
		ArrayList<String> infolist = new ArrayList<String>();
		infolist.add(arena);
		infoMeta.setLore(infolist);
		info.setItemMeta(infoMeta);
		
		ItemStack setLobbySpawn = new ItemStack(Material.BEACON);
		ItemMeta setLobbySpawnMeta = setLobbySpawn.getItemMeta();
		setLobbySpawnMeta.setDisplayName(mm.getMessages().getString("Messages.gui.arenasetup.setLobbySpawn"));
		ArrayList<String> setLobbySpawnlist = new ArrayList<String>();
		setLobbySpawnlist.add("§b" + ploc.getWorld().getName());
		setLobbySpawnlist.add(" ");
		setLobbySpawnlist.add("§3X§f: §6" + (int) ploc.getX() + " §3Y§f: §6" + (int) ploc.getY() + " §3Z§f: §6" + (int) ploc.getZ());
		setLobbySpawnlist.add(" ");
		setLobbySpawnlist.add("§bYaw§f: §6" + (int) ploc.getYaw());
		setLobbySpawnlist.add("§bPitch§f: §6" + (int) ploc.getPitch());
		setLobbySpawnMeta.setLore(setLobbySpawnlist);
		setLobbySpawn.setItemMeta(setLobbySpawnMeta);
		
		ItemStack minPlayerPlus = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		ItemMeta minPlayerPlusMeta = minPlayerPlus.getItemMeta();
		minPlayerPlusMeta.setDisplayName(mm.getMessages().getString("Messages.gui.arenasetup.minPlayerPlus"));
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
		minPlayerInfoMeta.setDisplayName(mm.getMessages().getString("Messages.gui.arenasetup.minPlayerInfo").replace("%minplayers%", minplayer+""));
		minPlayerInfo.setItemMeta(minPlayerInfoMeta);
		
		ItemStack minPlayerNeg = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
		ItemMeta minPlayerNegMeta = minPlayerNeg.getItemMeta();
		minPlayerNegMeta.setDisplayName(mm.getMessages().getString("Messages.gui.arenasetup.minPlayerNeg"));
		minPlayerNeg.setItemMeta(minPlayerNegMeta);

		
		ItemStack maxPlayerPlus = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		ItemMeta maxPlayerPlusMeta = maxPlayerPlus.getItemMeta();
		maxPlayerPlusMeta.setDisplayName(mm.getMessages().getString("Messages.gui.arenasetup.maxPlayerPlus"));
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
		maxPlayerInfoMeta.setDisplayName(mm.getMessages().getString("Messages.gui.arenasetup.maxPlayerInfo").replace("%maxplayers%", maxplayer+""));
		maxPlayerInfo.setItemMeta(maxPlayerInfoMeta);
		
		ItemStack maxPlayerNeg = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
		ItemMeta maxPlayerNegMeta = maxPlayerNeg.getItemMeta();
		maxPlayerNegMeta.setDisplayName(mm.getMessages().getString("Messages.gui.arenasetup.maxPlayerNeg"));
		maxPlayerNeg.setItemMeta(maxPlayerNegMeta);
		
		
		inv.setItem(4, info);
		inv.setItem(25, setLobbySpawn);
		
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
