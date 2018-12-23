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
		$('.btn-open').on('click', listCompany.loadAddOpenAccountModal);
		$('.btn-open-submit').on('click', listCompany.companyOpenAccount);
		$('.btn-tansfer').on('click', listCompany.intoAddCompanyTransfer);
        $('.btn-accbal').on('click', listCompany.listCompanyAccBal);
	},
	searchReset:function(){
		location = Helper.getRootPath() + '/company/listCompany.do';
	},
	intoAddcompany:function(){
		var url = Helper.getRootPath()+"/company/intoAddCompany.do";
		location.href=url;
	},
	intoEditcompany:function(){
		var companyId = $(this).attr('companyId');
		var url = Helper.getRootPath()+"/company/intoEditCompany.do?companyId="+companyId;
		location.href=url;
	},
	deleteCompanyCommit:function(){
		var companyId = $(this).attr('companyId');
		Helper.confirm("您是否删除该企业？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/company/deleteCompany.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "companyId": companyId
	            },
	            success: function (data) {
	            	if(data.status){
	            		location.href=Helper.getRootPath() + '/company/listCompany.do?operStatus=4';
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
	loadAddOpenAccountModal:function(){
		var companyId = $(this).attr('companyId');
		$('#companyId').val(companyId);
		$('#addOpenAccountModal').modal({
			backdrop : "static"
		});
	},
	companyOpenAccount : function() {
		var companyId = $('#companyId').val();
		
		$('#msg').modal({
			backdrop : "static"
		});
		
		$.ajax({								  
            url: Helper.getRootPath() + '/company/openAccountCompany.do',
            type: 'post',
            dataType : "json",
            data: {
                "companyId": companyId,
                "orderName": "企业"+companyId+"开户"
            },
            traditional:true,
            success: function (data) {
            	if(data.status){
            		location.href=Helper.getRootPath() + '/company/listCompany.do?operStatus=1';
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
    intoAddCompanyTransfer:function(){
		var companyId = $(this).attr('companyId');
		var url = Helper.getRootPath()+"/company/intoAddCompanyTransfer.do?companyId="+companyId;
		location.href=url;
	},
    listCompanyAccBal : function () {
        var companyId = $(this).attr('companyId');
        var url = Helper.getRootPath()+"/company/listCompanyAccBal.do?companyId="+companyId;
        location.href=url;
    }
}
