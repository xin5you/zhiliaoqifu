$(document).ready(function() {
	listCateGory.init();
})

var listCateGory = {
	init : function() {
		listCateGory.initEvent();
	},

	initEvent : function() {
		$('.btn-search').on('click', listCateGory.searchData);
		$('.btn-reset').on('click', listCateGory.searchReset);
		$('.btn-edit').on('click', listCateGory.intoEditCategory);
		$('.btn-submit').on('click', listCateGory.editCategoryCommit);
		$('.btn-category').on('click', listCateGory.syncGoodsCategory);
	},
	searchData : function() {
		document.forms['searchForm'].submit();
	},
	searchReset : function() {
		location = Helper.getRootPath() + '/ecom/category/listCateGory';
	},
	intoEditCategory:function(){
		$('#modal').modal({
			backdrop : "static"
		});
		var catId = $(this).attr('catId');
		
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/ecom/category/getCategory',
			data:{
				"catId":catId
			},
			dataType : 'json',
			success : function(data) {
				$("#editCatId").val(data.catId);
				$("#editCatName").val(data.catName);
				$("#editCatSolr").val(data.catSolr);
				$("#editListShow").val(data.listShow);
				$("#editEcomType").val(data.ecomType);
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});
	},
	editCategoryCommit:function(){
		var solr = $('#editCatSolr').val()
		if(solr==''){
			Helper.alert("请输入分类排序");
		}
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/ecom/category/commitEditCategory',
			data:{
				"catId":$('#editCatId').val(),
				"catSolr":$('#editCatSolr').val(),
				"listShow":$('#editListShow').val(),
			},
			dataType : 'json',
			success : function(data) {
				if ("00" == data.code) {
					Helper.confirm_one('编辑成功', function() {
						location = Helper.getRootPath() + '/ecom/category/listCateGory';
					});
				}
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});
	},
	syncGoodsCategory:function(){
		var ecomCode = $("#ecomCode").val();
		$.ajax({
			type : 'GET',
			url : Helper.getRootPath() + '/ecom/category/syncGoodsCategory',
			data:{
				"ecomCode":ecomCode
			},
			dataType : 'json',
			success : function(data) {
				if ("00" == data.code) {
					Helper.confirm_one('同步商品分类成功', function() {
						location = Helper.getRootPath() + '/ecom/category/listCategory?ecomCode='+data.object;
					});
				} else {
					Helper.alert("同步商品信息失败");
				}
			}
		});
	}
}
