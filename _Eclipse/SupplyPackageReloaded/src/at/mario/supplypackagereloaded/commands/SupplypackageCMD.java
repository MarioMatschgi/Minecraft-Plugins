package at.mario.supplypackagereloaded.commands;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import at.mario.supplypackagereloaded.Main;
import at.mario.supplypackagereloaded.manager.ConfigManagers.DataManager;
import at.mario.supplypackagereloaded.manager.ConfigManagers.MessagesManager;

public class SupplypackageCMD implements CommandExecutor {

	/*
	 *	/sp {call, flare, additem/add} {player} 
	 */
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		MessagesManager mm = new MessagesManager();
		//DataManager dm = new DataManager();
		
		if (args.length < 1) {
			// Not enough args
			
			sender.sendMessage(mm.getMessages().getString("Messages.command.notEnoughArgs").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
		} else {
			if (args[0].equalsIgnoreCase("call")) {
				// Call package direct
				
				/*
				 * 
				 * 
				 * 
				 * ToDO:
				 * CHECK FOR ARGS BEFORE PLAYER
				 * 
				 * 
				 * 
				 */
				if (sender instanceof Player) {
					spawnSupplypackage((Player) sender);
				} else {
					if (args.length >= 2) {
						Player player = Bukkit.getPlayer(args[1]);
						
						if (player != null && player.isOnline()) {
							spawnSupplypackage(player);
						} else 
							sender.sendMessage(mm.getMessages().getString("Messages.command.playerNotOnline").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%player%", args[1]));
					} else
						sender.sendMessage(mm.getMessages().getString("Messages.command.notEnoughArgs").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				}
			} else if (args[0].equalsIgnoreCase("flare")) {
				// Give flare torch before
				
				
			} else if (args[0].equalsIgnoreCase("additem") || args[0].equalsIgnoreCase("add")) {
				// Add item
				
				if (sender instanceof Player) {
					Player p = (Player) sender;
					
					if (args.length >= 2) {
						if (Main.isInteger(args[1])) {
							addSupplies(p.getItemInHand(), Main.parseInt(args[1]));
							sender.sendMessage(mm.getMessages().getString("Messages.command.successfullyAddedItem").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
						} else 
							sender.sendMessage(mm.getMessages().getString("Messages.command.enterAProbabilty").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
					} else
						sender.sendMessage(mm.getMessages().getString("Messages.command.notEnoughArgs").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
				} else 
					sender.sendMessage(ChatColor.RED+"You have to be a player!");
			} else {
				// Wrong args
				
				sender.sendMessage(mm.getMessages().getString("Messages.command.wrongArgs").replace("%prefix%", mm.getMessages().getString("Messages.prefix")));
			}
		}
		
		return true;
	}

	@SuppressWarnings("deprecation")
	public static void spawnSupplypackage(Player p) {
		MessagesManager mm = new MessagesManager();
		
		Location loc = p.getLocation();
		loc.setY(Main.getInstance().getConfig().getInt("Config.maxSpawnHeight"));
		
		FallingBlock fblock = p.getWorld().spawnFallingBlock(loc, Material.valueOf(Main.getInstance().getConfig().getString("Config.fallingBlockMaterial")), (byte) 0);
		fblock.setDropItem(false);
		fblock.setHurtEntities(false);
		fblock.setInvulnerable(true);
		
		Main.fallingBlocks.put(fblock, p);
		Main.fallingBlockIDs.put(fblock, Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				if (fblock.isDead()) {
					Location loc = fblock.getLocation();
					
					if (loc.getWorld().getBlockAt(loc).getType() != Material.valueOf(Main.getInstance().getConfig().getString("Config.fallingBlockMaterial")) && 
							loc.getWorld().getBlockAt(loc).getType() != Material.valueOf(Main.getInstance().getConfig().getString("Config.landedBlockMaterial"))) {
						loc.setY(loc.getY()+1);
					
						fblock.getLocation().getWorld().getBlockAt(loc).setType(Material.valueOf(Main.getInstance().getConfig().getString("Config.landedBlockMaterial")));

						Main.fallingBlocks.remove(fblock);
						Main.blocks.put(fblock.getLocation().getWorld().getBlockAt(loc), p);
						
						p.sendMessage(mm.getMessages().getString("Messages.successfullyLanded").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).
								replace("%x%", loc.getBlockX()+"").replace("%y%", loc.getBlockY()+"").replace("%z%", loc.getBlockZ()+""));
					}
					
					Bukkit.getScheduler().cancelTask(Main.fallingBlockIDs.get(fblock));

					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
						
						@Override
						public void run() {
							Main.blocks.put(fblock.getLocation().getBlock(), null);
						}
					}, 20 * Main.getInstance().getConfig().getInt("Config.blockProtection"));
					
					return;
				}
			}
		}, 0, Main.getInstance().getConfig().getInt("Config.plopUpdateRate")));
	}
	
	public static void giveSupplies(Player p) {
		DataManager dm = new DataManager();
		
		LinkedHashMap<ItemStack, Integer> lmap = new LinkedHashMap<ItemStack, Integer>(Main.getSupplies());
		for (int i = 0; i < lmap.size(); i++) {
			ItemStack item = dm.getData().getItemStack("Data.supplies.item_" + i).clone();
			Integer probability = dm.getData().getInt("Data.supplies.probability_" + i);
			
			if (new Random().nextDouble() < new Double(probability) / 100)
				p.getInventory().addItem(item);
		}
	}
	
	public static void addSupplies(ItemStack item, Integer probability) {
		if (item != null && item.getType() != Material.AIR) {
			HashMap<ItemStack, Integer> supplies = Main.getSupplies();
			supplies.put(item, probability);
			
			Main.setSupplies(supplies);
		}
	}
}
