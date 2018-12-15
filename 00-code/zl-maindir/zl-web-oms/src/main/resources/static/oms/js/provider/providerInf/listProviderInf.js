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
		$('.btn-openAccount').on('click', listTelProviderInf.loadAddOpenAccountModal);
		$('.btn-openAccount-submit').on('click', listTelProviderInf.telProviderOpenAccount);
		$('.btn-transfer').on('click', listTelProviderInf.loadAddProviderTransferModal);
		$('.btn-submit').on('click', listTelProviderInf.addProviderTransferCommit);
	},
	searchData: function(){
		document.forms['searchForm'].submit();
	},
	searchReset: function(){
		location = Helper.getRootPath() + '/provider/providerInf/listProviderInf.do';
	},
	intoAddTelProviderInf:function(){
		var url = Helper.getRootPath()+"/provider/providerInf/intoAddProviderInf.do";
		location.href=url;
	},
	intoEditTelProviderInf:function(){
		var providerId = $(this).attr('providerId');
		var url = Helper.getRootPath()+"/provider/providerInf/intoEditProviderInf.do?providerId="+providerId;
		location.href=url;
	},
	intoViewTelProviderInf:function(){
		var providerId = $(this).attr('providerId');
		var url = Helper.getRootPath()+"/provider/providerInf/intoViewProviderInf.do?providerId="+providerId;
		location.href=url;
	},
	deleteTelProviderInfCommit:function(){
		var providerId = $(this).attr('providerId');
		Helper.confirm("您是否删除该记录？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/provider/providerInf/deleteProviderInfCommit.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "providerId": providerId
	            },
	            success: function (result) {
	            	if(result.status){
	            		location.href=Helper.getRootPath() + '/provider/providerInf/listProviderInf.do?operStatus=4';
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
	loadAddOpenAccountModal:function(){
		var providerId = $(this).attr('providerId');
		$('#providerId').val(providerId);
		$('#addOpenAccountModal').modal({
			backdrop : "static"
		});
	},
	telProviderOpenAccount : function() {
		var providerId = $('#providerId').val();
		
		$('#msg').modal({
			backdrop : "static"
		});
		
		$.ajax({								  
            url: Helper.getRootPath() + '/provider/providerInf/providerOpenAccount.do',
            type: 'post',
            dataType : "json",
            data: {
                "providerId": providerId,
                "companyId": providerId,
                "orderName": "供应商"+providerId+"开户"
            },
            success: function (data) {
            	if(data.status){
            		location.href=Helper.getRootPath() + '/provider/providerInf/listProviderInf.do?operStatus=4';
            	}else{
            		$('#msg').modal('hide');
            		Helper.alter(data.msg);
            		return false;
            	}
            },
            error:function(){
            	$('#msg').modal('hide');
            	Helper.alert("系统超时，请稍微再试试");
            	return false;
            }
      });
	},
	loadAddProviderTransferModal:function(){
		$('#addTransferModal').modal({
			backdrop : "static"
		});
	},
	addProviderTransferCommit:function(){
		var amount = $("#amount").val();
		var companyId = $("#companyId").val();
		var providerId = $(this).attr('providerId');
		if(amount==''){
			Helper.alert("请输入转账金额");
    		return false;
		}
		if(companyId==''){
			Helper.alert("请选择转账至企业");
    		return false;
		}
		if(!/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(amount)){
			Helper.alert("请输入正确的转账金额");
    		return false;
		}
		$.ajax({
			url: Helper.getRootPath() + '/provider/providerInf/addProviderTransferCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "amount" : amount, 
                "companyId" : companyId, 
                "providerId" : providerId
            },
            success: function (result) {
            	if(result.status) {
                	location = Helper.getRootPath() + '/provider/providerInf/listProviderInf.do?operStatus=4';
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
