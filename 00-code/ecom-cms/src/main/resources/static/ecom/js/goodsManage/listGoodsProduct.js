$(document).ready(function() {
	listGoodsProduct.init();
})

var listGoodsProduct = {
	init : function() {
		listGoodsProduct.initEvent();
	},

	initEvent : function() {
		$('.btn-edit').on('click', listGoodsProduct.intoEditGoodsProduct);
		$('.btn-submit').on('click', listGoodsProduct.commitEditGoodsProduct);
	},
	intoEditGoodsProduct:function(){
		$('#modal').modal({
			backdrop : "static"
		});
		var productId = $(this).attr('productId');
		
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/ecom/goodsManage/intoEditGoodsProduct',
			data:{
				"productId":productId
			},
			dataType : 'json',
			success : function(data) {
				$("#productId").val(data.productId);
				$("#goodName").val(data.goodsName);
				$("#pageTitle").val(data.pageTitle);
				$("#goodsPrice").val(data.goodsPrice);
				$("#productEnable").val(data.productEnable);
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});
	},
	commitEditGoodsProduct:function(){
		var goodsPrice = $("#goodsPrice").val();
		var productEnable = $("#productEnable").val();
		
		var reg=/^[1-9]{1}\d*(\.\d{1,2})?$/;
		
		if(!reg.test(goodsPrice)){
			Helper.alert("请输入正确的金额");
			return false;
		}
		
		var goodsId =$("#goodsId").val();
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/ecom/goodsManage/commitEditGoodsProduct',
			data:{
				"goodsId":$('#goodsId').val(),
				"productId":$('#productId').val(),
				"productEnable":productEnable,
				"goodsPrice":goodsPrice
			},
			dataType : 'json',
			success : function(data) {
				if ("00" == data.code) {
					Helper.confirm_one('编辑商品Sku信息成功', function() {
						location = Helper.getRootPath() + '/ecom/goodsManage/intoEditGoodsProductPrice?goodsId='+$("#goodsId").val();
					});
				}
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});
		
	}
	
}
