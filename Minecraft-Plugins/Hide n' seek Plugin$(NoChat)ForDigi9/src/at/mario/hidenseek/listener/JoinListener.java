package at.mario.hidenseek.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.StatsManager;
import at.mario.hidenseek.pictureLogin.me.itsnathang.picturelogin.util.PictureWrapper;

public class JoinListener implements Listener {
	
	private Main plugin;
	private Player player;
	  
	public JoinListener(Main plugin) {
		this.plugin = plugin;
	}
	  
	@EventHandler(priority=EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent e) {
		DataManager dm = new DataManager();
		
		player = e.getPlayer();
		Player p = e.getPlayer();
		
		if (!dm.getData().contains("Data." + p.getName() + ".joinmes")) {
			dm.getData().set("Data." + p.getName() + ".joinmes", 0);
			dm.saveData();
		}
		if (!dm.getData().contains("Data." + p.getName() + ".seekerPasses")) {
			dm.getData().set("Data." + p.getName() + ".seekerPasses", 0);
			dm.saveData();
		}
		
		if (!StatsManager.hasStats(p)) {
			StatsManager.setStats(p, 0, 0, 0, 0, 0, 0);
		}

		dm.getData().set("Data." + p.getName() + ".position.world", p.getLocation().getWorld().getName());
		dm.getData().set("Data." + p.getName() + ".position.pitch", p.getLocation().getPitch());
		dm.getData().set("Data." + p.getName() + ".position.yaw", p.getLocation().getYaw());
		dm.getData().set("Data." + p.getName() + ".position.x", p.getLocation().getX());
		dm.getData().set("Data." + p.getName() + ".position.y", p.getLocation().getY());
		dm.getData().set("Data." + p.getName() + ".position.z", p.getLocation().getZ());
		dm.saveData();
		
		initialiseIMG();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				//Main.hideNameTag(p);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
					
					@Override
					public void run() {
						//Main.showNameTag(p);
					}
				}, 15 * 20);
			}
		}, 5 * 20);
	}
	  
	private void initialiseIMG() {
		PictureWrapper wrapper = new PictureWrapper(this.plugin, this.player);
		wrapper.sendNOImage();
	}
}
