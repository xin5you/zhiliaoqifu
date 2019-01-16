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
        $('.btn-view').on('click', listGoodsProduct.intoViewGoodsProduct);
		$('.btn-close').on('click',listGoodsProduct.searchReset);
        $('#picUrlFile').on('change', listGoodsProduct.imageUpload);
        $('.btn-updateEnable').on('click',listGoodsProduct.intoUpdateGoodsProductEnable);
        $('.btn-updateEnableCommit').on('click',listGoodsProduct.updateGoodsProductEnable);
        $('#specId').on('change', listGoodsProduct.getSpecValuesBySpecId);
        $('.btn-backGoodsInf').on('click',listGoodsProduct.intoBackGoodsInf);
	},
	
	initTip: function (intoType) {
        var ttip_validator = $('#goodsProductInfo').validate({
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
                specId: { required: true},
                specValueId: { required: true},
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
                specId: { required: "请选择规格"},
                specValueId: { required: "请选择规格值"},
                is_store: { required: "请输入可用库存"},
                enable_store: { required: "请输入总库存"},
                goods_price: { required: "请输入商品价格"},
                goods_cost: { required: "请输入商品成本价"},
                mkt_price: { required: "请输入商品市场价"},
                page_title: { required: "请输入货品标题"},
                meta_description: { required: "请输入货品描述"},
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
	    var goodsId = $("#goods_id").val();
		Helper.post('/goodsManage/goodsInf/getGoodsProductList?goodsId=' + goodsId);
	},
    intoAddGoodsProduct : function(){
        listGoodsProduct.loadModal(1, $(this).attr('productId'));
        listGoodsProduct.initTip(1);
	},
	intoEditGoodsProduct : function(){
        listGoodsProduct.loadModal(2, $(this).attr('productId'));
        listGoodsProduct.initTip(2);
	},
    intoViewGoodsProduct : function(){
        listGoodsProduct.loadModal(3, $(this).attr('productId'));
        listGoodsProduct.initTip(3);
    },
    intoBackGoodsInf : function () {
        Helper.post('/goodsManage/goodsInf/getGoodsInfList');
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
                Helper.alert("网络异常，请稍后再试");
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
                Helper.alert("网络异常，请稍后再试");
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
                error : function() {
                    Helper.alert("网络异常，请稍后再试");
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
            /*var goodsId = $("#goods_id").val();
            $.ajax({
                url: Helper.getRootPath() + '/goodsManage/goodsInf/getGoodsProductListByGoodsId',
                type: 'post',
                dataType : "json",
                data: {
                    "goodsId": goodsId
                },
                success : function (data1) {
                    console.log(data1);
                    if (data1 != null && data1.length >= 1) {
                        $('#specId').val(data1[0].TbEcomGoodsProduct.specId);
                        $('#spec_id').val(data1[0].TbEcomGoodsProduct.specId);
                        $("#specId").attr("disabled","true");
                        $.ajax({
                            url : Helper.getRootPath() + '/goodsManage/goodsSpec/getSpecValuesBySpecId',
                            type : 'post',
                            dataType : "json",
                            data : {
                                "specId": data1[0].specId
                            },
                            success : function (data2) {
                                console.log(data2);
                                if (data2 == null || data2 == '') {
                                    Helper.alert("网络异常，请稍后再试");
                                    return false;
                                }
                                $("#specValueId").empty();
                                $("#specValueId").append("<option value=''>"+"---请选择---"+"</option>");
                                for(var i = 0; i < data2.length; i++){
                                    $("#specValueId").append("<option value='"+data2[i].specValueId+"'>"+data2[i].specValue+"</option>");//新增
                                }
                            },
                            error : function() {
                                Helper.alert("网络异常，请稍后再试");
                                return false;
                            }
                        });
                    }
                },
                error : function() {
                    Helper.alert("网络异常，请稍后再试");
                }
            });*/
			return;
		}else if(type == 2){
			$('#modal_h').html("编辑商品Sku信息信息");
		} else if(type == 3){
            $('#modal_h').html("商品Sku信息详情");
            $("#is_store").attr("readonly","readonly");
            $("#sku_code").attr("readonly","readonly");
            $("#enable_store").attr("readonly","readonly");
            $("#goods_price").attr("readonly","readonly");
            $("#goods_cost").attr("readonly","readonly");
            $("#mkt_price").attr("readonly","readonly");
            $("#page_title").attr("readonly","readonly");
            $("#meta_description").attr("readonly","readonly");
            $("#picUrlFile").hide();
            $("#remarks").attr("readonly","readonly");
            $("#default_sku_code").attr("disabled","true");
            $("#specId").attr("disabled","true");
            $("#specValueId").attr("disabled","true");
            $(".btn-submit").attr("disabled","true");
        }

        /*$.ajax({
            url: Helper.getRootPath() + '/goodsManage/goodsSpec/getSpecValuesByProductId',
            type: 'post',
            dataType : "json",
            data: {
                "productId": productId
            },
            success : function (data) {
                if (data == null || data == '') {
                    Helper.alert("网络异常，请稍后再试");
                    return false;
                }
                $("#specValueId").empty();
                $("#specValueId").append("<option value=''>"+"---请选择---"+"</option>");
                for(var i = 0; i < data.length; i++){
                    $("#specValueId").append("<option value='"+data[i].specValueId+"'>"+data[i].specValue+"</option>");//新增
                }
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
        });

		$.ajax({
            url: Helper.getRootPath() + '/goodsManage/goodsInf/getGoodsProduct',
            type: 'post',
            dataType : "json",
            data: {
                "productId": productId
            },
            success : function (data) {
            	$('#product_id').val(data.productId);
                $('#is_store').val(data.isStore);
            	$('#sku_code').val(data.skuCode);
                $('#enable_store').val(data.enableStore);
            	$('#goods_price').val(data.goodsPrice);
                $('#goods_cost').val(data.goodsCost);
                $('#mkt_price').val(data.mktPrice);
                $('#page_title').val(data.pageTitle);
                $('#meta_description').val(data.metaDescription);
                $('#pic_url').val(data.picUrl);
                $('#default_sku_code').val(data.isDefault);
                $('#specId').val(data.specId);
                $('#spec_id').val(data.specId);
                $('#specValueId').val(data.specValueId);
                $('#remarks').val(data.remarks);
                var goodsId = $("#goods_id").val();
                $.ajax({
                    url: Helper.getRootPath() + '/goodsManage/goodsInf/getGoodsProductListByGoodsId',
                    type: 'post',
                    dataType : "json",
                    data: {
                        "goodsId": goodsId
                    },
                    success : function (result) {
                        if (result != null && result.length > 1) {
                            $("#specId").attr("disabled","true");
                        }
                    },
                    error : function() {
                        Helper.alert("网络异常，请稍后再试");
                    }
                });
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
	    });*/

        $.ajax({
            url: Helper.getRootPath() + '/goodsManage/goodsSpec/getSpecValuesByProductId',
            type: 'post',
            dataType : "json",
            data: {
                "productId": productId
            },
            success : function (data) {
                console.log(data);
                if (data == null || data == '') {
                    Helper.alert("网络异常，请稍后再试");
                    return false;
                }
                $("#specValueId").empty();
                $("#specValueId").append("<option value=''>"+"---请选择---"+"</option>");
                for(var i = 0; i < data.length; i++){
                    $("#specValueId").append("<option value='"+data[i].specValueId+"'>"+data[i].specValue+"</option>");
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
                        $('#is_store').val(data.isStore);
                        $('#sku_code').val(data.skuCode);
                        $('#enable_store').val(data.enableStore);
                        $('#goods_price').val(data.goodsPrice);
                        $('#goods_cost').val(data.goodsCost);
                        $('#mkt_price').val(data.mktPrice);
                        $('#page_title').val(data.pageTitle);
                        $('#meta_description').val(data.metaDescription);
                        $('#pic_url').val(data.picUrl);
                        $('#default_sku_code').val(data.isDefault);
                        $('#specId').val(data.specId);
                        $('#spec_id').val(data.specId);
                        $('#specValueId').val(data.specValueId);
                        $('#remarks').val(data.remarks);
                    },
                    error : function() {
                        Helper.alert("网络异常，请稍后再试");
                    }
                });
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
        });

		$("#modal").on("hidden.bs.modal", function(e) {
			$("#sku_code").removeAttr('readonly');
            $("#is_store").removeAttr('readonly');
			$("#enable_store").removeAttr('readonly');
			$("#goods_price").removeAttr('readonly');
            $("#goods_cost").removeAttr('readonly');
            $("#mkt_price").removeAttr('readonly');
            $("#page_title").removeAttr('readonly');
            $("#meta_description").removeAttr('readonly');
            $("#picUrlFile").show();
            $("#remarks").removeAttr('readonly');
            $("#default_sku_code").removeAttr('disabled');
            $("#specValueId").removeAttr('disabled');
            $("#specId").removeAttr('disabled');
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
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
        });
    },
    getSpecValuesBySpecId : function () {
        var specId = $("#specId").val();
        $("#spec_id").val(specId);
        $.ajax({
            url : Helper.getRootPath() + '/goodsManage/goodsSpec/getSpecValuesBySpecId',
            type : 'post',
            dataType : "json",
            data : {
                "specId": specId
            },
            success : function (data) {
                if (data == null || data == '') {
                    Helper.alert("网络异常，请稍后再试");
                    return false;
                }
                $("#specValueId").empty();
                $("#specValueId").append("<option value=''>"+"---请选择---"+"</option>");
                for(var i = 0; i < data.length; i++){
                    $("#specValueId").append("<option value='"+data[i].specValueId+"'>"+data[i].specValue+"</option>");//新增
                }
                /*$("#specValueId option:eq(0)").attr('selected', 'selected');//选中第一个*/
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
                return false;
            }
        });
    }
}
