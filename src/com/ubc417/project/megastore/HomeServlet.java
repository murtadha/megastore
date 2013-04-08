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
import com.ubc417.project.megastore.data.Auctions;

@SuppressWarnings("serial")
public class HomeServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		HttpSession session = req.getSession();
		Entity user = (Entity) session.getAttribute("user");
		if (user == null) {
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher("/welcome.jsp");
			rd.forward(req, resp);
		} else {
			ArrayList<Entity> recommendations = Auctions.getRecommendedAuctions(user);
			req.setAttribute("recommendations", recommendations);
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher("/createAuction.jsp");
			rd.forward(req, resp);
		}
	}	
}
