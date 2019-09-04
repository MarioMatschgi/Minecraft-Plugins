package at.mario.lobby;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilGUI
{
  private Player player;
  @SuppressWarnings("unused")
private AnvilClickEventHandler handler;
  private static Class<?> BlockPosition;
  private static Class<?> PacketPlayOutOpenWindow;
  private static Class<?> ContainerAnvil;
  private static Class<?> ChatMessage;
  private static Class<?> EntityHuman;
  @SuppressWarnings({ "unchecked", "rawtypes" })
private HashMap<AnvilSlot, ItemStack> items = new HashMap();
  private Inventory inv;
  private Listener listener;
  
  private void loadClasses()
  {
    BlockPosition = NMSManager.get().getNMSClass("BlockPosition");
    PacketPlayOutOpenWindow = NMSManager.get().getNMSClass("PacketPlayOutOpenWindow");
    ContainerAnvil = NMSManager.get().getNMSClass("ContainerAnvil");
    EntityHuman = NMSManager.get().getNMSClass("EntityHuman");
    ChatMessage = NMSManager.get().getNMSClass("ChatMessage");
  }
  
  public AnvilGUI(final Player player, final AnvilClickEventHandler handler)
  {
    loadClasses();
    this.player = player;
    this.handler = handler;
    
    this.listener = new Listener()
    {
      @EventHandler
      public void onInventoryClick(InventoryClickEvent event)
      {
        if ((event.getWhoClicked() instanceof Player)) {
          if (event.getInventory().equals(AnvilGUI.this.inv))
          {
            event.setCancelled(true);
            
            ItemStack item = event.getCurrentItem();
            int slot = event.getRawSlot();
            String name = "";
            if ((item != null) && 
              (item.hasItemMeta()))
            {
              ItemMeta meta = item.getItemMeta();
              if (meta.hasDisplayName()) {
                name = meta.getDisplayName();
              }
            }
            AnvilGUI.AnvilClickEvent clickEvent = new AnvilGUI.AnvilClickEvent(AnvilGUI.this, AnvilGUI.AnvilSlot.bySlot(slot), name);
            
            handler.onAnvilClick(clickEvent);
            if (clickEvent.getWillClose()) {
              event.getWhoClicked().closeInventory();
            }
            if (clickEvent.getWillDestroy()) {
              AnvilGUI.this.destroy();
            }
          }
        }
      }
      
      @EventHandler
      public void onInventoryClose(InventoryCloseEvent event)
      {
        if ((event.getPlayer() instanceof Player))
        {
          Inventory inv = event.getInventory();
          player.setLevel(player.getLevel() - 1);
          if (inv.equals(AnvilGUI.this.inv))
          {
            inv.clear();
            AnvilGUI.this.destroy();
          }
        }
      }
      
      @EventHandler
      public void onPlayerQuit(PlayerQuitEvent event)
      {
        if (event.getPlayer().equals(AnvilGUI.this.getPlayer()))
        {
          player.setLevel(player.getLevel() - 1);
          AnvilGUI.this.destroy();
        }
      }
    };
    Bukkit.getPluginManager().registerEvents(this.listener, Main.getInstance());
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
  
  public void setSlot(AnvilSlot slot, ItemStack item)
  {
    this.items.put(slot, item);
  }
  
  @SuppressWarnings("rawtypes")
public void open()
    throws IllegalAccessException, InvocationTargetException, InstantiationException
  {
    this.player.setLevel(this.player.getLevel() + 1);
    try
    {
      Object p = NMSManager.get().getHandle(this.player);
      
      Object container = ContainerAnvil.getConstructor(new Class[] { NMSManager.get().getNMSClass("PlayerInventory"), NMSManager.get().getNMSClass("World"), BlockPosition, EntityHuman }).newInstance(new Object[] { NMSManager.get().getPlayerField(this.player, "inventory"), NMSManager.get().getPlayerField(this.player, "world"), BlockPosition.getConstructor(new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE }).newInstance(new Object[] { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) }), p });
      NMSManager.get().getField(NMSManager.get().getNMSClass("Container"), "checkReachable").set(container, Boolean.valueOf(false));
      
      Object bukkitView = NMSManager.get().invokeMethod("getBukkitView", container);
      this.inv = ((Inventory)NMSManager.get().invokeMethod("getTopInventory", bukkitView));
      for (AnvilSlot slot : this.items.keySet()) {
        this.inv.setItem(slot.getSlot(), (ItemStack)this.items.get(slot));
      }
      int c = ((Integer)NMSManager.get().invokeMethod("nextContainerCounter", p)).intValue();
      
      Object chatMessageConstructor = ChatMessage.getConstructor(new Class[] { String.class, Object[].class });
      Object playerConnection = NMSManager.get().getPlayerField(this.player, "playerConnection");
      Object packet = PacketPlayOutOpenWindow.getConstructor(new Class[] { Integer.TYPE, String.class, NMSManager.get().getNMSClass("IChatBaseComponent"), Integer.TYPE }).newInstance(new Object[] { Integer.valueOf(c), "minecraft:anvil", ((Constructor)chatMessageConstructor).newInstance(new Object[] { "Repairing", new Object[0] }), Integer.valueOf(0) });
      
      Method sendPacket = NMSManager.get().getMethod("sendPacket", playerConnection.getClass(), new Class[] { PacketPlayOutOpenWindow });
      sendPacket.invoke(playerConnection, new Object[] { packet });
      
      Field activeContainerField = NMSManager.get().getField(EntityHuman, "activeContainer");
      if (activeContainerField != null)
      {
        activeContainerField.set(p, container);
        
        NMSManager.get().getField(NMSManager.get().getNMSClass("Container"), "windowId").set(activeContainerField.get(p), Integer.valueOf(c));
        
        NMSManager.get().getMethod("addSlotListener", activeContainerField.get(p).getClass(), new Class[] { p.getClass() }).invoke(activeContainerField.get(p), new Object[] { p });
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void destroy()
  {
    this.player = null;
    this.handler = null;
    this.items = null;
    
    HandlerList.unregisterAll(this.listener);
    
    this.listener = null;
  }
  
  public static enum AnvilSlot
  {
    INPUT_LEFT(0),  INPUT_RIGHT(1),  OUTPUT(2);
    
    private int slot;
    
    private AnvilSlot(int slot)
    {
      this.slot = slot;
    }
    
    public static AnvilSlot bySlot(int slot)
    {
      AnvilSlot[] arrayOfAnvilSlot;
      int j = (arrayOfAnvilSlot = values()).length;
      for (int i = 0; i < j; i++)
      {
        AnvilSlot anvilSlot = arrayOfAnvilSlot[i];
        if (anvilSlot.getSlot() == slot) {
          return anvilSlot;
        }
      }
      return null;
    }
    
    public int getSlot()
    {
      return this.slot;
    }
  }
  
  public static abstract interface AnvilClickEventHandler
  {
    public abstract void onAnvilClick(AnvilGUI.AnvilClickEvent paramAnvilClickEvent);
  }
  
  public class AnvilClickEvent
  {
    private AnvilGUI.AnvilSlot slot;
    private String name;
    private boolean close = true;
    private boolean destroy = true;
    
    public AnvilClickEvent(AnvilGUI anvilGUI, AnvilGUI.AnvilSlot slot, String name)
    {
      this.slot = slot;
      this.name = name;
    }
	
	public AnvilGUI.AnvilSlot getSlot()
    {
      return this.slot;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public boolean getWillClose()
    {
      return this.close;
    }
    
    public void setWillClose(boolean close)
    {
      this.close = close;
    }
    
    public boolean getWillDestroy()
    {
      return this.destroy;
    }
    
    public void setWillDestroy(boolean destroy)
    {
      this.destroy = destroy;
    }
  }
}
