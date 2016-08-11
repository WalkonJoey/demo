<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<!-- 此页面用于TAB选项卡，展示相关联的物流信息 -->
</head>
<body class="order">
	<table class="tab_item_view_table">
	<c:choose>
		<c:when test="${tabEntity.logiNo !=null}">
			<!-- label>物流发运单信息：</label -->
				<tr>
					<th><label>发运单号:</label></th>
					<td>
						${shipEntity.shipmentCode}
					</td>
					
					<th><label>发运日期:</label></th>
					<td>
						${bomSentDt}
					</td>
				</tr>
				
				<tr>
					<th><label>物流单号:</label></th>
					<td>
						${tabEntity.logiNo}
					</td>
					
					<th><label>物流公司:</label></th>
					<td>
						${tabEntity.logiComCode}
					</td>
				</tr>
				
<!-- 				<tr> -->
<!-- 					<th><label>发货人:</label></th> -->
<!-- 					<td> -->
<%-- 						<input  type="text" name="sender" value="${tabEntity.sender}" readonly size="15"/> --%>
<!-- 					</td> -->
					
<!-- 					<th><label>发货人电话:</label></th> -->
<!-- 					<td> -->
<%-- 						<input  type="text" name="mobile" value="${tabEntity.mobile}" readonly size="15"/> --%>
<!-- 					</td> -->
					
<!-- 					<th><label>收件国家:</label></th> -->
<!-- 					<td> -->
<%-- 						<input  type="text" name="destCountry" value="${tabEntity.destCountry}" readonly size="15"/> --%>
<!-- 					</td> -->
					
<!-- 					<th><label>收件地址:</label></th> -->
<!-- 					<td> -->
<%-- 						<input  type="text" name="destAddress" value="${tabEntity.destAddress}" readonly size="15"/> --%>
<!-- 					</td> -->
<!-- 				</tr> -->
				
<!-- 				<tr> -->
<!-- 					<th><label>收件人:</label></th> -->
<!-- 					<td> -->
<%-- 						<input  type="text" name="destName" value="${tabEntity.destName}" readonly size="15"/> --%>
<!-- 					</td> -->
					
<!-- 					<th><label>收件人电话:</label></th> -->
<!-- 					<td> -->
<%-- 						<input  type="text" name="destMobile" value="${tabEntity.destMobile}" readonly size="15"/> --%>
<!-- 					</td> -->
					
<!-- 					<th><label>物流费用:</label></th> -->
<!-- 					<td> -->
<%-- 						<input  type="text" name="cost" value="${tabEntity.cost}" readonly size="15"/> --%>
<!-- 					</td> -->
					
<!-- 					<th><label>结算状态:</label></th> -->
<!-- 					<td> -->
<%-- 						<c:choose> --%>
<%-- 							<c:when test="${tabEntity.payStatus==0}"> --%>
<!-- 								<input  type="text" name="payStatus" value="未结算" readonly size="15"/> -->
<%-- 							</c:when> --%>
							
<%-- 							<c:when test="${tabEntity.payStatus==1}"> --%>
<!-- 								<input  type="text" name="payStatus" value="已结算" readonly size="15"/> -->
<%-- 							</c:when> --%>
							
<%-- 							<c:otherwise> --%>
<!-- 								<input  type="text" name="payStatus" value="未知" readonly size="15"/> -->
<%-- 							</c:otherwise> --%>
<%-- 						</c:choose> --%>
						
<!-- 					</td> -->
<!-- 				</tr> -->
		</c:when>
		<c:otherwise>
		<tr>
				<th>没有相关的物流发运单信息</th>
		</tr>
		</c:otherwise>
	</c:choose>
		</table>
</body>
</html>