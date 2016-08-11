<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<c:import url="../component/userDialog.jsp" />
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" charset="UTF-8">
	var centerTabs;
	var stock_dtgrid;
	var _skuNo = "${skuNo}";
	$(function() {
		$.ajax({
			type : "post",
			url : "productManage/getProductDetail.do",
			data : {
				productID : "${productID}",skuNo:"${skuNo}"
			},
			success : function(data) {

				if (data.success) {
					/* var commonAttrList = data.obj.commonAttrList; */
					/* var attrList = data.obj.attrList;
					var attrContent = "<ul>";
					for(var j = 0;j<attrList.length;j++){
						attrContent+="<li><span>"+attrList[j].attrName.attrName+":</span><span>"+attrList[j].attrValue.valueName+"</span></li>";
					}
					attrContent += "</ul>";
					$(".attr_list_content").html(attrContent); */
					
					var pics = data.obj.picture.picPath.split(",");
					var desPicContent = "<ul>";
					for(var i = 0;i<pics.length;i++){
						if(i==0){
							//var _src = "https://www.myled.com/image/cache/product/"+pics[0].substring(1,2)+"/"+pics[0].substring(2,3)+pics[0].substring(0,pics[0].lastIndexOf("."))+"-170x170.jpg";
							/* var _src = "https://www.myled.com/image/cache/"+pics[0].substring(0,pics[0].lastIndexOf("."))+"-170x170.jpg"; */
							var _src = pics[0];
							$(".main_img_content").html("<img src='"+_src+"'/>");
						}else{
							/* var _src = "https://www.myled.com/image/cache/"+pics[i].substring(0,pics[i].lastIndexOf("."))+"-170x170.jpg"; */
							var _src = pics[i];
							desPicContent+="<li><img src='"+_src+"'/></li>";
						}
					}
					desPicContent += "</ul>";
					$(".des_img_content").html(desPicContent);
				}
			},
			dataType : "json"
		});
		
		//获取库存信息
		/* $.ajax({
			type : "post",
			url : "stockManage/findPdtStock4dgByCondition.do",
			data : {
				skuNo:"${skuNo}"
			},
			success : function(data) {
				if (data) {
					var _total = data.total;
					var _rows = data.rows;
					var htmlTable = "<table  class='item_view_table'><tr><td>仓库编码</td><td>采购未匹配数量</td><td>在途数量</td><td>未上架数量</td><td>可售数量</td><td>不可售数量</td><td>销售未匹配数量</td><td>已出库数量</td><td>更新时间</td></tr>";
					for(var i=0;i<_total;i++){
						htmlTable+="<tr><td>"+(_rows[i].whCode==null?"":_rows[i].whCode)+"</td><td>"+_rows[i].theoryPoRemainQty+"</td><td>"+_rows[i].onWayQty+"</td>"
						+"<td>"+_rows[i].pendingQty+"</td><td>"+_rows[i].availableQty+"</td><td>"+_rows[i].unavailableQty+"</td>"
						+"<td>"+_rows[i].totalReservedQty+"</td><td>"+_rows[i].shippedQty+"</td><td>"+_rows[i].updateDt+"</td></tr>";
					}
					htmlTable+="</table>";
					$('.stock_dtgrid_wap').append(htmlTable);
					console.log("data:"+JSON.stringify(data));
				}
			},
			dataType : "json"
		}); */
		
		$('#tab_container').tabs(
		{
			border : false,
			onSelect : function(title,index) {
			var tab = $('#tab_container').tabs('getSelected');
				if (index == 0) {
					/* tab.panel('refresh','vendor/getVendorTab/${po.vendorID}'); */
				}else if (index == 1) {
					$.ajax({
						type:"post",
						url:"productManage/getProductAttachments.do",
						data:{productID:"${productID}"},
						success:function(data){
							if(data.obj.picPath!=null&&data.obj.picPath!=""){
								var pics = data.obj.picPath.split(",");
								var skuNo = data.obj.skuNo;
								var desPicContent = "<ul>";
								for(var i = 0;i<pics.length;i++){
									var name = pics[i];
									if(pics[i].indexOf("web/images/upload/"+skuNo+"/") != -1){
										name = pics[i].replace("web/images/upload/"+skuNo+"/","");
									}
									desPicContent+="<li><a href='javascript:void(0);' data-src='"+pics[i]+"' onclick = \"downloadAttachement('"+pics[i]+"','"+name+"')\">"+name+"</a><span><input type='button' value='删除'/></span></li>";
								}
								desPicContent += "</ul>";
								$(".des_attachment_content").html(desPicContent);
								$(".des_attachment_content :button").click(function(){
									var $this =$(this);
									$.messager.confirm("提示","确定要删除这样附件？",function(r){
										if(r){
											$this.parent().parent().remove();
										}
									});
								});
							}else{
							}
							
						},
						dataType:"json"});
				} else if (index == 2) {
					/* tab.panel('refresh','delivery/getDeliveryTab/1/${po.poID}'); */
				}else {
				}

			}
		});
		$('#tab_container').tabs('getTab',0);
	});//$(function())预加载完成
	function disProdBillStatstc(skuNo,description){
		var encodeParam=encodeURI(encodeURI(description));
		parent.addTab('tabId_prodBillStatistic','产品订单信息','stockManage/prodBillStatisticPg.do?skuNo='+skuNo+'&description='+encodeParam);
	}
	
	function closeTab(isFresh){
		if(isFresh){
			parent.refreshTab("产品信息");
		}
		parent.deletePanel("产品"+_skuNo+"详情");
	}
</script>
<style>
body{
	font-size:12px;
}
.wap h3{
	/* background-color: #e8e8e8;
    font-size: 16px;
    height: 30px;
    line-height: 30px; */
    background: linear-gradient(to bottom, #eee 0px, #ccc 100%) repeat scroll 0 0 rgba(0, 0, 0, 0);
    border-bottom: 1px solid #ccc;
    height: 35px;
    line-height: 35px;
    margin: 0;
    padding-left: 20px;
}

.common_info_content .tableForm tr{
	line-height:28px;
}
.common_info_content .tableForm tr th{
	padding-left:20px;
	text-align:right;
	padding-right:5px;
	vertical-align:top;
	
}
.tableForm tr td{
	border-bottom:1px solid #c9c9c9;
	text-align:center;
}
.common_info_content .tableForm tr td{
	min-width:150px;
	/* white-space: normal; */
	max-width:200px;
	overflow:hidden;
}
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
    height: 85px;
    margin-left: 20px;
    padding-left: 50px;
    text-indent: 1em;
    word-wrap: break-word;
}

.long_info.prod_description > span {
    float: left;
}
/*产品属性展示部分*/
.attr_list_content ul {
	width: 100%;
	overflow: hidden;
}

.attr_list_content li {
	float: left;
    margin-left: 10px;
    margin-top: 10px;
    width: 200px;
}

.attr_list_content li span:nth-child(2) {
	color: green;
}

/*产品图片显示部分*/
.prod_img_list {
	width: 100%;
	overflow: hidden;
}

.main_img_wap {
	width: 200px;
	float: left;
}

.des_img_wap {
	margin-left: 200px;
}

.main_img_content img {
	width: 100px;
	height: 100px;
}

.des_img_content ul {
	width: 100%;
	overflow: hidden;
}

.des_img_content ul li {
	width: 104px;
	height: 104px;
	float: left;
}

.des_img_content ul li img {
	width: 100px;
	height: 100px;
}

.rel_opt_wap {
	width:100%;
	margin-top:20px;
	min-height:200px;
	overflow:hidden;
}
/*Tab的 公共 样式 */
.tab_wap{
	height:315px;
	float:left;
	border:1px solid #ccc;
}
#tab_container{
	width:100%;
	clear:both;
}
/*审批操作对应样式*/
.examine_approve{
	/* width:290px;
	float:left; */
	/* padding-top:50px; */
	margin-left:74%;
	border-top:1px solid #ccc;
}
.exam_appr_tb{
	width:100%;
	height:315px;
}

.exam_appr_tb ul{
	height:32px;
	margin-top:10px;
}
.exam_appr_tb ul li{
	height:32px;
	line-height:30px;
	float:left;
	vertical-align:middle;
}
.exam_appr_tb ul li:nth-child(1){
	width:95px;
	text-align:right;
}
.exam_appr_tb ul li:nth-child(2){
	width:180px;
}
.exam_appr_tb ul:nth-child(1){
	height:60px;
}
.option_comment textarea{
	height:60px;
	width:180px;
}

.exam_appr_tb ul li input{
	padding:6px;
	width:180px;
	
}
.approve_btn{
	width:270px;
	margin:10px auto;
}
.approve_btn span{
	display:inline-block;
	height:30px;
	line-height:30px;
	width:130px;
	margin:0px auto;
	
}
.approve_btn span input{
	height:30px;
	font-weight:bold;
	letter-spacing: 1em;
	width:264px;
	border-radius:5px;
}
.approve_btn_wap{
	width:270px;
	margin:10px auto;
}
.approve_btn_wap span input{
	height:30px;
	font-weight:bold;
	letter-spacing: 1em;
	width:130px;
	border-radius:5px;
}
/*库存信息展示*/
.stock_dtgrid_wap{
}
.stock_dtgrid_wap table{
	width:100%;
	font-size:12px;
}
.stock_dtgrid_wap table tr{
	height:22px;
	line-height:22px;
	text-align:center;
}
.stock_dtgrid_wap table tr:nth-child(1){
	font-weight:bold;
}
.stock_dtgrid_wap table tr td{
	width:60px;
}
.stock_dtgrid_wap table tr td:nth-child(1){
	width:80px;
}
.stock_dtgrid_wap table tr td:nth-child(2){
width:80px;
}
.stock_dtgrid_wap table tr td:nth-child(9){
width:140px;
}


.description_content {
    word-wrap: break-word;
    white-space: normal;
}
.common_info_content{
	word-break: break-all;
}
</style>
</head>
<body class="view_body">
	<h3 class="view_title"><b>产品基本信息：</b></h3>
	<div class="wap">
		<div class="prod_common_info">
			<div class="common_info_content">
				<table class="view_table">
					<tr>
						<th width="100">产品SKU:</th>
						<td>${productInfo.skuNo}</td>
						<th>产品中文名称:</th>
						<td>${productInfo.productCnName}</td>
					</tr>
					<tr>
						<th>产品英文名称:</th>
						<td colspan="3">${productInfo.productEnName}</td>
					</tr>
					<tr>
						<th>产品一级分类:</th>
						<td>${productInfo.productCategory}</td>
						<th>产品二级分类:</th>
						<td>${productInfo.twoLevelCategory}</td>
					</tr>
					<tr>
						<th>产品品牌:</th>
						<td>${productInfo.brand}</td>
						<th>关键字:</th>
						<td>${productInfo.keyword}</td>
					</tr>
					<tr>
						<th>产品型号:</th>
						<td>${productInfo.productModel}</td>
						<th>供应商编码:</th>
						<td>${productInfo.vendorCode}</td>
					</tr>
					<tr>
						<th>保质期:</th>
						<td>${productInfo.shelfLife}</td>
						<th>发货时效:</th>
						<td>${productInfo.deliveryTime}小时</td>
						
					</tr>
					<tr>
						<th>出售价格:</th>
						<td>${productInfo.salePrice}元</td>
						<th>采购价格:</th>
						<td>${productInfo.purchasePrice}元</td>
					</tr>
					<tr>
						<th>净重:</th>
						<td>${productInfo.beforePackWeight}g</td>
						<th>打包重量:</th>
						<td>${productInfo.afterPackWeight}g</td>
						
					</tr>
					<tr>
						<th>打包后长度:</th>
						<td>${productInfo.packageLength}cm</td>
						<th>打包后宽度:</th>
						<td>${productInfo.packageWidth}cm</td>
					</tr>
					<tr>
						<th>打包后高度:</th>
						<td>${productInfo.packageHeight}cm</td>
						<th>认证类型:</th>
						<td>${productInfo.authType}</td>
					</tr>
					<tr>
						
					</tr>
					<tr>
						<th>起定量:</th>
						<td>${productInfo.minOrderQty}</td>
					</tr>
					<tr>
						<th>供货单位:</th>
						<td>${productInfo.vendorName}</td>
						<th>发货地:</th>
						<td>${productInfo.address}</td>
					</tr>
					<tr>
						<th>创建日期:</th>
						<td><fmt:formatDate value="${productInfo.createDt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<th>最近操作日期:</th>
						<td><fmt:formatDate value="${productInfo.operateDt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					</tr>
					<tr>
						<th>包装清单:</th>
						<td colspan="3">${productInfo.packageList}</td>
					</tr>
					
					<tr>
						<th>产品参数:</th>
						<td colspan="3">${productInfo.specParam}</td>
					</tr>
					<tr>
						<th>产品描述:</th>
						<td colspan="3"><div class="description_content">${productInfo.description}</div></td>
					</tr>
					<tr>
						<th>产品备注:</th>
						<td colspan="3"><div class="comment_content">${productInfo.comment}</div></td>
					</tr>
				</table>
				
			</div>
		</div>
		<div class="rel_opt_wap">
			<!-- 这部分为选项卡 -->
			<div>
				<div id="tab_container" class="easyui-tabs" >
					<!-- iframe中的seamless属性是为了html5，但目前支持的浏览器还很少，故使用frameborder属性 -->
					<!-- <div title="库存信息">
						<div class="stock_dtgrid_wap">
							<table id="stock_dtgrid" class="item_view_table"></table>
						</div>
					</div> -->
					<div title="图片信息">
						<div class="prod_img_list">
							<div class="main_img_wap">
								<h4>产品主图片</h4>
								<div class="main_img_content"></div>
							</div>
							<div class="des_img_wap">
								<h4>产品描述图片</h4>
								<div class="des_img_content"></div>
							</div>
						</div>
					</div>
					<div title="附件信息">
						<div class="des_attachment_content"></div>
					</div>
					<!-- <div title="审核动态">
					</div> -->
				</div>
			</div>
			
		</div>
	</div>
</body>
</html>