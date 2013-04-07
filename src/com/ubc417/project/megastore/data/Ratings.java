package com.ubc417.project.megastore.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class Ratings {
	public static Entity createRating(String target,
			String rater,
			String value){
		Entity ratingEntity = new Entity("Rating", target);
		ratingEntity.setProperty("rater", rater);
		ratingEntity.setProperty("value", value);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(ratingEntity);
		
		return ratingEntity;
	}
	
	//grabs all ratings for given userKey. Takes average and returns an Int.
	public static Integer getRatingsForUser(Entity user){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Rating").setAncestor(user.getKey());
		
		Iterable<Entity> userRatings = ds.prepare(q).asIterable();
		
		int numRatings = 0;
		int sumRatings = 0;
		for(Entity rating : userRatings){
			sumRatings += (Integer)rating.getProperty("value");
			numRatings++;
		}
		
		return (sumRatings/numRatings);
	}
	
}
