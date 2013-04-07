/*
 * Users data class, for interacting with our datastore of users
 */

package com.ubc417.project.megastore.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

public class Users {
	public static Entity CreateUser(String username, String password, boolean store){
		//grab numUsers
		
		Entity userEntity = new Entity("User");
		userEntity.setProperty("username", username);
		userEntity.setProperty("password", password);
		
		if(store){
			DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
			datastoreService.put(userEntity);
			
		}
		return userEntity;
		
	}
	
	public static Boolean DeleteUser(Key userKey){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		if(userKey != null){
			datastoreService.delete(userKey);
			return true;
			
		} else {
			return false;
			
		}

	}
	
	public static Iterable<Entity> GetAllUsers(){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("User");
		
		return datastoreService.prepare(query).asIterable();
		
	}
	
}
