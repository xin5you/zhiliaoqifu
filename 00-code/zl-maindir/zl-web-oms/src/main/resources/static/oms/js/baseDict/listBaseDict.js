$(document).ready(function() {
	listBaseDict.init();
})

var listBaseDict = {
	init : function() {
		listBaseDict.initEvent();
	},

	initEvent:function(){
		$('.btn-edit').on('click', listBaseDict.intoEditBaseDict);
		$('.btn-reset').on('click', listBaseDict.searchReset);
	},
	searchReset: function(){
		location = Helper.getRootPath() + '/baseDict/listBaseDict.do';
	},
	intoEditBaseDict:function(){
		var dictId = $(this).attr('dictId');
		var url = Helper.getRootPath()+"/baseDict/intoEditBaseDict.do?dictId="+dictId;
		location.href=url;
	}
	
}
