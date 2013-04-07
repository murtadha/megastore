package com.ubc417.project.megastore.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class Ratings {
	public static Entity CreateRating(String userBeingRated,
			String userDoingRating,
			String rating){
		Entity ratingEntity = new Entity("Rating");
		ratingEntity.setProperty("userBeingRated", userBeingRated);
		ratingEntity.setProperty("userDoingRating", userDoingRating);
		ratingEntity.setProperty("rating", rating);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(ratingEntity);
		
		return ratingEntity;
	}
}
