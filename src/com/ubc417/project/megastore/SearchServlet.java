package com.ubc417.project.megastore;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.ubc417.project.megastore.data.Auctions;

public class SearchServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String searchString = req.getParameter("searchString");
		ArrayList<Entity> list;
		String[] items;
		String error;
		
		if (searchString != null && !searchString.equals("")) {
			list = Auctions.searchAuctions(searchString);
			error = "Couldn't find items with name: " + searchString;
		} else {
			list = null;
			error = "Please provide a string to search for items";
		}
		
		if (list != null && list.size() > 0) {
			items = new String[list.size()];
			
			for (int i = 0; i < items.length; i++) {
				items[i] = (String)list.get(i).getProperty("name");
			}
		} else {
			items = new String[1];
			items[0] = error;
		}
		
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher("/search.jsp");
		req.setAttribute("items", items);
		rd.forward(req, resp);
	}

}
