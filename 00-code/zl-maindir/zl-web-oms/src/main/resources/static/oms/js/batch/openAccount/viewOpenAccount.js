$(document).ready(function() {
	viewOpenAccount.init();
})

var viewOpenAccount = {
	init : function() {
		viewOpenAccount.initEvent();
	},

	initEvent:function(){
		/*$('.btn-search').on('click', viewOpenAccount.searchData);*/
		$('.btn-reset').on('click', viewOpenAccount.searchReset);
	},
	searchData:function(){

		document.forms['searchForm'].submit();
	},
	searchReset:function(){
		var orderId = $("#orderId").val();
		location = Helper.getRootPath() + '/batch/openAccount/intoViewOpenAccount.do?orderId='+orderId;
	}
}
