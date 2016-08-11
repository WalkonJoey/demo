<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<title>在线用户管理</title>

</head>
<body class="easyui-layout" fit="true" onkeydown="doSearch(event.keyCode,searchFun)">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar" class="datagrid-toolbar" style="height: auto;">
			<div> 
				<a class="easyui-linkbutton" iconCls="icon-remove" onclick="removeOnlineUser();"
					plain="true" href="javascript:void(0);">踢出在线用户</a>
			</div>
		</div>
		<table id="datagrid"></table>
	</div>

</body>

<script type="text/javascript" charset="UTF-8">
	var datagrid;
	$(function(){
		//创建easyui datagrid控件  并从后台获取数据绑定到上面
		datagrid = $('#datagrid').datagrid({
			url : 'user/getOnlinUsers',
			toolbar : '#toolbar',
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			pageList : [ 5, 10, 20, ],
			fit : true,
			fitColumns : false,
			rownumbers: true,
			nowrap : false,
			border : false,
			checkOnSelect : true,
			selectOnCheck : true,
			idField : 'loginID',
			columns : [ [ //每个列具体内容 field的内容要与传递过来的json数据中的Key值对应相等
			{
				field : 'loginID',
				title : '登录ID',
				width : 120,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'userCnName',
				title : '用户中文名',
				width : 120,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			},  {
				field : 'userEnName',
				title : '用户英文名',
				width : 120,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'gender',
				title : '性别',
				align:'center',
				width : 50,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				},
				formatter:function(value,rec){  
					if(value==1)
	                	return '男';  
					else 
						return '女';
	            }  
				
			},  {
				field : 'ipAddress',
				title : '登录IP地址',
				width : 160,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			},
			{
				field : 'departJob',
				title : '部门职务',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			},  {
				field : 'loginTime',
				title : '登陆时间',
				align:'center',
				width : 160,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, ] ]
		});

		});

	//删除用户
	function removeOnlineUser() {
		var rows = datagrid.datagrid('getSelected');
		if (rows) {
			$.messager.confirm('请确认', '您要将这些在线用户踢出？', function(r) {
				if (r) {
					$.ajax({
						url : 'user/deleteOnlineUser',
						data : {
							userID: rows.userID
						},
						cache : false,
						success : function(response) {
							datagrid.datagrid('unselectAll');
							datagrid.datagrid('reload');
							$.messager.show({
								title : '提示',
								msg : '踢出成功！'
							});
						}
					});
				}
			});
		} else {
			$.messager.alert('提示', '请选择要踢出的用户！', 'error');
		}
	}
</script>
</html>