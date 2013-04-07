<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org   /TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Welcome to MegaStore</title>
	</head>
	<body>
	
		<form action="/search" method="GET" >
		<h2 style="display: inline-block;">Search</h2>
		<input type="text" name="searchString"/>
		<input type="submit" value="Search" />
		</form>
				
		<ul>
		<% ArrayList<Entity> auctions = (ArrayList<Entity>)request.getAttribute("auctions"); %>
		<div><%= request.getAttribute("message") %></div>
		<% if (auctions != null) for (Entity auction : auctions) { %>
		<li><a href="/auction?auctionKey=<%=KeyFactory.keyToString(auction.getKey())%>"><%= auction.getProperty("name") %></a></li>
		<% } %>
		</ul>		
 	</body>
</html>