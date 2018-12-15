$(document).ready(function () {
    editBaseDict.init();
})

var editBaseDict = {
    init: function () {
    	editBaseDict.initEvent();
    },
    initEvent:function(){
		$('.btn-submit').on('click', editBaseDict.editBaseDictSubmit);
	},
	editBaseDictSubmit:function(){
		var dictId = $("#dictId").val();
		var dictName = $("#dictName").val();
		var dictCode = $("#dictCode").val();
		var dictValue = $("#dictValue").val();
		var isdefault = $("#isdefault").val();
		var dictType = $("#dictType").val();
		var remarks = $("#remarks").val();
		
		if (!dictName) {
			Helper.alert("请输入字典名称");
			return false;
		}
		if (!dictCode) {
			Helper.alert("请输入字典代码");
			return false;
		}
		if (!dictValue) {
			Helper.alert("请输入字典值");
			return false;
		}
		Helper.confirm("确定提交吗？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/baseDict/editBaseDictCommit.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "dictId" : dictId, 
	                "dictName" : dictName,
	                "dictCode" : dictCode,
	                "dictValue" : dictValue,
	                "isdefault" : isdefault,
	                "dictType" : dictType,
	                "remarks" : remarks
	            },
	            success: function (data) {
	            	if(data.status) {
	                	location = Helper.getRootPath() + '/baseDict/listBaseDict.do';
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
	}
};

