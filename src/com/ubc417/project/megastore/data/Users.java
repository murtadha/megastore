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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

public class Users {
	
	public static final int NUM_SHARDS = 5;
	public static final int NUM_SEARCH_STRINGS = 3;
	
	public static Entity CreateUser(String username, String password){
		
		ArrayList<String> arrayListSearchedStrings = new ArrayList<String>();
		arrayListSearchedStrings.ensureCapacity(NUM_SEARCH_STRINGS);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Entity userEntity = new Entity("User", getShardedUsername(username, 0));
		userEntity.setProperty("password", password);
		userEntity.setProperty("username", username);
		userEntity.setProperty("shardNum", 0);
		userEntity.setProperty("arrayListSearchedStrings", arrayListSearchedStrings);
		
		ds.put(userEntity);
		
		for (int i = 1; i < NUM_SHARDS; i++) {
			Entity userShard = new Entity("User", getShardedUsername(username, i));
			userShard.setProperty("shardNum", i);
			userShard.setProperty("username", username);
			ds.put(userShard);
		}
			
		return userEntity;
	}
	
	public static String getShardedUsername(String username, int shard) {
		return shard == 0 ? username : username + "_" + shard;
	}
	
	public static void DeleteUser(Key userKey){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//query grab all Bids where ancestor is auctionKey
		Query q = new Query("Auction").setAncestor(userKey);
		Iterable<Entity> iterableAuctionsToDelete = ds.prepare(q).asIterable();
		
		//delete all our bids
		for(Entity auctionToDelete : iterableAuctionsToDelete){
			ds.delete(auctionToDelete.getKey());
		}
		
		//delete our auction
		ds.delete(userKey);
	}
	
	public static Iterable<Entity> GetAllUsers(){
		DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		Query query = new Query("User");
		
		return datastoreService.prepare(query).asIterable();
	}
	
	public static Entity getUserForKey(Key userKey){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity userEntity = null;
		try {
			userEntity = ds.get(userKey);
		} catch (EntityNotFoundException e) {
			System.err.println("ERROR::User not found in getUserForKey");
			e.printStackTrace();
		}
		return userEntity;
	}
	
	@SuppressWarnings("unchecked")
	public static void addSearchString(Entity user, String searchStringToAdd) throws EntityNotFoundException{
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//TODO:wrap in txn
		ArrayList<String> arrayListSearchedStrings = ((ArrayList<String>) user.getProperty("arrayListSearchedStrings"));
		
		if(arrayListSearchedStrings == null){
			arrayListSearchedStrings = new ArrayList<String>();
			arrayListSearchedStrings.add(searchStringToAdd);
		} else {
			if(!arrayListSearchedStrings.contains(searchStringToAdd)){
				if (arrayListSearchedStrings.size() >= NUM_SEARCH_STRINGS)
					arrayListSearchedStrings.remove(0);
				arrayListSearchedStrings.add(searchStringToAdd);
			}
		}
		
		user.setProperty("arrayListSearchedStrings", arrayListSearchedStrings);
		ds.put(user);
	}

	@SuppressWarnings("deprecation")
	public static Key getShardedOwnerForAuction(Key owner) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		int totalAuctions = 0;
		for (int i = 0; i < NUM_SHARDS; i++) {
			Key shardedUser = KeyFactory.createKey("User", getShardedUsername(owner.getName(), i));
			
			Query query = new Query("Auction");
			query.setAncestor(shardedUser);
			
			totalAuctions += ds.prepare(query).countEntities();
		}
		return KeyFactory.createKey("User", getShardedUsername(owner.getName(), totalAuctions % NUM_SHARDS));
	}
	
	// Given a key for a user shard (with shardNum potentially > 0), we return the Entity
	// that has shardNum = 0
	public static Entity getMainEntityFromShard(Key userShard) throws EntityNotFoundException {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity shard = ds.get(userShard);
		return ds.get(KeyFactory.createKey("User", (String)shard.getProperty("username")));
	}
}