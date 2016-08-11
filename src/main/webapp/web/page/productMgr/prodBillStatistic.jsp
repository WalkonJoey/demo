<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="web/css/common/reset.css">
<style>
.common_info .tableForm tr{
	line-height:28px;
}
.common_info .tableForm tr th{
	padding-left:20px;
	text-align:right;
	padding-right:5px;
}
.tableForm tr td{
	border-bottom:1px solid #c9c9c9;
	text-align:center;
}
.common_info .tableForm tr td{
	min-width:150px;
	max-width:200px;
	
}

/*产品描述和产品备注*/
.long_info_wap{
	width:100%;
	overflow:hidden;
}
.long_info_header{
	height:18px;
	line-height:18px;
	background-color:#e8e8e8;
}
.long_info_header span{
	display:inline-block;
	width:47%;
	float:left;
	margin-left:20px;
}
.long_info{
	box-sizing: border-box;
    float: left;
    height: 85px;
    margin-left: 20px;
    padding-left: 50px;
    text-indent: 1em;
    width: 47%;
    word-wrap: break-word;
}
/*产品基本信息部分*/
.crumbs_nav{
	height:30px;
	background-color:#ddd;
	line-height:30px;
	border-bottom:1px solid #ccc;
	padding-left:20px;
}
/* 自定义Tab的 公共 样式 */
.rel_opt_wap{
	margin-top:10px;
}
.opt_tabs {
	float: left;

	width: 100%;

	border-bottom: 1px solid #ccc;
}

.opt_tabs ul {
	list-style: none outside none;
	margin: 0;
	padding: 0px 5px;
}

.tab_nav {
	float: left;
	line-height: 24px;
	margin-right: 5px;
	padding: 2px 20px 0 15px;
	background-color: #ccc;
	cursor: pointer;
	color:#666;
}

.nav_action {
	color: #fff;
	background: #5ba9de;
}

.opt_tabs_body {
	border-bottom: 1px solid #ccc;
	border-left: 1px solid #ccc;
	border-right: 1px solid #ccc;
	box-sizing: border-box;
	float: left;

	width: 100%;
}

