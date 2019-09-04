package at.mario.hidenseek.listener;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.Roles;
import at.mario.hidenseek.Effects.ParticleEffect;
import at.mario.hidenseek.gamestates.EndingState;
import at.mario.hidenseek.gamestates.IngameState;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.manager.PackageSender;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;
import at.mario.hidenseek.manager.ConfigManagers.StatsManager;
import at.mario.hidenseek.scoreboards.GameScoreboard;

public class DamageListener implements Listener {
	
	@EventHandler
	public void onDmgByEnt(EntityDamageByEntityEvent e) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
		if (configSection != null) {
			for (String key : configSection.getKeys(false)) {
				if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
					if (e.getEntity() instanceof Player) {
						Player p = (Player) e.getEntity();
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
								return;
							}
						}
					}
				} else if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState) {
					e.setDamage(0);
					if (e.getEntity() instanceof Player) {
						
						Player p = (Player) e.getEntity();
						if (e.getDamager() instanceof Player) {
							Player damaged = (Player) e.getDamager();
							
							if (Roles.roles.get(damaged) == Roles.SEEKER) {
								if (Roles.roles.get(p) == Roles.HIDER) {
									if (!damaged.hasPotionEffect(PotionEffectType.BLINDNESS) || !damaged.hasPotionEffect(PotionEffectType.JUMP) || !damaged.hasPotionEffect(PotionEffectType.SLOW)) {
										List<Player> players = Main.ArenaPlayer.get(key);
										
										Roles.roles.remove(p);
										Roles.roles.put(p, Roles.SEEKER);
										
										StatsManager.setStat(damaged, "playersFound", StatsManager.getStats(damaged).getInt("playersFound") + 1);
										StatsManager.setStat(p, "gotFounded", StatsManager.getStats(p).getInt("gotFounded") + 1);
										
										PackageSender.sendTitle(p, mm.getMessages().getString("Messages.roleMessage.title.seeker"), mm.getMessages().getString("Messages.roleMessage.subtitle.seeker"), 
												Main.getInstance().getConfig().getInt("Config.roleMessage.fadeIn"), Main.getInstance().getConfig().getInt("Config.roleMessage.duration"), 
												Main.getInstance().getConfig().getInt("Config.roleMessage.fadeOut"));
										
										Main.sendToArenaOnly(key, mm.getMessages().getString("Messages.roleMessage.hiderFound.all").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seeker%", 
												damaged.getName()).replace("%hider%", p.getName()));
										damaged.sendMessage(mm.getMessages().getString("Messages.roleMessage.hiderFound.seeker").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seeker%", 
												damaged.getName()).replace("%hider%", p.getName()));
										p.sendMessage(mm.getMessages().getString("Messages.roleMessage.hiderFound.hider").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seeker%", 
												damaged.getName()).replace("%hider%", p.getName()));
										for (int i = 0; i < players.size(); i++) {
											Player player = players.get(i);
											GameScoreboard.setScoreboard(key, player);
										}
										ParticleEffect.LAVA.display((float) 0.2, 0, (float) 0.2, 10, 50, p.getLocation(), 15);
										
										p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Main.getInstance().getConfig().getInt("Config.newSeekerWaitDuration") * 20, 999999999, true, false));
										p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Main.getInstance().getConfig().getInt("Config.newSeekerWaitDuration") * 20, 100, true, false));
										
										return;
									} else {
										e.setCancelled(true);
										return;
									}
								} else {
									e.setCancelled(true);
									return;
								}
							} else {
								e.setCancelled(true);
								return;
							}
						} else {
							e.setCancelled(true);
							return;
						}
					} else {
						e.setCancelled(true);
						return;
					}
				} else if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
					if (e.getEntity() instanceof Player) {
						Player p = (Player) e.getEntity();
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDmgByBlock(EntityDamageByBlockEvent e) {
		DataManager dm = new DataManager();
		
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
			if (configSection != null) {
				for (String key : configSection.getKeys(false)) {
					if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
							}
						}
					} else if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState) {
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
							}
						}
					} else if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDmg(EntityDamageEvent e) {
		DataManager dm = new DataManager();
		
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
			if (configSection != null) {
				for (String key : configSection.getKeys(false)) {
					if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof LobbyState) {
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
							}
						}
					} else if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof IngameState) {
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
							}
						}
					} else if (Main.getInstance().getGameStateManager().getCurrentGameState(key) instanceof EndingState) {
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(p)) {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
}
