package at.mario.piratecraft.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.gamestates.LobbyState;
import at.mario.piratecraft.inventories.ShopInventory;
import at.mario.piratecraft.manager.ConfigManagers.DataManager;
import at.mario.piratecraft.manager.ConfigManagers.MessagesManager;
import at.mario.piratecraft.utils.PlayerUtil;

public class PlayerInteractListener implements Listener {
	
	public static HashMap<Player, Integer> taskIDs = new HashMap<Player, Integer>();
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		Block block = e.getClickedBlock();
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			// SIGN check
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
					ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
					if (configSection != null) {
						for (String key : configSection.getKeys(false)) {
							if (dm.getData().contains("Data.arenas." + key + ".signs")) {
								ArrayList<LinkedHashMap<String, Object>> signlist = (ArrayList<LinkedHashMap<String, Object>>) dm.getData().get("Data.arenas." + key + ".signs");
								for (int i = 0; i < signlist.size(); i++) {
									LinkedHashMap<String, Object> section = signlist.get(i);
									
									if (section.get("world").equals(block.getWorld().getName()) && (double) section.get("x") == (double) block.getX() && (double) section.get("y") == (double) block.getY() && (double) 
											section.get("z") == (double) block.getZ() ) {
										PlayerUtil.joinGame(key, p);
										return;
									}
								}
							}
						}
					}
				}
			}
			
			if (item != null && item.hasItemMeta() && item.getType() != Material.AIR) {
				for (String key : Main.ArenaPlayer.keySet()) {
					List<Player> players = Main.ArenaPlayer.get(key);
					
					if (players.contains(p)) {
						// leave check
						if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.leaveItem.name"))) {
							PlayerUtil.leaveGame(true, key, p);
							e.setCancelled(true);
							return;
						}
						// SHOP check
						else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.shopItem.name"))) {
							ShopInventory.getInstance().newInventory(p);
							e.setCancelled(true);
							return;
						}
						// START check
						else if (item.getItemMeta().getDisplayName().equals(mm.getMessages().getString("Messages.lobbyitems.startItem.name"))) {
							if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
								LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState(key);
								
								if (lobbyState.getLobbycountdown().isRunning(key)) {
									if (!(lobbyState.getLobbycountdown().getSeconds(key) <= 3) ) {
										lobbyState.getLobbycountdown().setSeconds(3, key);
										e.setCancelled(true);
									}
								}
							}
							e.setCancelled(true);
							return;
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e) {
		
	}
}
