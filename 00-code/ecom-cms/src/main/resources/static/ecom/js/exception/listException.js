$(document).ready(function() {
	listException.init();
})

var listException = {
	init : function() {
		$('.btn-reset').on('click', listException.searchReset);
	},
	searchReset : function() {
		Helper.post('/exception/getExceptionList');
	}
};