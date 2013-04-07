package com.ubc417.project.megastore.data;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

public class Bids {
	public static Entity createBid(String item, 
			String bidder, 
			int price,
			Key auctionKey){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();	
		
		Entity bidEntity = new Entity("Bid", auctionKey);
		bidEntity.setProperty("item", item);
		bidEntity.setProperty("bidder", bidder);
		bidEntity.setProperty("price", price);
		
		ds.put(bidEntity);
		
		return bidEntity;
		
	}
	
	public static Boolean deleteBid(Key bidKey){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		if(bidKey != null){
			ds.delete(bidKey);
			return true;
			
		} else {
			return false;
			
		}
		
	}
	
	public static Iterable<Entity> getBidsForUser(String username){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Bids");
		
		Iterable<Entity> allBids = ds.prepare(q).asIterable();
		ArrayList<Entity> returnedBids  = null;
		
		for(Entity currentBid : allBids){
			if(currentBid.getProperty("name").toString().contains(username)){
				extracted(returnedBids).add(currentBid);
			
			}
			
		}
		
		return extracted(returnedBids);
	}
	
	private static ArrayList<Entity> extracted(ArrayList<Entity> returnedItems) {
		return returnedItems;
		
	}
	
}
