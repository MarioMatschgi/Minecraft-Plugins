package at.mario.hidenseek.gamestates;

import java.util.HashMap;

import at.mario.hidenseek.countdowns.LobbyCountdown;

public class LobbyState extends GameState {
	
	public static HashMap<String, Integer> minPlayers;
	public static HashMap<String, Integer> maxPlayers;
	
	private LobbyCountdown lobbycountdown;

	@Override
	public void start(String arena) {
		lobbycountdown = new LobbyCountdown();
		// lobbycountdown.idle(arena);
	}

	@Override
	public void stop(String arena) {
		if (lobbycountdown.isIdling(arena)) {
			lobbycountdown.cancelIdleing(arena);
		}
		if (lobbycountdown.isRunning(arena)) {
			lobbycountdown.cancel(arena);
		}
	}
	
	public LobbyCountdown getLobbycountdown() {
		return lobbycountdown;
	}
}
