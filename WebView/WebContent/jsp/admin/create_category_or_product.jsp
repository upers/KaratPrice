<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%@ page import="com.karat.jpamodel.User, java.util.List, com.karat.jpamodel.Category"%>
<%
	Category  category = (Category) request.getAttribute("category");
%>

<mt:admin_tamplate title="Категории">
	<jsp:attribute name="content">
	<table style="width: 100%" border="1" cellpadding="5" cellspacing="5">
		<h2><%= category.getName() %></h2>
  		<h3> Данная категория пустая выберите что вы хотите создать</h3>
  		<div class="two_link_page">
			<a href="/admin/category_management/add-category/<%=category.getId() %>">Подкатегорию</a>
  			<a href="/admin/product_management/add-product/<%=category.getId() %>">Товар</a>  		
  		</div>
	</jsp:attribute>
</mt:admin_tamplate>