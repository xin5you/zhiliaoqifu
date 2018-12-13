$(document).ready(function() {
	listTelProviderInf.init();
})

var listTelProviderInf = {
	init : function() {
		listTelProviderInf.initEvent();
		var operStatus=$("#operStatus").val();
		Helper.operTip(operStatus);
	},

	initEvent:function(){
		$('.btn-edit').on('click', listTelProviderInf.intoEditTelProviderInf);
		$('.btn-delete').on('click', listTelProviderInf.deleteTelProviderInfCommit);
		$('.btn-add').on('click', listTelProviderInf.intoAddTelProviderInf);
		$('.btn-view').on('click', listTelProviderInf.intoViewTelProviderInf);
		$('.btn-search').on('click', listTelProviderInf.searchData);
		$('.btn-reset').on('click', listTelProviderInf.searchReset);
		$('.btn-openAccount').on('click', listTelProviderInf.telProviderOpenAccount);
		$('.btn-transfer').on('click', listTelProviderInf.loadAddTransferModal);
		$('.btn-submit').on('click', listTelProviderInf.addTransferCommit);
	},
	searchData: function(){
		document.forms['searchForm'].submit();
	},
	searchReset: function(){
		location = Helper.getRootPath() + '/provider/providerInf/listTelProviderInf.do';
	},
	intoAddTelProviderInf:function(){
		var url = Helper.getRootPath()+"/provider/providerInf/intoAddTelProviderInf.do";
		location.href=url;
	},
	intoEditTelProviderInf:function(){
		var providerId = $(this).attr('providerId');
		var url = Helper.getRootPath()+"/provider/providerInf/intoEditTelProviderInf.do?providerId="+providerId;
		location.href=url;
	},
	intoViewTelProviderInf:function(){
		var providerId = $(this).attr('providerId');
		var url = Helper.getRootPath()+"/provider/providerInf/intoViewTelProviderInf.do?providerId="+providerId;
		location.href=url;
	},
	deleteTelProviderInfCommit:function(){
		var providerId = $(this).attr('providerId');
		Helper.confirm("您是否删除该记录？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/provider/providerInf/deleteTelProviderInfCommit.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "providerId": providerId
	            },
	            success: function (result) {
	            	if(result.status){
	            		location.href=Helper.getRootPath() + '/provider/providerInf/listTelProviderInf.do?operStatus=4';
	            	}else{
	            		Helper.alter(result.msg);
	            	}
	            },
	            error:function(){
	            	Helper.alert("系统故障，请稍后再试");
	            }
	      });
		});
	},
	telProviderOpenAccount : function() {
		var providerId = $(this).attr('providerId');
		Helper.confirm("您是否对该供应商进行开户？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/provider/providerInf/telProviderOpenAccount.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "providerId": providerId
	            },
	            success: function (data) {
	            	if(data.status){
	            		location.href=Helper.getRootPath() + '/provider/providerInf/listTelProviderInf.do?operStatus=4';
	            	}else{
	            		Helper.alter(data.msg);
	            		return false;
	            	}
	            },
	            error:function(){
	            	Helper.alert("系统超时，请稍微再试试");
	            	return false;
	            }
	      });
		});
	},
	loadAddTransferModal:function(){
		$('#addTransferModal').modal({
			backdrop : "static"
		});
	},
	addTransferCommit:function(){
		var name = $("#name").val();
		var phone = $("#phone").val();
		var card = $("#card").val();
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
		if(card.length>18){
			Helper.alert("请输入有效的身份证");
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
			url: Helper.getRootPath() + '/speaccount/batchRecharge/addAccountInf.do',
            type: 'post',
            dataType : "json",
            data: {
                "name" : name, 
                "phone" : phone, 
                "card" : card,
                "money":money
            },
            success: function (result) {
            	if(result.status) {
                	location = Helper.getRootPath() + '/speaccount/batchRecharge/intoAddRecharge.do';
                } else {
                	Helper.alert(result.msg);
                	return false;
                }
            },
            error:function(){
            	Helper.alert("系统故障，请稍后再试");
            }
		});
	
	}
}
