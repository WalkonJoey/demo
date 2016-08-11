<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<!-- 此页面用于TAB选项卡，展示相关联的各SKU的收货入库上架数量信息 -->
</head>
<body class="order">
	<table class="tab_item_view_table">
		<c:choose>
			<c:when test="${poItemQty_tab!=null&&!empty poItemQty_tab}">
				<c:forEach var="itemQty" items="${poItemQty_tab}" varStatus="status">
					<tr>
						<th>skuNo：</th>
						<td>${itemQty.skuNo}</td>
						<th>已发货数量：</th>
						<td>${itemQty.deliveredQty}</td>
						<th>已收货数量：</th>
						<td>${itemQty.receivedQty}</td>
						<th>已上架数量：</th>
						<td>${itemQty.onshelfQty}</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<th>没有相关的skuNo数量信息</th>
				</tr>

			</c:otherwise>
		</c:choose>
	</table>
</body>
</html>