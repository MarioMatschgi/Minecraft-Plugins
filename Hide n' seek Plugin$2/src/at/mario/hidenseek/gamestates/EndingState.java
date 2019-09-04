package at.mario.hidenseek.gamestates;

import org.bukkit.Bukkit;

public class EndingState extends GameState {

	@Override
	public void start() {
		Bukkit.broadcastMessage("§aDer ENDINGSTATE wurde aktiviert");
	}

	@Override
	public void stop() {
		Bukkit.broadcastMessage("§cDer ENDINGSTATE wurde deaktiviert");
	}

}
