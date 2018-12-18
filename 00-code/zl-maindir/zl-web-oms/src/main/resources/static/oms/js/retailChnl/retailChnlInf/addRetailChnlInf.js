$(document).ready(function () {
    addRetailChnlInf.init();
})

var addRetailChnlInf = {

    init: function () {
    	addRetailChnlInf.initEvent();
    },
	initEvent:function(){
		$('#addSubmitBtn').on('click', addRetailChnlInf.addRetailChnlInfCommit);
	},
	addRetailChnlInfCommit:function(){
    	var channelName=$('#channelName').val();
    	if(channelName ==''){
    		Helper.alert("请输入分销商名称");
    		return false;
    	}
    	var channelCode=$('#channelCode').val();
    	if(channelCode ==''){
    		Helper.alert("请输入分销商编号");
    		return false;
    	}
    	
    	var channelKey=$('#channelKey').val();
    	if(channelKey ==''){
    		Helper.alert("请输入分销商KEY");
    		return false;
    	}
    	var phoneNo=$('#phoneNo').val();
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
    	var channelReserveAmt=$('#channelReserveAmt').val();
    	var channelPrewarningAmt=$('#channelPrewarningAmt').val();
    	var email=$('#email').val();
    	var remarks=$('#remarks').val();
    	Helper.confirm("确定提交吗？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/addRetailChnlInfCommit.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "channelName" : channelName,
	                "channelCode" : channelCode,
	                "channelKey" : channelKey,
	                "channelReserveAmt" : channelReserveAmt,
	                "channelPrewarningAmt" : channelPrewarningAmt,
	                "phoneNo" : phoneNo,
	                "email" : email,
	                "remarks" : remarks
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

