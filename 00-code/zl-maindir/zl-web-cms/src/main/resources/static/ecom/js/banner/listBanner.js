$(document).ready(function() {
	listBanner.init();
})

var listBanner = {
	init : function() {
        listBanner.initEvent();
	},

	initEvent:function(){
		$('.btn-reset').on('click', listBanner.searchReset);
		$('.btn-add').on('click', listBanner.intoAddBanner);
		$('.btn-edit').on('click', listBanner.intoEditBanner);
		$('.btn-delete').on('click', listBanner.deleteBanner);
		$('.btn-close').on('click',listBanner.searchReset);
        $('#imageUrlFile').on('change', listBanner.imageUpload);
        /*$('.btn-updateDisable').on('click',listBanner.intoUpdateDisable);
        $('.btn-updateDisableCommit').on('click',listBanner.updateDisableCommit);*/
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
                image_url: { required: true},
                spec: { required: true},
                position: { required: true},
                sort: { required: true}
            },
            messages: {
                image_url: { required: "请选择banner图片"},
                spec: { required: "请输入规格"},
                position: { required: "请输入banner位置"},
                sort: { required: "请输入排序号"}
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
                    listBanner.addBanner();
            	}else if(intoType == 2) {
                    listBanner.editBanner();
            	}
                return false;
            },
            success: $.noop 
        });
    },
    imageUpload : function() {
        var imageUrlFile = $("#imageUrlFile").val();
        $("#image_url").val(imageUrlFile);
    },
	searchReset : function(){
		Helper.post('/banner/getBannerList');
	},
	intoAddBanner : function(){
        listBanner.loadModal(1, $(this).attr('bannerId'));
        listBanner.initTip(1);
	},
	intoEditBanner : function(){
        listBanner.loadModal(2, $(this).attr('bannerId'));
        listBanner.initTip(2);
	},
	addBanner:function(){
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
                    Helper.confirm_one('新增banner信息成功', function() {
                        listBanner.searchReset();
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
    editBanner:function(){
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
                    Helper.confirm_one('编辑banner信息成功', function() {
                        listBanner.searchReset();
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
	deleteBanner : function(){
		var bannerId = $(this).attr('bannerId');
		if(bannerId == null || bannerId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("你是否删除该记录？",function(){
			$.ajax({								  
				url : Helper.getRootPath() + '/banner/deleteBanner',
				type : 'post',
				dataType : "json",
				data : {
					"bannerId": bannerId
				},
				success : function (data) {
					if(data.code == '00') {
						Helper.confirm_one('删除banner信息成功', function(){
                            listBanner.searchReset();
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
	loadModal : function(type, bannerId){
		$('#modal').modal({
			backdrop : "static"
		});
		if(type == 1){
			$('#modal_h').html("新增banner信息");
			return;
		}else if(type == 2){
			$('#modal_h').html("编辑banner信息");
		}
		
		$.ajax({								  
            url: Helper.getRootPath() + '/banner/getBanner',
            type: 'post',
            dataType : "json",
            data: {
                "bannerId": bannerId
            },
            success : function (data) {
            	$('#banner_id').val(data.id);
            	$('#image_url').val(data.imageUrl);
            	$('#banner_url').val(data.bannerUrl);
            	$('#spec').val(data.spec);
            	$('#position').val(data.position);
                $('#banner_text').val(data.bannerText);
                $('#sort').val(data.sort);
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
	    });
		
		$("#modal").on("hidden.bs.modal", function(e) {
			$("#image_url").removeAttr('readonly');
			$("#banner_url").removeAttr('readonly');
			$("#spec").removeAttr('readonly');
            $("#position").removeAttr('readonly');
            $("#banner_text").removeAttr('readonly');
            $("#sort").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	},
    intoUpdateDisable : function () {
        var bannerId = $(this).attr("bannerId");
        $.ajax({
            url: Helper.getRootPath() + '/banner/getBanner',
            type: 'post',
            dataType : "json",
            data: {
                "bannerId": bannerId
            },
            success : function (data) {
                $('#bannerId_').val(data.id);
                $('#disable_').val(data.disable);
            },
            error : function(){
                Helper.alert("系统故障，请稍后再试");
                return false;
            }
        });
        $('#modal2').modal({
            backdrop : "static"
        });
    },
    updateDisableCommit : function () {
        $(".btn-updateDisableCommit").attr('disabled',"true");
        var bannerId = $("#bannerId_").val();
        var disable = $("#disable_").val();
        $.ajax({
            url : Helper.getRootPath() + '/banner/updateBannerDisable',
            type : 'post',
            dataType : "json",
            data : {
                "bannerId": bannerId,
                "disable" : disable
            },
            success : function (data) {
                $(".btn-updateDisableCommit").removeAttr("disabled");
                if(data.code == '00') {
                    Helper.confirm_one('编辑banner停用状态成功', function(){
                        listBanner.searchReset();
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
    }
}
