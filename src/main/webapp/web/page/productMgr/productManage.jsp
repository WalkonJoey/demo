<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<style>
.wap{
	width:100%;
	height:100%;
}
.btn_td{
	padding-left:24px;
}
/*datagrid*/
.datagrid_wap{
	width:100%;
	height:500px;
	
}
.datagrid_content{
	border: 1px solid #ccc;
    float: left;
    height: 440px;
    overflow-x: hidden;
    overflow-y: auto;
    width: 300px;
}


/* easyuiDialog */
.dia_attr_list{
	width: 98%;
	border:1px solid #ccc;
	
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
.l-btn-icon{/*iconfont*/
	font-family:"iconfont" !important;
  	font-size:16px;
  	font-style:normal;
 	-webkit-font-smoothing: antialiased;
  	-webkit-text-stroke-width: 0.2px;
 	-moz-osx-font-smoothing: grayscale;
}
</style>
<script type="text/javascript">
var datagrid;

var vendorComboGrid;
var importProdDialog;
var excelForm;
var displayRowNumber;
$(function() {
	var clientHeight = document.body.clientHeight;
	if(clientHeight>700){
		displayRowNumber = 25;
	}else{
		displayRowNumber = 15;
	}
	excelForm = $("#excelForm").form();
	
	importProdDialog = $('#importProdDialog').show().dialog({
		modal : true,
		title : '批量导入产品',
		height : 250,
		width : 500,
		buttons : [{
					text : '确定',
					handler : function() {
						excelForm.form('submit', {
							url : 'productManage/importProduct.do',
							success : function(data) {
								var dt = $.parseJSON(data);
								if (dt.success) {
									$.messager.progress('close');
									parent.showPromptMessages("提示",dt.msg);
									
									parent.refreshTab("产品信息");
								}else{
									$.messager.progress('close');
									$.messager.alert('提示',dt.msg);
								}
							}
						});
						importProdDialog.dialog('close');
						$.messager.progress();
					}
				}]
	}).dialog('close');//importProdDialog
	
	datagrid = $('#datagrid').datagrid({
		url : 'productManage/findProductsByCondition.do',
		toolbar : '#toolbar',
		iconCls : 'icon-save',
		pagination : true,
		pageSize : displayRowNumber,
		pageList : [10,15, 20, 25, 50,100,200],
		fit : true,
		fitColumns : false,
		nowrap : true,
		border : false,
		idField : 'skuNo',
		sortName : 'skuNo',
		sortOrder : 'asc',
		checkOnSelect : false,
		selectOnCheck : false,
		singleSelect : true,
		rownumbers : true,
		checkbox:true,
		autoRowHeight:false,
		rowStyler: function(index,row){
				return 'height:38px;line-height:38px;'; // return inline style
		},
		frozenColumns : [[{
			title : '产品ID',
			field : 'productID',
			width : 100,
			checkbox:true,
			formatter : function(value,row, index) {
					return '<span style="width:36px;height:36px;">'+value+'</span>';
				}
			}, {
			title : '产品编号',
			field : 'skuNo',
			align : 'center',
			sortable : true,
			width : 100,
			formatter : function(value,row, index) {
					return "<a href='javascript:void(0);' onclick='showThisProdDetail("+row.skuNo+","+row.productID+");'>"+value+"</a>";
				}
		}, {
			title : '供货单位',
			field : 'vendorName',
			align : 'center',
			width : 100

		}]],
		columns : [[{
					title : '产品中文名称',
					field : 'productCnName',
					align : 'center',
					editor : {
						type : 'validatebox',
						options : {
							required : true
						}
					},
					width : 150
					
				},{
					title : '产品英文名称',
					field : 'productEnName',
					align : 'center',
					editor : {
						type : 'validatebox',
						options : {
							required : true
						}
					},
					width : 150
					
				}, {
					title : '库存',
					field : 'minQtyInStock',
					align : 'center',
					width : 50
				}, {
					title : '采购价格',
					field : 'purchasePrice',
					align : 'right',
					width : 60
				}, {
					title : '图片',
					field : 'picturePath',
					width : 70,
					align : 'center',
					formatter : function(value, row, index) {
						if (value != null) {
							//var url = "https://www.myled.com/image/cache/product/"+value.substring(1,2)+"/"+value.substring(2,3)+value.substring(0,value.lastIndexOf("."))+"-59x59.jpg";
							/*var url = "https://www.myled.com/image/cache/"+value.substring(0,value.lastIndexOf("."))+"-170x170"+value.substring(value.lastIndexOf("."),value.length);*/
							var url = "web/images/upload/"+row.skuNo+"/main.jpg";
							return '<img style="width:36px;height:36px;" onmouseover= displayBigPic(this) src="'
									+ url + '" />';
						} else {
							return value;
						}
					}
				}, {
					title : '打包前重量',
					field : 'beforePackWeight',
					align : 'center',
					hidden : true,
					width : 70
				}, {
					title : '打包后重量',
					field : 'afterPackWeight',
					align : 'center',
					hidden : true,
					width : 70
				}/*, {
					title : '是否备案',
					field : 'isRegister',
					align : 'center',
					width : 60,
					formatter : function(value, rec) {
						if (value == 1)
							return '是';
						else
							return '否';
					}
				}*/, {
					field : 'operatorName',
					title : '操作人员',
					width : 100,
					align : 'center',
					editor : {
						type : 'validatebox',
						options : {
							required : true
						}
					}
				}, {
					field : 'operateDt',
					title : '操作时间',
					width : 140,
					sortable : true,
					align : 'center',
					editor : {
						type : 'validatebox',
						options : {
							required : true
						}
					}
				}]]
	});//datagrid
	// 绑定回车事件
	document.onkeydown = function(e) {
		var ev = document.all ? window.event : e;
		if (ev.keyCode == 13) {// 如（ev.ctrlKey && ev.keyCode==13）为ctrl+Center
								// 触发
			searchFun();
		}
	};
	//绑定文件上传类型验证
	$('#file').MultiFile({
		  maxlength: '2',
		  accept:'xls|xlsx',
	      STRING: {
	        remove:'删除',
	        selected:'Selecionado: $file',
	        denied:'不支持上传该文件类型 $ext!'
	       }
	     });
	     
	vendorComboGrid = $(".search_table input[name='vendorID']").combogrid({
				panelWidth : 314,
				panelHeight : 360,
				height : 28,
				pagination : true,
				width : 202,
				idField : 'vendorID',
				textField : 'vendorName',
				url : 'vendor/getVendorList.do',
				columns : [[{
							field : 'vendorID',
							title : 'Code',
							hidden : true,
							width : 60
						}, {
							field : 'vendorCode',
							title : '供应商码',
							width : 124
						}, {
							field : 'vendorName',
							title : '供应商名',
							width : 166
						}]]
			});//vendorComboGrid
			
});// $(function(){})完

function displayBigPic(obj) {
	var $this = $(obj);
	var sParent = $this.parents("body");
	var _src = $this.attr("src");
	/*将url处理一下*/
	var src = _src.replace(/170x170/,"455x455");
	console.log(src);
	$this.hover(function() {
				// $("body").append("<div id='display-big-pic'><img
				// src='"+src+"'/></div>");
				var top = sParent.offset().top + 150 + "px";
				var left = sParent.offset().left +150 + "px";
				/* console.log("width:"+$this.width+"height:"+$this.height); */
				/*var width = $this.width;
				var height = $this.height;*/
				$("#display-big-pic").css("top", top);
				$("#display-big-pic").css("left", left);
				$("#display-big-pic img").attr("src", src);
				$("#display-big-pic").show();
			}, function() {
				$("#display-big-pic").hide();
			});
}

function append() {
	parent.addTab('tabId_addProduct', '增加产品', 'productManage/updateProductPg.do?productID=-1');
}

function editProductInfo() {
	var rows = datagrid.datagrid('getSelections');
	if (rows.length > 1) {
		var names = [];
		for (var i = 0; i < rows.length; i++) {
			names.push(rows[i].productID);
		}
		$.messager.show({
					msg : '只能择一个产品进行编辑！您已经选择了【' + names.join(',') + '】'
							+ rows.length + '个产品',
					title : '提示'
				});
	} else if (rows.length == 1) {
		// 首先加载tab页面 调用父页面的方法
		parent.addTab('tabId_updateProduct', '修改产品',
				'productManage/updateProductPg.do?productID='
						+ rows[0].productID);
	} else {
		$.messager.alert('提示', '请选择要编辑的记录！', 'error');
	}
}
function changeProdPic() {
	var rows = datagrid.datagrid('getSelections');
	if (rows.length > 1) {
		var names = [];
		for (var i = 0; i < rows.length; i++) {
			names.push(rows[i].roleID);
		}
		$.messager.show({
					msg : '只能择一个产品进行编辑！您已经选择了【' + names.join(',') + '】'
							+ rows.length + '个产品',
					title : '提示'
				});
	} else if (rows.length == 1) {
		// 首先加载tab页面 调用父页面的方法
		parent.addTab('tabId_updateProductPic', '修改产品图片',
				'productManage/updateProductPicPg.do?productID='
						+ rows[0].productID);
	} else {
		$.messager.alert('提示', '请选择要编辑的记录！', 'warning');
	}
}
function importAttachment() {
	var rows = datagrid.datagrid('getSelections');
	if (rows.length > 1) {
		var names = [];
		for (var i = 0; i < rows.length; i++) {
			names.push(rows[i].roleID);
		}
		$.messager.show({
					msg : '只能择一个产品进行编辑！您已经选择了【' + names.join(',') + '】'
							+ rows.length + '个产品',
					title : '提示'
				});
	} else if (rows.length == 1) {
		// 首先加载tab页面 调用父页面的方法
		parent.addTab('tabId_updateProductPic', '附件维护',
				'productManage/importAttachmentPg.do?productID='
						+ rows[0].productID);
	} else {
		$.messager.alert('提示', '请选择要编辑的记录！', 'warning');
	}
}
function removeProduct() {
	var skuNos = [];
	var rows = datagrid.datagrid('getChecked');
	if (rows.length > 0) {
		$.messager.confirm('请确认', '您要删除当前所选的 '+rows.length+' 个项目？', function(r) {
					if (r) {
						for (var i = 0; i < rows.length; i++) {
							skuNos.push(rows[i].skuNo);
						}
						$.post('productManage/deleteProduct.do',{
										skuNos : skuNos.join(',')
									},function(data) {
										console.log(data);
										if(data.success){
											datagrid.datagrid('uncheckAll');
											datagrid.datagrid('reload');
											$.messager.show({
														title : '提示',
														msg : '删除成功！'
													});
										}else{
											$.messager.alert('提示','删除产品出现未知错误！');
										}
									},"json"
								);
					}
				});
	} else {
		$.messager.alert('提示', '请选中记录前面的复选框进行删除！', 'warning');
	}
}

function showDetail() {
	var row = datagrid.datagrid('getSelected');
	if (row != null) {
		parent.addTab('tabId_productDetail', '产品'+row.skuNo+'详情',
				'productManage/productDetailPg.do?productID=' + row.productID);
	} else {
		$.messager.alert('提示', '请选则一个产品！', 'warning');
	}
}
function showThisProdDetail(skuNo,productID){
//		console.log(row.skuNo);
		/*parent.addTab('tabId_productDetail', '产品详情('+row.skuNo+')',
				'productManage/productDetailPg.do?productID=' + row.productID);*/
	parent.addTab('tabId_productDetail', '产品'+skuNo+'详情',
				'productManage/productDetailPg.do?productID=' + productID);
}
function importProduct(){
	importProdDialog.dialog('open');
}

//批量导出
function exportProd() {
	$.messager.confirm('提示信息', '导出的数据越多时间越长，请耐心等待。', function(result){
		if (result){
			var rowItems = datagrid.datagrid('getChecked');
			if (rowItems==null||rowItems.length==0) {
				parent.$.messager.alert('提示', '请先选择需要导出的订单（勾选多选框）！', 'warning');
				$.messager.progress('close');
				return;
			}
			var poIDArr = [];
			for(var i = 0;i<rowItems.length;i++){
				poIDArr.push(rowItems[i].productID);
			}
			var prodIDs = poIDArr.join("-");
			window.location.href = 'productManage/exportExcel?prodIDs='+prodIDs;
		}
	});
}
function searchFun(){
	var _createDtB;
	var _createDtE;
	//console.log($("#batchSkuNo").val().replace(/\n/g,","));
	datagrid.datagrid('load',
		{
			isSearch:"yes",
			skuNo : $('.search_table input[name=skuNo]').val(),
			billStatus : $('.search_table select[name=billStatus]').val(),
			productCnName : $('.search_table input[name=productCnName]').val(),
			batchSkuNo : $("#batchSkuNo").val().replace(/\n/g,","),
			createDtB : _createDtB,
			createDtE : _createDtE
		});
}

</script>
</head>
<body class="easyui-layout" fit="true" onkeydown="doSearch(event.keyCode,searchFun)">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar" class="datagrid-toolbar" >
			<div class="search_top">
			<div class="s_top_content">
				<table class="search_table">
					<tr>
						<th>SKU码:</th>
						<td><input name="skuNo" /></td>
						<th class="advanced_element">产品名称:</th>
						<td class="advanced_element">
							<input  name="productCnName" type="text"/>
						</td>
						<!-- <th class="advanced_element">是否备案:</th>
						<td class="advanced_element">
							<select  name="billStatus">
								<option value="-1">默认</option>
								<option value="0">草稿</option>
								<option value="30">保宏创建中</option>
								<option value="32">备案中</option>
								<option value="33">备案失败</option>
								<option value="125">备案成功</option>
							</select>
						</td> -->
						
						<td><div class="advanced_btn_wap">
								<a class="easyui-linkbutton confirmButton" iconCls="icon-search"
									plain="false" onclick="searchFun();" href="javascript:void(0);">查找</a>
								<a id="common-clear-btn" class="easyui-linkbutton" iconCls="icon-sdelete" plain="false" onclick="clearFun($('#datagrid'));"
										href="javascript:void(0);">清空</a>
								<a class="easyui-linkbutton" plain="false"
									onclick="switchAdvanced($('#datagrid'));" href="javascript:void(0);">切换到高级搜索</a>
							</div>
						</td>
					</tr>
					<tr class="advanced_element">
						
						<!-- <th>分类:</th>
						<td><input name="categoryID" disabled="disabled"/></td> -->
						<!-- <th>供应商产品编码:</th>
						<td><input name="vendorProdCode"/></td> -->
						<th>创建日期开始于:</th>
						<td><input name="createDtB" class="easyui-datebox" data-options="width:150,height:28"/></td>
						<th>创建日期截止于:</th>
						<td><input name="createDtE" class="easyui-datebox" data-options="width:150,height:28"/></td>
						<td class="btn_td" colspan="2">
						</td>
					</tr>
					<tr class="advanced_element">
						<th>批量sku（以换行符分隔）:</th>
						<td colspan="3"><textarea onkeydown = "event.stopPropagation();" id="batchSkuNo" style="width:100%;height:80px;padding:3px;"></textarea></td>
						<td class="btn_td" colspan="2">
							<div class="common_search_wap">
								<a class="easyui-linkbutton confirmButton" iconCls="icon-search"
									plain="false" onclick="searchFun();" href="javascript:void(0);">查找</a>
								<a class="easyui-linkbutton" iconCls="icon-sdelete" plain="false" onclick="clearFun($('#datagrid'));"
										href="javascript:void(0);">清空</a>
								<a class="easyui-linkbutton"  plain="false"
									onclick="switchCommon($('#datagrid'));" href="javascript:void(0);">切换到普通搜索</a>
							</div>
						</td>
					</tr>
					
				</table>
			</div>
			<!-- s_top_content -->
		</div>
		<!-- search_top -->
			<div>
				<a class="easyui-linkbutton" iconCls="icon-add" onclick="append();"
					plain="true" href="javascript:void(0);">新增</a>
				<a class="easyui-linkbutton" iconCls="icon-import"
					onclick="importProduct();" plain="true" href="javascript:void(0);">批量导入</a>
				<a class="easyui-linkbutton" iconCls="icon-edit" onclick="editProductInfo();"
					plain="true" href="javascript:void(0);">编辑</a>
				<a class="easyui-linkbutton" iconCls="icon-changepic" onclick="changeProdPic();"
					plain="true" href="javascript:void(0);">新增或修改图片</a>
				<a class="easyui-linkbutton" iconCls="icon-changepic" onclick="importAttachment();"
					plain="true" href="javascript:void(0);">附件维护</a>
				<!-- <a class="easyui-linkbutton" iconCls="icon-xiugai01"
					onclick="importDynProductAttr();" plain="true" href="javascript:void(0);">批量修改</a> -->
				<!-- <a class="easyui-linkbutton" iconCls="icon-approve"
					onclick="batchApproveProd();" plain="true" href="javascript:void(0);">批量审批</a> -->
				<!-- <a class="easyui-linkbutton" iconCls="icon-sync" onclick="batchUpdProdAttr();"
					plain="true" href="javascript:void(0);">批量更新产品</a>
				<a class="easyui-linkbutton" iconCls="icon-daoru"
					onclick="importPurchasePrice();" plain="true" href="javascript:void(0);">导入最新采购价格</a> -->
				<a class="easyui-linkbutton" iconCls="icon-daochu"
					onclick="exportProd();" plain="true" href="javascript:void(0);">导出产品信息</a>
				<a class="easyui-linkbutton" iconCls="icon-cuowu"
					onclick="removeProduct();" plain="true" href="javascript:void(0);">删除</a>
				
			</div>
		</div>
		<table id="datagrid"></table>
	</div>
	<div id="importProdDialog">
		<form id="excelForm" method="post" enctype="multipart/form-data">
			<table id="tableForm" class="tableForm">
				<tr align="center">
					<td>Excel选择:<img class="help" title="文件格式限定.xls/xlsx"
						src="web/images/common/help.gif" /></td>
					<td><input type="file" name="file" id="file"
						onkeypress="return false" /></td>
				</tr>
			</table>
			<div><a onclick="downloadTemplate('productManage/downloadTemplate.do?fileName=productTemplate.xlsx&exportName=产品导入模板.xlsx');" href="javascript:void(0);">下载模板</a></div>
		</form>
	</div>
	<!-- <div id="importProdAttrDialog">
		<form id="prodAttrForm" method="post" enctype="multipart/form-data">
			<table class="tableForm">
				<tr align="center">
					<td>Excel选择:<img class="help" title="文件格式限定.xls/xlsx"
						src="web/images/common/help.gif" /></td>
					<td><input type="file" name="file" id="file"
						onkeypress="return false" /></td>
				</tr>
			</table>
			<div><a href="productManage/downloadTemplate.do?fileName=productAttrTemplate.xlsx">下载模板</a></div>
		</form>
	</div> -->
	<div id="display-big-pic" hidden><img src=""/></div>
</body>

</html>