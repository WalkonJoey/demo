<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<jsp:include page="../include/meta.jsp" />
<jsp:include page="../include/easyui.jsp" />
<!DOCTYPE html">
<html>
<head>
<title>用户管理</title>
<script type="text/javascript">
var datagrid;
var passwordInput;
var roleTree;
var userRoleDialog;
var userRoleForm;
var jsonify = function(obj){
    var seen = [];
    var json = JSON.stringify(obj, function(key, value){
        if (typeof value === 'object') {
            if ( !seen.indexOf(value) ) {
                return '__cycle__' + (typeof value) + '[' + key + ']'; 
            }
            seen.push(value);
        }
        return value;
    }, 4);
	return json;
};
	$(function(){
		userRoleForm = $('#userRoleForm').form();
		//分配角色对话框
		userRoleDialog = $('#userRoleDialog').show().dialog({
			modal : true,
			title : '角色信息',
			buttons : [ {
				text : '确定',
				handler : function() {
					/*var nodes = $('#roleTree').tree('getChecked');
					var targetUser=$('#targetUserID').val();
					console.log(nodes);
					$.post("role/updateUserRole",{userID:targetUser,choosedRoles:JSON.stringify(nodes)},function(data){
						userRoleDialog.dialog('close');
						$.messager.show({
							msg : '分配角色成功！',
							title : '提示'
						});
					},"json");*/
					var nodes = $('#roleTree').tree('getChecked');
					var targetUser=$('#targetUserID').val();
					console.log(nodes);
					var jsonArr = new Array();
					for(var i = 0;i<nodes.length;i++){
						var jsonNode = {"id":nodes[i].id}
						jsonArr.push(jsonNode);
					}
					for(var i = 0;i<jsonArr.length;i++){
						console.log(jsonArr[i].id);
					}
					$.post("role/updateUserRole",{userID:targetUser,choosedRoles:JSON.stringify(jsonArr)},function(data){
						userRoleDialog.dialog('close');
						$.messager.show({
							msg : '分配角色成功！',
							title : '提示'
						});
					},"json");
				}
			} ]
		}).dialog('close');/*end  userRoleDialog */
	//创建easyui datagrid控件  并从后台获取数据绑定到上面
	datagrid = $('#datagrid').datagrid({
		url : 'user/getUsersForDatagridPagination',
		toolbar : '#toolbar',
		iconCls : 'icon-save',
		pagination : true,
		pageSize : 15,
		pageList : [ 10, 15, 20,30 ],
		fit : true,
		fitColumns : false,
		nowrap : true,
		border : false,
		checkOnSelect : false, 
		selectOnCheck : false, 
		rownumbers: true,
		singleSelect:true,
		autoRowHeight:false,
		rowStyler: function(index,row){
				return 'height:30px'; // return inline style
		},
		idField : 'loginID',
		columns : [ [ //每个列具体内容 field的内容要与传递过来的json数据中的Key值对应相等
		{
			field : 'loginID',
			title : '登录ID',
			width : 100,
			checkbox : true
		}, {
			field : 'userCnName',
			title : '用户中文名',
			sortable:true,
			width : 120
		},  {
			field : 'userEnName',
			sortable:true,
			title : '用户英文名',
			width : 120
		}, {
			field : 'gender',
			title : '性别',
			align:'center',
			width : 40,
			formatter:function(value,rec){  
				if(value==1)
                	return '男';  
				else 
					return '女';
            }  
			
		},
		/*{
			field : 'age',
			title : '年龄',
			align:'center',
			width : 40,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		},*/ {
			field : 'departmentName',
			title : '所属部门',
			align:'center',
			width : 100
		}, {
			field : 'departJob',
			title : '部门职位',
			align:'center',
			width : 100
		},{
			field : 'telephone',
			title : '电话',
			width : 100
		},
		 {
			field : 'isEnable',
			title : '是否启用',
			align:'center',
			width : 60,
			formatter:function(value,rec){  
				if(value==1)
                	return '是';  
				else 
					return '否';
            }  
		}, {
			field : 'loginTime',
			title : '上次登陆时间',
			align:'center',
			width : 150
		}, {
			field : 'createDt',
			title : '创建时间',
			align:'center',
			width : 150
		}, {
			field : 'operateDt',
			title : '最后更新时间',
			sortable:true,
			align:'center',
			width : 150
		} ] ]
	});/* end  userDataGrid */

	$("input[name='departmentID']").combobox({
		url:'user/getDepartmentList.do',
		valueField: 'id',
	    textField: 'text',
		width:190,
		height:30, 
		required:true,
		panelHeight : 100,
		onBeforeLoad:function(param){
			
		}
	});/* End department combobox */
});/* End  $(function(){}) */
//增加用户
function append() {
	parent.addTab('tabId_addUser','增加用户','user/addOrUpdateUserPg.do');
}

