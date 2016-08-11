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
.search_table input.review_url{
	width:350px;
}
.search_table input.review_num{
	width:80px;
}
.review_display table{
	width:100%;
}
.review_display table tr{
	height:40px;
}
.review_display table th{
	width:80px;
}
.review_display table td.i_star{
	width:80px;
}
.review_display table td.i_review{
	overflow: hidden;
	/* white-space: nowrap;
	text-overflow: ellipsis; */
}
</style>

</head>
<body onkeydown="doSearch(event.keyCode,searchFun)">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar" class="datagrid-toolbar" >
			<div class="search_top">
				<div class="s_top_content">
					<table class="search_table">
						<tr>
							<th>请填写url:</th>
							<td><input class="review_url" name="amazonReviewUrl" /></td>
							<th>页数(10条/页):</th>
							<td><input class="review_num" name="reviewPageNum" /></td>
							<td><div class="advanced_btn_wap">
									<a class="easyui-linkbutton confirmButton" iconCls="icon-search"
										plain="false" onclick="searchFun();" href="javascript:void(0);">获取</a>
									<a id="common-clear-btn" class="easyui-linkbutton" iconCls="icon-sdelete" plain="false" onclick="clearFun($('#datagrid'));"
											href="javascript:void(0);">清空</a>
								</div>
							</td>
							<td><div class="advanced_btn_wap">
									<a id= "copyButton" class="easyui-linkbutton confirmButton" iconCls="icon-approval"
										plain="false" href="javascript:void(0);">复制评论</a>
									<a class="easyui-linkbutton confirmButton" iconCls="icon-ssave"
										plain="false" onclick = "getAndExportReview()" href="javascript:void(0);">获取并导出</a>
								</div>
							</td>
						</tr>
					</table>
				</div><!-- s_top_content -->
			</div><!-- search_top -->
		</div>
		<div id="review-display-wap" class="review_display">
		
		</div>
	</div>
</body>
<script type="text/javascript" src="http://cdn.bootcss.com/zclip/1.1.2/jquery.zclip.js"></script>
<script type="text/javascript">

$(function() {
	// 绑定回车事件
	document.onkeydown = function(e) {
		var ev = document.all ? window.event : e;
		if (ev.keyCode == 13) {// 如（ev.ctrlKey && ev.keyCode==13）为ctrl+Center
								// 触发
			searchFun();
		}
	};
	
	$("#copyButton").zclip({
        path:'http://cdn.bootcss.com/zclip/1.1.2/ZeroClipboard.swf', //记得把ZeroClipboard.swf引入到项目中 
        copy:function(){
        	
        	 var html = '';  
             $('#review-display-wap').find('tr').each(function () {  
                 $(this).find('td').each(function () {  
                     html += $(this).text() + '\t';  
                 })  
                 html += '\n';  
             });
        	console.log(html);
            return html;
        }
    });
});// $(function(){})完
function copyReview(){
	var review=$("#review-display-wap"); 
	review.select(); // 选择对象 
	document.execCommand("Copy"); // 执行浏览器复制命令 
	$.messager.show({
		title : '提示',
		msg : "复制成功"
	});
}

function getAndExportReview(){
	var amazonReviewUrl = $('.search_table input[name=amazonReviewUrl]').val();
	var reviewPageNum = $('.search_table input[name=reviewPageNum]').val();
	if(reviewPageNum){
		window.location.href = 'productManage/getAndExportReview?amazonReviewUrl='+amazonReviewUrl+'&reviewPageNum='+reviewPageNum;
	}else{
		window.location.href = 'productManage/getAndExportReview?amazonReviewUrl='+amazonReviewUrl+'&reviewPageNum=1';
	}
}
function searchFun(){
	$.messager.progress();
	var amazonReviewUrl = $('.search_table input[name=amazonReviewUrl]').val();
	$.post('productManage/getAmazonReview.do',{
		amazonReviewUrl : amazonReviewUrl
	},function(data) {
		console.log(data);
		$.messager.progress('close');
		if(data.success){
			$.messager.show({
						title : '提示',
						msg : data.msg
					});
			var json = data.obj;
			var html = "<table>";
			$.each(json, function (index, item) {  
                //循环获取数据    
                var star = json[index].star;  
                var review = json[index].review;
                html += "<tr><th>评论级别：</th><td class='i_star'>" + star + "星</td><th>评论详情：</th><td class='i_review'>" + review + "</td></tr>"
            });
			html += "</table>";
			$("#review-display-wap").html(html);
		}else{
			$.messager.alert('错误',data.msg);
		}
	},"json");
}

</script>

</html>