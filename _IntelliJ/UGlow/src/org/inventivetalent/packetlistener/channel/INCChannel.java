/*     */ package org.inventivetalent.packetlistener.channel;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.inventivetalent.packetlistener.Cancellable;
/*     */ import org.inventivetalent.packetlistener.IPacketListener;
/*     */ import org.inventivetalent.reflection.minecraft.Minecraft;
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
/*     */ public class INCChannel
/*     */   extends ChannelAbstract
/*     */ {
/*  45 */   private static final Field channelField = networkManagerFieldResolver.resolveByFirstTypeSilent(Channel.class);
/*     */   
/*     */   public INCChannel(IPacketListener iPacketListener) {
/*  48 */     super(iPacketListener);
/*     */   }
/*     */   
/*     */   public void addChannel(final Player player)
/*     */   {
/*     */     try {
/*  54 */       final Channel channel = getChannel(player);
/*  55 */       this.addChannelExecutor.execute(new Runnable()
/*     */       {
/*     */         public void run() {
/*     */           try {
/*  59 */             channel.pipeline().addBefore("packet_handler", "packet_listener_player", new INCChannel.ChannelHandler(INCChannel.this, player));
/*     */           } catch (Exception e) {
/*  61 */             throw new RuntimeException(e);
/*     */           }
/*     */         }
/*     */       });
/*     */     } catch (ReflectiveOperationException e) {
/*  66 */       throw new RuntimeException("Failed to add channel for " + player, e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeChannel(Player player)
/*     */   {
/*     */     try {
/*  73 */       final Channel channel = getChannel(player);
/*  74 */       this.removeChannelExecutor.execute(new Runnable()
/*     */       {
/*     */         public void run() {
/*     */           try {
/*  78 */             if (channel.pipeline().get("packet_listener_player") != null) {
/*  79 */               channel.pipeline().remove("packet_listener_player");
/*     */             }
/*     */           } catch (Exception e) {
/*  82 */             throw new RuntimeException(e);
/*     */           }
/*     */         }
/*     */       });
/*     */     } catch (ReflectiveOperationException e) {
/*  87 */       throw new RuntimeException("Failed to remove channel for " + player, e);
/*     */     }
/*     */   }
/*     */   
/*     */   Channel getChannel(Player player) throws ReflectiveOperationException {
/*  92 */     Object handle = Minecraft.getHandle(player);
/*  93 */     Object connection = playerConnection.get(handle);
/*  94 */     return (Channel)channelField.get(networkManager.get(connection));
/*     */   }
/*     */   
/*     */   public ChannelAbstract.IListenerList newListenerList()
/*     */   {
/*  99 */     return new ListenerList();
/*     */   }
/*     */   
/*     */   class ListenerList<E> extends ArrayList<E> implements ChannelAbstract.IListenerList<E> {
/*     */     ListenerList() {}
/*     */     
/*     */     public boolean add(E paramE) {
/*     */       try {
/* 107 */         final E a = paramE;
/* 108 */         INCChannel.this.addChannelExecutor.execute(new Runnable()
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 112 */               Channel channel = null;
/* 113 */               while (channel == null) {
/* 114 */                 channel = (Channel)INCChannel.channelField.get(a);
/*     */               }
/* 116 */               if (channel.pipeline().get("packet_listener_server") == null) {
/* 117 */                 channel.pipeline().addBefore("packet_handler", "packet_listener_server", new INCChannel.ChannelHandler(INCChannel.this, new INCChannel.INCChannelWrapper(INCChannel.this, channel)));
/*     */               }
/*     */             }
/*     */             catch (Exception localException) {}
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (Exception localException) {}
/* 125 */       return super.add(paramE);
/*     */     }
/*     */     
/*     */     public boolean remove(Object arg0)
/*     */     {
/*     */       try {
/* 131 */         final Object a = arg0;
/* 132 */         INCChannel.this.removeChannelExecutor.execute(new Runnable()
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 136 */               Channel channel = null;
/* 137 */               while (channel == null) {
/* 138 */                 channel = (Channel)INCChannel.channelField.get(a);
/*     */               }
/* 140 */               channel.pipeline().remove("packet_listener_server");
/*     */             }
/*     */             catch (Exception localException) {}
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (Exception localException) {}
/* 147 */       return super.remove(arg0);
/*     */     }
/*     */   }
/*     */   
/*     */   class ChannelHandler extends ChannelDuplexHandler implements ChannelAbstract.IChannelHandler
/*     */   {
/*     */     private Object owner;
/*     */     
/*     */     public ChannelHandler(Player player) {
/* 156 */       this.owner = player;
/*     */     }
/*     */     
/*     */     public ChannelHandler(ChannelWrapper channelWrapper) {
/* 160 */       this.owner = channelWrapper;
/*     */     }
/*     */     
/*     */     public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */     {
/* 165 */       Cancellable cancellable = new Cancellable();
/* 166 */       Object pckt = msg;
/* 167 */       if (ChannelAbstract.Packet.isAssignableFrom(msg.getClass())) {
/* 168 */         pckt = INCChannel.this.onPacketSend(this.owner, msg, cancellable);
/*     */       }
/* 170 */       if (cancellable.isCancelled()) return;
/* 171 */       super.write(ctx, pckt, promise);
/*     */     }
/*     */     
/*     */     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */     {
/* 176 */       Cancellable cancellable = new Cancellable();
/* 177 */       Object pckt = msg;
/* 178 */       if (ChannelAbstract.Packet.isAssignableFrom(msg.getClass())) {
/* 179 */         pckt = INCChannel.this.onPacketReceive(this.owner, msg, cancellable);
/*     */       }
/* 181 */       if (cancellable.isCancelled()) return;
/* 182 */       super.channelRead(ctx, pckt);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 187 */       return "INCChannel$ChannelHandler@" + hashCode() + " (" + this.owner + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   class INCChannelWrapper extends ChannelWrapper<Channel> implements ChannelAbstract.IChannelWrapper
/*     */   {
/*     */     public INCChannelWrapper(Channel channel)
/*     */     {
/* 195 */       super();
/*     */     }
/*     */     
/*     */     public SocketAddress getRemoteAddress()
/*     */     {
/* 200 */       return ((Channel)channel()).remoteAddress();
/*     */     }
/*     */     
/*     */     public SocketAddress getLocalAddress()
/*     */     {
/* 205 */       return ((Channel)channel()).localAddress();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/channel/INCChannel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */