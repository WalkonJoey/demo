<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<!-- 此页面用于TAB选项卡，展示相关联的出货单信息 -->
</head>
<body>
	<c:choose>
		<c:when test="${tabEntity!=null}">
			<label>物流发运单信息：</label>
			<table class="tab_item_view_table">
				<tr>
					<th><label>出货单号:</label></th>
					<td>
						${tabEntity.shipmentID}/>
					</td>
					
					<th><label>出货日期:</label></th>
					<td>
						${tabEntity.bomSentDt}/>
					</td>
				</tr>
				
				<tr>
					<th><label>操作人员:</label></th>
					<td>
						${tabEntity.operatorName}/>
					</td>
					
					<th><label>操作时间:</label></th>
					<td>
						${tabEntity.operateDt}/>
					</td>
				</tr>
			</table>
		</c:when>
		<c:otherwise>
				<p>没有相关的出货单信息
		</c:otherwise>
	</c:choose>
</body>
</html>