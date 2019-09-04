package at.mario.masterbuilders.pictureLogin.me.itsnathang.picturelogin.config;

import java.io.File;

import at.mario.masterbuilders.Main;

public class FallbackPicture {
    private final Main plugin;

    public FallbackPicture(Main plugin2) {
        this.plugin = plugin2;
    }

    public File get() {
        final String FALLBACK_PATH = plugin.getDataFolder() + File.separator + "fallback.png";

        File image = new File(FALLBACK_PATH);

        if (!image.exists())
            plugin.saveResource("fallback.png", false);

        if (Main.getInstance().getConfig().getBoolean("fallback", true))
            image = new File(FALLBACK_PATH);
        else
            image = null;

        return image;
    }

}
