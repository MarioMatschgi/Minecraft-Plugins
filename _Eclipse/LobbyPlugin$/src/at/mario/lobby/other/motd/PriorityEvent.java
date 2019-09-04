package at.mario.lobby.other.motd;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PriorityEvent implements Listener {
	
	  @EventHandler(priority=EventPriority.MONITOR)
	  public void onServerListPing(ServerListPingEvent event)
	  {
	    event.setMotd(MotdCMD.Motd.replaceAll("(&([a-r0-9]))", "��$2"));
	  }
}
