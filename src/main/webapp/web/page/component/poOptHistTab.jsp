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
			<c:when test="${poOptHistList!=null&&!empty poOptHistList}">
				<c:forEach var="optHist" items="${poOptHistList}" varStatus="status">
					<tr>
						<th>操作类型：</th>
						<td>
							<c:choose>
								<c:when test="${optHist.operateType == 1}">
									新增
								</c:when>
								<c:when test="${optHist.operateType == 2}">
									修改
								</c:when>
								<c:when test="${optHist.operateType == 3}">
									删除
								</c:when>
								<c:when test="${optHist.operateType == 4}">
									修改
								</c:when>
								<c:otherwise>
									未知
								</c:otherwise>
							</c:choose>
						
						</td>
						<th>备注：</th>
						<td>${optHist.comment}</td>
						<th>操作人：</th>
						<td>${optHist.creatorName}</td>
						<th>操作时间：</th>
						<td><fmt:formatDate value="${optHist.createDt}"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td style="text-align:center;">
						<script type="text/javascript">
							function showPoHist(histID){
								parent.addTab('tabId_viewPo', '采购订单历史详情', 'po/viewHist/'+ histID);
							}
						</script>
						<c:if test="${optHist.operateType == 4}">
							<a href="javascript:void(0);" onclick="showPoHist(${optHist.histID})">查看</a>
						</c:if>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<th>没有相关的操作信息</th>
				</tr>

			</c:otherwise>
		</c:choose>
	</table>
</body>
</html>