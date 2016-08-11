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
.wap_main_pic{
	width:270px;
	float:left;
}
.main_img_content{
	padding:0px 10px;
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
	width:100px;
	height:100px;
	float:left;	
	margin-left:10px;
}
.old_pic ul li span{
	display:inline-block;
}
.old_pic img{
	width:100px;
	height:100px;
}
.main_pic_input_wap{
	display:none;
}
/*添加图像*/
.upload_pic {
	height: 370px;
	/* width: 100%; 如果设置这个再设置边框，就会超出容器*/
	border: 1px solid #ccc;
	border-radius: 5px;
}
.new_pic_upload h4,.new_pic_upload h5{
	padding-left:10px;
}
.main_img_content{
	width:100px;
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
		url:"productManage/getProductPics.do",
		data:{productID:"${productID}"},
		success:function(data){
			if(data.obj.picture.picPath!=null){
				var pics = data.obj.picture.picPath.split(",");
				var desPicContent = "<ul>";
				for(var i = 0;i<pics.length;i++){
					if(i==0){
						$(".main_img_content").html("<img src='"+pics[0]+"' alt='主图片'/><span><input type='button' id='main-pic-btn' value='删除'/></span>");
					}else{
						desPicContent+="<li><img src='"+pics[i]+"' alt='描述图片'/><span><input type='button' value='删除'/></span></li>";
					}
				}
				desPicContent += "</ul>";
				$(".des_img_content").html(desPicContent);
				$(".des_img_content :button").click(function(){
					var $this =$(this);
					$.messager.confirm("提示","确定要删除这样图片？",function(r){
						if(r){
							$this.parent().parent().remove();
						}
					});
				});
				//给按钮绑定删除事件
				if($("#main-pic-btn")){
					$("#main-pic-btn").click(function(){
						var $this =$(this);
						$.messager.confirm("提示","确定要删除这样图片？",function(r){
							if(r){
								$this.parent().parent().remove();
								$(".main_pic_input_wap").show();
							}
							/* $(".des_pic_wap").before("上传主正面图片:<input type='file' name='mainPic' onchange='picOnChange(this)' data-relpic='mainPic' />"); */
						});
					});
					console.log("ssss");
				}else{
					$(".main_pic_input_wap").css("display","block");
				}
			}else{
				$(".main_pic_input_wap").css("display","block");
			}
			
		},
		dataType:"json"});
	
	
});
//简单的验证上传的图片是否合法
function validateUploadImg(obj) {
	var isPic = ["gif", "jpg", "jpeg", "png", "bmp"];
	var filePath = $(obj).val();
	var strTemp = filePath.split(".");
	var strCheck = strTemp[strTemp.length - 1];
	var index = $.inArray(strCheck.toLowerCase(), isPic);
	if (index == -1) {
		return false;
	}
	return true;
}
// 删除图片新增录入框
function deletePic(doc) {
	$.messager.confirm("提示", "是否删除这个图片", function(r) {
				if (r) {
					var grandparent = $(doc).parent("li").parent("ul");// 获取通过this传递过来的元素的直接父元素
					var parentB = $(doc).parent("li").prev()
							.children("input[type='file']");
					var relpic = parentB.data("relpic");
					console.log(relpic);
					var img = $(".pic_display img[name='" + relpic + "']");// 删除按钮的同时
					// 也要删除展示的图片
					if (img != null && img != "undefined") {
						img.remove();
						$(".prod_pic_info img[name='" + relpic + "']").remove();
					}
					grandparent.remove();// 删除该元素及其所有子元素

				}
			});
}
// 新增图片录入标签
function addPic() {
	var nowM = new Date().getTime();
	var addUl = $("#add-pic-btn");
	addUl
			.before("<ul><li class='li_pic_input'><input type='file' name='productPics' onchange='picOnChange(this)' data-relpic='"
					+ nowM
					+ "'/></li>"
					+ "<li class='li_pic_opt'><input type='button' class='sbutton' value='删除' onclick='deletePic(this);' /></li></ul>");
}

