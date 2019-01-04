$(document).ready(function() {
	listGoodsInf.init();
})

var listGoodsInf = {
	init : function() {
        listGoodsInf.initEvent();
	},

	initEvent:function(){
		$('.btn-reset').on('click', listGoodsInf.searchReset);
		$('.btn-add').on('click', listGoodsInf.intoAddGoodsInf);
		$('.btn-edit').on('click', listGoodsInf.intoEditGoodsInf);
		$('.btn-delete').on('click', listGoodsInf.deleteGoodsInf);
		$('.btn-close').on('click',listGoodsInf.searchReset);
        $('#goodsImgFile').on('change', listGoodsInf.imageUpload);
        $('.btn-updateEnable').on('click',listGoodsInf.intoUpdateGoodsEnable);
        $('.btn-updateEnableCommit').on('click',listGoodsInf.updateGoodsEnable);
        $('.btn-addGoodsImg').on('click',listGoodsInf.intoAddGoodsImg);
        $('.btn-addGoodsView').on('click',listGoodsInf.intoAddGoodsView);
        $('.btn-addGoodsSku').on('click',listGoodsInf.intoAddGoodsSku);
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
                goods_name: { required: true},
                goods_img: { required: true},
                spu_code: { required: true},
                ecom_code: { required: true},
                goods_type: { required: true},
                b_id: { required: true},
                unit: { required: true},
                weight: { required: true},
                /*default_sku_code: { required: true},*/
                /*market_enable: { required: true},*/
                /*brief: { required: true},*/
                goods_detail: { required: true},
                have_groups: { required: true},
                is_disabled: { required: true},
                is_hot: { required: true}
            },
            messages: {
                goods_name: { required: "请输入商品名称"},
                goods_img: { required: "请选择商品图片"},
                spu_code: { required: "请输入Spu代码"},
                ecom_code: { required: "请选择分销商代码"},
                goods_type: { required: "请选择商品类型"},
                b_id: { required: "请选择所属专项类型"},
                unit: { required: "请选择商品单位"},
                weight: { required: "请输入商品重量"},
                /*default_sku_code: { required: "请选择默认的Sku"},*/
                /*market_enable: { required: "请选择上下架状态"},*/
                /*brief: { required: "请输入商品简介"},*/
                goods_detail: { required: "请选择商品富文本ID"},
                have_groups: { required: "请选择商品组合状态"},
                is_disabled: { required: "请选择商品禁用状态"},
                is_hot: { required: "请选择商品热销状态"}
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
                    listGoodsInf.addGoodsInf();
            	}else if(intoType == 2) {
                    listGoodsInf.editGoodsInf();
            	}
                return false;
            },
            success: $.noop 
        });
    },
    imageUpload : function() {
        var goodsImgFile = $("#goodsImgFile").val();
        $("#goods_img").val(goodsImgFile);
    },
	searchReset : function(){
		Helper.post('/goodsManage/goodsInf/getGoodsInfList');
	},
	intoAddGoodsInf : function(){
        listGoodsInf.loadModal(1, $(this).attr('goodsId'));
        listGoodsInf.initTip(1);
	},
	intoEditGoodsInf : function(){
        listGoodsInf.loadModal(2, $(this).attr('goodsId'));
        listGoodsInf.initTip(2);
	},
	addGoodsInf:function(){
        $.ajax({
            type: 'POST',
            url: Helper.getRootPath() + '/goodsManage/goodsInf/addGoodsInf',
            data: new FormData($("#goodsInfo")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType: 'json',
            success: function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('新增商品信息成功', function() {
                        listGoodsInf.searchReset();
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
    editGoodsInf:function(){
        $.ajax({
            type : 'POST',
            url : Helper.getRootPath() + '/goodsManage/goodsInf/editGoodsInf',
            data: new FormData($("#goodsInfo")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType: 'json',
            success : function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('编辑商品信息成功', function() {
                        listGoodsInf.searchReset();
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
	deleteGoodsInf : function(){
		var goodsId = $(this).attr('goodsId');
		if(goodsId == null || goodsId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("你是否删除该记录？",function(){
			$.ajax({								  
				url : Helper.getRootPath() + '/goodsManage/goodsInf/deleteGoodsInf',
				type : 'post',
				dataType : "json",
				data : {
					"goodsId": goodsId
				},
				success : function (data) {
					if(data.code == '00') {
						Helper.confirm_one('删除商品信息成功', function(){
                            listGoodsInf.searchReset();
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
	loadModal : function(type, goodsId){
		$('#modal').modal({
			backdrop : "static"
		});
		if(type == 1){
			$('#modal_h').html("新增商品信息");
			return;
		}else if(type == 2){
			$('#modal_h').html("编辑商品信息");
		}

        /*$.ajax({
            url: Helper.getRootPath() + '/goodsManage/goodsInf/getGoodsProductByGoodsId',
            type: 'post',
            dataType : "json",
            data: {
                "goodsId": goodsId
            },
            success : function (data) {
                $("#default_sku_code").empty();
                $.each(data, function(i, item){
                    $("#default_sku_code").append($("<option/>").text(item.skuCode).attr("value",item.skuCode));
                });
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
        });*/
		
		$.ajax({								  
            url: Helper.getRootPath() + '/goodsManage/goodsInf/getGoodsInf',
            type: 'post',
            dataType : "json",
            data: {
                "goodsId": goodsId
            },
            success : function (data) {
            	$('#goods_id').val(data.goodsId);
            	$('#goods_name').val(data.goodsName);
                $('#goods_img').val(data.goodsImg);
            	$('#spu_code').val(data.spuCode);
                $('#ecom_code').val(data.ecomCode);
                $('#goods_type').val(data.goodsType);
                $('#b_id').val(data.BId);
                $('#unit').val(data.unit);
                $('#weight').val(data.weight);
                /*$('#default_sku_code').val(data.defaultSkuCode);*/
                /*$('#market_enable').val(data.marketEnable);*/
                $('#brief').val(data.brief);
                $('#goods_detail').val(data.goodsDetail);
                $('#have_groups').val(data.haveGroups);
                $('#is_disabled').val(data.isDisabled);
                $('#is_hot').val(data.isHot);
                $('#remarks').val(data.remarks);
            },
            error : function(){
            	Helper.alert("系统故障，请稍后再试");
            }
	    });
		
		$("#modal").on("hidden.bs.modal", function(e) {
			$("#goods_name").removeAttr('readonly');
			$("#goods_img").removeAttr('readonly');
			$("#spu_code").removeAttr('readonly');
            $("#ecom_code").removeAttr('readonly');
            $("#goods_type").removeAttr('readonly');
            $("#b_id").removeAttr('readonly');
            $("#unit").removeAttr('readonly');
            $("#weight").removeAttr('readonly');
            /*$("#default_sku_code").removeAttr('readonly');*/
            /*$("#market_enable").removeAttr('readonly');*/
            $("#brief").removeAttr('readonly');
            $("#goods_detail").removeAttr('readonly');
            $("#have_groups").removeAttr('readonly');
            $("#is_disabled").removeAttr('readonly');
            $("#is_hot").removeAttr('readonly');
            $("#remarks").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	},
    intoUpdateGoodsEnable : function () {
        var goodsId = $(this).attr("goodsId");
        $('#modal').modal({
            backdrop : "static"
        });
        $("#goodsId_").val(goodsId);
    },
    updateGoodsEnable : function () {
        $("#btn-updateEnableCommit").attr('disabled',"true");
        var goodsId = $("#goodsId_").val();
        var marketEnable = $("#marketEnable_").val();
        $.ajax({
            url : Helper.getRootPath() + '/goodsManage/goodsInf/updateGoodsInfEnable',
            type : 'post',
            dataType : "json",
            data : {
                "goodsId": goodsId,
                "marketEnable" : marketEnable
            },
            success : function (data) {
                $(".btn-updateEnableCommit").removeAttr("disabled");
                if(data.code == '00') {
                    Helper.confirm_one('编辑商品上下架成功', function(){
                        listGoodsInf.searchReset();
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
    intoAddGoodsImg : function () {
        var goodsId = $(this).attr("goodsId");
        Helper.post('/goodsManage/goodsInf/getGoodsGalleryList');
    },
    intoAddGoodsView : function () {
        var goodsId = $(this).attr("goodsId");
        Helper.post('/goodsManage/goodsInf/getGoodsDetailList');
    },
    intoAddGoodsSku : function () {
        var goodsId = $(this).attr("goodsId");
        Helper.post('/goodsManage/goodsInf/getGoodsSkuList');
    }
}
