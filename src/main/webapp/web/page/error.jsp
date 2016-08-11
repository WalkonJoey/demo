<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Complex Layout - jQuery EasyUI Demo</title>
	<link rel="stylesheet" type="text/css" href="web/css/common/easyui.css">
	<link rel="stylesheet" type="text/css" href="web/css/common/icon.css">
	<link rel="stylesheet" type="text/css" href="web/css/common/demo.css">
	<script src="web/js/common/jquery-1.11.1.js"></script>
	<script src="web/js/common/jquery.easyui.min.js"></script>
</head>
<body>
<center>
<br>
<br>
<h2 style="font-size: 20px; color: red;">出错了！！！详细请看控制台日志</h2>
</center>

${requestScope.ex.message}
</body>
</html>