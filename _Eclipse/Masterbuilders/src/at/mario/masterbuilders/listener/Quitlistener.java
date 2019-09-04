package at.mario.masterbuilders.listener;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import at.mario.masterbuilders.Main;

public class Quitlistener implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		for (String key : Main.ArenaPlayer.keySet()) {
			List<Player> players = Main.ArenaPlayer.get(key);
			
			if (players != null && players.contains(p)) {
				Main.leaveGame(key, p);
			}
		}
		Main.removeLobbyItems(p);
	}
}
