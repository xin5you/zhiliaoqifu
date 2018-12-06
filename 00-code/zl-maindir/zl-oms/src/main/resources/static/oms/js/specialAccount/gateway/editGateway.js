
$(document).ready(function () {
    editGateway.init();
})

var editGateway = {

    init: function () {
    	editGateway.initEvent();
    },
    initEvent:function(){
		$('.btn-submit').on('click', editGateway.editGatewaySubmit);
	},
	editGatewaySubmit: function(){
		var gId = $("#gId").val();
		var desc = $("#desc").val();
		var name = $("#name").val();
		if(name ==''){
    		Helper.alert("请输入网关名称");
    		return false;
    	}
		var code = $("#code").val();
		var remarks = $("#remarks").val();
		var bId=[];
        $("input[name='bId']:checked").each(function(i){
        	bId[i] = $(this).val();
         });
		$.ajax({								  
			 url: Helper.getRootPath() + '/specialAccount/gateway/editGateway.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	            	"gId" : gId, 
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
	                }else {
	                	Helper.alert("修改网关信息失败，请重新修改");
					}
	            },
	            error:function(){
	            	Helper.alert("系统故障，请稍后再试");
	            }
		    });
	}
};

