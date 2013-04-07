package com.ubc417.project.megastore.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

public class Comments {
	//creates a new comment entity; inputs are the commentER's user key, the target user key and the comment (string)
	public static Entity createComment(
			Key userTarget,
			Key userCommenter, 
			String value) {
		Entity commentEntity = new Entity("Comment", userTarget);
		commentEntity.setProperty("commenter", userCommenter);
		commentEntity.setProperty("value", value);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(commentEntity);
		
		return commentEntity;
	}
	
	//returns an iterable of comment entities for a given user key
	public static Iterable<Entity> getComments(Entity user){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Comment").setAncestor(user.getKey());
		
		return ds.prepare(q).asIterable();
	}
}
