package at.mario.hidenseek.gamestates;

import java.util.HashMap;

import org.bukkit.entity.Player;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.countdowns.LobbyCountdown;

public class LobbyState extends GameState {
	
	public static HashMap<String, Integer> minPlayers;
	public static HashMap<String, Integer> maxPlayers;
	
	public static HashMap<Player, String> joinmePassPlayer = new HashMap<Player, String>();
	
	private LobbyCountdown lobbycountdown;

	@Override
	public void start(String arena) {
		Main.getInstance().getGameStateManager().setIsJoinme(arena, false);
		lobbycountdown = new LobbyCountdown();
		// lobbycountdown.idle(arena);
	}

	@Override
	public void stop(String arena) {
		joinmePassPlayer = new HashMap<Player, String>();
		
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
