package com.ubc417.project.megastore.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class Bids {
	public static Entity CreateBid(String itemBidOn, 
			String bidderUsername, 
			int bidPrice){
		Entity bidEntity = new Entity("Bid");
		bidEntity.setProperty("itemBidOn", itemBidOn);
		bidEntity.setProperty("bidder", bidderUsername);
		bidEntity.setProperty("bidPrice", bidPrice);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(bidEntity);
		
		return bidEntity;
		
	}
}
