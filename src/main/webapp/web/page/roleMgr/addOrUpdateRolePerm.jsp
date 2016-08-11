<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<c:import url="../include/meta.jsp" />
<c:import url="../include/easyui.jsp" />
<%
	String roleId4Perm = (String)request.getAttribute("roleID4Perm");
%>
<!DOCTYPE html>
<html>
<head>
<script>
var dialogCloseFlag = false;
var flag = 0;
var permTree;
var treeNode;
var permForm;
var dataFilterDialog;
var dataFilterDatagrid;
var roleID = "<%=roleId4Perm%>";
$(function(){
	permForm = $('#filter-form').form();
	//创建easyui tree对象
	permTree = $('#rolePermTree').tree({
		url:'role/getRolePermissionTree.do?roleID=<%=roleId4Perm%>',
			method : 'get',
			animate : true,
			checkbox : true,
			onSelect:function(node){
				$(this).tree('check', node.target);
			},
			onCheck:function(node,checked){
				//如果是选中
				if(checked){
					if(node.attributes.enableDataPermission==1){
	                	//清空以前的table元素，并将这个权限所有的数据权限显示出来
	                	initRightArea(node,true);
	                }
	                else{
	                	initRightArea(node,false);
	                } 
				}
			},
			onClick: function (node) {
                
            }
		});
	
	dataFilterDialog = $("#data-filter-dialog").show().dialog({
		modal : true,
		title : '增加数据过滤',
		height:450,
		buttons : '#dialogButton',
		onBeforeClose : function(){
			if(treeNode!=null){
				if(dialogCloseFlag){
					$('#data-filter-grid').datagrid("reload");
					$('.filter-table').empty();//清除table中的所有元素
					return true;
				}
				if(confirm("是否关闭此页面?"))
				{ 
					//$('#data-filter-grid').datagrid("reload");
					$('.filter-table').empty();//清除table中的所有元素
					return true;
				}else{
					return false;
				} 
			}
		},
		onOpen : function(){
			dialogCloseFlag = false;
		}
	}).dialog('close'); 
	
	dataFilterDatagrid = $('#data-filter-grid').datagrid({
		url : null,
		iconCls : 'icon-save',
		fit : true,
		fitColumns : false,

		border : false,
		singleSelect:true,
		idField : 'innerNo',
		columns : [ [ //每个列具体内容 field的内容要与传递过来的json数据中的Key值对应相等
		{
		      field : 'innerNo',
		      title : '编号',
		      width : 60,
		      editor : {
		      	type : 'validatebox',
		      	options : {
		      		required : true
		      	}
		     }
		},{
			field : 'leftParentheses',
			title : '左括号',
			width : 60,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		}, {
			field : 'propertyTitle',
			title : '属性名',
			width : 180,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		},{
			field : 'midOperator',
			title : '判断符',
			width : 60,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		},{
			field : 'propertyValue',
			title : '属性值',
			width : 100,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		},{
			field : 'rightParenthese',
			title : '右括号',
			width : 60,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		},{
			field : 'logical',
			title : '逻辑符',
			width : 80,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		},{
			field : 'operatorName',
			title : '操作人',
			width : 80,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		},{
			field : 'operateDt',
			title : '操作时间',
			width : 120,
			editor : {
				type : 'validatebox',
				options : {
					required : true
				}
			}
		}
		]]
	});
});
//node为树节点   enDataFilter表示是否启用数据过滤
function initRightArea(node,enDataFilter){
	flag=0;
	//判断是否是第一次点击
	//if(treeNode =="" ||treeNode == undefined ||treeNode == null){
	isBlockTag(enDataFilter);
	if(enDataFilter==true){
		treeNode = node;
	    $('.filter-table').attr('id',node.id);
	    //初始化datagrid数据
	    initDatagrid(node);
		return;
	}
	//}
	//判断是否将以前的数据过滤字段删除
	/* $.messager.confirm('确认放弃', '是否清空"'+treeNode.text+'"的数据过滤？如数据需要，先保存！', function(r) {
		if (r) {
			if(enDataFilter == true){
				cancelEdit();
				//获取点击的节点   
	            treeNode = node;
	        	$('.filter-table').attr('id',node.id);
	        	//增加行，并将input转换为combobox
	            initDataFilterRow(node);//这里应该加载以前的数据过滤项
	        	//将对应的按钮变为可见
	        	isBlockTag(true);
			}
			else{
				cancelEdit();
				isBlockTag(false);
			}
		}
	}); */
}
function isBlockTag(isBlock){
	if(isBlock==true){
		$('#noDataFilterMessage').css('display','none');
		$("#data-filter-right").css("display","block");
	}
	else{
		$('noDataFilterMessage').css('display','bolck');
		$("#data-filter-right").css("display","none");
		
	}

}
function initDatagrid(node){
	var gridOpts = dataFilterDatagrid.datagrid('options');
	/* if(gridOpts.url==null||gridOpts.url!=nodeUrl){
		dataFilterDatagrid = 
	} */
    gridOpts.url = 'permManage/getExistDFilterGrid.do?roleID=<%=roleId4Perm%>&permID='+node.id;
	dataFilterDatagrid.datagrid('reload');
}
function addDataFilterRow(){
	var innerFlag=flag;
	console.log($(".filter-table").html());
	$(".filter-table").append('<tr><td class="left_parenthesis"><input class="left_parenthesis'+flag+' id="left_parenthesis'+flag+
		'" /></td><td><input class="attrName" type="text" id="'+treeNode.id+flag+
		'"/></td><td><input class="condition'+flag+' id="condition'+flag+
		'"/></td><td><input style="width:114px" id="attribute'+flag+'" class="attribute"/></td><td class="right_parenthesis"><input class="right_parenthesis'+flag+
		'" type="text"/></td><td class="join_Condition"><input class="expression'+flag+
		'" type="text"/></td><td class="deleteRow" onclick="deleteRow(this);">删除行</td></tr>');
	$('#'+treeNode.id+flag).combotree({
		url : 'permManage/getPermAttriTree.do?permID='+treeNode.id,
		editable : false,
		height : '20',
		width : '220',
		panelHeight : '480',//点击之后下拉菜单的单行高度
		panelWidth : '220',
		formatter:function(node){
			var text = node.text;
			text = text.substring(text.lastIndexOf(".")+1);
			return text;
		},
		onLoadSuccess:function(node, data){
			},
		onSelect : function(node){
			var isLeaf = $(this).tree('isLeaf', node.target);  
		    if (!isLeaf) {  
		        //清除选中  
		        $('#'+treeNode.id+innerFlag).combotree('clear'); 
		    }else{
		    	switch(node.attributes.propType){
				case 'SELECTABLE_VALUE':
					var tdTag = $('#attribute'+innerFlag).parents('td');
					if(tdTag.children('span').length>0){
						$('#attribute'+innerFlag).datetimebox("destroy");
						tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
		    		}
					$('#attribute'+innerFlag).combobox({
						url:'permManage/getValueByAttr.do?permID='+treeNode.id+'&attributeName='+node.id,
						valueField:'id',
						textField:'text',
						width : 110,
						editable : true,
						panelHeight : 100
					});
					break;
				case 'CUSTOM_STRING':
					var tdTag = $('#attribute'+innerFlag).parents('td');
					if(tdTag.children('span').length>0){
						$('#attribute'+innerFlag).datetimebox("destroy");
						tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
		    		}
					$('#attribute'+innerFlag).validatebox({
					    required: true
					});
					break;
				case 'CUSTOM_NUMBER':
					var tdTag = $('#attribute'+innerFlag).parents('td');
					if(tdTag.children('span').length>0){
						$('#attribute'+innerFlag).datetimebox("destroy");
						tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
		    		}
					$('#attribute'+innerFlag).numberbox({
					    required : true,
					    min:0
					});
					break;
				case 'CUSTOM_DATETIME':
					var tdTag = $('#attribute'+innerFlag).parents('td');
					if(tdTag.children('span').length>0){
						$('#attribute'+innerFlag).datetimebox("destroy");
						tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
		    		}
					$('#attribute'+innerFlag).datetimebox({
					    required : true
					});
					break;
				default:
					break;
					
				}
		    	var initVal = $(node.target).find(".tree-title").text();
		    	var currentNode= node;
				var parentNode = $(this).tree("getParent",currentNode.target);
				var text = initVal;
				while(parentNode!=null){
					text+="."+parentNode.text;
					parentNode = $(this).tree("getParent",parentNode.target);
				}
				var cText = text.split(".");
				console.log(cText);
				var transText = "";
				for(var i=cText.length-1;i>=0;i--){
					transText += cText[i];
					if((i-1)>=0){
						transText += ".";
					}
				}
				console.debug(transText);
				var aac = transText.split(".");
				for(var val in aac){
					console.log(aac[val]);
				}
				node.text=transText;
		    }
		},
		
		onClick: function (node) {
		}
	});
	$('.left_parenthesis'+flag).combobox({
		valueField: 'label',
		textField: 'value',
		width : 80,
		editable : false,
		panelHeight : 100,
		data: [{
			label: '(',
			value: '('
		},{
			label: '((',
			value: '(('
		},{
			label: '(((',
			value: '((('
		},{
			label: '((((',
			value: '(((('
		}]
	});
	$('.right_parenthesis'+flag).combobox({
		valueField: 'label',
		textField: 'value',
		width :80,
		editable : false,
		panelHeight : 100,
		data: [{
			label: ')',
			value: ')'
		},{
			label: '))',
			value: '))'
		},{
			label: ')))',
			value: ')))'
		},{
			label: '))))',
			value: '))))'
		}]
	});
	$('.expression'+flag).combobox({
		valueField: 'label',
		textField: 'value',
		width : 80,
		editable : true,
		panelHeight : 60,
		data: [{
			label: 'and',
			value: '并且'
		},{
			label: 'or',
			value: '或者'
		}]
	});
	$('.condition'+flag).combobox({
		valueField: 'label',
		textField: 'value',
		width : 100,
		editable : false,
		panelHeight : 160,
		data: [{
			label: '>',
			value: '大于'
		},{
			label: '>=',
			value: '大于等于'
		},{
			label: '<',
			value: '小于'
		},{
			label: '<=',
			value: '小于等于'
		},{
			label: '!=',
			value: '不等于'
		},{
			label: '=',
			value: '等于'
		},{
			label: 'like',
			value: '类似'
		}]
	});
	flag++;
	
}
function closePage(){
	flag=0;
	parent.deletePanel("分配权限");
}
function submitDataFilterForm(){
	$.messager.confirm('请确认', '是否保存'+treeNode.text+'的数据过滤？', function(r) {
		if (r) {
			var attriArray = new Array();
			$('#filter-form').children().each(function(index,element){
				 var obj = $(element);//获取当前元素  为table
				 //var objId = $(this).attr("id");//获取当前table的id  或者$(element).attr("id");
				 var tbodyObj = obj.find('tbody');//table下面还有一个tbody子元素 及所有的tr元素
				 tbodyObj.children().each(function(trIndex,trElement){
					 attriArray[trIndex] = new Array();
					 var trObj = $(trElement);//获取tr元素对象
					 trObj.find(':input:not(:hidden)').each(function(tdIndex,tdElement){
						 if(tdIndex==3){
							
							 /* var iComboTree = $(this).parent().prev('input').combotree('getChecked');
							 attriArray[trIndex][tdIndex] = JSON.stringify(iComboTree); */
							 attriArray[trIndex][tdIndex] = $(this).val();
						 }else{
						 	 attriArray[trIndex][tdIndex] = $(this).siblings('input').val();
						 }
					 });
					 //下面的代码是增加所选属性的中文路径到数组
					 var tdTree = trObj.find('.attrName').combotree('tree');
					 var selectedNode = tdTree.tree('getSelected');
					 console.log(selectedNode.text);
					 attriArray[trIndex][6] = selectedNode.text;
				 });
			     
			}); 
			$.post("permManage/addDataFilter.do",{roleID:'<%=roleId4Perm%>',permID:treeNode.id,dataFilter:JSON.stringify(attriArray)},function(data){
				
			},"json");
			closeDialog(true);
		}
	});
	
}


function deleteRow(obj){
	//flag--;//不用将数据过滤总行数减1 ，可能从中间行删除
	 $.messager.confirm('删除', '是否删除该行？', function(r) {
			if (r) {
				var trNode = obj.parentNode;
				trNode.parentNode.removeChild(trNode);
			}
	 });
	
}
function cancelEdit(){
	flag=0;//将数据过滤总行数设为0
	$('.filter-table').empty();//清除table中的所有元素
	isBlockTag(false);//对应按钮变为不可见
	treeNode = null; //不选中任何节点
}
function deleteAllRow(){
	 $.messager.confirm('确认放弃', '是否删除说有行？', function(r) {
			if (r) {
				flag=0;
				$('.filter-table').empty();//清除table中的所有元素
			}
	 });
	
}
//提交用户权限，不包括提交数据权限
function submitTreeForm(){
	var nodes = permTree.tree('getChecked',['checked','indeterminate']);
	var jsonArr = new Array();
	for(var i=0;i<nodes.length;i++){
		var jsonNode = {"id":nodes[i].id};
		jsonArr.push(jsonNode);
	}
	$.post("permManage/assignRolePerm.do",{roleID:roleID, treeNodes:JSON.stringify(jsonArr)},function(data){
		if(data.success){
			parent.showPromptMessages("提示","角色权限分配成功成功！");
			closePage();
		}
	},"json");
}
function initDataFilterRow(){
	$.post("permManage/getExistDataFilter.do",{roleID:'<%=roleId4Perm%>',permID:treeNode.id},function(data){
		//var jsonObj = JSON.parse(data);
		//var jsonObj = eval('('+data+')');
		//将后台传递过来的字符串转换为json对象   这是一种没有键的非标准json字符串，上面两种转换形式都会出
		console.warn(eval(data));
		var jsonObj = eval(data);
		 for(var trObj in jsonObj){
			 var tdObj = eval(jsonObj[trObj]);
			 createExistDataFilterRow(jsonObj[trObj]);
		 } 
	},"json");
}
//创建已有的datafilter行记录
function createExistDataFilterRow(data){
	var innerFlag = flag;//当调用这个方法时，获取JS全局变量flag,那么在这个方法内，innerFlag就可以供方法内的方法调用，而且在编译的时候将innerFlag的值已经绑定在方法体内的变量中
	console.log(eval(data));
	var tdObj = eval(data);
	$('#'+treeNode.id).append('<tr><td class="left_parenthesis"><input class="left_parenthesis'+flag+' id="left_parenthesis'+flag+
			'" /></td><td><input class="attrName" type="text" value='+tdObj.propertyName+' id="'+treeNode.id+flag+
			'"/></td><td><input class="condition'+flag+' id="condition'+flag+
			'"/></td><td><input style="width:114px" id="attribute'+flag+'" class="attribute" value='+tdObj.propertyValue+' /></td><td class="right_parenthesis"><input class="right_parenthesis'+flag+
			'" type="text"/></td><td class="join_Condition"><input class="expression'+flag+
			'" type="text"/></td><td class="deleteRow" onclick="deleteRow(this);">删除行</td></tr>');
	$('#'+treeNode.id+flag).combotree({
		url : 'permManage/getPermAttriTree.do?permID='+treeNode.id,
		editable : false,
		height : '20',
		width : '220',
		cascadeCheck : false,
		panelHeight : '480',//点击之后下拉菜单的单行高度
		panelWidth : '220',
		formatter:function(node){
			var text = node.text;
			text = text.substring(text.lastIndexOf(".")+1);
			return text;
		},
		onBeforeLoad : function(){},
		onLoadSuccess:function(){
			//$('.tree-checkbox').unbind('click');
			var thisTree = $('#'+treeNode.id+innerFlag).combotree("tree");
			var selectNode= thisTree.tree("getSelected");
			//thisTree.tree("select",selectNode.target);
			switch(selectNode.attributes.propType){
			case 'SELECTABLE_VALUE':
				var tdTag = $('#attribute'+innerFlag).parents('td');
				if(tdTag.children('span').length>0){
					$('#attribute'+innerFlag).datetimebox("destroy");
					tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
	    		}
				$('#attribute'+innerFlag).combobox({
					url:'permManage/getValueByAttr.do?permID='+treeNode.id+'&attributeName='+selectNode.id,
					valueField:'id',
					textField:'text',
					width : 110,
					editable : true,
					value : tdObj.propertyValue,
					panelHeight : 100
				});
				$('#attribute'+innerFlag).combobox("select",tdObj.propertyValue);
				break;
			case 'CUSTOM_STRING':
				var tdTag = $('#attribute'+innerFlag).parents('td');
				if(tdTag.children('span').length>0){
					$('#attribute'+innerFlag).datetimebox("destroy");
					tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
	    		}
				$('#attribute'+innerFlag).validatebox({
				    required: true
				});
				$('#attribute'+innerFlag).val(tdObj.propertyValue);
				break;
			case 'CUSTOM_NUMBER':
				var tdTag = $('#attribute'+innerFlag).parents('td');
				if(tdTag.children('span').length>0){
					$('#attribute'+innerFlag).datetimebox("destroy");
					tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
	    		}
				$('#attribute'+innerFlag).numberbox({
				    required : true,
				    value : tdObj.propertyValue,
				    min:0
				});
				break;
			case 'CUSTOM_DATETIME':
				var tdTag = $('#attribute'+innerFlag).parents('td');
				if(tdTag.children('span').length>0){
					$('#attribute'+innerFlag).datetimebox("destroy");
					tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
	    		}
				$('#attribute'+innerFlag).datetimebox({
					value : tdObj.propertyValue,
				    required : true
				});
				console.log("tdObj.propertyValue"+tdObj.propertyValue);
				$('#attribute'+innerFlag).datetimebox('setValue', tdObj.propertyValue);
				break;
			default:
				break;
				
			}
			},
		onSelect : function(node){
			//判断是否是叶子节点  
			var isLeaf = $(this).tree('isLeaf', node.target);  
		    if (!isLeaf) {  
		        //清除选中  
		        $('#'+treeNode.id+innerFlag).combotree('clear'); 
		    }else{
		    	switch(node.attributes.propType){
				case 'SELECTABLE_VALUE':
					var tdTag = $('#attribute'+innerFlag).parents('td');
					if(tdTag.children('span').length>0){
						$('#attribute'+innerFlag).datetimebox("destroy");
						tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
		    		}
					$('#attribute'+innerFlag).combobox({
						url:'permManage/getValueByAttr.do?permID='+treeNode.id+'&attributeName='+node.id,
						valueField:'id',
						textField:'text',
						width : 110,
						editable : true,
						panelHeight : 100
					});
					break;
				case 'CUSTOM_STRING':
					var tdTag = $('#attribute'+innerFlag).parents('td');
					if(tdTag.children('span').length>0){
						$('#attribute'+innerFlag).datetimebox("destroy");
						tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
		    		}
					$('#attribute'+innerFlag).validatebox({
					    required: true
					});
					break;
				case 'CUSTOM_NUMBER':
					var tdTag = $('#attribute'+innerFlag).parents('td');
					if(tdTag.children('span').length>0){
						$('#attribute'+innerFlag).datetimebox("destroy");
						tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
		    		}
					$('#attribute'+innerFlag).numberbox({
					    required : true,
					    min:0
					});
					break;
				case 'CUSTOM_DATETIME':
					var tdTag = $('#attribute'+innerFlag).parents('td');
					if(tdTag.children('span').length>0){
						$('#attribute'+innerFlag).datetimebox("destroy");
						tdTag.html('<input style="width:114px" id="attribute'+innerFlag+'" class="attribute"/>');
		    		}
					$('#attribute'+innerFlag).datetimebox({
					    required : true
					});
					break;
				default:
					break;
					
				}
		    	//找到树节点对应的dom对象，并且获取class="tree-title"子元素的值，这个值是初始化树的时候就设定好的，改变treeNode.text是不会改变该元素的值
		    	/* var initVal = $(node.target).find(".tree-title").text();
		    	var currentNode= node;
				var parentNode = $(this).tree("getParent",currentNode.target);
				var text = initVal;
				//找出节点的所有父节点 将所有找到的text相加
				while(parentNode!=null){
					text+="."+parentNode.text;
					parentNode = $(this).tree("getParent",parentNode.target);
				}
				var cText = text.split(".");
				var transText = "";
				//反转text的值
				for(var i=cText.length-1;i>=0;i--){
					transText += cText[i];
					if((i-1)>=0){
						transText += ".";
					}
				}
				var aac = transText.split(".");
				for(var val in aac){
					console.log(aac[val]);
					console.log(val);
				}
				node.text=transText; */
		    }
		},
		onClick: function (node) {
		}
	});
	$('.left_parenthesis'+flag).combobox({
		valueField: 'label',
		textField: 'value',
		width : 80,
		editable : false,
		panelHeight : 100,
		data: [{
			label: '(',
			value: '('
		},{
			label: '((',
			value: '(('
		},{
			label: '(((',
			value: '((('
		},{
			label: '((((',
			value: '(((('
		}],
		 onLoadSuccess: function () { //加载完成后,设置选中第一项
			 $(this).combobox("select",tdObj.leftParentheses);
         }
	});
	$('.right_parenthesis'+flag).combobox({
		valueField: 'label',
		textField: 'value',
		width :80,
		editable : false,
		panelHeight : 100,
		data: [{
			label: ')',
			value: ')'
		},{
			label: '))',
			value: '))'
		},{
			label: ')))',
			value: ')))'
		},{
			label: '))))',
			value: '))))'
		}],
		onLoadSuccess: function () { //加载完成后,设置选中第一项
			 $(this).combobox("select",tdObj.rightParenthese);
        }
	});
	if(treeNode.attributes.enableDataPermission)
	$('.attribute'+flag).validatebox({});
	$('.expression'+flag).combobox({
		valueField: 'label',
		textField: 'value',
		width : 80,
		editable : true,
		panelHeight : 60,
		data: [{
			label: 'and',
			value: '并且'
		},{
			label: 'or',
			value: '或者'
		}],
		onLoadSuccess: function () { //加载完成后,设置选中第一项
			 $(this).combobox("select",tdObj.logical);
        }
	});
	$('.condition'+flag).combobox({
		valueField: 'label',
		textField: 'value',
		width : 100,
		editable : false,
		panelHeight : 160,
		data: [{
			label: '>',
			value: '大于'
		},{
			label: '>=',
			value: '大于等于'
		},{
			label: '<',
			value: '小于'
		},{
			label: '<=',
			value: '小于等于'
		},{
			label: '!=',
			value: '不等于'
		},{
			label: '=',
			value: '等于'
		},{
			label: 'like',
			value: '类似'
		}],
		onLoadSuccess: function () { //加载完成后,设置选中第一项
			 $(this).combobox("select",tdObj.midOperator);
        }
	});
	
	flag++;
}
function showDataDialog(){
	//初始化dialog里面的行数据
	initDataFilterRow();
	//显示dialog
	dataFilterDialog.dialog("open");
}
function closeDialog(directClose){
	if(directClose){
		flag=0;//将数据过滤总行数设为0
		dialogCloseFlag = true;
		//$('.filter-table').empty();//清除table中的所有元素
		dataFilterDialog.dialog('close');
	}else{
		$.messager.confirm('关闭', '是否关闭此页？', function(r) {
			if (r) {
				flag=0;//将数据过滤总行数设为0
				dialogCloseFlag = true;
				//$('.filter-table').empty();//清除table中的所有元素
				dataFilterDialog.dialog('close');
			}
		});
	}
}
</script>
<style>
#perm-content{
	position:relative;
	width:100%;
	height:500px;
}
#perm-tree{
	width:300px;
	padding-left:30px;
	float:left;
	overflow-y:auto;
	height:500px;
	}
