package at.mario.hidenseek.gamestates;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.Roles;
import at.mario.hidenseek.countdowns.GameCountdown;
import at.mario.hidenseek.countdowns.SeekerReleaseCountdown;
import at.mario.hidenseek.manager.PackageSender;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;
import at.mario.hidenseek.manager.ConfigManagers.StatsManager;

public class IngameState extends GameState {
	
	private GameCountdown gamecountdown;
	private SeekerReleaseCountdown seekerReleaseCountdown;
	public static HashMap<Player, String> seekerPassPlayer = new HashMap<Player, String>();
	public static HashMap<Player, Integer> possibilitiyPoints = new HashMap<Player, Integer>();
	
	public int totalPoints = 0;
	
	@Override
	public void start(String arena) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		seekerReleaseCountdown = new SeekerReleaseCountdown();
		seekerReleaseCountdown.run(arena);
		
		
		List<Player> players = Main.ArenaPlayer.get(arena);
		
		int erhöhung = 100;
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			if (seekerPassPlayer.containsKey(player)) {
				// Spieler hat Pass benutzt
				
				possibilitiyPoints.put(player, 100 * ((erhöhung + 100) / 100));
			} else {
				// Spieler hat keinen Pass benutzt
				
				possibilitiyPoints.put(player, 100);
			}
			
			totalPoints += possibilitiyPoints.get(player);
		}
		int idx = new Random().nextInt(totalPoints);
		
		int temp = 0;
		Player seeker = null;
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			temp += possibilitiyPoints.get(player);
			
			if (idx <= temp) {
				seeker = player;
				break;
			}
		}
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			if (Main.getInstance().getConfig().get("Config.gamemode.inGame") instanceof String) {
				if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("survival")) {
					player.setGameMode(GameMode.SURVIVAL);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("creative")) {
					player.setGameMode(GameMode.CREATIVE);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("adventure")) {
					player.setGameMode(GameMode.ADVENTURE);
				} else if (Main.getInstance().getConfig().getString("Config.gamemode.inGame").equalsIgnoreCase("spectator")) {
					player.setGameMode(GameMode.SPECTATOR);
				}
			} else {
				if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 0) {
					player.setGameMode(GameMode.SURVIVAL);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 1) {
					player.setGameMode(GameMode.CREATIVE);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 2) {
					player.setGameMode(GameMode.ADVENTURE);
				} else if (Main.getInstance().getConfig().getInt("Config.gamemode.inGame") == 3) {
					player.setGameMode(GameMode.SPECTATOR);
				}
			}
			
			Main.removeLobbyItems(player);
		}
		
		Location loc = seeker.getLocation();
		loc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + arena + ".spawn.world")));
		loc.setPitch((float) ((double) dm.getData().get("Data.arenas." + arena + ".spawn.pitch")));
		loc.setYaw((float) ((double) dm.getData().get("Data.arenas." + arena + ".spawn.yaw")));
		loc.setX(dm.getData().getDouble("Data.arenas." + arena + ".spawn.x"));
		loc.setY(dm.getData().getDouble("Data.arenas." + arena + ".spawn.y"));
		loc.setZ(dm.getData().getDouble("Data.arenas." + arena + ".spawn.z"));
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			
			player.teleport(loc);
			//Main.hideNameTag(player);
			
			if (player == seeker) {
				Main.arenaFistSeeker.put(arena, seeker);
				Roles.roles.put(player, Roles.SEEKER);
				StatsManager.setStat(player, "wasSeeker", StatsManager.getStats(player).getInt("wasSeeker") + 1);
				
				PackageSender.sendTitle(player, mm.getMessages().getString("Messages.roleMessage.title.seeker"), mm.getMessages().getString("Messages.roleMessage.subtitle.seeker"), 
						Main.getInstance().getConfig().getInt("Config.roleMessage.fadeIn"), Main.getInstance().getConfig().getInt("Config.roleMessage.duration"), Main.getInstance().getConfig().getInt("Config.roleMessage.fadeOut"));
				
				setupSeeker(player);
			} else {
				Roles.roles.put(player, Roles.HIDER);
				StatsManager.setStat(player, "wasHider", StatsManager.getStats(player).getInt("wasHider") + 1);
				
				PackageSender.sendTitle(player, mm.getMessages().getString("Messages.roleMessage.title.hider"), mm.getMessages().getString("Messages.roleMessage.subtitle.hider"), 
						Main.getInstance().getConfig().getInt("Config.roleMessage.fadeIn"), Main.getInstance().getConfig().getInt("Config.roleMessage.duration"), Main.getInstance().getConfig().getInt("Config.roleMessage.fadeOut"));
			}
		}
		
		totalPoints = 0;
		seekerPassPlayer = new HashMap<Player, String>();
		possibilitiyPoints = new HashMap<Player, Integer>();
		
		gamecountdown = new GameCountdown();
		gamecountdown.run(arena);
	}
	
	@Override
	public void stop(String arena) {
		if (gamecountdown.isRunning(arena)) {
			gamecountdown.cancel(arena);
		}
	}
	
	public void setupSeeker(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Main.getInstance().getConfig().getInt("Config.seekerWaitDuration") * 20, 255, false, false));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Main.getInstance().getConfig().getInt("Config.seekerWaitDuration") * 20, 255, false, false));
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Main.getInstance().getConfig().getInt("Config.seekerWaitDuration") * 20, 128, false, false));
		
		Main.giveIngameItems(p);
		
		//ParticleEffect.LAVA.display((float) 0.2, 0, (float) 0.2, 10, 50, p.getLocation(), 15);
		// Eventuell: 
		p.spawnParticle(Particle.LAVA, p.getLocation(), 10, 1.0, 0, 1.0);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Main.getInstance().getConfig().getInt("Config.maxGameLenght") * 60 * 20, Main.getInstance().getConfig().getInt("Config.seekerSpeedLevel"), false, false));
			}
		}, Main.getInstance().getConfig().getInt("Config.seekerWaitDuration") * 20);
	}
	
	public GameCountdown getGamecountdown() {
		return gamecountdown;
	}
}
