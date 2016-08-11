<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="./include/meta.jsp" />
<c:import url="./include/easyui.jsp" />
<!DOCTYPE html>
<html>
<head>
<link rel="shortcut icon" href="web/images/main/favicon.ico"/>
<link rel="bookmark" href="web/images/main/favicon.ico"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>产品管理小工具</title>
<link rel="stylesheet" type="text/css" href="web/css/home/main.css">
<script type="text/javascript" src="web/js/pushlet/ajax-pushlet-client.js" charset="utf-8"></script>
<script type="text/javascript" charset="utf-8">
	var globalIsEnable=1;//用来判断是否有tabs被锁定
	var centerTabs;
	var tabsMenu;
	var windowWidth = window.innerWidth-2+"px";//浏览器的可视宽度
	var windowHeight = window.innerHeight-2+"px";
	var pubNoticeDialog;
	var announceDialog;
	var announceForm;
	$(function() {
		//用于pushlet
		p_join_listen('/wgerp/notice/${CURRENT_USER.userID}');//个人通知  
	    PL.setFun(onData);  
	    //为了初始化窗口时，界面跟着变化，并且在缩小浏览器窗口时会有滚动条
		$(".wap").css({"width":windowWidth,"height":windowHeight,"margin":"0 auto"});
		//创建Tab面板右键目录
		tabsMenu = $('#tabsMenu').menu({
			onClick : function(item) {
				var curTabTitle = $(this).data('tabTitle');
				var type = $(item.target).attr('type');

				if (type === 'refresh') {
					refreshTab(curTabTitle);
					return;
				}

				if (type === 'close') {
					var t = centerTabs.tabs('getTab', curTabTitle);
					if (t.panel('options').closable) {
						centerTabs.tabs('close', curTabTitle);
					}
					return;
				}

				var allTabs = centerTabs.tabs('tabs');
				var closeTabsTitle = [];
				//遍历关闭其他的tab面板
				$.each(allTabs, function() {
					var opt = $(this).panel('options');
					if (opt.closable && opt.title != curTabTitle && type === 'closeOther') {
						closeTabsTitle.push(opt.title);
					} else if (opt.closable && type === 'closeAll') {
						closeTabsTitle.push(opt.title);
					}
				});

				for ( var i = 0; i < closeTabsTitle.length; i++) {
					centerTabs.tabs('close', closeTabsTitle[i]);
				}
			}
		});

		//创建easyui tab并绑定右键目录
		centerTabs = $('#centerTabs').tabs({
			fit : true,
			border : false,
			tabWidth:"100",
			onClose : function(title,index){
				enableAllTabs();
			},
			onContextMenu : function(e, title) {
				e.preventDefault();
				tabsMenu.menu('show', {
					left : e.pageX,
					top : e.pageY
				}).data('tabTitle', title);
			}/* ,
			onLoad : function(panel){
				try {
					$.messager.progress('close');
				} catch (e) {
				}
			} */
		});
		//显示公告信息对话框
		pubNoticeDialog = $("#pub-notice-dialog").show().dialog({
 			modal : true,
 			title : '公告信息',
 			height : 450,
 			width : 600
 		}).dialog('close');//pubNoticeDialog
 		
		//加载通知信息
		reloadNotice();
		//2014 09 24
		//$('#west-nav').accordion('select',0);

	});//预加载函数完
	
	function reloadNotice(){
		$(".sys_notice").empty();
		$(".p_notice_item_wap").empty();
		$.ajax({
			url:"notice/getAllPrivateNotice/${CURRENT_USER.userID}",
			type:"post",
			data:{},
			dataType:"json",
			success:function(data){
				if(data.success){
					if(data.obj.Product){
						var noticeList = data.obj.Product;
						var htmlContent = "<div class='p_notice_classify'><h4><b>产品</b><a href='javascript:void(0);' onclick=addTab('产品信息','产品信息','productManage.do?productManagePage')>更多</a></h4><ol>";
						for(var i=0;i<noticeList.length;i++){
							htmlContent+="<li><span>"+noticeList[i].noticeTitle
							+"</span><a href='javascript:void(0);' onclick=\"doOnClick('"+noticeList[i].id+"','产品','"+noticeList[i].content+"','"+noticeList[i].url+"');\">"+noticeList[i].content+"</a><time>" +noticeList[i].sentDt+"</time></li>";
							if(i>5){//最多显示6条
								break;
							}
						}
						htmlContent+="</ol></div>";
						$(".p_notice_item_wap").append(htmlContent);
					}
					
					if(data.obj.Purchase){
						var noticeList = data.obj.Purchase;
						var htmlContent = "<div class='p_notice_classify'><h4><b>采购</b><a href='javascript:void(0);' onclick=addTab('采购订单管理','采购订单管理','/wgerp/po.do?poManagePage')>更多</a></h4><ol>";
						for(var i=0;i<noticeList.length;i++){
							htmlContent+="<li><span>"+noticeList[i].noticeTitle
								+"</span><a href='javascript:void(0);' onclick=\"doOnClick('"+noticeList[i].id+"','采购订单','"+noticeList[i].content+"','"+noticeList[i].url+"');\">"
								+noticeList[i].content+"</a><time>" +noticeList[i].sentDt+"</time></li>";
							if(i>5){//最多显示6条
								break;
							}
						}
						htmlContent+="</ol></div>";
						$(".p_notice_item_wap").append(htmlContent);
					}
					if(data.obj.Stock){
						var noticeList = data.obj.Stock;
						var htmlContent = "<div class='p_notice_classify'><h4><b>库存</b><a href='javascript:void(0);' onclick=addTab('库存','销售订单管理','/wgerp//saleManage.do?saleManagePage')>更多</a></h4><ol>";
						for(var i=0;i<noticeList.length;i++){
							htmlContent+="<li>"+noticeList[i].noticeTitle
								+"<a href='javascript:void(0);' onclick=\"doOnClick('"+noticeList[i].id+"','库存订单','"+noticeList[i].content+"','"+noticeList[i].url+"');\">"
								+noticeList[i].content+"</a><time>" +noticeList[i].sentDt+"</time></li>";
							if(i>5){//最多显示6条
								break;
							}
						}
						htmlContent+="</ol></div>";
						$(".p_notice_item_wap").append(htmlContent);
					}
					if(data.obj.Sale){
						var noticeList = data.obj.Sale;
						var htmlContent = "<div class='p_notice_classify'><h4><b>销售</b><a href='javascript:void(0);' onclick=addTab('销售订单管理','销售订单管理','/wgerp//saleManage.do?saleManagePage')>更多</a></h4><ol>";
						for(var i=0;i<noticeList.length;i++){
							htmlContent+="<li>"+noticeList[i].noticeTitle
								+"<a href='javascript:void(0);' onclick=\"doOnClick('"+noticeList[i].id+"','销售订单','"+noticeList[i].content+"','"+noticeList[i].url+"');\">"
								+noticeList[i].content+"</a><time>" +noticeList[i].sentDt+"</time></li>";
							if(i>5){//最多显示6条
								break;
							}
						}
						htmlContent+="</ol></div>";
						$(".p_notice_item_wap").append(htmlContent);
					}
					if(data.obj.Public){
						var noticeList = data.obj.Public;
						var htmlContent = "<ol class='pb_notice_ol'>";
						for(var i=0;i<noticeList.length;i++){
							console.log(noticeList[i].noticeTitle);
							var index = i+1;//在这里加上需要的原因未chrome里面有给li增加float属性之后，就不能显示ol的序号的bug
							//var obj = {noticeID:index,title:"公告",billNo:noticeList[i].noticeTitle,url:noticeList[i].content};
							var obj = new Object();
							obj.noticeID = index;
							obj.title = "公告";
							obj.billNo = noticeList[i].noticeTitle;
							obj.url = noticeList[i].content;
							htmlContent+="<li class='pb_notice_title'><span>"+index+". "+noticeList[i].noticeTitle
								+"</span><a href='javascript:void(0);' class='pb_notice_btn' onclick=\"doOnClick('"+noticeList[i].id
								+"','公告','"+noticeList[i].noticeTitle+"','"+noticeList[i].content+"');\">阅读</a><time>"+noticeList[i].sentDt+"</time></li>";
							if(i>6){//最多显示6条
								break;
							}
						}
						htmlContent+="</ol>";
						$(".sys_notice").append(htmlContent);
					}
					
					if(data.obj.Sale){
						
					}
				}
			}
		});
	}
	//显示产品详情
	function showProdDetail(skuNo,url){
		addTab('tabId_productDetail', '产品('+skuNo+')详情',url);
	}
	
	//增加tab面板
	function addTab(tabId,cname,curl,isRefresh) {
		//首先判断时候有被锁定的tabs，如果有被锁定的tabs，那么就不能创建新的tabs
		if(globalIsEnable==0){
			return;
		}
		//判断对应title的tab是否存在，如果存在，就让其选中
		if (centerTabs.tabs('exists', cname)) {
			centerTabs.tabs('select', cname);
			if(isRefresh){
				centerTabs.tabs("close",cname);
				centerTabs.tabs('add', {
					title : cname,
					closable : true,
					content : '<iframe id="'+tabId+'" src="' + curl + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>',
					tools : [ {
						iconCls : 'icon-mini-refresh',
						handler : function() {
							refreshTab(cname);
						}
					} ]
				});
			}

		} else {
			if (curl && curl.length > 0) {
				//进度条
				$.messager.progress({
						text : '页面加载中....',
						interval : 100
					}); 
					window.setTimeout(function() {
						try {
							$.messager.progress('close');
						} catch (e) {
						}
					}, 500);

				centerTabs.tabs('add', {
					title : cname,
					closable : true,
					content : '<iframe id="'+tabId+'" src="' + curl + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>',
					tools : [ {
						iconCls : 'icon-mini-refresh',
						handler : function() {
							refreshTab(cname);
						}
					} ]
				});
			} else {
				centerTabs.tabs('add', {
					title : cname,
					closable : true,
					iconCls : ciconCls,
					content : '<iframe src="error/err.jsp" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>',
					tools : [ {
						iconCls : 'icon-mini-refresh',
						handler : function() {
							refreshTab(cname);
						}
					} ]
				});
			}
		}
	}
	//刷新面板
	function refreshTab(title) {
		if(centerTabs.tabs('exists', title)){
			var tab = centerTabs.tabs('getTab', title);
			centerTabs.tabs('update', {
				tab : tab,
				options : tab.panel('options')
			});
		}
		
	}
	
	//保存筛选条件的刷新面板
	function refreshSearchTab(title) {
		if(centerTabs.tabs('exists', title)){
			var tab = centerTabs.tabs('getTab', title);
			var $searchBtn = tab.panel("body").find("iframe")[0].contentWindow.document.getElementById("searchBtn");
			$searchBtn.click();
		}
		
	}
	
	//刷新面板,同时也更新TAB标题
	function refreshTabWithTitle(title,newTitle) {
		var tab = centerTabs.tabs('getTab', title);
		centerTabs.tabs('update', {
			title: newTitle,
			tab : tab,
			options : tab.panel('options')
		});
	}
	
	//注销登录
	function logout(){
		 $.messager.confirm("操作提示", "您确定要执行操作吗？", function (data) {
	            if (data) {
	            	 $.post('loginManage/logout.do', function() {
					window.location.replace(location);
				});
			}
		});

	}
	//显示用户信息
	function userInfo() {
		addTabFun({
			src : 'userController.do?userInfo',
			title : '个人信息'
		});
	}
	
	//删除tabs的panel
	function deletePanel(title){
		var t = centerTabs.tabs('getTab', title);
		if (t.panel('options').closable) {
			centerTabs.tabs('close', title);
		}
	}
	//让其他的tabs变为不可编辑
	function disableOtherTabs(currtitle){
		var allTabs = centerTabs.tabs('tabs');
		var disableTabsTitle = [];
		//遍历其他的tab面板
		$.each(allTabs, function() {
			var opt = $(this).panel('options');
			if (opt.title != currtitle) {
				disableTabsTitle.push(opt.title);
			} 
		});

		for ( var i = 0; i < disableTabsTitle.length; i++) {
			console.warn(disableTabsTitle[i]);
			centerTabs.tabs('disableTab', disableTabsTitle[i]);
		}
		globalIsEnable=0;
	}
	//让其他的tabs变为不可编辑
	function enableAllTabs(){
		var allTabs = centerTabs.tabs('tabs');
		$.each(allTabs, function(){
			var opt = $(this).panel('options');
			centerTabs.tabs('enableTab', opt.title);
		});
		globalIsEnable=1;
	}
	//在main.jsp窗口显示提示框  message为提示信息 title为提示框标题
	function showPromptMessages(title,message){
		$.messager.show({
			msg : message,
			title : title
		});
	}
	//接收pushlet推送的通知
     function onData(event) {
		var userName=event.get("userName");
		var billNo=event.get("billNo");
		var url=event.get("url");
		var title = event.get("title");
		var noticeID = event.get("noticeID");
		console.log("main.jsp");
		if(title=="公告"){
			 showPromptMessages("信息提示","<a href='javascript:void(0);' onclick=\"doOnClick('"+noticeID+"','"+title+"','"+billNo+"','"+url+"');\">您有最新公告:"+billNo+"</a>");
		}else{
			console.log("main prompt");
		   	showPromptMessages("信息提示","<a href='javascript:void(0);' onclick=\"doOnClick('"+noticeID+"','"+title+"','"+billNo+"','"+url+"');\">"+userName+"刚刚操作了单据"+billNo+"</a>");
		}
     }  
     //通知详情界面
    function doOnClick(noticeID,title,billNo,url){
    	 //首先将单据该为已读
    	 if(title=="公告"){
    		$.ajax({
    			 url:"notice/getNoticeByID.do",
    			 type:"post",
    			 data:{noticeID : noticeID},
    			 dataType:"json",
    			 success:function(data){
    				 if(data.success){
	    				 $(".pub_notice_title").text(data.obj.noticeTitle);
	    				 $(".pub_notice_content").html(data.obj.content);
	    				 $(".pub_notice_foot time").text(data.obj.sentDt);
	    				 pubNoticeDialog.dialog("open");
    				 }
    			 }
    		 });
    	 }else{
    		 $.ajax({
    			 url:"notice/upNoticeReaded.do",
    			 data:{noticeID : noticeID},
    			 type:"post",
    			 dataType:"json",
    			 success:function(data){
    				 if(data.success){
    					 
    				 }
    			 }
    		 });
	    	 addTab('tabId_viewPo', title + billNo + '详情',url,false);
	    	 reloadNotice();
    	 }
     }
     
     //发布公告
     function announce(){
    	 if(announceDialog==undefined){
    		 announceForm = $("#announceForm").form();
	    	 announceDialog = $("#announceDialog").show().dialog({
	    		 modal : true,
	    		title : '新增公告',
	    		onClose:function(){
	    			reloadNotice();
	    		},
	    		buttons : [ {
	    			text : '确定',
	    			handler : function() {
	    				var isValid = announceForm.form('validate');
	    				if (!isValid){
	    					return;
	    				}
	    					announceForm.form('submit', {
	    						url : 'notice/addPublicNotice.do',
	    						success : function(data) {
	    							var jsonData = $.parseJSON(data); 
	    							if(jsonData.success){
		    							announceDialog.dialog('close');
		    							$.messager.show({
		    								msg : '公告发布成功！',
		    								title : '提示'
		    							});
	    							}else{
	    								$.messager.alert("错误",jsonData.msg);
	    							}
	    						}
	    					});
	    				}
	    			} ]
	  		}).dialog('close');//pubNoticeDialog
	    	 announceDialog.dialog("open");
    	 }else{
    		 announceDialog.dialog("open");
    	 }
     }

