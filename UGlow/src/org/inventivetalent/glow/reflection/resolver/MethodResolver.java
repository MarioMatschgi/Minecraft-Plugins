/*     */ package org.inventivetalent.glow.reflection.resolver;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.inventivetalent.glow.reflection.resolver.wrapper.MethodWrapper;
/*     */ import org.inventivetalent.glow.reflection.util.AccessUtil;
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
/*     */ public class MethodResolver
/*     */   extends MemberResolver<Method>
/*     */ {
/*     */   public MethodResolver(Class<?> clazz)
/*     */   {
/*  42 */     super(clazz);
/*     */   }
/*     */   
/*     */   public MethodResolver(String className) throws ClassNotFoundException {
/*  46 */     super(className);
/*     */   }
/*     */   
/*     */   public Method resolveIndex(int index) throws IndexOutOfBoundsException, ReflectiveOperationException
/*     */   {
/*  51 */     return AccessUtil.setAccessible(this.clazz.getDeclaredMethods()[index]);
/*     */   }
/*     */   
/*     */   public Method resolveIndexSilent(int index)
/*     */   {
/*     */     try {
/*  57 */       return resolveIndex(index);
/*     */     }
/*     */     catch (IndexOutOfBoundsException|ReflectiveOperationException localIndexOutOfBoundsException) {}
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   public MethodWrapper resolveIndexWrapper(int index)
/*     */   {
/*  65 */     return new MethodWrapper(resolveIndexSilent(index));
/*     */   }
/*     */   
/*     */   public MethodWrapper resolveWrapper(String... names) {
/*  69 */     return new MethodWrapper(resolveSilent(names));
/*     */   }
/*     */   
/*     */   public MethodWrapper resolveWrapper(ResolverQuery... queries) {
/*  73 */     return new MethodWrapper(resolveSilent(queries));
/*     */   }
/*     */   
/*     */   public Method resolveSilent(String... names) {
/*     */     try {
/*  78 */       return resolve(names);
/*     */     }
/*     */     catch (Exception localException) {}
/*  81 */     return null;
/*     */   }
/*     */   
/*     */   public Method resolveSilent(ResolverQuery... queries)
/*     */   {
/*  86 */     return (Method)super.resolveSilent(queries);
/*     */   }
/*     */   
/*     */   public Method resolve(String... names) throws NoSuchMethodException {
/*  90 */     ResolverQuery.Builder builder = ResolverQuery.builder();
/*  91 */     for (String name : names) {
/*  92 */       builder.with(name);
/*     */     }
/*  94 */     return resolve(builder.build());
/*     */   }
/*     */   
/*     */   public Method resolve(ResolverQuery... queries) throws NoSuchMethodException
/*     */   {
/*     */     try {
/* 100 */       return (Method)super.resolve(queries);
/*     */     } catch (ReflectiveOperationException e) {
/* 102 */       throw ((NoSuchMethodException)e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Method resolveObject(ResolverQuery query) throws ReflectiveOperationException
/*     */   {
/* 108 */     for (Method method : this.clazz.getDeclaredMethods()) {
/* 109 */       if ((method.getName().equals(query.getName())) && ((query.getTypes().length == 0) || (ClassListEqual(query.getTypes(), method.getParameterTypes())))) {
/* 110 */         return AccessUtil.setAccessible(method);
/*     */       }
/*     */     }
/* 113 */     throw new NoSuchMethodException();
/*     */   }
/*     */   
/*     */   protected NoSuchMethodException notFoundException(String joinedNames)
/*     */   {
/* 118 */     return new NoSuchMethodException("Could not resolve method for " + joinedNames + " in class " + this.clazz);
/*     */   }
/*     */   
/*     */   static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
/* 122 */     boolean equal = true;
/* 123 */     if (l1.length != l2.length) return false;
/* 124 */     for (int i = 0; i < l1.length; i++) {
/* 125 */       if (l1[i] != l2[i]) {
/* 126 */         equal = false;
/* 127 */         break;
/*     */       }
/*     */     }
/* 130 */     return equal;
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/reflection/resolver/MethodResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */