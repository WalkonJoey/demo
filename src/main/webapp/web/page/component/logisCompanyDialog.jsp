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
	/*创建快递物流信息对话框*/
	var logisCompanyDatagrid;
	function openLogisCompanyDialog(obj, url,callBackFunc) {
		var logisCompanyDialog = $('#logisCompanyDialog').show().dialog({
			autoOpen : false,
			modal : true,
			bgiframe : true,
			position : [ 'center', 'top' ],
			width : 500,
			height : 600,
			draggable : true,
			resizable : true,
			onClose : function() {
				var rows = $('#logisCompanyDatagrid').datagrid("getSelections");
				callBackFunc(obj, rows);
			},
		});

		logisCompanyDatagrid = $('#logisCompanyDatagrid').datagrid({
			url : url,
			title : '',
			pagination : true,
			pageSize : 10,
			pageList : [ 10,20],
			fit : true,
			fitColumns : true,
			nowrap : true,
			border : true,
			idField : 'companyID',
			sortOrder : 'asc',
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers : true,
			singleSelect : true,
			onDblClickRow : function(index, field, value) {
				var ed = $('#logisCompanyDatagrid').datagrid('selectRow', index);
				$('#logisCompanyDialog').dialog("close");
			},
			columns : [ [ {
				title : '物流ID',
				field : 'companyID',
				width : 10,
				hidden : true
			}, {
				title : '快递公司代码',
				field : 'companyCode',
				width : 30
			}, {
				title : '快递公司名称',
				field : 'companyCNName',
				width : 50

			}] ]
		});
		
		
		logisCompanySearchBox = $('#logisCompanySearch').searchbox({ 
			searcher:logisCompanySearch, 
			menu:'#logisCompanyMenu', 
			prompt:'请输入',
			height:26,
			width:300
			});
	}
	
	
	/*搜索*/
	function logisCompanySearch(value,name){
		logisCompanyDatagrid.datagrid('load',
				{
					isSearch:"yes",
					param_name :name,
					param_value:value
				});
	}
</script>
</head>
<body>
	<div id="logisCompanyDialog" title="选择快递物流" style="display: none; overflow: hidden; height: 600px;">
		<div class="dialog_top">
				<input id="logisCompanySearch" style="width: 120px"></input>
					
		</div>
		<div id="logisCompanyMenu" style="width: 120px">
						<div data-options="name:'companyCode'">公司代码</div>
						<div data-options="name:'companyCNName'">公司名称</div>
		</div>
		<div style="height: 520px">
		<div id="logisCompanyDatagrid"></div>
		</div>
	</div>
</body>
</html>