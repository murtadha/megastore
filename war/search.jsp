<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>`
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org   /TR/html4/loose.dtd">`
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Welcome to MegaStore</title>`
	</head>
	<body>
		<h2>Search</h2>
		<form action="/search" method="GET">
		<input type="text" name="search_string"/><br>
		</form>
		
		<ul>
		<% String[] items = (String [])request.getAttribute("items"); %>
		<% for (String i : items) { %>
		<li><%= i %></li>
		<% } %>
		</ul>		
 	</body>
</html>