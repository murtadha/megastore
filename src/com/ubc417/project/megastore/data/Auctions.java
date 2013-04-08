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
		Key shardedOwner = Users.getShardedOwnerForAuction(owner);
		Entity itemEntity = new Entity("Auction", shardedOwner);
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
			long endTime = (Long) currentAuction.getProperty("endTime");
			if(endTime > System.currentTimeMillis() && 
			   Pattern.compile(Pattern.quote(searchString), Pattern.CASE_INSENSITIVE).matcher(currentAuctionNameString).find()) {
				returnedAuctions.add(currentAuction);
			}
		}
		
		return returnedAuctions;
	}
	
	public static Iterable<Entity> getUserAuctions(Entity user) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ArrayList<Entity> results = new ArrayList<Entity>();
		
		for (int i = 0; i < Users.NUM_SHARDS; i++) {
			Key shardedUser = KeyFactory.createKey("User", Users.getShardedUsername(user.getKey().getName(), i));
			
			Query query = new Query("Auction");
			query.setAncestor(shardedUser);
			
			for (Entity e : ds.prepare(query).asIterable()) {
				results.add(e);
			}
		}
		
		return results;
	}
	
	public static void deleteAuction(Key auctionKey) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//query grab all Bids where ancestor is auctionKey
		Query q = new Query("Bid").setAncestor(auctionKey);
		Iterable<Entity> iterableBidsToDelete = ds.prepare(q).asIterable();
		
		//delete all our bids
		for(Entity bidToDelete : iterableBidsToDelete){
			ds.delete(bidToDelete.getKey());
		}
		
		//delete our auction
		ds.delete(auctionKey);
	}
	
	public static ArrayList<Entity> getRecommendedAuctions(Entity user) {
		ArrayList<String> searchStrings = (ArrayList<String>)user.getProperty("arrayListSearchedStrings");
		ArrayList<Entity> results = new ArrayList<Entity>();
		
		if(searchStrings != null){
			for (String s : searchStrings) {
				for (Entity e : Auctions.searchAuctions(s)) {
					if (!results.contains(e)) {
						results.add(e);
					}
				}
			}
		}
		
		return results;
	}
}
