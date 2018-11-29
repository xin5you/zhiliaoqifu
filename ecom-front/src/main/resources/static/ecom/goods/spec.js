var Eop = Eop||{};

Eop.Spec={
	init:function(haveSpec){
		if(haveSpec==1){
			var self = this;
			this.refresh();
			$("#goods-spec .spec-item a[specvid!='']").click(function(){
				var link = $(this);
				if(link.attr("class")!='choose_tab' && link.attr("class")!='disabled_tab' ){
					self.specClick($(this));
				}
				return false;
			});
		}else{
			BtnTipRefresh.refresh(Products);
		}
	},
	specClick:function(specLink){
		specLink.parents("ul").find("a[specvid!='']").removeClass("choose_tab");
		specLink.addClass("choose_tab");
		this.refresh(specLink);
	},
	//根据当前选择的规格找到货品
	findProduct:function(vidAr){
		
		var pros =[];
		//判断两个数组元素值是否相同，不判断位置情况
		function arraySame(ar1,ar2){
			//if(ar1.length!=ar2.length) return false;
			for(i in ar1){
				if($.inArray(ar1[i].toString(),ar2)==-1){ //不存在
					return false;
				}
			}
			return true;
		}
		var self = this;
		for(i in Products){
			var product= Products[i];
			if(arraySame(vidAr,product.specs)){
				pros[pros.length] =product; 
			}
		}
		return pros;
	}
	,
	refresh:function(specLink){
		var self = this;
		var product_ar=[];
 
		$("#goods-spec .spec-item a.choose_tab").each(function(){
			var link = $(this);
			product_ar[product_ar.length]=parseInt(link.attr("specvid"));
		});
		var pro =this.findProduct(product_ar);
		for(i in Refresh){
			Refresh[i].refresh(pro,specLink,product_ar);
		}
	}

};
var StateRefresh={
	ArrrRemove:function( ar,obj) {  
		var new_ar =[];
		for( var i in ar ){
			if(obj!= ar[i]){
				new_ar.push(ar[i]);
			}
		}
		return new_ar;
	},


	refresh:function(pro,specLink,product_ar){
		//pro:找到的product [{sprc:{}},{}]
		//product_ar:选中的规格[1,2]
		
		var self  = this;
		if(product_ar.length>0){
		//从目前未选中的规格中循环
			$(".spec-item").not($(".spec-item") ).find("a").each(function(){
				var link = $(this);
				var proar=product_ar;
				link.parents(".spec-item").find("a").not(this).each(function(){
					var specvid = parseInt($(this).attr("specvid"));
					proar= self.ArrrRemove(proar,specvid);
				});
				
				var specvid = parseInt(link.attr("specvid"));
				proar.push(specvid);
				
				var result =Eop.Spec.findProduct(proar);
				if(!result || result.length==0){
					link.addClass("disabled_tab");
				}else{
					link.removeClass("disabled_tab");
				}
				proar.pop();
				
			});
		}
	}
}

var PriceRefresh={
	refresh:function(pro){
		if(pro.length==1){
			$(".kaben-price").text(pro[0].goodsPrice );
			$(".productAttr-tip").text(pro[0].pageTitle);
			$("#product_img_small").attr("src",pro[0].picurl);
			$("#productId").val(pro[0].productId);
			$("#isStore").val(pro[0].isStore); //sku 库存
		}
	}
};
function canBuy(){
	$(".buynow-btn").unbind('click');
	$(".buynow-btn").bind('click',function(){
		Goods.addToCart($(this));
	});
	
	$(".addtocart-btn").unbind('click');
	$(".addtocart-btn").bind('click',function(){
		Goods.addToCart($(this));
		return false;
	});
}


var BtnTipRefresh = {
	refresh:function(pro){
		if(pro.length==1){
			if(pro[0].isStore==0){
				canBuy();
			}else{
				canBuy();
				$(".buynow-btn").show();
				$(".addtocart-btn").show();
			}
		}else{
//			$(".buynow-btn").unbind('click');
//			$(".buynow-btn").bind('click',function(){return false;});
//			
//			$(".addtocart-btn").unbind('click');
//			$(".addtocart-btn").bind('click',function(){return false;});
		}
	}	
};
var Refresh=[PriceRefresh,BtnTipRefresh,StateRefresh];