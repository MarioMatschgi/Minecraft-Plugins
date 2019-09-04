package at.mario.utilreloaded.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {
	
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent e) {
		String originalMsg = e.getMessage();
		String formattedMsg = "";
		String originalColorFormat = ChatColor.getLastColors(originalMsg);
		if (originalColorFormat == null)
			originalColorFormat = "";

		//String previousFormat = "";
		String currentFormat = "";
		
		
		char[] chars = originalMsg.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char charAfterFormat = 0;
			
			if (chars[i] == '*') {
				if (i+1 < chars.length && chars[i+1] == '*') {
					if (i+2 < chars.length && chars[i+2] == '*') {
						// ***
						
						if (currentFormat.contains("§l§o")) {
							// Wenn bereits blod blod weggeben
							
							currentFormat = currentFormat.replace("§l§o", "");
							currentFormat += "§r" + originalColorFormat;
						} else {
							// Wenn kein blod dann dazugeben
							
							currentFormat += "§l§o";
						}
						charAfterFormat = chars[i+2];
						i += 2;
					} else {
						// Nur **
						
						if (currentFormat.contains("§l")) {
							// Wenn bereits blod blod weggeben
							
							currentFormat = currentFormat.replace("§l", "");
							currentFormat += "§r" + originalColorFormat;
						} else {
							// Wenn kein blod dann dazugeben
							
							currentFormat += "§l";
						}
						charAfterFormat = chars[i+1];
						i++;
					}
				} else {
					// Nur *
					
					if (currentFormat.contains("§o")) {
						// Wenn bereits italic italic weggeben
						
						currentFormat = currentFormat.replace("§o", "");
						currentFormat += "§r" + originalColorFormat;
					} else {
						// Wenn kein italic dann dazugeben
						
						currentFormat += "§o";
					}
				}

				formattedMsg += currentFormat;
				if (charAfterFormat != '*')
					formattedMsg += charAfterFormat;
			} else {
				// Kein Zeichen

				charAfterFormat = chars[i];
				
				formattedMsg += charAfterFormat;
			}
			
			//e.getPlayer().sendMessage(formattedMsg);
			//System.out.println(formattedMsg);
		}
		formattedMsg += originalColorFormat;
		
		
		e.setMessage(formattedMsg);
	}
}
