<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<!-- 此页面用于弹出供应商列表选择对话框，选择供应商-->
<style>
.dialog_top{
	width:350px;
	padding:10px;
}
</style>
<script type="text/javascript">
	/*创建币种列表对话框*/
	var currencyDatagrid;
	var currencySearchBox;
	function openCurrencyDialog(obj, callBackFunc) {
		var currencyDialog = $('#currencyDialog').show().dialog({
			autoOpen : false,
			modal : false,
			bgiframe : false,
			position : [ 'center', 'top' ],
			width : 500,
			height : 600,
			draggable : true,
			resizable : false,
			onClose : function() {
				var rows = currencyDatagrid.datagrid("getSelections");
				callBackFunc(obj, rows);
				currencyDatagrid.datagrid('unselectAll');
			}
		});

		currencyDatagrid = $('#currencyDatagrid').datagrid({
			url : 'currency/queryCurrencyList',
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
				var ed = currencyDatagrid.datagrid('selectRow', index);
				currencyDialog.dialog("close");
			},
			columns : [ [ {
				title : 'id',
				field : 'id',
				width : 10,
				hidden : true
			}, {
				field : 'currencyCode',
				title : '币种代码',
				align : 'center',
				width : 20,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'currencyName',
				title : '币种名称',
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
		
		
		currencySearchBox = $('#currencySearch').searchbox({ 
			searcher : currencySearch, 
			menu:'#currencyMenu', 
			prompt:'请输入',
			height:26,
			width:300
			});
	}

	/*搜索框所有调用*/
	function currencySearch(value, name) {
		/* $.ajax({
			url : 'currencyManage/getVendorList4Dialog',
			data : {
				param_name : name,
				param_value : value
			},
			cache : false,
			success : function(response) {
				$('#currencyDatagrid').datagrid('loadData', response);
			}
		}); */
		if(name=="currencyCode"){
			currencyDatagrid.datagrid('load',
					{
						isSearch:"yes",
						currencyCode : value
					});
		}else{
			currencyDatagrid.datagrid('load',
					{
						isSearch:"yes",
						currencyName : value
					});
		}
		
	}
</script>
</head>
<body>
	<div id="currencyDialog" title="选择币种"
		style="display: none; overflow: hidden;">
		<div class="dialog_top">
				<input id="currencySearch"></input>
					
		</div>
		<div id="currencyMenu">
			<div data-options="name:'currencyCode'">币种代码</div>
			<div data-options="name:'currencyName'">币种名称</div>
		</div>
		<div style="height: 520px">
			<div id="currencyDatagrid"></div>
		</div>
	</div>
</body>
</html>