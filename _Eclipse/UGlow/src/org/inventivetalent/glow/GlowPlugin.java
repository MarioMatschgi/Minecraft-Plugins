/*    */ package org.inventivetalent.glow;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.inventivetalent.apihelper.APIManager;
/*    */ import org.inventivetalent.glow.mcstats.MetricsLite;
/*    */ 
/*    */ public class GlowPlugin extends org.bukkit.plugin.java.JavaPlugin
/*    */ {
/*    */   static GlowPlugin instance;
/* 10 */   GlowAPI glowAPI = new GlowAPI();
/*    */   
/*    */   public void onLoad()
/*    */   {
/* 14 */     instance = this;
/*    */     
/* 16 */     APIManager.registerAPI(this.glowAPI, this);
/*    */   }
/*    */   
/*    */   public void onEnable()
/*    */   {
/*    */     try {
/* 22 */       MetricsLite metrics = new MetricsLite(this);
/* 23 */       if (metrics.start()) {
/* 24 */         getLogger().info("Metrics started");
/*    */       }
/*    */     }
/*    */     catch (Exception localException) {}
/*    */     
/*    */ 
/* 30 */     APIManager.initAPI(GlowAPI.class);
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/GlowPlugin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */