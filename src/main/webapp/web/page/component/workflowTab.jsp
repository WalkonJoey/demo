<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<!-- 此页面用于TAB选项卡，展示相关联的工作流信息 -->
</head>
<body class="order">
	<table class="tableForm">
		<c:choose>
			<c:when test="${taskList!=null}">
				<tr>
					<th>单据审批流程图：</th>
				</tr>
				<c:forEach var="task" items="${taskList}" varStatus="status">
					<tr>
						<th><label>序号${status.count}：</label></th>
						<th><label>操作人</label></th>
						<td>[${task.actor.userCnName}]</td>
						<th><label>审批动作：</label></th>
						<td>[ <c:choose>
								<c:when test="${task.approveStatus==1}">
					提交
					</c:when>
								<c:when test="${task.approveStatus==2}">
					同意
					</c:when>
								<c:when test="${task.approveStatus==4}">
					拒绝
					</c:when>
								<c:when test="${task.approveStatus==16}">
					关闭
					</c:when>
								<c:otherwise>审批中</c:otherwise>
							</c:choose> ]
						</td>
						<th><label>审批时间：</label></th>
						<td>[<fmt:formatDate value="${task.approveDt}"
								pattern="yyyy-MM-dd HH:mm:ss" />]
						</td>
						<th><label>备注：</label></th>
						<td>[${task.comment}]
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<th>没有此单据相关的审批信息</th>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>
</body>
</html>