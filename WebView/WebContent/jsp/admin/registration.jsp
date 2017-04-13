<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%
 	String error = (String)request.getAttribute("ErrorMessage");
%>

<mt:admin_tamplate title="Регистрация пользователя">
	<jsp:attribute name="content">
	<div class="title_label">
		<h2>Регистрация пользователя</h2>	
	</div>
		<div class="registration">
			<form action="/admin/registration" method="post">
				<div class="regist_label">Логин: </div><input type="text" name="login"><br>
				<div class="regist_label">Пароль: </div><input type="password" name="password"><br>
				<div class="regist_label">Пароль: </div><input type="password" name="password2"><br>
				<div class="regist_label">Почта:</div><input type="text" name="email"><br>
				<div class="regist_label">Имя:</div><input type="text" name="name"><br>
				<div class="regist_label">Фамилия: </div><input type="text" name="surname"><br>
				<div class="regist_label">Кампания:</div><input type="text" name="company"><br>
				<input class="submit" type="submit" value="Зарегистрировать">
			</form>
		</div>
	<%	if (error != null) { %>
	<script type="text/javascript">
		alert("<%= error %>");
	</script>
<%	} %>
	</jsp:attribute>
</mt:admin_tamplate>
