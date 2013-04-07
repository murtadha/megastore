package com.ubc417.project.megastore.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

public class Bids {
	public static Boolean createBid(Key auctionItem, 
			Key bidder, 
			int price,
			Key auctionKey) throws EntityNotFoundException{
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = ds.beginTransaction();
		
		Boolean success = false;
		
		try{
			Entity auctionToUpdate = ds.get(auctionItem);
			auctionToUpdate.setProperty("highestBid", price);
			ds.put(auctionToUpdate);
			txn.commit();
		} finally {
			if(txn.isActive()){
				txn.rollback();
			} else {
				Entity bidEntity = new Entity("Bid", auctionItem);
				bidEntity.setProperty("bidder", bidder);
				bidEntity.setProperty("price", price);
				
				ds.put(bidEntity);
				
				success = true;
			}
		}
		return success;
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
	
	public static Iterable<Entity> getBidsForUser(Entity user){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Bids").setAncestor(user.getKey());
		
		return ds.prepare(q).asIterable();
	}
	
}
