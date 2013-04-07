package com.ubc417.project.megastore;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.ubc417.project.megastore.data.Auctions;
import com.ubc417.project.megastore.data.Users;

@SuppressWarnings("serial")
public class SearchServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String searchString = req.getParameter("searchString");
		//grab our session's user entity
		HttpSession session = req.getSession();
		Entity user = (Entity) session.getAttribute("user");
		if (user == null) {
			resp.sendRedirect("/");
			return;
		}
		ArrayList<Entity> list = null;
		String message = "";
		
		if (searchString != null && !searchString.equals("")) {
			//add our search string to our queue
			try {
				Users.addSearchString(user, searchString);
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			list = Auctions.searchAuctions(searchString);
			if (list.size() == 0)
				message = "Couldn't find items with name: " + searchString;
		} else {
			message = "Please provide a string to search for items";
		}
		
		req.setAttribute("auctions", list);
		req.setAttribute("message", message);
				
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher("/search.jsp");
		rd.forward(req, resp);
	}

}
