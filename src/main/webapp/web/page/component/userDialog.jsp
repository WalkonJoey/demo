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
	function openUserDialog(obj, url, callBackFunc) {
		var userDialog = $('#userDialog').show().dialog({
			autoOpen : false,
			modal : true,
			bgiframe : true,
			position : [ 'center', 'top' ],
			width : 500,
			height : 500,
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
			pageSize : 15,
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
				var ed = $('#userDatagrid').datagrid('selectRow', index);
				$('#userDialog').dialog("close");
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
				width : 50

			} ] ]
		});

		userSearchBox = $('#userSearch').searchbox({
			searcher : userSearch,
			menu : '#userMenu',
			prompt : '请输入',
			height : 26,
			width : 300
		});
	}

	/*搜索*/
	function userSearch(value, name) {
		/* $.ajax({
			url : 'user/getUser4Dialog/'+tag,
			data : {
				param_name:name,
				param_value:value
			},
			cache : false,
			success : function(response) {
				$('#userDatagrid').datagrid('loadData',response);
			}
		}); */
		/* if(name==="userCnName"){
			userDatagrid.datagrid('load',
					{
						isSearch:"yes",
						userCnName : value
					});
		}else{
			userDatagrid.datagrid('load',
					{
						isSearch:"yes",
						departJob : value
					});
		} */
		userDatagrid.datagrid('load', {
			isSearch : "yes",
			param_name : name,
			param_value : value
		});
		//});
	}
</script>
</head>
<body>
	<div id="userDialog" title="选择用户"
		style="display: none; overflow: hidden; height: 500px;">
		<!-- <table>
			<tr>
				<td><input id="userSearch" class="easyui-searchbox"
					data-options="searcher:userSearch,prompt:'请输入',menu:'#userMenu'"></input>
					<div id="userMenu" style="width: 120px">
						<div data-options="name:'userCnName'">用户中文名</div>
					</div></td>
					<td>
					</td>
			<tr>
		</table> -->
		<div class="dialog_top">
			<input id="userSearch"></input>
		</div>
		<div id="userMenu">
			<div data-options="name:'userCnName'">用户中文名</div>
			<!-- <div data-options="name:'departJob'">职务</div> -->
		</div>
		<div style="height: 420px">
			<div id="userDatagrid"></div>
		</div>
	</div>
</body>
</html>