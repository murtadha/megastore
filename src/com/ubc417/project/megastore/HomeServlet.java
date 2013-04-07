package com.ubc417.project.megastore;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.ubc417.project.megastore.data.Auctions;
import com.ubc417.project.megastore.data.Users;

@SuppressWarnings("serial")
public class HomeServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		HttpSession session = req.getSession();
		if (session.getAttribute("user") == null) {
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher("/welcome.jsp");
			rd.forward(req, resp);
		} else {
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher("/createAuction.jsp");
			rd.forward(req, resp);
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		System.err.println("DEBUG::in SearchServlet doPost");
		
		if(req.getParameter("buttonEvent").equals("createAuction")) {
			HttpSession session = req.getSession();
			Entity user = (Entity)session.getAttribute("user");
			
			if (user != null) {
				String enteredItemName = req.getParameter("enteredItemName");
				String enteredItemDescription = req.getParameter("enteredItemDescription");
				int startingPrice = Integer.parseInt(req.getParameter("enteredStartingBid"));
				int period = Integer.parseInt(req.getParameter("enteredPeriod"));
				
				long startTime = System.currentTimeMillis();
				
				Entity createdItem = Auctions.createAuction(
						user.getKey(),
						enteredItemName,
						enteredItemDescription,
						startTime,
						startTime + period*3600,
						startingPrice);
				if(createdItem != null){
					resp.sendRedirect("/createAuctionSuccess");
				} else {
					resp.sendRedirect("/createAuctionError");
				}
			} else {
				resp.sendRedirect("/");
			}
			
		} else if(req.getParameter("searchButton").equals("search")){
			//TODO:handle search
			
		}
		
	}

}
