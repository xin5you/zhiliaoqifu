$(document).ready(function() {
	listCompany.init();
})

var listCompany = {

	init : function() {
		listCompany.initEvent();
		var operStatus=$("#operStatus").val();
		Helper.operTip(operStatus);
	},

	initEvent:function(){
	
		$('.btn-edit').on('click', listCompany.intoEditcompany);
		$('.btn-delete').on('click', listCompany.deleteCompanyCommit);
		$('.btn-add').on('click', listCompany.intoAddcompany);
		$('.btn-reset').on('click', listCompany.searchReset);
		$('.btn-open').on('click', listCompany.openAccountCompany);
		$('.btn-tansfer').on('click', listCompany.intoAddTransferAccount);
		
	},
	searchReset:function(){
		location = Helper.getRootPath() + '/specialAccount/company/listCompany.do';
	},
	intoAddcompany:function(){
		var url = Helper.getRootPath()+"/specialAccount/company/intoAddCompany.do";
		location.href=url;
	},
	intoEditcompany:function(){
		var companyId = $(this).attr('companyId');
		var url = Helper.getRootPath()+"/specialAccount/company/intoEditCompany.do?companyId="+companyId;
		location.href=url;
	},
	deleteCompanyCommit:function(){
		var companyId = $(this).attr('companyId');
		Helper.confirm("您是否删除该企业？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/specialAccount/company/deleteCompany.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "companyId": companyId
	            },
	            success: function (data) {
	            	if(data.status){
	            		location.href=Helper.getRootPath() + '/specialAccount/company/listCompany.do?operStatus=4';
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
	openAccountCompany : function() {
		var companyId = $(this).attr('companyId');
		Helper.confirm("您是否对该企业进行开户？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/specialAccount/company/openAccountCompany.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "companyId": companyId
	            },
	            success: function (data) {
	            	if(data.status){
	            		location.href=Helper.getRootPath() + '/specialAccount/company/listCompany.do?operStatus=4';
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
	intoAddTransferAccount:function(){
		var companyId = $(this).attr('companyId');
		var url = Helper.getRootPath()+"/specialAccount/company/intoAddTransferAccount.do?companyId="+companyId;
		location.href=url;
	}
}
