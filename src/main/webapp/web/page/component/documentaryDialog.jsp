<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
.dialog_top {
	width: 350px;
	padding: 10px;
}
</style>
<script type="text/javascript">
	/*创建用户列表对话框*/
	var userDatagrid;
	function openDocumentaryDialog(obj, url, callBackFunc) {
		var userDialog = $('#userDialog').show().dialog({
			autoOpen : false,
			modal : true,
			bgiframe : true,
			position : [ 'center', 'top' ],
			width : 380,
			height : 360,
			draggable : true,
			resizable : true,
			onClose : function() {
				var rows = $('#userDatagrid').datagrid("getSelections");
				callBackFunc(obj, rows);
				$('#userDatagrid').datagrid('unselectAll');
			},
		});

		userDatagrid = $('#userDatagrid').datagrid({
			url : url,
			title : '',
			pagination : true,
			pageSize : 10,
			pageList : [ 10,15,20 ],
			fit : true,
			fitColumns : true,
			nowrap : true,
			border : true,
			idField : 'userID',
			sortOrder : 'asc',
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers : true,
			singleSelect : true,
			onDblClickRow : function(index, field, value) {
				//var ed = $('#userDatagrid').datagrid('selectRow', index);
				userDialog.dialog("close");
			},
			columns : [ [ {
				title : '用户ID',
				field : 'userID',
				width : 10,
				hidden : true
			}, {
				title : '用户中文名',
				field : 'userCnName',
				width : 30
			},{
				title : '用户英文名',
				field : 'userEnName',
				width : 30
			},{
				title : '所属部门',
				field : 'departmentName',
				width : 30

			} ] ]
		});
	}

</script>
</head>
<body>
	<div id="userDialog" title="选择用户"
		style="display: none; overflow: hidden; height: 360px;">
		<div style="height: 320px">
			<div id="userDatagrid"></div>
		</div>
	</div>
</body>
</html>