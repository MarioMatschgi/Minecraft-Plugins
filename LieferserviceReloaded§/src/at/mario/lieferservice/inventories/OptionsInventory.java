package at.mario.lieferservice.inventories;

import java.util.Arrays;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

import at.mario.lieferservice.Main;
import at.mario.lieferservice.Request;
import at.mario.lieferservice.RequestStatus;
import at.mario.lieferservice.manager.ConfigManagers.DataManager;
import at.mario.lieferservice.manager.ConfigManagers.MessagesManager;

public class OptionsInventory {

	public OptionsInventory() { }
	
	private static OptionsInventory instance = new OptionsInventory();
	
	public static OptionsInventory getInstance() {
		return instance;
	}
	
	@SuppressWarnings("deprecation")
	public void newInventory(Player p, Request request) {
		MessagesManager mm = new MessagesManager();
		DataManager dm = new DataManager();
		
		Inventory inv = Main.getPlugin().getServer().createInventory(null, 3 * 9, mm.getMessages().getString("Messages.gui.optionsInventory.title"));
		
		ItemStack requestNumber = new ItemStack(Material.SIGN);
		requestNumber.setAmount(1);
		ItemMeta requestNumberMeta = requestNumber.getItemMeta();
		requestNumberMeta.setDisplayName("ID: " + request.requestNumber);
		requestNumber.setItemMeta(requestNumberMeta);
		
		ItemStack back = new ItemStack(Material.BARRIER);
		back.setAmount(1);
		ItemMeta backNumberMeta = back.getItemMeta();
		backNumberMeta.setDisplayName(mm.getMessages().getString("Messages.gui.optionsInventory.back"));
		back.setItemMeta(backNumberMeta);
		
		ItemStack item = request.requestedItem.clone();
		item.setAmount(1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(request.requester.getName());
		meta.setLore(Arrays.asList(
				"§bPlayer§f: §6" + request.requester.getName(), 
				"§bLocation§f: §eX§f: §6" + Math.round(request.xToDeliver) + " §eY§f: §6" + Math.round(request.yToDeliver) + " §eZ§f: §6" + Math.round(request.zToDeliver), 
				"§bItem§f: " + request.requestedItem.getType(),
				"§bAmount§f: " + request.requestedItem.getAmount()
			));
		item.setItemMeta(meta);
		
		Dye dye = new Dye();
		
		dye.setColor(DyeColor.RED);
		ItemStack denyRequest = dye.toItemStack();
		denyRequest.setAmount(1);
		ItemMeta denyRequestMeta = denyRequest.getItemMeta();
		denyRequestMeta.setDisplayName(mm.getMessages().getString("Messages.gui.optionsInventory.deny"));
		denyRequest.setItemMeta(denyRequestMeta);
		
		dye.setColor(DyeColor.LIME);
		ItemStack finishRequest = dye.toItemStack();
		finishRequest.setAmount(1);
		ItemMeta finishRequestMeta = finishRequest.getItemMeta();
		finishRequestMeta.setDisplayName(mm.getMessages().getString("Messages.gui.optionsInventory.finish"));
		finishRequest.setItemMeta(finishRequestMeta);
		

		inv.setItem(0, requestNumber);
		inv.setItem(4, item);
		
		inv.setItem(20, finishRequest);
		inv.setItem(24, denyRequest);

		inv.setItem(26, back);
		
		request.status = RequestStatus.NOT_INPROGRESS;
		Main.requests.set(request.requestNumber-1, request);
		dm.getData().set("Data.requests", Main.requests);
		dm.saveData();
		
		p.openInventory(inv);
	}
}
