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
	/*创建分类列表对话框*/
	var categoryDatagrid;
	var categorySearchBox;
	function openCategoryDialog(obj,url, callBackFunc) {
		var categoryDialog = $('#categoryDialog').show().dialog({
			autoOpen : false,
			modal : false,
			bgiframe : false,
			position : [ 'center', 'top' ],
			width : 500,
			height : 600,
			draggable : true,
			resizable : false,
			onClose : function() {
				var rows = $('#categoryDatagrid').datagrid("getSelections");
				callBackFunc(obj, rows);
				$('#categoryDatagrid').datagrid('unselectAll');
			}
		});

		categoryDatagrid= $('#categoryDatagrid').datagrid({
			url : url,
			title : '',
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 15, 20 ],
			fit : true,
			fitColumns : true,
			nowrap : true,
			border : true,
			sortOrder : 'asc',
			checkOnSelect : true,
			selectOnCheck : true,
			rownumbers : true,
			singleSelect : true,
			idField : 'categoryID',
			onDblClickRow : function(index, field, value) {
				var ed = $('#categoryDatagrid').datagrid('selectRow', index);
				categoryDialog.dialog("close");
			},
			columns : [ [ 
			   {
				field : 'categoryCode',
				title : '分类代码',
				align : 'center',
				width : 20,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				field : 'categoryCnName',
				title : '分类名称',
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
		
		
		categorySearchBox = $('#categorySearch').searchbox({ 
			searcher : categorySearch, 
			menu:'#categoryMenu', 
			prompt:'请输入',
			height:26,
			width:300
			});
	}

	/*搜索框所有调用*/
	function categorySearch(value, name) {
		/* $.ajax({
			url : 'productManage/getCategoryList4Dialog',
			data : {
				param_name : name,
				param_value : value
			},
			cache : false,
			success : function(response) {
				$('#categoryDatagrid').datagrid('loadData', response);
			}
		}); */
		categoryDatagrid.datagrid('load',
				{
					isSearch:"yes",
					param_name : name,
					param_value : value
				});
		/* if(name==="categoryCode"){
			categoryDatagrid.datagrid('load',
					{
						isSearch:"yes",
						categoryCode : value
					});
		}else{
			categoryDatagrid.datagrid('load',
					{
						isSearch:"yes",
						categoryCnName : value
					});
		} */
	}
</script>
</head>
<body>
	<div id="categoryDialog" title="选择类别" style="display: none; overflow: hidden;height: 600px;">
		<!-- <table>
			<tr>
				<td><input id="categorySearch" class="easyui-searchbox"
					data-options="searcher:categorySearch,prompt:'请输入',menu:'#categoryMenu'"></input>
					<div id="categoryMenu" style="width: 120px">
						<div data-options="name:'categoryCode'">分类编码</div>
						<div data-options="name:'categoryCnName'">分类名称</div>
					</div></td>
			<tr>
		</table> -->
		<div class="dialog_top">
				<input id="categorySearch"></input>
		</div>
		<div id="categoryMenu">
						<div data-options="name:'categoryCode'">分类编码</div>
						<div data-options="name:'categoryCnName'">分类名称</div>
					</div>
		<div style="height: 520px">
			<div id="categoryDatagrid"></div>
		</div>
	</div>
</body>
</html>