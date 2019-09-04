package at.mario.lobby.inventories;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class DailyRewardInventory {

	public DailyRewardInventory() { }
	
	private static DailyRewardInventory instance = new DailyRewardInventory();
	
	public static DailyRewardInventory getInstance() {
		return instance;
	}
	
	public void newInventory(Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 36, mm.getMessages().getString("Messages.gui.DailyReward.title"));

		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String date = format.format(now);
		int day = Integer.parseInt(date.substring(0, 2));
		int month = Integer.parseInt(date.substring(3, 5));
		int year = Integer.parseInt(date.substring(6, 10));
		
		
		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);
		
		ItemStack info = new ItemStack(Material.SIGN);
		ItemMeta infoMeta = info.getItemMeta();
		infoMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.info"));
		info.setItemMeta(infoMeta);

		ItemStack rewardfree = new ItemStack(Material.CHEST);
		ItemMeta rewardfreeMeta = rewardfree.getItemMeta();
		
		if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".lastDailyreward")) {
			int pDay = dm.getData().getInt("Data." + p.getName().toLowerCase() + ".lastDailyreward.day");
			int pMonth = dm.getData().getInt("Data." + p.getName().toLowerCase() + ".lastDailyreward.month");
			int pYear = dm.getData().getInt("Data." + p.getName().toLowerCase() + ".lastDailyreward.year");
			
			if (year == pYear && month == pMonth && day == pDay) {
				String timeLeft = "1 " + mm.getMessages().getString("Messages.gui.DailyReward.days");
				rewardfreeMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.recentGot").replace("%date%", timeLeft+""));
			} else {
				rewardfreeMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.collectReward").replace("%type%", ""));
			}
		} else {
			rewardfreeMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.collectReward").replace("%type%", ""));
		}
		rewardfree.setItemMeta(rewardfreeMeta);
		
		ItemStack rewardVIP = new ItemStack(Material.ENDER_CHEST);
		ItemMeta rewardVIPMeta = rewardVIP.getItemMeta();
		
		if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".lastVIPDailyreward")) {
			int pDay = dm.getData().getInt("Data." + p.getName().toLowerCase() + ".lastVIPDailyreward.day");
			int pMonth = dm.getData().getInt("Data." + p.getName().toLowerCase() + ".lastVIPDailyreward.month");
			int pYear = dm.getData().getInt("Data." + p.getName().toLowerCase() + ".lastVIPDailyreward.year");
			
			int timeLeftint = 0;
			
			if (year == pYear && month == pMonth) {
				timeLeftint = day - pDay;
				if (timeLeftint < 0) {
					timeLeftint = timeLeftint + -1;
				}
				timeLeftint = 30 - timeLeftint;
				String timeLeft = timeLeftint + " " + mm.getMessages().getString("Messages.gui.DailyReward.days");
				rewardVIPMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.recentGot").replace("%date%", timeLeft+""));
			} else {
				rewardVIPMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.collectReward").replace("%type%", "VIP"));
			}
		} else {
			rewardVIPMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.collectReward").replace("%type%", "VIP"));
		}

		if (!(p.hasPermission("lobby.vip") || p.hasPermission("lobby.mvip") || p.hasPermission("lobby.vipplus") || p.hasPermission("lobby.builder") || p.hasPermission("lobby.admin") || p.isOp()) ) {
			rewardVIPMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.unlockRewardVIP"));
		}
		rewardVIP.setItemMeta(rewardVIPMeta);
		
		ItemStack rewardVIPPlus = new ItemStack(Material.ENDER_CHEST);
		ItemMeta rewardVIPPlusMeta = rewardVIPPlus.getItemMeta();
		
		if (dm.getData().contains("Data." + p.getName().toLowerCase() + ".lastVIPPlusDailyreward")) {
			int pDay = dm.getData().getInt("Data." + p.getName().toLowerCase() + ".lastVIPPlusDailyreward.day");
			int pMonth = dm.getData().getInt("Data." + p.getName().toLowerCase() + ".lastVIPPlusDailyreward.month");
			int pYear = dm.getData().getInt("Data." + p.getName().toLowerCase() + ".lastVIPPlusDailyreward.year");
			
			int timeLeftint = 0;
			
			if (year == pYear && month == pMonth) {
				timeLeftint = day - pDay;
				if (timeLeftint < 0) {
					timeLeftint = timeLeftint + -1;
				}
				timeLeftint = 30 - timeLeftint;
				String timeLeft = timeLeftint + " " + mm.getMessages().getString("Messages.gui.DailyReward.days");
				rewardVIPPlusMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.recentGot").replace("%date%", timeLeft+""));
			} else {
				rewardVIPPlusMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.collectReward").replace("%type%", "MVIP/VIPPlus"));
			}
		} else {
			rewardVIPPlusMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.collectReward").replace("%type%", "MVIP/VIPPlus"));
		}

		if (!(p.hasPermission("lobby.vip") || p.hasPermission("lobby.mvip") || p.hasPermission("lobby.vipplus") || p.hasPermission("lobby.builder") || p.hasPermission("lobby.admin") || p.isOp()) ) {
			rewardVIPPlusMeta.setDisplayName(mm.getMessages().getString("Messages.gui.DailyReward.unlockRewardVIP"));
		}
		rewardVIPPlus.setItemMeta(rewardVIPPlusMeta);
		
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
		inv.setItem(10, nothing);
		inv.setItem(11, nothing);
		inv.setItem(12, nothing);
		inv.setItem(13, nothing);
		inv.setItem(14, nothing);
		inv.setItem(15, nothing);
		inv.setItem(16, nothing);
		inv.setItem(17, nothing);
		
		inv.setItem(18, nothing);
		inv.setItem(19, nothing);
		inv.setItem(20, rewardfree);
		inv.setItem(21, nothing);
		inv.setItem(22, rewardVIP);
		inv.setItem(23, nothing);
		inv.setItem(24, rewardVIPPlus);
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
		
		p.openInventory(inv);
	}
}
