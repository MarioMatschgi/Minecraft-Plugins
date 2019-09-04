package at.mario.piratecraft.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class PiratecraftTabComleter implements TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> list = new ArrayList<String>();
		
		if (args.length == 1) {
			list.add("help");
			list.add("arena");
			// list.add("setmainlobby");
			// list.add("join");
			// list.add("joinme");
			// list.add("stats");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("arena")) {
				list.add("list");
				list.add("info");
				list.add("create");
				list.add("remove");
				list.add("setup");
			} else if (args[0].equalsIgnoreCase("stats")) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					list.add(player.getName());
				}
			}
		}
		/* Removes incongruous tab completions. */
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			String command = iterator.next().toLowerCase();
			if (!command.startsWith(args[args.length - 1].toLowerCase())) {
				iterator.remove();
			}
		}
		return list;
	}
}