$(document).ready(function() {
	listBillingType.init();
})

var listBillingType = {
	init : function() {
		listBillingType.initEvent();
	},

	initEvent:function(){
		$('.btn-edit').on('click', listBillingType.intoEditBillingType);
		$('.btn-delete').on('click', listBillingType.deleteBillingType);
		$('.btn-add').on('click', listBillingType.intoAddBillingType);
		$('.btn-reset').on('click', listBillingType.searchReset);
	},
	searchReset: function(){
		location = Helper.getRootPath() + '/billingType/listBillingType.do';
	},
	intoAddBillingType:function(){
		var url = Helper.getRootPath()+"/billingType/intoAddBillingType.do";
		location.href=url;
	},
	intoEditBillingType:function(){
		var bId = $(this).attr('bId');
		var url = Helper.getRootPath()+"/billingType/intoEditBillingType.do?bId="+bId;
		location.href=url;
	},
	
	deleteBillingType:function(){
		var bId = $(this).attr('bId');
		Helper.confirm("确定删除该商品？",function(){
			$.ajax({								  
				url: Helper.getRootPath() + '/billingType/deleteBillingTypeCommit.do',
				type: 'post',
				dataType : "json",
				data: {
					"bId": bId
				},
				success: function (data) {
					if(data.status) {
						location = Helper.getRootPath() + '/billingType/listBillingType.do';
					} else {
						Helper.alert(data.msg);
						return false;
					}
				},
				error:function(){
					Helper.alert("系统故障，请稍后再试");
				}
			});
		});
	}
}
