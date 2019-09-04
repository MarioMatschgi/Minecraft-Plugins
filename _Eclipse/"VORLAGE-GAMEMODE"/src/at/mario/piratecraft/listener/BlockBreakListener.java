package at.mario.piratecraft.listener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.material.Sign;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.gamestates.EndingState;
import at.mario.piratecraft.gamestates.IngameState;
import at.mario.piratecraft.gamestates.LobbyState;
import at.mario.piratecraft.manager.ConfigManagers.DataManager;

public class BlockBreakListener implements Listener {

	@SuppressWarnings("unchecked")
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		DataManager dm = new DataManager();
		
		Block block = e.getBlock();
		
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		if (configSection != null) {
			for (String key : configSection.getKeys(false)) {
				if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState || Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState || 
						Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
					
					if (Main.ArenaPlayer.containsKey(key)) {
						if (Main.ArenaPlayer.get(key).contains(e.getPlayer())) {
							e.setCancelled(true);
						}
					}
				} else if (dm.getData().contains("Data.arenas." + key + ".signs")) {
					ArrayList<LinkedHashMap<String, Object>> signlist = (ArrayList<LinkedHashMap<String, Object>>) dm.getData().get("Data.arenas." + key + ".signs");
					for (int i = 0; i < signlist.size(); i++) {
						LinkedHashMap<String, Object> section = signlist.get(i);
						
						if (section.get("world").equals(block.getWorld().getName()) && (double) section.get("x") == (double) block.getX() && (double) section.get("y") == (double) block.getY() && (double) section.get("z") == (double) 
								block.getZ() ) {
							signlist.remove(i);
							
							dm.getData().set("Data.arenas." + key + ".signs", signlist);
							dm.saveData();
							break;
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void signDetachCheck(BlockPhysicsEvent event) {
		DataManager dm = new DataManager();

		Block block = event.getBlock();
		
		if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
			Sign s = (Sign) block.getState().getData();
			Block attachedBlock = block.getRelative(s.getAttachedFace());
			if (attachedBlock.getType() == Material.AIR) {
				
				
				ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
				if (configSection != null) {
					for (String key : configSection.getKeys(false)) {
						if (dm.getData().contains("Data.arenas." + key + ".signs")) {
							ArrayList<LinkedHashMap<String, Object>> signlist = (ArrayList<LinkedHashMap<String, Object>>) dm.getData().get("Data.arenas." + key + ".signs");
							for (int i = 0; i < signlist.size(); i++) {
								LinkedHashMap<String, Object> section = signlist.get(i);
								
								if (section.get("world").equals(block.getWorld().getName()) && (double) section.get("x") == (double) block.getX() && (double) section.get("y") == (double) block.getY() && (double) section.get("z") == (double) 
										block.getZ() ) {
									signlist.remove(i);
									
									dm.getData().set("Data.arenas." + key + ".signs", signlist);
									dm.saveData();
									break;
								}
							}
						}
					}
				}
			}
		}
	}
}
