package com.ubc417.project.megastore.data;

import java.util.ConcurrentModificationException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

public class Bids {
	
	private static final int NUM_TRIES = 7;
	private static final long TRY_DELAY = 500;
	
	public static Boolean createBid(Key auctionKey, 
			Key biddingUserKey, 
			int price) throws EntityNotFoundException{
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		int i = 0;
		while(true){
			Transaction txn = ds.beginTransaction();
			try{
//				Boolean success = false;
				
				if(!checkPriceSanity(auctionKey, price)){//if priceCheck fails...
					return false;//fail out
				}

				Entity bidEntity = new Entity("Bid", auctionKey);
				bidEntity.setProperty("bidder", biddingUserKey);
				bidEntity.setProperty("price", price);
				ds.put(txn, bidEntity);
				
				Entity auctionToUpdate = ds.get(auctionKey);
				auctionToUpdate.setProperty("highestBid", bidEntity.getKey());
				ds.put(txn, auctionToUpdate);
			} finally {
				try{
					txn.commit();
				} catch(final ConcurrentModificationException ex1) {
					try { Thread.sleep(TRY_DELAY); }
					catch(final InterruptedException ex2) {}
					if (i++ < NUM_TRIES)
						continue;
					else
						throw ex1;
				}
			}
			if (i>0) System.out.println(i + " tries");
			return true;
		}
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
	
	public static Iterable<Entity> getBidsForAuction(Key auctionKey){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Bid").setAncestor(auctionKey);
		
		return ds.prepare(q).asIterable();
	}
	
}
