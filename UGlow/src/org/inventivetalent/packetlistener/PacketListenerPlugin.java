/*    */ package org.inventivetalent.packetlistener;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.plugin.PluginManager;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ import org.inventivetalent.apihelper.APIManager;
/*    */ import org.inventivetalent.packetlistener.mcstats.MetricsLite;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketListenerPlugin
/*    */   extends JavaPlugin
/*    */ {
/* 38 */   private PacketListenerAPI packetListenerAPI = new PacketListenerAPI();
/*    */   
/*    */ 
/*    */   public void onLoad()
/*    */   {
/* 43 */     APIManager.registerAPI(this.packetListenerAPI, this);
/*    */   }
/*    */   
/*    */   public void onEnable()
/*    */   {
/* 48 */     if (!this.packetListenerAPI.injected) {
/* 49 */       getLogger().warning("Injection failed. Disabling...");
/* 50 */       Bukkit.getPluginManager().disablePlugin(this);
/* 51 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 55 */       MetricsLite metrics = new MetricsLite(this);
/* 56 */       if (metrics.start()) {
/* 57 */         getLogger().info("Metrics started");
/*    */       }
/*    */     }
/*    */     catch (Exception localException) {}
/*    */     
/*    */ 
/* 63 */     APIManager.initAPI(PacketListenerAPI.class);
/*    */   }
/*    */   
/*    */ 
/*    */   public void onDisable()
/*    */   {
/* 69 */     APIManager.disableAPI(PacketListenerAPI.class);
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/PacketListenerPlugin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */