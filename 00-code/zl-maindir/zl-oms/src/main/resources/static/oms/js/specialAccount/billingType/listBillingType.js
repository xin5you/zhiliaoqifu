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
		$('.btn-submit').on('click', listBillingType.editBillingType);
		$('.btn-reset').on('click', listBillingType.searchReset);
	},

	intoAddBillingType:function(){
		listBillingType.loadBillingTypeModal(2, null);
	},
	intoEditBillingType:function(){
		var bId = $(this).attr('bId');
		listBillingType.loadBillingTypeModal(1, bId);
	},
	editBillingType:function(){
		var bId = $("#billingTypeInf_bId").val();
		var name = $("#billingTypeInf_name").val();
		var code = $("#billingTypeInf_code").val();
		var remarks = $("#billingTypeInf_remarks").val();
		var type = $("input:radio[name='type']:checked").val();
		console.log(type)
		Helper.confirm("确定提交吗？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/specialAccount/billingType/editBillingType.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "bId" : bId, 
	                "name" : name,
	                "code" : code,
	                "remarks" : remarks,
	                "type" : type
	            },
	            success: function (data) {
	            	if(data > 0) {
	                	location = Helper.getRootPath() + '/specialAccount/billingType/listBillingType.do';
	                } else if(data < 0){
	                	Helper.alert("账户类型名称已存在，请重新添加");
	                	return false;
	                }else {
	                	Helper.alert("系统故障，请稍后再试");
	                	return false;
					}
	            },
	            error:function(){
	            	Helper.alert("系统故障，请稍后再试");
	            }
		    });
		});
	},
	deleteBillingType:function(){
		var bId = $(this).attr('bId');
		Helper.confirm("确定删除该商品？",function(){
			$.ajax({								  
				url: Helper.getRootPath() + '/specialAccount/billingType/deleteBillingType.do',
				type: 'post',
				dataType : "json",
				data: {
					"bId": bId
				},
				success: function (data) {
					if(data > 0) {
						location = Helper.getRootPath() + '/specialAccount/billingType/listBillingType.do';
					} else {
						Helper.alert("系统故障，请稍后再试");
						return false;
					}
				},
				error:function(){
					Helper.alert("系统故障，请稍后再试");
				}
			});
		});
	},
	searchReset: function(){
		location = Helper.getRootPath() + '/specialAccount/billingType/listBillingType.do';
	},
	loadBillingTypeModal : function(type, bId){
		$('#billingTypeModal').modal({
			backdrop : "static"
		});
		if(type == 1){
			$('#billingTypeModal_h').html("账户类型编辑");
			$.ajax({								  
	            url: Helper.getRootPath() + '/specialAccount/billingType/getBillingTypeById.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "bId": bId
	            },
	            success: function (data) {
	            	$('#billingTypeInf_bId').val(data.bId);
	            	$('#billingTypeInf_name').val(data.name);
	            	$('#billingTypeInf_code').val(data.code);
	            	$('#billingTypeInf_remarks').val(data.remarks);
	            	if (data.type=='A') {
	            		$("input:radio[value='A']").attr('checked', true);
					} else {
						$("input:radio[value='B']").attr('checked', true);
					}
	            },
	            error:function(){
	            	Helper.alert("系统故障，请稍后再试");
	            }
		    });
		}else if(type == 2){
			$('#billingTypeModal_h').html("账户类型新增");
			$('#billingTypeInf_bId').val("");
        	$('#billingTypeInf_name').val("");
        	$('#billingTypeInf_code').val("");
        	$('#billingTypeInf_remarks').val("");
		}
		
	}
}
