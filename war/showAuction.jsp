<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.ubc417.project.megastore.data.Users;" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org   /TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Viewing Auction</title>
	</head>
	<body>
		<% Entity auction = (Entity)request.getAttribute("auction"); %>
		<% Entity owner = (Entity)request.getAttribute("owner"); %>
		
		<% if (auction == null) { %>
			<h>Couldn't find auction with id=<%= request.getParameter("id") %></h>
		<% } else { %>
			<h2><%= auction.getProperty("name") %></h2>
			<h>Description:</h>
			<%= auction.getProperty("description") %>
			<ul>
			<li>Owner: <a href="/user?userKey=<%=KeyFactory.keyToString(owner.getKey())%>"><%= owner.getProperty("username") %></a></li>
			<li>Highest Bid: <%= request.getAttribute("highestBid") %></li>
			<li>Start Time: <%= request.getAttribute("startTime") %></li>
			<li>End Time: <%= request.getAttribute("endTime") %></li>
			</ul>
			<form action="/auction" method="POST">
			<input type="hidden" name="auctionKey" value="<%= KeyFactory.keyToString(auction.getKey()) %>" />
			<% if (owner.getKey().equals(((Entity)session.getAttribute("user")).getKey())) {%>
				<input type="submit" value="Delete" name="action"/><br>
			<% } else { %>
				Bid:<input type="text" name="price"/><br>
				<input type="submit" value="Bid" name="action"/><br>
			<% } %>
			</form>
			<h3>Bid History:</h3>
			<ul>
			<% Iterable<Entity> bidHistory = (Iterable<Entity>)request.getAttribute("bidHistory"); %>
			<% if (bidHistory != null) for (Entity bid : bidHistory) { %>
			<li>
				<a href="/user?userKey=<%=KeyFactory.keyToString((Key)bid.getProperty("bidder"))%>">
					<%= Users.getUserForKey((Key)bid.getProperty("bidder")).getProperty("username") %>
				</a> 
				bidded:<%= bid.getProperty("price") %>
			</li>
			<% } %>
			</ul>
		<% } %>
 	</body>
</html>