/*    */ package org.inventivetalent.reflection.resolver;
/*    */ 
/*    */ import java.lang.reflect.Member;
/*    */ import org.inventivetalent.reflection.resolver.wrapper.WrapperAbstract;
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
/*    */ public abstract class MemberResolver<T extends Member>
/*    */   extends ResolverAbstract<T>
/*    */ {
/*    */   protected Class<?> clazz;
/*    */   
/*    */   public MemberResolver(Class<?> clazz)
/*    */   {
/* 48 */     if (clazz == null) throw new IllegalArgumentException("class cannot be null");
/* 49 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public MemberResolver(String className) throws ClassNotFoundException {
/* 53 */     this(new ClassResolver().resolve(new String[] { className }));
/*    */   }
/*    */   
/*    */   public abstract T resolveIndex(int paramInt)
/*    */     throws IndexOutOfBoundsException, ReflectiveOperationException;
/*    */   
/*    */   public abstract T resolveIndexSilent(int paramInt);
/*    */   
/*    */   public abstract WrapperAbstract resolveIndexWrapper(int paramInt);
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/reflection/resolver/MemberResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */