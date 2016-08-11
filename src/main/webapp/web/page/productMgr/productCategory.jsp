<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.l-btn-icon{/*iconfont*/
	font-family:"iconfont" !important;
  	font-size:16px;
  	font-style:normal;
 	-webkit-font-smoothing: antialiased;
  	-webkit-text-stroke-width: 0.2px;
 	-moz-osx-font-smoothing: grayscale;
}
/*点击分类 显示分类对应关联属性*/
.p_lable{
	/* border-top:1px dashed #ccc; */
	font-weight:bold;
	height:25px;
	line-height:25px;
}
.rel_attr_checkbox{
	padding-left:20px;
}
.attr_name_dis_ctrl{
	display:inline-block;
	padding:4px 8px;
	color:green;
}
.atrr_commet_wap{
	padding:10px 20px;
}
.category_comment_wap{
	padding:10px 20px;
	overflow:hidden;
}
.category_comment_wap div{
	margin-top:10px;
}
.category_comment_wap h3{
	font-weight:bold;
}
.category_comment_contnt{
	text-indent:2em;
	padding-left:10px;
	height:25px;
	line-height:25px;
}

/*新增分类*/
.cg_name_wap{
	padding:4px 0px;
}
.s_lable{
	height:24px;
	line-height:24px;
}
.cg_name_content{
	height:24px;
	line-height:24px;
}
.cg_name_content input{
	padding:5px;
}
.rel_attr_checkbox span{
	display: inline-block;
    font-size: 13px;
    height: 28px;
    line-height: 24px;
}
.rel_attr_checkbox input{
	margin-left: 25px;
    width: 20px;
}
.ad_ctgy_comment_wap h3{
	height:24px;
	line-height:24px;
	font-weight:bold;
	width:50px;
	float:left;
}
.ad_ctgy_comment_wap{
	border-top:1px dashed #ccc;
	margin-bottom:20px;
	padding:4px 0px;
	border-bottom:1px dashed #ccc;
}
.ad_ctgy_comment_wap textarea{
	height:60px;
	width:300px;
}



