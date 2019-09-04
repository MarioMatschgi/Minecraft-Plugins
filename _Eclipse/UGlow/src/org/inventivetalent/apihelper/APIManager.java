/*     */ package org.inventivetalent.apihelper;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.inventivetalent.apihelper.exception.APIRegistrationException;
/*     */ import org.inventivetalent.apihelper.exception.MissingHostException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class APIManager
/*     */ {
/*  84 */   private static final Map<API, RegisteredAPI> HOST_MAP = new HashMap();
/*  85 */   private static final Map<Class<? extends API>, Set<Plugin>> PENDING_API_CLASSES = new HashMap();
/*  86 */   private static final Logger LOGGER = Logger.getLogger("APIManager");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RegisteredAPI registerAPI(API api)
/*     */     throws APIRegistrationException
/*     */   {
/*  97 */     if (HOST_MAP.containsKey(api)) throw new APIRegistrationException("API for '" + api.getClass().getName() + "' is already registered");
/*  98 */     RegisteredAPI registeredAPI = new RegisteredAPI(api);
/*  99 */     HOST_MAP.put(api, registeredAPI);
/*     */     
/*     */ 
/* 102 */     api.load();
/*     */     
/* 104 */     LOGGER.fine("'" + api.getClass().getName() + "' registered as new API");
/* 105 */     return registeredAPI;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RegisteredAPI registerAPI(API api, Plugin host)
/*     */     throws IllegalArgumentException, APIRegistrationException
/*     */   {
/* 121 */     validatePlugin(host);
/* 122 */     registerAPI(api);
/* 123 */     return registerAPIHost(api, host);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static API registerEvents(API api, Listener listener)
/*     */     throws APIRegistrationException
/*     */   {
/* 137 */     if (!HOST_MAP.containsKey(api)) throw new APIRegistrationException("API for '" + api.getClass().getName() + "' is not registered");
/* 138 */     RegisteredAPI registeredAPI = (RegisteredAPI)HOST_MAP.get(api);
/* 139 */     if (registeredAPI.eventsRegistered) {
/* 140 */       return api;
/*     */     }
/* 142 */     Bukkit.getPluginManager().registerEvents(listener, registeredAPI.getNextHost());
/* 143 */     registeredAPI.eventsRegistered = true;
/* 144 */     return api;
/*     */   }
/*     */   
/*     */ 
/*     */   private static void initAPI(API api)
/*     */     throws APIRegistrationException
/*     */   {
/* 151 */     if (!HOST_MAP.containsKey(api)) throw new APIRegistrationException("API for '" + api.getClass().getName() + "' is not registered");
/* 152 */     RegisteredAPI registeredAPI = (RegisteredAPI)HOST_MAP.get(api);
/*     */     
/*     */ 
/* 155 */     registeredAPI.init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void initAPI(Class<? extends API> clazz)
/*     */     throws APIRegistrationException
/*     */   {
/* 166 */     API clazzAPI = null;
/* 167 */     for (API api : HOST_MAP.keySet()) {
/* 168 */       if (api.getClass().equals(clazz)) {
/* 169 */         clazzAPI = api;
/* 170 */         break;
/*     */       }
/*     */     }
/* 173 */     if (clazzAPI == null)
/* 174 */       if (PENDING_API_CLASSES.containsKey(clazz)) {
/* 175 */         LOGGER.info("API class '" + clazz.getName() + "' is not yet initialized. Creating new instance.");
/*     */         try {
/* 177 */           clazzAPI = (API)clazz.newInstance();
/* 178 */           registerAPI(clazzAPI);
/* 179 */           for (??? = ((Set)PENDING_API_CLASSES.get(clazz)).iterator(); ???.hasNext();) { Plugin plugin = (Plugin)???.next();
/* 180 */             if (plugin != null) registerAPIHost(clazzAPI, plugin);
/*     */           }
/*     */         } catch (ReflectiveOperationException e) {
/* 183 */           LOGGER.warning("API class '" + clazz.getName() + "' is missing valid constructor");
/*     */         }
/* 185 */         PENDING_API_CLASSES.remove(clazz);
/* 186 */       } else { throw new APIRegistrationException("API for class '" + clazz.getName() + "' is not registered");
/*     */       }
/* 188 */     initAPI(clazzAPI);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void disableAPI(API api)
/*     */   {
/* 195 */     if (!HOST_MAP.containsKey(api)) return;
/* 196 */     RegisteredAPI registeredAPI = (RegisteredAPI)HOST_MAP.get(api);
/*     */     
/*     */ 
/* 199 */     registeredAPI.disable();
/*     */     
/* 201 */     HOST_MAP.remove(api);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void disableAPI(Class<? extends API> clazz)
/*     */   {
/* 212 */     API clazzAPI = null;
/* 213 */     for (API api : HOST_MAP.keySet()) {
/* 214 */       if (api.getClass().equals(clazz)) {
/* 215 */         clazzAPI = api;
/* 216 */         break;
/*     */       }
/*     */     }
/* 219 */     disableAPI(clazzAPI);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void require(Class<? extends API> clazz, @Nullable Plugin host)
/*     */   {
/*     */     try
/*     */     {
/* 233 */       if (host == null) throw new APIRegistrationException();
/* 234 */       registerAPIHost(clazz, host);
/*     */     } catch (APIRegistrationException e) {
/* 236 */       if (PENDING_API_CLASSES.containsKey(clazz)) {
/* 237 */         ((Set)PENDING_API_CLASSES.get(clazz)).add(host);
/*     */       } else {
/* 239 */         Set<Plugin> hosts = new HashSet();
/* 240 */         hosts.add(host);
/* 241 */         PENDING_API_CLASSES.put(clazz, hosts);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static RegisteredAPI registerAPIHost(API api, Plugin host)
/*     */     throws APIRegistrationException
/*     */   {
/* 253 */     validatePlugin(host);
/* 254 */     if (!HOST_MAP.containsKey(api)) throw new APIRegistrationException("API for '" + api.getClass().getName() + "' is not registered");
/* 255 */     RegisteredAPI registeredAPI = (RegisteredAPI)HOST_MAP.get(api);
/* 256 */     registeredAPI.registerHost(host);
/*     */     
/* 258 */     LOGGER.fine("'" + host.getName() + "' registered as Host for '" + api + "'");
/* 259 */     return registeredAPI;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RegisteredAPI registerAPIHost(Class<? extends API> clazz, Plugin host)
/*     */     throws APIRegistrationException
/*     */   {
/* 269 */     validatePlugin(host);
/* 270 */     API clazzAPI = null;
/* 271 */     for (API api : HOST_MAP.keySet()) {
/* 272 */       if (api.getClass().equals(clazz)) {
/* 273 */         clazzAPI = api;
/* 274 */         break;
/*     */       }
/*     */     }
/* 277 */     if (clazzAPI == null) throw new APIRegistrationException("API for class '" + clazz.getName() + "' is not registered");
/* 278 */     return registerAPIHost(clazzAPI, host);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Plugin getAPIHost(API api)
/*     */     throws APIRegistrationException, MissingHostException
/*     */   {
/* 288 */     if (!HOST_MAP.containsKey(api)) throw new APIRegistrationException("API for '" + api.getClass().getName() + "' is not registered");
/* 289 */     return ((RegisteredAPI)HOST_MAP.get(api)).getNextHost();
/*     */   }
/*     */   
/*     */   private static void validatePlugin(Plugin plugin) {
/* 293 */     if ((plugin instanceof API)) throw new IllegalArgumentException("Plugin must not implement API");
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/apihelper/APIManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */