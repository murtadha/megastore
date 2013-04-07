package com.ubc417.project.megastore;

import java.io.IOException;
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
import com.ubc417.project.megastore.data.Users;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		System.err.println("DEBUG::in doPost...");
		String enteredUsername = req.getParameter("enteredUsername");
		String enteredPassword = req.getParameter("enteredPassword");
		
		//do login stuff
		System.err.println("DEBUG::doing login stuff...");
		Boolean loginSuccessful = false;
		Entity currentUser = null;
		for(Entity user : Users.GetAllUsers()){
			//check if username and password match
			if(user.getProperty("username").equals(enteredUsername)
					&& user.getProperty("password").equals(enteredPassword)){
				//if yes break and redirect
				currentUser = user;
				loginSuccessful = true;
				break;
			}
		}
		
		if(loginSuccessful){
			//redirect to home screen
			HttpSession session = req.getSession();
			session.setAttribute("user", currentUser);
			resp.sendRedirect("/");
		} else {
			//display error page
			System.err.println("DEBUG::displaying error page...");
			resp.sendRedirect("/loginError.jsp");
		}
	}
}
