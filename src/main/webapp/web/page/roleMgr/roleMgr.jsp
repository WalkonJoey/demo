<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<style>
	.dlog_tb_form tbody{
		display:table;
	}
	.dlog_tb_form tr{
		height:45px;
	}
	.dlog_tb_form th{
		padding:0px 10px;
		text-align:right;
	}
	.i_input{
	border: 1px solid #c9c9c9;
    border-radius: 3px;
    font-size: 14px;
    height: 18px;
    padding: 5px;
    width: 200px;
    box-sizing: content-box;
}
</style>

</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false"
		style="overflow: hidden;">
		<table id="datagrid"></table>
	</div>

	<div id="easyuiDialog">
		<form id="easyuiForm" method="post">
			<table class="dlog_tb_form">
				<tr style="display: none;">
					<th>角色ID:</th>
					<td><input name="roleID" type="hidden" /></td>
				</tr>
				<tr>
					<th>角色代码:</th>
					<td><input name="roleCode" class="i_input easyui-validatebox"
						required="true" /></td>
				</tr>
				<tr>
					<th>角色名称:</th>
					<td><input name="roleName" class="i_input easyui-validatebox"
						required="true" /></td>
				</tr>
				<tr>
					<th>是否启用:</th>
					<td><input name="isEnable" type="radio" value="1"
						checked="checked" />启用<input name="isEnable" type="radio"
						value="0" />不启用</td>
				</tr>
				<tr>
					<th>备注:</th>
					<td><input name="comment" class="i_input easyui-validatebox"/></td>
				</tr>
				
			</table>
		</form>
	</div>
	
	<div id="Dialog">
		<form id="permissionForm" method="post">
			<ul id="permissionTree" ></ul>
		</form>
	</div>
</body>

