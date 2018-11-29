$(document).ready(function() {
	category.init();

	
})

var category = {
	init : function() {
		category.initEvent();
	},

	initEvent : function() {
		$(".tab").on('click',category.getSecondCategory);
		var goodsList = $(".goods_list");
		var ecomCode = $("#ecomCode").val();
		var catId = $("#defaultFirstCategoryId").val();
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/goods/category/listGoods',
			data : {
				"firstCateGory" : catId,
				"ecomCode" : ecomCode
			},
			success:function(data){
				$("div[categoryId='"+catId+"']").addClass("choose_tab");
				for(var key in data){
					for(var i in data[key]){
//						alert(data[key][i].goodsName)
						var goods = "<a class=\"list_detail\" href='"+Helper.getRootPath()+"/ecom/goods/detail?goodsId="+data[key][i].goodsId+"'>" +
						"<div class=\"pic\"><img class=\"banner_pic\" src='' data-imgurl='"+data[key][i].picUrl+"'></div>" +
						"<div class=\"goods_title\">"+data[key][i].goodsName+"</div>" +
						"<div class=\"price\">¥"+data[key][i].goodsPrice+"</div>" +
						"</a>";
						
						goodsList.append(goods);
					}
				}
				 loadingImg.lazyLoading();
			},
			error:function(){
				
			}
		});
	},
	getSecondCategory:function(){
		$(this).addClass("choose_tab");
		var catId = $(this).attr("categoryId");
		var ecomCode = $("#ecomCode").val();
		var brandList = $("#brand_list");
		brandList.html("");
		var  html1 =$("<div class=\"jd_drop_down_content show_table\" id=\"secondCategory\"></div>");
		var html2 = $("<div class=\"brand_list\"></div>");
		var  html3 = "<div class=\"jd_drop_down_bg\"></div>";
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/goods/category/getSecondCategory',
			data : {
				"catId" : catId,
				"ecomCode" : ecomCode
			},
			success:function(data){
				if(data != null){
					for ( var i in data.secondList) {
						var categoryDiv = "<div class=\"tab hideclear\" onclick=\"category.getGoodsList(this);\" firstId='"+data.secondList[i].parentId+"' secondId='"+data.secondList[i].catId+"'>" +
//								"<div class=\"pic\"><img src='"+Helper.getRootPath()+"/front/images/hkbstore/pics/class1.png"+"' /></div>" +
								"<p>"+data.secondList[i].catName+"</p>" +
								"</div>";
						
						html2.append(categoryDiv);
					}
					html1.append(html2);
					brandList.append(html1,html3);
				}
//				showDown.changeTab();

			    showDown.hideBackground();
			},
			error:function(){
				
			}
		});
	},
	getGoodsList:function(e){
		
		var goodsList = $(".goods_list");
		var ecomCode = $("#ecomCode").val();
		
		goodsList.html("");
		
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/goods/category/listGoods',
			data : {
				"firstCateGory" : $(e).attr("firstId"),
				"ecomCode" : ecomCode,
				"secondCategory":$(e).attr("secondId")
			},
			success:function(data){
				if(data != null){
					$("div[categoryId='"+$(e).attr("firstId")+"']").addClass("choose_tab");
					for(var key in data){
						for(var i in data[key]){
//						alert(data[key][i].goodsName)
							var goods = "<a class=\"list_detail\" href='"+Helper.getRootPath()+"/ecom/goods/detail?goodsId="+data[key][i].goodsId+"'>" +
							"<div class=\"pic\"><img class=\"banner_pic\" src=\"\" data-imgurl='"+data[key][i].picUrl+"'></div>" +
							"<div class=\"goods_title\">"+data[key][i].goodsName+"</div>" +
							"<div class=\"price\">¥"+data[key][i].goodsPrice+"</div>" +
							"</a>";
							
							goodsList.append(goods);
						}
					}
				}
				 loadingImg.lazyLoading();
			},
			error:function(){
				
			}
		});
		
		$("#secondCategory").removeClass("show_table");

	}
}
