package at.mario.lobby.listener;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.mario.lobby.Main;
import at.mario.lobby.manager.ConfigManagers.DataManager;
import at.mario.lobby.manager.ConfigManagers.MessagesManager;

public class GamemodeListener implements Listener {
	
	@EventHandler
	public void onGamemode(PlayerGameModeChangeEvent e) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		Player p = e.getPlayer();
		
		if (e.getNewGameMode() == GameMode.CREATIVE) {
			Main.removeLobbyItems(p);
			if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.builder")) {
				dm.getData().set("Data." + p.getName().toLowerCase() + ".build", true);
				mm.sendMessage("Messages.build.enabled", p);
			}
			p.setAllowFlight(true);
		} else if (e.getNewGameMode() == GameMode.SPECTATOR) {
			p.setAllowFlight(true);
		} else {
			if (Main.isinLobby(p.getLocation())) {
				giveLobbyItems(p);
			}
			if (p.hasPermission("lobby.admin") || p.hasPermission("lobby.builder")) {
				dm.getData().set("Data." + p.getName().toLowerCase() + ".build", false);
				mm.sendMessage("Messages.build.disabled", p);
			}
			p.setAllowFlight(false);
		}
		dm.saveData();
	}
	
	public static void giveLobbyItems(Player p) {
		MessagesManager mm = new MessagesManager();
		
		ItemStack teleporter = new ItemStack(Material.COMPASS);
		ItemMeta teleporterMeta = teleporter.getItemMeta();
		teleporterMeta.setDisplayName(mm.getMessages().getString("Messages.inventory.teleporter"));
		ArrayList<String> teleporterList = new ArrayList<String>();
		teleporterList.add(mm.getMessages().getString("Messages.inventory.meta.teleporter"));
		teleporterMeta.setLore(teleporterList);
		teleporter.setItemMeta(teleporterMeta);

		ItemStack profile = new ItemStack(Material.CHEST);
		ItemMeta profileMeta = profile.getItemMeta();
		profileMeta.setDisplayName(mm.getMessages().getString("Messages.inventory.profiles"));
		ArrayList<String> profileList = new ArrayList<String>();
		profileList.add(mm.getMessages().getString("Messages.inventory.meta.profiles"));
		profileMeta.setLore(profileList);
		profile.setItemMeta(profileMeta);

		ItemStack visibility = new ItemStack(Material.INK_SACK, 1, (short) 10);
		ItemMeta visibilityMeta = visibility.getItemMeta();
		visibilityMeta.setDisplayName(mm.getMessages().getString("Messages.inventory.visibility"));
		ArrayList<String> visibilityList = new ArrayList<String>();
		visibilityList.add(mm.getMessages().getString("Messages.inventory.meta.visibility"));
		visibilityMeta.setLore(visibilityList);
		visibility.setItemMeta(visibilityMeta);
		
		ItemStack silenthub = new ItemStack(Material.TNT);
		ItemMeta silenthubMeta = silenthub.getItemMeta();
		silenthubMeta.setDisplayName(mm.getMessages().getString("Messages.inventory.silenthub"));
		ArrayList<String> silenthubList = new ArrayList<String>();
		silenthubList.add(mm.getMessages().getString("Messages.inventory.meta.silenthub"));
		silenthubMeta.setLore(silenthubList);
		silenthub.setItemMeta(silenthubMeta);
		
		
		p.getInventory().setItem(0, teleporter);
		p.getInventory().setItem(2, visibility);
		p.getInventory().setItem(4, profile);
		if (p.hasPermission("lobby.vip") || p.hasPermission("lobby.admin")) {
			p.getInventory().setItem(6, silenthub);
		}
	}
}
