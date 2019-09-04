package at.mario.utilreloaded.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class QuitListener implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		MessagesManager mm = new MessagesManager();
		
		Player p = e.getPlayer();
		
		e.setQuitMessage(mm.getMessages().getString("Messages.quitMessage").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", p.getName()));
	}
}
