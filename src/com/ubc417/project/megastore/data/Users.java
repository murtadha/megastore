/*
 * Users data class, for interacting with our datastore of users
 */

package com.ubc417.project.megastore.data;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

public class Users {
	public static Entity CreateUser(String username, String password, boolean store){
		Entity userEntity = new Entity("User");
		ArrayList<String> arrayListSearchedStrings = new ArrayList<String>();
		for(int i = 0; i<3; i++){
			arrayListSearchedStrings.add("dummy");
		}
		
		userEntity.setProperty("username", username);
		userEntity.setProperty("password", password);
		userEntity.setProperty("arrayListSearchedStrings", arrayListSearchedStrings);
		
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
	
	@SuppressWarnings("unchecked")
	public static void addSearchString(Entity user, String searchStringToAdd) throws EntityNotFoundException{
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//TODO:wrap in txn
		ArrayList<String> arrayListSearchedStrings = ((ArrayList<String>) user.getProperty("arrayListSearchedStrings"));
			
		arrayListSearchedStrings.remove(0);
		arrayListSearchedStrings.add(searchStringToAdd);
		user.setProperty("arrayListSearchedStrings", arrayListSearchedStrings);
		
		ds.put(user);
	}
}