#data-filter-dialog{
	/* margin-left: 320px; 
    padding: 20px 20px 0;*/
    /* position: absolute; */
    width: 845px;
    height:400px;
	}
#data-filter{
	width:auto;
	}
#data-filter-title{
	width:auto;
	}
#buttonBox{
	
    margin: 3px 0;
}

.calendar table td, .calendar table th{
	width:20px;
}
#data-filter-dialog td{
	width:124px;
	border:1px solid #ccc;
}
#data-filter-dialog th{
	width:120px;
	text-align:center;
	border:1px solid #ccc;
}
#data-filter-dialog .join_Condition{
	width:80px;
}
#data-filter-dialog .left_parenthesis{
	width:80px;
}
#data-filter-dialog .right_parenthesis{
	width:80px;
}
#data-filter-dialog .attrName{
	width:227px;
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
/* .addRowBtn{
	background: none repeat scroll 0 0 #fff;
    border-radius: 0;
    clear: both;
    float: none;
    font-weight: normal;
    height: 22px;
    margin-left: 488px;
    margin-top: 0;
    width: 52px;
} */
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
.filter-table .deleteRow{

	height:20px;
	cursor:pointer;
	text-align:center;
}
#data-filter-right{
	display:none;
	position:absolute;
	margin-left:345px;
	margin-top:0px;
	width:860px;
}
.diaButton{
	font-size:16px;
	font-weight:bold;
	
	width:140px;
	height:28px;
}
.opRowButton{
	font-size:14px;
	font-weight:bold;
	
	width:100px;
	height:25px;
}

