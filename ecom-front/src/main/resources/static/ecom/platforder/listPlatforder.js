$(document).ready(function() {
	listPlatforder.init();
	listPlatforder.checkPayStatus();
})

var listPlatforder = {
	init : function() {
		$('.middle').on('click', listPlatforder.deletePlatfOrder);
		$('#all').on('click', listPlatforder.getAllPlatfOrder);
		$('#takeDelivery').on('click', listPlatforder.getTakeDeliveryPlatfOrder);
		$('#noPayment').on('click', listPlatforder.getNoPaymentPlatfOrder);
		$('#accomplish').on('click', listPlatforder.getAccomplishPlatfOrder); 
	},
	deletePlatfOrder : function() {
		var orderId = $(this).attr('orderId');
		if (orderId == null || orderId == '') {
			jfShowTips.toastShow({'text' :"系统故障，请稍后再试"});
			return false;
		}
		jfShowTips.dialogShow({
            'mainText': '友情提示',
            'minText': '确认要删除该订单吗？',
            'checkFn': function () {
    			$.ajax({
    				url : Helper.getRootPath() + '/goods/platfOrder/deletePlatfOrder',
    				type : 'post',
    				dataType : "json",
    				data : {
    					"orderId" : orderId
    				},
    				success : function(data) {
    					if (data.code == '00') {
    						Helper.post("/goods/platfOrder/getPlatfOrderGoodsByMemberId");
    					}  else {
    						jfShowTips.toastShow({'text' :data.msg});
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
	checkPayStatus: function(){
		var payStatus = $("#payStatus").val();
		if(payStatus == ''){
			$(".tab").removeClass("choose_tab");
			$("#all").addClass("choose_tab");
		}else if(payStatus == '2'){
			$(".tab").removeClass("choose_tab");
			$("#takeDelivery").addClass("choose_tab");
		}else if(payStatus == '0'){
			$(".tab").removeClass("choose_tab");
			$("#noPayment").addClass("choose_tab");
		}else if(payStatus == '9'){
			$(".tab").removeClass("choose_tab");
			$("#accomplish").addClass("choose_tab");
		}
	},
	getAllPlatfOrder: function(){
		window.location.href= Helper.getRootPath()+"/goods/platfOrder/getPlatfOrderGoodsByMemberId?payStatus="+'';
	},
	getNoPaymentPlatfOrder: function(){
		window.location.href= Helper.getRootPath()+"/goods/platfOrder/getPlatfOrderGoodsByMemberId?payStatus="+'0';
	},
	getTakeDeliveryPlatfOrder: function(){
		window.location.href= Helper.getRootPath()+"/goods/platfOrder/getPlatfOrderGoodsByMemberId?payStatus="+'2';
	},
	getAccomplishPlatfOrder: function(){
		window.location.href= Helper.getRootPath()+"/goods/platfOrder/getPlatfOrderGoodsByMemberId?payStatus="+'9';
	}
};