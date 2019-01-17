$(document).ready(function() {
	listGoodsSpec.init();
	$("#specImgFile").hide();
})

var listGoodsSpec = {
	init : function() {
        listGoodsSpec.initEvent();
	},

	initEvent:function(){
		$('.btn-reset').on('click', listGoodsSpec.searchReset);
		$('.btn-add').on('click', listGoodsSpec.intoAddGoodsSpec);
		$('.btn-edit').on('click', listGoodsSpec.intoEditGoodsSpec);
		$('.btn-delete').on('click', listGoodsSpec.deleteGoodsSpec);
		$('.btn-close').on('click',listGoodsSpec.searchReset);
        /*$('#specImgFile').on('change', listGoodsSpec.imageUpload);*/
        $('.btn-addSpecValues').on('click',listGoodsSpec.intoAddSpecValues);
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
                /*spec_img: { required: true},*/
                spec_order: { required: true}
            },
            messages: {
                spec_name: { required: "请输入规格名称"},
                /*spec_img: { required: "请选择规格图片"},*/
                spec_order: { required: "请输入规格序号"}
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
                    listGoodsSpec.addGoodsSpec();
            	}else if(intoType == 2) {
                    listGoodsSpec.editGoodsSpec();
            	}
                return false;
            },
            success: $.noop 
        });
    },
    imageUpload : function() {
        var specImgFile = $("#specImgFile").val();
        $("#spec_img").val(specImgFile);
    },
	searchReset : function(){
		Helper.post('/goodsManage/goodsSpec/getGoodsSpecList');
	},
	intoAddGoodsSpec : function(){
        listGoodsSpec.loadModal(1, $(this).attr('specId'));
        listGoodsSpec.initTip(1);
	},
	intoEditGoodsSpec : function(){
        listGoodsSpec.loadModal(2, $(this).attr('specId'));
        listGoodsSpec.initTip(2);
	},
	addGoodsSpec:function(){
        $.ajax({
            type: 'POST',
            url: Helper.getRootPath() + '/goodsManage/goodsSpec/addGoodsSpec',
            data: new FormData($("#goodsSpecInfo")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType: 'json',
            success: function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('新增规格信息成功', function() {
                        listGoodsSpec.searchReset();
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
    editGoodsSpec:function(){
        $.ajax({
            type : 'POST',
            url : Helper.getRootPath() + '/goodsManage/goodsSpec/editGoodsSpec',
            data: new FormData($("#goodsSpecInfo")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType: 'json',
            success : function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('编辑规格信息成功', function() {
                        listGoodsSpec.searchReset();
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
	deleteGoodsSpec : function(){
		var specId = $(this).attr('specId');
		if(specId == null || specId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("你是否删除该记录？",function(){
			$.ajax({								  
				url : Helper.getRootPath() + '/goodsManage/goodsSpec/deleteGoodsSpec',
				type : 'post',
				dataType : "json",
				data : {
					"specId": specId
				},
				success : function (data) {
					if(data.code == '00') {
						Helper.confirm_one('删除规格信息成功', function(){
                            listGoodsSpec.searchReset();
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
	loadModal : function(type, specId){
		$('#modal').modal({
			backdrop : "static"
		});
		if(type == 1){
			$('#modal_h').html("新增规格信息");
			return;
		}else if(type == 2){
			$('#modal_h').html("编辑规格信息");
		}
		
		$.ajax({								  
            url: Helper.getRootPath() + '/goodsManage/goodsSpec/getGoodsSpec',
            type: 'post',
            dataType : "json",
            data: {
                "specId": specId
            },
            success : function (data) {
            	$('#spec_id').val(data.specId);
            	$('#spec_name').val(data.specName);
            	$('#spec_order').val(data.specOrder);
            	/*$('#spec_img').val(data.specImg);*/
                $('#remarks').val(data.remarks);
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
	    });
		
		$("#modal").on("hidden.bs.modal", function(e) {
			$("#spec_name").removeAttr('readonly');
			/*$("#spec_img").removeAttr('readonly');*/
			$("#spec_order").removeAttr('readonly');
            $("#remarks").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	},
    intoAddSpecValues : function () {
        var specId = $(this).attr("specId");
        Helper.post('/goodsManage/goodsSpec/getGoodsSpecValuesList?specId='+specId);
    }
}
