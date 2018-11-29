$(document).ready(function() {
	listExpressPlatf.init();
})

var listExpressPlatf = {
	init : function() {
		$('.btn-reset').on('click', listExpressPlatf.searchReset);
		$('.btn-view').on('click', listExpressPlatf.getExpressPlatfProductListByPackId);
	},
	searchReset : function() {
		Helper.post('/platforder/expressPlatf/getExpressPlatfList');
	},
	getExpressPlatfProductListByPackId: function(){
		var packId = $(this).attr('packId');
		Helper.post('/platforder/expressPlatf/getExpressPlatfProductListByPackId?packId='+packId);
	}
};