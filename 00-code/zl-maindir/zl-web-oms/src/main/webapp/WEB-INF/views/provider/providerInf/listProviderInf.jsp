<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <script src="${ctx}/static/oms/js/provider/providerInf/listProviderInf.js"></script>
</head>
<body>
	   <%@ include file="/WEB-INF/views/common/navbar.jsp"%>
            <div id="contentwrapper">
                <div class="main_content">
                	<nav>
			            <div id="jCrumbs" class="breadCrumb module">
			                <ul>
			                    <li><a href="#"><i class="icon-home"></i></a></li>
			                    <li>手机充值</li>
			                    <li><a href="${ctx }/provider/providerInf/listProviderInf.do">供应商信息管理</a></li>
			                    <li>供应商信息列表</li>
			                </ul>
			            </div>
			        </nav>
					<form id="searchForm" action="${ctx }/provider/providerInf/listProviderInf.do" class="form-inline" method="post">
						<input type="hidden" id="operStatus"  value="${operStatus }"/>
						<h3 class="heading">供应商信息列表</h3>
						<div class="row-fluid" id="h_search">
							 <div class="span10">
		                       	<div class="input-prepend">
		           			   	   	<span class="add-on">供应商名称</span><input id="providerName" name="providerName" type="text" class="input-medium" maxlength="32" value="${providerInf.providerName }" />
		                       	</div>
							</div>
							<div class="pull-right">
								<button type="submit" class="btn btn-search"> 查 询 </button>
								<button type="reset" class="btn btn-inverse btn-reset">重 置</button>
								<button type="button" class="btn btn-primary btn-add">新增供应商</button>
							</div>
						</div>
						
				         </br >       
				         <table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				             <thead>
					             <tr>
					               <th>供应商名称</th>
					               <th>app_url</th>
					               <th>app_Secret</th>
					               <th>access_token</th>
					               <th>默认路由标识</th>
					               <th>供应商折扣</th>
					               <th>操作顺序</th>
					               <th>是否开户</th>
					               <th>操作</th>
					             </tr>
				             </thead>
				             <tbody>
				             <c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
				                 <tr>
				                 	<td>${entity.providerName}</td>
				                 	<td>${entity.appUrl}</td>
									<td>${entity.appSecret}</td>
									<td>${entity.accessToken}</td>
				                    <td>${entity.defaultRoute}</td>
				                    <td>${entity.providerRate}</td>
				                    <td>${entity.operSolr}</td>
				                    <td>
				                    	<c:if test="${entity.isOpen == '0'}">未开户</c:if>
				                    	<c:if test="${entity.isOpen == '1'}">已开户</c:if>
				                    </td>
				                    <td>
				                    <c:if test="${entity.isOpen=='0'}">
										<a providerId="${entity.providerId}" title="开户" class="btn-mini btn-openAccount" href="#"><i class="icon-pencil"></i></a>
				                    </c:if>
				                    <%-- <sec:authorize access="hasRole('ROLE_TEL_PROVIDER_INF_INTOEDIT')"> --%>
									<a providerId="${entity.providerId}" title="编辑" class="btn-mini btn-edit" href="#"><i class="icon-edit"></i></a>
									<%-- </sec:authorize> --%>
									<%-- <sec:authorize access="hasRole('ROLE_TEL_PROVIDER_INF_DELETE')"> --%>
									<a providerId="${entity.providerId}" title="删除" class="btn-mini btn-delete" href="#"><i class="icon-remove"></i></a>
									<%-- </sec:authorize> --%>
									<%-- <sec:authorize access="hasRole('ROLE_TEL_PROVIDER_INF_VIEW')"> --%>
									<a providerId="${entity.providerId}" title="详情" class="btn-mini btn-view" href="#"><i class="icon-search"></i></a>
									<%-- </sec:authorize> --%>
				                    <c:if test="${entity.isOpen=='1'}">
										<a providerId="${entity.providerId}" title="转账" class="btn-mini btn-transfer" href="#"><i class="icon-pencil"></i></a>
				                    </c:if>
				                    </td>
				                 </tr>
				             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
				      </form>
			   </div>
	    </div>
	    
	    <div id="addOpenAccountModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<form class="form-horizontal">
		    <div class="modal-header">
		        <button class="close" data-dismiss="modal">&times;</button>
		        <h3 id="commodityInfModal_h">开户</h3>
		    </div>
		    <div class="modal-body">
		        <input type="hidden" id="providerId" name="providerId"/>
		        <span>你确定对该供应商开户吗？</span>
		    </div>
		</form>
		<div class="modal-footer" style="text-align: center;">
		    <button class="btn btn-primary btn-openAccount-submit">确 定  </button>
		    <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
		</div>
	</div>
	    
	    <div id="addTransferModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<form class="form-horizontal">
		    <div class="modal-header">
		        <button class="close" data-dismiss="modal">&times;</button>
		        <h3 id="commodityInfModal_h">转账</h3>
		    </div>
		    <div class="modal-body">
		        <input type="hidden" id="provider_id" name="provider_id"/>
		        <fieldset>
		            <div class="control-group">
		                <label class="control-label">转账金额：</label>
		                <div class="controls">
		                    <input type="text" class="span3" id="amount" name="amount"/>
		                    <span class="help-block"></span>
		                </div>
		            </div>
		            <div class="control-group">
		                <label class="control-label">转至企业：</label>
		                <div class="controls">
		                    <select name="companyId" id="companyId" class="input-medium">
								<option value="">--请选择--</option>
								<c:forEach var="c" items="${companyList}" varStatus="sta">
									<option value="${c.companyId}"   >${c.name}</option>
								</c:forEach>
			            	</select>
		                    <span class="help-block"></span>
		                </div>  
		            </div>
		        </fieldset>
		    </div>
		</form>
		<div class="modal-footer" style="text-align: center;">
		    <button class="btn btn-primary btn-submit">确 定  </button>
		    <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
		</div>
	</div>
	
	<div id="msg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
              <div class="modal-header">
                    
                    <h3 id="commodityInfModal_h">温馨提示</h3>
                </div>
                <br/><br/><br/>
              <h3 align="center">信息正在处理......</h3>
        </div>
</body>
</html>
