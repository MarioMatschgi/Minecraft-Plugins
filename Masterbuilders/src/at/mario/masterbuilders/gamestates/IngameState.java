package at.mario.masterbuilders.gamestates;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import at.mario.masterbuilders.Main;
import at.mario.masterbuilders.countdowns.GameCountdown;
import at.mario.masterbuilders.countdowns.SeekerReleaseCountdown;
import at.mario.masterbuilders.manager.ConfigManagers.DataManager;

public class IngameState extends GameState {
	
	private GameCountdown gamecountdown;
	private SeekerReleaseCountdown seekerReleaseCountdown;
	
	@Override
	public void start(String arena) {
		DataManager dm = new DataManager();
		
		seekerReleaseCountdown = new SeekerReleaseCountdown();
		seekerReleaseCountdown.run(arena);
		
		
		List<Player> players = Main.ArenaPlayer.get(arena);
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			if (Main.getInstance().getConfig().get("Config.gamemode.inGame") instanceof String) {
				if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("survival")) {
					player.setGameMode(GameMode.SURVIVAL);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("creative")) {
					player.setGameMode(GameMode.CREATIVE);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("adventure")) {
					player.setGameMode(GameMode.ADVENTURE);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("spectator")) {
					player.setGameMode(GameMode.SPECTATOR);
				}
			} else {
				if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 0) {
					player.setGameMode(GameMode.SURVIVAL);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 1) {
					player.setGameMode(GameMode.CREATIVE);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 2) {
					player.setGameMode(GameMode.ADVENTURE);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 3) {
					player.setGameMode(GameMode.SPECTATOR);
				}
			}
			
			Main.removeLobbyItems(player);
		}
		
		Location loc = new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0);
		loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arena + ".spawn.world")));
		loc.setPitch((float) ((double) dm.getData().get("Data.arenas." + arena + ".spawn.pitch")));
		loc.setYaw((float) ((double) dm.getData().get("Data.arenas." + arena + ".spawn.yaw")));
		loc.setX(dm.getData().getDouble("Data.arenas." + arena + ".spawn.x"));
		loc.setY(dm.getData().getDouble("Data.arenas." + arena + ".spawn.y"));
		loc.setZ(dm.getData().getDouble("Data.arenas." + arena + ".spawn.z"));
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			player.teleport(loc);
		}
		
		gamecountdown = new GameCountdown();
		gamecountdown.run(arena);
	}
	
	@Override
	public void stop(String arena) {
		if (gamecountdown.isRunning(arena)) {
			gamecountdown.cancel(arena);
		}
	}
	
	public GameCountdown getGamecountdown() {
		return gamecountdown;
	}
}
