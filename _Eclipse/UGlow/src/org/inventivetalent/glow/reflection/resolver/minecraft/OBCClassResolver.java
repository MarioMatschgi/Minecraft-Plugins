/*    */ package org.inventivetalent.glow.reflection.resolver.minecraft;
/*    */ 
/*    */ import org.inventivetalent.glow.reflection.minecraft.Minecraft;
/*    */ import org.inventivetalent.glow.reflection.resolver.ClassResolver;
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
/*    */ public class OBCClassResolver
/*    */   extends ClassResolver
/*    */ {
/*    */   public Class resolve(String... names)
/*    */     throws ClassNotFoundException
/*    */   {
/* 41 */     for (int i = 0; i < names.length; i++) {
/* 42 */       if (!names[i].startsWith("org.bukkit.craftbukkit")) {
/* 43 */         names[i] = ("org.bukkit.craftbukkit." + Minecraft.getVersion() + names[i]);
/*    */       }
/*    */     }
/* 46 */     return super.resolve(names);
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/reflection/resolver/minecraft/OBCClassResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */