package at.mario.masterbuilders.listener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import at.mario.masterbuilders.Main;
import at.mario.masterbuilders.gamestates.EndingState;
import at.mario.masterbuilders.gamestates.GameState;
import at.mario.masterbuilders.gamestates.IngameState;
import at.mario.masterbuilders.gamestates.LobbyState;
import at.mario.masterbuilders.manager.ConfigManagers.DataManager;
import at.mario.masterbuilders.manager.ConfigManagers.MessagesManager;

public class SignChangeListener implements Listener {

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		String[] lines = e.getLines();
		Block block = e.getBlock();
		Player p = e.getPlayer();
		
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
					

					int currentPlayers = 0;
					if (Main.ArenaPlayer.containsKey(lines[1])) {
						currentPlayers = Main.ArenaPlayer.get(lines[1]).size();
					}
					
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
					
					sign.setLine(i, string.replace("%status%", status).replace("%map%", lines[1]).replace("%minplayer%", dm.getData().getInt("Data.arenas." + lines[1] + ".minplayers")+"").
							replace("%maxplayer%", dm.getData().getInt("Data.arenas." + lines[1] + ".maxplayers")+"").replace("%players%", currentPlayers +""));
				}
				sign.update();
				
				ArrayList<LinkedHashMap<String, Object>> signlist = new ArrayList<LinkedHashMap<String, Object>>();
				if (dm.getData().contains("Data.arenas." + lines[1] + ".signs")) {
					signlist = (ArrayList<LinkedHashMap<String, Object>>) dm.getData().get("Data.arenas." + lines[1] + ".signs");
				}
				LinkedHashMap<String, Object> section = new LinkedHashMap<String, Object>();
				section.put("world", block.getWorld().getName());
				section.put("x", (double) block.getX());
				section.put("y", (double) block.getY());
				section.put("z", (double) block.getZ());
				
				Main.getInstance().updateAllSigns();
				
				signlist.add(section);
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
			if (dm.getData().getString("Data.mainlobbyspawn.world") == null || dm.getData().get("Data.mainlobbyspawn.pitch") == null || dm.getData().get("Data.mainlobbyspawn.yaw") == null || dm.getData().get("Data.mainlobbyspawn.x") == null 
					|| dm.getData().get("Data.mainlobbyspawn.y") == null || dm.getData().get("Data.mainlobbyspawn.z") == null) {
				p.sendMessage(mm.getMessages().getString("Messages.noMainLobbySpawn").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void updateSigns(String arena) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		if (dm.getData().contains("Data.arenas." + arena + ".signs")) {
			
			ArrayList<LinkedHashMap<String, Object>> signlistEig = (ArrayList<LinkedHashMap<String, Object>>) dm.getData().get("Data.arenas." + arena + ".signs");
			if (signlistEig != null && !signlistEig.isEmpty()) {
				for (int i = 0; i < signlistEig.size(); i++) {
					LinkedHashMap<String, Object> section = signlistEig.get(i);
					
					if (section != null) {
						Location loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
						loc.setWorld(Bukkit.getWorld((String) section.get("world")));
						loc.setX((double) section.get("x"));
						loc.setY((double) section.get("y"));
						loc.setZ((double) section.get("z"));
						
						Block block = loc.getBlock();
						BlockState state = block.getState();
						if (block.getState() instanceof Sign) {
							Sign sign = (Sign) state;
							
							List<String> list = mm.getMessages().getStringList("Messages.sign.format");
							for (int i1 = 0; i1 < 4; i1++) {
								String string = list.get(i1);

								int currentPlayers = 0;
								
								if (Main.ArenaPlayer != null) {
									if (Main.ArenaPlayer.containsKey(arena)) {
										if (Main.ArenaPlayer.get(arena) != null) {
											if (!Main.ArenaPlayer.get(arena).isEmpty()) {
												currentPlayers = Main.ArenaPlayer.get(arena).size();
											}
										}
									}
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
								
								sign.setLine(i1, string.replace("%status%", status).replace("%map%", arena).replace("%minplayer%", dm.getData().getInt("Data.arenas." + arena + ".minplayers")+"").
										replace("%maxplayer%", LobbyState.maxPlayers.get(arena)+"").replace("%players%", currentPlayers +""));
							}
							sign.update();
						}
					}
				}
			}
			
			
			
			
			
			
			/*
			ArrayList<Location> signlist = (ArrayList<Location>) dm.getData().get("Data.arenas." + arena + ".signs");
			if (signlist != null && !signlist.isEmpty()) {
				for (int i = 0; i < signlist.size(); i++) {
					if (signlist.get(i) != null) {
						Location loc = signlist.get(i);
						

						Bukkit.getConsoleSender().sendMessage("§C#######LOCATION: " + loc);

						
						Bukkit.getConsoleSender().sendMessage("§C#######I: " + i);
						Bukkit.getConsoleSender().sendMessage("§C#######Signlist: " + signlist);
						
						
						Bukkit.getConsoleSender().sendMessage("§C#######WORLD: " + loc.getWorld());
						Bukkit.getConsoleSender().sendMessage("§C#######WORLDNAME: " + loc.getWorld().getName());
						Bukkit.getConsoleSender().sendMessage("§C#######WORLD2: " + Bukkit.getWorld(loc.getWorld().getName()));
						
						Block block = Bukkit.getWorld(loc.getWorld().getName()).getBlockAt(loc);
						BlockState state = block.getState();
						if (block.getState() instanceof Sign) {
							Sign sign = (Sign) state;
							
							List<String> list = mm.getMessages().getStringList("Messages.sign.format");
							for (int i1 = 0; i1 < 4; i1++) {
								String string = list.get(i1);

								int currentPlayers = 0;
								
								if (Main.ArenaPlayer != null) {
									if (Main.ArenaPlayer.containsKey(arena)) {
										if (Main.ArenaPlayer.get(arena) != null) {
											if (!Main.ArenaPlayer.get(arena).isEmpty()) {
												currentPlayers = Main.ArenaPlayer.get(arena).size();
											}
										}
									}
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
								
								sign.setLine(i1, string.replace("%status%", status).replace("%map%", arena).replace("%minplayer%", dm.getData().getInt("Data.arenas." + arena + ".minplayers")+"").
										replace("%maxplayer%", LobbyState.maxPlayers.get(arena)+"").replace("%players%", currentPlayers +""));
							}
							sign.update();
						}
					}
				}
			}*/
		}
	}
}
