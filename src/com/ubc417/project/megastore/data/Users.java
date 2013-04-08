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
	
	public static boolean DeleteUser(Key userKey){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Entity userEntity;
		try {
			userEntity = ds.get(userKey);
		} catch (EntityNotFoundException e2) {
			e2.printStackTrace();
			return false;
		}
		
		//delete all bids the user had made
		Iterable<Entity> iterableBidsToDelete;
		iterableBidsToDelete = Bids.getBidsForUser(userEntity);
		if(iterableBidsToDelete != null){
			for(Entity bidToDelete : iterableBidsToDelete){
				Bids.deleteBid(bidToDelete.getKey());
			}
		}
		
		//delete all auctions the user had made
		Iterable<Entity> iterableAuctionsToDelete = Auctions.getUserAuctions(userEntity);
		if(iterableAuctionsToDelete != null){
			for(Entity auctionToDelete : iterableAuctionsToDelete){
				Auctions.deleteAuction(auctionToDelete.getKey());
			}
		}
		
		//delete user and all shards of said user
		if(userKey != null){
			String username = userKey.getName();
			for(int i = 0; i < NUM_SHARDS; i++){
				Key key = KeyFactory.createKey("User", getShardedUsername(username, i));
				ds.delete(key);
			}
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