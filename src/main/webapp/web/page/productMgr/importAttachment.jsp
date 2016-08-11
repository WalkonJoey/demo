<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../include/meta.jsp"></jsp:include>
<jsp:include page="../include/easyui.jsp"></jsp:include>
<style>
.wap h3{
    background: linear-gradient(to bottom, #eee 0px, #ccc 100%) repeat scroll 0 0 rgba(0, 0, 0, 0);
    border-bottom: 1px solid #ccc;
    height: 35px;
    line-height: 35px;
    margin: 0;
    padding-left: 20px;
}
/*old_pic*/
.old_pic{
	height:165px;
	border:1px solid #ccc;
}
.old_pic h4,.old_pic h5{
	padding-left:10px;
}

.old_pic h5 em{
	font-size:10px;
	color:red;
}
.wap_des_pic{
	float:left;
	margin-left:20px;
}
.old_pic ul li{
	float:left;	
	margin-right:30px;
}
.old_pic ul li span{
	display:inline-block;
	margin-left:10px;
}
.main_pic_input_wap{
	display:none;
}
/*添加图像*/
.upload_pic {
	/* width: 100%; 如果设置这个再设置边框，就会超出容器*/
	min-height:50px;
	border: 1px solid #ccc;
	border-radius: 5px;
}
.new_pic_upload h4,.new_pic_upload h5{
	padding-left:10px;
}

.s_c_button{
	background-image: linear-gradient(center top ,  #5fb4ee, #2493de);
	background:-moz-linear-gradient(center top ,  #5fb4ee, #2493de);
	background:-webkit-linear-gradient(center top ,  #5fb4ee, #2493de);
	background:linear-gradient:(center top ,  #5fb4ee, #2493de);
    border: 1px solid #ccc;
    color: #000000;
}
.input_upload {
	float: left;
	width: 300px;
	height: 360px;
	margin: 5px;
	border: 1px solid #ccc;
	border-radius: 5px;
	overflow-y:auto;
	overflow-x:hidden;
}
.des_pic_wap ul li ul{
	height:30px;
}
.des_pic_wap ul li ul li{
	float:left;
}
.li_pic_input{
	width:250px;
}
.li_pic_opt{
	width:40px;
}
.pic_display {
	margin: 5px 5px 5px 320px;
	height: 360px;
	border: 1px solid #ccc;
	border-radius: 5px;
}
.des_prod_pic img{
	margin-left:10px;
}
/* ***********button*************** */
.iButton {
	font-size: 14px;
	font-weight: bold;
	
	width: 140px;
	height: 35px;
	float: left;
	margin-left: 30px;
	margin-top: 5px;
}

.cancelButton {
	letter-spacing: 5px;
	font-size: 16px;
	
}

.confirmButton {
	letter-spacing: 5px;
	font-size: 16px;
	
}

.bottom {
	height: 40px;
	width: 380px;
	margin: 2px auto;
	float:right;
	text-align:center;
}
</style>
<script type="text/javascript" charset="UTF-8">


$(function(){
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
				$(".des_img_content").html(desPicContent);
				$(".des_img_content :button").click(function(){
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
	
	
});
function downloadAttachement(fileUrl,name)
{
	$.messager.confirm('请确认', '确定下载此文件？', function(r) {
		if (r) {
			var url = "productManage/downloadAttachment.do?fileUrl="+fileUrl+"&fileName="+name;
			url=encodeURI(encodeURI(url));
			window.location.href = url;
		}
	});
}
// 删除附件新增录入框
function deletePic(doc) {
	$.messager.confirm("提示", "是否删除这个附件", function(r) {
				if (r) {
					var grandparent = $(doc).parent("li").parent("ul");// 获取通过this传递过来的元素的直接父元素
					var parentB = $(doc).parent("li").prev()
							.children("input[type='file']");
					var relpic = parentB.data("relpic");
					console.log(relpic);
					var img = $(".pic_display img[name='" + relpic + "']");// 删除按钮的同时
					// 也要删除展示的附件
					if (img != null && img != "undefined") {
						img.remove();
						$(".prod_pic_info img[name='" + relpic + "']").remove();
					}
					grandparent.remove();// 删除该元素及其所有子元素

				}
			});
}


function submitForm(){
	var paths="";
	$(".des_img_content a").each(function(){
		if(paths==""){
			paths+=$(this).data("src");
		}else{
			paths+=","+$(this).data("src");
		}
	});
	console.log("cccc"+paths);
	$("input[name='oldPics']").val(paths);
	
	var options = {
			type: 'post',
			dataType:'json',
	        success: function (data) {
	        	console.log("success");
	        	parent.refreshTab("附件维护");
	    }
	};
	// ajaxForm
	$("#pic-form").ajaxForm(options);
	$("#pic-form").ajaxSubmit(options);
	 
	
	/* $("#pic-form").submit(function(){
		console.log("come");
		$(this).ajaxSubmit({
			type: 'post', // 提交方式 get/post
			dataType:'json',
			success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
	            // 此处可对 data 作相关处理
	            console.log("cccc"+data);
	            parent.refreshTab("附件维护");
	        }
		});
		return false;   //阻止表单默认提交  
	}); */
}


function closePage(){
	$.messager.confirm('请确认', '您要放弃修改产品附件？', function(r) {
		if (r) {
			parent.deletePanel("附件维护");
		}
	});
}
</script>
</head>
<body>
<div class="wap">
<h3><b>更改产品附件</b></h3>
<div class="old_pic">
	<h4>产品前附件：</h4>
	<div class="wap_des_pic">
		<h5>描述附件<em>(在下面增加将不会覆盖原来的描述附件)</em></h5>
		<div class="des_img_content"></div>
	</div>
</div>
<div class=" new_pic_upload">
				<form id="pic-form" enctype="multipart/form-data" action="productManage/importAttachments.do" method="post">
					<div class="step_title">
						<h5 class="title">上传附件：</h5>
					</div>
					<div class="upload_pic">
						<input type="hidden" name="productID" value="${productID}"/>
						<input type="hidden" name= "oldPics"/>
						<input type="file" multiple="multiple"
													name="productPics" data-relpic='prod-dec00' />
					</div>
				</form>
			</div>
			<div class="bottom">
				<div class="btns">
					<input class="easyui-linkbutton iButton cancelButton"
						onclick="closePage();" type="button" value="取消" />
					<input id="nextStep"
						class="easyui-linkbutton iButton confirmButton"
						type="button" onclick="submitForm();" value="确定" />
				</div>
			</div>
	</div>
</body>
</html>