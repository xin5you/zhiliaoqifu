
$(document).ready(function () {
    addCompany.init();
})

var addCompany = {
    init: function () {
    	addCompany.initEvent();
    },
    initEvent:function(){
		$('.btn-submit').on('click', addCompany.addCompanySubmit);
	},
	addCompanySubmit: function(){
		var name = $("#name").val();
		var lawCode = $("#lawCode").val();
		var address = $("#address").val();
		var contacts = $("#contacts").val();
		var phoneNo = $("#phoneNo").val();
		var remarks = $("#remarks").val();
		var transFlag = $("#transFlag").val();
		if(name ==''){
    		Helper.alert("请输入企业名称");
    		return false;
    	}
		if(lawCode =='' || lawCode == null){
    		Helper.alert("请输入统一社会信用代码");
    		return false;
    	}
		if(address =='' || address == null){
    		Helper.alert("请输入地址");
    		return false;
    	}
		if(contacts =='' || contacts == null){
    		Helper.alert("请输入联系人");
    		return false;
    	}
		if(phoneNo =='' || phoneNo == null){
    		Helper.alert("请输入联系电话");
    		return false;
    	} else {
    		if (phoneNo.length < 11) {
    			Helper.alert("手机号必须要是11位");
    			return false;
    		} else {
    			var reg = /^1[3|4|5|7|8][0-9]{9}$/; //验证规则
    			if (!(reg.test(phoneNo))) {
    				Helper.alert("请输入正确的联系电话");
    				return false;
    			}
    		}
    	}
		if (transFlag == null || transFlag == "") {
			Helper.alert("请选择交易开关");
    		return false;
		}
		$.ajax({								  
			url: Helper.getRootPath() + '/company/addCompany.do',
			type: 'post',
			dataType : "json",
			data: {
				"name" : name,
				"lawCode" : lawCode,
				"address" : address,
				"contacts" : contacts,
				"phoneNo" : phoneNo,
				"transFlag" : transFlag,
				"remarks" : remarks
			},
			traditional:true,
			success: function (data) {
				if(data.status) {
					location = Helper.getRootPath() + '/company/listCompany.do';
				}else {
					Helper.alert(data.msg);
					return false;
				}
			},
			error:function(){
				Helper.alert("系统故障，请稍后再试");
				return false;
			}
		});
	}
};

