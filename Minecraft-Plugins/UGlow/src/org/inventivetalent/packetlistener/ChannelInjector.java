/*    */ package org.inventivetalent.packetlistener;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.inventivetalent.packetlistener.channel.ChannelAbstract;
/*    */ import org.inventivetalent.reflection.resolver.ClassResolver;
/*    */ import org.inventivetalent.reflection.resolver.ConstructorResolver;
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
/*    */ public class ChannelInjector
/*    */ {
/* 42 */   private static final ClassResolver CLASS_RESOLVER = new ClassResolver();
/*    */   private ChannelAbstract channel;
/*    */   
/*    */   public boolean inject(IPacketListener iPacketListener)
/*    */   {
/* 47 */     List<Exception> exceptions = new ArrayList();
/*    */     try {
/* 49 */       Class.forName("net.minecraft.util.io.netty.channel.Channel");
/* 50 */       this.channel = newChannelInstance(iPacketListener, "org.inventivetalent.packetlistener.channel.NMUChannel");
/* 51 */       System.out.println("[PacketListenerAPI] Using NMUChannel");
/* 52 */       return true;
/*    */     } catch (Exception e) {
/* 54 */       exceptions.add(e);
/*    */       try
/*    */       {
/* 57 */         Class.forName("io.netty.channel.Channel");
/* 58 */         this.channel = newChannelInstance(iPacketListener, "org.inventivetalent.packetlistener.channel.INCChannel");
/* 59 */         System.out.println("[PacketListenerAPI] Using INChannel");
/* 60 */         return true;
/*    */       } catch (Exception e1) {
/* 62 */         exceptions.add(e1);
/*    */         
/* 64 */         for (Exception e : exceptions)
/* 65 */           e.printStackTrace();
/*    */       } }
/* 67 */     return false;
/*    */   }
/*    */   
/*    */   protected ChannelAbstract newChannelInstance(IPacketListener iPacketListener, String clazzName) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 71 */     return (ChannelAbstract)new ConstructorResolver(CLASS_RESOLVER.resolve(new String[] { clazzName })).resolve(new Class[][] { { IPacketListener.class } }).newInstance(new Object[] { iPacketListener });
/*    */   }
/*    */   
/*    */   public void addChannel(Player p) {
/* 75 */     this.channel.addChannel(p);
/*    */   }
/*    */   
/*    */   public void removeChannel(Player p) {
/* 79 */     this.channel.removeChannel(p);
/*    */   }
/*    */   
/*    */   public void addServerChannel() {
/* 83 */     this.channel.addServerChannel();
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/ChannelInjector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */