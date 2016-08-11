<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
</head>
<body class="order">
	<table class="tab_item_view_table">
		<c:choose>
			<c:when test="${po_tab!=null&&!empty po_tab}">
				<c:forEach var="poVo" items="${po_tab}" varStatus="status">
					<c:forEach var="soPoItem" items="${poVo.value}"
						varStatus="itemStatus">
						<c:if test="${itemStatus.first}">
							<tr>
								<th>销售订单号：</th>
								<td><a href="javascript:void(0);"
									onClick="openSoViewTab('${soPoItem.saleID}','${soPoItem.saleNo}');return false;">
										${soPoItem.saleNo}</a></td>
								<th>skuNo：</th>
								<td>${soPoItem.skuNo}</td>
								<th>使用数量：</th>
								<td>${soPoItem.matchQty}</td>
								<th>SO确认时间：</th>
								<td><fmt:formatDate value="${soPoItem.soConfirmDt}" pattern="yyyy-MM-dd"/></td>
							</tr>
						</c:if>
						<c:if test="${!itemStatus.first}">
							<tr>
								<th></th>
								<td></td>
								<th>skuNo：</th>
								<td>${soPoItem.skuNo}</td>
								<th>使用数量：</th>
								<td>${soPoItem.matchQty}</td>
								<th>SO确认时间：</th>
								<td><fmt:formatDate value="${soPoItem.soConfirmDt}" pattern="yyyy-MM-dd"/></td>
							</tr>
						</c:if>
					</c:forEach>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<th>没有相关的采购订单信息</th>
				</tr>

			</c:otherwise>
		</c:choose>
	</table>
</body>
</html>