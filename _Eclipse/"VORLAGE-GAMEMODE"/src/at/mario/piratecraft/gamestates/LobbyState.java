package at.mario.piratecraft.gamestates;

import java.util.HashMap;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.countdowns.LobbyCountdown;

public class LobbyState extends GameState {
	
	public static HashMap<String, Integer> minPlayers;
	public static HashMap<String, Integer> maxPlayers;
	
	
	private LobbyCountdown lobbycountdown;

	@Override
	public void start(String arena) {
		Main.getInstance().getGameStateManager().setIsJoinme(arena, false);
		lobbycountdown = new LobbyCountdown();
	}

	@Override
	public void stop(String arena) {
		if (lobbycountdown.isIdling(arena)) {
			lobbycountdown.cancelIdleing(arena);
		}
		if (lobbycountdown.isRunning(arena)) {
			lobbycountdown.cancel(arena);
		}
		Main.getInstance().getGameStateManager().setIsJoinme(arena, false);
	}
	
	public LobbyCountdown getLobbycountdown() {
		return lobbycountdown;
	}
}
