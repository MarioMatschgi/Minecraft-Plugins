package at.mario.hidenseek.manager;

import java.util.HashMap;

import at.mario.hidenseek.gamestates.EndingState;
import at.mario.hidenseek.gamestates.GameState;
import at.mario.hidenseek.gamestates.IngameState;
import at.mario.hidenseek.gamestates.LobbyState;

public class GameStateManager {
	
	private GameState[] gameStates = new GameState[3];
	private HashMap<String, GameState> currentGameStates;
	private HashMap<String, Boolean> isJoinme;
	
	public GameStateManager() {
		currentGameStates = new HashMap<String, GameState>();
		isJoinme = new HashMap<String, Boolean>();
		gameStates[GameState.LOBBY_STATE] = new LobbyState();
		gameStates[GameState.INGAME_STATE] = new IngameState();
		gameStates[GameState.ENDING_STATE] = new EndingState();
	}
	
	public void setGameState(int GameState, String arena) {
		if (currentGameStates.get(arena) != null) {
			currentGameStates.get(arena).stop(arena);
		}
		currentGameStates.put(arena, gameStates[GameState]);
		currentGameStates.get(arena).start(arena);
	}
	
	public void stopCurrentGameState(String arena) {
		currentGameStates.get(arena).stop(arena);
		currentGameStates.remove(arena);
	}
	
	public GameState getCurrentGameState(String arena) {
		return currentGameStates.get(arena);
	}
	
	public GameState[] getGameStates() {
		return gameStates;
	}
	
	public Boolean getIsJoinme(String arena) {
		return isJoinme.get(arena);
	}

	public Boolean setIsJoinme(String arena, Boolean bool) {
		return isJoinme.put(arena, bool);
	}
}
