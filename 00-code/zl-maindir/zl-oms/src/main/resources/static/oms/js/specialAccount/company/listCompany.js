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
		$('.btn-delete').on('click', listCompany.deletecompanyCommit);
		$('.btn-add').on('click', listCompany.intoAddcompany);
		//$('.btn-view').on('click', listCompany.intoViewcompany);
		$('.btn-reset').on('click', listCompany.searchReset);
	},
	searchReset:function(){
		location = Helper.getRootPath() + '/specialAccount/company/listCompany.do';
	},
	intoAddcompany:function(){
		var url = Helper.getRootPath()+"/specialAccount/company/intoAddCompany.do";
		location.href=url;
	},
	intoEditcompany:function(){
		var cId = $(this).attr('cId');
		var url = Helper.getRootPath()+"/specialAccount/company/intoEditCompany.do?cId="+cId;
		location.href=url;
	},
	deletecompanyCommit:function(){
		var cId = $(this).attr('cId');
		Helper.confirm("您是否删除该企业？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/specialAccount/company/deleteCompany.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "cId": cId
	            },
	            success: function (result) {
	            	if(result.status){
	            		location.href=Helper.getRootPath() + '/specialAccount/company/listCompany.do?operStatus=4';
	            	}else{
	            		Helper.alter("删除企业信息失败，请重新操作");
	            	}
	            },
	            error:function(){
	            	Helper.alert("系统超时，请稍微再试试");
	            }
	      });
		});
	}
}
