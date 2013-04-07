package com.ubc417.project.megastore;

import java.io.IOException;
import java.text.SimpleDateFormat;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.apphosting.utils.remoteapi.RemoteApiPb.Request;

public class AuctionServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String id= req.getParameter("id");
		//Key key = KeyFactory.createKey("Auction", id);
		Key key = KeyFactory.stringToKey(id);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ServletContext sc = getServletContext();
		
		try {
			Entity auction = ds.get(key);
			req.setAttribute("auction", auction);
			String startTime = new SimpleDateFormat("MM/dd/yyyy").format(auction.getProperty("startTime"));
			String endTime = new SimpleDateFormat("MM/dd/yyyy").format(auction.getProperty("endTime"));
			String highestBid = (String) auction.getProperty("highestBid");
			String price;
			if (highestBid.equals("null")) {
				price = auction.getProperty("startPrice").toString();
				
			} else {
				Key key2 = KeyFactory.createKey("Bid", highestBid);
				Entity bid = ds.get(key2);
				price = (String) bid.getProperty("price");
			}
			req.setAttribute("highestBid", price);
			req.setAttribute("startTime", startTime);
			req.setAttribute("endTime", endTime);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		
		RequestDispatcher rd = sc.getRequestDispatcher("/showAuction.jsp");
		rd.forward(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		// Handle creating bid here
	}

}
