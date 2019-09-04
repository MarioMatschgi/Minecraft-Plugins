package at.mario.gravity.inventories;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import at.mario.gravity.Main;
import at.mario.gravity.gamestates.IngameState;
import at.mario.gravity.manager.ConfigManagers.MessagesManager;

public class TeleporterInventory {

	public TeleporterInventory() { }
	
	private static TeleporterInventory instance = new TeleporterInventory();
	
	public static TeleporterInventory getInstance() {
		return instance;
	}

	public void newInventory(String arena, Player p) {
		MessagesManager mm = new MessagesManager();
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 54, mm.getMessages().getString("Messages.gui.teleporter.title").replace("%arena%", arena));

		ItemStack nothing = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
		ItemMeta nothingMeta = nothing.getItemMeta();
		nothingMeta.setDisplayName(" ");
		nothing.setItemMeta(nothingMeta);
		
		for (int i = 0; i < IngameState.ArenaUsedMaps.get(arena).size(); i++) {
			ConfigurationSection usedMap = IngameState.ArenaUsedMaps.get(arena).get(i);

			ItemStack map = new ItemStack(Material.MAP, 1);
			ItemMeta mapMeta = map.getItemMeta();
			mapMeta.setDisplayName(mm.getMessages().getString("Messages.gui.teleporter.mapFormat").replace("%map%", usedMap.getName()));
			map.setItemMeta(mapMeta);
			
			inv.setItem(i, map);
		}
		
		int playerHeadsPlaced = 0;
		for (int i = 1; i <= Main.getInstance().getConfig().getInt("Config.levelAmount"); i++) {
			for (Entry<Player, Integer> levelPlayer : IngameState.ArenaPlayerLevels.get(arena).entrySet()) {
				if (levelPlayer.getValue() == i) {
					if (!IngameState.ArenaFinishedPlayers.get(arena).containsKey(levelPlayer.getKey())) {
						ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
						SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
						skullMeta.setOwner(levelPlayer.getKey().getName());
						skullMeta.setDisplayName(mm.getMessages().getString("Messages.gui.teleporter.playerHeadFormat").replace("%player%", levelPlayer.getKey().getName()));
						skull.setItemMeta(skullMeta);
						
						inv.setItem(playerHeadsPlaced + IngameState.ArenaUsedMaps.get(arena).size(), skull);
						playerHeadsPlaced++;
					}
				}
			}
		}
		
		
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null) {
				inv.setItem(i, nothing);
			}
		}
		
		p.openInventory(inv);
	}
}
