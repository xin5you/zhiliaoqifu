$(document).ready(function() {
	listGoodsSpuInfo.init();
})

var listGoodsSpuInfo = {
	init : function() {
		listGoodsSpuInfo.initEvent();
	},

	initEvent : function() {
		$('#all_Spus_List_Id').on('click', listGoodsSpuInfo.onChange);
		$('.goods_ckeck').on('click', listGoodsSpuInfo.goodsChange);
		$('.btn-syc').on('click', listGoodsSpuInfo.sycGoods);
		$('.btn-add').on('click', listGoodsSpuInfo.addGoods);
		$('.btn-view').on('click', listGoodsSpuInfo.viewGoods);
		$('#ecomCode').on('change',listGoodsSpuInfo.getGoods);
		$('.btn-search').on('click', listGoodsSpuInfo.searchData);
		$('.btn-reset').on('click', listGoodsSpuInfo.searchReset);
	},
	onChange : function() {
		if ($("#all_Spus_List_Id").is(":checked")) {
			$(".goods_ckeck").attr('checked', true);
		} else {
			$(".goods_ckeck").attr('checked', false);
		}
	},
	goodsChange : function() {
		if ($(".goods_ckeck").length == $(".goods_ckeck:checked").length) {
			$('#all_Spus_List_Id').attr('checked', true);
		} else {
			$('#all_Spus_List_Id').attr('checked', false);
		}
	},
	sycGoods : function() {
		var ecomCode = $("#ecomCode").val();
		$.ajax({
			type : 'GET',
			url : Helper.getRootPath() + '/ecom/goodsManage/commitSyncGoodsInfo',
			data:{
				"ecomCode":ecomCode
			},
			dataType : 'json',
			success : function(data) {
				if ("00" == data.code) {
					Helper.confirm_one('同步SPU编号成功', function() {
						location = Helper.getRootPath() + '/ecom/goodsManage/listGoodsSpuInfo?ecomCode='+data.object;
					});
				}
			}
		});
	},
	addGoods:function(){
		var ecomCode = $("#ecomCode").val();
		var arr = new Array();
		$(".goods_ckeck:checked").each(function(i){
			arr[i] = $(this).val();
		});
		var vals = arr.join(",");
		$.ajax({
			type : 'GET',
			url : Helper.getRootPath() + '/ecom/goodsManage/commitGoodsInfo',
			data:{
				"ecomCode":ecomCode,
				"ids":vals
			},
			dataType : 'json',
			success : function(data) {
				if ("00" == data.code) {
					Helper.confirm_one('同步商品信息成功', function() {
						location = Helper.getRootPath() + '/ecom/goodsManage/listGoodsSpuInfo?ecomCode='+data.object;
					});
//					Helper.alert("同步商品信息成功");
//					location = Helper.getRootPath() + '/ecom/goodsManage/listGoodsSpuInfo?ecomCode='+data.object;
				} else {
					Helper.alert("同步商品信息失败");
				}
			}
		});
	},
	viewGoods:function(){
		var spuCode = $(this).attr('spuCode');
		var ecomCode = $("#ecomCode").val();
		location = Helper.getRootPath() + '/ecom/goodsManage/getGoodsInfo?spuCode='+spuCode+'&ecomCode='+ecomCode;
	},
	getGoods:function(){
		location = Helper.getRootPath() + '/ecom/goodsManage/listGoodsSpuInfo?ecomCode='+$(this).val();
	},
	searchData : function() {
		document.forms['searchForm'].submit();
	},
	searchReset : function() {
		location = Helper.getRootPath() + '/ecom/goodsManage/listGoodsSpuInfo';
	},
}
