$(document).ready(function() {
	listGoodsSpecValues.init();
    $("#specImageFile").hide();
})

var listGoodsSpecValues = {
	init : function() {
        listGoodsSpecValues.initEvent();
	},

	initEvent:function(){
		$('.btn-reset').on('click', listGoodsSpecValues.searchReset);
		$('.btn-add').on('click', listGoodsSpecValues.intoAddGoodsSpecValues);
		$('.btn-edit').on('click', listGoodsSpecValues.intoEditGoodsSpecValues);
		$('.btn-delete').on('click', listGoodsSpecValues.deleteGoodsSpecValues);
		$('.btn-close').on('click',listGoodsSpecValues.searchReset);
        /*$('#spec_type').on('change', listGoodsSpecValues.goodsSpecValuesType);*/
       /* $('#specImageFile').on('change', listGoodsSpecValues.imageUpload);*/
        $('.btn-backGoodsSpec').on('click',listGoodsSpecValues.intoBackGoodsSpec);
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
                spec_value_name: { required: true},
                spec_order: { required: true}
                /*spec_type: { required: true}*/
            },
            messages: {
                spec_value_name: { required: "请输入规格值名称"},
                spec_order: { required: "请输入规格值序号"}
                /*spec_type: { required: "请选择规格值类型"}*/
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
                    listGoodsSpecValues.addGoodsSpecValues();
            	}else if(intoType == 2) {
                    listGoodsSpecValues.editGoodsSpecValues();
            	}
                return false;
            },
            success: $.noop 
        });
    },
    imageUpload : function() {
        var specImageFile = $("#specImageFile").val();
        $("#spec_image").val(specImageFile);
    },
	searchReset : function(){
	    var specId = $("#spec_id").val();
		Helper.post('/goodsManage/goodsSpec/getGoodsSpecValuesList?specId='+specId);
	},
	intoAddGoodsSpecValues : function(){
        listGoodsSpecValues.loadModal(1, $(this).attr('specValueId'));
        listGoodsSpecValues.initTip(1);
	},
	intoEditGoodsSpecValues : function(){
        listGoodsSpecValues.loadModal(2, $(this).attr('specValueId'));
        listGoodsSpecValues.initTip(2);
	},
	addGoodsSpecValues:function(){
        $.ajax({
            type: 'POST',
            url: Helper.getRootPath() + '/goodsManage/goodsSpec/addGoodsSpecValues',
            data: new FormData($("#goodsSpecValuesInfo")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType: 'json',
            success: function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('新增规格值信息成功', function() {
                        listGoodsSpecValues.searchReset();
                    });
                } else {
                    Helper.alert(data.msg);
                    return false;
                }
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
                return false;
            }
        });
    },
    editGoodsSpecValues:function(){
        $.ajax({
            type : 'POST',
            url : Helper.getRootPath() + '/goodsManage/goodsSpec/editGoodsSpecValues',
            data: new FormData($("#goodsSpecValuesInfo")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType: 'json',
            success : function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('编辑规格值信息成功', function() {
                        listGoodsSpecValues.searchReset();
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
	deleteGoodsSpecValues : function(){
		var specValueId = $(this).attr('specValueId');
		if(specValueId == null || specValueId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("你是否删除该记录？",function(){
			$.ajax({								  
				url : Helper.getRootPath() + '/goodsManage/goodsSpec/deleteGoodsSpecValues',
				type : 'post',
				dataType : "json",
				data : {
					"specValueId": specValueId
				},
				success : function (data) {
					if(data.code == '00') {
						Helper.confirm_one('删除规格信息成功', function(){
                            listGoodsSpecValues.searchReset();
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
	loadModal : function(type, specValueId){
		$('#modal').modal({
			backdrop : "static"
		});
		if(type == 1){
			$('#modal_h').html("新增规格值信息");
			return;
		}else if(type == 2){
			$('#modal_h').html("编辑规格值信息");
		}
		
		$.ajax({								  
            url: Helper.getRootPath() + '/goodsManage/goodsSpec/getGoodsSpecValues',
            type: 'post',
            dataType : "json",
            data: {
                "specValueId": specValueId
            },
            success : function (data) {
            	$('#spec_value_id').val(data.specValueId);
                $('#spec_value_name').val(data.specValueName);
                /*$('#spec_type').val(data.specType);*/
                $('#spec_order').val(data.specOrder);
            	$('#spec_value').val(data.specValue);
            	/*$('#spec_image').val(data.specImage);*/
                $('#remarks').val(data.remarks);
                /*if ($('#spec_type').val() == '0') {
                    $("#specImageFile").hide();
                    $("#spec_value").removeAttr('disabled');
                } else {
                    $("#specImageFile").show();
                    $("#spec_value").attr('disabled', "true");
                }*/
            },
            error : function(){
            	Helper.alert("系统故障，请稍后再试");
            }
	    });
		
		$("#modal").on("hidden.bs.modal", function(e) {
            $("#spec_value_name").removeAttr('readonly');
			$("#spec_value").removeAttr('readonly');
			/*$("#spec_type").removeAttr('readonly');*/
			$("#spec_order").removeAttr('readonly');
            /*$("#spec_image").removeAttr('readonly');*/
            $("#remarks").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	},
    goodsSpecValuesType : function () {
	    var specType = $('#spec_type').val();
        if (specType == '0') {
            $("#spec_image").attr('disabled', "true");
            $("#specImageFile").hide();
            $("#spec_value").removeAttr('disabled');
        } else if (specType == '1') {
            $("#spec_value").attr('disabled', "true");
            $("#spec_image").removeAttr('disabled');
            $("#specImageFile").show();
        }
    },
    intoBackGoodsSpec : function () {
        Helper.post('/goodsManage/goodsSpec/getGoodsSpecList');
    }
}
