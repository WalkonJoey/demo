<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${sessionScope.CURRENT_USER.loginID == null }">
	<jsp:forward page="login/login.jsp"></jsp:forward>
</c:if>
	<jsp:forward page="main.jsp"></jsp:forward>