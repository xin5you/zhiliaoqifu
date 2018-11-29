$(document).ready(function() {
	listMember.init();
})

var listMember = {
	init : function() {
		$('.btn-reset').on('click', listMember.searchReset);
	},
	searchReset : function() {
		Helper.post('/member/memberInf/getMemberInfList');
	}
};