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
	/*创建产品列表对话框*/
	var g_vendorID;
	var productDatagrid;
	function openProductDialog(obj,vendorID, callBackFunc) {
		g_vendorID=vendorID;
		var productDialog = $('#productDialog').show().dialog({
			autoOpen : false,
			modal : true,
			bgiframe : true,
			position : [ 'center', 'top' ],
			width : 500,
			height : 600,
			draggable : true,
			resizable : true,
			onClose : function() {
				var rows = $('#productDatagrid').datagrid("getSelections");
				callBackFunc(obj, rows);
				$('#productDatagrid').datagrid('unselectAll');
			},
		});

		productDatagrid = $('#productDatagrid').datagrid({
			url : 'productManage/getProduct4Dialog/'+vendorID,
			title : '',
			pagination : true,
			pageSize : 10,
			pageList : [ 10,15 ],
			fit : true,
			fitColumns : true,
			nowrap : true,
			border : true,
			idField : 'skuNo',
			sortOrder : 'asc',
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers : true,
			singleSelect : true,
			onDblClickRow : function(index, field, value) {
				var ed = productDatagrid.datagrid('selectRow', index);
				$('#productDialog').dialog("close");
			},
			columns : [ [ {
				title : '产品ID',
				field : 'productID',
				width : 10,
				hidden : true
			}, {
				title : '产品编号',
				field : 'skuNo',
				width : 30
			}, {
				title : '供方型号',
				field : 'vendorModel',
				width : 50

			}, {
				title : '产品中文名称',
				field : 'productName',
				width : 50
			} ] ]
		});
		
		productSearchBox = $('#productSearch').searchbox({ 
			searcher : productSearch, 
			menu:'#productMenu', 
			prompt:'请输入',
			height:26,
			width:300
			});
	}
	
	////用于其他-所有skuNo产品
	function openProduct4_Dialog(obj, callBackFunc) {
		var productDialog = $('#productDialog').show().dialog({
			autoOpen : false,
			modal : true,
			bgiframe : true,
			position : [ 'center', 'top' ],
			width : 500,
			height : 600,
			draggable : true,
			resizable : true,
			onClose : function() {
				var rows = $('#productDatagrid').datagrid("getSelections");
				callBackFunc(obj, rows);
				$('#productDatagrid').datagrid('unselectAll');
			},
		});
		
		productDatagrid = $('#productDatagrid').datagrid({
			url : 'productManage/getProduct4_Dialog',
			title : '',
			pagination : true,
			pageSize : 10,
			pageList : [ 10,15,20,25,30],
			fit : true,
			fitColumns : true,
			nowrap : true,
			border : true,
			idField : 'skuNo',
			sortOrder : 'asc',
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers : true,
			singleSelect : true,
			onDblClickRow : function(index, field, value) {
				var ed = productDatagrid.datagrid('selectRow', index);
				$('#productDialog').dialog("close");
			},
			columns : [ [ {
				title : '产品ID',
				field : 'productID',
				width : 10,
				hidden : true
			}, {
				title : '产品编号',
				field : 'skuNo',
				width : 30
			}, {
				title : '供方型号',
				field : 'vendorModel',
				width : 50

			}, {
				title : '产品中文名称',
				field : 'productName',
				width : 50
			} ] ]
		});
		
		productSearchBox = $('#productSearch').searchbox({ 
			searcher : productSearch, 
			menu:'#productMenu', 
			prompt:'请输入',
			height:26,
			width:300
			});
	}
	
	
	/*搜索*/
	function productSearch(value,name){
		/* $.ajax({
			url : 'productManage/getProduct4Dialog/'+g_vendorID,
			data : {
				param_name:name,
				param_value:value
			},
			cache : false,
			success : function(response) {
				$('#productDatagrid').datagrid('loadData',response);
			}
		}); */
		productDatagrid.datagrid('load',
				{
					isSearch:"yes",
					param_name :name,
					param_value:value
				});
	}
</script>
</head>
<body>
	<div id="productDialog" title="选择产品" style="display: none; overflow: hidden; height: 600px;">
		<!-- <table>
			<tr>
				<td><input id="productSearch" class="easyui-searchbox"
					data-options="searcher:productSearch,prompt:'请输入',menu:'#productMenu'"></input>
					<div id="productMenu" style="width: 120px">
						<div data-options="name:'skuNo'">skuNo</div>
						<div data-options="name:'productName'">产品名称</div>
					</div></td>
					<td>
					</td>
			<tr>
		</table> -->
		<div class="dialog_top">
				<input id="productSearch"></input>
		</div>
		<div id="productMenu">
			<div data-options="name:'skuNo'">skuNo</div>
			<div data-options="name:'productName'">产品名称</div>
		</div>
		<div style="height: 520px">
		<div id="productDatagrid"></div>
		</div>
	</div>
</body>
</html>