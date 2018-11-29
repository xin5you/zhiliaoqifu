$(document).ready(function() {
	listPlatforder.init();
})

var listPlatforder = {
	init : function() {
		$('.cancle_order').on('click', listPlatforder.cancleOrder);
		$('.goto_pay').on('click', listPlatforder.confirmOrder);	//确认收货
		$('.sale').on('click', listPlatforder.appliyRefund);	//申请售后
	},
	cancleOrder : function() {
		var sOrderId = $(this).attr('sOrderId');
		var orderId = $("#orderId").val();
		if (sOrderId == null || sOrderId == '') {
			jfShowTips.toastShow({'text' :"系统故障，请稍后再试"});
			return false;
		}
		if (orderId == null || orderId == '') {
			jfShowTips.toastShow({'text' :"系统故障，请稍后再试"});
			return false;
		}
		jfShowTips.dialogShow({
            'mainText': '友情提示',
            'minText': '确认要取消该订单吗？',
            'checkFn': function () {
                //点击取消后做的事件
            	$.ajax({
        			url : Helper.getRootPath() + '/ecom/order/cancelOrder',
        			type : 'post',
        			dataType : "json",
        			data : {
        				"sOrderId" : sOrderId
        			},
        			success : function(data) {
        				if (data.code == '200') {
        					window.location.href= Helper.getRootPath()+"/goods/platfOrder/getPlatfOrderGoodsDetails?orderId="+orderId;
        				}  else {
        					jfShowTips.toastShow({'text' :"系统故障，请稍后再试"});
        					return false;
        				}
        			},
        			error : function() {
        				jfShowTips.toastShow({'text' :data.msg});
        			}
        		});
            }
        })
	},
	confirmOrder : function() {
		var sOrderId = $(this).attr('sOrderId');
		var orderId = $("#orderId").val();
		if (sOrderId == null || sOrderId == '') {
			jfShowTips.toastShow({'text' :"系统故障，请稍后再试"});
			return false;
		}
		if (orderId == null || orderId == '') {
			jfShowTips.toastShow({'text' :"系统故障，请稍后再试"});
			return false;
		}
		jfShowTips.dialogShow({
            'mainText': '友情提示',
            'minText': '确认收货吗？',
            'checkFn': function () {
                //点击取消后做的事件
            	$.ajax({
        			url : Helper.getRootPath() + '/ecom/order/confirmOrder',
        			type : 'post',
        			dataType : "json",
        			data : {
        				"sOrderId" : sOrderId
        			},
        			success : function(data) {
        				if (data.code == '200') {
        					window.location.href= Helper.getRootPath()+"/goods/platfOrder/getPlatfOrderGoodsDetails?orderId="+orderId;
        				}  else {
        					jfShowTips.toastShow({'text' :"系统故障，请稍后再试"});
        					return false;
        				}
        			},
        			error : function() {
        				jfShowTips.toastShow({'text' :data.msg});
        			}
        		});
            }
        })
	},
	appliyRefund:function(){
		var oItemId = $(this).attr('oItemId');
		window.location.href= Helper.getRootPath()+"/ecom/returns/toReturnsGoods?itemId="+oItemId;
	}
};