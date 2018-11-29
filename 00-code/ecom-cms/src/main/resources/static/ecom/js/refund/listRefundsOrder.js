$(document).ready(function() {
	listRefundsOrder.init();
})

var listRefundsOrder = {
	init : function() {
		listRefundsOrder.initEvent();
	},

	initEvent : function() {
		$('.btn-search').on('click', listRefundsOrder.searchData);
		$('.btn-reset').on('click', listRefundsOrder.searchReset);
		$('.btn-refund').on('click', listRefundsOrder.refundCommit);
	},
	searchData : function() {
		document.forms['searchForm'].submit();
	},
	searchReset : function() {
		location = Helper.getRootPath() + '/ecom/refund/listRefundsOrder';
	},
	refundCommit:function(){
		var sOrderId = $(this).attr('sOrderId');
		if (sOrderId == null || sOrderId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定要退款吗？", function() {
			$.ajax({
				type : 'POST',
				url : Helper.getRootPath() + '/ecom/refund/refundCommit',
				data:{
					"sOrderId":sOrderId
				},
				dataType : 'json',
				success : function(data) {
					if ("00" == data.code) {
						Helper.confirm_one('退款成功', function() {
							location = Helper.getRootPath() + '/ecom/refund/listRefundsOrder';
						});
					}else{
						Helper.alert(data.msg);
					}
				},
				error : function() {
					Helper.alert("系统故障，请稍后再试");
				}
			});
		});
	}
}
