/*     */ package org.inventivetalent.reflection.minecraft;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.inventivetalent.reflection.resolver.ConstructorResolver;
/*     */ import org.inventivetalent.reflection.resolver.FieldResolver;
/*     */ import org.inventivetalent.reflection.resolver.MethodResolver;
/*     */ import org.inventivetalent.reflection.resolver.minecraft.OBCClassResolver;
/*     */ import org.inventivetalent.reflection.util.AccessUtil;
/*     */ import sun.reflect.ConstructorAccessor;
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
/*     */ public class Minecraft
/*     */ {
/*  50 */   static final Pattern NUMERIC_VERSION_PATTERN = Pattern.compile("v([0-9])_([0-9])*_R([0-9])");
/*     */   
/*     */   public static final Version VERSION;
/*     */   
/*  54 */   private static OBCClassResolver obcClassResolver = new OBCClassResolver();
/*     */   private static Class<?> CraftEntity;
/*     */   
/*     */   static {
/*  58 */     VERSION = Version.getVersion();
/*  59 */     System.out.println("[ReflectionHelper] Version is " + VERSION);
/*     */     try
/*     */     {
/*  62 */       CraftEntity = obcClassResolver.resolve(new String[] { "entity.CraftEntity" });
/*     */     } catch (ReflectiveOperationException e) {
/*  64 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String getVersion()
/*     */   {
/*  72 */     return VERSION.name() + ".";
/*     */   }
/*     */   
/*     */   public static Object getHandle(Object object) throws ReflectiveOperationException {
/*     */     Method method;
/*     */     try {
/*  78 */       method = AccessUtil.setAccessible(object.getClass().getDeclaredMethod("getHandle", new Class[0]));
/*     */     } catch (ReflectiveOperationException e) { Method method;
/*  80 */       method = AccessUtil.setAccessible(CraftEntity.getDeclaredMethod("getHandle", new Class[0]));
/*     */     }
/*  82 */     return method.invoke(object, new Object[0]);
/*     */   }
/*     */   
/*     */   public static Entity getBukkitEntity(Object object) throws ReflectiveOperationException {
/*     */     Method method;
/*     */     try {
/*  88 */       method = AccessUtil.setAccessible(object.getClass().getDeclaredMethod("getBukkitEntity", new Class[0]));
/*     */     } catch (ReflectiveOperationException e) { Method method;
/*  90 */       method = AccessUtil.setAccessible(CraftEntity.getDeclaredMethod("getHandle", new Class[0]));
/*     */     }
/*  92 */     return (Entity)method.invoke(object, new Object[0]);
/*     */   }
/*     */   
/*     */   public static Object getHandleSilent(Object object) {
/*     */     try {
/*  97 */       return getHandle(object);
/*     */     }
/*     */     catch (Exception localException) {}
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   public static enum Version {
/* 104 */     UNKNOWN(-1), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 111 */     v1_7_R1(10701), 
/* 112 */     v1_7_R2(10702), 
/* 113 */     v1_7_R3(10703), 
/* 114 */     v1_7_R4(10704), 
/*     */     
/* 116 */     v1_8_R1(10801), 
/* 117 */     v1_8_R2(10802), 
/* 118 */     v1_8_R3(10803), 
/*     */     
/* 120 */     v1_8_R4(10804), 
/*     */     
/* 122 */     v1_9_R1(10901), 
/* 123 */     v1_9_R2(10902), 
/*     */     
/* 125 */     v1_10_R1(11001);
/*     */     
/*     */     private int version;
/*     */     
/*     */     private Version(int version) {
/* 130 */       this.version = version;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public int version()
/*     */     {
/* 137 */       return this.version;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean olderThan(Version version)
/*     */     {
/* 145 */       return version() < version.version();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean newerThan(Version version)
/*     */     {
/* 153 */       return version() >= version.version();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean inRange(Version oldVersion, Version newVersion)
/*     */     {
/* 162 */       return (newerThan(oldVersion)) && (olderThan(newVersion));
/*     */     }
/*     */     
/*     */     public boolean matchesPackageName(String packageName) {
/* 166 */       return packageName.toLowerCase().contains(name().toLowerCase());
/*     */     }
/*     */     
/*     */     public static Version getVersion() {
/* 170 */       String name = Bukkit.getServer().getClass().getPackage().getName();
/* 171 */       String versionPackage = name.substring(name.lastIndexOf('.') + 1) + ".";
/* 172 */       for (Version version : values()) {
/* 173 */         if (version.matchesPackageName(versionPackage)) return version;
/*     */       }
/* 175 */       System.err.println("[ReflectionHelper] Failed to find version enum for '" + name + "'/'" + versionPackage + "'");
/*     */       
/* 177 */       System.out.println("[ReflectionHelper] Generating dynamic constant...");
/* 178 */       Matcher matcher = Minecraft.NUMERIC_VERSION_PATTERN.matcher(versionPackage);
/* 179 */       while (matcher.find()) {
/* 180 */         if (matcher.groupCount() >= 3)
/*     */         {
/* 182 */           String majorString = matcher.group(1);
/* 183 */           String minorString = matcher.group(2);
/* 184 */           if (minorString.length() == 1) minorString = "0" + minorString;
/* 185 */           String patchString = matcher.group(3);
/* 186 */           if (patchString.length() == 1) { patchString = "0" + patchString;
/*     */           }
/* 188 */           String numVersionString = majorString + minorString + patchString;
/* 189 */           int numVersion = Integer.parseInt(numVersionString);
/* 190 */           String packge = versionPackage.substring(0, versionPackage.length() - 1);
/*     */           
/*     */           try
/*     */           {
/* 194 */             Field valuesField = new FieldResolver(Version.class).resolve(new String[] { "$VALUES" });
/* 195 */             Version[] oldValues = (Version[])valuesField.get(null);
/* 196 */             Version[] newValues = new Version[oldValues.length + 1];
/* 197 */             System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
/* 198 */             Version dynamicVersion = (Version)Minecraft.newEnumInstance(Version.class, new Class[] { String.class, Integer.TYPE, Integer.TYPE }, new Object[] { packge, 
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 203 */               Integer.valueOf(newValues.length - 1), 
/* 204 */               Integer.valueOf(numVersion) });
/* 205 */             newValues[(newValues.length - 1)] = dynamicVersion;
/* 206 */             valuesField.set(null, newValues);
/*     */             
/* 208 */             System.out.println("[ReflectionHelper] Injected dynamic version " + packge + " (#" + numVersion + ").");
/* 209 */             System.out.println("[ReflectionHelper] Please inform inventivetalent about the outdated version, as this is not guaranteed to work.");
/* 210 */             return dynamicVersion;
/*     */           } catch (ReflectiveOperationException e) {
/* 212 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/* 216 */       return UNKNOWN;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 221 */       return name() + " (" + version() + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   public static Object newEnumInstance(Class clazz, Class[] types, Object[] values) throws ReflectiveOperationException {
/* 226 */     Constructor constructor = new ConstructorResolver(clazz).resolve(new Class[][] { types });
/* 227 */     Field accessorField = new FieldResolver(Constructor.class).resolve(new String[] { "constructorAccessor" });
/* 228 */     ConstructorAccessor constructorAccessor = (ConstructorAccessor)accessorField.get(constructor);
/* 229 */     if (constructorAccessor == null) {
/* 230 */       new MethodResolver(Constructor.class).resolve(new String[] { "acquireConstructorAccessor" }).invoke(constructor, new Object[0]);
/* 231 */       constructorAccessor = (ConstructorAccessor)accessorField.get(constructor);
/*     */     }
/* 233 */     return constructorAccessor.newInstance(values);
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/reflection/minecraft/Minecraft.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */