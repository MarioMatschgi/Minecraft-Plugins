package at.mario.hidenseek.gamestates;

import org.bukkit.Bukkit;

public class IngameState extends GameState {

	@Override
	public void start() {
		Bukkit.broadcastMessage("§aDer INGAMESTATE wurde aktiviert");
	}

	@Override
	public void stop() {
		Bukkit.broadcastMessage("§cDer INGAMESTATE wurde deaktiviert");
	}

}
