$(document).ready(function() {
	ListGoodsDetail.init();
	var size = $("#pageListSize").val();
	if (size - 0 == 0 || size == '' || size == null) {
	    $("#btn-add").show();
    } else {
        $("#btn-add").hide();
    }
})

var ListGoodsDetail = {
	init : function() {
        ListGoodsDetail.initEvent();
	},

	initEvent:function(){
		$('.btn-reset').on('click', ListGoodsDetail.searchReset);
		$('.btn-add').on('click', ListGoodsDetail.intoAddGoodsDetail);
		$('.btn-edit').on('click', ListGoodsDetail.intoEditGoodsDetail);
		$('.btn-delete').on('click', ListGoodsDetail.deleteGoodsDetail);
        $('.btn-view').on('click', ListGoodsDetail.intoViewGoodsDetail);
		$('.btn-close').on('click',ListGoodsDetail.searchReset);
        $('.btn-backGoodsInf').on('click',ListGoodsDetail.intoBackGoodsInf);
        $('.btn-add-intro').on('click',ListGoodsDetail.intoAddGoodsIntro);
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
                detail_name: { required: true}
            },
            messages: {
                detail_name: { required: "请商品详情名称"}
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
                    ListGoodsDetail.addGoodsDetail();
            	} else if(intoType == 2) {
                    ListGoodsDetail.editGoodsDetail();
            	}
                return false;
            },
            success: $.noop 
        });
    },
	searchReset : function(){
	    var goodsId = $("#goodsId").val();
		Helper.post('/goodsManage/goodsInf/getGoodsDetailList?goodsId=' + goodsId);
	},
	intoAddGoodsDetail : function(){
        ListGoodsDetail.loadModal(1, $(this).attr('detailId'));
        ListGoodsDetail.initTip(1);
	},
	intoEditGoodsDetail : function(){
        ListGoodsDetail.loadModal(2, $(this).attr('detailId'));
        ListGoodsDetail.initTip(2);
	},
    intoViewGoodsDetail : function(){
    ListGoodsDetail.loadModal(3, $(this).attr('detailId'));
    ListGoodsDetail.initTip(3);
    },
    intoBackGoodsInf : function () {
        Helper.post('/goodsManage/goodsInf/getGoodsInfList');
    },
	addGoodsDetail:function(){
        var goodsId = $("#goodsId").val();
        var detailName = $("#detail_name").val();
        var remarks = $("#remarks").val();
        if(goodsId == null || goodsId == '') {
            Helper.alert("系统故障，请稍后再试");
            return false;
        }
        $.ajax({
            type: 'POST',
            url: Helper.getRootPath() + '/goodsManage/goodsInf/addGoodsDetail',
            dataType: 'json',
            data : {
                "goodsId" : goodsId,
                "detailName" : detailName,
                "remarks" : remarks
            },
            success: function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('新增商品详情信息成功', function() {
                        ListGoodsDetail.searchReset();
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
    editGoodsDetail:function(){
        var detailId = $("#detail_id").val();
        var goodsId = $("#goodsId").val();
        var detailName = $("#detail_name").val();
        var remarks = $("#remarks").val();
        if(detailId == null || detailId == '' || goodsId == null || goodsId == '') {
            Helper.alert("系统故障，请稍后再试");
            return false;
        }
        $.ajax({
            type : 'POST',
            url : Helper.getRootPath() + '/goodsManage/goodsInf/editGoodsDetail',
            dataType: 'json',
            data : {
                "detailId" : detailId,
                "goodsId" : goodsId,
                "detailName" : detailName,
                "remarks" : remarks
            },
            success : function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('编辑商品详情信息成功', function() {
                        ListGoodsDetail.searchReset();
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
	deleteGoodsDetail : function(){
		var detailId = $(this).attr('detailId');
		if(detailId == null || detailId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("你是否删除该记录？",function(){
			$.ajax({								  
				url : Helper.getRootPath() + '/goodsManage/goodsInf/deleteGoodsDetail',
				type : 'post',
				dataType : "json",
				data : {
					"detailId": detailId
				},
				success : function (data) {
					if(data.code == '00') {
						Helper.confirm_one('删除商品详情信息成功', function(){
                            ListGoodsDetail.searchReset();
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
	loadModal : function(type, detailId){
		$('#modal').modal({
			backdrop : "static"
		});
		if(type == 1){
			$('#modal_h').html("新增商品详情信息");
			return;
		}else if(type == 2){
			$('#modal_h').html("编辑商品详情信息");
		} else if(type == 3){
            $('#modal_h').html("商品详情信息详情");
            $("#detail_name").attr("readonly","readonly");
            $("#intro").attr("readonly","readonly");
            $("#remarks").attr("readonly","readonly");
            $(".btn-submit").attr("disabled","true");
        }
		
		$.ajax({								  
            url: Helper.getRootPath() + '/goodsManage/goodsInf/getGoodsDetail',
            type: 'post',
            dataType : "json",
            data: {
                "detailId": detailId
            },
            success : function (data) {
            	$('#detail_id').val(data.detailId);
                $('#detail_name').val(data.detailName);
                $('#intro').val(data.intro);
                $('#remarks').val(data.remarks);
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
	    });
		
		$("#modal").on("hidden.bs.modal", function(e) {
			$("#detail_name").removeAttr('readonly');
            $("#intro").removeAttr('readonly');
            $("#remarks").removeAttr('readonly');
			$(".btn-submit").removeAttr('disabled');
		});
	},
    intoAddGoodsIntro : function () {
        var detailId = $(this).attr('detailId');
        Helper.post('/goodsManage/goodsInf/intoAddGoodsIntro?detailId=' + detailId);
    }
}
