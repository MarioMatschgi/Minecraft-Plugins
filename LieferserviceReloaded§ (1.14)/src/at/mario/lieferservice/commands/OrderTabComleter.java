package at.mario.lieferservice.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class OrderTabComleter implements TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> list = new ArrayList<String>();
		
		if (args.length == 1) {
			for (Material mat : Material.values()) {
				String name = mat.name();

				String[] nameArr = name.split("\\.");
				if (nameArr.length > 0) {
					name = nameArr[nameArr.length-1];
				}
				
				list.add(name);
			}
		} else if (args.length == 3) {
			list.add("stacks");
			list.add("items");
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