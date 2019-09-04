package at.mario.utilreloaded.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.mario.utilreloaded.Main;
import at.mario.utilreloaded.manager.ConfigManagers.DataManager;
import at.mario.utilreloaded.manager.ConfigManagers.MessagesManager;

public class NightvisionCMD implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				// Effekt geben
				p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999 * 20, 255, false, false));
				p.sendMessage(mm.getMessages().getString("Messages.command.nightvision.successSelf").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
			} else if (args.length > 0) {
				if (args[0].equalsIgnoreCase("help")) {
					CMDInfo(p);
				}
				
				else if (args[0].equalsIgnoreCase("auto")) {
					if (args.length > 1) {
						Player player = Bukkit.getPlayer(args[1]);
						
						if (player != null && player.isOnline()) {
							boolean hasAuto = false;
							if (dm.getData().contains("Data." + player.getName() + ".autoNightvision")) {
								hasAuto = dm.getData().getBoolean("Data." + player.getName() + ".autoNightvision");
							}
							dm.getData().set("Data." + player.getName() + ".autoNightvision", !hasAuto);
							dm.saveData();
							
							if (!hasAuto) {
								player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999 * 20, 255, false, false));
							} else {
								player.removePotionEffect(PotionEffectType.NIGHT_VISION);
							}

							p.sendMessage(mm.getMessages().getString("Messages.command.nightvision.successOtherAuto").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
									replace("%autonightvision%", !hasAuto+"").replace("%player%", player.getName()));
						}
					} else {
						boolean hasAuto = false;
						if (dm.getData().contains("Data." + p.getName() + ".autoNightvision")) {
							hasAuto = dm.getData().getBoolean("Data." + p.getName() + ".autoNightvision");
						}
						dm.getData().set("Data." + p.getName() + ".autoNightvision", !hasAuto);
						dm.saveData();

						p.sendMessage(mm.getMessages().getString("Messages.command.nightvision.successSelfAuto").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
								replace("%autonightvision%", !hasAuto+""));
					}
				}
				
				else if (args[0].equalsIgnoreCase("remove")) {
					if (args.length > 1) {
						Player player = Bukkit.getPlayer(args[1]);
						
						if (player != null && player.isOnline()) {
							player.removePotionEffect(PotionEffectType.NIGHT_VISION);
							p.sendMessage(mm.getMessages().getString("Messages.command.nightvision.successOtherRemoved").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
									replace("%player%", player.getName()));
						}
					} else {
						p.removePotionEffect(PotionEffectType.NIGHT_VISION);
						p.sendMessage(mm.getMessages().getString("Messages.command.nightvision.successSelfRemoved").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					}
				} 
				
				else {
					int duration = 99999;
					Player player = Bukkit.getPlayer(args[0]);
					
					if (args.length > 1) {
						if (Main.isInteger(args[1])) {
							duration = Main.parseInt(args[1]);
						}
					}
					
					if (player != null && player.isOnline()) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, duration * 20, 255, false, false));
						if (duration == 99999) {
							p.sendMessage(mm.getMessages().getString("Messages.command.nightvision.successOther").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
									replace("%player%", player.getName()));
						} else {
							p.sendMessage(mm.getMessages().getString("Messages.command.nightvision.successOtherDuration").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
									replace("%duration%", duration+"").replace("%player%", player.getName()));
						}
					} else {
						if (Main.isInteger(args[0])) {
							duration = Main.parseInt(args[0]);
							p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, duration * 20, 255, false, false));
							p.sendMessage(mm.getMessages().getString("Messages.command.nightvision.successSelfDuration").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
									replace("%duration%", duration+""));
						}
					}
				}
			}
		} else {
			sender.sendMessage("§cYou have to be a player");
		}
		
		return true;
	}

	public static void CMDInfo(Player p) {
		p.sendMessage("§f--------§bUtil§3Reloaddedf-------------------------");
		p.sendMessage("§6/nightvision §f| §eThe main command");
		p.sendMessage("§6Aliasses§f: §env");
		p.sendMessage(" ");
		p.sendMessage("§6/nightvision §f| §eGives you nightvision till you die");  // Geht
		p.sendMessage("§6/nightvision %duration% §f| §eGives you nightvision for %duration% seconds");  // Geht
		p.sendMessage("§6/nightvision help §f| §eDisplays this message");  // Geht
		p.sendMessage(" ");
		p.sendMessage("§6/nightvision auto §f| §eToggles the automation mode");
		p.sendMessage("§6/nightvision auto %player% §f| §eToggles the automation mode for %player%");
		p.sendMessage(" ");
		p.sendMessage("§6/nightvision remove §f| §eRemoves nightvision");  // Geht
		p.sendMessage("§6/nightvision remove %player% §f| §eRemoves nightvision for %player%");  // Geht
		p.sendMessage(" ");
		p.sendMessage("§6/nightvision %player% §f| §eGives %player% nightvision till he dies");  // Geht
		p.sendMessage("§6/nightvision %player% %duration% §f| §eGives %player% nightvision for %duration% seconds");  // Geht
		p.sendMessage("§f-----------------------------------------------");
	}
}
