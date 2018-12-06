$(document).ready(function() {
	listGateway.init();
})

var listGateway = {

	init : function() {
		listGateway.initEvent();
		var operStatus=$("#operStatus").val();
		Helper.operTip(operStatus);
	},

	initEvent:function(){
	
		$('.btn-edit').on('click', listGateway.intoEditGateway);
		$('.btn-delete').on('click', listGateway.deleteGatewayCommit);
		$('.btn-add').on('click', listGateway.intoAddGateway);
		//$('.btn-view').on('click', listGateway.intoViewGateway);
		$('.btn-reset').on('click', listGateway.searchReset);
	},
	searchReset:function(){
		location = Helper.getRootPath() + '/specialAccount/gateway/listGateway.do';
	},
	intoAddGateway:function(){
		var url = Helper.getRootPath()+"/specialAccount/gateway/intoAddGateway.do";
		location.href=url;
	},
	intoEditGateway:function(){
		var gId = $(this).attr('gId');
		var url = Helper.getRootPath()+"/specialAccount/gateway/intoEditGateway.do?gId="+gId;
		location.href=url;
	},
	deleteGatewayCommit:function(){
		var gId = $(this).attr('gId');
		Helper.confirm("您是否删除该网关？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/specialAccount/gateway/deleteGateway.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "gId": gId
	            },
	            success: function (result) {
	            	if(result.status){
	            		location.href=Helper.getRootPath() + '/specialAccount/gateway/listGateway.do?operStatus=4';
	            	}else{
	            		Helper.alter("删除失败，请重新操作");
	            	}
	            },
	            error:function(){
	            	Helper.alert("系统超时，请稍微再试试");
	            }
	      });
		});
	}
}
