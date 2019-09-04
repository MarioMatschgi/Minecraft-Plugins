package at.mario.hidenseek.gamestates;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.Roles;
import at.mario.hidenseek.countdowns.GameCountdown;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;

public class IngameState extends GameState {
	
	private GameCountdown gamecountdown;
	
	@Override
	public void start(String arena) {
		DataManager dm = new DataManager();
		List<Player> players = Main.ArenaPlayer.get(arena);
		
		Bukkit.broadcastMessage("§aDer INGAMESTATE wurde aktiviert");
		Location loc = (Location) dm.getData().get("Data.arenas." + arena + ".spawn");

		int rand = new Random().nextInt(players.size());
		Player p = players.get(rand);
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			player.teleport(loc);
			
			if (player == p) {
				Roles.roles.put(player, Roles.SEEKER);
			} else {
				Roles.roles.put(player, Roles.HIDER);
			}
		}
		
		gamecountdown = new GameCountdown();
		gamecountdown.run(arena);
	}

	@Override
	public void stop(String arena) {
		Bukkit.broadcastMessage("§cDer INGAMESTATE wurde deaktiviert");
	}
	
	public GameCountdown getGamecountdown() {
		return gamecountdown;
	}
}
