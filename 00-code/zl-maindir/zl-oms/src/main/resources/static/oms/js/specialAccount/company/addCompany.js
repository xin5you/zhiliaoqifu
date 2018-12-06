
$(document).ready(function () {
    addCompany.init();
})

var addCompany = {
    init: function () {
    	addCompany.initEvent();
    },
    initEvent:function(){
		$('.btn-submit').on('click', addCompany.addCompanySubmit);
		$('#comCode').select2();
	},
	addCompanySubmit: function(){
		/*var type = $("#type").val();
		if(type ==''){
    		Helper.alert("请输入企业类型");
    		return false;
    	}
	    var regtitle = /[0-9]{2}/;							
		if (!regtitle.test(type) || type > 99) {				
			Helper.alert("企业类型必须是两位数字");	
			return false;
			} 	*/
		var name = $("#name").val();
		if(name ==''){
    		Helper.alert("请输入企业名称");
    		return false;
    	}
		var comCode = $("#comCode").val();
		if(comCode =='' || comCode == null){
    		Helper.alert("请选择企业地区");
    		return false;
    	}
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
	            url: Helper.getRootPath() + '/specialAccount/company/addCompany.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "bId" : bId, 
	                "name" : name,
	                "comCode" : comCode,
	                "lawCode" : lawCode,
	                "address" : address,
	                "contacts" : contacts,
	                "phoneNO" : phoneNO,
	                "flag" : flag,
	                "remarks" : remarks
	               /* "type" : type*/
	            },
	            traditional:true,
	            success: function (data) {
	            	if(data == 'success') {
	                	location = Helper.getRootPath() + '/specialAccount/company/listCompany.do';
	                }else if(data == 'repeat'){
	                	Helper.alert("企业信息已存在，请重新输入");
					}else if(data == 'false'){
						Helper.alert("企业信息新增失败，请重试");
					}
	            },
	            error:function(){
	            	Helper.alert("系统故障，请稍后再试");
	            }
		    });
	}
};

