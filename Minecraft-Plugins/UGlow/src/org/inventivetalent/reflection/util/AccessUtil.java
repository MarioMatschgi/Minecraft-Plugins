/*    */ package org.inventivetalent.reflection.util;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AccessUtil
/*    */ {
/*    */   public static Field setAccessible(Field field)
/*    */     throws ReflectiveOperationException
/*    */   {
/* 48 */     field.setAccessible(true);
/* 49 */     Field modifiersField = Field.class.getDeclaredField("modifiers");
/* 50 */     modifiersField.setAccessible(true);
/* 51 */     modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
/* 52 */     return field;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Method setAccessible(Method method)
/*    */     throws ReflectiveOperationException
/*    */   {
/* 63 */     method.setAccessible(true);
/* 64 */     return method;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Constructor setAccessible(Constructor constructor)
/*    */     throws ReflectiveOperationException
/*    */   {
/* 75 */     constructor.setAccessible(true);
/* 76 */     return constructor;
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/reflection/util/AccessUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */