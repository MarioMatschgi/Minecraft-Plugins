/*    */ package org.inventivetalent.reflection.resolver.minecraft;
/*    */ 
/*    */ import org.inventivetalent.reflection.minecraft.Minecraft;
/*    */ import org.inventivetalent.reflection.resolver.ClassResolver;
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
/*    */ public class NMSClassResolver
/*    */   extends ClassResolver
/*    */ {
/*    */   public Class resolve(String... names)
/*    */     throws ClassNotFoundException
/*    */   {
/* 41 */     for (int i = 0; i < names.length; i++) {
/* 42 */       if (!names[i].startsWith("net.minecraft.server")) {
/* 43 */         names[i] = ("net.minecraft.server." + Minecraft.getVersion() + names[i]);
/*    */       }
/*    */     }
/* 46 */     return super.resolve(names);
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/reflection/resolver/minecraft/NMSClassResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */