package at.mario.lobby.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import at.mario.lobby.Main;

public class TeleportListener implements Listener {
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		
		if (Main.isinLobby(e.getTo())) {
			Main.giveLobbyItems(p);
		} else {
			Main.removeLobbyItems(p);
		}
	}
}
