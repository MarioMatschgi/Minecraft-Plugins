package at.mario.pets.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import at.mario.pets.Pets;

public class Quitlistener implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Pets pets = new Pets();
		pets.removePet(e.getPlayer());
	}
}
