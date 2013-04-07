package com.ubc417.project.megastore.data;

import java.util.ArrayList;
import com.google.appengine.api.datastore.*;

public class Auctions {
	public static Entity createAuction(String name, 
			String owner, 
			String startTime, 
			String endTime,
			String description,
			int startPrice){
		Entity itemEntity = new Entity("Item");
		itemEntity.setProperty("name", name);
		itemEntity.setProperty("owner", owner);
		itemEntity.setProperty("startTime", startTime);
		itemEntity.setProperty("endTime", endTime);
		itemEntity.setProperty("description", description);
		itemEntity.setProperty("startPrice", startPrice);
		
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		datastoreService.put(itemEntity);

		return itemEntity;
		
	}
	
	public static ArrayList<Entity> searchAuctions(String searchString){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Auction");
		
		Iterable<Entity> allItems = datastoreService.prepare(query).asIterable();
		ArrayList<Entity> returnedItems = null;
		
		for(Entity currentItem : allItems){
			if(currentItem.getProperty("name").toString().contains(searchString)){
				extracted(returnedItems).add(currentItem);
			
			}
			
		}
		
		return extracted(returnedItems);
		
	}
	
	public static ArrayList<Entity> getCurrentUserAuctions(String owner){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Auction");
		
		Iterable<Entity> allItems = ds.prepare(query).asIterable();
		ArrayList<Entity> returnedItems = null;
		
		for(Entity currentItem : allItems){
			if(currentItem.getProperty("owner").toString().contains(owner)){
				extracted(returnedItems).add(currentItem);
			
			}
			
		}
		
		return extracted(returnedItems);
	}

	private static ArrayList<Entity> extracted(ArrayList<Entity> returnedItems) {
		return returnedItems;
		
	}
	
	//TODO
	public static Iterable<Entity> getItemRecommendations(String username){
		return null;
		
	}
}
