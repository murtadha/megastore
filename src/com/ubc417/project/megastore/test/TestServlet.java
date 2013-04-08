package com.ubc417.project.megastore.test;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.ubc417.project.megastore.data.Auctions;
import com.ubc417.project.megastore.data.Bids;
import com.ubc417.project.megastore.data.Users;


public class TestServlet extends HttpServlet {
	public static final int NUM_AUCTIONS = 200;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String username = "sam";
		String password = "sam";
		Entity user = Users.CreateUser(username, password);
		
		for (int i = 0; i < NUM_AUCTIONS; i++) {
			Auctions.createAuction(
					user.getKey(),
					"" + i,
					"" + i,
					System.currentTimeMillis(),
					System.currentTimeMillis()+1000*3600*24,
					1);
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String userKey = req.getParameter("userKey");
		Key key = KeyFactory.stringToKey(userKey);
		
		try {
			Entity auction = getRandomAuction(key);
			Key highestBid = (Key) auction.getProperty("highestBid");
			long price = 2;
			if (highestBid != null) {
				DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
				Entity bid = ds.get(highestBid);
				price = (Long) bid.getProperty("price")+1;
			}
			Bids.createBid(auction.getKey(), key, (int)price);
		} catch(EntityNotFoundException e) {
			e.printStackTrace(System.err);
			throw new ServletException("holy shit something fucked up happened");
		}
	}
	
	public static Entity getRandomAuction(Key user) throws EntityNotFoundException {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		int index = new Random().nextInt(NUM_AUCTIONS);
		Query query = new Query("Auction").setFilter(new FilterPredicate("name", FilterOperator.EQUAL, ""+index));
		return ds.prepare(query).asSingleEntity();
	}
}
