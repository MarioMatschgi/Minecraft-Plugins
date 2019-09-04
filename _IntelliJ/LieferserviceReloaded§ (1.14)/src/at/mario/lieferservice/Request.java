package at.mario.lieferservice;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

@SerializableAs("Request")
public class Request implements ConfigurationSerializable {
	public int requestNumber;
	
	public OfflinePlayer requester;
	public ItemStack requestedItem;
	
	public String status;
	
	public double xToDeliver;
	public double yToDeliver;
	public double zToDeliver;
	
	public Request(int requestNumber, OfflinePlayer requester, ItemStack requestedItem, String status, double xToDeliver, double yToDeliver, double zToDeliver) {
		this.requestNumber = requestNumber;
		
		this.requester = requester;
		this.requestedItem = requestedItem;
		
		this.status = status;
		
		this.xToDeliver = xToDeliver;
		this.yToDeliver = yToDeliver;
		this.zToDeliver = zToDeliver;
	}
	
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("requestNumber", this.requestNumber);
		
		map.put("requester", this.requester);
		map.put("requestedItem", this.requestedItem);
		
		map.put("status", this.status);
		
		map.put("xToDeliver", this.xToDeliver);
		map.put("yToDeliver", this.yToDeliver);
		map.put("zToDeliver", this.zToDeliver);
		
		return map;
	}
	
	public static Request deserialize(Map<String, Object> map) {
		Request request = new Request((int) map.get("requestNumber"), (OfflinePlayer) map.get("requester"), (ItemStack) map.get("requestedItem"), (String) map.get("status"), (double) map.get("xToDeliver"), (double) map.get("yToDeliver"), 
				(double) map.get("zToDeliver"));
		
		return request;
	}
	
	public static void updateIds() {
		for (int i = 0; i < Main.requests.size(); i++) {
			Request request = Main.requests.get(i);
			
			request.requestNumber = i + 1;
		}
	}
}
