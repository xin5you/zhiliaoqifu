$(document).ready(function() {
	solrGoodsList.init();
})

var solrGoodsList = {
	init : function() {
		$('#priceSort').on('click', solrGoodsList.solrGoodsListByPriceSort);
		$('#comprehensive').on('click', solrGoodsList.solrGoodsListByAll);
		$('#salesVolumeSort').on('click', solrGoodsList.solrGoodsListBySalesVolume);
	},
	solrGoodsListByAll: function(){
		var psort = "1";
		var keyword = $("#keyword").val();
		var ecomCode = $("#ecomCode").val();
		window.location.href= Helper.getRootPath()+"/ecom/solrGoods/listSolrGoods?keyword="+keyword+"&psort="+psort+"&ecomCode="+ecomCode;
	},
	solrGoodsListByPriceSort: function(){
		var psort = "2";
		var keyword = $("#keyword").val();
		var ecomCode = $("#ecomCode").val();
		window.location.href= Helper.getRootPath()+"/ecom/solrGoods/listSolrGoods?keyword="+keyword+"&psort="+psort+"&ecomCode="+ecomCode;
	},
	solrGoodsListBySalesVolume: function(){
		var psort = "3";
		var keyword = $("#keyword").val();
		var ecomCode = $("#ecomCode").val();
		window.location.href= Helper.getRootPath()+"/ecom/solrGoods/listSolrGoods?keyword="+keyword+"&psort="+psort+"&ecomCode="+ecomCode;
	}
};