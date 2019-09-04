package at.mario.hidenseek.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Particle;
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
import at.mario.hidenseek.countdowns.GameCountdown;
import at.mario.hidenseek.gamestates.EndingState;
import at.mario.hidenseek.gamestates.IngameState;
import at.mario.hidenseek.gamestates.LobbyState;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;
import at.mario.hidenseek.manager.ConfigManagers.StatsManager;
import at.mario.hidenseek.scoreboards.GameScoreboard;

public class DamageListener implements Listener {
	
	@SuppressWarnings("deprecation")
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
						
						Player hider = (Player) e.getEntity();
						if (Main.ArenaPlayer.containsKey(key)) {
							if (Main.ArenaPlayer.get(key).contains(hider)) {
								if (e.getDamager() instanceof Player) {
									Player seeker = (Player) e.getDamager();
									if (Main.ArenaPlayer.containsKey(key)) {
										if (Main.ArenaPlayer.get(key).contains(hider)) {
											if (Roles.roles.get(seeker) == Roles.SEEKER) {
												if (Roles.roles.get(hider) == Roles.HIDER) {
													if (!seeker.hasPotionEffect(PotionEffectType.BLINDNESS) || !seeker.hasPotionEffect(PotionEffectType.JUMP) || !seeker.hasPotionEffect(PotionEffectType.SLOW)) {
														if (seeker.getItemInHand().getItemMeta().getDisplayName().equals(mm.getMessages().get("Messages.ingameitems.seekerStickItem.name"))) {
															List<Player> players = Main.ArenaPlayer.get(key);

															List<Player> hiders = new ArrayList<Player>();
															for (int i = 0; i < players.size(); i++) {
																Player player = players.get(i);
																if (Roles.roles.get(player) == Roles.HIDER) {
																	hiders.add(player);
																}
															}

															Roles.roles.remove(hider);
															Roles.roles.put(hider, Roles.SEEKER);
															if (hiders.size() - 1 == 0) {
																Main.arenaLastHider.put(key, hider);
															}
															
															HashMap<Player, Integer> tmp = new HashMap<Player, Integer>();
															if (Main.arenaSeekerFoundPlayer.containsKey(key) && Main.arenaSeekerFoundPlayer.get(key) != null &&
																	Main.arenaSeekerFoundPlayer.containsKey(key) && Main.arenaSeekerFoundPlayer.get(key) != null) {

																tmp = Main.arenaSeekerFoundPlayer.get(key);
																tmp.put(seeker, tmp.get(seeker) + 1);
															} else {
																tmp.put(seeker, 1);
															}
															Main.arenaSeekerFoundPlayer.put(key, tmp);
															
															tmp = new HashMap<Player, Integer>();
															if (Main.arenaHiderFoundAfterSeconds.containsKey(key) && Main.arenaHiderFoundAfterSeconds.get(key) != null) 
																tmp = Main.arenaHiderFoundAfterSeconds.get(key);
															tmp.put(hider, Main.getInstance().getConfig().getInt("Config.maxGameLenght") * 60 - GameCountdown.getSeconds().get(key));
															Main.arenaHiderFoundAfterSeconds.put(key, tmp);
															
															StatsManager.setStat(seeker, "playersFound", StatsManager.getStats(seeker).getInt("playersFound") + 1);
															StatsManager.setStat(hider, "gotFounded", StatsManager.getStats(hider).getInt("gotFounded") + 1);
															
															/*PackageSender.sendTitle(hider, mm.getMessages().getString("Messages.roleMessage.title.seeker"), mm.getMessages().getString("Messages.roleMessage.subtitle.seeker"), 
																	Main.getInstance().getConfig().getInt("Config.roleMessage.fadeIn"), Main.getInstance().getConfig().getInt("Config.roleMessage.duration"), 
																	Main.getInstance().getConfig().getInt("Config.roleMessage.fadeOut"));*/
															
															Main.sendToArenaOnly(key, mm.getMessages().getString("Messages.roleMessage.hiderFound.all").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seeker%", 
																	seeker.getName()).replace("%hider%", hider.getName()));
															seeker.sendMessage(mm.getMessages().getString("Messages.roleMessage.hiderFound.seeker").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seeker%", 
																	seeker.getName()).replace("%hider%", hider.getName()));
															hider.sendMessage(mm.getMessages().getString("Messages.roleMessage.hiderFound.hider").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%seeker%", 
																	seeker.getName()).replace("%hider%", hider.getName()));
															for (int i = 0; i < players.size(); i++) {
																Player player = players.get(i);
																GameScoreboard.setScoreboard(key, player);
															}
															//ParticleEffect.LAVA.display((float) 0.2, 0, (float) 0.2, 10, 50, p.getLocation(), 15);
															// Eventuell: 
															hider.spawnParticle(Particle.LAVA, hider.getLocation(), 10, 1.0, 0, 1.0);

															hider.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Main.getInstance().getConfig().getInt("Config.seekerWaitDuration") * 20, 255, false, false));
															hider.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Main.getInstance().getConfig().getInt("Config.seekerWaitDuration") * 20, 255, false, false));
															hider.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Main.getInstance().getConfig().getInt("Config.seekerWaitDuration") * 20, 128, false, false));

															Main.giveIngameItems(hider);
															
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
										}
									}
								}
							}
						}
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
