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
				<c:forEach var="poVo" items="${po_tab}" varStatus="status">
					<c:forEach var="soPoItem" items="${poVo.value}"
						varStatus="itemStatus">
						<c:if test="${itemStatus.first}">
							<tr>
								<th>采购订单号：</th>
								<td><a href="javascript:void(0);"
									onClick="openPoViewTab('${soPoItem.poID}','${soPoItem.poNo}');return false;">
										${soPoItem.poNo}</a></td>
								<th>skuNo：</th>
								<td>${soPoItem.skuNo}</td>
								<th>匹配数量：</th>
								<td>${soPoItem.matchQty}</td>
								<th>采购总数量：</th>
								<td>${soPoItem.poItemVo.prodQty}</td>
								<th>区外收货数量：</th>
								<td>${soPoItem.poItemVo.outReceivingQty}</td>
								<th>入库单送货数量：</th>
								<td>${soPoItem.poItemVo.realDeliveredQty}</td>
								<th>入库单收货数量：</th>
								<td>${soPoItem.poItemVo.receivedQty}</td>
								<th>入库单上架数量：</th>
								<td>${soPoItem.poItemVo.onshelfQty}</td>
							</tr>
						</c:if>
						<c:if test="${!itemStatus.first}">
							<tr>
								<th></th>
								<td></td>
								<th>skuNo：</th>
								<td>${soPoItem.skuNo}</td>
								<th>匹配数量：</th>
								<td>${soPoItem.matchQty}</td>
								<th>采购总数量：</th>
								<td>${soPoItem.poItemVo.prodQty}</td>
								<th>区外收货数量：</th>
								<td>${soPoItem.poItemVo.outReceivingQty}</td>
								<th>入库单送货数量：</th>
								<td>${soPoItem.poItemVo.realDeliveredQty}</td>
								<th>入库单收货数量：</th>
								<td>${soPoItem.poItemVo.receivedQty}</td>
								<th>入库单上架数量：</th>
								<td>${soPoItem.poItemVo.onshelfQty}</td>
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