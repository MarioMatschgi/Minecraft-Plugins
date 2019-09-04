package at.mario.utilreloaded.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class JoinListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		MessagesManager mm = new MessagesManager();
		
		Player p = e.getPlayer();
		
		e.setJoinMessage(mm.getMessages().getString("Messages.joinMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()));
	}
}
