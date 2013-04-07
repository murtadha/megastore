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
			String startTime = new SimpleDateFormat("MM/dd/yyyy").format(auction.getProperty("startTime"));
			String endTime = new SimpleDateFormat("MM/dd/yyyy").format(auction.getProperty("endTime"));
			Key highestBid = (Key) auction.getProperty("highestBid");
			String price;
			System.out.printf("auctionKey = %s, parentKey = %s, userKey = %s\n",
					KeyFactory.keyToString(auction.getKey()),
					KeyFactory.keyToString(auction.getParent()),
					KeyFactory.keyToString(((Entity)req.getSession().getAttribute("user")).getKey())
					);
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
		// Handle creating bid here
		Entity user = (Entity)req.getSession().getAttribute("user");
		if (user == null) {
			resp.sendRedirect("/");
			return;
		}
		int price = Integer.parseInt(req.getParameter("price"));
		Key auctionKey = KeyFactory.stringToKey((req.getParameter("auctionKey")));
		try {
			Bids.createBid(auctionKey, user.getKey(), price);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.sendRedirect("/auction?auctionKey=" + req.getParameter("auctionKey"));
	}

}
