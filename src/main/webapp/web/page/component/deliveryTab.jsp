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
			<c:when test="${delivery_tab!=null&&!empty delivery_tab}">
				<c:forEach var="delivery" items="${delivery_tab}" varStatus="status">
					<c:forEach var="deliveryPoItem" items="${delivery.value}"
						varStatus="itemStatus">
						<c:if test="${itemStatus.first}">
							<tr>
								<th>快递单号：</th>
								<td>
								<a href="javascript:void(0);"
									onClick="openDeliveryViewTab('${deliveryPoItem.delivery.deliveryID}','${deliveryPoItem.delivery.deliveryNo}');return false;">${deliveryPoItem.delivery.express.expressNo}</a>
								</td>
								<th>skuNo：</th>
								<td><input type="text" name="skuNo" class="no_border"
									value="${deliveryPoItem.skuNo}" readonly /></td>
								<th>发货数量：</th>
								<td><input type="text" name="sentQty" style="width:40px" class="no_border"
									value="${deliveryPoItem.sentQty}" readonly /></td>
							</tr>
						</c:if>
						<c:if test="${!itemStatus.first}">
							<tr>
								<th></th>
								<td></td>
								<th>skuNo：</th>
								<td><input type="text" name="skuNo" class="no_border"
									value="${deliveryPoItem.skuNo}" readonly /></td>
								<th>发货数量：</th>
								<td><input type="text" name="sentQty" style="width:40px" class="no_border"
									value="${deliveryPoItem.sentQty}" readonly /></td>
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