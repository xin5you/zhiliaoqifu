$(document).ready(function() {
    listWithdrawOrder.init();
})

var listWithdrawOrder = {

	init : function() {
        listWithdrawOrder.initEvent();
	},

	initEvent:function(){
        $('.btn-view').on('click', listWithdrawOrder.viewWithdrawOrder);
        $('.btn-detail').on('click', listWithdrawOrder.listWithdrawDetail);
	},
    viewWithdrawOrder: function(){
		var batchNo = $(this).attr('batchNo');
        location = Helper.getRootPath() + '/withdraw/getWithdrawOrder.do?batchNo='+batchNo;
    },
    listWithdrawDetail: function(){
        var batchNo = $(this).attr('batchNo');
        location = Helper.getRootPath() + '/withdraw/listWithdrawDetail.do?batchNo='+batchNo;
    }
}
