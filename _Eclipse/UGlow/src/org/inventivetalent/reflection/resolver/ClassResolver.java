/*    */ package org.inventivetalent.reflection.resolver;
/*    */ 
/*    */ import org.inventivetalent.reflection.resolver.wrapper.ClassWrapper;
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
/*    */ public class ClassResolver
/*    */   extends ResolverAbstract<Class>
/*    */ {
/*    */   public ClassWrapper resolveWrapper(String... names)
/*    */   {
/* 39 */     return new ClassWrapper(resolveSilent(names));
/*    */   }
/*    */   
/*    */   public Class resolveSilent(String... names) {
/*    */     try {
/* 44 */       return resolve(names);
/*    */     }
/*    */     catch (Exception localException) {}
/* 47 */     return null;
/*    */   }
/*    */   
/*    */   public Class resolve(String... names) throws ClassNotFoundException {
/* 51 */     ResolverQuery.Builder builder = ResolverQuery.builder();
/* 52 */     for (String name : names)
/* 53 */       builder.with(name);
/*    */     try {
/* 55 */       return (Class)super.resolve(builder.build());
/*    */     } catch (ReflectiveOperationException e) {
/* 57 */       throw ((ClassNotFoundException)e);
/*    */     }
/*    */   }
/*    */   
/*    */   protected Class resolveObject(ResolverQuery query) throws ReflectiveOperationException
/*    */   {
/* 63 */     return Class.forName(query.getName());
/*    */   }
/*    */   
/*    */   protected ClassNotFoundException notFoundException(String joinedNames)
/*    */   {
/* 68 */     return new ClassNotFoundException("Could not resolve class for " + joinedNames);
/*    */   }
/*    */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/reflection/resolver/ClassResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */