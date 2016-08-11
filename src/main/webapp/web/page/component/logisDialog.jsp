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
	/*创建境外物流信息对话框*/
	var logisDatagrid;
	function openLogisDialog(obj, url,callBackFunc) {
		var logisDialog = $('#logisDialog').show().dialog({
			autoOpen : false,
			modal : true,
			bgiframe : true,
			position : [ 'center', 'top' ],
			width : 500,
			height : 600,
			draggable : true,
			resizable : true,
			onClose : function() {
				var rows = $('#logisDatagrid').datagrid("getSelections");
				callBackFunc(obj, rows);
				logisDatagrid.datagrid('unselectAll');
			},
		});

		logisDatagrid = $('#logisDatagrid').datagrid({
			url : url,
			title : '',
			pagination : true,
			pageSize : 10,
			pageList : [ 10,15,20],
			fit : true,
			fitColumns : true,
			nowrap : true,
			border : true,
			idField : 'logiID',
			sortOrder : 'asc',
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers : true,
			singleSelect : true,
			onDblClickRow : function(index, field, value) {
				var ed = $('#logisDatagrid').datagrid('selectRow', index);
				logisDialog.dialog("close");
			},
			columns : [ [ {
				title : '物流ID',
				field : 'logiID',
				width : 10,
				hidden : true
			},{
				title : '收件人姓名',
				field : 'destName',
				width : 30
			},{
				title : '国家地区',
				field : 'destCountry',
				width : 30
			},{
				title : '物流单号',
				field : 'logiNo',
				width : 30
			}, {
				title : '物流公司',
				field : 'logiComCode',
				width : 50

			}] ]
		});
		
		
		logisSearchBox = $('#logisSearch').searchbox({ 
			searcher:logisSearch, 
			menu:'#logisMenu', 
			prompt:'请输入',
			height:26,
			width:300
			});
	}
	
	
	/*搜索*/
	function logisSearch(value,name){
		/* $.ajax({
			url : 'logiManage/getProduct4Dialog',
			data : {
				param_name:name,
				param_value:value
			},
			cache : false,
			success : function(response) {
				$('#logisDatagrid').datagrid('loadData',response);
			}
		}); */
		
		if(name=="logiNo"){
			logisDatagrid.datagrid('load',
					{
						isSearch:"yes",
						logiNo : value
					});
		}else if(name == 'logiComCode'){
			logisDatagrid.datagrid('load',
					{
						isSearch:"yes",
						logiComCode : value
					});
		}else if(name == 'destCountry'){
			logisDatagrid.datagrid('load',
					{
						isSearch:"yes",
						destCountry : value
					});
		}else{
			logisDatagrid.datagrid('load',
					{
						isSearch:"yes",
						destName : value
					});
		}
	}
</script>
</head>
<body>
	<div id="logisDialog" title="选择境外物流" style="display: none; overflow: hidden; height: 600px;">
		<!-- <table>
			<tr>
				<td><input id="logisSearch" class="easyui-searchbox"
					data-options="searcher:logisSearch,prompt:'请输入',menu:'#logisMenu'"></input>
					</td>
					<td>
					</td>
			<tr>
		</table> -->
		<div class="dialog_top">
			<!-- <input id="currencySearch" class="easyui-searchbox"
					data-options="searcher:currencySearch,prompt:'请输入',menu:'#currencyMenu'"></input> -->
				<input id="logisSearch" style="width: 120px"></input>
					
		</div>
		<div id="logisMenu" style="width: 120px">
						<div data-options="name:'logiNo'">物流单号</div>
						<div data-options="name:'logiComCode'">物流公司</div>
						<div data-options="name:'destName'">收件人姓名</div>
		</div>
		<div style="height: 520px">
		<div id="logisDatagrid"></div>
		</div>
	</div>
</body>
</html>