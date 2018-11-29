$(document).ready(function() {
	$(".listorder").click(function(){
		$("#myForm").submit();
	});
//	$(".listorder").click(function(){
//		
//		var id = $(this).attr("name");
//		var mobile = $("#mobile").val();
//		var openID = $("#openID").val();
//		console.log(mobile);
//		Helper.post('/order/viewOrder?id='+id+"&mobile=" + mobile + "&openID=" + openID);
//	});
	
})
