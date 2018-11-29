$(document).ready(function() {
	regmethods.init();
})

var regmethods = {
	init : function() {
		regmethods.initEvent();
	},

	initEvent : function() {
		$('.btn-submit').on('click', regmethods.registerCallbackMethod);
	},
	registerCallbackMethod : function() {
		var methods = $("#auto_methods").val();
		$(".btn-submit").attr('disabled',"true");
		$.ajax({
			url : Helper.getRootPath() + '/you163/api/registerCallbackMethod',
			type : 'post',
			dataType : "json",
			data : {
				methods : methods
			},
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '200') {
					Helper.confirm_one('自助注册成功', function() {
//						location = Helper.getRootPath() + '/system/role/listRole';
						Helper.post('/you163/api/getCallBackMethods');
					});
				} else {
					Helper.alert(data.msg);
					return false;
				}
			},
	        error : function(){
	        	Helper.alert(data.msg);
	        }
		});
	}
}
