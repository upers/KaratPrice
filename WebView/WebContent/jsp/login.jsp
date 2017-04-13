<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/css/style.css" rel="stylesheet" type="text/css">
<title>Войти</title>
</head>
<body>
<%
 	String error = (String)request.getAttribute("ErrorMessage");
%>	
	<div class="login_form">
		<div class="login">
			<form action="/signin" method="post">
			Логин:<br> <input type="text" name="login"><br> 
			Пароль:<br> <input type="password" name="password"><br> <br>
			<input type="submit" value="Войти">
		</form>		
		</div>
	</div>
<%	if (error != null) { %>
	<script type="text/javascript">
		alert("<%= error %>");
	</script>
<%	} %>
</body>
</html>