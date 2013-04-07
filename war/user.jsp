<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org   /TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>User's Profile</title>
	</head>
	<body>
		<% Entity user = (Entity)request.getAttribute("user"); %>
		<% boolean viewingSelf = user.getKey().equals(((Entity)session.getAttribute("user")).getKey()); %>
		<h2>Viewing <u><%= user.getProperty("username") %>'s</u> Profile</h2>
		Rating: <%= request.getAttribute("rating") %>
		<% if (viewingSelf == false) { %>
			<form action="/user" method="post">
				<select name="value">
				<% for (int i=1; i<6; i++) { %>
					<option value="<%=i%>"><%=i%></option>
				<% } %>
				</select>
				<input type="submit" name="action" value="Rate" />
				<input type="hidden" name="userKey" value="<%= KeyFactory.keyToString(user.getKey()) %>" />
			</form>
		<% } %>
		<h4><u>Comments</u></h4>
		<% if (viewingSelf == false) { %>
			<form action="/user" method="post">
				<input type="text" name="value" />
				<input type="submit" name="action" value="Comment" />
				<input type="hidden" name="userKey" value="<%= KeyFactory.keyToString(user.getKey()) %>" />
			</form>
		<% } %>
		<ul>
		<% Iterable<Entity> comments = (Iterable<Entity>)request.getAttribute("comments"); %>
		<% if (comments == null) { %>
		Can't find any comments about user
		<% } else for (Entity comment : comments) { %>
		<li><%= comment.getProperty("value") %></li>
		<% } %>
		</ul>		
 	</body>
</html>