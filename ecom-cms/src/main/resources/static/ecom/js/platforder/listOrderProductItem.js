$(document).ready(function() {
	listOrderProductItem.init();
})

var listOrderProductItem = {
	init : function() {
		$('.btn-reset').on('click', listOrderProductItem.searchReset);
	},
	searchReset : function() {
		Helper.post('/platforder/orderProductItem/getOrderProductItemList');
	}
};