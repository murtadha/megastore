<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*" %>
    <%@ page import="com.google.appengine.api.datastore.Entity" %>
    <%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<!DOCTYPE html>
<html>
<body>
	<h1>
		Welcome <%= ((Entity)session.getAttribute("user")).getProperty("username") %>
	</h1>
	<a href="/logout">Logout</a>
	<a href="/activity">Activity</a>
	
		<form action="/search" method="GET" >
		<h2 style="display: inline-block;">Search</h2>
		<input type="text" name="searchString"/>
		<input type="submit" value="Search" />
		</form>
	
	<h2>Create Auction</h2>
	<form action="/auction" method="POST">
		<h>Item Name:</h><input type="text" name="enteredItemName"/><br>
		<h>Item Description:</h><input type="text" name="enteredItemDescription"/><br>
		<h>Starting Bid:</h><input type="text" name="enteredStartingBid"/><br>
		<h>Period:</h><input type="text" name="enteredPeriod"/>hours<br>
		<input type="submit" name="action" value="Create"/><br>
	</form>
	
</body>
</html>
