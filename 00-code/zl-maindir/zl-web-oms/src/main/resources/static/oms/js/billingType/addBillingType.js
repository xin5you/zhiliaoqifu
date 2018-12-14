$(document).ready(function () {
    addBillingType.init();
})

var addBillingType = {
    init: function () {
    	addBillingType.initEvent();
    },
    initEvent:function(){
		$('.btn-submit').on('click', addBillingType.addBillingTypeSubmit);
	},
	addBillingTypeSubmit:function(){
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
	            url: Helper.getRootPath() + '/billingType/addBillingType.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "bName" : name,
	                "code" : code,
	                "remarks" : remarks,
	                "loseFee" : loseFee,
	                "buyFee" : buyFee
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
};

