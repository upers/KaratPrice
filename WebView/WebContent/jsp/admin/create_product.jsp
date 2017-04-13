<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%@ page import="com.karat.jpamodel.User, java.util.List, com.karat.jpamodel.Category"%>
<%
	Integer categoryId = (Integer)request.getAttribute("category_id");
%>

<mt:admin_tamplate title="Создать товар">
	<jsp:attribute name="content">
		<div class="registration">
			<form action="/admin/product_management/create-product" method="post">
			<i>Название товара: </i><input name="product_name" type="text" value=""><br>
			<i>Входная цена: </i><input name="product_price" type="text" value=""><br>
			<input name="category_id" type="hidden" value="<%= categoryId%>">
			<input type="submit" value="Создать">
		</form>
		</div>
	</jsp:attribute>
</mt:admin_tamplate>