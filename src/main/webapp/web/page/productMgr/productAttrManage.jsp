<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.wap{
	width:100%;
	height:100%;
}

#toolbar{
	
	width:100%;
	border:1px solid #ccc;
	/* padding:5px 5px 5px 10px; */
}
.datagrid_wap{
	width:100%;
	height:650px;
	/* margin:0 1% 1% 1%; */

}
.middle-img{position:absolute;left:26.5%;top:35%;font-size:25px;color:#ccc;}
.datagrid_content{
	border: 1px solid #ccc;
     float: left;
    height: 560px;
    overflow-x: hidden;
    overflow-y: auto;
    width: 450px;
}
.dg_click_detail{

    box-sizing: border-box;
    height: 562px;
    margin-left: 450px;
    overflow-y: auto;
    padding: 5px;
    position: relative;
}
.dg_click_detail h3{
	 border: 1px solid #ccc;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
    height: 19px;
    line-height: 18px;
    margin: 0;
    text-indent: 1em;
	
}
.dg_row_comment{
	border: 1px solid #ccc;
    border-bottom-left-radius: 3px;
    border-bottom-right-radius: 3px;
    height: 35px;
    padding: 5px;
}
.attr_list{
	height:298px;
	border:1px solid #ccc;
	border-bottom-left-radius:3px;
	border-bottom-right-radius:3px;
}
.attr_val_dg_wap{
	width:450px;
	height:431px;
	float:left;
	border:1px solid #ccc;
}
.attr_val_comment{
 	border: 1px solid #ccc;
    box-sizing: border-box;
    height: 373px;
    margin-left: 452px;
    padding: 20px 30px;
    text-indent: 1em;
}

/* easyui的CSS样式 */
#toolbar{
	margin-bottom:10px;
}



/* easyuiDialog的CSS样式 */
.dia_attr_list{
	width: 98%;

	border-radius:3px;
	margin:5px;
	height: 218px;
	margin:20px auto;
}

.easyui_form .tableForm tr {
	width: 300px;
	height: 30px;
	padding: 2px;
}

.easyui_form .tableForm tr th {
	width: 80px;
	text-align: right;
	font-size: 14px;
}

.easyui_form .tableForm tr td {
	width: 200px;
}

.easyui_form .tableForm tr td input {
	width: 180px;
	height: 22px;
}

.easyui_form .tableForm tr td input[type="radio"] {
	width: 30px;
	height: 14px;
}

.tableForm textarea {
	height: 64px;
	width: 478px;
}
#attrValDialog{
}
.dg_arrow{position:absolute;}
</style>
<script type="text/javascript" charset="UTF-8">
var datagrid;
var easyuiDialog;
var easyuiForm;
var attrValForm;
var attrValDg;//attrValueDatagrid

