package at.mario.hidenseek.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;

public class Quitlistener implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		DataManager dm = new DataManager();
		
		Player p = e.getPlayer();
		
		if (Main.ArenaPlayer.containsValue(p)) {
			String arena = "";
			for (Object o : Main.ArenaPlayer.keySet()) {
				if (Main.ArenaPlayer.get(o).equals(p)) {
					arena = (String) o;
				}
			}
			Main.ArenaPlayer.values().remove(p);
			SignChangeListener.updateSigns(arena);
			
			int currentPlayers = 0;
			for (String key2 : Main.ArenaPlayer.keySet()) {
				if (key2 == arena) {
					currentPlayers++;
				}
			}

			if (Main.getInstance().getGameStateManager().getCurrentGameState() instanceof LobbyState) {
				LobbyState lobbyState = (LobbyState) Main.getInstance().getGameStateManager().getCurrentGameState();
				
				if (currentPlayers < LobbyState.MIN_PLAYERS) {
					// LobbyCountdown stoppen...
					if (lobbyState.getLobbycountdown().isRunning()) {
						lobbyState.getLobbycountdown().cancel();
						if (!lobbyState.getLobbycountdown().isIdling()) {
							lobbyState.getLobbycountdown().idle();
						}
					}
				}
			}
			
			Location loc = (Location) dm.getData().get("Data.mainlobbyspawn");
			p.teleport(loc);
		}
	}
}
