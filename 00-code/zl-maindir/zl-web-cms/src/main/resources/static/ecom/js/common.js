//solve ie8 bug	
if(!Array.indexOf)
{
    Array.prototype.indexOf = function(obj)
    {              
        for(var i=0; i<this.length; i++)
        {
            if(this[i]==obj)
            {
                return i;
            }
        }
        return -1;
    }
}

Date.prototype.format = function(format)
  {
	/*
	 * format="yyyy-MM-dd hh:mm:ss";
	 */
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}

Number.prototype.toPercent = function(){
	return Math.round(this * 10000)/100 + '%';
}
 

$(document).ready(function() {
	EcomCommon.init();
});
    
EcomCommon = {
	safeCodeElements: [],	
	safeCodeCallback: null,
		
	init: function(){
		EcomCommon.initEvent();
	},
	initEvent: function(){
		$('#userPWdSubmitBtn').on('click', EcomCommon.updatePwdCommit);
		$('.logout_user').on('click',  EcomCommon.logout);
	},
	loadUpdatePwdModal : function(){
		$('#updatePWDModal').modal({
			backdrop : "static"
		});
	},
	updatePwdCommit:function(){
		var oldPasswrodpage = $("#oldPasswrodPage").val();
		var newPasswordPage =$("#newPasswordPage").val();
		var newPassword2Page =$("#newPassword2Page").val();
		
		if(oldPasswrodpage ==null || oldPasswrodpage==''){
			Helper.alert('请输入您的旧密码');
			return;
		}
		if(newPasswordPage ==null || newPasswordPage==''){
			Helper.alert('请输入您的新密码');
			return;
		}
		if(newPassword2Page ==null || newPassword2Page==''){
			Helper.alert('请输入您的二次确认密码');
			return;
		}
		
		if(newPassword2Page!=newPasswordPage){
			Helper.alert('您输入的新密码和确认密码不匹配，请重新输入');
			return;
		}
		
		Helper.confirm("确定修改密码？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/system/user/updatePwdCommit',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "oldPasswrod" :  $.md5(oldPasswrodpage), 
	                "newPasswordPage" : $.md5(newPasswordPage), 
	                "newPassword2Page" : $.md5(newPassword2Page)
	            },
	            success: function (data) {
	            	if(data.code == '00') {
	            		Helper.confirm_one("密码修改成功", function(){
	            			$.ajax({
		        		        url: Helper.getRootPath() + '/logout',
		        		        type: 'post',
		        		        dataType: "json",
		        		        data: {},
		        		        success: function (data) {
		        		            if (data.code == '00') {
		        		               location.href = Helper.getRootPath() + "/login";
		        		            }
		        		        }
		        		    });
	            		});
	                } else {
	                	Helper.alert(data.msg);
	                	return false;
	                }
	            },
	            error:function(){
	            	Helper.alert(data.msg);
	            }
		    });
		});
	},
	logout: function(){
		Helper.confirm('确认要退出？', function(){
			$.ajax({
		        url: Helper.getRootPath() + '/logout',
		        type: 'post',
		        dataType: "json",
		        data: {},
		        success: function (data) {
		            if (data.code == '00') {
		               location.href = Helper.getRootPath() + "/login";
		            }
		        }
		    });
		});
	}
};

//* tooltips
gebo_tips = {
	init: function() {
		if(!is_touch_device()){
			var shared = {
			style		: {
					classes: 'ui-tooltip-shadow ui-tooltip-tipsy'
				},
				show		: {
					delay: 100
				},
				hide		: {
					delay: 0
				}
			};
			if($('.ttip_b').length) {
				$('.ttip_b').qtip( $.extend({}, shared, {
					position	: {
						my		: 'top center',
						at		: 'bottom center',
						viewport: $(window)
					}
				}));
			}
			if($('.ttip_t').length) {
				$('.ttip_t').qtip( $.extend({}, shared, {
					position: {
						my		: 'bottom center',
						at		: 'top center',
						viewport: $(window)
					}
				}));
			}
			if($('.ttip_l').length) {
				$('.ttip_l').qtip( $.extend({}, shared, {
					position: {
						my		: 'right center',
						at		: 'left center',
						viewport: $(window)
					}
				}));
			}
			if($('.ttip_r').length) {
				$('.ttip_r').qtip( $.extend({}, shared, {
					position: {
						my		: 'left center',
						at		: 'right center',
						viewport: $(window)
					}
				}));
			};
		}
	}
};
	