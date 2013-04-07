package com.ubc417.project.megastore;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;
import com.ubc417.project.megastore.data.Comments;
import com.ubc417.project.megastore.data.Ratings;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		HttpSession session = req.getSession();
		Entity user = (Entity) session.getAttribute("user");
		if (user == null) {
			// Can't view other users if not logged in
			resp.sendRedirect("/");
		} else {
			String userKey = req.getParameter("userKey");
			Key key = KeyFactory.stringToKey(userKey);
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			try {
				user = ds.get(key);
				req.setAttribute("user", user);
				float rating = Ratings.getRatingsForUser(user);
				Iterable<Entity> comments = Comments.getComments(user);
				req.setAttribute("comments", comments);
				req.setAttribute("rating", rating);
				
				ServletContext sc = getServletContext();
				RequestDispatcher rd = sc.getRequestDispatcher("/user.jsp");
				rd.forward(req, resp);
			} catch (EntityNotFoundException e) {
				// TODO show error that requested user doesn't exist
				e.printStackTrace();
				resp.sendRedirect("/");
			}
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		HttpSession session = req.getSession();
		Entity user = (Entity) session.getAttribute("user");
		String userKey = req.getParameter("userKey");
		if (user == null) {
			// Can't rate or comment if not logged in
			resp.sendRedirect("/");
		} else {
			Key target = KeyFactory.stringToKey(userKey);
			if (req.getParameter("action").toLowerCase().equals("rate")) {
				int value = Integer.parseInt(req.getParameter("value"));
				Ratings.createRating(target, user.getKey(), value);
			} else if (req.getParameter("action").toLowerCase().equals("comment")) {
				String value = req.getParameter("value");
				Comments.createComment(target, user.getKey(), value);
			} else {
				// shouldn't be here
				System.err.println("user trying to mess with us " + req);
				resp.sendRedirect("/");
				return;
			}
			resp.sendRedirect("/user?userKey=" + userKey);
		}
	}
}
