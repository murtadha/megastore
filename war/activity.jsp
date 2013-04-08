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
		<title>Welcome to MegaStore</title>
	</head>
	<body>
		<h2><%= ((Entity)session.getAttribute("user")).getProperty("username") %>'s Activity</h2>
		
		<ul>
		<h4><u>Auctions</u></h4>
		<% Iterable<Entity> auctions = (Iterable<Entity>)request.getAttribute("auctions"); %>
		<% if (auctions == null) { %>
		Can't find any stupid auctions
		<% } else for (Entity auction : auctions) { %>
		<li>
			<a href="/auction?auctionKey=<%=KeyFactory.keyToString(auction.getKey())%>"><%= auction.getProperty("name") %></a>
			<% if ((Long)auction.getProperty("endTime") < System.currentTimeMillis()) { %><b>EXPIRED</b><% } %>
		</li>
		<% } %>
		<h4><u>Bids</u></h4>
		<% Iterable<Entity> bids = (Iterable<Entity>)request.getAttribute("bids"); %>
		<% Map<Key, Entity> bidsAuctions = (Map<Key, Entity>)request.getAttribute("bidsAuctions"); %>
		<% if (bids == null) { %>
		Can't find any stupid bids
		<% } else for (Entity bid : bids) { %>
		<%	Entity auction = bidsAuctions.get(bid.getParent()); %>
		<li>
			<a href="/auction?auctionKey=<%=KeyFactory.keyToString(auction.getKey())%>"><%= auction.getProperty("name") %></a>
			Bid Value: <%= bid.getProperty("price") %>
		</li>
		<% } %>
		</ul>		
 	</body>
</html>