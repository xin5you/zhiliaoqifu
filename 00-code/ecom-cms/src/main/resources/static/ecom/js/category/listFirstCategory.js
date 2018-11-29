$(document).ready(function() {
	listFirstCategory.init();
})

var listFirstCategory = {
	init : function() {
		listFirstCategory.initEvent();
	},

	initEvent : function() {
		$('.btn-search').on('click', listFirstCategory.searchData);
		$('.btn-reset').on('click', listFirstCategory.searchReset);
		$('.btn-edit').on('click', listFirstCategory.intoEditCategory);
		$('.btn-submit').on('click', listFirstCategory.editCategoryCommit);
	},
	searchData : function() {
		document.forms['searchForm'].submit();
	},
	searchReset : function() {
		location = Helper.getRootPath() + '/ecom/category/listFirstCategory';
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
						location = Helper.getRootPath() + '/ecom/category/listFirstCategory';
					});
				}
			},
			error : function() {
				Helper.alert("系统故障，请稍后再试");
			}
		});
	}
}
