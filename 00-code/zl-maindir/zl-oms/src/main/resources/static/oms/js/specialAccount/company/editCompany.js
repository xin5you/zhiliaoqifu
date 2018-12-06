
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
		var cId = $("#cId").val();
/*		var type = $("#type").val();
		if(type ==''){
    		Helper.alert("请输入企业类型");
    		return false;
    	}
		var regtitle = /[0-9]{2}/;							
		if (!regtitle.test(type) || type > 99) {				
			Helper.alert("企业类型必须是两位数字");	
			return false;
			}*/ 	
		var name = $("#name").val();
		if(name ==''){
    		Helper.alert("请输入企业名称");
    		return false;
    	}
		var comCode = $("#comCode").val();
		var lawCode = $("#lawCode").val();
		if(lawCode =='' || lawCode == null){
    		Helper.alert("请输入统一社会信用代码");
    		return false;
    	}
		var address = $("#address").val();
		var contacts = $("#contacts").val();
		var phoneNO = $("#phoneNO").val();
		var remarks = $("#remarks").val();
		var flag = $("input:radio[name='flag']:checked").val();
		var bId=[];
        $("input[name='bId']:checked").each(function(i){
        	bId[i] = $(this).val();
         });
		$.ajax({								  
	            url: Helper.getRootPath() + '/specialAccount/company/editCompany.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "bId" : bId, 
	                "cId" : cId, 
	                "name" : name,
	                "comCode" : comCode,
	                "lawCode" : lawCode,
	                "address" : address,
	                "contacts" : contacts,
	                "phoneNO" : phoneNO,
	                "flag" : flag,
	                "remarks" : remarks
	                /*"type" : type*/
	            },
	            traditional:true,
	            success: function (data) {
	            	if(data == 'success') {
	                	location = Helper.getRootPath() + '/specialAccount/company/listCompany.do';
	                }else {
	                	Helper.alert("修改企业信息失败，请重新修改");
					}
	            },
	            error:function(){
	            	Helper.alert("系统故障，请稍后再试");
	            }
		    });
	}
};

