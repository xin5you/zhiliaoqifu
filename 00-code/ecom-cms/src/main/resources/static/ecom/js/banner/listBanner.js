$(document).ready(function() {
	listBanner.init();
})

var listBanner = {
	init : function() {
		listBanner.initEvent();
	},

	initEvent : function() {
		$('.btn-reset').on('click', listBanner.searchReset);
		$('.btn-add').on('click', listBanner.intoAdd);
		$('.btn-edit').on('click', listBanner.intoEdit);
		$('.btn-delete').on('click', listBanner.deleteBanner);
		$('.btn-close').on('click', listBanner.searchReset);
		$('#imageUrlFile').on('change', listBanner.fileUpload);
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
				bannerUrl : {
					required : true
				},
				imageUrl : {
					required : true
				},
				remarks_ : {
					required : true
				}
			},
			messages : {
				bannerUrl : {
					required : "请输入图文链接"
				},
				imageUrl : {
					required : "请选择图片"
				},
				remarks_ : {
					required : "请输入备注"
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
					listBanner.addBanner();
				} else if (intoType == 2) {
					listBanner.editBanner();
				}
				return false;
			},
			success : $.noop
		});
	},
	intoAdd : function() {
		listBanner.loadModal(1, $(this).attr('bannerId'));
		listBanner.initTip(1);
	},
	intoEdit : function() {
		listBanner.loadModal(2, $(this).attr('bannerId'));
		listBanner.initTip(2);
	},
	addBanner : function() {
		$.ajax({
			type: 'POST',
			url: Helper.getRootPath() + '/banner/addBanner',
			data: new FormData($("#bannerInfo")[0]),
			processData: false,
			contentType: false, 
            async: false,  
            cache: false,  
			dataType: 'json',
			success: function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('新增Banner信息成功', function() {
						listBanner.searchReset();
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
	editBanner : function() {
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/banner/editBanner',
			data: new FormData($("#bannerInfo")[0]),
			processData: false,
			contentType: false, 
            async: false,  
            cache: false,  
			dataType: 'json',
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('编辑Banner信息成功', function() {
						listBanner.searchReset();
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
	deleteBanner : function() {
		var id = $(this).attr('bannerId');
		if (id == null || id == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定删除该Banner信息？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/banner/deleteBanner',
				type : 'post',
				dataType : "json",
				data : {
					"id" : id
				},
				success : function(data) {
					if (data.code == '00') {
						Helper.confirm_one('删除Banner成功', function() {
							listBanner.searchReset();
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
		Helper.post('/banner/listBanner');
	},
	fileUpload : function(){
		var imgUrl = $("#imageUrlFile").val();
		$("#imageUrl").val(imgUrl);
	},
	
	loadModal : function(type, bannerId) {
		$('#modal').modal({
			backdrop : "static"
		});
		if (type == 1) {
			$('#modal_h').html("添加Banner");
			return;
		} else if (type == 2) {
			$('#modal_h').html("编辑Banner");
		}

		$.ajax({
			url : Helper.getRootPath() + '/banner/getBanner',
			type : 'post',
			dataType : "json",
			data : {
				"id" : bannerId
			},
			success : function(data) {
				$('#banner_id').val(data.id);
				$('#imageUrl').val(data.imageUrl);
				$('#bannerUrl').val(data.bannerUrl);
				$('#spec').val(data.spec);
				$('#remarks_').val(data.remarks);
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});

		$("#modal").on("hidden.bs.modal", function(e) {
			$("#imageUrl").removeAttr('readonly');
			$("#bannerUrl").removeAttr('readonly');
			$("#spec").removeAttr('readonly');
			$("#remarks_").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	}
}
