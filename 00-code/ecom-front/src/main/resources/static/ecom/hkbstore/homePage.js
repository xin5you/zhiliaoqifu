$(document).ready(function() {
	homePage.init();
})

var homePage = {
	init : function() {
		$('#welfaremart').on('click', homePage.findAccHKBBal);	
	},
	
	findAccHKBBal:function(){
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/ecom/hkbstore/findJurisdiction',
			dataType : 'json',
			success : function(data) {
//				console.log(data);
				if(data.blackInf == null){
					location = data.hkb_h5_url + '/welfareMart/toWelfareMartHomePage.html';
				}else{
					jfShowTips.toastShow({'text' :'对不起，您暂时无法操作'});
					return false;
				}
			},
			error : function() {
				jfShowTips.toastShow({'text' :'网络异常'});
			}
		});
	}
}