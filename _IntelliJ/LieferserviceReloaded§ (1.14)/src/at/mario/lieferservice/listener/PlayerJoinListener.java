package at.mario.lieferservice.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import at.mario.lieferservice.Main;
import at.mario.lieferservice.Request;
import at.mario.lieferservice.RequestStatus;
import at.mario.lieferservice.manager.ConfigManagers.MessagesManager;

public class PlayerJoinListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void InvenClick(PlayerJoinEvent e) {
		MessagesManager mm = new MessagesManager();
		
		Player p = e.getPlayer();
		
		if (p.getName().equals("Mario_Matschgi")) {
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getPlugin(), new Runnable() {
				@Override
				public void run() {
					int newRequests = 0;
					for (int i = 0; i < Main.requests.size(); i++) {
						Request request = Main.requests.get(i);
						
						if (request.status.equals(RequestStatus.NEW_REQUEST)) 
							newRequests++;
					}
					
					if (newRequests == 0) {
						p.sendMessage(mm.getMessages().getString("Messages.newRequest.noNew").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("§nl", "\n"));
					} else {
						p.sendMessage(mm.getMessages().getString("Messages.newRequest.new").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("§nl", "\n").
								replace("%amount%", newRequests+""));
					}
				}
			}, 2);
		}
	}
}
