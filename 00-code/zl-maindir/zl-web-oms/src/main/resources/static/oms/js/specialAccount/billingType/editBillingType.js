$(document).ready(function () {
    editBillingType.init();
})

var editBillingType = {
    init: function () {
    	editBillingType.initEvent();
    },
    initEvent:function(){
		$('.btn-submit').on('click', editBillingType.editBillingTypeSubmit);
	},
	editBillingTypeSubmit:function(){
		var bId = $("#bId").val();
		var name = $("#bName").val();
		var code = $("#code").val();
		var remarks = $("#remarks").val();
		var loseFee = $("#loseFee").val();
		var buyFee = $("#buyFee").val();
		if (!name) {
			Helper.alert("请输入账户类型名称");
			return false;
		}
		if (!loseFee) {
			Helper.alert("请输入折损率");
			return false;
		}
		if (!buyFee) {
			Helper.alert("请输入可购率");
			return false;
		}
		Helper.confirm("确定提交吗？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/specialAccount/billingType/editBillingType.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "bId" : bId, 
	                "bName" : name,
	                "code" : code,
	                "remarks" : remarks,
	                "loseFee" : loseFee,
	                "buyFee" : buyFee
	            },
	            success: function (data) {
	            	if(data.status) {
	                	location = Helper.getRootPath() + '/specialAccount/billingType/listBillingType.do';
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
};

