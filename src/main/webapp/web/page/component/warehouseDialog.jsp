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
	/*创建仓库列表对话框*/
	var warehouseDatagrid;
	function openWarehouseDialog(obj,url,callBackFunc) {
		var warehouseDialog = $('#warehouseDialog').show().dialog({
			autoOpen : false,
			modal : true,
			bgiframe : true,
			position : [ 'center', 'top' ],
			width : 500,
			height : 380,
			draggable : true,
			resizable : true,
			onClose : function() {
				var rows = $('#warehouseDatagrid').datagrid("getSelections");
				callBackFunc(obj, rows);
				$('#warehouseDatagrid').datagrid('unselectAll');
			},
		});

		warehouseDatagrid = $('#warehouseDatagrid').datagrid({
			url : url,
			title : '',
			pagination : true,
			pageSize : 10,
			pageList : [ 10,15 ],
			fit : true,
			fitColumns : true,
			nowrap : true,
			border : true,
			idField : 'whID',
			sortOrder : 'asc',
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers : true,
			singleSelect : true,
			onDblClickRow : function(index, field, value) {
				var ed = warehouseDatagrid.datagrid('selectRow', index);
				$('#warehouseDialog').dialog("close");
			},
			columns : [ [ {
				title : '仓库ID',
				field : 'whID',
				width : 10,
				hidden : true
			}, {
				title : '仓库编号',
				field : 'whCode',
				width : 30
			}, {
				title : '仓库名称',
				field : 'whName',
				width : 50

			}, {
				title : '地址',
				field : 'whAddress',
				width : 50
			} ] ]
		});
		
		warehouseSearchBox = $('#warehouseSearch').searchbox({ 
			searcher : warehouseSearch, 
			menu:'#warehouseMenu', 
			prompt:'请输入',
			height:26,
			width:300
			});
	}
	
	
	/*搜索*/
	function warehouseSearch(value,name){
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
		warehouseDatagrid.datagrid('load',
				{
					isSearch:"yes",
					param_name :name,
					param_value:value
				});
	}
</script>
</head>
<body>
	<div id="warehouseDialog" title="选择仓库" style="display: none; overflow: hidden; height: 600px;">
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
				<input id="warehouseSearch"></input>
		</div>
		<div id="warehouseMenu">
			<div data-options="name:'whCode'">仓库编码</div>
		</div>
		<div style="height: 520px">
		<div id="warehouseDatagrid"></div>
		</div>
	</div>
</body>
</html>