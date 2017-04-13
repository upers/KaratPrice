<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%@ page import="com.karat.jpamodel.User, java.util.List, com.karat.jpamodel.Category"%>
<%
	List<Category> categories = (List<Category>) request.getAttribute("categories");
	Integer parentCategoryId = (Integer)request.getAttribute("parent_category_id");
%>

<mt:admin_tamplate title="Категории">
	<jsp:attribute name="content">
	
	<div class="title_label">
		<h2>Управление категориями</h2>	
	</div>
	
	<a class="button" href="/admin/category_management/add-category/<%= (parentCategoryId == null) ? "" : parentCategoryId%>">Добавить категорию</a>
	<a class="button_big_width" href="/admin/excel/update_product_price">Загрузить цены из Excel</a>
	
	<table>
  		<tr>
	   		<th>Имя</th>
    		<th>
  				Подкатегории
    		</th>
    		<th>
    			Удалить
    		</th>
  		</tr>
  		<% for (Category category : categories)  {%>
	   		<tr>
	   			<td>
	   				<form action="/admin/category_management/change-name" method="post">
	   					<input class="input_and_button_in_one_cell_input" type="text" name="category_name" value="<%= category.getName() %>">
	   					<input type="hidden" value="<%= category.getId() %>" name="category_id">
	   					<input class="input_and_button_in_one_cell_submit" type="submit" value="Изменить">
	   				</form>
	   			</td>
    			<td>
    				<a href="/admin/category_management/<%= category.getId()%>">Показать</a>
    			</td>
    			<td>
    			<form action="/admin/category_management/delete" method="post">
	   					<input type="hidden" value="<%= category.getId() %>" name="category_id">
	   					<input type="submit" value="Удалить">
	   			</form>
    			</td>
  			</tr>				
    	<% } %>
	</table>
	</jsp:attribute>
</mt:admin_tamplate>