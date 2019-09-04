package at.mario.masterbuilders.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

// Getestet auf 1.8.9 & 1.9.2 -> Keine Probleme
public class PackageSender {

	// Für Reflection
	private static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<Class<?>, Class<?>>();

	// Packetnamen & ChatComponent
	private static Class<?> packetPlayOutTitle, packetPlayOutTitleEnumClass, packetPlayOutPlayerListHeaderFooter,
			packetPlayOutChat, packetPlayOutWorldBorder, packetPlayOutWorldBorderEnumClass, damageSourceClass,
			chatComponent, worldBorderClass;

	// Konstruktoren
	private static Constructor<?> packetPlayOutTitleConstructor, packetPlayOutSubtitleConstructor,
			packetPlayOutPlayerListHeaderFooterConstructor, packetPlayOutChatConstructor,
			packetPlayOutChatActionbarConstructor, packetPlayOutWorldBorderConstructor, chatComponentConstructor;

	// Version
	private static final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];

	// Für die Initalisierung
	private static boolean inited = false;

	static {

		try {

			// Init Classes
			packetPlayOutTitle = getNMSClass("PacketPlayOutTitle");
			packetPlayOutTitleEnumClass = packetPlayOutTitle.getDeclaredClasses()[0];
			packetPlayOutPlayerListHeaderFooter = getNMSClass("PacketPlayOutPlayerListHeaderFooter");
			packetPlayOutChat = getNMSClass("PacketPlayOutChat");
			packetPlayOutWorldBorder = getNMSClass("PacketPlayOutWorldBorder");
			packetPlayOutWorldBorderEnumClass = packetPlayOutWorldBorder.getDeclaredClasses()[1];
			damageSourceClass = getNMSClass("DamageSource");
			worldBorderClass = getNMSClass("WorldBorder");
			chatComponent = getNMSClass("IChatBaseComponent");

			// Konstruktoren initalisieren
			packetPlayOutSubtitleConstructor = packetPlayOutTitle.getConstructor(packetPlayOutTitleEnumClass,
					chatComponent);
			packetPlayOutTitleConstructor = packetPlayOutTitle.getConstructor(packetPlayOutTitleEnumClass,
					chatComponent, int.class, int.class, int.class);
			packetPlayOutPlayerListHeaderFooterConstructor = packetPlayOutPlayerListHeaderFooter.getConstructor();
			packetPlayOutChatConstructor = packetPlayOutChat.getConstructor(chatComponent);
			packetPlayOutChatActionbarConstructor = packetPlayOutChat.getConstructor(chatComponent, byte.class);
			packetPlayOutWorldBorderConstructor = packetPlayOutWorldBorder.getConstructor(worldBorderClass,
					packetPlayOutWorldBorderEnumClass);
			chatComponentConstructor = getNMSClass("ChatComponentText").getConstructor(String.class);

			// Check
			inited = true;
		} catch (Exception e) {
			inited = false;
		}
	}

	public static void setLastDamageCauseByPlayer(Player damageGetter, Player damageDealer, float damage) {
		if (!inited)
			return;
		try {
			Object handleGetter = getHandle(damageGetter);
			Class<?> handleClass = handleGetter.getClass();
			Object handleDamageDealer = getHandle(damageDealer);
			Method damageEntity = handleClass.getMethod("damageEntity", damageSourceClass, float.class);
			Method playerAttack = damageSourceClass.getMethod("playerAttack", getNMSClass("EntityHuman"));
			damageEntity.invoke(handleGetter, playerAttack.invoke(null, handleDamageDealer), damage);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void sendWorldBorder(Player player, double size, Location center) {
		if (!inited)
			return;
		try {
			Object handle = getHandle(player);
			Object worldBorder = worldBorderClass.newInstance();
			Object connection = getField(handle.getClass(), "playerConnection").get(handle);
			Method sendPacket = getMethod(connection.getClass(), "sendPacket", new Class[0]);

			Field borderSize = getField(worldBorder.getClass(), "d");
			borderSize.setAccessible(true);
			borderSize.set(worldBorder, size);

			Method setCenter = getMethod("setCenter", worldBorder.getClass(), double.class, double.class);
			setCenter.invoke(worldBorder, center.getX(), center.getZ());

			Method setSize = getMethod("setSize", worldBorder.getClass(), double.class);
			setSize.invoke(worldBorder, size);

			Object packet = packetPlayOutWorldBorderConstructor.newInstance(worldBorder,
					Enum.valueOf((Class<Enum>) packetPlayOutWorldBorderEnumClass, "INITIALIZE"));

			sendPacket.invoke(connection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void sendWorldBorder(Player player, double size, double minX, double minZ, double maxX, double maxZ) {
		if (!inited)
			return;
		double midX = (maxX + minX) / 2;
		double midZ = (maxZ + minZ) / 2;
		try {
			Object handle = getHandle(player);
			Object worldBorder = worldBorderClass.newInstance();
			Object connection = getField(handle.getClass(), "playerConnection").get(handle);
			Method sendPacket = getMethod(connection.getClass(), "sendPacket", new Class[0]);

			Field borderSize = getField(worldBorder.getClass(), "d");
			borderSize.setAccessible(true);
			borderSize.set(worldBorder, size);

			Method setCenter = getMethod("setCenter", worldBorder.getClass(), double.class, double.class);
			setCenter.invoke(worldBorder, midX, midZ);

			Method setSize = getMethod("setSize", worldBorder.getClass(), double.class);
			setSize.invoke(worldBorder, size);

			Object packet = packetPlayOutWorldBorderConstructor.newInstance(worldBorder,
					Enum.valueOf((Class<Enum>) packetPlayOutWorldBorderEnumClass, "INITIALIZE"));

			sendPacket.invoke(connection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		if (!inited)
			return;
		try {
			Object handle = getHandle(player);
			Object connection = getField(handle.getClass(), "playerConnection").get(handle);
			Method sendPacket = getMethod(connection.getClass(), "sendPacket", new Class[0]);
			Object titleJSON = chatComponentConstructor.newInstance(title);
			Object subtitleJSON = chatComponentConstructor.newInstance(subtitle);
			Object packet = packetPlayOutTitleConstructor.newInstance(
					Enum.valueOf((Class<Enum>) packetPlayOutTitleEnumClass, "TITLE"), titleJSON, fadeIn, stay, fadeOut);
			Object subtitlePacket = packetPlayOutSubtitleConstructor
					.newInstance(Enum.valueOf((Class<Enum>) packetPlayOutTitleEnumClass, "SUBTITLE"), subtitleJSON);
			sendPacket.invoke(connection, packet);
			sendPacket.invoke(connection, subtitlePacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendHeaderAndFooter(Player p, String head, String foot) {
		if (!inited)
			return;
		try {
			Object handle = getHandle(p);
			Object connection = getField(handle.getClass(), "playerConnection").get(handle);
			Method sendPacket = getMethod(connection.getClass(), "sendPacket",
					Class.forName("net.minecraft.server." + version + ".Packet"));
			Object componentHeader = chatComponentConstructor.newInstance(head);
			Object componentFooter = chatComponentConstructor.newInstance(foot);
			Object packet = packetPlayOutPlayerListHeaderFooterConstructor.newInstance();
			Field a = packet.getClass().getDeclaredField("a");
			a.setAccessible(true);
			a.set(packet, componentHeader);

			Field b = packet.getClass().getDeclaredField("b");
			b.setAccessible(true);
			b.set(packet, componentFooter);

			sendPacket.invoke(connection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendMessageWithChatHover(Player player, String text, String show, String cmd) {
		if (!inited)
			return;
		try {
			Object handle = getHandle(player);
			Object connection = getField(handle.getClass(), "playerConnection").get(handle);
			Method sendPacket = getMethod(connection.getClass(), "sendPacket", new Class[0]);
			Object component = chatComponent.getDeclaredClasses()[0]
					.getDeclaredMethod("a", new Class[] { String.class }).invoke(chatComponent.getDeclaredClasses()[0],
							new Object[] { "{\"text\":\"\",\"extra\":[{\"text\":\"" + text
									+ "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + show
									+ "\"},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + cmd + "\"}}]}" });
			Object packet = packetPlayOutChatConstructor.newInstance(new Object[] { component });

			sendPacket.invoke(connection, new Object[] { packet });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendMessageWithChatHoverWithoutCommand(Player player, String text, String show) {
		if (!inited)
			return;
		try {
			Object handle = getHandle(player);
			Object connection = getField(handle.getClass(), "playerConnection").get(handle);
			Method sendPacket = getMethod(connection.getClass(), "sendPacket", new Class[0]);
			Object component = chatComponent.getDeclaredClasses()[0]
					.getDeclaredMethod("a", new Class[] { String.class }).invoke(chatComponent.getDeclaredClasses()[0],
							new Object[] { "{\"text\":\"\",\"extra\":[{\"text\":\"" + text
									+ "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + show + "\"}}]}" });
			Object packet = packetPlayOutChatConstructor.newInstance(new Object[] { component });

			sendPacket.invoke(connection, new Object[] { packet });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendActionbar(Player player, String msg) {
		if (!inited)
			return;
		try {
			String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
			Object handle = getHandle(player);
			Object connection = getField(handle.getClass(), "playerConnection").get(handle);
			Method sendPacket = getMethod(connection.getClass(), "sendPacket",
					Class.forName("net.minecraft.server." + version + ".Packet"));
			Object chat = chatComponentConstructor.newInstance(msg);
			Object packet = packetPlayOutChatActionbarConstructor.newInstance(chat, (byte) 2);

			sendPacket.invoke(connection, packet);

		} catch (Exception e) {

		}

	}

	@SuppressWarnings("rawtypes")
	private static Class<?> getPrimitiveType(Class<?> clazz) {
		return CORRESPONDING_TYPES.containsKey(clazz) ? (Class) CORRESPONDING_TYPES.get(clazz) : clazz;
	}

	@SuppressWarnings("rawtypes")
	private static Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
		int a = classes != null ? classes.length : 0;
		Class[] types = new Class[a];
		for (int i = 0; i < a; i++) {
			types[i] = getPrimitiveType(classes[i]);
		}
		return types;
	}

	private static boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
		if (a.length != o.length) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			if ((!a[i].equals(o[i])) && (!a[i].isAssignableFrom(o[i]))) {
				return false;
			}
		}
		return true;
	}

	private static Object getHandle(Object obj) {
		try {
			return getMethod("getHandle", obj.getClass(), new Class[0]).invoke(obj, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private static Method getMethod(String name, Class<?> clazz, Class<?>... paramTypes) {
		Class[] t = toPrimitiveTypeArray(paramTypes);
		Method[] arrayOfMethod;
		int j = (arrayOfMethod = clazz.getMethods()).length;
		for (int i = 0; i < j; i++) {
			Method m = arrayOfMethod[i];
			Class[] types = toPrimitiveTypeArray(m.getParameterTypes());
			if ((m.getName().equals(name)) && (equalsTypeArray(types, t))) {
				return m;
			}
		}
		return null;
	}

	private static String getVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		return version;
	}

	private static Class<?> getNMSClass(String className) {
		String fullName = "net.minecraft.server." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	private static Field getField(Class<?> clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
		Method[] arrayOfMethod;
		int j = (arrayOfMethod = clazz.getMethods()).length;
		for (int i = 0; i < j; i++) {
			Method m = arrayOfMethod[i];
			if ((m.getName().equals(name)) && ((args.length == 0) || (ClassListEqual(args, m.getParameterTypes())))) {
				m.setAccessible(true);
				return m;
			}
		}
		return null;
	}

	private static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
		boolean equal = true;
		if (l1.length != l2.length) {
			return false;
		}
		for (int i = 0; i < l1.length; i++) {
			if (l1[i] != l2[i]) {
				equal = false;
				break;
			}
		}
		return equal;
	}

	public void setValue(Object obj, String name, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception localException) {
		}
	}

	public Object getValue(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception localException) {
		}
		return null;
	}
}
