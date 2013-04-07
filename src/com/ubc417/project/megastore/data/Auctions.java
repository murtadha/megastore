package com.ubc417.project.megastore.data;

import java.util.ArrayList;
import java.util.regex.Pattern;

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
		itemEntity.setProperty("highestBid", null);
		
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		datastoreService.put(itemEntity);

		return itemEntity;
	}
	
	public static ArrayList<Entity> searchAuctions(String searchString) {
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Auction");
		
		Iterable<Entity> allAuctions = datastoreService.prepare(query).asIterable();
		ArrayList<Entity> returnedAuctions = new ArrayList<Entity>();
		
		// TODO: improve search algo
		for(Entity currentAuction : allAuctions) {
			String currentAuctionNameString = currentAuction.getProperty("name").toString();
			if(Pattern.compile(Pattern.quote(searchString), Pattern.CASE_INSENSITIVE).matcher(currentAuctionNameString).find()) {
				returnedAuctions.add(currentAuction);
			}
		}
		
		return returnedAuctions;
	}
	
	public static Iterable<Entity> getUserAuctions(Entity user) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("Auction");
		query.setAncestor(user.getKey());
		
		return ds.prepare(query).asIterable();
	}
	
	public static void deleteAuction(Key auctionKey){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.delete(auctionKey);
		// TODO need to delete bids associated with this auction
	}
}
