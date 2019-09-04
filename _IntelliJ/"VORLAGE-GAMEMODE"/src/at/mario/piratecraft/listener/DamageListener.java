package at.mario.piratecraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {
	
	@EventHandler
	public void onDmgByEnt(EntityDamageByEntityEvent e) {
		
	}
	
	@EventHandler
	public void onDmgByBlock(EntityDamageByBlockEvent e) {
		
	}
	
	@EventHandler
	public void onDmg(EntityDamageEvent e) {
		
	}
}
