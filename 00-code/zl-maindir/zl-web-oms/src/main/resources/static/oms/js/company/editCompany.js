
$(document).ready(function () {
    editCompany.init();
})

var editCompany = {
    init: function () {
    	editCompany.initEvent();
    },
    initEvent:function(){
		$('.btn-submit').on('click', editCompany.editCompanySubmit);
	},
	editCompanySubmit: function(){
		var companyId = $("#companyId").val();
		var name = $("#name").val();
		if(name ==''){
    		Helper.alert("请输入企业名称");
    		return false;
    	}
		var lawCode = $("#lawCode").val();
		if(lawCode =='' || lawCode == null){
    		Helper.alert("请输入统一社会信用代码");
    		return false;
    	}
		var address = $("#address").val();
		var contacts = $("#contacts").val();
		var phoneNo = $("#phoneNo").val();
		var remarks = $("#remarks").val();
		var transFlag = $("#transFlag").val();
		if (transFlag == null || transFlag == "") {
			Helper.alert("请选择交易开关");
    		return false;
		}
		$.ajax({								  
	            url: Helper.getRootPath() + '/company/editCompany.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "companyId" : companyId, 
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

