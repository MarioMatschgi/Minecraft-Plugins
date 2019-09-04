/*     */ package org.inventivetalent.glow.reflection.resolver;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import org.inventivetalent.glow.reflection.resolver.wrapper.ConstructorWrapper;
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
/*     */ public class ConstructorResolver
/*     */   extends MemberResolver<Constructor>
/*     */ {
/*     */   public ConstructorResolver(Class<?> clazz)
/*     */   {
/*  42 */     super(clazz);
/*     */   }
/*     */   
/*     */   public ConstructorResolver(String className) throws ClassNotFoundException {
/*  46 */     super(className);
/*     */   }
/*     */   
/*     */   public Constructor resolveIndex(int index) throws IndexOutOfBoundsException, ReflectiveOperationException
/*     */   {
/*  51 */     return AccessUtil.setAccessible(this.clazz.getDeclaredConstructors()[index]);
/*     */   }
/*     */   
/*     */   public Constructor resolveIndexSilent(int index)
/*     */   {
/*     */     try {
/*  57 */       return resolveIndex(index);
/*     */     }
/*     */     catch (IndexOutOfBoundsException|ReflectiveOperationException localIndexOutOfBoundsException) {}
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   public ConstructorWrapper resolveIndexWrapper(int index)
/*     */   {
/*  65 */     return new ConstructorWrapper(resolveIndexSilent(index));
/*     */   }
/*     */   
/*     */   public ConstructorWrapper resolveWrapper(Class<?>[]... types) {
/*  69 */     return new ConstructorWrapper(resolveSilent(types));
/*     */   }
/*     */   
/*     */   public Constructor resolveSilent(Class<?>[]... types) {
/*     */     try {
/*  74 */       return resolve(types);
/*     */     }
/*     */     catch (Exception localException) {}
/*  77 */     return null;
/*     */   }
/*     */   
/*     */   public Constructor resolve(Class<?>[]... types) throws NoSuchMethodException {
/*  81 */     ResolverQuery.Builder builder = ResolverQuery.builder();
/*  82 */     for (Class<?>[] type : types)
/*  83 */       builder.with(type);
/*     */     try {
/*  85 */       return (Constructor)super.resolve(builder.build());
/*     */     } catch (ReflectiveOperationException e) {
/*  87 */       throw ((NoSuchMethodException)e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Constructor resolveObject(ResolverQuery query) throws ReflectiveOperationException
/*     */   {
/*  93 */     return AccessUtil.setAccessible(this.clazz.getDeclaredConstructor(query.getTypes()));
/*     */   }
/*     */   
/*     */   public Constructor resolveFirstConstructor() throws ReflectiveOperationException {
/*  97 */     Constructor[] arrayOfConstructor = this.clazz.getDeclaredConstructors();int i = arrayOfConstructor.length;int j = 0; if (j < i) { Constructor constructor = arrayOfConstructor[j];
/*  98 */       return AccessUtil.setAccessible(constructor);
/*     */     }
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   public Constructor resolveFirstConstructorSilent() {
/*     */     try {
/* 105 */       return resolveFirstConstructor();
/*     */     }
/*     */     catch (Exception localException) {}
/* 108 */     return null;
/*     */   }
/*     */   
/*     */   public Constructor resolveLastConstructor() throws ReflectiveOperationException {
/* 112 */     Constructor constructor = null;
/* 113 */     for (Constructor constructor1 : this.clazz.getDeclaredConstructors()) {
/* 114 */       constructor = constructor1;
/*     */     }
/* 116 */     if (constructor != null) return AccessUtil.setAccessible(constructor);
/* 117 */     return null;
/*     */   }
/*     */   
/*     */   public Constructor resolveLastConstructorSilent() {
/*     */     try {
/* 122 */       return resolveLastConstructor();
/*     */     }
/*     */     catch (Exception localException) {}
/* 125 */     return null;
/*     */   }
/*     */   
/*     */   protected NoSuchMethodException notFoundException(String joinedNames)
/*     */   {
/* 130 */     return new NoSuchMethodException("Could not resolve constructor for " + joinedNames + " in class " + this.clazz);
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/glow/reflection/resolver/ConstructorResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */