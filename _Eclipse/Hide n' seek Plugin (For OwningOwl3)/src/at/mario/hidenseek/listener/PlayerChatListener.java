package at.mario.hidenseek.listener;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import at.mario.hidenseek.Main;
import at.mario.hidenseek.manager.ConfigManagers.DataManager;
import at.mario.hidenseek.manager.ConfigManagers.MessagesManager;

public class PlayerChatListener
  implements Listener
{
  @EventHandler
  public void playerChat(AsyncPlayerChatEvent e)
  {
	  if (Main.getInstance().getConfig().getBoolean("Config.useChatSystem")) {
		DataManager dm = new DataManager();
	    MessagesManager mm = new MessagesManager();
	    
	    Player p = e.getPlayer();
	    String msg = mm.getMessages().getString("Messages.chatFormat").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%msg%", e.getMessage()).replace("%player%", p.getDisplayName());
	    
	    ConfigurationSection configSection = dm.getData().getConfigurationSection("Data.arenas");
	    if (configSection != null)
	    {
	      Boolean isInArena = Boolean.valueOf(false);
	      for (String key : configSection.getKeys(false)) {
	        if (Main.ArenaPlayer.containsKey(key))
	        {
	          if ((Main.ArenaPlayer.get(key)).contains(p))
	          {
	            e.setCancelled(true);
	            Main.sendToArenaOnly(key, msg);
	            isInArena = Boolean.valueOf(true);
	            break;
	          }
	        }
	        else if ((Main.SpectateArenaPlayer.containsKey(key)) && 
	          ((Main.SpectateArenaPlayer.get(key)).contains(p)))
	        {
	          e.setCancelled(true);
	          Main.sendToSpectatorsOnly(key, msg);
	          isInArena = Boolean.valueOf(true);
	          break;
	        }
	      }
	      if (!isInArena.booleanValue())
	      {
	        String msg2 = mm.getMessages().getString("Messages.chatFormatForNoneArena").replace("%prefix%", mm.getMessages().getString("Messages.prefix")).replace("%world%", p.getWorld().getName())
	          .replace("%msg%", e.getMessage()).replace("%player%", p.getDisplayName());
	        
	        e.setCancelled(true);
	        Main.sendToNormalOnly(msg2);
	      }
	    }
	  }
  }
}