<script type="text/javascript">
var datagrid;
var easyuiDialog;
var easyuiForm;
var permissionDialog;
//var permissionForm;
var permissionTree;
$(function(){
	
	easyuiForm = $('#easyuiForm').form();
	
	easyuiDialog = $('#easyuiDialog').show().dialog({
		modal : true,
		title : '角色信息',
		width : 480,
		height : 300,
		buttons : [ {
			text : '确定',
			handler : function() {
				if (easyuiForm.find('[name=roleID]').val() != '') {
					var isValid = easyuiForm.form('validate');
						if (!isValid){
							return;
						}
					easyuiForm.form('submit', {
						url : 'role/updateRole',
						success : function(data) {
							easyuiDialog.dialog('close');
							$.messager.show({
								msg : '角色编辑成功！',
								title : '提示'
							});
							datagrid.datagrid('reload');
						}
					});
				} else {
					var isValid = easyuiForm.form('validate');
						if (!isValid){
							return;
						}
					easyuiForm.form('submit', {
						url : 'role/addRole',
						success : function(data) {
							try {
								var d = $.parseJSON(data);
								if (d) {
									easyuiDialog.dialog('close');
									$.messager.show({
										msg : '角色创建成功！',
										title : '提示'
									});
									datagrid.datagrid('reload');
								}
							} catch (e) {
								$.messager.show({
									msg : '角色名称已存在！',
									title : '提示'
								});
							}
						}
					});
				}
			}
		} ]
	}).dialog('close'); 
	
	datagrid = $('#datagrid').datagrid({
		url : 'role/getRolesForDatagrid',
		title : '',
		iconCls : 'icon-save',
		pagination : true,
		pageSize : 15,
		pageList : [10, 15,20,30 ],
		fit : true,
		fitColumns : false,
		rownumbers: true,
		nowrap : false,
		border : false,
		idField : 'roleID',
		sortName : 'roleID',
		sortOrder : 'desc',
		checkOnSelect : true,
		selectOnCheck : true,
		singleSelect:true,
		frozenColumns : [ [ {
			title : '角色ID',
			field : 'roleID',
			width : 10,
			sortable : true,
			checkbox:true
		},{
			title : '角色代码',
			field : 'roleCode',
			width : 100
		}, {
			title : '角色名称',
			field : 'roleName',
			width : 150
		} ] ],
		columns : [ [  {
			title : '是否启用',
			field : 'isEnable',
			align:'center',
			sortable:true,
			width : 70,
			formatter:function(value,row,index){  
				if(value==1)
                	return '是';  
				else 
					return '否';
            },
            sorter:function(a,b){  
                  return a>b?1:-1;
            } 
		}, {
			title : '备注',
			field : 'comment',
			width : 300
		}, {
			field : 'operatorName',
			title : '操作人员',
			align:'center',
			width : 100,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		}, {
			field : 'operateDt',
			title : '操作时间',
			align:'center',
			sortable:true,
			width : 160,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		}] ],
		toolbar : [ {
			text : '增加',
			iconCls : 'icon-add',
			handler : function() {
				append();
			}
		}, '-', {
			text : '修改',
			iconCls : 'icon-edit',
			handler : function() {
				edit();
			}
		}, '-', {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				removeRole();
			}
		}, '-', {
			text : '分配权限',
			iconCls : 'icon-permission',
			handler : function() {
				assignPermission();
			}
		}, '-', {
			text : '取消选中',
			iconCls : 'icon-undo',
			handler : function() {
				datagrid.datagrid('unselectAll');
			}
		} ]/* ,
		onRowContextMenu : function(e, rowIndex, rowData) {
			e.preventDefault();
			$(this).datagrid('unselectAll');
			$(this).datagrid('selectRow', rowIndex);
			$('#menu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		} */
	});
});

	function append() {
		easyuiDialog.dialog('open');
		easyuiForm.form('clear');
		easyuiForm.form('load', {
			isEnable : '1'
		});
	}

	function edit() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 1) {
			$.messager.show({
				msg : '只能择一个角色进行编辑！您已经选择了'
						+ rows.length + '个角色',
				title : '提示'
			});

		} else if (rows.length == 1) {
			easyuiDialog.dialog('open');
			easyuiForm.form('clear');
			console.log(rows[0].isEnable+"-------------");
			easyuiForm.form('load', {
				roleID : rows[0].roleID,
				roleCode : rows[0].roleCode,
				roleName : rows[0].roleName,
				comment : rows[0].comment,
				isEnable : rows[0].isEnable+""
			});
		}else{
			$.messager.alert('提示', '请选择要编辑的记录！', 'error');
		}
	   //parent.addTab('tabId_updateRole','修改角色','role/updateRolePage.do?roleID='+rows[0].roleID);
	}

	function removeRole() {
		var roleIds = [];
		var rows = datagrid.datagrid('getSelections');
		console.log(rows[0].roleID+"**********");
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
				if (r) {
					for ( var i = 0; i < rows.length; i++) {
						roleIds.push(rows[i].roleID);
					}
					$.ajax({
						url : 'role/deleteRole.do',
						data : {
							//roleIds : roleIds.join(',')
							roleIds : rows[0].roleID
						},
						cache : false,
						success : function(response) {
							datagrid.datagrid('unselectAll');
							datagrid.datagrid('reload');
							$.messager.show({
								title : '提示',
								msg : '删除成功！'
							});
						}
					});
				}
			});
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}
	
	
	
	
	//给角色分配权限
	function assignPermission() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 1) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].roleID);
			}
			$.messager.show({
				msg : '只能择一个角色进行编辑！您已经选择了【' + names.join(',') + '】'
						+ rows.length + '个角色',
				title : '提示'
			});
		}else if (rows.length == 1) {
			//首先加载tab页面 调用父页面的方法
			parent.addTab('tabId_addRolePerm','分配权限','role/addOrUpdateRolePermPage.do?roleID='+rows[0].roleID);
			parent.disableOtherTabs('分配权限');
		}
		else{
			$.messager.alert('提示', '请选择要编辑的记录！', 'error');
		}
	} 

</script>
</html>