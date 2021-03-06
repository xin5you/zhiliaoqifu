$(document).ready(function() {
	editRecharge.init();
})

var editRecharge = {
	init : function() {
		editRecharge.initEvent();
		var operStatus=$("#operStatus").val();
		Helper.operTip(operStatus);
	},

	initEvent:function(){
		$('.btn-editAddRecharge').on('click', editRecharge.loadEditAddRechargeModal);
		$('.btn-submit').on('click', editRecharge.addOrderCommit);
		$('.btn-delete').on('click', editRecharge.deleteOrderCommit);
	},
	loadEditAddRechargeModal:function(){
		$('#editAddRechargeModal').modal({
			backdrop : "static"
		});
	},
	addOrderCommit:function(){
		var orderId = $("#orderId").val();
		var name = $("#name").val();
		var phone = $("#phone").val();
		var card = $("#card").val();
		var companyId = $("#companyId").val();
		var accountType = $("#accountType").val();
		var bizType = $("#bizType").val();
		var money = $("#money").val();
		var re = /^1\d{10}$/;
		if(name==''){
			Helper.alert("请输入姓名");
    		return false;
		}
		if(phone==''){
			Helper.alert("请输入手机号码");
    		return false;
		}
		if(phone.length!=11){
			Helper.alert("请输入有效的手机号码");
    		return false;
		}
		if(!re.test(phone)){
			Helper.alert("请输入有效的手机号码");
    		return false;
		}
		if(card.lengh>18){
			Helper.alert("请输入有效的身份证");
    		return false;
		}
		if(card.length>18){
			Helper.alert("请输入有效的身份证");
    		return false;
		}
		if(companyId==''){
			Helper.alert("企业名称不正确");
    		return false;
		}
		if(money==''){
			Helper.alert("请输入充值金额");
    		return false;
		}
		if(!/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(money)){
			Helper.alert("请输入正确的充值金额");
    		return false;
		}
		$.ajax({
			url: Helper.getRootPath() + '/batch/recharge/addOrderListCommit.do',
            type: 'post',
            dataType : "json",
            data: {
            	"orderId":orderId,
                "name" : name, 
                "phone" : phone, 
                "card" : card,
                "companyId" : companyId,
                "accountType" : accountType,
                "bizType" : bizType,
                "money":money
            },
            success: function (result) {
            	if(result.status) {
                	location = Helper.getRootPath() + '/batch/recharge/intoEditRecharge.do?operStatus=1&orderId='+orderId;
                } else {
                	Helper.alert(result.msg);
                	return false;
                }
            },
            error:function(){
            	Helper.alert("系统故障，请稍后再试");
            }
		});
	},
	deleteOrderCommit:function(){
		var orderListId = $(this).attr('orderListId');
		var orderId = $("#orderId").val();
		Helper.confirm("您确认删除该用户信息？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/batch/recharge/deleteOrderListCommit.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "orderListId": orderListId
	            },
	            success: function (result) {
	            	if(result.status){
	            		location.href=Helper.getRootPath() + '/batch/recharge/intoEditRecharge.do?operStatus=4&orderId='+orderId;
	            	}else{
	            		Helper.alter(result.msg);
	            	}
	            },
	            error:function(){
	            	Helper.alert("系统故障，请稍后再试");
	            }
	      });
		});
	}
}
