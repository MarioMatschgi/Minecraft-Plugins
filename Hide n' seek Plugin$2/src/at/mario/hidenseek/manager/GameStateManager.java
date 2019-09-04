package at.mario.hidenseek.manager;

import at.mario.hidenseek.gamestates.EndingState;
import at.mario.hidenseek.gamestates.GameState;
import at.mario.hidenseek.gamestates.IngameState;
import at.mario.hidenseek.gamestates.LobbyState;

public class GameStateManager {
	
	private GameState[] gameStates = new GameState[3];
	private GameState currentGameState;
	
	public GameStateManager() {
		gameStates[GameState.LOBBY_STATE] = new LobbyState();
		gameStates[GameState.INGAME_STATE] = new IngameState();
		gameStates[GameState.ENDING_STATE] = new EndingState();
	}
	
	public void setGameState(int GameState) {
		if (currentGameState != null) {
			currentGameState.stop();
		}
		currentGameState = gameStates[GameState];
		currentGameState.start();
	}
	
	public void stopCurrentGameState() {
		currentGameState.stop();
		currentGameState = null;
	}
	
	public GameState getCurrentGameState() {
		return currentGameState;
	}
}
