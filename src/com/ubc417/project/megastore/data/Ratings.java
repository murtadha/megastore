package com.ubc417.project.megastore.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

public class Ratings {
	public static Entity createRating(Key target,
			Key rater,
			int value){
		Entity ratingEntity = new Entity("Rating", target);
		ratingEntity.setProperty("rater", rater);
		ratingEntity.setProperty("value", value);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(ratingEntity);
		
		return ratingEntity;
	}
	
	//grabs all ratings for given userKey. Takes average and returns an Int.
	public static Float getRatingsForUser(Entity user){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Rating").setAncestor(user.getKey());
		
		Iterable<Entity> userRatings = ds.prepare(q).asIterable();
		
		int numRatings = 0;
		int sumRatings = 0;
		for(Entity rating : userRatings){
			sumRatings += (Long)rating.getProperty("value");
			numRatings++;
		}
		
		return numRatings > 0 ? ((float)sumRatings/numRatings) : 0;
	}
	
}
