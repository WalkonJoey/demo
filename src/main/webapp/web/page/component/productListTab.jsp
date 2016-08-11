<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<!-- 此页面用于TAB选项卡，展示供应商相关联的在售产品列表信息 -->
<script type="text/javascript">
$(function(){
	var registeredDatagrid = $('#productRegistered_dtgrid').datagrid({
		url : 'productManage/getOnSaleProductListTab?vendorID=${vendor.vendorID}',
		toolbar : '#toolbar',
		iconCls : 'icon-save',
		pagination : true,
		pageSize : 15,
		pageList : [ 10, 15, 20],
		fit : true,
		fitColumns : false,
		nowrap : true,
		border : false,
		checkOnSelect : false, 
		selectOnCheck : false, 
		rownumbers: true,
		singleSelect:true,
		autoRowHeight:false,
		rowStyler: function(index,row){
				return 'height:50px'; // return inline style
		},
		idField : 'skuNo',
		columns : [ [ 
		{
			field : 'skuNo',
			title : 'skuNo',
			width : 100,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		}, {
			title : '产品中文名称',
			field : 'productName',
			align : 'center',
			width : 150
			
		}, {
			title : '供方型号',
			field : 'vendorModel',
			align : 'center',
			width : 180

		}, {
			title : '单位',
			field : 'unit',
			align : 'center',
			width : 50
		},{
			title : '采购价格',
			field : 'purchasePrice',
			align : 'center',
			width : 60
		}, {
			title : '图片',
			field : 'picturePath',
			width : 120,
			align : 'center',
			formatter : function(value, row, index) {
				if (value != null) {
					var mainPic = value.split(",");
					return '<img style="width:36px;height:36px;" onmouseover= displayBigPic(this) src="'
							+ mainPic[0] + '" />';
				} else {
					return value;
				}
			}
		} ] ]
	});
});
</script>
</head>
<body>
	<br>
<table id="productRegistered_dtgrid"></table>
</body>
</html>