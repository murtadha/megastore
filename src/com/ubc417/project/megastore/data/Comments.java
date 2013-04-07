package com.ubc417.project.megastore.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class Comments {
	public static Entity createComment(String commenter, String target, String value){
		Entity commentEntity = new Entity("Comment");
		commentEntity.setProperty("value", value);
		commentEntity.setProperty("target", target);
		commentEntity.setProperty("commenter", commenter);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(commentEntity);
		
		return commentEntity;
		
	}
	
	public static Iterable<Entity> getComments(Key userKey){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Comment").setFilter(new FilterPredicate("target", FilterOperator.EQUAL, userKey));
		
		
		Iterable<Entity> currentUserComments = ds.prepare(q).asIterable();
		return currentUserComments;
	}
}
