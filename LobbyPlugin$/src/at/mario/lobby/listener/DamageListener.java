package at.mario.lobby.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class DamageListener implements Listener {
	
	@EventHandler
	public void onDmgByEnt(EntityDamageByEntityEvent e) {
		MoveListener ml = new MoveListener();
		MessagesManager mm = new MessagesManager();
		
		Entity entity = (Entity) e.getEntity();
		Entity damager = (Entity) e.getDamager();

		if (e.getDamager() != null) {
			if (InventoryClick.pets.containsValue(damager)) {
				e.setCancelled(true);
				return;
			}
		}
		if (entity.getCustomName() != null) {
			if (entity.getCustomName().equals(mm.getMessages().getString("Messages.dailyReward.dailyRewardVillagerName"))) {
				e.setCancelled(true);
				return;
			}
		}
		if (InventoryClick.pets.containsValue(entity)) {
			e.setCancelled(true);
			return;
		}
		if (entity instanceof Player) {
			Player p = (Player) entity;
			if (ml.isinLobby(p)) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onDmgByBlock(EntityDamageByBlockEvent e) {
		MessagesManager mm = new MessagesManager();
		
		Entity entity = (Entity) e.getEntity();

		if (entity.getCustomName() != null) {
			if (entity.getCustomName().equals(mm.getMessages().getString("Messages.dailyReward.dailyRewardVillagerName"))) {
				e.setCancelled(true);
				return;
			}
		}
		if (InventoryClick.pets.containsValue(entity)) {
			e.setCancelled(true);
			return;
		}
		MoveListener ml = new MoveListener();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			if (ml.isinLobby(p)) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onDmg(EntityDamageEvent e) {
		MessagesManager mm = new MessagesManager();
		
		Entity entity = (Entity) e.getEntity();

		if (entity.getCustomName() != null) {
			if (entity.getCustomName().equals(mm.getMessages().getString("Messages.dailyReward.dailyRewardVillagerName"))) {
				e.setCancelled(true);
				return;
			}
		}
		if (InventoryClick.pets.containsValue(entity)) {
			e.setCancelled(true);
			return;
		}
		MoveListener ml = new MoveListener();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			if (ml.isinLobby(p)) {
				e.setCancelled(true);
				return;
			}
		}
	}
}
