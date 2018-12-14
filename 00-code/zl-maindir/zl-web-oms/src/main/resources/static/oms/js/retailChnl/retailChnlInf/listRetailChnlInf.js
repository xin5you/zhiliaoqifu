$(document).ready(function() {
	listTelChannelInf.init();
})

var listTelChannelInf = {
	init : function() {
		listTelChannelInf.initEvent();
		var operStatus=$("#operStatus").val();
		Helper.operTip(operStatus);
	},

	initEvent:function(){
		$('.btn-edit').on('click', listTelChannelInf.intoEditTelChannelInf);
		$('.btn-delete').on('click', listTelChannelInf.deleteTelChannelInfCommit);
		$('.btn-add').on('click', listTelChannelInf.intoAddTelChannelInf);
		$('.btn-view').on('click', listTelChannelInf.intoViewTelChannelInf);
		$('.btn-search').on('click', listTelChannelInf.searchData);
		$('.btn-reset').on('click', listTelChannelInf.searchReset);
		$('.btn-grant-role').on('click', listTelChannelInf.intoAddTelChannelReserve);
		$('.a').on('click', listTelChannelInf.intoAddTelChannelRate);
		$('.btn-openAccount').on('click', listTelChannelInf.telChannelOpenAccount);
	},
	searchData: function(){
		document.forms['searchForm'].submit();
	},
	searchReset: function(){
		location = Helper.getRootPath() + '/retailChnl/retailChnlInf/listRetailChnlInf.do';
	},
	
	intoAddTelChannelInf:function(){
		var url = Helper.getRootPath()+"/retailChnl/retailChnlInf/intoAddRetailChnlInf.do";
		location.href=url;
	},
	intoEditTelChannelInf:function(){
		var channelId = $(this).attr('channelId');
		var url = Helper.getRootPath()+"/retailChnl/retailChnlInf/intoEditRetailChnlInf.do?channelId="+channelId;
		location.href=url;
	},
	intoViewTelChannelInf:function(){
		var channelId = $(this).attr('channelId');
		var url = Helper.getRootPath()+"/retailChnl/retailChnlInf/intoViewRetailChnlInf.do?channelId="+channelId;
		location.href=url;
	},
	intoAddTelChannelReserve:function(){
		var channelId = $(this).attr('channelId');
		var url = Helper.getRootPath()+"/retailChnl/retailChnlInf//listRetailChnlReserve.do?channelId="+channelId;
		location.href=url;
	},
	intoAddTelChannelRate:function(){
		var channelId = $(this).attr('channelId');
		var url = Helper.getRootPath()+"/retailChnl/retailChnlInf/intoAddRetailChnlRate.do?channelId="+channelId;
		location.href=url;
	},
	deleteTelChannelInfCommit:function(){
		var channelId = $(this).attr('channelId');
		Helper.confirm("您是否删除该记录？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/deleteRetailChnlInfCommit.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "channelId": channelId
	            },
	            success: function (result) {
	            	if(result.status){
	            		location.href=Helper.getRootPath() + '/retailChnl/retailChnlInf/listRetailChnlInf.do?operStatus=4';
	            	}else{
	            		Helper.alter(result.msg);
	            		return false;
	            	}
	            },
	            error:function(){
	            	Helper.alert("系统故障，请稍后再试");
	            }
	      });
		});
	},
	telChannelOpenAccount : function() {
		var channelId = $(this).attr('channelId');
		Helper.confirm("您是否对该分销商进行开户？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/retailChnlOpenAccount.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "channelId": channelId
	            },
	            success: function (data) {
	            	if(data.status){
	            		location.href=Helper.getRootPath() + '/retailChnl/retailChnlInf/listRetailChnlInf.do?operStatus=4';
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
	}
}
