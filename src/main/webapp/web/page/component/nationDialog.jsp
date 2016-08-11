<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
.dialog_top{
	width:350px;
	padding:10px;
}
</style>
<script type="text/javascript">
	/*创建国家地区列表对话框*/
	var nationDatagrid;
	function openNationDialog(obj,url, callBackFunc) {
		var nationDialog = $('#nationDialog').show().dialog({
			autoOpen : false,
			modal : false,
			bgiframe : false,
			position : [ 'center', 'top' ],
			width : 500,
			height : 600,
			draggable : true,
			resizable : false,
			onClose : function() {
				var rows = nationDatagrid.datagrid("getSelections");
				callBackFunc(obj, rows);
				nationDatagrid.datagrid('unselectAll');
			}
		});

		nationDatagrid = $('#nationDatagrid').datagrid({
			url : url,
			title : '',
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 15, 20 ],
			fit : true,
			fitColumns : true,
			nowrap : true,
			border : true,
			idField : 'ID',
			sortOrder : 'asc',
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers : true,
			singleSelect : true,
			onDblClickRow : function(index, field, value) {
				var ed = nationDatagrid.datagrid('selectRow', index);
				nationDialog.dialog("close");
			},
			columns : [ [ {
				title : '国家ID',
				field : 'ID',
				width : 10,
				hidden : true
			}, {
				field : 'nationCode',
				title : '国家代码',
				align : 'center',
				width : 20,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'nationCNName',
				title : '国家中文名称',
				align : 'center',
				width : 50,
				sortable : true,
				order : 'asc',
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'nationENName',
				title : '国家英文名称',
				align : 'center',
				width : 50,
				sortable : true,
				order : 'asc',
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}] ]
		});
		
		nationSearchBox = $('#nationSearch').searchbox({ 
			searcher : nationSearch, 
			menu:'#nationMenu', 
			prompt:'请输入',
			height:26,
			width:300
			});
	}

	/*搜索框所有调用*/
	function nationSearch(value, name) {
		if(name=="nationCode"){
			nationDatagrid.datagrid('load',
					{
						isSearch:"yes",
						nationCode : value
					});
		}else if(name=="nationCNName"){
			nationDatagrid.datagrid('load',
					{
						isSearch:"yes",
						nationCNName : value
					});
		}else{
			nationDatagrid.datagrid('load',
					{
						isSearch:"yes",
						nationENName : value
					});
		}
	}
</script>
</head>
<body>
	<div id="nationDialog" title="选择国家地区"
		style="display: none; overflow: hidden;">
		<!-- <table>
			<tr>
				<td><label></label></td>
				<td><input id="vendorSearch" class="easyui-searchbox"
					data-options="searcher:vendorSearch,prompt:'请输入',menu:'#vendorMenu'"></input>
					</td>
			<tr>
		</table>
		<br> -->
		<div class="dialog_top">
				<input id="nationSearch"></input>
		</div>
		<div id="nationMenu" style="width: 120px">
						<div data-options="name:'nationCode'">国家代码</div>
						<div data-options="name:'nationCNName'">国家中文名称</div>
						<div data-options="name:'nationENName'">国家英文名称</div>
					</div>
		<div style="height: 520px">
			<div id="nationDatagrid"></div>
		</div>
	</div>
</body>
</html>