$(function(){
	
	easyuiForm = $('#easyuiForm').form();
	attrValForm =$('#attrValForm').form();
	easyuiDialog = $('#easyuiDialog').show().dialog({
		modal : true,
		title : '属性名称信息',
		height : 360,
		width : 450,
		buttons : [ {
			text : '确定',
			handler : function() {
				var idVal = easyuiForm.find('input[name=attrID]').val();
				if (idVal != '-1'&&''!=idVal) {
					easyuiForm.form('submit', {
						url : 'productAttrManage/updatePdtAttrName.do',
						success : function(data) {
							easyuiDialog.dialog('close');
							$.messager.show({
								msg : '属性名编辑成功！',
								title : '提示'
							});
							datagrid.datagrid('reload');
						}
					});
				} else {
					easyuiForm.form('submit', {
						url : 'productAttrManage/addPdtAttrName.do',
						success : function(data) {
							try {
								var d = $.parseJSON(data);
								if (d) {
									easyuiDialog.dialog('close');
									$.messager.show({
										msg : '属性名创建成功！',
										title : '提示'
									});
									datagrid.datagrid('reload');
								}
							} catch (e) {
								$.messager.show({
									msg : '属性名称已存在！',
									title : '提示'
								});
							}
						}
					});
				}
			}
		} ]
	}).dialog('close');
	//属性值编辑dialog
	attrValDialog = $('#attrValDialog').show().dialog({
		modal : true,
		title : '属性值信息',
		height : 360,
		width : 450,
		buttons : [ {
			text : '确定',
			handler : function() {
				var idVal = attrValForm.find('[name=attrValueID]').val();
				if (idVal != '-1'&&idVal!='') {
					attrValForm.form('submit', {
						url : 'productAttrManage/updatePdtAttrValue.do',
						success : function(data) {
							attrValDialog.dialog('close');
							$.messager.show({
								msg : '属性值编辑成功！',
								title : '提示'
							});
							attrValDg.datagrid('reload');
						}
					});
				} else {
					attrValForm.form('submit', {
						url : 'productAttrManage/addPdtAttrValue.do',
						success : function(data) {
							try {
								var d = $.parseJSON(data);
								if (d) {
									attrValDialog.dialog('close');
									$.messager.show({
										msg : '属性值创建成功！',
										title : '提示'
									});
									attrValDg.datagrid('reload');
								}
							} catch (e) {
								$.messager.show({
									msg : '属性值已经存在！',
									title : '提示'
								});
							}
						}
					});
				}
			}
		} ]
	}).dialog('close');

	
	datagrid = $('#datagrid').datagrid({
		url : 'productAttrManage/findPdtAttrNames4dg.do',
		title : '',
		iconCls : 'icon-save',
		pagination : true,
		pageSize : 15,
		pageList : [ 15, 20, 30, 40 ],
		fit : true,
		fitColumns : false,
		nowrap : false,
		border : false,
		idField : 'attrID',
		sortName : 'attrID',
		sortOrder : 'asc',
/* 		checkOnSelect : true,
		selectOnCheck : true, */
		singleSelect:true,
		rownumbers:true,
		frozenColumns : [ [ {
			title : '属性ID',
			field : 'attrID',
			width : 100,
			hidden:true
/* 			checkbox:true */
		},{
			title : '属性名称',
			field : 'attrName',
			align:"center",
			sortable : true,
			width : 210
		}, 
		{
			title : '属性码',
			field : 'attrCode',
			align:"center",
			width : 210
			
		} ] ],
		columns : [ [ {
			title : '备注',
			field : 'comment',
			width : 280,
			hidden:true
		}] ],
		onSelect:function(rowIndex,rowData){
			if(rowData.comment){
				$(".dg_row_comment").text(rowData.comment);
			}
			var options = attrValDg.datagrid("options");
			options.url = "productAttrManage/getPdtAttrValByNId4dg.do";
			attrValDg.datagrid("reload",{
				isSearch : "yes",
				attrID : rowData.attrID
			});
		}
	});
	
	attrValDg = $('#attrValueDg').datagrid({
		url : null,
		iconCls : 'icon-save',
		pagination : true,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40 ],
		fit : true,
		fitColumns : false,
		nowrap : false,
		border : false,
		idField : 'attrValueID',
		sortName : 'attrValueID',
		sortOrder : 'asc',
		singleSelect:true,
		rownumbers:true,
		frozenColumns : [ [ {
			title : '属性值ID',
			field : 'attrValueID',
			width : 100,
			hidden:true
/* 			checkbox:true */
		},{
			title : '属性ID',
			field : 'attrID',
			hidden:true,
			width : 120
		}, 
		{
			title : '属性值',
			field : 'valueName',
			align:"center",
			width : 210
			
		},{
			title : '属性值码',
			field : 'valueCode',
			align:"center",
			width : 210
			
		} ] ],
		columns : [ [ {
			title : '备注',
			field : 'comment',
			width : 280,
			hidden:true
		}] ],
		onSelect:function(rowIndex,rowData){
			if(rowData.comment){
				$(".attr_val_comment_content").text(rowData.comment);
			}
		}
	});
});



	function appendAttr() {
		easyuiDialog.dialog('open');
		easyuiForm.form('clear');
		easyuiForm.form('load', {//加载数据到form框中 这里的key值对应 input的name属性
			attrID : "-1"
		});
	}

	function editAttr() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 1) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].roleID);
			}
			$.messager.show({
				msg : '只能择一个产品进行编辑！您已经选择了【' + names.join(',') + '】'
						+ rows.length + '个产品',
				title : '提示',
			});
		}else if (rows.length == 1) {
			easyuiDialog.dialog('open');
			easyuiForm.form('clear');
			easyuiForm.form('load', {//加载数据到form框中 这里的key值对应 input的name属性
				attrID : rows[0].attrID,
				attrName : rows[0].attrName,
				attrCode : rows[0].attrCode,
				comment : rows[0].comment
			});
		}
		else{
			$.messager.alert('提示', '请选择要编辑的记录！', 'error');
		}
	}

	function removeAttr() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
				if (r) {
					$.ajax({
						url : 'productAttrManage/deleteAttr.do',
						data : {
							attrID : rows[0].attrID
						},
						cache : false,
						success : function(response) {
							datagrid.datagrid('unselectAll');
							datagrid.datagrid('reload');
							if(response.success){
								$.messager.show({
									title : '提示',
									msg : '删除成功！'
								});
								attrValDg.datagrid('reload');
							}else{
								$.messager.alert('提示', response.meg, 'error');
							}
						}
					});
				}
			});
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}
	
	function appendAttrVal() {
		var rows = datagrid.datagrid('getSelections');
		if (rows.length > 0) {
			attrValDialog.dialog('open');
			attrValForm.form('clear');
			attrValForm.form('load', {//加载数据到form框中 这里的key值对应 input的name属性
				attrValueID : "-1",
				attrID: rows[0].attrID
			});
			
		} else {
			$.messager.alert('提示', '请选择对应属性在，再增加值！', 'warning');
		}
	}

	function editAttrVal() {
		var rows = attrValDg.datagrid('getSelections');
		if (rows.length > 1) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].roleID);
			}
			$.messager.show({
				msg : '只能择一个产品进行编辑！您已经选择了【' + names.join(',') + '】'
						+ rows.length + '个产品',
				title : '提示',
			});
		}else if (rows.length == 1) {
			attrValDialog.dialog('open');
			attrValForm.form('clear');
			attrValForm.form('load', {//加载数据到form框中 这里的key值对应 input的name属性
				attrValueID : rows[0].attrValueID,
				attrID: rows[0].attrID,
				valueName : rows[0].valueName,
				valueCode : rows[0].valueCode,
				comment : rows[0].comment
			});
			//首先加载tab页面 调用父页面的方法
			//parent.addTab('tabId_updatePdtAttrName','修改产品属性','productAttrManage/addOrUpdtPdtAttrNamePg.do?attrID='+rows[0].attrID);
		}
		else{
			$.messager.alert('提示', '请选择要编辑的记录！', 'error');
		}
	}

	function removeAttrVal() {
		var rows = attrValDg.datagrid('getSelections');
		if (rows.length > 0) {
			$.messager.confirm('请确认', '您要删除当前所选项目？', function(r) {
				if (r) {
					$.ajax({
						url : 'productAttrManage/deleteAttrValue.do',
						data : {
							attrID : rows[0].attrID,
							attrValueID : rows[0].attrValueID
						},
						cache : false,
						success : function(response) {
							attrValDg.datagrid('unselectAll');
							attrValDg.datagrid('reload');
							$.messager.show({
								title : '提示',
								msg : '删除成功！'
							});
						}
					});
				}
			});
		} else {
			$.messager.alert('提示', '请选择要删除的记录！', 'error');
		}
	}
	function searchFun(){
		datagrid.datagrid('load',
			{
				isSearch:"yes",
				attrName : $('.search_table input[name=attrName]').val(),
				attrCode : $('.search_table input[name=attrCode]').val()
			});
	}
