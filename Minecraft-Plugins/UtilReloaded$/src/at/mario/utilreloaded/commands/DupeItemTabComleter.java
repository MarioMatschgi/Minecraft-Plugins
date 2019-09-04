package at.mario.utilreloaded.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class DupeItemTabComleter implements TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> list = new ArrayList<String>();
		
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