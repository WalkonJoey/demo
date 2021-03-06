<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<!-- 此页面用于TAB选项卡，展示相关联的入库信息 -->
</head>
<body class="order">
	<table class="tab_item_view_table">
		<c:choose>
			<c:when test="${whEntry_tab!=null}">
				<c:forEach var="whEntry" items="${whEntry_tab}" varStatus="status">
					<c:forEach var="whEntryItem" items="${whEntry.entryList}"
						varStatus="itemStatus">
						<c:if test="${itemStatus.first}">
							<tr>
								<th>入库单号：</th>
								<td>
								<a href="javascript:void(0);"
									onClick="disWhEntryInfo('${whEntryItem.whEntryNo}');return false;">${whEntryItem.whEntryNo}</a>
								</td>
								<th>skuNo：</th>
								<td>${whEntryItem.skuNo}</td>
								<th>发货数量：</th>
								<td>${whEntryItem.deliveredQty}</td>
								<th>收货数量：</th>
								<td>${whEntryItem.receivedQty}</td>
								<th>上架数量：</th>
								<td>${whEntryItem.onShelfQty}</td>
							</tr>
						</c:if>
						<c:if test="${!itemStatus.first}">
							<tr>
								<th></th>
								<td></td>
								<th>skuNo：</th>
								<td>${whEntryItem.skuNo}</td>
								<th>发货数量：</th>
								<td>${whEntryItem.deliveredQty}</td>
								<th>收货数量：</th>
								<td>${whEntryItem.receivedQty}</td>
								<th>上架数量：</th>
								<td>${whEntryItem.onShelfQty}</td>
							</tr>
						</c:if>
					</c:forEach>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<th>没有相关的入库信息</th>
				</tr>

			</c:otherwise>
		</c:choose>
	</table>
</body>
</html>