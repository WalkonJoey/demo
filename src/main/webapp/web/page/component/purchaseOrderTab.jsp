<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<!-- 此页面用于TAB选项卡，展示相关联的发货关联采购信息 -->
</head>
<body class="order">
	<table class="tab_item_view_table">
		<c:choose>
			<c:when test="${po_tab!=null&&!empty po_tab}">
				<c:forEach var="delivery" items="${po_tab}" varStatus="status">
					<c:forEach var="deliveryPoItem" items="${delivery.value}"
						varStatus="itemStatus">
						<c:if test="${itemStatus.first}">
							<tr>
								<th>采购订单号：</th>
								<td><a href="javascript:void(0);"
									onClick="openPoViewTab('${deliveryPoItem.purchaseOrder.poID}','${deliveryPoItem.purchaseOrder.poNo}');return false;">${deliveryPoItem.purchaseOrder.poNo}</a></td>
								<th>skuNo：</th>
								<td>${deliveryPoItem.skuNo}</td>
								<th>发货数量：</th>
								<td>${deliveryPoItem.sentQty}</td>
							</tr>
						</c:if>
						<c:if test="${!itemStatus.first}">
							<tr>
								<th></th>
								<td></td>
								<th>skuNo：</th>
								<td>${deliveryPoItem.skuNo}</td>
								<th>发货数量：</th>
								<td>${deliveryPoItem.sentQty}</td>
							</tr>
						</c:if>
					</c:forEach>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<th>没有相关的发货信息</th>
				</tr>

			</c:otherwise>
		</c:choose>
	</table>
</body>
</html>