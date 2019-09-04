package at.mario.lobby.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class LobbyTabCompleter implements TabCompleter {

	/*
	 * (non-Javadoc)
	 * @see org.bukkit.command.TabCompleter#onTabComplete(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> list = new ArrayList<String>();
		/* List of all available broadcast types. */
		if (args.length == 1) {
			list.add("help");
			list.add("setspawn");
			list.add("setloc1");
			list.add("setloc2");
			list.add("wand");
			list.add("sorttp");
			list.add("addtp");
			list.add("removetp");
			list.add("reload");
			list.add("setDailyreward");
			list.add("removeDailyreward");
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