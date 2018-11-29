$(document).ready(function() {
	listFloorEgoods.init();
})

var listFloorEgoods = {
	init : function() {
		listFloorEgoods.initEvent();
	},

	initEvent : function() {
		$('.btn-submit').on('click', listFloorEgoods.addFloorEgoods);
//		$('.btn-back').on('click', listFloorEgoods.backBaEshop);
		$('.btn-reset').on('click', listFloorEgoods.searchReset);
		$('.btn-add').on('click', listFloorEgoods.intoEgoodsList);
		$('.btn-delete').on('click', listFloorEgoods.deleteFloorEgoods);
	},
	deleteFloorEgoods: function(){
		var goodsId = $(this).attr('goodsId');
		var floorId = $("#floorId").val();
		if (goodsId == null || goodsId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		if (floorId == null || floorId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定移除该商品？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/floor/deleteFloorGoods',
				type : 'post',
				dataType : "json",
				data : {
					"floorId" : floorId,
					"goodsId" : goodsId
				},
				success : function(data) {
					if (data.code == '00') {
						Helper.confirm_one('删除楼层信息成功', function() {
							listFloorEgoods.searchReset();
						});
					}  else {
						Helper.alert(data.msg);
						return false;
					}
				},
				error : function() {
					Helper.alert(data.msg);
				}
			});
		});
	},
	intoEgoodsList: function(){
		var floorId = $("#floorId").val();
		Helper.post('/floor/intoEgoodsList?floorId='+floorId);
	},
	 searchReset: function(){
		 var floorId = $('#floorId').val();
		 Helper.post('/floor/intoFloorEgoodsList?floorId='+floorId);
	 },
//	 addFloorEgoods : function() {
//		var obj = [];
//		$("input:checkbox[name='goodsId']:checked").each(function() {
//			obj.push($(this).val());
//		});
//		var ids = obj.join(",");
//		var floorId = $("#floorId").val();
//		$(".btn-submit").attr('disabled',"true");
//		$.ajax({
//			url : Helper.getRootPath() + '/floor/addFloorEgoods',
//			type : 'post',
//			dataType : "json",
//			data : {
//				floorId : floorId,
//				ids : ids
//			},
//			success : function(data) {
//				$(".btn-submit").removeAttr("disabled");
//				if (data.code == '00') {
//					Helper.confirm_one('添加楼层商品成功', function() {
//						Helper.post('/floor/listFloors');
//					});
//				} else {
//					Helper.alert(data.msg);
//					return false;
//				}
//			},
//	        error : function(){
//	        	Helper.alert(data.msg);
//	        }
//		});
//	},
//	backBaEshop : function() {
//		Helper.post('/floor/listFloors');
//	}
}
