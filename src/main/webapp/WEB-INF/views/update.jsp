<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="update" method="post">
		<table border=1>
	    	<tr><input type="hidden" name="bno" value="${update.bno}"></tr>
	    	<tr><td>제목</td><td><input type="text" name="title" value="${update.title}"></td></tr>
		    <tr><td>내용</td><td><textarea name="text" cols="50" rows="30" maxlength="2000">${update.text}</textarea></td></tr>
	    	<tr><td>작성자</td><td>${update.writer}</td></tr>
			<tr><td>작성일자</td><td>${update.date}</td></tr>
		</table>
		<input type="submit" value="수정하기">
	</form>
	
</body>
</html>