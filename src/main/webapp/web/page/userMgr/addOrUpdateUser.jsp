<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<jsp:include page="../include/meta.jsp" />
<jsp:include page="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>

<style>
.content{
	width:100%;
	
	font-size:14px;
}
#userDialog {
	width: 90%;
	margin:10px auto;
	overflow: hidden;
}

.table_form {
	/* display: table-cell; */
	width: 100%;
	height: auto;
	margin: auto;
}
.table_form tbody{
	display:table;/*不加这句chrome显示出错*/
}
.table_form tr {
	width: 300px;
	height: 45px;
	/* padding: 4px; */
	vertical-align:middle;
}

.table_form tr th {
	text-align: right;
	font-weight:normal;
	padding: 0 10px 0 40px;
}

/* .table_form tr td {
	width: 200px;
} */
.table_form .combo{/*覆盖EasyUI的css属性*/
	border-radius:3px;
}
.i_input{
	border: 1px solid #c9c9c9;
    border-radius: 3px;
    font-size: 14px;
    height: 18px;
    padding: 5px;
    width: 200px;
    box-sizing: content-box;
}
.table_form tr td input[type="radio"] {
	width: 30px;
	height: 14px;
}


#foot{
	width:100%;
	border-top: 1px solid #ccc;
}
#foot-button{
	
	padding:5px;
}
.cancelButton{
	letter-spacing:5px;
	font-size:16px;
	
}
.confirmButton{
	letter-spacing:5px;
	font-size:16px;
	
}
.iButton{
	font-size:14px;
	font-weight:bold;
	width:140px;
	height:28px;
	float:left;
	margin-left:30px;
	margin-top:20px;
}
</style>
</head>
<body>
<div class="content">
	<div id="userDialog">
		<form id="infoForm" method="post">
			<input name="userID" type="hidden" <c:if test="${userInfo != null}">value="${userInfo.userID}"</c:if>/>
			<table class="table_form">
				<tr>
					<th>登陆ID:</th>
					<td>
						<input name="loginID" class="i_input  easyui-validatebox" required="true" validType="validateUserName" <c:if test="${userInfo != null}"> value="${userInfo.loginID}"</c:if> />
					</td>
					<th>年龄:</th>
					<td>
					<input name="age" class="i_input easyui-numberbox" data-options="min:0,max:150,width:212,height:30"
							required="true" <c:if test="${userInfo != null}">value="${userInfo.age}"</c:if> />
					</td>
				</tr>
				<tr>
					<th>中文名:</th>
					<td>
						<input name="userCnName" class="i_input  easyui-validatebox" required="true" <c:if test="${userInfo != null}"> value="${userInfo.userCnName}"</c:if>/>
					</td>
					<th>英文名:</th>
					<td>
						<input name="userEnName" class="i_input  easyui-validatebox" required="true" validType="validateUserName" <c:if test="${userInfo != null}">value="${userInfo.userEnName}" </c:if>	/>
					</td> 
				</tr>
				<tr>
					<th>性别:</th>
					<td><input name="gender" type="radio" value="1" checked="checked"
						/>男<input name="gender" type="radio"
						value="0" <c:if test="${userInfo.gender == 0}">checked="checked"</c:if> />女
					</td>
					<th>身份证号:</th>
					<td>
						<input name="idCard" class="i_input  easyui-validatebox" validType="validateIdcard" <c:if test="${userInfo != null}">value="${userInfo.idCard}"  disabled="true"</c:if> />
					</td>
				</tr>
				<tr title="如果不更改密码,请留空!">
					<th>登录密码:</th>
					<td>
						<input name="password" type="password" class="i_input  easyui-validatebox"
    required="required" oninput="changePassword();"<c:if test="${userInfo != null}">value="${userInfo.password}"</c:if>/>
					</td>
					<th>确认密码:</th>
					<td>
						<input name="confirmPassword" type="password" class="i_input  easyui-validatebox"
    required="required" validType="isEquals" disabled="disabled" <c:if test="${userInfo != null}">value="${userInfo.password}"</c:if>/>
					</td>
					
				</tr>
				<tr title="拥有角色">
				<th>email:</th>
					<td><input name="email" class="i_input  easyui-validatebox"
						<c:if test="${userInfo != null}">value="${userInfo.email}"</c:if>/></td>
					<th>联系地址:</th>
					<td><input name="address" class="i_input" <c:if test="${userInfo != null}">value="${userInfo.address}"</c:if>/></td>
				</tr>

				<tr>
					<th>部门代号:</th>
					<td><input name="departmentID" class="i_input"/></td>
					<th>家庭地址:</th>
					<td><input name="homeAddr" class="i_input" <c:if test="${userInfo != null}">value="${userInfo.homeAddr}"</c:if>/></td>
				</tr>
				<tr>
					<th>部门职务:</th>
					<td>
						<input name="departJob" class="i_input" class="i_input  easyui-validatebox" <c:if test="${userInfo != null}"> value="${userInfo.departJob}"</c:if>/>
					</td>
						
					<th>联系电话:</th>
					<td><input name="telephone" class="i_input  easyui-validatebox" validType="validatePhone"
						<c:if test="${userInfo != null}">value="${userInfo.telephone}"</c:if>/></td>
				</tr>
				<tr>
					<th>所属角色:</th>
					<td><input name="roleID" id="roleId" disabled="disabled" class="i_input" /></td>
					<th>是否可用:</th>
					<td><input name="isEnable" type="radio" value="1" checked="checked"
						 />启用<input name="isEnable" type="radio"
						value="0" <c:if test="${userInfo.isEnable == 0}">checked="checked"</c:if>/>不启用</td>
				</tr>
			
			</table>
		</form>
	</div>
