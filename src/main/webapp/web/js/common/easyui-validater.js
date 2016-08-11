// 自定义验证
	$.extend($.fn.validatebox.defaults.rules, {
	    isEquals: {
			validator: function(value){
				//return value == $(param[0]).val();
				var $pwd = $("input[name='password']").val();
				console.log(value+"=========="+$pwd);
				return $pwd==value;
			},
			message: '前后两次输入的信息不一致！'
	    },
	    validateIdcard: {
			validator: function(value){
				var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
				   return reg.test(value);
			},
			message: '身份证信息输入不合法！'
	    },
	    validateUserName:{
			validator: function(value){
				var reg = /^[a-zA-Z]{1}([a-zA-Z0-9]|[_]){2,19}$/;  
				   return reg.test(value);
			},
			message: '用户名输入不合法！'
	    },
	    validatePhone:{
			validator: function(value){
				var reg = /(^\d{11})|(^[0-9]{4}[-]{1}[0-9]{8})|(^[0-9]{3}[-]{1}[0-9]{3}[-]{1}[0-9]{4})$/;  
				   return reg.test(value);
			},
			message: '电话信息输入不合法！'
	    },
	    validateAge:{
			validator: function(value){
				var reg = /(^\d{11})|(^[0-9]{4}[-]{1}[0-9]{8})$/;  
				   return reg.test(value);
			},
			message: '电话信息输入不合法！'
	    },
	    mobile:{ // 移动手机号码
	    	validator: function (value) {  
	            var reg = /^1[3|4|5|7|8|9]\d{9}$/;  
	            return reg.test(value);  
	        },  
	        message: '手机号码格式不准确.例:13762285845'
	    },
	    phone: { // 电话号码  正确格式为：XXXX-XXXXXXX，XXXX-XXXXXXXX，XXX-XXXXXXX，XXX-XXXXXXXX，XXXXXXX，XXXXXXXX。
	    	validator : function(value) {  
            	//var reg = /(^[0-9]{4}[-]{1}[0-9]{8})$/;/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/
	    		//var reg = /^\d{3,4}?\d{7,8}$/; /d{3}-/d{8}|/d{4}-/d{7} 
	    		var reg = /^(\(\d{3,4}\)|\d{3,4}-)?\d{7,8}$/;
				return reg.test(value);
            },  
            message : '电话号码格式不准确.例:0755-4405222或010-87888822'
        },
        postCode:{ // 国内邮编验证 
	    	validator: function (value) {  
	            var reg = /^[1-9]\d{5}$/;  
	            return reg.test(value);  
	        },  
	        message: '邮编必须是非0开始的6位数字.例:518000'
	    },
	    currencyCode:{ // 币种编码验证 (匹配大写字母)
	    	validator: function (value) {
	            var reg = /^[A-Z]+$/;
	            return reg.test(value);
	        },
	        message: '币种编码必须是大写字母.例:RMB'
	    },
	    currencyName:{ // 币种名称验证 
	    	validator: function (value) {
	            var reg = /^[\u4e00-\u9fa5]+$/;
	            return reg.test(value);
	        },
	        message: '币种名称必须是中文.例:人民币'
	    },
	    validateSmall:{ // 匹配小写字母 
	    	validator: function (value) {
	            var reg = /^[a-z]+$/;
	            return reg.test(value);
	        },
	        message: '必须是小写字母.例:a'
	    },
	    validateEg:{ // 匹配英文 
	    	validator: function (value) {
	            var reg = /^[a-zA-Z]+$/;
	            return reg.test(value);
	        },
	        message: '必须是英文.例:English'
	    },
	    validateNumber:{ // 匹配英文 
	    	validator: function (value) {
	            var reg = /^[1-9]([0-9])*$/;
	            return reg.test(value);
	        },
	        message: '必须是正整数.例:123'
	    }
	});