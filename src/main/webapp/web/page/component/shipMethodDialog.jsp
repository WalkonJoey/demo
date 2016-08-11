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
	/*创建运输方式列表对话框*/
	var shipMethodDatagrid;
	function openShipMethodDialog(obj,url, callBackFunc) {
		var shipMethodDialog = $('#shipMethodDialog').show().dialog({
			autoOpen : false,
			modal : false,
			bgiframe : false,
			position : [ 'center', 'top' ],
			width : 500,
			height : 600,
			draggable : true,
			resizable : false,
			onClose : function() {
				var rows = shipMethodDatagrid.datagrid("getSelections");
				callBackFunc(obj, rows);
				shipMethodDatagrid.datagrid('unselectAll');
			}
		});

		shipMethodDatagrid = $('#shipMethodDatagrid').datagrid({
			url : url,
			title : '',
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 15, 20 ],
			fit : true,
			fitColumns : true,
			nowrap : true,
			border : true,
			idField : 'id',
			sortOrder : 'asc',
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers : true,
			singleSelect : true,
			onDblClickRow : function(index, field, value) {
				var ed = shipMethodDatagrid.datagrid('selectRow', index);
				shipMethodDialog.dialog("close");
			},
			columns : [ [ {
				title : 'id',
				field : 'id',
				width : 10,
				hidden : true
			}, {
				field : 'shipCode',
				title : '运输代码',
				align : 'center',
				width : 20,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'shipCNName',
				title : '中文名称',
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
				field : 'shipENName',
				title : '英文名称',
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
		
		shipMethodSearchBox = $('#shipMethodSearch').searchbox({ 
			searcher : shipMethodSearch, 
			menu:'#shipMethodMenu', 
			prompt:'请输入',
			height:26,
			width:300
			});
	}

	/*搜索框所有调用*/
	function shipMethodSearch(value, name) {
		if(name=="shipCode"){
			shipMethodDatagrid.datagrid('load',
					{
						isSearch:"yes",
						shipCode : value
					});
		}else if(name=="shipCNName"){
			shipMethodDatagrid.datagrid('load',
					{
						isSearch:"yes",
						shipCNName : value
					});
		}else{
			shipMethodDatagrid.datagrid('load',
					{
						isSearch:"yes",
						shipENName : value
					});
		}
	}
</script>
</head>
<body>
	<div id="shipMethodDialog" title="选择运输方式"
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
				<input id="shipMethodSearch"></input>
		</div>
		<div id="shipMethodMenu" style="width: 120px">
						<div data-options="name:'shipCode'">运输代码</div>
						<div data-options="name:'shipCNName'">中文名称</div>
						<div data-options="name:'shipENName'">英文名称</div>
					</div>
		<div style="height: 520px">
			<div id="shipMethodDatagrid"></div>
		</div>
	</div>
</body>
</html>