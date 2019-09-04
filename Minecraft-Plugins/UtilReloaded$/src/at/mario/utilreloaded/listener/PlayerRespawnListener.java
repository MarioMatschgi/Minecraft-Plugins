package at.mario.utilreloaded.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.mario.utilreloaded.Main;
import at.mario.utilreloaded.manager.ConfigManagers.DataManager;

public class PlayerRespawnListener implements Listener {
	
	@EventHandler
	public void playerRespawn(PlayerRespawnEvent e) {
		Player p = (Player) e.getPlayer();
		
		DataManager dm = new DataManager();
		
		
		boolean hasAuto = false;
		if (dm.getData().contains("Data." + p.getName() + ".autoNightvision")) {
			hasAuto = dm.getData().getBoolean("Data." + p.getName() + ".autoNightvision");
		}
		
		if (hasAuto) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999 * 20, 255, false, false));
				}
			}, 3);
		}
	}
}
