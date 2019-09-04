package at.mario.piratecraft.gamestates;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.manager.StatsManager;
import at.mario.piratecraft.manager.ConfigManagers.DataManager;
import at.mario.piratecraft.utils.ItemsUtil;
import at.mario.piratecraft.utils.PlayerUtil;

public class IngameState extends GameState {
	
	public static HashMap<String, Long> gameStarts = new HashMap<String, Long>();
	
	@Override
	public void start(String arena) {
		DataManager dm = new DataManager();
		
		List<Player> players = Main.ArenaPlayer.get(arena);
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			PlayerUtil.setGamemode(player, "inGame");
			ItemsUtil.removeItems(player);
		}
		
		Location loc = new Location(Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0, 0.0F, 0.0F);
		loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + arena + ".spawn.world")));
		loc.setPitch((float) ((double) dm.getData().get("Data.maps." + arena + ".spawn.pitch")));
		loc.setYaw((float) ((double) dm.getData().get("Data.maps." + arena + ".spawn.yaw")));
		loc.setX(dm.getData().getDouble("Data.maps." + arena + ".spawn.x"));
		loc.setY(dm.getData().getDouble("Data.maps." + arena + ".spawn.y"));
		loc.setZ(dm.getData().getDouble("Data.maps." + arena + ".spawn.z"));
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			player.teleport(loc);
			StatsManager.setStat(player, "gamesPlayed", StatsManager.getStats(player).getInt("gamesPlayed") + 1);
		}
	}
	
	@Override
	public void stop(String arena) {
		
	}
}
