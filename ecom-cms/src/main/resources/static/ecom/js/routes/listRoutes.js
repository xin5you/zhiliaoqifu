$(document).ready(function() {
	listRoutes.init();
})

var listRoutes = {
	init : function() {
		listRoutes.initEvent();
	},

	initEvent : function() {
		$('.btn-reset').on('click', listRoutes.searchReset);
		$('.btn-add').on('click', listRoutes.intoAdd);
		$('.btn-edit').on('click', listRoutes.intoEdit);
		$('.btn-delete').on('click', listRoutes.deleteRoutes);
		$('.btn-close').on('click', listRoutes.searchReset);
		$('#loginFile').on('change', listRoutes.fileUpload);
	},
	initTip : function(intoType) {
		var ttip_validator = $('.form_validation_tip').validate({
			onkeyup : false,
			errorClass : 'error',
			validClass : 'valid',
			highlight : function(element) {
				$(element).closest('div').addClass("f_error");
			},
			unhighlight : function(element) {
				$(element).closest('div').removeClass("f_error");
			},
			invalidHandler : function(form, validator) {
			},
			rules : {
				ecom_name : {
					required : true
				},
				ecomUrl : {
					required : true
				},
				orderUrl : {
					required : true
				},
				ecomLogo : {
					required : true
				}
			},
			messages : {
				ecom_name : {
					required : "请输入电商名称"
				},
				ecomUrl : {
					required : "请输入入口链接"
				},
				orderUrl : {
					required : "请输入订单链接"
				},
				ecomLogo : {
					required : "请选择图片"
				}
			},
			errorPlacement : function(error, element) {
				var elem = $(element);
				if (!error.is(':empty')) {
					if ((elem.is(':checkbox')) || (elem.is(':radio'))) {
						elem.filter(':not(.valid)').parent('label').parent('div').find('.error_placement').qtip({
							overwrite : false,
							content : error,
							position : {
								my : 'left center',
								at : 'center right',
								viewport : $(window),
								adjust : {
									x : 6
								}
							},
							show : {
								event : false,
								ready : true
							},
							hide : false,
							style : {
								classes : 'ui-tooltip-red ui-tooltip-rounded' 
							}
						}).qtip('option','content.text', error);
					} else {
						var xPoint = 5;
						elem.filter(':not(.valid)').qtip({
							overwrite : false,
							content : error,
							position : {
								my : 'left center',
								at : 'center right',
								viewport : $(window),
								adjust : {
									x : xPoint,
									y : 0
								}
							},
							show : {
								event : false,
								ready : true
							},
							hide : false,
							style : {
								classes : 'ui-tooltip-red ui-tooltip-rounded'
							}
						}).qtip('option','content.text', error);
					}
					;
				} else {
					if ((elem.is(':checkbox')) || (elem.is(':radio'))) {
						elem.parent('label').parent('div').find('.error_placement').qtip('destroy');
					} else {
						elem.qtip('destroy');
					}
				}
			},
			submitHandler : function(form) {
				$(".btn-submit").attr('disabled',"true");
				if (intoType == 1) {
					listRoutes.addRoutes();
				} else if (intoType == 2) {
					listRoutes.editRoutes();
				}
				return false;
			},
			success : $.noop
		});
	},
	intoAdd : function() {
		listRoutes.loadModal(1, $(this).attr('routesId'));
		listRoutes.initTip(1);
	},
	intoEdit : function() {
		listRoutes.loadModal(2, $(this).attr('routesId'));
		listRoutes.initTip(2);
	},
	addRoutes : function() {
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/routes/addRoutes',
			data: new FormData($("#routesInfo")[0]),
			processData: false,
			contentType: false, 
            async: false,  
            cache: false,  
			dataType: 'json',
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('新增电商入口信息成功', function() {
						listRoutes.searchReset();
					});
				} else {
					Helper.alert(data.msg);
					return false;
				}
			},
			error : function() {
				Helper.alert(data.msg);
			}
		});
	},
	editRoutes : function() {
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/routes/editRoutes',
			data: new FormData($("#routesInfo")[0]),
			processData: false,
			contentType: false, 
            async: false,  
            cache: false,  
			dataType: 'json',
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('编辑电商入口信息成功', function() {
						listRoutes.searchReset();
					});
				} else {
					Helper.alert(data.msg);
					return false;
				}
			},
			error : function() {
				Helper.alert(data.msg);
			}
		});
	},
	deleteRoutes : function() {
		var id = $(this).attr('routesId');
		if (id == null || id == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定删除该电商入口信息？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/routes/deleteRoutes',
				type : 'post',
				dataType : "json",
				data : {
					"id" : id
				},
				success : function(data) {
					if (data.code == '00') {
						Helper.confirm_one('删除电商入口成功', function() {
							listRoutes.searchReset();
						});
					} else {
						Helper.alert(data.msg);
						return false;
					}
				},
				error : function() {
					Helper.alert(data.msg);
				}
			});
		});
	},
	searchReset : function() {
		Helper.post('/routes/listRoutes');
	},
	fileUpload : function(){
		var logoUrl = $("#loginFile").val();
		$("#ecomLogo").val(logoUrl);
	},

	loadModal : function(type, routesId) {
		$('#modal').modal({
			backdrop : "static"
		});
		if (type == 1) {
			$('#modal_h').html("添加电商");
			return;
		} else if (type == 2) {
			$('#modal_h').html("编辑电商");
		}

		$.ajax({
			url : Helper.getRootPath() + '/routes/getRoutes',
			type : 'post',
			dataType : "json",
			data : {
				"id" : routesId
			},
			success : function(data) {
				$('#routes_id').val(data.id);
				$('#ecom_name').val(data.ecomName);
				$('#ecomCode').val(data.ecomCode);
				$('#ecomUrl').val(data.ecomUrl);
				$('#ecomType').val(data.ecomType);
				$('#ecomLogo').val(data.ecomLogo);
				$('#orderUrl').val(data.orderUrl);
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});

		$("#modal").on("hidden.bs.modal", function(e) {
			$("#ecom_name").removeAttr('readonly');
			$("#ecomCode").removeAttr('readonly');
			$("#ecomUrl").removeAttr('readonly');
			$("#ecomType").removeAttr('readonly');
			$("#ecomLogo").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	}
};