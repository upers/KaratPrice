<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%@ page import="com.karat.jpamodel.User, java.util.List, com.karat.models.ProductMarkup"%>
<%
	User user = (User) request.getAttribute("user");
	List<ProductMarkup> products = (List<ProductMarkup>) request.getAttribute("products");
%>

<mt:admin_tamplate title="<%= user.getName() %>">
	<jsp:attribute name="content">
	
	<div class="title_label">
		<h2>Управление наценкой на товар у пользователя: <%= user.getName() %></h2>	
	</div>
	
	<table>
		<h2>Пользователь: <%= user.getName() %></h2>
  		<tr>
	   		<th>Категория</th>
    		<th>
  				Наценка в %
    		</th>
    		
  		</tr>
  		<% for (ProductMarkup productM : products)  {%>
	   		<tr>
	   			<td><%= productM.getProductName() %></td>
    			<td>
    				<form action="/admin/user_management/product" method="post">
    					<input class="input_and_button_in_one_cell_input" type="text" name="product_markup" value="<%= productM.getMarkup() %>">
    					<input type="hidden" name="user_id" value="<%= user.getId() %>">
    					<input type="hidden" name="product_id" value="<%= productM.getProductId() %>">
    					<input class="input_and_button_in_one_cell_submit" type="submit" value="Изменить">
    				</form>
    			</td>
  			</tr>				
    	<% } %>
	</table>
	</jsp:attribute>
</mt:admin_tamplate>
