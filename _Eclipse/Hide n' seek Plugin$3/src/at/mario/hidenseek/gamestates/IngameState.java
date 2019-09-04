package at.mario.hidenseek.gamestates;

import org.bukkit.Bukkit;

public class IngameState extends GameState {

	@Override
	public void start(String arena) {
		Bukkit.broadcastMessage("§aDer INGAMESTATE wurde aktiviert");
	}

	@Override
	public void stop(String arena) {
		Bukkit.broadcastMessage("§cDer INGAMESTATE wurde deaktiviert");
	}

}
