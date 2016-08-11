<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">
<link rel="stylesheet" type="text/css" href="web/css/common/easyui.css">
<link rel="stylesheet" type="text/css" href="web/css/common/reset.css">
<link rel="stylesheet" type="text/css" href="web/css/common/icon.css">
<link rel="stylesheet" type="text/css" href="web/css/common/public.css">
<link rel="stylesheet" type="text/css" href="web/css/common/templatePage.css">
<link rel="stylesheet" type="text/css" href="web/css/common/iconfont.css"> 
<!-- <link rel="stylesheet" type="text/css" href="web/css/common/font-awesome.min.css"> -->
<!-- <link rel="stylesheet"  type="text/css" href="web/js/magiczoom/magiczoom.css"/> -->
<script src="web/js/common/jquery-1.11.1.js" charset="utf-8"></script>
<script src="web/js/common/jquery.easyui.min.js" charset="utf-8"></script>
<script src="web/js/common/pagination-common.js" charset="utf-8"></script>
<script src="web/js/common/jquery-cookie.js" charset="utf-8"></script>
<script src="web/js/common/easyui-lang-zh_CN.js" charset="utf-8"></script>
<script src="web/js/common/moveBeyond.js" charset="utf-8"></script>
<script src="web/js/common/dynamic-table.js" charset="utf-8"></script>
<script src="web/js/common/jquery.MultiFile.js" charset="utf-8"></script>
<!-- <script src="web/js/common/field-check.js" charset="utf-8"></script> -->
<script src="web/js/common/ui.js" charset="utf-8"></script>
<!-- <script src="web/js/magiczoom/magiczoom.js" type="text/javascript"  charset="utf-8"></script> -->
<script src="web/js/common/easyui-validater.js" charset="utf-8"></script>
<script src="web/js/common/jquery.form.js" charset="utf-8"></script>
<script type="text/javascript">

/*关闭当前查看页面函数*/
function closePage() {
	window.close();
}

/*新打开一个页面*/
function newPage(url){
	window.open(url);
}

/*查询面板 搜索按钮事件*/
function doSearch(va,func) {
	if (va == 13) {
		func();
	}
}


function today(){
	return new Date();
}

function todayDiffMonth(diffMonth){
	var d=new Date(); 
	var vyear=d.getFullYear();
	var vmonth=d.getMonth()+diffMonth; 
	if(vmonth<=-1){ 
		vmonth+= 12; 
		vyear-=1;
	}else if(vmonth>=12){
		vmonth -= 12; 
		vyear+=1;
	}
	var vday = d.getDate(); 
	var val = vyear+"-"+vmonth+"-"+vday; 
	return val; 
}

//打开TAB查看详情页
function openPoViewTab(poID,poNo) {
	parent.addTab('tabId_viewPo', '采购订单' + poNo + '详情', 'po/view/'
			+ poID);
}
//打开TAB查看详情页
function openSoViewTab(saleID,saleNo) {
	parent.addTab('tabId_viewSo','销售订单'+saleNo+'详情','saleManage/saleView.do?saleID=' + saleID);
}

//打开TAB查看详情页
function openDeliveryViewTab(deliveryID,deliveryNo){
	parent.addTab('tabId_viewDelivery','发货单'+deliveryNo+'详情','delivery/view/'+deliveryID);
}

function disWhEntryInfo(entryNo){
	parent.addTab('tabId_whEntryDetail','入库单详情','whEntryManage/getWHEntryDetail.do?whEntryNo='+entryNo,true);
}

/**
 * dialogID dialog对应div的id名称
 */
function createImportDialog(dialogID,easyuiForm,title,url,datagrid,easyuiDialog){
	var excelDialog = $('#'+dialogID).show().dialog({
		modal : true,
		title : title,
		buttons : [ {
			text : '导入',
			handler : function() {
				easyuiForm.form('submit', {
						url : url,
						success : function(data) {
							var d = $.parseJSON(data);
							if (d.success) {
								$.messager.progress('close');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
								datagrid.datagrid('reload');
									
							}else{
								$.messager.progress('close');
								$.messager.alert('提示',d.msg,'warning');
								datagrid.datagrid('reload');
							}
						}
					});
				easyuiDialog.dialog('close');
				$.messager.progress(); 

			}
		} ]
	}).dialog('close');
	
	return excelDialog;
}

/*var url = 'ncEntryMgr/downExcel.do?fileName=saleTemplate.xlsx&exportName=应付单模板.xlsx';*/
function downloadTemplate(url){
	url=encodeURI(encodeURI(url));
	window.location.href = url;
}
</script>
