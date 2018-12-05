$(document).ready(function() {
	listUserRole.init();
})

var listUserRole = {
	init : function() {
		listUserRole.initEvent();
	},

	initEvent:function(){
		$('.btn-submit').on('click', listUserRole.addUserRole);
		$('#selectAll').on('click', listUserRole.selectAll);
		$('.btn-back').on('click', listUserRole.backUserList);
	},
	selectAll: function(){
		if ($("#selectAll").attr("checked")) {  
	        $(":checkbox").attr("checked", true);  
	    } else {  
	        $(":checkbox").attr("checked", false);  
	    }
	},
	addUserRole : function(){
		var obj = [];
		$("input:checkbox[name='roleId']:checked").each(function() { 
			obj.push($(this).val());
		});
		var ids = obj.join(",");
		var userId = $("#userId").val();
		$(".btn-submit").attr('disabled',"true");
		$.ajax({
			url : Helper.getRootPath() + '/system/user/addUserRole',
			type : 'post',
			dataType : "json",
			data : {
				userId : userId,
				ids : ids
			},
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('新增用户角色成功', function() {
//						location = Helper.getRootPath() + '/system/user/listUser';
						Helper.post('/system/user/listUser');
					});
				} else if (data.code == '06') {
					Helper.alert(data.msg);
					return false;
				} else if (data.code == '10') {
					Helper.alert(data.msg);
					return false;
				} else {
					Helper.alert(data.msg);
					return false;
				}
			},
			error : function(){
				Helper.alert(data.msg);
		    }
		});
	},
	backUserList : function() {
		Helper.post('/system/user/listUser');
	}
}
