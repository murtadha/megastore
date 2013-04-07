<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org   /TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Viewing Auction</title>
	</head>
	<body>
		<% Entity auction = (Entity)request.getAttribute("auction"); %>
		
		<% if (auction == null) { %>
			<h>Couldn't find auction with id=<%= request.getParameter("id") %></h>
		<% } else { %>
			<h2><%= auction.getProperty("name") %></h2>
			<h>Description:</h>
			<%= auction.getProperty("description") %>
			<ul>
			<li>Highest Bid: <%= request.getAttribute("highestBid") %></li>
			<li>Start Time: <%= request.getAttribute("startTime") %></li>
			<li>End Time: <%= request.getAttribute("endTime") %></li>
			</ul>
			<% if (auction.getParent().equals(((Entity)session.getAttribute("user")).getKey())) {%>
				<form action="/auction" method="DELETE">
				<input type="submit" value="Delete"/><br>
				<input type="hidden" name="auctionKey" value="<%= KeyFactory.keyToString(auction.getKey()) %>" />
				</form>
			<% } else { %>
				<form action="/auction" method="POST">
				Bid:<input type="text" name="price"/><br>
				<input type="hidden" name="auctionKey" value="<%= KeyFactory.keyToString(auction.getKey()) %>" />
				</form>
			<% } %>
		<% } %>
 	</body>
</html>