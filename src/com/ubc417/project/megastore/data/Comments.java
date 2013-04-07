package com.ubc417.project.megastore.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class Comments {
	public static Entity CreateComment(String comment, String user, String commenter){
		Entity commentEntity = new Entity("Comment");
		commentEntity.setProperty("comment", comment);
		commentEntity.setProperty("user", user);
		commentEntity.setProperty("commenter", commenter);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(commentEntity);
		
		return commentEntity;
		
	}
}