</script>


</head>
<body>
<div class="wap">
<div id="layout" class="easyui-layout">

	<div region="north" id="north" class= "north_area">
		<div class="top-bg">	
			<b class="logo">产品管理小工具</b>
			<div class="rightinfo">
					<span class="select logout" onclick="logout();"><img class="logout_img" src="web/images/home/logout.png"><!-- <i class="fa fa-power-off"></i> --></span>
					<span class="select currentUser">${CURRENT_USER.userCnName}</span>
					
			</div>
		</div>
	</div>
	
	
	<!-- 这里的div可以直接添加一条href属性，就相当于是iframe.如href="test.jsp" -->
	<!-- 正左边panel -->
	<div region="west" class="west_area">
		<div class="easyui-accordion" data-options=" fit:false,border:false" id="west-nav">
			<!-- selected -->
			<c:set var="lastCode" value="0"></c:set>
			<c:forEach var="menuItem" items="${menuList}" varStatus="status">
				<c:choose>
					<c:when test="${menuItem.parentCode=='0'}">
						<c:if test="${lastCode!=menuItem.parentCode}">
							</div>
							</div>
						</c:if>
						<div title="${menuItem.permName}">
						<div>
					</c:when>
					<c:otherwise>
						<c:set var="lastCode" value="${menuItem.code}"></c:set>
							<span
								onclick="addTab('${menuItem.permID}','${menuItem.permName}','${menuItem.nodeUrl}');"><img
										src="web/images/home/icons/menu/user.png" />${menuItem.permName}</span>
					</c:otherwise>
				</c:choose>
				<c:if test="${status.last}">
					</div>
					</div>
				</c:if>
			</c:forEach>
		</div>
	</div>
	<!-- 正中间panel -->
	<div class="center_area" region="center">

		<div id="centerTabs" class="center_tabs">
			<div title="首页" style="overflow: hidden;" data-options="tools : [ {
						iconCls : 'icon-mini-refresh',
						handler : function() {
							reloadNotice();
						}
					} ]">
				<div class="div_box" style="width:'auto';">
					<h3>公告<c:if test="${CURRENT_USER.type==100}"><a href="javascript:void(0);" onclick="announce();">发布公告 </a></c:if></h3>
					<div class="sys_notice">
						<!-- 沃光ERP v1.0 beta1即将于15年3月1日发布公测版本，敬请期待---  -->
					</div>
					<div class="private_notice_wap">
						<h3>个人通知及任务</h3>
						<div class="p_notice_item_wap">
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div id="tabsMenu" style="width: 120px; display: none;">
			<div type="refresh">刷新</div>
			<div class="menu-sep"></div>
			<div type="close">关闭</div>
			<div type="closeOther">关闭其他</div>
			<div type="closeAll">关闭所有</div>
		</div>
		<div id="pub-notice-dialog">
			<div class="pub_notice_title"></div>
			<div class="pub_notice_content"></div>
			<div class="pub_notice_foot"><time></time></div>
		</div>
	</div>
	<!-- 正下方panel -->
	<div region="south" class="south_area"
		align="center">
		<address>© 2015-2016 华强国际创客中心. All Rights Reserved. build 160713</address>
	</div>
</div>
</div>

<div id="announceDialog" style="display:none;">
		<form id="announceForm"  class="announce_form" method="post">
			<table>
				<tr>
					<th>公告标题:</th>
					<td><input name="noticeTitle" class="easyui-validatebox" maxlength="20" required="true" type="text"/></td>
				</tr>
				<tr>
					<th>公告内容:</th>
					<td><textarea name="content"></textarea></td>
				</tr>
			</table>
		</form>
</div>
</body>
</html>