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
	/*创建供应商列表对话框*/
	var vendorDatagrid;
	function openVendorDialog(obj,url, callBackFunc) {
		var vendorDialog = $('#vendorDialog').show().dialog({
			autoOpen : false,
			modal : false,
			bgiframe : false,
			position : [ 'center', 'top' ],
			width : 500,
			height : 600,
			draggable : true,
			resizable : false,
			onClose : function() {
				var rows = vendorDatagrid.datagrid("getSelections");
				callBackFunc(obj, rows);
				vendorDatagrid.datagrid('unselectAll');
			}
		});

		vendorDatagrid = $('#vendorDatagrid').datagrid({
			url : url,
			title : '',
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 15, 20 ],
			fit : true,
			fitColumns : true,
			nowrap : true,
			border : true,
			idField : 'vendorID',
			sortOrder : 'asc',
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers : true,
			singleSelect : true,
			onDblClickRow : function(index, field, value) {
				var ed = vendorDatagrid.datagrid('selectRow', index);
				vendorDialog.dialog("close");
			},
			columns : [ [ {
				title : '供应商ID',
				field : 'vendorID',
				width : 10,
				hidden : true
			}, {
				field : 'vendorCode',
				title : '供应商代码',
				align : 'center',
				width : 20,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'vendorName',
				title : '供应商名称',
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
			} ] ]
		});
		
		vendorSearchBox = $('#vendorSearch').searchbox({ 
			searcher : vendorSearch, 
			menu:'#vendorMenu', 
			prompt:'请输入',
			height:26,
			width:300
			});
	}

	/*搜索框所有调用*/
	function vendorSearch(value, name) {
		if(name==="vendorCode"){
			vendorDatagrid.datagrid('load',
					{
						isSearch:"yes",
						vendorCode : value
					});
		}else{
			vendorDatagrid.datagrid('load',
					{
						isSearch:"yes",
						vendorName : value
					});
		}
	}
</script>
</head>
<body>
	<div id="vendorDialog" title="选择供应商"
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
				<input id="vendorSearch"></input>
		</div>
		<div id="vendorMenu" style="width: 120px">
						<div data-options="name:'vendorCode'">供应商代码</div>
						<div data-options="name:'vendorName'">供应商名称</div>
					</div>
		<div style="height: 520px">
			<div id="vendorDatagrid"></div>
		</div>
	</div>
</body>
</html>