//编辑用户
function edit() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 1) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].roleID);
			}
			$.messager.show({
				msg : '只能择一个用户进行编辑！您已经选择了【' + names.join(',') + '】'
						+ rows.length + '个用户',
				title : '提示'
			});
		}else if (rows.length == 1) {
			//首先加载tab页面 调用父页面的方法
			parent.addTab('tabId_updateUser','修改用户','user/addOrUpdateUserPg.do?userID='+rows[0].userID);
		}
		else{
			$.messager.alert('提示', '请选择要编辑的记录！', 'error');
		}
}


//编辑一个用户的角色
function editRoleTree() {
	var rows = datagrid.datagrid('getSelections');
	if (rows.length > 1) {
		var names = [];
		for ( var i = 0; i < rows.length; i++) {
			names.push(rows[i].loginID);
		}
		$.messager.show({
			msg : '只能择一个用户进行编辑！您已经选择了【' + names.join(',') + '】'
					+ rows.length + '个用户',
			title : '提示'
		});
	} 
	else if (rows.length == 1) {
		$('#roleTree').tree({
			url:'user/getRolesTree?userID='+rows[0].userID,
			method:'post',
			animate:true,
			checkbox:true
		});
		userRoleDialog.dialog('open');
		userRoleForm.form('clear');
		$.post("role/getSpecialUserRoles",{userID:rows[0].userID},
				  function(data){
					roleNames = data;
					userRoleForm.form('load', {//加载数据到form框中 这里的key值对应 input的name属性
						userID : rows[0].userID,
						userName : rows[0].userCnName,
						roleNames : roleNames
					});
				  },
				  "text");
	}else{
		$.messager.alert('提示', '请选择要编辑的记录！', 'error');
	}
} 
//批量编辑角色
function editRoles() {
	var ids = [];
	var rows = datagrid.datagrid('getSelections');
	if (rows.length > 0) {
		for ( var i = 0; i < rows.length; i++) {
			ids.push(rows[i].id);
		}
		userRoleForm.find('input[name=userIds]').val(ids.join(','));
		userRoleDialog.dialog('open');
	} else {
		$.messager.alert('提示', '请选择要编辑的记录！', 'error');
	}
} 

