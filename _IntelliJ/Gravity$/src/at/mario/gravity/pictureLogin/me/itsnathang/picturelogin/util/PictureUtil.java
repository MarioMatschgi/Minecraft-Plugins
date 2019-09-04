package at.mario.gravity.pictureLogin.me.itsnathang.picturelogin.util;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import at.mario.gravity.Main;
import at.mario.gravity.pictureLogin.com.bobacadodl.imgmessage.ImageMessage;
import at.mario.gravity.pictureLogin.me.itsnathang.picturelogin.config.FallbackPicture;
import me.clip.placeholderapi.PlaceholderAPI;

public class PictureUtil {
	private final Main plugin;
	
	public PictureUtil(Main plugin) {
		this.plugin = plugin;
	}
	
	private URL newURL(String player_uuid, String player_name) {
		String url = Main.getInstance().getURL()
				.replace("%uuid%" , player_uuid)
				.replace("%pname%", player_name);

		try {
			return new URL(url);
		} catch (Exception e) { return null; }
	}
	
	private BufferedImage getImage(Player player) {
		URL head_image = newURL(player.getUniqueId().toString(), player.getName());

		// URL Formatted correctly.
		if (head_image != null) {
            try {
                return ImageIO.read(head_image);
            } catch (Exception e) {
                e.printStackTrace();
            }
		}

		// Incorrectly formatted URL or couldn't load from URL
        try {
		    return ImageIO.read(new FallbackPicture(plugin).get());
        } catch (Exception e) {
            e.printStackTrace();
		    return null;
        }
	}
	public ImageMessage createPictureMessage(Player player, List<String> messages) {
		BufferedImage image = getImage(player);

		if (image == null) return null;

		messages.replaceAll((message) -> addPlaceholders(message, player));
		
		return Main.getInstance().getMessage(messages, image);
	}

	public void sendOutPictureMessage(ImageMessage picture_message) {
        plugin.getServer().getOnlinePlayers().forEach((online_player) -> {
            picture_message.sendToPlayer(online_player);
        });
    }

	public void sendNOPictureMessage(ImageMessage picture_message) {
        plugin.getServer().getOnlinePlayers().forEach((online_player) -> {
            // picture_message.sendToPlayer(online_player);
        });
    }

    // String Utility Functions

	@SuppressWarnings("deprecation")
	private String addPlaceholders(String msg, Player player) {
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		msg = msg.replace("%pname%", player.getName());
		msg = msg.replace("%uuid%", player.getUniqueId().toString());
		msg = msg.replace("%online%", String.valueOf(plugin.getServer().getOnlinePlayers().size()));
		msg = msg.replace("%max%", String.valueOf(plugin.getServer().getMaxPlayers()));
		msg = msg.replace("%motd%", plugin.getServer().getMotd());
		msg = msg.replace("%displayname%", player.getDisplayName());

		
			msg = PlaceholderAPI.setPlaceholders(player, msg);

		return msg;
	}

    public void clearChat(Player player) {
        for (int i = 0; i < 20; i++) {
            player.sendMessage("");
        }
    }
	
}
