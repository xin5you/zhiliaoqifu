$(document).ready(function() {
	mconsentApplication.init();
})

var mconsentApplication = {
	init : function() {
		$('#offerExpressCommit').on('click', mconsentApplication.offerExpressCommit);	//点击选择地址
	},
	offerExpressCommit:function(){
		var returnsId = $("#returnsId").text();
		var trackingCompany = $("#trackingCompany").val();
		var trackingNum = $("#trackingNum").val();
//		alert(returnsId+ " " +trackingCompany+ " " +trackingNum);
		var full_url = Helper.getRootPath() + "/ecom/returns/offerExpress";
		var form = $("<form></form>");
		form.attr('action', full_url);
		form.attr('method', 'post');
		form.attr('target', '_self');
		form.append("<input type='hidden' id='returnsId' name='returnsId' value='"+returnsId+"' />");
		form.append("<input type='hidden' id='trackingCompany' name='trackingCompany' value='"+trackingCompany+"' />");
		form.append("<input type='hidden' id='trackingNum' name='trackingNum' value='"+trackingNum+"' />");
		form.css('display', 'none');
		form.submit();
	}
};