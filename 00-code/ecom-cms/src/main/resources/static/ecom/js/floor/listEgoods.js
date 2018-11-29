$(document).ready(function() {
	listFloorEgoods.init();
})

var listFloorEgoods = {
	init : function() {
		listFloorEgoods.initEvent();
	},

	initEvent : function() {
		$('.btn-submit').on('click', listFloorEgoods.addFloorEgoods);
		$('#selectAll').on('click', listFloorEgoods.selectAll);
		$('.btn-back').on('click', listFloorEgoods.backBaEshop);
		$('.btn-reset').on('click', listFloorEgoods.searchReset);
	},
	selectAll: function(){
		 if ($("#selectAll").attr("checked")) {
			 $(":checkbox").attr("checked", true);
		 } else {
			 $(":checkbox").attr("checked", false);
		 }
	 },
	 searchReset: function(){
		 var floorId = $('#floorId').val();
		 Helper.post('/floor/intoEgoodsList?floorId='+floorId);
	 },
	 addFloorEgoods : function() {
		var obj = [];
		$("input:checkbox[name='goodsId']:checked").each(function() {
			obj.push($(this).val());
		});
		var ids = obj.join(",");
		var floorId = $("#floorId").val();
		$(".btn-submit").attr('disabled',"true");
		$.ajax({
			url : Helper.getRootPath() + '/floor/addFloorEgoods',
			type : 'post',
			dataType : "json",
			data : {
				floorId : floorId,
				ids : ids
			},
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('添加楼层商品成功', function() {
						Helper.post('/floor/intoFloorEgoodsList?floorId='+floorId);
					});
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
		var floorId = $('#floorId').val();
		Helper.post('/floor/intoFloorEgoodsList?floorId='+floorId);
	}
}
