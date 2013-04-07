package com.ubc417.project.megastore;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

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
import com.google.appengine.api.datastore.Key;
import com.ubc417.project.megastore.data.Auctions;
import com.ubc417.project.megastore.data.Bids;

public class ActivityServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		HttpSession session = req.getSession();
		Entity user = (Entity)session.getAttribute("user");
		if (user != null) {
			Iterable<Entity> auctions = Auctions.getUserAuctions(user);
			req.setAttribute("auctions", auctions);
			Vector<Key> keys = new Vector<Key>();
			Iterable<Entity> bids = Bids.getBidsForUser(user);
			req.setAttribute("bids", bids);
			
			for (Entity bid : bids) {
				keys.add(bid.getParent());
			}
			
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Map<Key, Entity> map = ds.get(keys);
			
			req.setAttribute("bidsAuctions", map);
			
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher("/activity.jsp");
			rd.forward(req, resp);
		} else {
			resp.sendRedirect("/");
		}
	}
}