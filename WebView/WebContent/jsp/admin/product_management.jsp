<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%@ page import="com.karat.jpamodel.User, java.util.List, com.karat.jpamodel.Product"%>
<%
	List<Product> products = (List<Product>) request.getAttribute("products");
	Integer categoryId = (Integer)request.getAttribute("category_id");
%>

<mt:admin_tamplate title="Продукты">
	<jsp:attribute name="content">
		<div class="title_label">
			<h2>Управление продуктами</h2>	
		</div>

	
	<a class="button" href="/admin/product_management/add-product/<%= categoryId %>">Добавить товар</a>
	
	<table>
  		<tr>
	   		<th>Имя</th>
	   		<th>Цена</th>
	   		<th>Изменить</th>
    		<th>
    			Удалить
    		</th>
  		</tr>
  		<% for (Product product : products)  {%>
	   		<tr>
 				<form action="/admin/product_management/change-name" method="post">
 				<td>
	 				<input type="text" name="product_name" value="<%= product.getName() %>">
 				</td>
 				 <td>
					<input type="text" name="product_price" value="<%= product.getPrice() %>"> 				 
 				</td>
 				 <td>
 				 	<input type="hidden" value="<%= product.getId() %>" name="product_id">
 				 	<input type="submit" value="Изменить">
 				</td>
   				</form>
    			<td>
    			<form action="/admin/product_management/delete" method="post">
	   				<input type="hidden" value="<%= product.getId() %>" name="product_id">
	   				<input type="submit" value="Удалить">
	   			</form>
    			</td>
  			</tr>				
    	<% } %>
	</table>
	</jsp:attribute>
</mt:admin_tamplate>