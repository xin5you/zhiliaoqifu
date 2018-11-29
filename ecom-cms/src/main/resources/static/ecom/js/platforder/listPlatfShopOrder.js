$(document).ready(function() {
	listPlatfShopOrder.init();
})

var listPlatfShopOrder = {
	init : function() {
//		$('.btn-search').on('click', listPlatfShopOrder.searchData);
		$('.btn-reset').on('click', listPlatfShopOrder.searchReset);
	},
	searchReset : function() {
		var orderId = $("#orderId").val();
		Helper.post('/platforder/platforder/getPlatfShopOrderList?orderId='+orderId);
	}
};