//删除用户
function removeUser() {
	
	var userIDs = [];
	var rows = datagrid.datagrid('getSelections');
	if (rows.length > 0) {
		parent.$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
			if (r) {
				for ( var i = 0; i < rows.length; i++) {
					userIDs.push(rows[i].userID);
				}
				$.ajax({
					url : 'user/deleteUser',
					data : {
						userIDs : userIDs.join(',')
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
		parent.$.messager.alert('提示', '请选择要删除的记录！', 'warning');
	}
}
//查找
function searchFun() {
	datagrid.datagrid('load',
	{
		loginID : $('.search_top input[name=loginID]').val(),
		userCnName : $('.search_top input[name=userCnName]').val(),
		isEnable : $('#isEnable').val(),	
		departmentID : $('.search_top input[name=departmentID]').val(),
		departJob : $('.search_top input[name=departJob]').val()/* ,
		operateDt : $('#toolbar input[comboname=operateDt]')
						.datetimebox('getValue') */
	});
}
function clearFun() {
	$('.search_top input').val('');
	$('#isEnable').val(-1);
	datagrid.datagrid('load', {});
} 

</script>
</head>
<body class="easyui-layout" fit="true" onkeydown="doSearch(event.keyCode,searchFun)">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar" class="datagrid-toolbar" style="height: auto;">
			<div class="search_top">
			<div class="s_top_content">
				<table class="search_table">
					<tr>
						<th>登录名:</th>
						<td><input name="loginID" /></td>
						<th>用户中文名:</th>
						<td><input name="userCnName" /></td>
						<th class="advanced_element">启用状态:</th>
						<td class="advanced_element">
							<select  name="isEnable" id="isEnable" >
								<option value="-1">所有</option>
								<option value="1">启用</option>
								<option value="0">未启用</option>	
							</select>
						</td>
						<td>
							<div class="advanced_btn_wap">
								<a class="easyui-linkbutton confirmButton" iconCls="icon-search"
									plain="false" onclick="searchFun();" href="javascript:void(0);">查找</a>
								<a class="easyui-linkbutton" iconCls="icon-sdelete" plain="false" onclick="clearFun();"
										href="javascript:void(0);">清空</a>
								<a class="easyui-linkbutton" plain="false"
									onclick="switchAdvanced($('#datagrid'));" href="javascript:void(0);">切换到高级搜索</a>
								</div>
						</td>
					</tr>
					<tr class="advanced_element">
					<th>职位:</th>
						<td><input name="departJob"/></td>
					<th>所属部门:</th>
						<td><input name="departmentID"/></td>
					<th></th>
					<td>
							<div class="common_search_wap">
								<a class="easyui-linkbutton confirmButton" iconCls="icon-search"
									plain="false" onclick="searchFun();" href="javascript:void(0);">查找</a>
								<a class="easyui-linkbutton" iconCls="icon-sdelete" plain="false" onclick="clearFun();"
										href="javascript:void(0);">清空</a>
								<a class="easyui-linkbutton"  plain="false"
									onclick="switchCommon($('#datagrid'));" href="javascript:void(0);">切换到普通搜索</a>
							</div>
					</td>
					</tr>
				</table>
			</div>
			<!-- s_top_content -->
			</div>
			<!-- search top -->
			<div> 
				<a class="easyui-linkbutton" iconCls="icon-add" onclick="append();"
					plain="true" href="javascript:void(0);">增加</a> <a
					class="easyui-linkbutton" iconCls="icon-remove"
					onclick="removeUser();" plain="true" href="javascript:void(0);">删除</a>
				<a class="easyui-linkbutton" iconCls="icon-edit" onclick="edit();"
					plain="true" href="javascript:void(0);">编辑</a><a
					class="easyui-linkbutton" iconCls="icon-role" onclick="editRoleTree();"
					plain="true" href="javascript:void(0);">分配角色</a><a
					class="easyui-linkbutton" iconCls="icon-undo"
					onclick="datagrid.datagrid('unselectAll');" plain="true"
					href="javascript:void(0);">取消选中</a>
			</div>
		</div>
		<table id="datagrid"></table>
	</div>

<div id="userRoleDialog" style="display: none; overflow: hidden;height:400px;width:350px;">
		<form id="userRoleForm" method="post">
			<table class="tableForm" style="height:50px;width:350px;">
				<tr>
				<th style="text-align:right;">用户名:</th>
				<td>
				<input id="targetUserID" type="hidden" name="userID" />
				<input type="hidden" name="roleNames" />
				<input type="text" name="userName" disabled= "true"/>
				</td>
				</tr>
			</table>
			<ul id="roleTree" ></ul>
		</form>
	</div>
	
</body>

</html>