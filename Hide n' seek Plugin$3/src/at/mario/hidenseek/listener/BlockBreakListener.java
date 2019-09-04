package at.mario.hidenseek.listener;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.material.Sign;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;

public class BlockBreakListener implements Listener {
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		DataManager dm = new DataManager();
		
		Block block = e.getBlock();
		
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		for(String key : configSection.getKeys(false)) {
			if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
				if (Main.ArenaPlayer.containsKey(key)) {
					if (Main.ArenaPlayer.get(key).contains(e.getPlayer())) {
						e.setCancelled(true);
					}
				}
			} else if (dm.getData().contains("Data.arenas." + key + ".signs")) {
				ArrayList<Location> signlist = (ArrayList<Location>) dm.getData().get("Data.arenas." + key + ".signs");
				if (signlist.contains(block.getLocation())) {
					signlist.remove(block.getLocation());
					dm.getData().set("Data.arenas." + key + ".signs", signlist);
					dm.saveData();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void signDetachCheck(BlockPhysicsEvent event) {
		DataManager dm = new DataManager();

		Block block = event.getBlock();
		
		Block b = event.getBlock();
		if (b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST) {
			Sign s = (Sign) b.getState().getData();
			Block attachedBlock = b.getRelative(s.getAttachedFace());
			if (attachedBlock.getType() == Material.AIR) {
				ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
				for(String key : configSection.getKeys(false)) {
					if (dm.getData().contains("Data.arenas." + key + ".signs")) {
						ArrayList<Location> signlist = (ArrayList<Location>) dm.getData().get("Data.arenas." + key + ".signs");
						if (signlist.contains(block.getLocation())) {
							signlist.remove(block.getLocation());
							dm.getData().set("Data.arenas." + key + ".signs", signlist);
							dm.saveData();
						}
					}
				}
			}
		}
	}
}
