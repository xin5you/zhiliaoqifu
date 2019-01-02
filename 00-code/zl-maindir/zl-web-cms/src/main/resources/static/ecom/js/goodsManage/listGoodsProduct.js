$(document).ready(function() {
	listGoodsProduct.init();
})

var listGoodsProduct = {
	init : function() {
        listGoodsProduct.initEvent();
	},

	initEvent:function(){
		$('.btn-reset').on('click', listGoodsProduct.searchReset);
		$('.btn-add').on('click', listGoodsProduct.intoAddGoodsProduct);
		$('.btn-edit').on('click', listGoodsProduct.intoEditGoodsProduct);
		$('.btn-delete').on('click', listGoodsProduct.deleteGoodsProduct);
		$('.btn-close').on('click',listGoodsProduct.searchReset);
        $('#picUrlFile').on('change', listGoodsProduct.imageUpload);
        $('.btn-updateEnable').on('click',listGoodsProduct.intoUpdateGoodsProductEnable);
        $('.btn-updateEnableCommit').on('click',listGoodsProduct.updateGoodsProductEnable);
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
                sku_code: { required: true},
                is_store: { required: true},
                enable_store: { required: true},
                goods_price: { required: true},
                goods_cost: { required: true},
                mkt_price: { required: true},
                page_title: { required: true},
                meta_description: { required: true},
                pic_url: { required: true}
            },
            messages: {
                sku_code: { required: "请输入Sku代码"},
                is_store: { required: "请输入可用库存"},
                enable_store: { required: "请输入总库存"},
                goods_price: { required: "请输入商品价格"},
                goods_cost: { required: "请输入商品成本价"},
                mkt_price: { required: "请输入商品市场价"},
                page_title: { required: "请输入页面标题"},
                meta_description: { required: "请输入页面描述"},
                pic_url: { required: "请选择图片"}
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
                    listGoodsProduct.addGoodsProduct();
            	}else if(intoType == 2) {
                    listGoodsProduct.editGoodsProduct();
            	}
                return false;
            },
            success: $.noop 
        });
    },
    imageUpload : function() {
        var picUrlFile = $("#picUrlFile").val();
        $("#pic_url").val(picUrlFile);
    },
	searchReset : function(){
		Helper.post('/goodsManage/goodsInf/getGoodsProductList');
	},
	intoAddGoodsInf : function(){
        listGoodsProduct.loadModal(1, $(this).attr('productId'));
        listGoodsProduct.initTip(1);
	},
	intoEditGoodsInf : function(){
        listGoodsProduct.loadModal(2, $(this).attr('productId'));
        listGoodsProduct.initTip(2);
	},
    addGoodsProduct:function(){
        $.ajax({
            type: 'POST',
            url: Helper.getRootPath() + '/goodsManage/goodsInf/addGoodsProduct',
            data: new FormData($("#goodsProductInfo")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType: 'json',
            success: function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('新增商品Sku信息成功', function() {
                        listGoodsProduct.searchReset();
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
    editGoodsProduct:function(){
        $.ajax({
            type : 'POST',
            url : Helper.getRootPath() + '/goodsManage/goodsInf/editGoodsProduct',
            data: new FormData($("#goodsProductInfo")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType: 'json',
            success : function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('编辑商品Sku信息成功', function() {
                        listGoodsProduct.searchReset();
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
	deleteGoodsProduct : function(){
		var productId = $(this).attr('productId');
		if(productId == null || productId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("你是否删除该记录？",function(){
			$.ajax({								  
				url : Helper.getRootPath() + '/goodsManage/goodsInf/deleteGoodsProduct',
				type : 'post',
				dataType : "json",
				data : {
					"productId": productId
				},
				success : function (data) {
					if(data.code == '00') {
						Helper.confirm_one('删除商品Sku信息成功', function(){
                            listGoodsProduct.searchReset();
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
	loadModal : function(type, productId){
		$('#modal').modal({
			backdrop : "static"
		});
		if(type == 1){
			$('#modal_h').html("新增商品Sku信息信息");
			return;
		}else if(type == 2){
			$('#modal_h').html("编辑商品Sku信息信息");
		}

		$.ajax({
            url: Helper.getRootPath() + '/goodsManage/goodsInf/getGoodsProduct',
            type: 'post',
            dataType : "json",
            data: {
                "productId": productId
            },
            success : function (data) {
            	$('#product_id').val(data.productId);
            	$('#sku_code').val(data.skuCode);
                $('#enable_store').val(data.enableStore);
            	$('#goods_price').val(data.goodsPrice);
                $('#goods_cost').val(data.goodsCost);
                $('#mkt_price').val(data.mktPrice);
                $('#page_title').val(data.pageTitle);
                $('#meta_description').val(data.metaDescription);
                $('#pic_url').val(data.picUrl);
                $('#remarks').val(data.remarks);
            },
            error : function(){
            	Helper.alert("系统故障，请稍后再试");
            }
	    });
		
		$("#modal").on("hidden.bs.modal", function(e) {
			$("#sku_code").removeAttr('readonly');
			$("#enable_store").removeAttr('readonly');
			$("#goods_price").removeAttr('readonly');
            $("#goods_cost").removeAttr('readonly');
            $("#mkt_price").removeAttr('readonly');
            $("#page_title").removeAttr('readonly');
            $("#meta_description").removeAttr('readonly');
            $("#pic_url").removeAttr('readonly');
            $("#remarks").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	},
    intoUpdateGoodsProductEnable : function () {
        var productId = $(this).attr("productId");
        $('#modal').modal({
            backdrop : "static"
        });
        $("#productId_").val(productId);
    },
    updateGoodsProductEnable : function () {
        $("#btn-updateEnableCommit").attr('disabled',"true");
        var productId = $("#productId_").val();
        var productEnable = $("#productEnable_").val();
        $.ajax({
            url : Helper.getRootPath() + '/goodsManage/goodsInf/updateGoodsProductEnable',
            type : 'post',
            dataType : "json",
            data : {
                "productId": productId,
                "productEnable" : productEnable
            },
            success : function (data) {
                $(".btn-updateEnableCommit").removeAttr("disabled");
                if(data.code == '00') {
                    Helper.confirm_one('编辑商品Sku上下架成功', function(){
                        listGoodsProduct.searchReset();
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
    }
}
