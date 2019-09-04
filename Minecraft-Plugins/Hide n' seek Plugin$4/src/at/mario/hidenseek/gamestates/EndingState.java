package at.mario.hidenseek.gamestates;

import org.bukkit.Bukkit;

public class EndingState extends GameState {

	@Override
	public void start(String arena) {
		Bukkit.broadcastMessage("§aDer ENDINGSTATE wurde aktiviert");
	}

	@Override
	public void stop(String arena) {
		Bukkit.broadcastMessage("§cDer ENDINGSTATE wurde deaktiviert");
	}

}
