$(document).ready(function() {
	listEsetting.init();
})

var listEsetting = {
	init : function() {
		listEsetting.initEvent();
		listEsetting.checkMoneyInit();
	},
	checkMoneyInit : function() {
		$("#full_money").keyup(function () {
            var reg = $(this).val().match(/\d+\.?\d{0,2}/);
            var txt = '';
            if (reg != null) {
                txt = reg[0];
            }
            $(this).val(txt);
        }).change(function () {
            $(this).keyup();
        });
		$("#ecom_freight").keyup(function () {
            var reg = $(this).val().match(/\d+\.?\d{0,2}/);
            var txt = '';
            if (reg != null) {
                txt = reg[0];
            }
            $(this).val(txt);
        }).change(function () {
            $(this).keyup();
        });
	},
	initEvent : function() {
		$('.btn-reset').on('click', listEsetting.searchReset);
		$('.btn-add').on('click', listEsetting.intoAdd);
		$('.btn-edit').on('click', listEsetting.intoEdit);
		$('.btn-delete').on('click', listEsetting.deleteEserve);
		$('.btn-close').on('click', listEsetting.searchReset);
		$('.btn-view').on('click', listEsetting.intoView);
		$('.btn-add-banner').on('click', listEsetting.intoAddBanner);
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
				ecom_Code : {
					required : true
				},
				ecom_Type : {
					required : true
				},
				full_money : {
					required : true
				},
				ecom_freight : {
					required : true
				},
				shop_desc : {
					required : true
				}
			},
			messages : {
				ecom_name : {
					required : "请输入电商名称"
				},
				ecom_Code : {
					required : "请输入电商代码"
				},
				ecom_Type : {
					required : "请输入电商类别"
				},
				full_money : {
					required : "请输入包邮金额"
				},
				ecom_freight : {
					required : "请输入运费"
				},
				shop_desc : {
					required : "请输入购物描述"
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
//				$(".btn-submit").attr('disabled',"true");
				var full_money = $('#full_money').val();
				var ecom_freight = $('#ecom_freight').val();
				var reg = /^\d{1,8}(\.\d{1,2})?$/;		//验证有1-2位小数的正实数
				if(!reg.test(full_money)){
					Helper.alert("包邮金额整数最多输入8位，或者有二位小数的数字，且整数总长度不超过8");
					return false;
				}
				if(!reg.test(ecom_freight)){
					Helper.alert("运费整数最多输入8位，或者有二位小数的数字，且整数总长度不超过8");
					return false;
				}
				if (intoType == 1) {
					listEsetting.addEsetting();
				} else if (intoType == 2) {
					listEsetting.editEsetting();
				}
				return false;
			},
			success : $.noop
		});
	},
	intoAdd : function() {
		listEsetting.loadModal(1, $(this).attr('id'));
		listEsetting.initTip(1);
	},
	intoEdit : function() {
		listEsetting.loadModal(2, $(this).attr('id'));
		listEsetting.initTip(2);
	},
	intoView : function() {
		listEsetting.loadModal(3, $(this).attr('id'));
//		listEsetting.initTip(2);
	},
	addEsetting : function() {
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/eSetting/addEsetting',
			data : {
				"ecomName" : $("#ecom_name").val(),
				"ecomCode" : $("#ecom_Code").val(),
				"ecomType" : $("#ecom_Type").val(),
				"fullMoney" : $("#full_money").val(),
				"ecomFreight" : $("#ecom_freight").val(),
				"shopDesc" : $("#shop_desc").val()
			},
			cache : false,
			dataType : 'json',
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('新增电商服务设置成功', function() {
						listEsetting.searchReset();
					});
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
	editEsetting : function() {
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/eSetting/editEsetting',
			data : {
				"id" : $("#eserve_id").val(),
				"ecomName" : $("#ecom_name").val(),
				"ecomCode" : $("#ecom_Code").val(),
				"ecomType" : $("#ecom_Type").val(),
				"fullMoney" : $("#full_money").val(),
				"ecomFreight" : $("#ecom_freight").val(),
				"shopDesc" : $("#shop_desc").val()
			},
			cache : false,
			dataType : 'json',
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('编辑电商服务设置成功', function() {
						listEsetting.searchReset();
					});
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
	deleteEserve : function() {
		var id = $(this).attr('id');
		if (id == null || id == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定删除该商城？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/eSetting/deleteEserve',
				type : 'post',
				dataType : "json",
				data : {
					"id" : id
				},
				success : function(data) {
					if (data.code == '00') {
						Helper.confirm_one('删除商城成功', function() {
							listEsetting.searchReset();
						});
					} else if (data.code == '03') {
						Helper.alert(data.msg);
						return false;
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
		Helper.post('/eSetting/listEsetting');
	},
	loadModal : function(type, id) {
		$('#modal').modal({
			backdrop : "static"
		});
		if (type == 1) {
			$('#modal_h').html("添加电商服务");
			return;
		} else if (type == 2) {
			$('#modal_h').html("编辑电商服务");
		}else{
			$('#modal_h').html("电商服务详情");
		}

		$.ajax({
			url : Helper.getRootPath() + '/eSetting/getEsetting',
			type : 'post',
			dataType : "json",
			data : {
				"id" : id
			},
			success : function(data) {
				$('#eserve_id').val(data.id);
				 $("#ecom_name").val(data.ecomName);
				 $("#ecom_Code").val(data.ecomCode);
				 $("#ecom_Type").val(data.ecomType);
				 $("#full_money").val(data.fullMoney);
				 $("#ecom_freight").val(data.ecomFreight);
				 $("#shop_desc").text(data.shopDesc);
				 if(type == 3){
					 $('#ecom_name').attr('disabled',true);
					 $('#ecom_Code').attr('disabled',true);
					 $('#ecom_Type').attr('disabled',true);
					 $('#full_money').attr('disabled',true);
					 $('#ecom_freight').attr('disabled',true);
					 $('#shop_desc').attr('disabled',true);
					 $('.btn-submit').hide();
				 }
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});

//		$("#modal").on("hidden.bs.modal", function(e) {
//			$("#mchnt_code").removeAttr('readonly');
//			$("#shop_code").removeAttr('readonly');
//			$("#eshop_name").removeAttr('readonly');
//			$(".btn-submit").removeAttr('disabled');
//		});
	},
	intoAddBanner: function(){
		var id = $(this).attr('id');
		Helper.post('/eSetting/intoAddBanner?settingId='+id);
	}
}
