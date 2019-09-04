package at.mario.hidenseek.gamestates;

import org.bukkit.Bukkit;

import at.mario.hidenseek.countdowns.LobbyCountdown;

public class LobbyState extends GameState {
	
	public static final int MIN_PLAYERS = 1, 
							MAX_PLAYERS = 1;
	
	private LobbyCountdown lobbycountdown;
	
	@Override
	public void start() {
		lobbycountdown = new LobbyCountdown();
		lobbycountdown.idle();
	}

	@Override
	public void stop() {
		Bukkit.broadcastMessage("§cDer LOBBYSTATE wurde deaktiviert");
		lobbycountdown.cancelIdleing();
	}
	
	public LobbyCountdown getLobbycountdown() {
		return lobbycountdown;
	}
}