/**/
.step01_left{ float:left;width:280px;border-right:1px solid #ccc;}
.step01_tree{
	height:100%;
	overflow:auto;
}
.step01_right{
	float:left;
	width:680px;
	margin-left:20px;
}
.probody{
	border: 1px solid #ccc;
    margin: 0 20px 31px;
    overflow: hidden;}

</style>
<script type="text/javascript" charset="UTF-8">
var ptCateloryTree;
var dNameImportDg;
var dNameDg;
var dNameForm;
var excelForm;
$(function(){
	dNameForm = $("#dNameForm").form();
	excelForm = $("#dynCForm").form();
	dNameDg = $("#dNameDg").show().dialog({
		modal : true,
		width:350,
		height:200,
		title : '修改申报名称',
		buttons : [ {
			text : '确定',
			handler : function() {
					dNameForm.form('submit', {
						url : 'categoryManage/updDeclareName.do',
						success : function(data) {
							ptCateloryTree.tree('reload');
							dNameDg.dialog('close');
							$.messager.show({
								msg : "修改申报名称成功！",
								title : '提示'
							});
						}
					});
			}
		} ]
	}).dialog('close');
	$('#file').MultiFile({
		maxlength: '1',
		accept:'xls|xlsx',
	    STRING: {
	    	remove:'删除',
	    	selected:'Selecionado: $file',
	   		denied:'不支持上传该文件类型 $ext!'
	    }
	});
	
	//创建easyui dialog控件
	dNameImportDg = $('#dNameImportDg').show().dialog({
		modal : true,
		title : '导入申报名称',
		buttons : [ {
			text : '导入',
			handler : function() {
				excelForm.form('submit', {
						url : 'categoryManage/batchUpdDeclareName.do',
						success : function(data) {
							var d = $.parseJSON(data);
							if (d.success) {
								$.messager.progress('close');
								$.messager.show({
									title : '提示',
									msg : d.msg
								});
								ptCateloryTree.tree('reload');
									
							}else{
								$.messager.progress('close');
								$.messager.alert('提示',d.msg,'warning');
								ptCateloryTree.tree('reload');
							}
						}
					});
				dNameImportDg.dialog('close');
				$.messager.progress(); 

			}
		} ]
	}).dialog('close');
	ptCateloryTree = $('#ptCateloryTree').tree({
		url:'categoryManage/getPtCateloryTree.do',
			method : 'get',
			animate : true,
			checkbox : false,
			onSelect:function(node){
				
				if($(".form_wap").css("display")=="block"){
					$.messager.confirm('提示信息', '您确定放弃编辑？', function(r){
						if (r){
							if(isSecondLayer(node)){
								$("#click-commt-contnt").text(node.attributes.comment);
								$("#declare-name-contnt").text(node.attributes.declareName);
								$("#category-code-contnt").text(node.id);
								$.ajax({
									type: "post",
							        url: "categoryManage/getCategoryInfo.do",
							        data: {categoryID:node.id},
							        dataType: "json",
							        success: function(data){
							        	if(data.success){
							        		$(".attr_name_dis_ctrl").hide();
							        		var attributesIds = data.obj.attributesIds;
							        		for(var i = 0; i < attributesIds.length; i++){
							    				/* $(":checkbox[name='attrID'][value='"+attributesIds[i]+"']").prop("checked",true); */
							    				$("."+attributesIds[i]).show();
							    			}
							        	}
							        }
								});
								blockBox("comment");
							}else{
								blockBox("none");
							}
						}
					});
				}else{
					if(isSecondLayer(node)){
						$("#click-commt-contnt").text(node.attributes.comment);
						$("#declare-name-contnt").text(node.attributes.declareName);
						$("#category-name-contnt").text(node.text);
						$("#category-code-contnt").text(node.id);
						$.ajax({
							type: "post",
					        url: "categoryManage/getCategoryInfo.do",
					        data: {categoryID:node.id},
					        dataType: "json",
					        success: function(data){
					        	if(data.success){
					        		$(".attr_name_dis_ctrl").hide();
					        		var attributesIds = data.obj.attributesIds;
					        		for(var i = 0; i < attributesIds.length; i++){
					    				$("."+attributesIds[i]).show();
					    			}
					        	}
					        }
						});
						blockBox("comment");
					}else{
						blockBox("none");
					}
				}
			},
			onClick: function (node) {
               
            }
		});
	
	
	$("input[name='vendorName']").combobox({
		url:'userManage/getDepartmentList.do',
		valueField: 'id',
	    textField: 'text',
		width:180,
		height:25,
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

function isSecondLayer(node){
	//var parentNode = ptCateloryTree.tree("getParent",node.target);
	if(node.id=="0"){//判断此节点是否是根节点
		return false;
	}/* else if(parentNode.id=="0"){//判断是否是大分类
		return false;
	} */
	return true;
}

function appendMenu(){
	$("input[name='categoryParentID']").val("0");
	blockBox("form");
	$(".rel_attr_wap").hide();
}
function append(){
	var selectedNode = ptCateloryTree.tree("getSelected");
	if(selectedNode==null||selectedNode=="undefined"){
		$.messager.alert('警告', '请选择一个大分类！！');
		return;
	}
	/* else if(selectedNode.id=="0"){
		$.messager.alert('警告', '目录下不允许创建小分类，请先创建大分类！');
		return;
	} */
	var parentNode = ptCateloryTree.tree("getParent",selectedNode.target);
	if(parentNode!=null&&parentNode.id!="0"){
		$.messager.alert('警告', '不能在小分类上创建分类！');
		return
	}
	if(selectedNode!=null&&selectedNode!="undefined"){
		blockBox("form");
		$("input[name='categoryParentID']").val(selectedNode.id);
	}
	else{
		$.messager.alert('请确认', '请选择一个节点！');
	}
}
function deleteCategory(){
	var selectedNode = ptCateloryTree.tree("getSelected");
	if(!ptCateloryTree.tree("isLeaf",selectedNode.target)){
		$.messager.alert('警告', '请先删除叶子节点！');
		return;
	}
	if(selectedNode!=null&&selectedNode!="undefined"){
		$.messager.confirm('确认对话框', '您想要删除'+selectedNode.text+'分类吗？', function(r){
			if (r){
				$.ajax({
					type:"post",
					url:'categoryManage/deleteCategory.do',
					data:{categoryID:selectedNode.id},
					dataType:"json",//如果写为json，如果后台不为json格式，下面的success方法就获取不到数据
					success:function(data){
						if(data.success){
							ptCateloryTree.tree("reload");
							parent.showPromptMessages("提示","删除成功！");
							blockBox("none");
						}
					}
				});
			}
		});
	}
	else{
		$.messager.alert('请确认', '请选择一个节点！');
	}
}
function edit(){
	var selectedNode = ptCateloryTree.tree("getSelected");
	var parentNode = ptCateloryTree.tree("getParent",selectedNode.target);
	var isLeaf = ptCateloryTree.tree("isLeaf",selectedNode.target);
	console.log(isLeaf);
	if(selectedNode!=null&&selectedNode!="undefined"){
		$("input[name='categoryID']").val(selectedNode.id);
		if(parentNode!=null&&parentNode!="undefined"){
			$("input[name='categoryParentID']").val(parentNode.id);
		}
		else{
			return;
		}
			blockBox("form");
			//$(".rel_attr_wap").show();
			$.ajax({
				type: "post",
		        url: "categoryManage/getCategoryInfo.do",
		        data: {categoryID:selectedNode.id},
		        dataType: "json",
		        success: function(data){
		        	if(data.success){
		        		$("input[name='categoryCnName']").val(data.obj.categoryCnName);
		        		$("input[name='categoryCode']").val(data.obj.categoryCode);
		        		$("textarea[name='comment']").val(data.obj.comment);
		        		var attributesIds = data.obj.attributesIds;
		        		for(var i = 0; i < attributesIds.length; i++){
		        			console.log(attributesIds[i]);
		    				$(":checkbox[name='attrID'][value='"+attributesIds[i]+"']").prop("checked",true);
		    			}
		        	}
		        }
			});
			$(".rel_attr_wap").show();
		}
	//}
	else{
		$.messager.alert('请确认', '请选择一个节点！');
	}
}


function blockBox(who){
	if("comment"==who){
		$(".step01_right").show();
		$(".comment").show();
		$(".rel_attr_wap").show();
		$(".form_wap").hide();
	} 
	else if("form"==who){
		$("#easyuiForm")[0].reset();
		$(":checkbox[name='attrID']").prop("checked",false);
		$(".rel_attr_wap").show();
		$(".step01_right").show();
		$(".comment").hide();
		$(".form_wap").show();
		$(":checkbox[name='attrID'][value='4']").prop("checked",true).parent().hide();//默认选中发光颜色属性
	}
	else if("none"){
		$(".form_wap").hide();
		$(".step01_right").hide();
	}
	
}
function submitForm(){
	var categoryID = $("input[name='categoryID']").val();
	var categoryCode = $("input[name='categoryCode']").val();
	if(categoryID=="-1"){
		$.ajax({
			type: "post",
	        url: "categoryManage/addCategory.do",
	        data: $('#easyuiForm').serialize(),
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		ptCateloryTree.tree("reload");
	        		blockBox("none");
	        		parent.showPromptMessages("提示","添加成功！");
	        	}
	        }
		});
	}
	else{
		$.ajax({
			type: "post",
	        url: "categoryManage/updateCategory.do",
	        data: $('#easyuiForm').serialize(),
	        dataType: "json",
	        success: function(data){
	        	if(data.success){
	        		var node = ptCateloryTree.tree('find', parseInt(categoryCode));
	        		blockBox("comment");
	        		ptCateloryTree.tree('select', node.target);
	        		parent.showPromptMessages("提示","添加成功！");
	        		ptCateloryTree.tree("reload");
	        	}
	        }
		});
	}
}

function cancelOpt(){
	$.messager.confirm('确认取消', '目前数据将不会保存，是否取消？', function(r){
		if (r){
			parent.refreshTab("产品分类");
		}
	});
}

function updDeclareName(){
	var selectedNode = ptCateloryTree.tree("getSelected");
	var parentNode = ptCateloryTree.tree("getParent",selectedNode.target);
	if(selectedNode){
		if(parentNode){
			dNameForm.form('clear');
			dNameDg.dialog('open');
			dNameForm.form('load', {
				categoryCode : selectedNode.id,
				declareName : selectedNode.attributes.declareName
			});
		}else{
			$.messager.alert('请确认', '请选择二级分类！');
		}
	}else{
		$.messager.alert('请确认', '请选择一个节点！');
	}
}
function batchUpdDeclareName(){
	dNameImportDg.dialog("open");
}
</script>
</head>
<body class="probody">
	<div class="wap">
		<div class="top">
			<div id="toolbar" class="datagrid-toolbar">
				<div>
					<!-- <a class="easyui-linkbutton" iconCls="icon-add"
						onclick="append();" plain="true" href="javascript:void(0);">新增大类</a> -->
					<!-- <a class="easyui-linkbutton" iconCls="icon-jia1" onclick="append();"
						plain="true" href="javascript:void(0);">新增分类</a>
					<a class="easyui-linkbutton" iconCls="icon-bianji" onclick="edit()" plain="true"
						href="javascript:void(0);">编辑</a>
					<a class="easyui-linkbutton" iconCls="icon-jian1"
						onclick="deleteCategory();" plain="true" href="javascript:void(0);">删除</a> -->
					<a class="easyui-linkbutton" iconCls="icon-zuixiabu" onclick="ptCateloryTree.tree('expandAll');"
						plain="true" href="javascript:void(0);">展开所有节点</a>
					<a class="easyui-linkbutton" iconCls="icon-zuishangbu"
						onclick="ptCateloryTree.tree('collapseAll');" plain="true"
						href="javascript:void(0);">折叠所有节点</a>
					<a class="easyui-linkbutton" iconCls="icon-xiugai01"
						onclick="updDeclareName();" plain="true" href="javascript:void(0);">修改申报名称</a>
					<a class="easyui-linkbutton" iconCls="icon-daoru"
						onclick="batchUpdDeclareName();" plain="true" href="javascript:void(0);">批量修改申报名称</a>
				</div>
			</div>
		</div>
		<!-- top -->
		<div class="content">
			<div class="step step01">
				<div class="step01_left">
					<div class="step01_tree" id="ptCateloryTree"></div>
				</div>
				<div class="step01_right" style="display:none;">
					<div class="comment">
						<!-- <span class="comment_content">这里显示提示信息！</span> -->
						<div class="rel_attr_wap commet_attr_wap">
							<div class="atrr_commet_wap">
								<p class="p_lable">关联属性:</p>
								<div class="rel_attr_checkbox">
									<c:forEach var="attr" items="${attrList}">
										<span class="attr_name_dis_ctrl ${attr.attrID}">
											${attr.attrName} </span>
									</c:forEach>
								</div>
							</div>
							
							<div class="category_comment_wap">
								<div><span>分类编码：</span><span id="category-code-contnt" class="category_code_contnt"></span></div>
								<div><span>分类名称：</span><span id="category-name-contnt" class="category_name_contnt"></span></div>
								<div><span>申报名称：</span><span id="declare-name-contnt" class="declare_name_contnt"></span></div>
								<div><span>备注：</span><span id="click-commt-contnt" class="category_comment_contnt"></span></div>
							</div>
						</div>
					</div>
					<div class="form_wap" style="display: none;">
						<form id="easyuiForm" method="post">
							<input name="categoryID" type="hidden" value="-1" /> <input
								name="categoryParentID" type="hidden" /><!--  <input
								name="categoryCode" type="hidden" value="-1" /> -->
							<div class="cg_name_wap">
								<span class="s_lable">分类名称:</span> <span class="cg_name_content"><input
									name="categoryCnName" class="easyui-validatebox"
									required="true"/></span>
								<span class="s_lable">分类编码:</span> <span class="cg_name_content"><input
									name="categoryCode" class="easyui-validatebox"
									required="true"/></span>
							</div>
							<div class="rel_attr_wap">
								<p class="p_lable">关联属性:（默认选中发光颜色属性）</p>
								<div class="rel_attr_checkbox">
									<c:forEach var="attr" items="${attrList}">
										<span> <input type="checkbox" value="${attr.attrID}"
											name="attrID" />${attr.attrName}
										</span>
									</c:forEach>
								</div>
								<div class="ad_ctgy_comment_wap">
									<h3>备注：</h3>
									<div class="ad_ctgy_comment_contnt">
										<textarea name="comment"></textarea>
									</div>
								</div>
							</div>
						</form>
						<div class="bottom">
							<div class="btns">
								<input class="easyui-linkbutton iButton cancelButton"
									onclick="cancelOpt();" type="button" value="取消" /> <input
									id="nextStep" class="easyui-linkbutton iButton confirmButton"
									type="button" onclick="submitForm();" value="确定" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- content -->
	<div style="clear:both;height:1px;overflow:hidden;"></div>
	<div id="dNameImportDg">
		<form id="dynCForm" method="post" enctype="multipart/form-data">
			<table class="tableForm">
				<tr align="center">
					<td>Excel选择:<img class="help" title="文件格式限定.xls/xlsx"
						src="web/images/common/help.gif" /></td>
					<td><input type="file" name="file" id="file"
						onkeypress="return false" /></td>
				</tr>
			</table>
			<div><a href="productManage/downloadTemplate.do?fileName=dynCategoryTemplate.xlsx">下载模板</a><h3 style="text-indent:2em;font-size:16px;color:red;">可修改字段详情见模板的第二页，注意区分大小写.</h3></div>
		</form>
	</div>
	<div id="dNameDg">
		<form id="dNameForm" method="post" enctype="multipart/form-data">
			<table class="dlog_tb_form">
				<tr align="center">
					<th>分类编码:</th>
					<td><input type="text" name="categoryCode" class="i_input"/></td>
				</tr>
				<tr align="center">
					<th>申报名称:</th>
					<td><input type="text" name="declareName" class="i_input"/></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>