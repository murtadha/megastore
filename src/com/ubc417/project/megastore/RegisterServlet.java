package com.ubc417.project.megastore;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Entity;
import com.ubc417.project.megastore.data.Users;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		System.err.println("DEBUG::in doPost...");
		String enteredUsername = req.getParameter("enteredUsername");
		String enteredPassword = req.getParameter("enteredPassword");
		
		//do signup stuff
		System.err.println("DEBUG::doing signup stuff...");
		
		System.err.println("enteredUsername length is " + enteredUsername.length());
		System.err.println("enteredPassword is " + enteredPassword);
		
		if(enteredUsername.length() == 0 || enteredPassword.length() == 0) {
			resp.sendRedirect("/signupError.jsp");
		} else if(checkDuplicateUsername(enteredUsername) == true) {
			//check for duplicate username in our DB
			resp.sendRedirect("/duplicateUsername.jsp");
		} else {
			Users.CreateUser(enteredUsername, enteredPassword, true);
			//redirect to home screen and save username
			HttpSession session = req.getSession(); 
			session.setAttribute("username", enteredUsername);
			resp.sendRedirect("/");
		}
		
	}
	
	//helper method
	//INPUT: some username to check for duplication in our DB
	//OUTPUT: returns true if duplicate detected, false otherwise
	//DESCRIPTION: helper method to check for username duplication on signup (ie: check username is unique in datastore)
	private Boolean checkDuplicateUsername(String usernameToCheck) {
		Iterable<Entity> allUsers = Users.GetAllUsers();
		Iterator<Entity> allUsersIterator = allUsers.iterator();
		//TODO:need to make this more efficient; hash instead of looping
		while(allUsersIterator.hasNext()) {
			Entity user = allUsersIterator.next();
			if(user.getProperty("username").toString().equalsIgnoreCase(usernameToCheck)) {
				//duplicate detected
				return true;
			}
		}
		return false;
	}
}
