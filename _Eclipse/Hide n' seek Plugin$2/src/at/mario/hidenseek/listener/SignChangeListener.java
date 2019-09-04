package at.mario.hidenseek.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;

public class SignChangeListener implements Listener {

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		String[] lines = e.getLines();
		Block block = e.getBlock();
		
		if (lines[0].equalsIgnoreCase("[HS]")) {
			e.setCancelled(true);
			if (dm.getData().contains("Data.arenas." + lines[1]) &&
				(dm.getData().contains("Data.arenas." + lines[1] + ".lobbyspawn")) && (dm.getData().contains("Data.arenas." + lines[1] + ".spawn")) && (dm.getData().contains("Data.arenas." + lines[1] + ".bounds.loc1")) && 
					(dm.getData().contains("Data.arenas." + lines[1] + ".bounds.loc2")) ) {
				
				BlockState state = block.getState();
				Sign sign = (Sign) state;
				
				List<String> list = mm.getMessages().getStringList("Messages.sign.format");
				for (int i = 0; i < 4; i++) {
					String string = list.get(i);
					
					String status = "";
					if (dm.getData().contains("Data.arenas." + lines[1] + ".status")) {
						status = mm.getMessages().getString("Messages.sign.status." + dm.getData().getString("Data.arenas." + lines[1] + ".status"));
					} else {
						status = mm.getMessages().getString("Messages.sign.status.stopped");
						dm.getData().set("Data.arenas." + lines[1] + ".status", "stopped");
						dm.saveData();
					}
					
					int players = 0;
					
					for (String key : Main.ArenaPlayer.keySet()) {
						if (key == lines[1]) {
							players++;
						}
					}
					
					sign.setLine(i, string.replace("%status%", status).replace("%map%", lines[1]).replace("%minplayer%", dm.getData().getInt("Data.arenas." + lines[1] + ".minplayer")+"").
							replace("%maxplayer%", dm.getData().getInt("Data.arenas." + lines[1] + ".maxplayer")+"").replace("%players%", players +""));
				}
				sign.update();
				
				ArrayList<Location> signlist = (ArrayList<Location>) dm.getData().get("Data.arenas." + lines[1] + ".signs");
				signlist.add(block.getLocation());
				dm.getData().set("Data.arenas." + lines[1] + ".signs", signlist);
				dm.saveData();
			} else {
				BlockState state = block.getState();
				Sign sign = (Sign) state;
				sign.setLine(0, lines[0]);
				sign.setLine(1, "§cCould not find map");
				sign.setLine(2, "§c" + lines[1]);
				sign.update();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void updateSigns(String arena) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		for(String key : configSection.getKeys(false)) {
			if (dm.getData().contains("Data.arenas." + key + ".signs")) {
				ArrayList<Location> signlist = (ArrayList<Location>) dm.getData().get("Data.arenas." + key + ".signs");
				for (int i = 0; i < signlist.size(); i++) {
					Location loc = signlist.get(i);
					
					Block block = Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(loc);
					BlockState state = block.getState();
					Sign sign = (Sign) state;
					
					List<String> list = mm.getMessages().getStringList("Messages.sign.format");
					for (int i1 = 0; i1 < 4; i1++) {
						String string = list.get(i1);
						
						String status = "";
						if (dm.getData().contains("Data.arenas." + arena + ".status")) {
							status = mm.getMessages().getString("Messages.sign.status." + dm.getData().getString("Data.arenas." + arena + ".status"));
						} else {
							status = mm.getMessages().getString("Messages.sign.status.stopped");
							dm.getData().set("Data.arenas." + arena + ".status", "stopped");
							dm.saveData();
						}

						int players = 0;
						
						for (String key1 : Main.ArenaPlayer.keySet()) {
							if (key1 == arena) {
								players++;
							}
						}
						
						sign.setLine(i1, string.replace("%status%", status).replace("%map%", arena).replace("%minplayer%", dm.getData().getInt("Data.arenas." + arena + ".minplayer")+"").
								replace("%maxplayer%", dm.getData().getInt("Data.arenas." + arena + ".maxplayer")+"").replace("%players%", players +""));
					}
					sign.update();
				}
			}
		}
	}
}
