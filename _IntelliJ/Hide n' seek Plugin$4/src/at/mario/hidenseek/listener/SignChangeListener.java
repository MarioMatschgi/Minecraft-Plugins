package at.mario.hidenseek.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.EndingState;
import at.mario.hidenseek.gamestates.GameState;
import at.mario.hidenseek.gamestates.IngameState;
import at.mario.hidenseek.gamestates.LobbyState;
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
					

					int currentPlayers = Main.ArenaPlayer.get(lines[1]).size();
					
					if (Main.getInstance().getGameStateManager().getCurrentGameState(lines[1]) == null) {
						Main.getInstance().getGameStateManager().setGameState(GameState.LOBBY_STATE, lines[1]);
					}
					
					String status = "";
					if (Main.getInstance().getGameStateManager().getCurrentGameState(lines[1]) instanceof LobbyState) {
						status = mm.getMessages().getString("Messages.sign.status.inLobby");
					} else if (Main.getInstance().getGameStateManager().getCurrentGameState(lines[1]) instanceof IngameState) {
						status = mm.getMessages().getString("Messages.sign.status.running");
					} else if (Main.getInstance().getGameStateManager().getCurrentGameState(lines[1]) instanceof EndingState) {
						status = mm.getMessages().getString("Messages.sign.status.stopped");
					} else {
						status = mm.getMessages().getString("Messages.sign.status.stopped");
					}
					
					sign.setLine(i, string.replace("%status%", status).replace("%map%", lines[1]).replace("%maxplayer%", dm.getData().getInt("Data.arenas." + lines[1] + ".maxplayer")+"").replace("%players%", currentPlayers +""));
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
		
		if (dm.getData().contains("Data.arenas." + arena + ".signs")) {
			ArrayList<Location> signlist = (ArrayList<Location>) dm.getData().get("Data.arenas." + arena + ".signs");
			for (int i = 0; i < signlist.size(); i++) {
				Location loc = signlist.get(i);
					
				Block block = Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(loc);
				BlockState state = block.getState();
				Sign sign = (Sign) state;
					
				List<String> list = mm.getMessages().getStringList("Messages.sign.format");
				for (int i1 = 0; i1 < 4; i1++) {
					String string = list.get(i1);

					int currentPlayers = 0;
					if (Main.ArenaPlayer.containsKey(arena)) {
						currentPlayers = Main.ArenaPlayer.get(arena).size();
					}
					
					String status = "";
					if (Main.getInstance().getGameStateManager().getCurrentGameState(arena) instanceof LobbyState) {
						status = mm.getMessages().getString("Messages.sign.status.inLobby");
					} else if (Main.getInstance().getGameStateManager().getCurrentGameState(arena) instanceof IngameState) {
						status = mm.getMessages().getString("Messages.sign.status.running");
					} else if (Main.getInstance().getGameStateManager().getCurrentGameState(arena) instanceof EndingState) {
						status = mm.getMessages().getString("Messages.sign.status.stopped");
					} else {
						status = mm.getMessages().getString("Messages.sign.status.stopped");
					}
				
					sign.setLine(i1, string.replace("%status%", status).replace("%map%", arena).replace("%minplayer%", dm.getData().getInt("Data.arenas." + arena + ".minplayer")+"").
							replace("%maxplayer%", LobbyState.maxPlayers.get(arena)+"").replace("%players%", currentPlayers +""));
				}
				sign.update();
			}
		}
	}
}
