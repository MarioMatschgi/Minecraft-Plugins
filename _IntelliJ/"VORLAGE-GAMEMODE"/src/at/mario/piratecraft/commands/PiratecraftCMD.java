package at.mario.piratecraft.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.mario.piratecraft.manager.ConfigManagers.DataManager;
import at.mario.piratecraft.manager.ConfigManagers.MessagesManager;

public class PiratecraftCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// DataManager dm = new DataManager();
		// MessagesManager mm = new MessagesManager();
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (args.length == 0) {
				CMDInfo(p);
			} else {
				if (args.length == 1) {
					ArenaCMDInfo(p);
				}
			}
		} else {
			sender.sendMessage("§cYou have to be a player");
		}
		
		return false;
	}
	
	public void ArenaCMDInfo(Player p) {
		p.sendMessage("--------§bPirate§3Craft§f--(/§apiratecraft arena§f)----------------");
		p.sendMessage("§6/piratecraft arena §f| §eThe main subcommand");
		p.sendMessage(" ");
		p.sendMessage("§f------------------------------------------------------");
	}

	public static void CMDInfo(Player p) {
		p.sendMessage("§f--------§bPirate§3Craft§f----------------------------------------");
		p.sendMessage("§6/piratecraft §f| §eThe main command");
		p.sendMessage("§6Aliasses§f: §epc§f, §epcr§f, §epiratecraftreloaded");
		p.sendMessage(" ");
		p.sendMessage("§6/piratecraft help §f| §eShows you this info");
		p.sendMessage("§6/piratecraft arena help§f| §eShows you the info of the \"/arena\" subcommand");
		p.sendMessage("§f------------------------------------------------------");
	}

	public void mapInfo(String[] args, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		
		String bounds = "  Position 1: ";
		Location pos1 = p.getLocation();
		
		if (dm.getData().contains("Data.maps." + args[1] + ".bounds.loc1")) {
			pos1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + args[1] + ".bounds.loc1.world")));
			pos1.setPitch((float) 0.0);
			pos1.setYaw((float) 0.0);
			pos1.setX(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc1.x"));
			pos1.setY(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc1.y"));
			pos1.setZ(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc1.z"));
			
			bounds = "  Position 1: " + "§nl    X: " + (int) pos1.getX() + " Y: " + (int) pos1.getY() + " Z: " + (int) pos1.getZ();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		Location pos2 = p.getLocation();
		if (dm.getData().contains("Data.maps." + args[1] + ".bounds.loc2")) {
			pos2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + args[1] + ".bounds.loc2.world")));
			pos2.setPitch((float) 0.0);
			pos2.setYaw((float) 0.0);
			pos2.setX(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc2.x"));
			pos2.setY(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc2.y"));
			pos2.setZ(dm.getData().getDouble("Data.maps." + args[1] + ".bounds.loc2.z"));
			
			bounds = bounds + "§nl  Position 2: " + "§nl    X: " + (int) pos2.getX() + " Y: " + (int) pos2.getY() + " Z: " + (int) pos2.getZ();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		
		Location spawnloc = p.getLocation();
		String spawn = "";
		if (dm.getData().contains("Data.maps." + args[1] + ".spawn")) {
			spawnloc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.maps." + args[1] + ".spawn.world")));
			spawnloc.setPitch(((Number) dm.getData().get("Data.maps." + args[1] + ".spawn.pitch")).floatValue());
			spawnloc.setYaw(((Number) dm.getData().get("Data.maps." + args[1] + ".spawn.yaw")).floatValue());
			spawnloc.setX(dm.getData().getDouble("Data.maps." + args[1] + ".spawn.x"));
			spawnloc.setY(dm.getData().getDouble("Data.maps." + args[1] + ".spawn.y"));
			spawnloc.setZ(dm.getData().getDouble("Data.maps." + args[1] + ".spawn.z"));
			
			spawn = "  X: " + (int) spawnloc.getX() + " Y: " + (int) spawnloc.getY() + " Z: " + (int) spawnloc.getZ() + "§nl  Yaw: " + (int) spawnloc.getYaw() + "§nl  Pitch: " + (int) spawnloc.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}

		p.sendMessage("--------§2Gravity§f: §aArena§f-----------------------------------");
		List<String> list = mm.getMessages().getStringList("Messages.command.maps.info");
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);
			
			p.sendMessage(string.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%map%", args[1]).replace("%worldname%", 
					dm.getData().getString("Data.maps." + args[1] + ".world")).replace("%bounds%", bounds.replace("§nl", "\n")).replace("%spawn%", 
							spawn.replace("§nl", "\n")));
		}
		p.sendMessage("§f------------------------------------------------------");
	}

	public void ArenaInfo(String[] args, Player p) {
		DataManager dm = new DataManager();
		MessagesManager mm = new MessagesManager();
		
		
		String lobbyspawn = " ";
		if (dm.getData().contains("Data.arenas." + args[1] + ".lobbyspawn")) {
			
			lobbyspawn = "  X: " + dm.getData().getDouble("Data.arenas." + args[1] + ".lobbyspawn.x") + " Y: " + dm.getData().getDouble("Data.arenas." + args[1] + ".lobbyspawn.y") + " Z: " + dm.getData().getDouble("Data.arenas." + args[1] + 
					".lobbyspawn.z") + "§nl  Yaw: " + (int) ((float) dm.getData().get("Data.arenas." + args[1] + ".lobbyspawn.yaw")) + "§nl  Pitch: " + (int) ((float) dm.getData().get("Data.arenas." + args[1] + ".lobbyspawn.pitch"));
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		
		String bounds = "  Position 1: ";
		Location pos1 = p.getLocation();
		
		if (dm.getData().contains("Data.arenas." + args[1] + ".bounds.loc1")) {
			pos1.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + args[1] + ".bounds.loc1.world")));
			pos1.setPitch((float) dm.getData().get("Data.arenas." + args[1] + ".bounds.loc1.pitch"));
			pos1.setYaw((float) dm.getData().get("Data.arenas." + args[1] + ".bounds.loc1.yaw"));
			pos1.setX(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc1.x"));
			pos1.setY(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc1.y"));
			pos1.setZ(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc1.z"));
			
			bounds = "  Position 1: " + "§nl    X: " + (int) pos1.getX() + " Y: " + (int) pos1.getY() + " Z: " + (int) pos1.getZ() + "§nl    Yaw: " + (int) pos1.getYaw() + "§nl    Pitch: " + (int) pos1.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		Location pos2 = p.getLocation();
		if (dm.getData().contains("Data.arenas." + args[1] + ".bounds.loc2")) {
			pos2.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + args[1] + ".bounds.loc2.world")));
			pos2.setPitch((float) dm.getData().get("Data.arenas." + args[1] + ".bounds.loc2.pitch"));
			pos2.setYaw((float) dm.getData().get("Data.arenas." + args[1] + ".bounds.loc2.yaw"));
			pos2.setX(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc2.x"));
			pos2.setY(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc2.y"));
			pos2.setZ(dm.getData().getDouble("Data.arenas." + args[1] + ".bounds.loc2.z"));
			
			bounds = bounds + "§nl  Position 2: " + "§nl    X: " + (int) pos2.getX() + " Y: " + (int) pos2.getY() + " Z: " + (int) pos2.getZ() + "§nl    Yaw: " + (int) pos2.getYaw() + "§nl    Pitch: " + (int) pos2.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}
		
		Location spawnloc = p.getLocation();
		String spawn = "";
		if (dm.getData().contains("Data.arenas." + args[1] + ".spawn")) {
			spawnloc.setWorld(Bukkit.getWorld(dm.getData().getString("Data.arenas." + args[1] + ".spawn.world")));
			spawnloc.setPitch((float) dm.getData().get("Data.arenas." + args[1] + ".spawn.pitch"));
			spawnloc.setYaw((float) dm.getData().get("Data.arenas." + args[1] + ".spawn.yaw"));
			spawnloc.setX(dm.getData().getDouble("Data.arenas." + args[1] + ".spawn.x"));
			spawnloc.setY(dm.getData().getDouble("Data.arenas." + args[1] + ".spawn.y"));
			spawnloc.setZ(dm.getData().getDouble("Data.arenas." + args[1] + ".spawn.z"));
			
			spawn = "  X: " + (int) spawnloc.getX() + " Y: " + (int) spawnloc.getY() + " Z: " + (int) spawnloc.getZ() + "§nl  Yaw: " + (int) spawnloc.getYaw() + "§nl  Pitch: " + (int) spawnloc.getPitch();
		} else {
			p.sendMessage(mm.getMessages().getString("Messages.command.arena.arenaNotFound").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]));
			return;
		}

		p.sendMessage("--------§2Hide n' seek§f: §aArena§f------------------------------");
		List<String> list = mm.getMessages().getStringList("Messages.command.arena.info");
		for (int i = 0; i < list.size(); i++) {
			String string = list.get(i);
			
			p.sendMessage(string.replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%arena%", args[1]).replace("%worldname%", 
					dm.getData().getString("Data.arenas." + args[1] + ".world")).replace("%lobbyspawn%", lobbyspawn.replace("§nl", "\n")).replace("%bounds%", bounds.replace("§nl", "\n")).replace("%spawn%", 
							spawn.replace("§nl", "\n")));
		}
		p.sendMessage("§f------------------------------------------------------");
	}
}
