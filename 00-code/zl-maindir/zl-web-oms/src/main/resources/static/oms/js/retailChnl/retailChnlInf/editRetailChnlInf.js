$(document).ready(function () {
    editRetailChnlInf.init();
})

var editRetailChnlInf = {

    init: function () {
    	editRetailChnlInf.initEvent();
    },
	initEvent:function(){
		$('#addSubmitBtn').on('click', editRetailChnlInf.editRetailChnlInfCommit);
	},
	editRetailChnlInfCommit:function(){
		var channelId=$('#channelId').val();
		var lockVersion=$('#lockVersion').val();
		var channelName=$('#channelName').val();
    	var channelCode=$('#channelCode').val();
        var lawCode=$('#lawCode').val();
    	var channelKey=$('#channelKey').val();
    	var phoneNo=$('#phoneNo').val();
    	/*var channelReserveAmt=$('#channelReserveAmt').val();
    	var channelPrewarningAmt=$('#channelPrewarningAmt').val();*/
    	var email=$('#email').val();
    	var remarks=$('#remarks').val();
    	if(channelName ==''){
    		Helper.alert("请输入分销商名称");
    		return false;
    	}
    	if(channelCode ==''){
    		Helper.alert("请输入分销商编号");
    		return false;
    	}
        if(lawCode ==''){
            Helper.alert("请输入分销商代码");
            return false;
        }
    	if(channelKey ==''){
    		Helper.alert("请输入分销商KEY");
    		return false;
    	}
    	if(phoneNo == ''){
    		Helper.alert("请输入管理员手机号");
    		return false;
    	} else {
    		if (phoneNo.length < 11) {
    			Helper.alert("手机号必须要是11位");
    			return false;
    		} else {
    			var reg = /^1[3|4|5|7|8][0-9]{9}$/; //验证规则
    			if (!(reg.test(phoneNo))) {
    				Helper.alert("请输入正确的手机号");
    				return false;
    			}
    		}
    	}
        if (email != null && email != '') {
            var reMail = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
            if (!reMail.test(email)) {
                Helper.alert("请输入正确的邮箱");
                return false;
            }
        }
    	Helper.confirm("确定提交吗？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/editRetailChnlInfCommit.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	            	"channelId" : channelId,
	                "channelName" : channelName,
	                "channelCode" : channelCode,
                    "lawCode" : lawCode,
	                "channelKey" : channelKey,
	                /*"channelReserveAmt" : channelReserveAmt,
	                "channelPrewarningAmt" : channelPrewarningAmt,*/
	                "phoneNo" : phoneNo,
	                "email" : email,
	                "remarks" : remarks,
	                "lockVersion" : lockVersion
	            },
	            success: function (data) {
	            	if(data.status) {
	                	location = Helper.getRootPath() + '/retailChnl/retailChnlInf/listRetailChnlInf.do';
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
    },
	
};