#wrap-datagrid{
	width:855px;
	height:450px;
}
#data-filter-grid{
	
}
.wrap-button{
	width:100px;
}
</style>
</head>
<body>
<div id="perm-content">
	<div id="perm-tree">
		<form id="rolePermForm" method="post">
			<span name="rolePermTree" id="rolePermTree"></span>
		</form>
	</div>
	<div id="data-filter-right">
		
		<input onclick="showDataDialog();" class="action-btn" type="button" value="编辑"/>
	
		<div id="wrap-datagrid" style="border:1px solid #ccc;">
			<div id="data-filter-grid" >
				
			</div>
		</div>
		
	</div>
	<div id="noDataFilterMessage" style="display:none;">
		<h1 id="noDataFilterPrompt" style="color:red;font-size:24px;">该权限项没有启用数据过滤权限</h1>
	</div>
	<div id="data-filter-dialog" style="padding:5px;">
		<!-- <h1 id="noDataFilterPrompt" style="color:red;font-size:24px;">该权限项没有启用数据过滤权限</h1> -->
		<div id = "buttonBox" >
			<input class="action-btn " style="margin-right:20px;" onclick="addDataFilterRow();" value="增加行" type="button"/>
			<input class="action-btn-defaule" onclick="deleteAllRow();" value="删除所有行" type="button"/>
			
		</div>
		<div id="data-filter-title" class="order" >
			
				<table class="entryTable" style="margin:0;padding:0;">
					<tr>
						<th class="left_parenthesis">左括号</th>
						<th class="attrName">属性名</th>
						<th>约束条件</th>
						<th>约束值</th>
						<th class="left_parenthesis">右括号</th>
						<th class="join_Condition">连接条件</th>
						<th>操作</th>
					</tr>
				</table>
				<div id="data-filter" >
					<form id="filter-form" method="post">
						<table class="filter-table entryTable"></table>
					</form>
				</div>
		</div>
	
	
	</div>
	<div id = "dialogButton" >
		<input class="easyui-linkbutton diaButton diaCancel" data-options="plain:false" onclick="javascript:closeDialog(false);" value="取消" type="button"/>
		<input class="easyui-linkbutton diaButton confirmButton " data-options="plain:false" onclick="javascript:submitDataFilterForm();" value="确定" type="button"/>
	</div>
</div>
<div id="foot">
<div id="foot-button">
	<input class="easyui-linkbutton iButton cancelButton" onclick="closePage();" value="取消"/>
	<input class="easyui-linkbutton iButton confirmButton" onclick="submitTreeForm();" value="确定"/></div>
</div>
</body>
</html>