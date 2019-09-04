/*     */ package org.inventivetalent.packetlistener.handler;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.Cancellable;
/*     */ import org.inventivetalent.packetlistener.channel.ChannelWrapper;
/*     */ import org.inventivetalent.reflection.resolver.FieldResolver;
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
/*     */ public abstract class PacketAbstract
/*     */ {
/*     */   private Player player;
/*     */   private ChannelWrapper channelWrapper;
/*     */   private Object packet;
/*     */   private Cancellable cancellable;
/*     */   protected FieldResolver fieldResolver;
/*     */   
/*     */   public PacketAbstract(Object packet, Cancellable cancellable, Player player)
/*     */   {
/*  47 */     this.player = player;
/*     */     
/*  49 */     this.packet = packet;
/*  50 */     this.cancellable = cancellable;
/*     */     
/*  52 */     this.fieldResolver = new FieldResolver(packet.getClass());
/*     */   }
/*     */   
/*     */   public PacketAbstract(Object packet, Cancellable cancellable, ChannelWrapper channelWrapper) {
/*  56 */     this.channelWrapper = channelWrapper;
/*     */     
/*  58 */     this.packet = packet;
/*  59 */     this.cancellable = cancellable;
/*     */     
/*  61 */     this.fieldResolver = new FieldResolver(packet.getClass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPacketValue(String field, Object value)
/*     */   {
/*     */     try
/*     */     {
/*  72 */       this.fieldResolver.resolve(new String[] { field }).set(getPacket(), value);
/*     */     } catch (Exception e) {
/*  74 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPacketValueSilent(String field, Object value)
/*     */   {
/*     */     try
/*     */     {
/*  86 */       this.fieldResolver.resolve(new String[] { field }).set(getPacket(), value);
/*     */     }
/*     */     catch (Exception localException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPacketValue(int index, Object value)
/*     */   {
/*     */     try
/*     */     {
/*  99 */       this.fieldResolver.resolveIndex(index).set(getPacket(), value);
/*     */     } catch (Exception e) {
/* 101 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPacketValueSilent(int index, Object value)
/*     */   {
/*     */     try
/*     */     {
/* 113 */       this.fieldResolver.resolveIndex(index).set(getPacket(), value);
/*     */     }
/*     */     catch (Exception localException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getPacketValue(String field)
/*     */   {
/*     */     try
/*     */     {
/* 126 */       return this.fieldResolver.resolve(new String[] { field }).get(getPacket());
/*     */     } catch (Exception e) {
/* 128 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getPacketValueSilent(String field)
/*     */   {
/*     */     try
/*     */     {
/* 140 */       return this.fieldResolver.resolve(new String[] { field }).get(getPacket());
/*     */     }
/*     */     catch (Exception localException) {}
/* 143 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getPacketValue(int index)
/*     */   {
/*     */     try
/*     */     {
/* 154 */       return this.fieldResolver.resolveIndex(index).get(getPacket());
/*     */     } catch (Exception e) {
/* 156 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getPacketValueSilent(int index)
/*     */   {
/*     */     try
/*     */     {
/* 168 */       return this.fieldResolver.resolveIndex(index).get(getPacket());
/*     */     } catch (Exception e) {
/* 170 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public FieldResolver getFieldResolver() {
/* 175 */     return this.fieldResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setCancelled(boolean b)
/*     */   {
/* 182 */     this.cancellable.setCancelled(b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isCancelled()
/*     */   {
/* 189 */     return this.cancellable.isCancelled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Player getPlayer()
/*     */   {
/* 198 */     return this.player;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasPlayer()
/*     */   {
/* 205 */     return this.player != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelWrapper<?> getChannel()
/*     */   {
/* 214 */     return this.channelWrapper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasChannel()
/*     */   {
/* 221 */     return this.channelWrapper != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPlayername()
/*     */   {
/* 230 */     if (!hasPlayer()) return null;
/* 231 */     return this.player.getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPacket(Object packet)
/*     */   {
/* 240 */     this.packet = packet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getPacket()
/*     */   {
/* 247 */     return this.packet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPacketName()
/*     */   {
/* 254 */     return this.packet.getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 259 */     return "Packet{ " + (getClass().equals(SentPacket.class) ? "[> OUT >]" : "[< IN <]") + " " + getPacketName() + " " + (hasChannel() ? getChannel().channel() : hasPlayer() ? getPlayername() : "#server#") + " }";
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/handler/PacketAbstract.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */