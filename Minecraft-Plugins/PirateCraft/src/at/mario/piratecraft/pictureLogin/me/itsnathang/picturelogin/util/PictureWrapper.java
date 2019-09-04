package at.mario.piratecraft.pictureLogin.me.itsnathang.picturelogin.util;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import at.mario.piratecraft.Main;
import at.mario.piratecraft.manager.ConfigManager.MessagesManager;
import at.mario.piratecraft.pictureLogin.com.bobacadodl.imgmessage.ImageMessage;

public class PictureWrapper extends BukkitRunnable {
    private static PictureUtil pictureUtil;
    private static Player player;

    public PictureWrapper(Main plugin, Player player) {
        PictureWrapper.pictureUtil = plugin.getPictureUtil();
        PictureWrapper.player      = player;
    }

    @Override
    public void run() {
        sendImage();
    }
    
    private static ImageMessage getMessage() {
    	MessagesManager mm = new MessagesManager();

        return pictureUtil.createPictureMessage(player, mm.getMessages().getStringList("Messages.joinme.message"));
    }

    private void sendImage() {
        ImageMessage pictureMessage = getMessage();

        if (pictureMessage == null) {
        	return;
        }

        pictureUtil.sendOutPictureMessage(pictureMessage);
    }
    public void sendNOImage() {
        ImageMessage pictureMessage = getMessage();

        if (pictureMessage == null) {
        	return;
        }

        pictureUtil.sendNOPictureMessage(pictureMessage);
    }

    public static ImageMessage getImage() {
        ImageMessage pictureMessage = getMessage();

        if (pictureMessage == null) {
        	return null;
        }
        
        return pictureMessage;
    }
}
