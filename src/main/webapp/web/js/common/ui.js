
$(function(){
	
	//$("input[required=true]").parent().prev("th").prepend("<span class='redcolor'>*</span>");
	$("input[required=true]").after("<span class='redcolor'>*</span>");
	$(".more-change").click(function(){
		$(".tableForm").find("tr:eq(2)").toggle(0,function(){
			
			if($(".more-change").text() == "高级选项隐藏"){
				$(".more-change").text("高级选项显示");
			}else{
				$(".more-change").text("高级选项隐藏");
			}
		});
		
		
	});
});
/*页面搜索框 普通模式与高级模式的切换*/
function switchAdvanced(datagridId){
	$(".advanced_element").show();
	$(".advanced_btn_wap").hide();
	$(".search_table input").css("clear","both");
	if(datagridId){
		datagridId.datagrid("resize");
	}else{
		$("#datagrid").datagrid("resize");
	}
}
function switchCommon(datagridId){
	$(".advanced_element").hide();
	$(".advanced_btn_wap").show();
	$(".search_table input").css("float","left");
	if(datagridId){
		datagridId.datagrid("resize");
	}else{
		$("#datagrid").datagrid("resize");
	}
}
function clearFun(datagridObj){
	$('.search_table input').val('');
	$('.search_table select').val("-1");
	if(datagridObj){
		datagridObj.datagrid("load",{});
	}else{
		$("#datagrid").datagrid("load",{});
	}
}