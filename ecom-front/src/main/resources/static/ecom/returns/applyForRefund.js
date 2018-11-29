$(document).ready(function() {
	applyForRefund.init();
})

var applyForRefund = {
	init : function() {
		$('#refundCommit').on('click', applyForRefund.refundCommit);	//点击选择地址
	},
	refundCommit:function(){
		var itemId = $("#itemId").val();
		var reason = $("#reason").val();
		var reasonDesc = $("#reasonDesc").val();
		
//		$.post(Helper.getRootPath() + "/ecom/returns/returnsGoods",{"itemId":itemId,"reason":reason,"reasonDesc":reasonDesc});
		
		var full_url = Helper.getRootPath() + "/ecom/returns/returnsGoods";
		var form = $("<form></form>");
		form.attr('action', full_url);
		form.attr('method', 'post');
		form.attr('target', '_self');
		form.append("<input type='hidden' id='itemId' name='itemId' value='"+itemId+"' />");
		form.append("<input type='hidden' id='reason' name='reason' value='"+reason+"' />");
		form.append("<input type='hidden' id='reasonDesc' name='reasonDesc' value='"+reasonDesc+"' />");
		form.css('display', 'none');
		form.submit();
	}
};