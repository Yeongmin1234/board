<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:forEach var="list" items="${list}">  
		<table border=1>
	    	<tr>${boardlist.bno}</tr>
	    	<tr><td>제목</td><td><a href="/read?bno=${list.bno}">${list.title}</a></td></tr>
	    	<tr><td>작성자</td><td>${list.writer}</td></tr>
			<tr><td>작성일자</td><td><fmt:formatDate value="${list.date}" pattern="yyyy/MM/dd"/></td></tr>
		</table>
	</c:forEach>
</body>
</html>