package com.ubc417.project.megastore.data;

import java.util.ArrayList;
import com.google.appengine.api.datastore.*;

public class Items {
	public static Entity CreateItem(String itemName, 
			String itemDescription, 
			String startingBid, 
			String seller){
		Entity itemEntity = new Entity("Item");
		itemEntity.setProperty("itemName", itemName);
		itemEntity.setProperty("itemDescription", itemDescription);
		itemEntity.setProperty("startingBid", startingBid);
		itemEntity.setProperty("seller", seller);	
		
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		datastoreService.put(itemEntity);

		return itemEntity;
		
	}
	
	public static ArrayList<Entity> searchItems(String searchString){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Item");
		
		Iterable<Entity> allItems = datastoreService.prepare(query).asIterable();
		ArrayList<Entity> returnedItems = null;
		
		for(Entity currentItem : allItems){
			if(currentItem.getProperty("itemName").toString().contains(searchString)){
				extracted(returnedItems).add(currentItem);
			
			}
			
		}
		
		return extracted(returnedItems);
		
	}
	
	//helper function
	//DESCRIPTION:TODO
	private static ArrayList<Entity> extracted(ArrayList<Entity> returnedItems) {
		return returnedItems;
		
	}
	
	public static Iterable<Entity> getItemRecommendations(String username){
		return null;
		
	}
}
