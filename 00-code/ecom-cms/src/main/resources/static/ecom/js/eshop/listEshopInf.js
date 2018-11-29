$(document).ready(function() {
	listEshopInf.init();
})

var listEshopInf = {
	init : function() {
		listEshopInf.initEvent();
	},

	initEvent : function() {
		$('.btn-reset').on('click', listEshopInf.searchReset);
		$('.btn-add').on('click', listEshopInf.intoAdd);
		$('.btn-edit').on('click', listEshopInf.intoEdit);
		$('.btn-delete').on('click', listEshopInf.deleteEshopInf);
		$('.btn-close').on('click', listEshopInf.searchReset);
		$('.btn-add-banner').on('click', listEshopInf.intoAddBanner);
		$('.btn-add-routes').on('click', listEshopInf.intoAddRoutes);
		$('#eshop_name').on('change', listEshopInf.nameChange);
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
				mchnt_code : {
					required : true
				},
				shop_code : {
					required : true
				}
			},
			messages : {
				mchnt_code : {
					required : "商户号为空"
				},
				shop_code : {
					required : "门店号为空"
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
					listEshopInf.addEshopInf();
				} else if (intoType == 2) {
					listEshopInf.editEshopInf();
				}
				return false;
			},
			success : $.noop
		});
	},
	intoAdd : function() {
		listEshopInf.loadModal(1, $(this).attr('eshopId'));
		listEshopInf.initTip(1);
	},
	intoEdit : function() {
		listEshopInf.loadModal(2, $(this).attr('eshopId'));
		listEshopInf.initTip(2);
	},
	addEshopInf : function() {
		var name = $("#eshop_name").val();
		if (name == "0") { 
			Helper.alert("请选择商城名称");
		} else {
			$.ajax({
				type : 'POST',
				url : Helper.getRootPath() + '/eshop/addEshopInf',
				data : {
					"mchntCode" : $("#mchnt_code").val(),
					"shopCode" : $("#shop_code").val(),
					"eshopName" : $("#eshop_name").val()
				},
				cache : false,
				dataType : 'json',
				success : function(data) {
					$(".btn-submit").removeAttr("disabled");
					if (data.code == '00') {
						Helper.confirm_one('新增商城信息成功', function() {
							listEshopInf.searchReset();
						});
					} else if (data.code == '01') {
						Helper.alert(data.msg);
						return false;
					} else if (data.code == '05') {
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
		}
	},
	editEshopInf : function() {
		var name = $("#eshop_name").val();
		if (name == "0") { 
			Helper.alert("请选择商城名称");
		} else {
			$.ajax({
				type : 'POST',
				url : Helper.getRootPath() + '/eshop/editEshopInf',
				data : {
					"id" : $("#eshop_id").val(),
					"mchntCode" : $("#mchnt_code").val(),
					"shopCode" : $("#shop_code").val(),
					"eshopName" : $("#eshop_name").val()
				},
				cache : false,
				dataType : 'json',
				success : function(data) {
					$(".btn-submit").removeAttr("disabled");
					if (data.code == '00') {
						Helper.confirm_one('编辑商城信息成功', function() {
							listEshopInf.searchReset();
						});
					} else if (data.code == '02') {
						Helper.alert(data.msg);
						return false;
					} else if (data.code == '05') {
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
		}
	},
	deleteEshopInf : function() {
		var id = $(this).attr('eshopId');
		if (id == null || id == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定删除该商城？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/eshop/deleteEshopInf',
				type : 'post',
				dataType : "json",
				data : {
					"id" : id
				},
				success : function(data) {
					if (data.code == '00') {
						Helper.confirm_one('删除商城成功', function() {
							listEshopInf.searchReset();
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
		Helper.post('/eshop/listEshopInf');
	},
	nameChange : function() {
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/eshop/nameChange',
			data : {
				"eshopName" : $("#eshop_name").val(),
			},
			cache : false,
			dataType : 'json',
			success : function(data) {
				$("#mchnt_code").val(data.mchntCode);
				$("#shop_code").val(data.shopCode);
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});
	},
	loadModal : function(type, eshopId) {
		$('#modal').modal({
			backdrop : "static"
		});
		if (type == 1) {
			$('#modal_h').html("添加商城");
			return;
		} else if (type == 2) {
			$('#modal_h').html("编辑商城");
		}

		$.ajax({
			url : Helper.getRootPath() + '/eshop/getEshopInf',
			type : 'post',
			dataType : "json",
			data : {
				"id" : eshopId
			},
			success : function(data) {
				$('#eshop_id').val(data.id);
				$('#mchnt_code').val(data.mchntCode);
				$('#shop_code').val(data.shopCode);
				$('#eshop_name').val(data.eshopName);
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});

		$("#modal").on("hidden.bs.modal", function(e) {
			$("#mchnt_code").removeAttr('readonly');
			$("#shop_code").removeAttr('readonly');
			$("#eshop_name").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	},
	intoAddBanner:function(){
		var eshopId = $(this).attr('eshopId');
		Helper.post('/eshopBanner/listEshopBanner/'+eshopId);
	},
	intoAddRoutes:function(){
		var eshopId = $(this).attr('eshopId');
		Helper.post('/eshopRoutes/listEshopRoutes/'+eshopId);
	}
}
