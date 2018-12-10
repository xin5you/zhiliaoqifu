$(document).ready(function() {
	listRoleResource.init();
})

var listRoleResource = {
	init : function() {
		listRoleResource.initEvent();
	},

	initEvent : function() {
		$('.btn-submit').on('click', listRoleResource.addRoleResource);
		$('#selectAll').on('click', listRoleResource.selectAll);
		$('.resourceId').on('click', listRoleResource.selectFirst);
		$('.btn-back').on('click', listRoleResource.backRoleList);
	},
	 selectAll: function(){
	 if ($("#selectAll").attr("checked")) {
	 $(":checkbox").attr("checked", true);
	 } else {
	 $(":checkbox").attr("checked", false);
	 }
	 },
	addRoleResource : function() {
		var obj = [];
		$("input:checkbox[name='resourceId']:checked").each(function() {
			obj.push($(this).val());
		});
		var ids = obj.join(",");
		var roleId = $("#roleId").val();
		$(".btn-submit").attr('disabled',"true");
		$.ajax({
			url : Helper.getRootPath() + '/system/role/addRoleResource',
			type : 'post',
			dataType : "json",
			data : {
				roleId : roleId,
				ids : ids
			},
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('新增角色资源成功', function() {
//						location = Helper.getRootPath() + '/system/role/listRole';
						Helper.post('/system/role/listRole');
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
	backRoleList : function() {
		Helper.post('/system/role/listRole');
	}
}
