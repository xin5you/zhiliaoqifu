/**
 * 商品操作js
 */
$(document).ready(function() {
	Goods.init();
})
var Goods={
	init:function(){
		$('#addbuttonMain').on('click', function(){
			Goods.addToCart($(this));
		});
		$('#buyNowMain').on('click', function(){
			Goods.addToCart($(this));
		});
	},
	addToCart:function(btn){
		$("#showMsg").val("提交中");
		loadingChange.showLoading();
		var self = this;
		var name = btn.attr("name");
		btn.attr("disabled",true);
		var action = $("#goodsform [name='action']").val();
		if(name =="buyNow"){
			action="purchaseNow";
		}
		
		var productId=$("#productId").val();
		if (!productId){
			loadingChange.hideLoading();
			btn.attr("disabled",false);
			jfShowTips.toastShow({'text' : '请选择商品规格'});
			return false;
		}
		
		var options={
			url: Helper.getRootPath()+"/ecom/cart/" + action + "?showCartData=0",
			dataType:"json",
			cache: false,             //清楚缓存，暂时测试，如果产生冲突，请优先考虑是否是这条语句。
			success:function(result){
				loadingChange.hideLoading();
				if(result.code=="00"){
					if(name!="buyNow"){
						jfShowTips.toastShow({'text' : '加入购物车成功'});
						$("#cartnum_boom1").text(result.object);
						$("#cartnum_boom2").text(result.object);
					}else{
						window.location.href= Helper.getRootPath()+"/ecom/cart/cartAccounts?isChange=2&cartId="+result.object;
					}
				}else{
					jfShowTips.toastShow({'text' :result.msg});
				}
				btn.attr("disabled",false);
			},
			error:function(){
				jfShowTips.toastShow({'text' : '系统请求超时，请稍后再试'});
				btn.attr("disabled",false);
			}
		};
		$("#goodsform").ajaxSubmit(options);		
	}
};
