<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%@ page import="com.karat.jpamodel.User, java.util.List, com.karat.models.CategoryMarkup"%>
<%
	User user = (User) request.getAttribute("user");
	List<CategoryMarkup> categories = (List<CategoryMarkup>) request.getAttribute("categories");
%>

<mt:admin_tamplate title="<%= user.getName() %>">
	<jsp:attribute name="content">
	
	<div class="title_label">
		<h2>Управление пользователем: <%= user.getName() %></h2>	
	</div>
	
	<table>
  		<tr>
	   		<th>Категория</th>
    		<th>
  				Наценка в %
    		</th>
    		<th>
    			Содержимое
    		</th>
  		</tr>
  		<% for (CategoryMarkup categoryM : categories)  {%>
	   	<tr>
	   		<td><%= categoryM.getCategoryName() %></td>
    		<td>
	    		<form action="/admin/user_management/category" method="post">
	    			<input class="input_and_button_in_one_cell_input" type="text" name="category_markup" value="<%= (categoryM.getMarkup() == null)? "" : categoryM.getMarkup() %>">
	    			<input type="hidden" name="user_id" value="<%= user.getId() %>">
	    			<input type="hidden" name="category_id" value="<%= categoryM.getCategoryId() %>">
	    			<input class="input_and_button_in_one_cell_submit" type="submit" value="Изменить">
	    		</form>
    		</td>
    		<td>
    			<a href="/admin/user_management/<%=user.getId() + "/" + categoryM.getCategoryId()%>">Показать</a>
    		</td>
  			</tr>				
    	<% } %>
	</table>
	
	</jsp:attribute>
</mt:admin_tamplate>
