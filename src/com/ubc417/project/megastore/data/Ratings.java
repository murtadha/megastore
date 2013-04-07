package com.ubc417.project.megastore.data;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class Ratings {
	public static Entity createRating(String target,
			String ragter,
			String value){
		Entity ratingEntity = new Entity("Rating");
		ratingEntity.setProperty("userBeingRated", target);
		ratingEntity.setProperty("userDoingRating", ragter);
		ratingEntity.setProperty("rating", value);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(ratingEntity);
		
		return ratingEntity;
		
	}
	
	public static Iterable<Entity> getRatingsForUser(String username){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Rating");
		Iterable<Entity> allRatings = ds.prepare(q).asIterable();
		ArrayList<Entity> ratingsForUser = null;
		
		for(Entity ratingEntity : allRatings){
			if(ratingEntity.getProperty("username").toString().contains(username)){
				extracted(ratingsForUser).add(ratingEntity);
				
			}
			
		}
		
		return allRatings;
		
	}

	private static ArrayList<Entity> extracted(ArrayList<Entity> ratingsForUser) {
		return ratingsForUser;
	}
	
}
