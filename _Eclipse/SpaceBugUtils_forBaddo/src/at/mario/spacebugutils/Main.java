package at.mario.spacebugutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
	public final Logger logger = Logger.getLogger("Minecraft");
	private static Main plugin;
	
	private Map<String, Long> joined = new HashMap<String, Long>();
	private List<String> list = new LinkedList<String>();
	private String stars = "*********************************************************";
	
	private String configPath;
	private FileConfiguration config;
	private FileConfiguration outConfig;
	
	private List<String> badWords; private String _badWords = "List of filtered words";
	
	private void loadConfig(){
		configPath = this.getDataFolder().getAbsolutePath() + File.separator + "config.yml";
		config = YamlConfiguration.loadConfiguration(new File(configPath));
		outConfig = new YamlConfiguration();	
		
		if(config.contains(_badWords))
			badWords = config.getStringList(_badWords);
		else
			badWords = new ArrayList<String>();
		
		outConfig.set(_badWords, badWords);
		
		saveConfig(outConfig, configPath);
	}
	
	private void saveConfig(FileConfiguration config, String path)
	{
        try{config.save(path);}
        catch(IOException exception){logger.info("Unable to write to the configuration file at \"" + path + "\"");}
	}

	public void onEnable()
	{
		PluginDescriptionFile pdfFile = getDescription();
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
		getServer().getPluginManager().registerEvents(this, this);
		plugin = this;
		
		loadConfig();
	}

	public void onDisable()
	{
		PluginDescriptionFile pdfFile = getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disabled!");
		
		saveConfig();
	}
	
	@EventHandler
	public void onDispense(BlockDispenseEvent event)
	{
		Block block = event.getBlock();
		if(block.getState() instanceof Dispenser)
		{
			Dispenser dis = (Dispenser)block.getState();
			Inventory inv = dis.getInventory();
			for(int i = 0; i < inv.getSize(); i++)
			{
				ItemStack stack = inv.getItem(i);
				if(stack != null && stack.getAmount() < 0)
				{
					inv.setItem(i, new ItemStack(Material.AIR));
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onOpenInv(InventoryOpenEvent event)
	{
		for(int i = 0; i < event.getInventory().getSize(); i++)
		{
			ItemStack stack = event.getInventory().getItem(i);
			if(stack != null && stack.getAmount() <= 0)
			{
				event.getInventory().setItem(i, new ItemStack(Material.AIR));
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onClickInv(InventoryClickEvent event)
	{
		for(int i = 0; i < event.getInventory().getSize(); i++)
		{
			ItemStack stack = event.getInventory().getItem(i);
			if(stack != null && stack.getAmount() <= 0)
			{
				event.getInventory().setItem(i, new ItemStack(Material.AIR));
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event)
	{
		joined.put(event.getPlayer().getName(), System.currentTimeMillis());
		list.add(event.getPlayer().getName());
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent event)
	{
		joined.remove(event.getPlayer().getName());
		list.remove(event.getPlayer().getName());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		//onlinelongest
		if(cmd.getName().equalsIgnoreCase("onlinelongest"))
		{
			if(sender instanceof Player && !sender.hasPermission("spacebugutils.onlinelongest"))
				sender.sendMessage("You ain't got the perms foo!");
			else
			{
				long curTime = System.currentTimeMillis();
				for(String p : list)
				{
					//if this player is no longer online, remove from list
					if(Bukkit.getPlayer(p) == null)
					{
						list.remove(p);
						continue;
					}
					Integer t = (int) (curTime - joined.get(p))/1000;
					
					String time = "";
					if (t >= 3600)
					{
						Integer hours = t / 3600; //how many hours
						Integer min = (t - (hours * 3600))/60;// left over minutes
						time = hours.toString() + " hour(s) " + min.toString() + " minutes";
					}
					else if (t >= 60)
					{
						Integer min = t/60;
						time = min.toString() + " minutes";
					}
					else
						time = t.toString() + " seconds";
					sender.sendMessage(ChatColor.GREEN+p+ " " + time);
				}
			}
		}
		
		//silentTP
		else if(cmd.getName().equalsIgnoreCase("silenttp"))
		{
			if(!(sender instanceof Player))
				sender.sendMessage("This command can only be run by a player.");
			else
			{
				Player p = (Player) sender;
				if(!p.hasPermission("spacebugutils.silenttp")) {
					sender.sendMessage("You ain't got the perms foo!");
					
					return true;
				}
				
				switch (args.length)
				{
				case 1: //proper amount of players sent
					//make sure the target is online/real
					Player tar = Bukkit.getPlayer(args[0]);
					if(tar == null)
						p.sendMessage(ChatColor.RED+"Player not found.");
					else
					{
						tar.teleport(p.getLocation());
						p.sendMessage("Very sneaky");
					}
					break;
				default: //they screwed up.
					return false;
				}
			}
		}
		
		//sbureload
		else if(cmd.getName().equalsIgnoreCase("sbureload"))
		{
			//if is player, and doesn't have permission
			if((sender instanceof Player) && !(sender.hasPermission("spacebugutils.reload")))
					sender.sendMessage(ChatColor.DARK_RED+"No permission.");
			else
			{
				loadConfig();
 			}
		}
		
		//sbs
		else if(cmd.getName().equalsIgnoreCase("sbspectate"))
		{
			if((sender instanceof Player) && (sender.hasPermission("spacebugutils.spectate")))
			{
				Player p = (Player)sender;
				GameMode mode = (p.getGameMode() == GameMode.SURVIVAL) ? GameMode.SPECTATOR : GameMode.SURVIVAL;
				p.setGameMode(mode);
				p.sendMessage(ChatColor.GREEN+"You are now in "+ mode.toString());
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You are not allowed to use this command!");
			}
		}
		
		//sbnick
		else if(cmd.getName().equalsIgnoreCase("sbnick"))
		{
			if((sender instanceof Player) && (sender.hasPermission("spacebugutils.canusenick")))
			{
				if(args.length != 2)
					return false;
				Player target = Bukkit.getPlayer(args[0]);
				String nick = args[1];
				
				if(nick.length() > 15)
					sender.sendMessage(ChatColor.RED + "Nickname is too long!");
				
				else if(!target.hasPermission("spacebugutils.nickname") && !nick.equalsIgnoreCase("off"))
					sender.sendMessage(ChatColor.RED + target.getName() + " hasn't purchased a name change.");
				
				else
				{
					String nickcmd = "nick " + target.getName() + " " + nick;
					String pexcmd = "pex user " + target.getName() + " remove spacebugutils.nickname";
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), nickcmd);
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), pexcmd);
					
					String doneMsg = (nick.equalsIgnoreCase("off")) 
							? "Removed " + target.getName() + "'s nickname"
							: target.getName() + "'s nickname is now " + nick;
					
					sender.sendMessage(ChatColor.GREEN + doneMsg);
				}
				
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "You are not allowed to use this command!");
			}
		}
		
		else if(cmd.getName().equalsIgnoreCase("increasehomes")){
			if(!(sender instanceof Player) || (sender instanceof Player) && (sender.hasPermission("spacebugutils.increasehomes"))){
				if(args.length != 2)
					return false;
				Player p = Bukkit.getPlayer(args[0]);
				if(p == null)
					return false;
				String add = "pex user " + p.getName() + " add essentials.sethome.multiple.";
				String remove = "pex user " + p.getName() + " remove essentials.sethome.multiple.";
				switch (args[1]){
					case "two":
						runCommand(add + "two");
						break;
					case "three":
						runCommand(remove + "two");
						runCommand(add + "three");
						break;
					case "four":
						runCommand(remove + "three");
						runCommand(add + "four");
						break;
					case "five":
						runCommand(remove + "four");
						runCommand(add + "five");
						break;
					case "six":
						runCommand(remove + "five");
						runCommand(add + "six");
						break;
					case "seven":
						runCommand(remove + "six");
						runCommand(add + "seven");
						break;						
					case "eight":
						runCommand(remove + "seven");
						runCommand(add + "eight");
						break;
					case "nine":
						runCommand(remove + "eight");
						runCommand(add + "nine");
						break;
					case "ten":
						runCommand(remove + "nine");
						runCommand(add + "ten");
						break;						
				}
				
			}
			else{
				sender.sendMessage(ChatColor.RED + "You are not allowed to use this command!");
			}
		}
		
		else if(cmd.getName().equalsIgnoreCase("sayraw")){
			if(sender instanceof Player && !sender.hasPermission("spacebugutils.sayraw")){
				sender.sendMessage(ChatColor.RED + "You are not allowed to use this command!");
				return true;
			}
			if(args.length < 1){
				return false;
			}
			
			String message = "";
			for(String arg : args){
				message += arg + " ";
			}
			message = message.substring(0, message.length()-1);
			
			String colorMessage = ChatColor.translateAlternateColorCodes('&', message);
			for(Player p : Bukkit.getOnlinePlayers()){
				p.sendMessage(colorMessage.split("\\\\n"));
			}
		}
		
		else if(cmd.getName().equalsIgnoreCase("showAlts")){
			if(sender instanceof Player && !sender.hasPermission("spacebugutils.showalts")){
				sender.sendMessage(ChatColor.RED + "You are not allowed to use this command.");
			}
			else{
				HashMap<String, List<String>> map = new HashMap<String, List<String>>();
				for(Player p : Bukkit.getOnlinePlayers()){
					String ip = p.getAddress().getHostString();
					if(!map.containsKey(ip)){
						List<String> list = new ArrayList<String>();
						map.put(ip, list);
					}
					map.get(ip).add(p.getName());
				}
				sender.sendMessage(ChatColor.BLUE + "These players are currently logged in with the same IP");
				for(String key : map.keySet()){
					if(map.get(key).size() > 1){
						sender.sendMessage("    " + ChatColor.GREEN + key);
						for(String name : map.get(key)){
							sender.sendMessage("        " + ChatColor.YELLOW + name);
						}
					}
				}
			}
		}
		
		else if(cmd.getName().equalsIgnoreCase("namecolor")){
			if(!(sender instanceof Player) || args.length != 1)
				return false;
			Player p = (Player) sender;
			String color = args[0];
			if(color.matches("[0-9a-fA-F]") && color.length() == 2){
				//if(sender.hasPermission("spacebugutils." + color)){
					runCommand("pex user " + p.getName() + " prefix &" + color);
					sender.sendMessage(ChatColor.GREEN + "Your name's color has been changed.");
				//}
				//else{
					//sender.sendMessage(ChatColor.RED + "You don't have permission for this color");
				//}
			}
			else{
				sender.sendMessage(ChatColor.RED + "This is not an acceptable color code. Please enter 0-9 or a-f");
			}
		}
		
		return true;
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onClick(PlayerInteractEvent event)
	{
		Player p = event.getPlayer();
		if(p.hasPermission("spacebugutils.changespawner")) return;
		
		
		/* BEGIN NEW CODE: Replaced by Mario_Matschgi */
		if (isEgg(p.getInventory().getItemInMainHand()) || isEgg(p.getInventory().getItemInOffHand()))
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
				if(event.getClickedBlock().getType() == Material.SPAWNER)
					event.setCancelled(true);
		/* END NEW CODE: Replaced by Mario_Matschgi */
		
		
		/* OLD CODE: Replaced by Mario_Matschgi
		
		Material inHand = p.getInventory().getItemInMainHand().getType();
		Material offHand = p.getInventory().getItemInOffHand().getType();
		
		//prevent players changing mob spawners
		if(inHand == Material.MONSTER_EGG || inHand == Material.MONSTER_EGGS ||
		   offHand == Material.MONSTER_EGG || offHand == Material.MONSTER_EGGS)
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
				if(event.getClickedBlock().getType() == Material.SPAWNER)
					event.setCancelled(true);
		*/
	}
	
	
	
	/* BEGIN: NEW: added by Mario_Matschgi */
	private final boolean isEgg(ItemStack item)
	{
		String name = item == null ? "" : item.getType().toString();
		return (name.contains("MONSTER_EGG")) || (name.endsWith("SPAWN_EGG"));
	  }
	/* END NEW: added by Mario_Matschgi */
	
	
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player p = event.getPlayer();
		if (p.hasPermission("spacebugutils.nether")) return;
		Location to = event.getTo();
		if (to.getWorld().getEnvironment().equals(World.Environment.NETHER))
		{
			if (to.getBlockY() > 127)
			{
				p.performCommand("spawn");
				p.sendMessage(ChatColor.RED + "Going above the Nether ceiling is not allowed");
				this.logger.info("[SpaceBugUtils] "+ p.getName() + " was prevented from going above nether.");
				event.setTo(event.getFrom());
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onTP(PlayerTeleportEvent event)
	{
		Player p = event.getPlayer();
		if (p.hasPermission("spacebugutils.nether")) return;
		Location to = event.getTo();
		if (to.getWorld().getEnvironment().equals(World.Environment.NETHER))
		{
			if (to.getBlockY() > 127)
			{
				p.sendMessage(ChatColor.RED + "Going above the Nether ceiling is not allowed");
				this.logger.info("[SpaceBugUtils] "+ p.getName() + " was prevented from going above nether.");
				event.setTo(event.getFrom());
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent event)
	{
		//don't lower/filter mods/admins
		if(event.getPlayer().hasPermission("spacebugutils.chat")) return;
		String msg = event.getMessage();
		//loop over msg and count caps
		int caps = 0;
		int low = 0;
		for(Character c : msg.toCharArray())
		{
			if(Character.isUpperCase(c)) 
				caps++;
			else
				low++;
		}
		//if more than half the characters are caps, make them lower case
		if ((double) caps/low > 1 && msg.length() > 5)
			msg = msg.toLowerCase();
		
		for(String word : badWords){
			String replace = stars.substring(0, word.length());
			msg = msg.replace(word, replace);
		}
		
		event.setMessage(msg);
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onEntityTarget(EntityTargetLivingEntityEvent event){
		//prevent excessively large XP orbs from merging and overflowing, becoming negative and breaking items
		if(event.getEntity() instanceof ExperienceOrb){
			ExperienceOrb orb = (ExperienceOrb) event.getEntity();
			if(orb.getExperience() < 1)
				orb.setExperience(7);
		}
	}
	
	private void print(String p){System.out.println(p);}
	@SuppressWarnings("unused")
	private void print(int i){print(i + "");}
	public static JavaPlugin getInstance() {return plugin;}
	private void runCommand(String cmd){Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);}
}

