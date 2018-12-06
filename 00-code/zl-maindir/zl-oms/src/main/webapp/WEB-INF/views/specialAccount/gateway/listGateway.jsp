<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <script src="${ctx}/static/oms/js/specialAccount/gateway/listGateway.js"></script>
</head>
<body>
	   <%@ include file="/WEB-INF/views/common/navbar.jsp"%>
            <div id="contentwrapper">
                <div class="main_content">
                	<nav>
			            <div id="jCrumbs" class="breadCrumb module">
			                <ul>
			                    <li><a href="#"><i class="icon-home"></i></a></li>
			                    <li>专用账户管理</li>
			                    <li><a href="${ctx }/specialAccount/gateway/listGateway.do">网关管理</a></li>
			                    <li>网关列表</li>
			                </ul>
			            </div>
			        </nav>
					<form id="searchForm" action="${ctx }/specialAccount/gateway/listGateway.do" class="form-inline" method="post">
						<input type="hidden" id="operStatus"  value="${operStatus }"/>
						<h3 class="heading">网关列表</h3>
						<div class="row-fluid" id="h_search">
							 <div class="span10">
		                       	<div class="input-prepend">
		           			   	   	<span class="add-on">网关名称</span><input id="name" name="name" type="text" class="input-medium" value="${gatewayInf.name }" />
		                       	</div>
<%-- 		                       	<div class="input-prepend">
		           			   	   	<span class="add-on">企业类型</span><input id="type" name="type" type="text" class="input-medium" value="${companyInf.type }" />
		                       	</div>
		                       	<div class="input-prepend">
		           			   	   	<span class="add-on">交易开关状态</span><input id="flag" name="flag" type="text" class="input-medium" value="${companyInf.flag }" />
		                       	</div> --%>
							</div>
							<div class="pull-right">
								<button type="submit" class="btn btn-search">查 询</button>
								<button type="reset" class="btn btn-inverse btn-reset">重 置</button>
								<sec:authorize access="hasRole('ROLE_GATEWAY_INFO_INTOADD')">
								<button type="button" class="btn btn-primary btn-add">新增网关</button>
								</sec:authorize>
							</div>
						</div>
						
				         </br >       
				         <table class="table table-striped table-bordered dTableR table-hover" id="dt_gal">
				             <thead>
				             <tr>
				               <th>网关名称</th>
				               <th>网关代码</th>
				               <th>描述</th>
				               <th>备注</th>
				               <th>操作</th>
				             </tr>
				             </thead>
				             <tbody>
				             <c:forEach var="g" items="${pageInfo.list}" varStatus="st">
				                 <tr>
				                 	<td>${g.name}</td>
				                    <td>${g.code}</td>
				                    <td>${g.desc}</td>				                 
				                    <td>${g.remarks}</td>
				                    <td>
				                            <sec:authorize access="hasRole('ROLE_GATEWAY_INFO_INTOEDIT')">
											<a gId="${g.gId}" title="编辑" class="btn-mini btn-edit" href="#"><i class="icon-edit"></i></a>
											</sec:authorize>
											<sec:authorize access="hasRole('ROLE_GATEWAY_INFO_DELETE')">
											<a gId="${g.gId}" title="删除" class="btn-mini btn-delete" href="#"><i class="icon-remove"></i></a>
											</sec:authorize>
				                    </td>
				                  </tr>
				             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
				      </form>
			   </div>
	    </div>
      <div id="msg" style="display: none;">${msg }</div>
<script>
$(function(){
	var msg = $('#msg').html();
	if (msg != "" && msg != null) {
		setTimeout(function() {
			Helper.alert(msg);	
		}, 1000);
	
	}
});
</script>
</body>
</html>
