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
import com.ubc417.project.megastore.data.Auctions;
import com.ubc417.project.megastore.data.Bids;

@SuppressWarnings("serial")
public class AuctionServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String id = req.getParameter("auctionKey");
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ServletContext sc = getServletContext();
		
		if (req.getSession().getAttribute("user") == null) {
			resp.sendRedirect("/");
			return;
		}
		
		try {
			Key key = KeyFactory.stringToKey(id);
			Entity auction = ds.get(key);
			req.setAttribute("auction", auction);
			Entity owner = ds.get(auction.getParent());
			req.setAttribute("owner", owner);
			String startTime = new SimpleDateFormat("MM/dd/yyyy").format(auction.getProperty("startTime"));
			String endTime = new SimpleDateFormat("MM/dd/yyyy").format(auction.getProperty("endTime"));
			Key highestBid = (Key) auction.getProperty("highestBid");
			String price;
			if (highestBid == null) {
				price = auction.getProperty("startPrice").toString();
			} else {
				Entity bid = ds.get(highestBid);
				price = bid.getProperty("price").toString();
			}
			req.setAttribute("highestBid", price);
			req.setAttribute("startTime", startTime);
			req.setAttribute("endTime", endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		RequestDispatcher rd = sc.getRequestDispatcher("/showAuction.jsp");
		rd.forward(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Entity user = (Entity)req.getSession().getAttribute("user");
		if (user == null) {
			resp.sendRedirect("/");
			return;
		}
		Key auctionKey = KeyFactory.stringToKey((req.getParameter("auctionKey")));
		
		if (req.getParameter("action").toLowerCase().equals("bid")) {
			// Handle creating bid here
			int price = Integer.parseInt(req.getParameter("price"));
			try {
				Bids.createBid(auctionKey, user.getKey(), price);
				// TODO show success message
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
				// TODO show error message
			}
			resp.sendRedirect("/auction?auctionKey=" + req.getParameter("auctionKey"));
		} else if (req.getParameter("action").toLowerCase().equals("delete")) {
			// Delete the auction only if the logged in user is owner
			if (user.getKey().equals(auctionKey.getParent())) {
				// TODO tell user that auction deleted successfully
				Auctions.deleteAuction(auctionKey);
				resp.sendRedirect("/");
			} else {
				// TODO create jsp file for failing to delete auction beceause you don't own it
				resp.sendRedirect("/createAuctionError.jsp");
			}
		} else {
			// shouldn't be here
			System.err.println("User trying to mess with the system. Log this attempt! " + req);
			resp.sendRedirect("/");
		}
	}

}
