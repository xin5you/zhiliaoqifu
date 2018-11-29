$(document).ready(function() {
	listGoodsInfo.init();
})

var listGoodsInfo = {
	init : function() {
		listGoodsInfo.initEvent();
	},

	initEvent : function() {
		$('.btn-edit').on('click', listGoodsInfo.intoEditGoods);
		$('.btn-submit').on('click', listGoodsInfo.commitEditGoods);
//		$('.btn-view').on('click', listGoodsInfo.viewGoods);
		$('.btn-search').on('click', listGoodsInfo.searchData);
		$('.btn-reset').on('click', listGoodsInfo.searchReset);
		$('.btn-editPrice').on('click', listGoodsInfo.intoEditGoodsProductPrice);
		$('.btn-solr').on('click', listGoodsInfo.addSolrGoods);	//同步solr库中的搜索商品信息
		$('.btn-delete').on('click', listGoodsInfo.deleteSolrGoodsByGoodsId);	//删除solr库中的搜索商品信息根据商品id
	},
	intoEditGoods:function(){
		$('#modal').modal({
			backdrop : "static"
		});
		var goodsId = $(this).attr('goodsId');
		
		
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/ecom/goodsManage/intoEditGoods',
			data:{
				"goodsId":goodsId
			},
			dataType : 'json',
			success : function(data) {
				$("#goodsId").val(data.goodsId);
				$("#goodName").val(data.goodsName);
//				$("#goodsPrice").val(data.goodsPrice);
				$("#market").val(data.marketEnable);
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});
		
	},
	commitEditGoods:function(){
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/ecom/goodsManage/commitEditGoods',
			data:{
				"goodsId":$('#goodsId').val(),
				"goodsName":$('#goodName').val(),
//				"goodsPrice":$('#goodsPrice').val(),
				"marketEnable":$('#market').val(),
			},
			dataType : 'json',
			success : function(data) {
				if ("00" == data.code) {
					Helper.confirm_one('编辑商品成功', function() {
						location = Helper.getRootPath() + '/ecom/goodsManage/listGoodsInfo';
					});
				}
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});
	},
//	viewGoods:function(){
//		var goodsId = $(this).attr('goodsId');
//		
//		location = Helper.getRootPath() + '/ecom/goodsManage/getGoodsInfo?goodsId='+goodsId;
//	},
	searchData : function() {
		document.forms['searchForm'].submit();
	},
	searchReset : function() {
		location = Helper.getRootPath() + '/ecom/goodsManage/listGoodsInfo';
	},
	intoEditGoodsProductPrice:function(){
		var goodsId = $(this).attr('goodsId');
		location = Helper.getRootPath() + '/ecom/goodsManage/intoEditGoodsProductPrice?goodsId='+goodsId;
	},
	addSolrGoods:function(){
		Helper.confirm("确定同步搜索库商品信息嘛？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/ecom/solrGoods/addSolrGoods',
				type : 'post',
				success : function(data) {
					if (data.code == '00') {
						Helper.confirm_one('同步搜索商品信息成功', function() {
							listGoodsInfo.searchReset();
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
	deleteSolrGoodsByGoodsId : function() {
		var goodsId = $(this).attr('goodsId');
		if (goodsId == null || goodsId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定删除该搜索商品？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/ecom/solrGoods/deleteSolrGoodsByGoodsId',
				type : 'post',
				dataType : "json",
				data : {
					"goodsId" : goodsId
				},
				success : function(data) {
					if (data.code == '00') {
						Helper.confirm_one('删除搜索商品信息成功', function() {
							listGoodsInfo.searchReset();
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
	}
}
