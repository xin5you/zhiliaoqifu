$(document).ready(function() {
	listPlatfShopOrderByPlatfOrder.init();
})

var listPlatfShopOrderByPlatfOrder = {
	init : function() {
//		$('.btn-search').on('click', listPlatfShopOrderByPlatfOrder.searchData);
		$('.btn-reset').on('click', listPlatfShopOrderByPlatfOrder.searchReset);
	},
	searchReset : function() {
		var orderId = $("#orderId").val();
		Helper.post('/platforder/platforder/getPlatfShopOrderListByPlatfOrder?orderId='+orderId);
	}
};