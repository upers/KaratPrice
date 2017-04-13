<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%@ page import="com.karat.jpamodel.User, java.util.List, com.karat.jpamodel.Category"%>
<%
	List<Category> categories = (List<Category>) request.getAttribute("categories");
	Integer parentCategoryId = (Integer)request.getAttribute("parent_category_id");
%>

<mt:tamplate title="Категории">
	<jsp:attribute name="content">
	
	<div class="title_label">
		<h2><a href="/user_controller">Категории</a></h2>	
	</div>
	
	<table>
  		<tr>
	   		<th>Имя</th>
    		<th>
  				Подкатегории
    		</th>
  		</tr>
  		<% for (Category category : categories)  {%>
	   		<tr>
	   			<td>
					<i><%= category.getName() %></i>
	   			</td>
    			<td>
    				<a href="/user_controller/<%= category.getId()%>">Показать</a>
    			</td>
  			</tr>				
    	<% } %>
	</table>
	</jsp:attribute>
</mt:tamplate>