$(document).ready(function() {
    listCoupon.init();
})

var listCoupon = {
	init : function() {
        listCoupon.initEvent();
	},

	initEvent:function(){
		$('.btn-reset').on('click', listCoupon.searchReset);
		$('.btn-add').on('click', listCoupon.intoAddCoupon);
		$('.btn-edit').on('click', listCoupon.intoEditCoupon);
		$('.btn-delete').on('click', listCoupon.deleteCoupon);
		$('.btn-close').on('click',listCoupon.searchReset);
        $('#specImgFile').on('change', listCoupon.imageUpload);
        $('.btn-addCouponValues').on('click',listCoupon.intoAddCouponValues);
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
                spec_name: { required: true},
                spec_img: { required: true},
                spec_order: { required: true}
            },
            messages: {
                spec_name: { required: "请输入卡券名称"},
                spec_img: { required: "请选择卡券图片"},
                spec_order: { required: "请输入可售数量"}
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
                    listCoupon.addCoupon();
            	}else if(intoType == 2) {
                    listCoupon.editCoupon();
            	}
                return false;
            },
            success: $.noop 
        });
    },
    imageUpload : function() {
        var iconImageFile = $("#iconImageFile").val();
        $("#iconImage").val(iconImageFile);
    },
	searchReset : function(){
		Helper.post('/couponManage/getCouponsInfList');
	},
	intoAddCoupon : function(){
        listCoupon.loadModal(1, $(this).attr('couponCode'));
        listCoupon.initTip(1);
	},
    intoEditCoupon : function(){
        listCoupon.loadModal(2, $(this).attr('couponCode'));
        listCoupon.initTip(2);
	},
    addCoupon:function(){
        $.ajax({
            type: 'POST',
            url: Helper.getRootPath() + '/couponManage/addCouponValues',
            data: new FormData($("#couponInfo")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType: 'json',
            success: function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('新增卡券信息成功', function() {
                        listCoupon.searchReset();
                    });
                } else {
                    Helper.alert(data.msg);
                    return false;
                }
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
        });
    },
    editCoupon:function(){
        $.ajax({
            type : 'POST',
            url : Helper.getRootPath() + '/couponManage/addCouponValues',
            data: new FormData($("#couponInfo")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType: 'json',
            success : function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('编辑规格信息成功', function() {
                        listCoupon.searchReset();
                    });
                } else {
                    Helper.alert(data.msg);
                    return false;
                }
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
        });
    },
	deleteCoupon : function(){
		var couponCode = $(this).attr('couponCode');
		if(couponCode == null || couponCode == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("你是否删除该记录？",function(){
			$.ajax({								  
				url : Helper.getRootPath() + '/couponManage/deleteCoupon',
				type : 'post',
				dataType : "json",
				data : {
					"couponCode": couponCode
				},
				success : function (data) {
					if(data.code == '00') {
						Helper.confirm_one('删除卡券信息成功', function(){
                            listCoupon.searchReset();
	                	});
					} else {
						Helper.alert(data.msg);
						return false;
					}
				},
                error : function() {
                    Helper.alert("网络异常，请稍后再试");
                }
			});
		});
	},
	loadModal : function(type, couponCode){
		$('#modal').modal({
			backdrop : "static"
		});
		if(type == 1){
			$('#modal_h').html("新增卡券信息");
			return;
		}else if(type == 2){
			$('#modal_h').html("编辑卡券信息");
		}
		
		$.ajax({								  
            url: Helper.getRootPath() + '/couponManage/getCouponsInf',
            type: 'post',
            dataType : "json",
            data: {
                "couponCode": couponCode
            },
            success : function (data) {
            	$('#couponCode').val(data.couponCode);
            	$('#coupon_Name').val(data.couponName);
            	$('#price').val(data.price);
            	$('#couponType').val(data.couponType);
                $('#couponDesc').val(data.couponDesc);
                $('#iconImage').val(data.iconImage);
                $('#totalNum').val(data.totalNum);
                $('#availableNum').val(data.availableNum);
                $('#remarks').val(data.remarks);
                $('#b_id').val(data.bid);
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
	    });
		
		$("#modal").on("hidden.bs.modal", function(e) {
			$("#spec_name").removeAttr('readonly');
			$("#spec_img").removeAttr('readonly');
			$("#spec_order").removeAttr('readonly');
            $("#remarks").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	},
    intoAddCouponValues : function () {
        var specId = $(this).attr("couponCode");
        Helper.post('/couponManage/addCouponsInf?couponCode='+couponCode);
    }
}
