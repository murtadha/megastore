<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>`
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org   /TR/html4/loose.dtd">`
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Welcome to MegaStore</title>`
	</head>
	<body>
		<h2>Login</h2>
		<form action="/login" method="POST">
		<input type="text" name="enteredUsername"/><br>
		<input type="password" name="enteredPassword"><br><br><br>
		<input type="submit" name="buttonEvent" value="login"/><br>
		</form>
		
		<h2>Sign-up</h2>
		<form action="/register" method="POST">
		<input type="text" name="enteredUsername"/><br>
		<input type="password" name="enteredPassword"><br><br><br>
		<input type="submit" name="buttonEvent" value="signup"/><br>
		</form>
		
 	</body>
</html>