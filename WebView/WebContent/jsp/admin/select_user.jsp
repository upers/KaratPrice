<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%@ page import="java.util.List,com.karat.jpamodel.User"%>
<%
	List<User> users = (List<User>) request.getAttribute("all_users");
%>

<mt:admin_tamplate title="Select User">
	<jsp:attribute name="content">
	
	<div class="title_label">
		<h2>Выберите пользователя</h2>	
	</div>
	
	<table>
  		<tr>
	   		<th>Компания</th>
    		<th>Имя</th> 
    		<th>Фамилия</th>
    		<th>Почта</th>
    		<th>Телефон</th>
    		<th>Выбрать</th>
  		</tr>
  		<%
			for (User user : users) {
		%>
			<tr>
				<td><%=( user.getCompany() == null ) ? "" : user.getCompany() %></td>
				<td><%=( user.getName() == null ) ? "" : user.getName()%></td>
				<td><%=( user.getSurname() == null ) ? "" : user.getSurname() %></td>
				<td><%=( user.getEmail() == null ) ? "" : user.getEmail() %></td>
				<td><%=( user.getPhone() == null ) ? "" : user.getPhone()  %></td>
				<td><a href="/admin/user_management/<%=user.getId()%>">Выбрать</a></td>
			</tr>	
		<%
			}
		%>
	</table>
	</jsp:attribute>
</mt:admin_tamplate>
