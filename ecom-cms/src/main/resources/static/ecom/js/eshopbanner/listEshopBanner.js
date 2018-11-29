$(document).ready(function() {
	listEshopBanner.init();
})

var listEshopBanner = {
	init : function() {
		listEshopBanner.initEvent();
	},

	initEvent : function() {
		$('.btn-submit').on('click', listEshopBanner.addEshopBanner);
		$('#selectAll').on('click', listEshopBanner.selectAll);
		$('.btn-back').on('click', listEshopBanner.backBaEshop);
	},
	 selectAll: function(){
	 if ($("#selectAll").attr("checked")) {
	 $(":checkbox").attr("checked", true);
	 } else {
	 $(":checkbox").attr("checked", false);
	 }
	 },
	 addEshopBanner : function() {
		var obj = [];
		$("input:checkbox[name='bannerId']:checked").each(function() {
			obj.push($(this).val());
		});
		var ids = obj.join(",");
		var eshopId = $("#eshopId").val();
		$(".btn-submit").attr('disabled',"true");
		$.ajax({
			url : Helper.getRootPath() + '/eshopBanner/addEshopBanner',
			type : 'post',
			dataType : "json",
			data : {
				eshopId : eshopId,
				ids : ids
			},
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('新增商城主页成功', function() {
						Helper.post('/eshop/listEshopInf');
					});
				} else if (data.code == '06') {
					Helper.alert(data.msg);
					return false;
				} else if (data.code == '10') {
					Helper.alert(data.msg);
					return false;
				} else {
					Helper.alert(data.msg);
					return false;
				}
			},
	        error : function(){
	        	Helper.alert(data.msg);
	        }
		});
	},
	backBaEshop : function() {
		Helper.post('/eshop/listEshopInf');
	}
}
