/*     */ package org.inventivetalent.reflection.minecraft;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.annotation.Nullable;
/*     */ import org.inventivetalent.reflection.resolver.ClassResolver;
/*     */ import org.inventivetalent.reflection.resolver.ConstructorResolver;
/*     */ import org.inventivetalent.reflection.resolver.FieldResolver;
/*     */ import org.inventivetalent.reflection.resolver.MethodResolver;
/*     */ import org.inventivetalent.reflection.resolver.ResolverQuery;
/*     */ import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;
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
/*     */ public class DataWatcher
/*     */ {
/*  42 */   static ClassResolver classResolver = new ClassResolver();
/*  43 */   static NMSClassResolver nmsClassResolver = new NMSClassResolver();
/*     */   
/*  45 */   static Class<?> ItemStack = nmsClassResolver.resolveSilent(new String[] { "ItemStack" });
/*  46 */   static Class<?> ChunkCoordinates = nmsClassResolver.resolveSilent(new String[] { "ChunkCoordinates" });
/*  47 */   static Class<?> BlockPosition = nmsClassResolver.resolveSilent(new String[] { "BlockPosition" });
/*  48 */   static Class<?> Vector3f = nmsClassResolver.resolveSilent(new String[] { "Vector3f" });
/*  49 */   static Class<?> DataWatcher = nmsClassResolver.resolveSilent(new String[] { "DataWatcher" });
/*  50 */   static Class<?> Entity = nmsClassResolver.resolveSilent(new String[] { "Entity" });
/*  51 */   static Class<?> TIntObjectMap = classResolver.resolveSilent(new String[] { "gnu.trove.map.TIntObjectMap", "net.minecraft.util.gnu.trove.map.TIntObjectMap" });
/*     */   
/*  53 */   static ConstructorResolver DataWacherConstructorResolver = new ConstructorResolver(DataWatcher);
/*     */   
/*  55 */   static FieldResolver DataWatcherFieldResolver = new FieldResolver(DataWatcher);
/*     */   
/*  57 */   static MethodResolver TIntObjectMapMethodResolver = new MethodResolver(TIntObjectMap);
/*  58 */   static MethodResolver DataWatcherMethodResolver = new MethodResolver(DataWatcher);
/*     */   
/*     */   public static Object newDataWatcher(@Nullable Object entity) throws ReflectiveOperationException {
/*  61 */     return DataWacherConstructorResolver.resolve(new Class[][] { { Entity } }).newInstance(new Object[] { entity });
/*     */   }
/*     */   
/*     */   public static Object setValue(Object dataWatcher, int index, Object dataWatcherObject, Object value) throws ReflectiveOperationException {
/*  65 */     if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
/*  66 */       return V1_8.setValue(dataWatcher, index, value);
/*     */     }
/*  68 */     return V1_9.setItem(dataWatcher, index, dataWatcherObject, value);
/*     */   }
/*     */   
/*     */   public static Object setValue(Object dataWatcher, int index, DataWatcher.V1_9.ValueType type, Object value) throws ReflectiveOperationException
/*     */   {
/*  73 */     return setValue(dataWatcher, index, type.getType(), value);
/*     */   }
/*     */   
/*     */   public static Object setValue(Object dataWatcher, int index, Object value, FieldResolver dataWatcherObjectFieldResolver, String... dataWatcherObjectFieldNames) throws ReflectiveOperationException {
/*  77 */     if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
/*  78 */       return V1_8.setValue(dataWatcher, index, value);
/*     */     }
/*  80 */     Object dataWatcherObject = dataWatcherObjectFieldResolver.resolve(dataWatcherObjectFieldNames).get(null);
/*  81 */     return V1_9.setItem(dataWatcher, index, dataWatcherObject, value);
/*     */   }
/*     */   
/*     */   public static Object getValue(DataWatcher dataWatcher, int index) throws ReflectiveOperationException
/*     */   {
/*  86 */     if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
/*  87 */       return V1_8.getValue(dataWatcher, index);
/*     */     }
/*  89 */     return V1_9.getValue(dataWatcher, Integer.valueOf(index));
/*     */   }
/*     */   
/*     */ 
/*     */   public static int getValueType(Object value)
/*     */   {
/*  95 */     int type = 0;
/*  96 */     if ((value instanceof Number)) {
/*  97 */       if ((value instanceof Byte)) {
/*  98 */         type = 0;
/*  99 */       } else if ((value instanceof Short)) {
/* 100 */         type = 1;
/* 101 */       } else if ((value instanceof Integer)) {
/* 102 */         type = 2;
/* 103 */       } else if ((value instanceof Float)) {
/* 104 */         type = 3;
/*     */       }
/* 106 */     } else if ((value instanceof String)) {
/* 107 */       type = 4;
/* 108 */     } else if ((value != null) && (value.getClass().equals(ItemStack))) {
/* 109 */       type = 5;
/* 110 */     } else if ((value != null) && ((value.getClass().equals(ChunkCoordinates)) || (value.getClass().equals(BlockPosition)))) {
/* 111 */       type = 6;
/* 112 */     } else if ((value != null) && (value.getClass().equals(Vector3f))) {
/* 113 */       type = 7;
/*     */     }
/*     */     
/* 116 */     return type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class V1_9
/*     */   {
/* 124 */     static Class<?> DataWatcherItem = DataWatcher.nmsClassResolver.resolveSilent(new String[] { "DataWatcher$Item" });
/* 125 */     static Class<?> DataWatcherObject = DataWatcher.nmsClassResolver.resolveSilent(new String[] { "DataWatcherObject" });
/*     */     static ConstructorResolver DataWatcherItemConstructorResolver;
/*     */     static FieldResolver DataWatcherItemFieldResolver;
/*     */     static FieldResolver DataWatcherObjectFieldResolver;
/*     */     
/*     */     public static Object newDataWatcherItem(Object dataWatcherObject, Object value)
/*     */       throws ReflectiveOperationException
/*     */     {
/* 133 */       if (DataWatcherItemConstructorResolver == null) DataWatcherItemConstructorResolver = new ConstructorResolver(DataWatcherItem);
/* 134 */       return DataWatcherItemConstructorResolver.resolveFirstConstructor().newInstance(new Object[] { dataWatcherObject, value });
/*     */     }
/*     */     
/*     */     public static Object setItem(Object dataWatcher, int index, Object dataWatcherObject, Object value) throws ReflectiveOperationException {
/* 138 */       return setItem(dataWatcher, index, newDataWatcherItem(dataWatcherObject, value));
/*     */     }
/*     */     
/*     */     public static Object setItem(Object dataWatcher, int index, Object dataWatcherItem) throws ReflectiveOperationException {
/* 142 */       Map<Integer, Object> map = (Map)DataWatcher.DataWatcherFieldResolver.resolve(new String[] { "c" }).get(dataWatcher);
/* 143 */       map.put(Integer.valueOf(index), dataWatcherItem);
/* 144 */       return dataWatcher;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public static Object getItem(Object dataWatcher, Object dataWatcherObject)
/*     */       throws ReflectiveOperationException
/*     */     {
/* 153 */       return DataWatcher.DataWatcherMethodResolver.resolve(new ResolverQuery[] { new ResolverQuery("c", new Class[] { DataWatcherObject }) }).invoke(dataWatcher, new Object[] { dataWatcherObject });
/*     */     }
/*     */     
/*     */     public static Object getValue(Object dataWatcher, Object dataWatcherObject) throws ReflectiveOperationException {
/* 157 */       return DataWatcher.DataWatcherMethodResolver.resolve(new String[] { "get" }).invoke(dataWatcher, new Object[] { dataWatcherObject });
/*     */     }
/*     */     
/*     */     public static Object getValue(Object dataWatcher, ValueType type) throws ReflectiveOperationException {
/* 161 */       return getValue(dataWatcher, type.getType());
/*     */     }
/*     */     
/*     */     public static Object getItemObject(Object item) throws ReflectiveOperationException {
/* 165 */       if (DataWatcherItemFieldResolver == null) DataWatcherItemFieldResolver = new FieldResolver(DataWatcherItem);
/* 166 */       return DataWatcherItemFieldResolver.resolve(new String[] { "a" }).get(item);
/*     */     }
/*     */     
/*     */     public static int getItemIndex(Object dataWatcher, Object item) throws ReflectiveOperationException {
/* 170 */       int index = -1;
/* 171 */       Map<Integer, Object> map = (Map)DataWatcher.DataWatcherFieldResolver.resolve(new String[] { "c" }).get(dataWatcher);
/* 172 */       for (Map.Entry<Integer, Object> entry : map.entrySet()) {
/* 173 */         if (entry.getValue().equals(item)) {
/* 174 */           index = ((Integer)entry.getKey()).intValue();
/* 175 */           break;
/*     */         }
/*     */       }
/* 178 */       return index;
/*     */     }
/*     */     
/*     */     public static Type getItemType(Object item) throws ReflectiveOperationException {
/* 182 */       if (DataWatcherObjectFieldResolver == null) DataWatcherObjectFieldResolver = new FieldResolver(DataWatcherObject);
/* 183 */       Object object = getItemObject(item);
/* 184 */       Object serializer = DataWatcherObjectFieldResolver.resolve(new String[] { "b" }).get(object);
/* 185 */       Type[] genericInterfaces = serializer.getClass().getGenericInterfaces();
/* 186 */       if (genericInterfaces.length > 0) {
/* 187 */         Type type = genericInterfaces[0];
/* 188 */         if ((type instanceof ParameterizedType)) {
/* 189 */           Type[] actualTypes = ((ParameterizedType)type).getActualTypeArguments();
/* 190 */           if (actualTypes.length > 0) {
/* 191 */             return actualTypes[0];
/*     */           }
/*     */         }
/*     */       }
/* 195 */       return null;
/*     */     }
/*     */     
/*     */     public static Object getItemValue(Object item) throws ReflectiveOperationException {
/* 199 */       if (DataWatcherItemFieldResolver == null) DataWatcherItemFieldResolver = new FieldResolver(DataWatcherItem);
/* 200 */       return DataWatcherItemFieldResolver.resolve(new String[] { "b" }).get(item);
/*     */     }
/*     */     
/*     */     public static void setItemValue(Object item, Object value) throws ReflectiveOperationException {
/* 204 */       DataWatcherItemFieldResolver.resolve(new String[] { "b" }).set(item, value);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public static enum ValueType
/*     */     {
/* 212 */       ENTITY_FLAG("Entity", 57), 
/*     */       
/*     */ 
/*     */ 
/* 216 */       ENTITY_AIR_TICKS("Entity", 58), 
/*     */       
/*     */ 
/*     */ 
/* 220 */       ENTITY_NAME("Entity", 59), 
/*     */       
/*     */ 
/*     */ 
/* 224 */       ENTITY_NAME_VISIBLE("Entity", 60), 
/*     */       
/*     */ 
/*     */ 
/* 228 */       ENTITY_SILENT("Entity", 61), 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 233 */       ENTITY_as("EntityLiving", 2), 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 238 */       ENTITY_LIVING_HEALTH("EntityLiving", new String[] { "HEALTH" }), 
/*     */       
/*     */ 
/* 241 */       ENTITY_LIVING_f("EntityLiving", 2), 
/*     */       
/*     */ 
/* 244 */       ENTITY_LIVING_g("EntityLiving", 3), 
/*     */       
/*     */ 
/* 247 */       ENTITY_LIVING_h("EntityLiving", 4), 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 254 */       ENTITY_INSENTIENT_FLAG("EntityInsentient", 0), 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 261 */       ENTITY_SLIME_SIZE("EntitySlime", 0), 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 266 */       ENTITY_WITHER_a("EntityWither", 0), 
/*     */       
/*     */ 
/* 269 */       ENTITY_WIHER_b("EntityWither", 1), 
/*     */       
/*     */ 
/* 272 */       ENTITY_WITHER_c("EntityWither", 2), 
/*     */       
/*     */ 
/* 275 */       ENTITY_WITHER_bv("EntityWither", 3), 
/*     */       
/*     */ 
/* 278 */       ENTITY_WITHER_bw("EntityWither", 4), 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 285 */       ENTITY_HUMAN_ABSORPTION_HEARTS("EntityHuman", 0), 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 290 */       ENTITY_HUMAN_SCORE("EntityHuman", 1), 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 295 */       ENTITY_HUMAN_SKIN_LAYERS("EntityHuman", 2), 
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 300 */       ENTITY_HUMAN_MAIN_HAND("EntityHuman", 3);
/*     */       
/*     */       private Object type;
/*     */       
/*     */       private ValueType(String className, String... fieldNames) {
/*     */         try {
/* 306 */           this.type = new FieldResolver(DataWatcher.nmsClassResolver.resolve(new String[] { className })).resolve(fieldNames).get(null);
/*     */         } catch (Exception e) {
/* 308 */           if (Minecraft.VERSION.newerThan(Minecraft.Version.v1_9_R1)) {
/* 309 */             System.err.println("[ReflectionHelper] Failed to find DataWatcherObject for " + className + " " + Arrays.toString(fieldNames));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       private ValueType(String className, int index) {
/*     */         try {
/* 316 */           this.type = new FieldResolver(DataWatcher.nmsClassResolver.resolve(new String[] { className })).resolveIndex(index).get(null);
/*     */         } catch (Exception e) {
/* 318 */           if (Minecraft.VERSION.newerThan(Minecraft.Version.v1_9_R1)) {
/* 319 */             System.err.println("[ReflectionHelper] Failed to find DataWatcherObject for " + className + " #" + index);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       public boolean hasType() {
/* 325 */         return getType() != null;
/*     */       }
/*     */       
/*     */       public Object getType() {
/* 329 */         return this.type;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class V1_8
/*     */   {
/* 339 */     static Class<?> WatchableObject = DataWatcher.nmsClassResolver.resolveSilent(new String[] { "WatchableObject", "DataWatcher$WatchableObject" });
/*     */     static ConstructorResolver WatchableObjectConstructorResolver;
/*     */     static FieldResolver WatchableObjectFieldResolver;
/*     */     
/*     */     public static Object newWatchableObject(int index, Object value)
/*     */       throws ReflectiveOperationException
/*     */     {
/* 346 */       return newWatchableObject(DataWatcher.getValueType(value), index, value);
/*     */     }
/*     */     
/*     */     public static Object newWatchableObject(int type, int index, Object value) throws ReflectiveOperationException {
/* 350 */       if (WatchableObjectConstructorResolver == null) { WatchableObjectConstructorResolver = new ConstructorResolver(WatchableObject);
/*     */       }
/*     */       
/*     */ 
/* 354 */       return WatchableObjectConstructorResolver.resolve(new Class[][] { { Integer.TYPE, Integer.TYPE, Object.class } }).newInstance(new Object[] { Integer.valueOf(type), Integer.valueOf(index), value });
/*     */     }
/*     */     
/*     */     public static Object setValue(Object dataWatcher, int index, Object value) throws ReflectiveOperationException {
/* 358 */       int type = DataWatcher.getValueType(value);
/*     */       
/* 360 */       Object map = DataWatcher.DataWatcherFieldResolver.resolve(new String[] { "dataValues" }).get(dataWatcher);
/* 361 */       DataWatcher.TIntObjectMapMethodResolver.resolve(new ResolverQuery[] { new ResolverQuery("put", new Class[] { Integer.TYPE, Object.class }) }).invoke(map, new Object[] { Integer.valueOf(index), newWatchableObject(type, index, value) });
/*     */       
/* 363 */       return dataWatcher;
/*     */     }
/*     */     
/*     */     public static Object getValue(Object dataWatcher, int index) throws ReflectiveOperationException {
/* 367 */       Object map = DataWatcher.DataWatcherFieldResolver.resolve(new String[] { "dataValues" }).get(dataWatcher);
/*     */       
/* 369 */       return DataWatcher.TIntObjectMapMethodResolver.resolve(new ResolverQuery[] { new ResolverQuery("get", new Class[] { Integer.TYPE }) }).invoke(map, new Object[] { Integer.valueOf(index) });
/*     */     }
/*     */     
/*     */     public static int getWatchableObjectIndex(Object object) throws ReflectiveOperationException {
/* 373 */       if (WatchableObjectFieldResolver == null) WatchableObjectFieldResolver = new FieldResolver(WatchableObject);
/* 374 */       return WatchableObjectFieldResolver.resolve(new String[] { "b" }).getInt(object);
/*     */     }
/*     */     
/*     */     public static int getWatchableObjectType(Object object) throws ReflectiveOperationException {
/* 378 */       if (WatchableObjectFieldResolver == null) WatchableObjectFieldResolver = new FieldResolver(WatchableObject);
/* 379 */       return WatchableObjectFieldResolver.resolve(new String[] { "a" }).getInt(object);
/*     */     }
/*     */     
/*     */     public static Object getWatchableObjectValue(Object object) throws ReflectiveOperationException {
/* 383 */       if (WatchableObjectFieldResolver == null) WatchableObjectFieldResolver = new FieldResolver(WatchableObject);
/* 384 */       return WatchableObjectFieldResolver.resolve(new String[] { "c" }).get(object);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/reflection/minecraft/DataWatcher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */