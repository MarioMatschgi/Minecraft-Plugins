/*    */ package org.inventivetalent.apihelper;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.inventivetalent.apihelper.exception.HostRegistrationException;
/*    */ import org.inventivetalent.apihelper.exception.MissingHostException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RegisteredAPI
/*    */ {
/*    */   protected final API api;
/* 42 */   protected final Set<Plugin> hosts = new HashSet();
/*    */   
/* 44 */   protected boolean initialized = false;
/*    */   
/*    */   protected Plugin initializerHost;
/* 47 */   protected boolean eventsRegistered = false;
/*    */   
/*    */   public RegisteredAPI(API api) {
/* 50 */     this.api = api;
/*    */   }
/*    */   
/*    */   public void registerHost(Plugin host) throws HostRegistrationException {
/* 54 */     if (this.hosts.contains(host)) throw new HostRegistrationException("API host '" + host.getName() + "' for '" + this.api.getClass().getName() + "' is already registered");
/* 55 */     this.hosts.add(host);
/*    */   }
/*    */   
/*    */   public Plugin getNextHost() throws MissingHostException {
/* 59 */     if (((this.api instanceof Plugin)) && (((Plugin)this.api).isEnabled())) {
/* 60 */       return (Plugin)this.api;
/*    */     }
/* 62 */     if (this.hosts.isEmpty()) {
/* 63 */       throw new MissingHostException("API '" + this.api.getClass().getName() + "' is disabled, but no other Hosts have been registered");
/*    */     }
/* 65 */     for (Iterator<Plugin> iterator = this.hosts.iterator(); iterator.hasNext();) {
/* 66 */       Plugin host = (Plugin)iterator.next();
/* 67 */       if (host.isEnabled()) {
/* 68 */         return host;
/*    */       }
/*    */     }
/* 71 */     throw new MissingHostException("API '" + this.api.getClass().getName() + "' is disabled and all registered Hosts are as well");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void init()
/*    */   {
/* 80 */     if (this.initialized) {
/* 81 */       return;
/*    */     }
/* 83 */     this.api.init(this.initializerHost = getNextHost());
/* 84 */     this.initialized = true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void disable()
/*    */   {
/* 93 */     if (!this.initialized) {
/* 94 */       return;
/*    */     }
/* 96 */     this.api.disable(this.initializerHost);
/* 97 */     this.initialized = false;
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/apihelper/RegisteredAPI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */