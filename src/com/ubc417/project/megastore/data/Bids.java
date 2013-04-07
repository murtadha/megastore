package com.ubc417.project.megastore.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

public class Bids {
	public static Boolean createBid(Key auctionKey, 
			Key biddingUserKey, 
			int price) throws EntityNotFoundException{
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = ds.beginTransaction();
		
		Boolean success = false;
		
		if(!checkPriceSanity(auctionKey, price)){//if priceCheck fails...
			return false;//fail out
		}
		
		try{
			Entity bidEntity = new Entity("Bid", auctionKey);
			bidEntity.setProperty("bidder", biddingUserKey);
			bidEntity.setProperty("price", price);
			ds.put(txn, bidEntity);
			
			Entity auctionToUpdate = ds.get(auctionKey);
			auctionToUpdate.setProperty("highestBid", bidEntity.getKey());
			ds.put(txn, auctionToUpdate);
			txn.commit();
		} finally {
			if(txn.isActive()){
				txn.rollback();
			} else {	
				success = true;
			}
		}
		return success;
	}
	
	private static Boolean checkPriceSanity(Key auctionKey, int price) throws EntityNotFoundException {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity auctionToCheck =  ds.get(auctionKey);
		
		Key highestBid = (Key) auctionToCheck.getProperty("highestBid");
		long highestPrice;
		
		if (highestBid == null) {
			highestPrice = (Long) auctionToCheck.getProperty("startPrice");
		} else {
			Entity bid = ds.get(highestBid);
			highestPrice = (Long) bid.getProperty("price");
		}
		
		if(highestPrice >= price) {
			return false;
		} else {
			return true;
		}
	}

	public static Boolean deleteBid(Key bidKey) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		if(bidKey != null){
			ds.delete(bidKey);
			return true;	
		} else {
			return false;
		}	
	}
	
	//grabs all bids for some bidder's user key
	public static Iterable<Entity> getBidsForUser(Entity bidder){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Bid").setFilter(
				new FilterPredicate("bidder", Query.FilterOperator.EQUAL, bidder.getKey()));
		
		return ds.prepare(q).asIterable();
	}
	
}
