/*     */ package org.inventivetalent.packetlistener.handler;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.inventivetalent.reflection.minecraft.Minecraft;
/*     */ import org.inventivetalent.reflection.resolver.FieldResolver;
/*     */ import org.inventivetalent.reflection.resolver.MethodResolver;
/*     */ import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;
/*     */ import org.inventivetalent.reflection.util.AccessUtil;
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
/*     */ public abstract class PacketHandler
/*     */ {
/*  45 */   private static final List<PacketHandler> handlers = new ArrayList();
/*     */   
/*     */   private boolean hasSendOptions;
/*     */   private boolean forcePlayerSend;
/*     */   private boolean forceServerSend;
/*     */   private boolean hasReceiveOptions;
/*     */   private boolean forcePlayerReceive;
/*     */   private boolean forceServerReceive;
/*     */   
/*     */   public static boolean addHandler(PacketHandler handler)
/*     */   {
/*  56 */     boolean b = handlers.contains(handler);
/*  57 */     if (!b) {
/*     */       try {
/*  59 */         PacketOptions options = (PacketOptions)handler.getClass().getMethod("onSend", new Class[] { SentPacket.class }).getAnnotation(PacketOptions.class);
/*  60 */         if (options != null) {
/*  61 */           handler.hasSendOptions = true;
/*  62 */           if ((options.forcePlayer()) && (options.forceServer())) throw new IllegalArgumentException("Cannot force player and server packets at the same time!");
/*  63 */           if (options.forcePlayer()) {
/*  64 */             handler.forcePlayerSend = true;
/*  65 */           } else if (options.forceServer()) {
/*  66 */             handler.forceServerSend = true;
/*     */           }
/*     */         }
/*     */       } catch (Exception e) {
/*  70 */         throw new RuntimeException("Failed to register handler (onSend)", e);
/*     */       }
/*     */       try {
/*  73 */         PacketOptions options = (PacketOptions)handler.getClass().getMethod("onReceive", new Class[] { ReceivedPacket.class }).getAnnotation(PacketOptions.class);
/*  74 */         if (options != null) {
/*  75 */           handler.hasReceiveOptions = true;
/*  76 */           if ((options.forcePlayer()) && (options.forceServer())) throw new IllegalArgumentException("Cannot force player and server packets at the same time!");
/*  77 */           if (options.forcePlayer()) {
/*  78 */             handler.forcePlayerReceive = true;
/*  79 */           } else if (options.forceServer()) {
/*  80 */             handler.forceServerReceive = true;
/*     */           }
/*     */         }
/*     */       } catch (Exception e) {
/*  84 */         throw new RuntimeException("Failed to register handler (onReceive)", e);
/*     */       }
/*     */     }
/*  87 */     handlers.add(handler);
/*  88 */     return !b;
/*     */   }
/*     */   
/*     */   public static boolean removeHandler(PacketHandler handler) {
/*  92 */     return handlers.remove(handler);
/*     */   }
/*     */   
/*     */   public static void notifyHandlers(SentPacket packet) {
/*  96 */     for (PacketHandler handler : ) {
/*     */       try {
/*  98 */         if ((!handler.hasSendOptions) || 
/*  99 */           (handler.forcePlayerSend ? 
/* 100 */           packet.hasPlayer() : 
/*     */           
/*     */ 
/* 103 */           (!handler.forceServerSend) || 
/* 104 */           (packet.hasChannel())))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 109 */           handler.onSend(packet); }
/*     */       } catch (Exception e) {
/* 111 */         System.err.println("[PacketListenerAPI] An exception occured while trying to execute 'onSend'" + (handler.plugin != null ? " in plugin " + handler.plugin.getName() : "") + ": " + e.getMessage());
/* 112 */         e.printStackTrace(System.err);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void notifyHandlers(ReceivedPacket packet) {
/* 118 */     for (PacketHandler handler : ) {
/*     */       try {
/* 120 */         if ((!handler.hasReceiveOptions) || 
/* 121 */           (handler.forcePlayerReceive ? 
/* 122 */           packet.hasPlayer() : 
/*     */           
/*     */ 
/* 125 */           (!handler.forceServerReceive) || 
/* 126 */           (packet.hasChannel())))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 131 */           handler.onReceive(packet); }
/*     */       } catch (Exception e) {
/* 133 */         System.err.println("[PacketListenerAPI] An exception occured while trying to execute 'onReceive'" + (handler.plugin != null ? " in plugin " + handler.plugin.getName() : "") + ": " + e.getMessage());
/* 134 */         e.printStackTrace(System.err);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equals(Object object)
/*     */   {
/* 141 */     if (this == object) return true;
/* 142 */     if ((object == null) || (getClass() != object.getClass())) { return false;
/*     */     }
/* 144 */     PacketHandler that = (PacketHandler)object;
/*     */     
/* 146 */     if (this.hasSendOptions != that.hasSendOptions) return false;
/* 147 */     if (this.forcePlayerSend != that.forcePlayerSend) return false;
/* 148 */     if (this.forceServerSend != that.forceServerSend) return false;
/* 149 */     if (this.hasReceiveOptions != that.hasReceiveOptions) return false;
/* 150 */     if (this.forcePlayerReceive != that.forcePlayerReceive) return false;
/* 151 */     if (this.forceServerReceive != that.forceServerReceive) return false;
/* 152 */     return this.plugin != null ? this.plugin.equals(that.plugin) : that.plugin == null;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 158 */     int result = this.hasSendOptions ? 1 : 0;
/* 159 */     result = 31 * result + (this.forcePlayerSend ? 1 : 0);
/* 160 */     result = 31 * result + (this.forceServerSend ? 1 : 0);
/* 161 */     result = 31 * result + (this.hasReceiveOptions ? 1 : 0);
/* 162 */     result = 31 * result + (this.forcePlayerReceive ? 1 : 0);
/* 163 */     result = 31 * result + (this.forceServerReceive ? 1 : 0);
/* 164 */     result = 31 * result + (this.plugin != null ? this.plugin.hashCode() : 0);
/* 165 */     return result;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 170 */     return "PacketHandler{hasSendOptions=" + this.hasSendOptions + ", forcePlayerSend=" + this.forcePlayerSend + ", forceServerSend=" + this.forceServerSend + ", hasReceiveOptions=" + this.hasReceiveOptions + ", forcePlayerReceive=" + this.forcePlayerReceive + ", forceServerReceive=" + this.forceServerReceive + ", plugin=" + this.plugin + '}';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<PacketHandler> getHandlers()
/*     */   {
/* 182 */     return new ArrayList(handlers);
/*     */   }
/*     */   
/*     */   public static List<PacketHandler> getForPlugin(Plugin plugin) {
/* 186 */     List<PacketHandler> handlers = new ArrayList();
/* 187 */     if (plugin == null) return handlers;
/* 188 */     for (PacketHandler h : getHandlers()) {
/* 189 */       if (plugin.equals(h.getPlugin()))
/* 190 */         handlers.add(h);
/*     */     }
/* 192 */     return handlers;
/*     */   }
/*     */   
/* 195 */   static NMSClassResolver nmsClassResolver = new NMSClassResolver();
/* 196 */   static FieldResolver EntityPlayerFieldResolver = new FieldResolver(nmsClassResolver.resolveSilent(new String[] { "EntityPlayer" }));
/* 197 */   static MethodResolver PlayerConnectionMethodResolver = new MethodResolver(nmsClassResolver.resolveSilent(new String[] { "PlayerConnection" }));
/*     */   private Plugin plugin;
/*     */   
/*     */   public void sendPacket(Player p, Object packet) {
/* 201 */     if ((p == null) || (packet == null)) throw new NullPointerException();
/*     */     try {
/* 203 */       Object handle = Minecraft.getHandle(p);
/* 204 */       Object connection = EntityPlayerFieldResolver.resolve(new String[] { "playerConnection" }).get(handle);
/* 205 */       PlayerConnectionMethodResolver.resolve(new String[] { "sendPacket" }).invoke(connection, new Object[] { packet });
/*     */     } catch (Exception e) {
/* 207 */       System.err.println("[PacketListenerAPI] Exception while sending " + packet + " to " + p);
/* 208 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public Object cloneObject(Object obj) throws Exception {
/* 213 */     if (obj == null) return obj;
/* 214 */     Object clone = obj.getClass().newInstance();
/* 215 */     for (Field f : obj.getClass().getDeclaredFields()) {
/* 216 */       f = AccessUtil.setAccessible(f);
/* 217 */       f.set(clone, f.get(obj));
/*     */     }
/* 219 */     return clone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public PacketHandler() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public PacketHandler(Plugin plugin)
/*     */   {
/* 231 */     this.plugin = plugin;
/*     */   }
/*     */   
/*     */   public Plugin getPlugin() {
/* 235 */     return this.plugin;
/*     */   }
/*     */   
/*     */   public abstract void onSend(SentPacket paramSentPacket);
/*     */   
/*     */   public abstract void onReceive(ReceivedPacket paramReceivedPacket);
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/handler/PacketHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */