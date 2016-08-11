<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:import url="../component/vendorDialog.jsp" />
<c:import url="../component/currencyDialog.jsp" />
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="web/css/common/reset.css">
<style type="text/css">
input::-moz-placeholder {
    color: #b9b9b9;
}
input::-ms-input-placeholder{
    color:#b9b9b9;
}
.top{margin-bottom:10px;}
.top h2{font-size:16px;font-weight:700;}
.top h2 span{font-size:14px;font-weight:500;}
.content{border:1px solid #ccc;margin-bottom:10px;background:#f1f1f1;}
.tableForm tr td input{width:236px;height:16px;margin-bottom:2px;padding:5px; }
.tableForm tr td input[type=radio]{width:50px;}
.tableForm th{text-align:right;padding-right:5px;}
.tableForm textarea{
	font-size: 12px;
    height: 80px;
    padding: 4px;
    width: 100%;
   }
.tableForm{width:900px}
/**/
#easyuiForm .tableForm tr .prompt_unit{
	width:30px;
	color:#ccc;
}
#easyuiForm .tableForm tr .package_size{
	height:100px;
}
#easyuiForm .tableForm tr .package_size span{
	display:block;
	height:22px;
	line-height:22px;
	margin-top:5px;
}
#easyuiForm .tableForm tr .prompt_unit p{
	height:22px;
	line-height:22px;
	margin-top:5px;
}
#easyuiForm .tableForm tr .package_size span input{
	width: 227px;
}
.step_title{font-size:14px;height:35px;line-height:35px;border-bottom:1px solid #ccc;padding-left:10px;margin-bottom:10px;}

/*按钮部分*/
.ibutton{background:#2bab21;color:#fff;}
</style>
</head>
<body  class="bodymargin2">
	<div class="wap">
		<div class="top">
		<c:choose>
			<c:when test="${productInfo != null}">
				<h2>修改产品:<span>${productInfo.skuNo}</span></h2>
			</c:when>
			<c:otherwise>
				<h2>添加产品</h2>
			</c:otherwise>
		</c:choose>
		</div>
		<div class="content">
			<div class="step step01">
				<div class="step_title">
					<h2 class="title">填写产品信息：</h2>
				</div>
				<form id="easyuiForm" method="post">
					<div id="clone4display">
						<input name="productID" type="hidden"
							<c:choose>
								<c:when test="${productInfo != null}">
									value="${productInfo.productID}"
								</c:when>
								<c:otherwise>
									value="-1"
								</c:otherwise>
							</c:choose> />
						<table class="tableForm" cellspacing="10">
							<tr>
								<th>产品编号:</th>
								<td><input name="skuNo" class="easyui-validatebox" required="true"
									<c:if test="${productInfo != null}"> value="${productInfo.skuNo}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
								<th>产品中文名称:</th>
								<td><input name="productCnName" class="easyui-validatebox" required="true"
									<c:if test="${productInfo != null}">value="${productInfo.productCnName}" </c:if> />
								</td>
								<td class="prompt_unit"></td>
							</tr>
							<tr>
								<th>产品英文名称:</th>
								<td><input name="productEnName" class="easyui-validatebox"
									<c:if test="${productInfo != null}"> value="${productInfo.productEnName}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
								<th>产品一级分类:</th>
								<td><input name="productCategory" class="easyui-validatebox" 
									<c:if test="${productInfo != null}"> value="${productInfo.productCategory}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
							</tr>
							<tr>
								<th>产品二级分类:</th>
								<td><input name="twoLevelCategory" class="easyui-validatebox"
									<c:if test="${productInfo != null}"> value="${productInfo.twoLevelCategory}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
								<th>品牌:</th>
								<td><input name="brand" class="easyui-validatebox"
									<c:if test="${productInfo != null}"> value="${productInfo.brand}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
							</tr>
							<tr>
								<th>产品型号:</th>
								<td><input name="productModel" class="easyui-validatebox"
									<c:if test="${productInfo != null}"> value="${productInfo.productModel}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
								<th>供应商编码:</th>
								<td><input name="vendorCode" class="easyui-validatebox"
									<c:if test="${productInfo != null}"> value="${productInfo.vendorCode}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
							</tr>
							<tr>
								<th>保质期:</th>
								<td><input name="shelfLife" class="easyui-validatebox"
									<c:if test="${productInfo != null}"> value="${productInfo.shelfLife}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
								<th>关键字:</th>
								<td><input name="keyword" class="easyui-validatebox"
									<c:if test="${productInfo != null}"> value="${productInfo.keyword}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
							</tr>
							<tr>
								<th>发货时效:</th>
								<td><input name="deliveryTime" class="easyui-validatebox"
									<c:if test="${productInfo != null}"> value="${productInfo.deliveryTime}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
								<th>认证方式:</th>
								<td><input name="authType" class="easyui-validatebox"
									<c:if test="${productInfo != null}"> value="${productInfo.authType}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
							</tr>
							<tr>
								<th>建议零售价格:</th>
								<td><input name="salePrice" class="easyui-validatebox" required="required" placeholder="请输入数字"
									<c:if test="${productInfo != null}">value="${productInfo.salePrice}"</c:if> /></td>
								<td class="prompt_unit">元</td>
								<th>批发价格:</th>
								<td><input name="purchasePrice" class="easyui-validatebox"
									required="required" placeholder="请输入数字"
									<c:if test="${productInfo != null}">value="${productInfo.purchasePrice}"</c:if> />
								</td>
								<td class="prompt_unit">元</td>
							</tr>
							<tr>
								<th><span class="redcolor">*</span>起订量:</th>
								<td><input name="minOrderQty" class="easyui-validatebox"
									required="required" placeholder="请输入数字" 
									<c:if test="${productInfo != null}">value="${productInfo.minOrderQty}"</c:if> />
								</td>
								<td class="prompt_unit">个</td>
								<th>打包后长度:</th>
								<td><input name="packageLength" class="easyui-validatebox"
									required="required" placeholder="请输入数字" 
									<c:if test="${productInfo != null}">value="${productInfo.packageLength}"</c:if> />
								</td>
								<td class="prompt_unit">cm</td>
							</tr>
							<tr>
								<th>打包后宽度:</th>
								<td><input name="packageWidth" class="easyui-validatebox"
									required="required" placeholder="请输入数字" 
									<c:if test="${productInfo != null}">value="${productInfo.packageWidth}"</c:if> />
								</td>
								<td class="prompt_unit">cm</td>
								<th>打包后高度:</th>
								<td><input name="packageHeight" class="easyui-validatebox"
									required="required" placeholder="请输入数字"
									<c:if test="${productInfo != null}">value="${productInfo.packageHeight}"</c:if> />
								</td>
								<td class="prompt_unit">cm</td>
							</tr>
							<tr>
								<th>净重:</th>
								<td><input name="beforePackWeight" class="easyui-validatebox"
								<c:if test="${productInfo != null}">value="${productInfo.beforePackWeight}"</c:if> /></td>
								<td class="prompt_unit">kg</td>
								<th>打包后重量:</th>
								<td><input name="afterPackWeight" class="easyui-validatebox"
								<c:if test="${productInfo != null}">value="${productInfo.afterPackWeight}"</c:if> /></td>
								<td class="prompt_unit">kg</td>
							</tr>
							<tr>
								<th>供货单位:</th>
								<td><input name="vendorName" class="easyui-validatebox"
									<c:if test="${productInfo != null}"> value="${productInfo.vendorName}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
								<th>发货地址:</th>
								<td><input name="address" class="easyui-validatebox"
									<c:if test="${productInfo != null}"> value="${productInfo.address}"</c:if> />
								</td>
								<td class="prompt_unit"></td>
							</tr>
							<tr>
								<th><span class="redcolor">*</span>产品参数:</th>
								<td colspan="5"><textarea name="specParam" placeholder="产品描述不能少于20个字"><c:if test="${productInfo != null}">${productInfo.specParam}</c:if></textarea></td>
							</tr>
							<tr>
								<th><span class="redcolor">*</span>包装清单:</th>
								<td colspan="5"><textarea name="packageList" placeholder="产品描述不能少于20个字"><c:if test="${productInfo != null}">${productInfo.packageList}</c:if></textarea></td>
							</tr>
							<tr>
								<th><span class="redcolor">*</span>产品描述:</th>
								<td colspan="5"><textarea name="description" placeholder="产品描述不能少于20个字"><c:if test="${productInfo != null}">${productInfo.description}</c:if></textarea></td>
							</tr>
							
						</table>
					</div>
				</form>
			</div>
		</div><!-- content -->
		<div class="bottom">
			<div class="btns">
				<input class="easyui-linkbutton iButton cancelButton"
					onclick="closePage();" type="button" value="取消" />
				<input id="nextStep"
					class="easyui-linkbutton iButton confirmButton nextStep"
					data-type="one" type="button" onclick="submitForm();" value="确认" />
			</div>
		</div>
	</div>
	<!-- wap -->
</body>
<script type="text/javascript">
/* var easyuiForm;
var bfProductID;
$(function() {
	
	easyuiForm = $("#easyuiForm").form();
}); */

// 提交表单
function submitForm() {
	var bfProductID = $("input[name='productID']").val();
	var eleArray = $('#easyuiForm').serializeArray();
	if(bfProductID>0){
		$.post("productManage/updateProduct.do", eleArray, function(data) {// 提交基本的产品信息
					if (data.success) {
						parent.showPromptMessages("提示","修改成功！");
						parent.refreshTab("产品信息");
						parent.deletePanel("修改产品");
						
					}
				}, "json");
	}else{
		$.post("productManage/addProduct.do", eleArray, function(data) {// 提交基本的产品信息
			if (data.success) {
				parent.showPromptMessages("提示","增加成功！");
				parent.refreshTab("产品信息");
				parent.deletePanel("增加产品");
				
			}
		}, "json");
	}
	

}
// 关闭产品增加页面
function closePage() {
	$.messager.confirm('请确认', '您要放弃修改产品？', function(r) {
				if (r) {
					parent.deletePanel("修改产品");
				}
			});
}

</script>

</html>