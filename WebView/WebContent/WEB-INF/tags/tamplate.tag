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
		<div>
            <ul id="log_out">                                              
                <li><a href="/signout">Выйти</a></li>
            </ul>
        </div>
	</div>
	
	<div class="center_content">
		${content}	
	</div>
</div>
</body>
</html>