.opt_item {
	height: 392px;
	padding: 10px;
}
/* Tab 自定义内容样式*/
.dtgrid_wap {
	height: 400px;
	border: 1px solid #ccc;
}
/* .dtgrid_wap_po{
	height: 400px;
	width:48%;
	margin-right: 20px;
	float:left;
}
.dtgrid_wap_so{
	height: 400px;
	width:48%;
} */
</style>
<script type="text/javascript" charset="UTF-8">
//打开TAB查看详情页
function openSoViewTab(saleID,saleNo) {
	parent.addTab('tabId_viewSo', '销售订单' + saleNo + '详情', 'saleManage/saleView?saleID='
		+ saleID);
}
//打开销售
function openSaleViewLink(mydatagrid,index) {
	var rowItems = $(mydatagrid).datagrid('getRows');
	var row = rowItems[index];
	parent.addTab('tabId_viewSo', '销售订单' + row.saleNo + '详情', 'saleManage/saleView?saleID='
			+ row.saleID);
}
var stock_dtgrid;
$(function(){
	//给自定义的Tab标签绑定事件
	$("#opt-tabs li").bind("click", function () {
        var index = $(this).index();
        var divs = $("#opt-tabs-body > div");
        $(this).parent().children("li").attr("class", "tab_nav");//将所有选项置为未选中
        $(this).attr("class", "nav_action tab_nav"); //设置当前选中项为选中样式
        divs.hide();//隐藏所有选中项内容
        divs.eq(index).show(); //显示选中项对应内容
    });
	
	$("#theory-unmatch").bind("click",function(){
		$("#theory_unmatch_dtgrid").datagrid({
			url : 'stockManage/findSkuQtyInfo/${skuNo}/${warehouseCode}/thoeryUnmatch',
			toolbar : '#toolbar',
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 20, 30,40,50, ],
			fit : true,
			fitColumns : false,
			nowrap : true,
			border : false,
			rownumbers: true,
			checkOnSelect : true,
			selectOnCheck : false,
			singleSelect:true,
			autoRowHeight:false,
			rowStyler: function(index,row){
					return 'height:30px'; // return inline style
			},
			idField : 'poID',
			columns : [ [ //每个列具体内容 field的内容要与传递过来的json数据中的Key值对应相等
			  {
				field : 'poID',
				title : '',
				sortable:true,
				align:'center',
				width : 20,
				hidden:true,
			},
			{
				field : 'poNo',
				title : '采购订单号',
				align:'center',
				width : 160,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				},
				formatter:function(value,row,index){ 
					return "<a href='javascript:void(0);' onclick=\"openPoViewTab('"+row.poID+"','"+row.poNo+"');return false;\">"+value+"</a>";
            } ,
			}, {
				field : 'skuNo',
				title : 'skuNo',
				align:'center',
				sortable:true,
				order: 'asc',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			},  
			{
				field : 'remainingQty',
				title : '理论未匹配数量',
				align:'center',
				sortable:true,
				order: 'asc',
				width : 160,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}] ]
		});
	});
	$("#on-way").bind("click",function(){
		$("#onway_dtgrid").datagrid({
			url : 'stockManage/findSkuQtyInfo/${skuNo}/${warehouseCode}/onway',
			toolbar : '#toolbar',
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 20, 30,40,50, ],
			fit : true,
			fitColumns : false,
			nowrap : true,
			border : false,
			rownumbers: true,
			checkOnSelect : true,
			selectOnCheck : false,
			singleSelect:true,
			autoRowHeight:false,
			rowStyler: function(index,row){
					return 'height:30px'; // return inline style
			},
			idField : 'deliveryID',
			columns : [ [ //每个列具体内容 field的内容要与传递过来的json数据中的Key值对应相等
			  {
				field : 'poID',
				title : '',
				sortable:true,
				align:'center',
				width : 20,
				hidden:true,
			},
			{
				field : 'poNo',
				title : '采购订单号',
				align:'center',
				width : 160,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				},
				formatter:function(value,row,index){ 
                	//return "<a href='delivery/view/"+row.deliveryID+"' target='_blank'>"+value+"</a>";
					return "<a href='javascript:void(0);' onclick=\"openPoViewTab('"+row.poID+"','"+row.poNo+"');return false;\">"+value+"</a>";
            } ,
			}, {
				field : 'skuNo',
				title : 'skuNo',
				align:'center',
				sortable:true,
				order: 'asc',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			},  
			{
				field : 'deliveredQty',
				title : '在途数量',
				align:'center',
				sortable:true,
				order: 'asc',
				width : 160,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}] ]
		});
	});
	/* $("#pending").bind("click",function(){
		$("#pending_dtgrid").datagrid({
			url : 'stockManage/findSkuQtyInfo/${skuNo}/${warehouseCode}/onpending',
			title : '',
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40 ],
			fit : true,
			fitColumns : false,
			nowrap : false,
			border : false,
			idField : 'whEntryID',
			sortName : 'whEntryNo',
			sortOrder : 'asc',
			singleSelect:true,
			rownumbers:true,
			frozenColumns : [ [ {
				title : 'poID',
				field : 'poID',
				width : 140,
				hidden:true
			},{
				title : '采购订单号',
				field : 'poNo',
				align:"center",
				sortable : true,
				width : 140,
				formatter : function(value, row, index) {
					return "<a href='javascript:void(0);' onclick=\"openPoViewTab('"+row.poID+"','"+row.poNo+"');return false;\">"+value+"</a>";
				}
			}, {
				field : 'skuNo',
				title : 'skuNo',
				align:'center',
				sortable:true,
				order: 'asc',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}, {
				title : '未上架数量',
				field : 'receivedQty',
				align:"center",
				width : 140
				
			}] ]
		});
	}); */
	$("#available").bind("click",function(){
		$("#avail_dtgrid").datagrid({
			url : 'stockManage/findSkuQtyInfo/${skuNo}/${warehouseCode}/available',
			title : '',
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 15, 20 ],
			fit : true,
			fitColumns : false,
			nowrap : false,
			border : false,
			idField : 'whEntryID',
			sortName : 'whEntryNo',
			sortOrder : 'asc',
	/* 		checkOnSelect : true,
			selectOnCheck : true, */
			singleSelect:true,
			rownumbers:true,
			frozenColumns : [ [ {
				title : '采购订单ID',
				field : 'poID',
				width : 140,
				hidden:true
			},{
				title : '采购订单编号',
				field : 'poNo',
				align:"center",
				sortable : true,
				width : 140,
				formatter : function(value, row, index) {
					return "<a href='javascript:void(0);' onclick=\"openPoViewTab('"+row.poID+"','"+row.poNo+"');return false;\">"+value+"</a>";
				}
			} ] ],
			columns : [ [ {
				field : 'skuNo',
				title : 'skuNo',
				align:'center',
				sortable:true,
				order: 'asc',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			},{
				title : '采购数量',
				field : 'prodQty',
				align:"center",
				width : 80,
				hidden:true
			},{
				title : '可用数量',
				field : 'remainingQty',
				align:"center",
				width : 160
			},{
				title : '备注',
				field : 'comment',
				align:"center",
				width : 100,
				hidden:true
			}] ]
		});
	});
	/* $("#unavailable").bind("click",function(){
		$("#unavail_dtgrid").datagrid({
			
		});
	}); */
	$("#reserved").bind("click",function(){
		/*$("#reserved_dtgrid").datagrid({
			url : 'stockManage/findReservedQtyInfo/${skuNo}/${warehouseCode}',
			toolbar : '#toolbar',
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 20, 30,40,50, ],
			fit : true,
			fitColumns : false,
			nowrap : true,
			border : false,
			rownumbers: true,
			checkOnSelect : true,
			selectOnCheck : false,
			singleSelect:true,
			autoRowHeight:false,
			rowStyler: function(index,row){
					return 'height:30px'; // return inline style
			},
			idField : 'poID',
			columns : [ [ //每个列具体内容 field的内容要与传递过来的json数据中的Key值对应相等
			{
			  	field : 'poID',
			  	title : '',
			  	sortable:true,
			  	order: 'asc',
			  	align:'center',
			  	width : 50,
			  	hidden:true
			},{
				field : 'poNo',
				title : '采购订单号',
				align:'center',
				sortable:true,
				order: 'asc',
				width : 150,
				formatter : function(value, row, index) {
					return "<a href='javascript:void(0);' onclick=\"openPoViewTab('"+row.poID+"','"+row.poNo+"');return false;\">"+value+"</a>";
				}
			}, {
				field : 'skuNo',
				title : 'skuNo',
				align:'center',
				sortable:true,
				order: 'asc',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			},{
				field : 'remainingQty',
				title : '冻结数量',
				align:'center',
				order: 'asc',
				width : 160,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}
			] ]
		}); */
		$("#reserved_dtgrid_so").datagrid({
			url : 'stockManage/findReservedQtySoInfo/${skuNo}/${warehouseCode}',
			toolbar : '',
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			pageList : [ 10, 20, 30,40, 50],
			fit : true,
			fitColumns : false,
			nowrap : true,
			border : false,
			rownumbers: true,
			checkOnSelect : true,
			selectOnCheck : false,
			singleSelect:true,
			autoRowHeight:false,
			rowStyler: function(index,row){
					return 'height:30px'; // return inline style
			},
			idField : 'saleID',
			columns : [ [ //每个列具体内容 field的内容要与传递过来的json数据中的Key值对应相等
			{
			  	field : 'saleID',
			  	title : '',
			  	sortable:true,
			  	order: 'asc',
			  	align:'center',
			  	width : 50,
			  	hidden:true
			},{
				field : 'saleNo',
				title : '销售订单号',
				align:'center',
				sortable:true,
				order: 'asc',
				width : 150,
				formatter : function(value, row, index) {
					return "<a href='javascript:void(0);' onclick=\"openSoViewTab('"+row.saleID+"','"+row.saleNo+"');return false;\">"+value+"</a>";
				}
			}, {
				field : 'skuNo',
				title : 'skuNo',
				align:'center',
				sortable:true,
				order: 'asc',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			},{
				field : 'unDealedQty',
				title : '未匹配数量',
				align:'center',
				order: 'asc',
				width : 160,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}
			] ]
		});
	});
	$("#shipped").bind("click",function(){
		$("#shipped_dtgrid").datagrid({
			url : 'stockManage/findSkuQtyInfo/${skuNo}/${warehouseCode}/shipment',
			toolbar : '#toolbar',
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			pageList : [10, 20,30,40,50, ],
			fit : true,
			fitColumns : false,
			nowrap : true,
			border : false,
			rownumbers: true,
			checkOnSelect : false,
			selectOnCheck : false,
			idField : 'whOutID',
			sortName: 'whOutNo',
			sortOrder: 'asc',
			singleSelect:true,
			autoRowHeight:false,
			rowStyler: function(index,row){
				return 'height:30px'; // return inline style
			},
			columns : [ [ //每个列具体内容 field的内容要与传递过来的json数据中的Key值对应相等
			{
				field : 'saleID',
			  	title : '',
			  	sortable:true,
			  	align:'center',
			  	width : 20,
			  	hidden:true
			},{
				field : 'saleNo',
				title : '销售订单号',
				align:'center',
				width : 160,
				sortable:true,
				order: 'asc',
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				},
				formatter:function(value,row,index){ 
					return "<a href='javascript:void(0);' onclick=\"openSoViewTab('"+row.saleID+"','"+row.saleNo+"');return false;\">"+value+"</a>";
				}
			}, {
				field : 'skuNo',
				title : 'skuNo',
				align:'center',
				sortable:true,
				order: 'asc',
				width : 100,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			},{
				field : 'prodQty',
				title : '数量',
				align:'center',
				width : 160,
				editor : {
					type : 'validatebox',
					options : {
						required : true
					}
				}
			}] ]
		});
	});
	//通过传递过来的条件，选择对应的tab
	if("${description}"){
		var description = "${description}";
		$("#opt-tabs li:contains("+description+")").click();
	}
});
</script>
</head>
<body class="view_body">
	<div class="wap">
		<div class="header_wap">
				<h3 class="view_title">产品 <b>${skuNo}</b> 库存详情</h3>
			<div class="common_info">
				<table class="view_table" cellspacing="0">
					<tr>
						<th>产品名称:</th>
						<td colspan="3">${productInfo.productName}</td>
					</tr>
					<tr>
						<th>规格参数:</th>
						<td>${productInfo.specParam}</td>
						<th>供应商型号:</th>
						<td>${productInfo.vendorModel}</td>
					</tr>
					<tr>
						<th>供应商名称:</th>
						<td>${productInfo.vendorName}</td>
						<th>销售价格:</th>
						<td>${productInfo.sellPrice}</td>
					</tr>
					<tr>
						<th>采购价格:</th>
						<td>${productInfo.purchasePrice}</td>
						<th>打包前重量:</th>
						<td>${productInfo.beforePackWeight}</td>
					</tr>
					<tr>
						<th>打包后重量:</th>
						<td>${productInfo.afterPackWeight}</td>
						<th></th>
						<td></td>
					</tr>
				</table>

			</div>
		</div>
		<div class="rel_opt_wap">
			<!-- <div id="opt-tab"></div> -->
			<div class="opt_tabs">
				<ul id="opt-tabs">
					<li class="tab_nav nav_action" id="theory-unmatch">理论未匹配</li>
					<li class="tab_nav" id="on-way">在途</li>
					<!-- <li class="tab_nav" id="pending">未上架</li> -->
					<li class="tab_nav" id="available">可销售</li>
					<!-- <li class="tab_nav" id="unavailable">不可售</li> --><!-- 2014/11/17 -->
					<li class="tab_nav" id="reserved">冻结(so未匹配)</li>
					<li class="tab_nav" id="shipped">出库</li>
				</ul>
			</div>
			<div id="opt-tabs-body" class="opt_tabs_body">
				<div class="opt_item stock_info" style="display: block">
					<div class="dtgrid_wap">
						<table id="theory_unmatch_dtgrid"></table>
					</div>
				</div>
				<div class="opt_item">
					<div class="dtgrid_wap">
						<table id="onway_dtgrid"></table>
					</div>
				</div>
				<!-- <div class="opt_item" style="display: none">
					<div class="dtgrid_wap">
						<table id="pending_dtgrid"></table>
					</div>
				</div> -->
					
				<div class="opt_item" style="display: none">
					<div class="dtgrid_wap">
						<table id="avail_dtgrid"></table>
					</div>
				</div>
				<!-- <div class="opt_item" style="display: none">
					<div class="dtgrid_wap">
						<table id="unavail_dtgrid"></table>
					</div>
				</div> --><!-- 2014/11/17 -->
				<div class="opt_item" style="display: none">
					<div class="dtgrid_wap">
						<!-- <div class="dtgrid_wap_po">
							<table id="reserved_dtgrid"></table>
						</div> -->
						<table id="reserved_dtgrid_so"></table>
						<!-- <div class="dtgrid_wap_so">
						</div> -->
					</div>
				</div>
				<div class="opt_item" style="display: none">
					<div class="dtgrid_wap">
						<table id="shipped_dtgrid"></table>
					</div>
				</div>
			</div>
		</div>
	</div><!-- wap -->
</body>
</html>