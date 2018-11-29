$(document).ready(function() {
	listOrderShip.init();
})

var listOrderShip = {
	init : function() {
		$('.btn-reset').on('click', listOrderShip.searchReset);
	},
	searchReset : function() {
		Helper.post('/platforder/orderShip/getOrderShipList');
	}
};