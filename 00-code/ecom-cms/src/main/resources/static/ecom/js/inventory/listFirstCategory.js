$(document).ready(function() {
	listInventory.init();
})

var listInventory = {
	init : function() {
		listInventory.initEvent();
	},

	initEvent : function() {
		$('.btn-search').on('click', listInventory.searchData);
		$('.btn-reset').on('click', listInventory.searchReset);
	},
	searchData : function() {
		document.forms['searchForm'].submit();
	},
	searchReset : function() {
		location = Helper.getRootPath() + '/ecom/inventory/listInventory';
	},
}
