<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="title" required="true" rtexprvalue="true" %>
<%@ attribute name="content" required="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
<title>${title }</title>
</head>
<body>
<div id="main_container">
	<div id="header">
		<div id="logo"><a href="home.html"><img src="${pageContext.request.contextPath}/images/logo.png" alt="" title="" border="0" /></a></div>
		<div id="menu">
            <ul>                                              
                <li><a href="/admin/category_management">Управления товарами</a></li>
                <li class="divider"></li>
                <li><a href="/admin/user_management">Управление пользователями</a></li>
                <li class="divider"></li>
                <li><a href="/admin/registration">Зарегистрировать пользователя</a></li>
            </ul>
        </div>
	</div>
	
	<div class="center_content">
		${content}	
	</div>
</div>
</body>
</html>