</script>
</head>
<!-- <body class="bodymargin">
<div class="wap"> -->
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar" class="datagrid-toolbar">
			<div class="search_top">
				<div class="s_top_content">
					<table class="search_table">
						<tr>
							<th>属性名:</th>
							<td><input name="attrName" /></td>
							<th>属性码:</th>
							<td>
								<input name="attrCode"/>
							</td>
							<td><div class="advanced_btn_wap">
									<a class="easyui-linkbutton confirmButton"
										iconCls="icon-search" plain="false" onclick="searchFun();"
										href="javascript:void(0);">查找</a> <a class="easyui-linkbutton"
										iconCls="icon-sdelete" plain="false" onclick="clearFun($('#datagrid'));"
										href="javascript:void(0);">清空</a>
								</div></td>
						</tr>
					</table>
				</div><!-- s_top_content -->
			</div><!-- search_top -->
			<div>
				<span style="text-indent:3px;">属性操作：</span>
				<a class="easyui-linkbutton" iconCls="icon-add" onclick="appendAttr();"
					plain="true" href="javascript:void(0);">新增属性</a> <a
					class="easyui-linkbutton" iconCls="icon-remove"
					onclick="removeAttr();" plain="true" href="javascript:void(0);">删除</a>
				<a class="easyui-linkbutton" iconCls="icon-edit" onclick="editAttr();"
					plain="true" href="javascript:void(0);">编辑</a>
				<a class="easyui-linkbutton" iconCls="icon-undo"
					onclick="datagrid.datagrid('unselectAll');" plain="true"
					href="javascript:void(0);">取消选中</a>
			</div>
		</div>
		<div class="middle-img"><i class="fa fa-arrow-right"></i></div>
		<div class="datagrid_wap">
			<div class="datagrid_content">
				<table id="datagrid"></table>
			</div>
			<div class="dg_click_detail">
				<div style="margin-bottom:5px;">
					<h3>备注：</h3>
					<div class="dg_row_comment"></div>
				</div>
				<h3>可选属性值：</h3>
				<div class="attr_list">
					<div id="valToolbar" class="datagrid-toolbar">
						<div>
							<span style="text-indent:3px;">属性值操作：</span>
							<a class="easyui-linkbutton" iconCls="icon-add" onclick="appendAttrVal();"
								plain="true" href="javascript:void(0);">新增</a> <a
								class="easyui-linkbutton" iconCls="icon-remove"
								onclick="removeAttrVal();" plain="true" href="javascript:void(0);">删除</a>
							<a class="easyui-linkbutton" iconCls="icon-edit" onclick="editAttrVal();"
								plain="true" href="javascript:void(0);">编辑</a>
							<a class="easyui-linkbutton" iconCls="icon-undo"
								onclick="attrValDg.datagrid('unselectAll');" plain="true"
								href="javascript:void(0);">取消选中</a>
						</div>
					</div>
					<div class="attr_val_dg_wap">
						<table id="attrValueDg"></table>
					</div>
					<div class="attr_val_comment">
						<h4>属性值备注：</h4>
						<div class="attr_val_comment_content"></div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
	<div class="dg_arrow"></div>
	<div id="easyuiDialog">
		<div class="dia_attr_list">
				<div class="attr_list_title">
					<h2 class="title">填写产品信息：</h2>
				</div>
				<form id="easyuiForm" method="post" class="easyui_form">
					<input name="attrID" type="hidden" value="-1"/>
					<table class="dlog_tb_form" cellspacing="10">
						<tr>
							<th>属性名称:</th>
							<td><input name="attrName" class="i_input easyui-validatebox"
								required="true"/>
							</td>
							
						</tr>
						<tr>
							<th>属性代码:</th>
							<td><input name="attrCode" class="i_input easyui-validatebox"
								required="true"/>
							</td>
						</tr>
						<tr>
							<th>备注:</th>
							<td colspan="4">
							<textarea name="comment">
							</textarea></td>
						</tr>

					</table>
				</form>
		</div>
	</div>
	<div id="attrValDialog">
		<div class="dia_attr_list">
				<div class="attr_list_title">
					<h2 class="title">填写产品信息：</h2>
				</div>
				<form id="attrValForm" method="post" class="easyui_form">
					<input name="attrID" type="hidden" value="-1"/>
					<input name="attrValueID" type="hidden" value="-1"/>
					<table class="dlog_tb_form" cellspacing="10">
						<tr>
							<th>属性值:</th>
							<td><input name="valueName" class="i_input easyui-validatebox"
								required="true"/>
							</td>
							
						</tr>
						<tr>
							<th>属性值码:</th>
							<td><input name="valueCode" class="i_input easyui-validatebox"
								required="true"/>
							</td>
						</tr>
						<tr>
							<th>备注:</th>
							<td colspan="4">
							<textarea name="comment">
							</textarea></td>
						</tr>

					</table>
				</form>
		</div>
	</div>
</body>
</html>