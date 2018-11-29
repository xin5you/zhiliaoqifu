$(document).ready(function() {
	listEshopRoutes.init();
})

var listEshopRoutes = {
	init : function() {
		listEshopRoutes.initEvent();
	},

	initEvent : function() {
		$('.btn-submit').on('click', listEshopRoutes.addEshopRoutes);
		$('#selectAll').on('click', listEshopRoutes.selectAll);
		$('.btn-back').on('click', listEshopRoutes.backRuEshop);
	},
	 selectAll: function(){
	 if ($("#selectAll").attr("checked")) {
	 $(":checkbox").attr("checked", true);
	 } else {
	 $(":checkbox").attr("checked", false);
	 }
	 },
	 addEshopRoutes : function() {
		var obj = [];
		$("input:checkbox[name='routesId']:checked").each(function() {
			obj.push($(this).val());
		});
		var ids = obj.join(",");
		var eshopId = $("#eshopId").val();
		$(".btn-submit").attr('disabled',"true");
		$.ajax({
			url : Helper.getRootPath() + '/eshopRoutes/addEshopRoutes',
			type : 'post',
			dataType : "json",
			data : {
				eshopId : eshopId,
				ids : ids
			},
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('新增商城电商入口成功', function() {
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
	backRuEshop : function() {
		Helper.post('/eshop/listEshopInf');
	}
}