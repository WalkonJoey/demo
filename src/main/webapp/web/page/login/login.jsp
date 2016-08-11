<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:import url="../include/meta.jsp"></c:import>
<c:import url="../include/easyui.jsp"></c:import>

<!DOCTYPE html>
<html>
<head>
<title>产品管理小工具</title>
<link rel="shortcut icon" href="web/images/main/favicon.ico"/>
<link rel="bookmark" href="web/images/main/favicon.ico"/>
<style type="text/css">
.erp-login {
	background: #efefef;
	padding: 0;
	margin: 0;
	font: normal 12px/24px "微软雅黑"
}

.erp-login .login-bg h4 {
	padding: 0;
	margin: 0;
	height: 30px;
	line-height: 30px;
	font-size: 18px;
	color: #2d442e;
	font-weight: normal;
	border-bottom: 1px solid #86a887;
	padding-bottom: 10px;
}

.erp-login .header {
	padding-top: 50px;
	text-align: left;
	height: 50px;
	line-height: 50px;
	font-size: 24px;
	padding-bottom: 20px;
}

.erp-login .footer {
	padding: 20px 0;
	color: #666;
	text-align: center
}

.erp-login .box-center {
	width: 720px;
	margin: 0 auto;
	overflow: hidden
}

.erp-login .login-bg {
	background: #9ec9a0;
	height: 350px;
}

.erp-login .login-form {
	background: #b9d8ba;
	width: 330px;
	height: 255px;
	border-radius: 10px;
	padding: 20px;
}

.erp-login  label {
	font-size: 14px;
	display: inline-block;
	width: 60px;
	color: #2d442e;
	margin-left: 10px;
	text-align: right;
	padding-right: 10px;
}

.erp-login .login-form {
	float: right;
	margin-top: 20px;
}

.erp-login .login-form div {
	margin-top: 10px;
}

.erp-login .login-form  .scode {
	width: 120px;
}

.erp-login .login-form input {
	width: 205px;
	height: 18px;
	padding:6px;
	box-sizing: content-box;
	border-radius:4px;
	border: 1px solid #9ec9a0;
	background: #f1f1f1;
}

.erp-login .login-form .login-btn {
	height: 35px;
	width: 218px;
	margin-left: 80px;
	background: #f1f1f1;
	border: 1px solid #ccc;
	border-radius: 4px;
}

.erp-login .login-form .login-btn:focus {
	background: #d8d8d8;
}

.erp-login .top-side {
	padding-top: 20px;
}

.erp-login .login-img {
	background: url("web/images/userlogin/login-img.jpg") center no-repeat;
	width: 187px;
	height: 244px;
	float: left;
	margin: 30px 0 0 50px;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		var loginName = $('loginName');
		var password = $('password');

		//查询cookie中是否存在记住的用户名和密码
		/* var re_loginName = $.cookie('re_loginName');
		var re_password = $.cookie('re_password');
		if(re_loginName!=null){
			loginName.value = re_loginName;
			if(re_password!=null){
				password.value = re_password;
				document.getElementById('remember').checked= true;
			}
		} */
	});
	function toSubmit() {
		$('#loginForm').submit();
	}
	/* $(document).ready(function() {
		$("#IbtnEnter").click(function() {
			("#submitBtnId").click();
		});

		var loginName = document.getElementById('loginName');
		var password = document.getElementById('password');

		//查询cookie中是否存在记住的用户名和密码
		var re_loginName = $.cookie('re_loginName');
		var re_password = $.cookie('re_password');
		if (re_loginName != null) {
			loginName.value = re_loginName;
			if (re_password != null) {
				password.value = re_password;
				document.getElementById('remember').checked = true;
			}
		}
	}); */
	function loginBtn(va) {
		if (va == 13) {
			$('#loginForm').submit();
		}
	}
	
	/*   function loadimage(){
	    //document.getElementById("randomImage").src = "CheckImage";
	  } */
</script>
</head>
<body class="erp-login" onkeydown="loginBtn(event.keyCode)">
	<div class="box-center header">产品管理小工具</div>
	<div class="login-bg">
		<div class="box-center">
			<div class="login-img"></div>
			<form id="loginForm" action="loginManage/login" method="post">
				<div id=user_login class="login-form">
					<h4>登录</h4>
					<div class="top-side">
						<span id=ValrUserName style="display: none; color: red"><c:out value="${result}"></c:out></span>
						<label>账号：</label><input type="text" value="" placeholder=""
							name="user.loginName" />
					</div>
					<div>
						<label>密码：</label><input type="password" value="" placeholder=""
							name="user.password" />
					</div>
					<div>
						<label>验证码：</label><input type="text" value="" placeholder="" name="imputtedDigits"
							class="scode" value="<c:out value="${checkImg}"></c:out>"/>
					<img id="randomImage" src="CheckImg" width="79" align="absmiddle"/>
					</div>
					<div>
					<button id="login" class="login-btn"  onClick="toSubmit();">登   录</button>
						<!-- <input id="login" type="button" class="login-btn" value="登   录"
							onClick="toSubmit();" /> -->
					</div>
				</div>
				<script type="text/javascript">
					var result = "${result}";
					if (result != "OK") {
						$('#ValrUserName').css('display', 'block');
					}
					result = null;
				</script>
			</form>

		</div>
	</div>
	<div class="box-center footer">© 2015-2016 华强国际创客中心. All Rights Reserved. v1.0 beta1</div>
</body>
</html>