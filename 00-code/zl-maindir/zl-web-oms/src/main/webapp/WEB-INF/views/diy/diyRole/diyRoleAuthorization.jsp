<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <link rel="stylesheet" href="${ctx}/static/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script src="${ctx}/static/ztree/jquery.ztree.core.min.js"></script>
	<script src="${ctx}/static/ztree/jquery.ztree.excheck.js"></script>
	<script src="${ctx}/static/oms/js/diy/diyRole/diyRoleAuthorization.js"></script>
</head>
	<SCRIPT type="text/javascript">
	$(document).ready(function() {
		var setting = {
			check: {
				enable: true
			},
			data: {
				simpleData: {
					enable: true
				}
			}
		};
		var roleId="${roleId}";
		$.ajax({
               url: Helper.getRootPath() + '/diy/diyRole/getDiyRoleResources.do',
               type: 'post',
               dataType : "json",
               data: {
                   id: roleId
               },
               success: function (m) {
					$.fn.zTree.init($("#grantRoleZtreeId"), setting, m.json);
               }
		});
	});
	</SCRIPT>


<BODY>

	<div class="alert alert-block alert-warning fade in right">
		<sec:authorize access="hasRole('ROLE_DIY_ROLE_AUTHCOMMIT')">
        <p><a class="btn btn-primary grant-role-submit" href="#">保存</a></p>
        </sec:authorize>
    </div>
    <input type="hidden" id="roleId" value="${roleId }">
	<div class="zTreeDemoBackground left">
		<ul id="grantRoleZtreeId" class="ztree"></ul>
	</div>
	
	<div id="msg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
              <div class="modal-header">
                    
                    <h3 id="commodityInfModal_h">温馨提示</h3>
                </div>
                <br/><br/><br/>
              <h3 align="center">信息正在处理......</h3>
        </div>
</BODY>
</html>