<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%@ page import="com.karat.jpamodel.User, java.util.List, com.karat.jpamodel.Category"%>
<%
	Integer categoryId = (request.getAttribute("category_id") == null) ? null : (Integer)request.getAttribute("category_id") ;
%>

<mt:admin_tamplate title="Создать категорию">
	<jsp:attribute name="content">
	<div class="registration">
		<form action="/admin/category_management/create-category" method="post">
			<i>Название категории: </i><input name="category_name" type="text" value=""><br>
			<input name="category_id" type="hidden" value="<%= (categoryId == null) ? "" : categoryId%>">
			<input type="submit" value="Создать">
		</form>
	</div>
	</jsp:attribute>
</mt:admin_tamplate>