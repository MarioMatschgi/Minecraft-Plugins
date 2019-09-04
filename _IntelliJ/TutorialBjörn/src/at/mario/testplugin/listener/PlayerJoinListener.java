package at.mario.testplugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import at.mario.testplugin.Main;

public class PlayerJoinListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		p.sendMessage(Main.PREFIX + "§bHi§f, §6" + p.getName());
		e.setJoinMessage("§bThe player §6" + p.getName() + " §bjoined the server§f!");
	}
}
