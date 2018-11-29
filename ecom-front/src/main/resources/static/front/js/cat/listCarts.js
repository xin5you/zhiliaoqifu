$(document).ready(function() {
	listCarts.init();
})

var listCarts = {
	init : function() {
		listCarts.initEvent();
	},

	initEvent : function() {
		
	 //如果购物车数据，在加载时is_check是选中的值， 全选按钮选中
		var singleChk=true; 
		for (var i = 0; i < singleChecked.length; i++) {
			if (singleChecked[i].checked == false) {
				return false;
			}
		}
		if(singleChk){
			shoppingCartChecked.checked = true;
		}
	}
}