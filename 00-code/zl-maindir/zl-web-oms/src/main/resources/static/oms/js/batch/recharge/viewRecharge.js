$(document).ready(function() {
	viewRecharge.init();
})

var viewRecharge = {
	init : function() {
        viewRecharge.initEvent();
	},

	initEvent:function(){
		/*$('.btn-search').on('click', viewRecharge.searchData);*/
		$('.btn-reset').on('click', viewRecharge.searchReset);
	},
	searchData:function(){

		document.forms['searchForm'].submit();
	},
	searchReset:function(){
		var orderId = $("#orderId").val();
		location = Helper.getRootPath() + '/batch/recharge/intoViewRecharge.do?orderId='+orderId;
	}
}
