
$(document).ready(function () {
    addGateway.init();
})

var addGateway = {

    init: function () {
    	addGateway.initEvent();
    },
    initEvent:function(){
		$('.btn-submit').on('click', addGateway.addGatewaySubmit);
	},
	addGatewaySubmit: function(){
		var desc = $("#desc").val();
		var name = $("#name").val();
		if(name ==''){
    		Helper.alert("请输入网关名称");
    		return false;
    	}
		var code = $("#code").val();
		if(code =='' || code == null){
    		Helper.alert("请选择网关代码");
    		return false;
    	}
		var remarks = $("#remarks").val();
		var bId=[];
        $("input[name='bId']:checked").each(function(i){
        	bId[i] = $(this).val();
         });
		$.ajax({								  
	            url: Helper.getRootPath() + '/specialAccount/gateway/addGateway.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "bId" : bId,
	                "desc" : desc,
	                "name" : name,
	                "remarks" : remarks,
	                "code" : code
	            },
	            traditional:true,
	            success: function (data) {
	            	if(data == 'success') {
	                	location = Helper.getRootPath() + '/specialAccount/gateway/listGateway.do';
	                }else if(data == 'repeat'){
	                	Helper.alert("网关信息已存在，请重新输入");
					}else if(data == 'false'){
						Helper.alert("网关信息新增失败，请重试");
					}
	            },
	            error:function(){
	            	Helper.alert("系统故障，请稍后再试");
	            }
		    });
	}
};

