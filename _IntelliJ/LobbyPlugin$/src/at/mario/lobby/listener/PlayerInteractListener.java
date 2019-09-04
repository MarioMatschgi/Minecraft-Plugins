package at.mario.lobby.listener;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import at.mario.lobby.Main;
import at.mario.lobby.commands.SilenthubCMD;
import at.mario.lobby.inventories.DailyRewardInventory;
import at.mario.lobby.inventories.MainInventory;
import at.mario.lobby.inventories.TeleportInventory;
import at.mario.lobby.inventories.VisibilityInventory;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class PlayerInteractListener implements Listener {
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		MessagesManager mm = new MessagesManager();
		Player p = e.getPlayer();
		Block block = e.getClickedBlock();
		DataManager dm = new DataManager();
		ItemStack item = p.getItemInHand();
		
		if (item == null || item.getItemMeta() == null) {
			return;
		}
		
		if (item.getItemMeta().getDisplayName() == Main.wandName) {
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
				dm.getData().set("Data.lobby.location.loc1.world", block.getLocation().getWorld().getName());
				dm.getData().set("Data.lobby.location.loc1.pitch", (float) block.getLocation().getPitch());
				dm.getData().set("Data.lobby.location.loc1.yaw", (float) block.getLocation().getYaw());
				dm.getData().set("Data.lobby.location.loc1.x", block.getLocation().getX());
				dm.getData().set("Data.lobby.location.loc1.y", block.getLocation().getY());
				dm.getData().set("Data.lobby.location.loc1.z", block.getLocation().getZ());
				dm.saveData();

				mm.sendReplacedMessage("%prefix% §aSuccesfully set the first position of the lobby boundaries!", p);
			} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				dm.getData().set("Data.lobby.location.loc2.world", block.getLocation().getWorld().getName());
				dm.getData().set("Data.lobby.location.loc2.pitch", (float) block.getLocation().getPitch());
				dm.getData().set("Data.lobby.location.loc2.yaw", (float) block.getLocation().getYaw());
				dm.getData().set("Data.lobby.location.loc2.x", block.getLocation().getX());
				dm.getData().set("Data.lobby.location.loc2.y", block.getLocation().getY());
				dm.getData().set("Data.lobby.location.loc2.z", block.getLocation().getZ());
				dm.saveData();
				
				mm.sendReplacedMessage("%prefix% §aSuccesfully set the second position of the lobby boundaries!", p);
			} else if (e.getAction() == Action.LEFT_CLICK_AIR) {
				dm.getData().set("Data.lobby.location.loc1.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.lobby.location.loc1.pitch", (float) p.getLocation().getPitch());
				dm.getData().set("Data.lobby.location.loc1.yaw", (float) p.getLocation().getYaw());
				dm.getData().set("Data.lobby.location.loc1.x", p.getLocation().getX());
				dm.getData().set("Data.lobby.location.loc1.y", p.getLocation().getY());
				dm.getData().set("Data.lobby.location.loc1.z", p.getLocation().getZ());
				dm.saveData();

				mm.sendReplacedMessage("%prefix% §aSuccesfully set the first position of the lobby boundaries!", p);
			} else if (e.getAction() == Action.RIGHT_CLICK_AIR) {
				dm.getData().set("Data.lobby.location.loc2.world", p.getLocation().getWorld().getName());
				dm.getData().set("Data.lobby.location.loc2.pitch", (float) p.getLocation().getPitch());
				dm.getData().set("Data.lobby.location.loc2.yaw", (float) p.getLocation().getYaw());
				dm.getData().set("Data.lobby.location.loc2.x", p.getLocation().getX());
				dm.getData().set("Data.lobby.location.loc2.y", p.getLocation().getY());
				dm.getData().set("Data.lobby.location.loc2.z", p.getLocation().getZ());
				dm.saveData();

				mm.sendReplacedMessage("%prefix% §aSuccesfully set the second position of the lobby boundaries!", p);
			}
		} else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.profiles") && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) ) {
			MainInventory.getInstance().newInventory(p);
		} else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.teleporter") && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) ) {
			TeleportInventory.getInstance().newInventory(p);
		} else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.visibility") && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) ) {
			VisibilityInventory.getInstance().newInventory(p);
		} else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.silenthub") && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) ) {
			if (SilenthubCMD.hiddenPlayers.containsKey(p)) {
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				     p.showPlayer(player);
				     SilenthubCMD.hiddenPlayers.remove(p);
				}

				mm.sendMessage("Messages.silenthub.disabled", p);
			} else {
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					p.hidePlayer(player);
					SilenthubCMD.hiddenPlayers.put(p, player);
				}
				mm.sendMessage("Messages.silenthub.enabled", p);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		MessagesManager mm = new MessagesManager();
		// DataManager dm = new DataManager();
		ItemStack item = p.getItemInHand();
		
		Entity entity = e.getRightClicked();
		
		if (item == null) {
			return;
		}
		
		if (entity == null) {
			return;
		}
		
		if (InventoryClick.pets.containsValue(entity)) {
			e.setCancelled(true);
		}
		if (entity.getCustomName() != null) {
			if (entity.getCustomName().equals(mm.getMessages().getString("Messages.dailyReward.dailyRewardVillagerName"))) {
				e.setCancelled(true);
				
				DailyRewardInventory.getInstance().newInventory(p);
			}
		}
		
		if (item.hasItemMeta()) {
			if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.profiles")) {
				MainInventory.getInstance().newInventory(p);
			} else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.teleporter")) {
				TeleportInventory.getInstance().newInventory(p);
			} else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.visibility")) {
				VisibilityInventory.getInstance().newInventory(p);
			} else if (item.getItemMeta().getDisplayName() == mm.getMessages().getString("Messages.inventory.silenthub")) {
				if (SilenthubCMD.hiddenPlayers.containsKey(p)) {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					     p.showPlayer(player);
					     SilenthubCMD.hiddenPlayers.remove(p);
					}
					
					mm.sendMessage("Messages.silenthub.enabled", p);
				} else {
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						p.hidePlayer(player);
						SilenthubCMD.hiddenPlayers.put(p, player);
					}
					mm.sendMessage("Messages.silenthub.disabled", p);
				}
			}
		}
	}
}
