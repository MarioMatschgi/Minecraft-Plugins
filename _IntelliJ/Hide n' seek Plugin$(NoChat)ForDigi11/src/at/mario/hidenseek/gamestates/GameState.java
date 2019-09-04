package at.mario.hidenseek.gamestates;

public abstract class GameState {
	
	public static final int LOBBY_STATE = 0,
							INGAME_STATE = 1,
							ENDING_STATE = 2;
	
	public abstract void start(String arena);
	public abstract void stop(String arena);
}
