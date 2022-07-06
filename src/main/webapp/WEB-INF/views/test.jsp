<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<c:choose>
	    <c:when test="${empty isIncludeOrNot}" >
			<h2 colspan="3">포함 / 미포함</h2>
	    </c:when>
	    <c:otherwise>
			<h2>${isIncludeOrNot}</h2>
	    </c:otherwise>
	</c:choose>
</body>
</html>