// 选择图片之后调用事件 带图片预览
function picOnChange(obj) {
	// 验证图片格式是否合法
	if (!validateUploadImg(obj)) {
		alert("上传图片不合法！请重新上传！");
		return;
	}
	
	
	window.URL = window.URL || window.webkitURL;
	var hasimg = $(".pic_display img[name='" + $(obj).data("relpic") + "']");
	if (hasimg != null && hasimg != "undefined") {
		hasimg.remove();
		$(".prod_pic_info img[name='" + $(obj).data("relpic") + "']").remove();
	}
	var fileList = $(obj).attr("name") == "mainPic"
			? $(".prod_pic").get(0)
			: $(".des_prod_pic").get(0);// 判断是哪个标签调用事件
	/* var disPicDiv = $(obj).attr("name") == "mainPic" ? $(".dis_main_prod_pic")
			.get(0) : $(".dis_des_prod_pic").get(0);// 判断是哪个标签调用事件 */
	var files = obj.files, img = new Image(), img2 = new Image();
	if (window.URL) {
		// File API
		img.src = window.URL.createObjectURL(files[0]); // 创建一个object
		img.name = $(obj).data("relpic"); // URL，并不是你的本地路径
		img.width = 100;
		img.onload = function(e) {
			window.URL.revokeObjectURL(this.src); // 图片加载后，释放object URL
		}
		/* img2.src = window.URL.createObjectURL(files[0]); // 创建一个object
		img2.name = $(obj).data("relpic"); // URL，并不是你的本地路径
		img2.width = 100;
		img2.onload = function(e) {
			window.URL.revokeObjectURL(this.src); // 图片加载后，释放object URL
		}
		omg2 = img;
		disPicDiv.appendChild(img2); */
		fileList.appendChild(img);
	} else if (window.FileReader) {
		// opera不支持createObjectURL/revokeObjectURL方法。我们用FileReader对象来处理
		var reader = new FileReader();
		reader.readAsDataURL(files[0]);
		reader.onload = function(e) {
			img.src = this.result;
			img.width = 100;
			fileList.appendChild(img);

		}
	} else {
		// ie
		obj.select();
		obj.blur();
		var nfile = document.selection.createRange().text;
		document.selection.empty();
		img.src = nfile;
		img.width = 100;
		img.onload = function() {
			alert(nfile + "," + img.fileSize + " bytes");
		}
		fileList.appendChild(img);

		// fileList.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image',src='"+nfile+"')";
	}
}

function submitForm(){
	var paths="";
	if($(".main_pic_input_wap").css("display")!="none"){
		if($("input[name='mainPic']").val()==""){
			$.messager.alert("提示","必须添加主图片");
			return;
		}
	}else{//如果没有删除主图片
		if($(".main_img_content img").attr("src")){
			paths+=$(".main_img_content img").attr("src");
		}
	}
	$(".des_img_content img").each(function(){
		if(paths==""){
			paths+=$(this).attr("src");
		}else{
			paths+=","+$(this).attr("src");
		}
	    });
	console.log(paths);
	$("input[name='oldPics']").val(paths);
	$("#pic-form").submit();
	
	/* 
	var dtd = $.Deferred(); // 新建一个deferred对象
	var wait = function(dtd){
		$("#pic-form").submit();
		//dtd.resolve(); // 改变deferred对象的执行状态
		var tasks = function(){
			dtd.resolve(); // 改变Deferred对象的执行状态
			};
		setTimeout(tasks,5000);
		return dtd;
	};
	$.when(wait(dtd)).done(function(){
		parent.showPromptMessages('提示','产品修改成功！');
		parent.refreshTab('产品信息');
		setInterval(parent.deletePanel('修改产品图片'),500);
	}).fail(function(){ alert("出错啦！"); }); */
}


function closePage(){
	$.messager.confirm('请确认', '您要放弃修改产品图片？', function(r) {
		if (r) {
			parent.deletePanel("修改产品图片");
		}
	});
}
</script>
</head>
<body>
<div class="wap">
<h3><b>更改产品图片</b></h3>
<div class="old_pic">
	<h4>产品前图片：</h4>
	<div class="wap_main_pic">
		<h5>主图片<em>(只有删除原有主图片才能添加新的主图片)</em></h5>
		<div class="main_img_content"></div>
	</div>
	<div class="wap_des_pic">
		<h5>描述图片<em>(在下面增加将不会覆盖原来的描述图片)</em></h5>
		<div class="des_img_content"></div>
	</div>
</div>
<div class=" new_pic_upload">
				<form id="pic-form" enctype="multipart/form-data" action="productManage/updateProductPic.do" method="post">
					<div class="step_title">
						<h5 class="title">上传照片：</h5>
					</div>
					<div class="upload_pic">
					<input type="hidden" name="productID" value="${productID}"/>
					<input type="hidden" name= "oldPics"/>
						<div class="input_upload">
							<div class="main_pic_input_wap">
							上传主正面图片:<input type="file" name="mainPic"
								onchange='picOnChange(this)' data-relpic="mainPic" />
							</div>
							<div class="des_pic_wap">
								<ul>
									<li>上传描述图片:
										<ul>
											<li class="li_pic_input"><input type="file"
												name="productPics"  onchange='picOnChange(this)'
												data-relpic='prod-dec00' /></li>
											<li class="li_pic_opt"><input type="button"
												class="s_d_button" value="删除" onclick="deletePic(this);" /></li>
										</ul>
										<ul id="add-pic-btn">
											<li><input type="button" class="s_c_button" value="增加"
												onclick="addPic();"></li>
										</ul>
									</li>
								</ul>
							</div>
						</div>
						<div class="pic_display">
							<h5>产品主图片：</h5>
							<div class="prod_pic"></div>
							<h5>产品描述图片：</h5>
							<div class="des_prod_pic"></div>
						</div>
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