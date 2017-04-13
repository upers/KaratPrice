<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%@ page import="com.karat.jpamodel.User, java.util.Map, com.karat.jpamodel.Category"%>
<%
	Map<String, Double> products = (Map<String, Double>) request.getAttribute("products");
%>

<mt:tamplate title="Категории">
	<jsp:attribute name="content">
	
	<div class="title_label">
		<h2><a href="/user_controller">Категории</a></h2>	
	</div>
	
	<table>
  		<tr>
	   		<th>Товар</th>
    		<th>
  				Цена
    		</th>
  		</tr>
  		<% for (String name : products.keySet())  {%>
	   		<tr>
	   			<td>
					<i><%= name %></i>
	   			</td>
    			<td>
    				<i><%= products.get(name) %></i>
    			</td>
  			</tr>				
    	<% } %>
	</table>
	</jsp:attribute>
</mt:tamplate>