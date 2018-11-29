$(document).ready(function() {
	listUser.init();
})

var listUser = {
	init : function() {
		listUser.initEvent();
	},

	initEvent:function(){
		$('.btn-reset').on('click', listUser.searchReset);
		$('.btn-add').on('click', listUser.intoAddUser);
		$('.btn-edit').on('click', listUser.intoEditUser);
		$('.btn-delete').on('click', listUser.deleteUser);
		$('.btn-close').on('click',listUser.searchReset);
		$('.btn-grant-role').on('click', listUser.intoAddRole);
	},
	
	initTip: function (intoType) {
        var ttip_validator = $('.form_validation_tip').validate({
        	onkeyup: false,
            errorClass: 'error',
            validClass: 'valid',
            highlight: function (element) {
                $(element).closest('div').addClass("f_error");
            },
            unhighlight: function (element) {
                $(element).closest('div').removeClass("f_error");
            },
            rules:{
            	loginName: { required: true},
            	uname: { required: true},
            	upassword: { required: true}
            },
            messages: {
            	loginName: { required: "请输入登录名"},
            	uname: { required: "请输入姓名"},
            	upassword: { required: "请输入密码"}
            },
            invalidHandler: function(form, validator) {
            },
            errorPlacement: function(error, element) {
                var elem = $(element);
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
                            })
                            // If we have a tooltip on this element already, just update its content
                            .qtip('option', 'content.text', error);

                    };
                }
                // If the error is empty, remove the qTip
                else {
                    if ((elem.is(':checkbox')) || (elem.is(':radio'))) {
                        elem.parent('label').parent('div').find('.error_placement').qtip('destroy');
                    } else {
                        elem.qtip('destroy');
                    }
                }
            },
            submitHandler: function (form) {
            	$(".btn-submit").attr('disabled',"true");
            	if(intoType == 1) {
            		listUser.addUser();
            	}else if(intoType == 2) {
            		listUser.editUser();
            	}
                return false;
            },
            success: $.noop 
        });
    },
	
	searchReset : function(){
//		location = Helper.getRootPath() + '/system/user/listUser';
		Helper.post('/system/user/listUser');
	},
	intoAddUser : function(){
		listUser.loadModal(1, $(this).attr('userId'));
		listUser.initTip(1);
	},
	intoEditUser : function(){
		listUser.loadModal(2, $(this).attr('userId'));
		listUser.initTip(2);
		$("#password_").click(function(){
			$("#password_").val("");
		});
	},
	addUser:function(){
		$.ajax({
            type: 'POST',
            url: Helper.getRootPath() + '/system/user/addUser',
            data: {
        		"loginName" : $("#login_name").val(),
        		"name": $("#name_").val(),
        		"password": $("#password_").val()
            },
            cache:false,
            dataType: 'json',
            success: function(data){
            	$(".btn-submit").removeAttr("disabled");
                if(data.code == '00') {
                	Helper.confirm_one('新增用户成功', function(){
                		listUser.searchReset();
                	});
                }else if(data.code == '05'){
                	Helper.alert(data.msg);
                	return false;
                }else{
                	Helper.alert(data.msg);
                	return false;
                }
            },
			error : function(){
				Helper.alert(data.msg);
			}
        });
    },
    editUser:function(){
		$.ajax({
            type: 'POST',
            url: Helper.getRootPath() + '/system/user/updateUser',
            data: {
            	"id" : $("#user_id").val(),
        		"loginName" : $("#login_name").val(),
        		"name": $("#name_").val(),
        		"password": $("#password_").val()
            },
            cache:false,
            dataType: 'json',
            success: function(data){
            	$(".btn-submit").removeAttr("disabled");
                if(data.code == '00') {
                	Helper.confirm_one('编辑用户成功', function(){
                		listUser.searchReset();
                	});
                }else if(data.code == '05'){
                	Helper.alert(data.msg);
                	return false;
                }else{
                	Helper.alert(data.msg);
                	return false;
                }
            },
			error : function(){
				Helper.alert(data.msg);
			}
        });
    },
	deleteUser : function(){
		var userId = $(this).attr('userId');
		if(userId == null || userId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("你是否删除记录？",function(){
			$.ajax({								  
				url : Helper.getRootPath() + '/system/user/deleteUser',
				type : 'post',
				dataType : "json",
				data : {
					"userId": userId
				},
				success : function (data) {
					if(data.code == '00') {
						Helper.confirm_one('删除用户成功', function(){
							listUser.searchReset();
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
	loadModal : function(type, userId){
		$('#modal').modal({
			backdrop : "static"
		});
		if(type == 1){
			$('#modal_h').html("新增用户");
			return;
		}else if(type == 2){
			$('#modal_h').html("编辑用户");
		}
		
		$.ajax({								  
            url: Helper.getRootPath() + '/system/user/getUser',
            type: 'post',
            dataType : "json",
            data: {
                "userId": userId
            },
            success : function (data) {
            	$('#user_id').val(data.id);
            	$('#login_name').val(data.loginName);
            	$('#name_').val(data.name);
            	$('#password_').val(data.password);
            },
            error : function(){
            	Helper.alert("系统故障，请稍后再试");
            }
	    });
		
		$("#modal").on("hidden.bs.modal", function(e) {
			$("#login_name").removeAttr('readonly');
			$("#name_").removeAttr('readonly');
			$("#password_").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	},
	
	intoAddRole:function(){
		var userId = $(this).attr('userId');
//		location = Helper.getRootPath() + '/system/user/listUserRole/'+userId;
		Helper.post('/system/user/listUserRole/'+userId);
	}
}
