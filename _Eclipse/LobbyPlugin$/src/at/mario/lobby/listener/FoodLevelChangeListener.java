package at.mario.lobby.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {
	
	@EventHandler
	public void foodLevelChange(FoodLevelChangeEvent e) {
		MoveListener ml = new MoveListener();
		
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			if (ml.isinLobby(p)) {
				p.setFoodLevel(20);
				p.setSaturation(21);
				e.setCancelled(true);
			}
		}
	}
}
