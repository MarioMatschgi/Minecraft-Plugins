/*    */ package me.solarshrieking.uGlow;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.UUID;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerJoinEvent;
/*    */ import org.bukkit.event.player.PlayerToggleSneakEvent;
/*    */ import org.bukkit.plugin.PluginManager;
/*    */ import org.bukkit.scheduler.BukkitScheduler;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PlayerListener
/*    */   implements Listener
/*    */ {
/*    */   private final Main plugin;
/*    */   private Commands commands;
/*    */   
/*    */   PlayerListener(Main plugin)
/*    */   {
/* 27 */     plugin.getServer().getPluginManager().registerEvents(this, plugin);
/* 28 */     this.plugin = plugin;
/* 29 */     this.commands = new Commands(plugin);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @EventHandler
/*    */   public void onPlayerJoin(PlayerJoinEvent e)
/*    */   {
/* 37 */     final Player player = e.getPlayer();
/* 38 */     String uuid = player.getUniqueId().toString();
/* 39 */     Boolean keepGlowOnLogin = Boolean.valueOf(this.plugin.getConfig().getBoolean("settings.keepGlowOnLogin"));
/*    */     
/* 41 */     if (!this.plugin.getConfig().contains("Players." + uuid)) {
/* 42 */       System.out.println("Player config not found, generating!");
/* 43 */       this.plugin.saveConfig();
/* 44 */       this.plugin.getConfig().set("Players." + uuid + ".glowing", Boolean.valueOf(false));
/* 45 */       this.plugin.getConfig().set("Players." + uuid + ".color", "NONE");
/* 46 */       this.plugin.saveConfig();
/* 47 */     } else if (keepGlowOnLogin.booleanValue())
/*    */     {
/* 49 */       Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
/*    */       {
/*    */ 
/* 52 */         public void run() { PlayerListener.this.commands.setGlowing(player, PlayerListener.this.commands.glowStatus(player)); } }, 20L);
/*    */ 
/*    */     }
/* 55 */     else if (!keepGlowOnLogin.booleanValue()) {
/* 56 */       this.commands.setGlowStatus(player, Boolean.valueOf(false));
/*    */     }
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void sprintToggle(PlayerToggleSneakEvent e)
/*    */   {
/* 63 */     final Player player = e.getPlayer();
/* 64 */     String uuid = player.getUniqueId().toString();
/* 65 */     Boolean isGlowing = Boolean.valueOf(this.plugin.getConfig().getBoolean("Players." + uuid + "glowing"));
/* 66 */     if (isGlowing.booleanValue())
/*    */     {
/*    */ 
/* 69 */       this.commands.setGlowing(player, Boolean.valueOf(false));
/*    */       
/*    */ 
/* 72 */       Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
/*    */       {
/*    */ 
/*    */ 
/*    */         public void run() {
/* 77 */           PlayerListener.this.commands.setGlowing(player, Boolean.valueOf(true)); } }, 20L);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/me/solarshrieking/uGlow/PlayerListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */