package com.ubc417.project.megastore.data;

import java.util.ArrayList;
import com.google.appengine.api.datastore.*;

public class Auctions {
	public static Entity createAuction(
			Key owner,
			String name,
			String description,
			long startTime, 
			long endTime,
			int startingPrice) {
		Entity itemEntity = new Entity("Auction", owner);
		itemEntity.setProperty("name", name);
		itemEntity.setProperty("startTime", startTime);
		itemEntity.setProperty("endTime", endTime);
		itemEntity.setProperty("description", description);
		itemEntity.setProperty("startPrice", startingPrice);
		itemEntity.setProperty("highestBid", "null");
		
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		datastoreService.put(itemEntity);

		return itemEntity;
	}
	
	public static ArrayList<Entity> searchAuctions(String searchString) {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Auction");
		
		Iterable<Entity> allItems = datastoreService.prepare(query).asIterable();
		ArrayList<Entity> returnedItems = new ArrayList<Entity>();
		
		// TODO: improve search algo
		for(Entity currentItem : allItems) {
			if(currentItem.getProperty("name").toString().contains(searchString)) {
				returnedItems.add(currentItem);
			}
		}
		
		return returnedItems;
	}
	
	public static ArrayList<Entity> getCurrentUserAuctions(String owner) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Auction");
		
		Iterable<Entity> allItems = ds.prepare(query).asIterable();
		ArrayList<Entity> returnedItems = new ArrayList<Entity>();
		
		for(Entity currentItem : allItems) {
			if(currentItem.getProperty("owner").toString().contains(owner)){
				returnedItems.add(currentItem);
			}
		}
		
		return returnedItems;
	}
	
	//TODO
	public static Iterable<Entity> getItemRecommendations(String username){
		return null;
		
	}
}
