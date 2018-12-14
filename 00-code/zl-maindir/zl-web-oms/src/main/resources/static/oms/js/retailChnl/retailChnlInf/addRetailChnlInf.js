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
    	$("#mainForm").submit();
    },
	
};

