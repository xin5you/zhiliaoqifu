$(document).ready(function() {
	listRole.init();
})

var listRole = {
	init : function() {
		listRole.initEvent();
	},

	initEvent:function(){
		$('.btn-add').on('click', listRole.intoAddRole);
		$('.btn-edit-role').on('click', listRole.intoEditRole);
		$('.btn-delete').on('click', listRole.deleteRole);
		$('.btn-grant-resource').on('click', listRole.intoAddResource);
		$('.btn-close').on('click',listRole.searchReset);
	},
	
	searchReset : function(){
//		location = Helper.getRootPath() + '/system/role/listRole';
		Helper.post('/system/role/listRole');
	},
	intoAddRole:function(){
		listRole.loadModal(1, $(this).attr('roleId'));
		listRole.initTip(1);
	},
	intoEditRole:function(){
		listRole.loadModal(2, $(this).attr('roleId'));
		listRole.initTip(2);
	},
	initTip: function (intoType) {
        var ttip_validator = $('.form_validation_tip').validate({
            onkeyup: false,
            errorClass: 'error',
            validClass: 'valid',
            ignore: "",
            highlight: function(element) {
                $(element).closest('div').addClass("f_error");
            },
            unhighlight: function(element) {
                $(element).closest('div').removeClass("f_error");
            },
            rules: {
            	roleName: { required: true},
            },
            messages: {
            	roleName: {
                    required: "请输入角色名称"
                }
            },
            invalidHandler: function(form, validator) {
            },
            errorPlacement: function(error, element) {
                var elem = $(element);
                var isPlaceholder = Helper.isPlaceholderSupported();
                if (!error.is(':empty')) {
                    if ((elem.is(':checkbox')) || (elem.is(':radio'))) {
                        elem.filter(':not(.valid)').parent('label').parent('div').find('.error_placement').qtip({
                                overwrite: false,
                                content: error,
                                position: {
                                    my: 'left center',
                                    at: 'center right',
                                    viewport: $(window),
                                    adjust: {
                                        x: 6
                                    }
                                },
                                show: {
                                    event: false,
                                    ready: true
                                },
                                hide: false,
                                style: {
                                    classes: 'ui-tooltip-red ui-tooltip-rounded' // Make it red... the classic error colour!
                                }
                            }).qtip('option', 'content.text', error);
                    } else {
                        var xPoint = 5;
                        elem.filter(':not(.valid)').qtip({
                                overwrite: false,
                                content: error,
                                position: {
                                    my: 'left center',
                                    at: 'center right',
                                    viewport: $(window),
                                    adjust: { x: xPoint, y: 0 }
                                },
                                show: {
                                    event: false,
                                    ready: true
                                },
                                hide: false,
                                style: {
                                    classes: 'ui-tooltip-red ui-tooltip-rounded' // Make it red... the classic error colour!
                                }
                            }).qtip('option', 'content.text', error);
                    };
                }
                else {
                    if ((elem.is(':checkbox')) || (elem.is(':radio'))) {
                        elem.parent('label').parent('div').find('.error_placement').qtip('destroy');
                    } else {
                        elem.qtip('destroy');
                    }
                }
            },
            submitHandler: function(form) {
            	$(".btn-submit").attr('disabled',"true");
            	if(intoType == 1) {
            		listRole.addRole();
            	}else if(intoType == 2) {
            		listRole.editRole();
            	}
                return false;
            },
            success: $.noop
        });
    },
	addRole:function(){
		$.ajax({
            type: 'POST',
            url: Helper.getRootPath() + '/system/role/addRole',
            data: {
        		"name" : $("#roleName_").val(),
        		"seq": $("#seq_").val(),
        		"description": $("#description_").val()
            },
            cache:false,
            dataType: 'json',
            success: function(data){
            	$(".btn-submit").removeAttr("disabled");
                if(data.code == '00') {
                	Helper.confirm_one('新增角色成功', function(){
                		listRole.searchReset();
                	});
                }else if(data.code == '05') {
                	Helper.alert(data.msg);
					return false;
                }else {
                	Helper.alert(data.msg);
                	return false;
                }
            },
			error : function() {
				Helper.alert(data.msg);
			}
        });
    },
    editRole:function(){
		$.ajax({
            type: 'POST',
            url: Helper.getRootPath() + '/system/role/updateRole',
            data: {
            	"id" : $("#roleId").val(),
        		"name" : $("#roleName_").val(),
        		"seq": $("#seq_").val(),
        		"description": $("#description_").val()
            },
            cache:false,
            dataType: 'json',
            success: function(data){
            	$(".btn-submit").removeAttr("disabled");
                if(data.code == '00') {
                	Helper.confirm_one('编辑角色成功', function(){
                		listRole.searchReset();
                	});
                } else if(data.code == '05') {
                	Helper.alert(data.msg);
                	return false;
                } else{
                	Helper.alert(data.msg);
                	return false;
                }
            },
			error : function() {
				Helper.alert(data.msg);
			}
        });
    },
   
    deleteRole : function(){
		var roleId = $(this).attr('roleId');
		if(roleId == null || roleId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("你是否删除记录？",function(){
			$.ajax({								  
				url : Helper.getRootPath() + '/system/role/deleteRole',
				type : 'post',
				dataType : "json",
				data : {
					"roleId": roleId
				},
				success : function (data) {
					if(data.code == '00') {
						Helper.confirm_one('删除角色成功', function(){
							listRole.searchReset();
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
		});
	},
	
	loadModal : function(type, roleId){
		$('#modal').modal({
			backdrop : "static"
		});
		if(type == 1){
			$('#modal_h').html("新增角色");
			return;
		}else if(type == 2){
			$('#modal_h').html("编辑角色");
		}
		
		$.ajax({								  
            url: Helper.getRootPath() + '/system/role/getRole',
            type: 'post',
            dataType : "json",
            data: {
                "roleId": roleId
            },
            success : function (data) {
            	$('#roleId').val(data.id);
            	$('#roleName_').val(data.name);
            	$('#seq_').val(data.seq);
            	$('#description_').val(data.description);
            },
            error : function(){
            	Helper.alert("系统故障，请稍后再试");
            }
	    });
		
		$("#modal").on("hidden.bs.modal", function(e) {
			$("#roleName_").removeAttr('readonly');
			$("#seq_").removeAttr('readonly');
			$("#description_").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	},
	intoAddResource:function(){
		var roleId = $(this).attr('roleId');
//		location = Helper.getRootPath() + '/system/role/listRoleResource/'+roleId;
		Helper.post('/system/role/listRoleResource/'+roleId);
	}
	
}