</div>
	<div id="foot">
		<div id="foot-button"><input class="easyui-linkbutton iButton cancelButton" onclick="closePage();" value="取消"/><input class="easyui-linkbutton iButton confirmButton" onclick="submitForm();" value="确定"/></div>
	</div>
</body>

<script type="text/javascript" src="web/js/common/easyui-validater.js"></script>
<script type="text/javascript">
var datagrid;
var userDialog;
var infoForm;
var passwordInput;
$(function(){
	$("input[name='departmentID']").combobox({
		url:'user/getDepartmentList.do?userID='+$("input[name='userID']").val(),
		valueField: 'id',
	    textField: 'text',
		width:212,
		height:30, 
		required:true,
		panelHeight : 100,
		onBeforeLoad:function(param){
		},
		onLoadSuccess:function(){
			var val = $("input[name='userID']").val();
			if(val!=""){
				console.log(val);
				$.ajax({
					type:"post",
					url:'roleManage/getSpecialUserRoles.do',
					data:{userID:val},
					dataType:"text",//如果写为json，如果后台不为json格式，下面的success方法就获取不到数据
					success:function(data){
						var roleInput = $("input[name='roleID']");
						roleInput.attr("disabled",false);
						$("input[name='roleID']").val(data);
						roleInput.attr("disabled",true);
					}
				});
				
			}
		}
	});
});


function closePage(){
	var hasUserID = $("input[name='userID']").val();
	console.log(hasUserID);
	if(hasUserID == ""){
		parent.deletePanel("增加用户");
	}else{
		parent.deletePanel("修改用户");
	}
}
function submitForm(){
	var isValid = $('#infoForm').form('validate');
	if (!isValid){
		return;
	}
	var hasUserID = $("input[name='userID']").val();
	console.log(hasUserID);
	if(hasUserID == ""){
		$.ajax({
			cache: true,
			type: "POST",
			url: 'user/addUser.do',
			data:$('#infoForm').serialize(),
			async: false,
			error: function(request) {
				$.messager.alert('error','添加失败!','error');
			},
			success: function(data) {
				if(data.success){
					parent.showPromptMessages("提示！","用户新增成功！");
					parent.refreshTab("用户管理");
					parent.deletePanel("增加用户");
				}else{
					$.messager.alert({
						msg : "用户新怎失败！",
						title : '提示'
					});
					$('#infoForm').reset(); 
					
				}
			}
		});
	}
	else{
		$.ajax({
			cache: true,
			type: "POST",
			url: 'user/updateUser.do',
			data:$('#infoForm').serialize(),
			async: false,
			error: function(request) {
				$.messager.alert('error','添加失败!','error');
			},
			success: function(data) {
				if(data.success){
					parent.showPromptMessages("提示","用户修改成功！");
					parent.refreshTab("用户管理");
					parent.deletePanel("修改用户");
				}else{
					$.messager.alert({
						msg : "用户新怎失败！",
						title : '提示'
					});
					$('#infoForm').reset(); 
				}
				
			}
		});
	}

}

function changePassword(){
	$("input[name='confirmPassword']").attr("disabled",false);
}
</script>
</html>