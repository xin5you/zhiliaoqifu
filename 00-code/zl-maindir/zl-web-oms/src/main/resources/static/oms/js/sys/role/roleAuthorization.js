$(document).ready(function() {
	grantRole.init();
})

var grantRole = {

	init : function() {
		grantRole.initEvent();
	},

	initEvent:function(){
		$('.grant-role-submit').on('click', grantRole.grantRoleSubmit);
	},
	grantRoleSubmit : function() {
		$(".grant-role-submit").attr('disabled',"true");
		var roleId=$('#roleId').val();
		var treeObj =$.fn.zTree.getZTreeObj("grantRoleZtreeId");
		var nodes=treeObj.getCheckedNodes(true);
		var ids=new Array();
		for(var i=0;i<nodes.length;i++){
			ids.push(nodes[i].id);
		}
	    $.ajax({
              url: Helper.getRootPath() + '/sys/role/submitRoleAuthorization.do',
              type: 'post',
              dataType : "json",
              data: {
                  roleId: roleId,
                  ids:ids
              },
              success: function (result) {
            	  $(".grant-role-submit").removeAttr("disabled");
            	  if(result.status){
            		  Helper.stickySuccess("角色授权成功");
            	  }else{
            		  Helper.stickyError(result.msg);
            		  return false;
            	  }
              },
              error:function(){
            	  Helper.stickyError("角色授权成功");
            	  return false;
              }
        });
	}
}
