<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="mt" tagdir="/WEB-INF/tags"%>
<%@ page
	import="com.karat.jpamodel.User, java.util.List, com.karat.models.CategoryMarkup"%>
<%
	String header = (String) request.getAttribute("header");
	String link = (String) request.getAttribute("link");
%>

<mt:admin_tamplate title="<%=header%>">
	<jsp:attribute name="content">
	
	<div class="title_label">
		<h2><%=header%></h2>	
	</div>
	<form method="post" action="<%=link%>" enctype="multipart/form-data">
		Выбирите excel документ
		<input type="file" name="excel_price" accept=".xls,.xlsx" id="fileChooser" /><br /><br />
		<input type="submit" value="Upload" />
	</form>
	
	</jsp:attribute>
</mt:admin_tamplate>
