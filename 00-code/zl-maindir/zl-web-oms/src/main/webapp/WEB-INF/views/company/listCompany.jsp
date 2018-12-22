<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <script src="${ctx}/static/oms/js/company/listCompany.js"></script>
</head>
<body>
	   <%@ include file="/WEB-INF/views/common/navbar.jsp"%>
            <div id="contentwrapper">
                <div class="main_content">
                	<nav>
			            <div id="jCrumbs" class="breadCrumb module">
			                <ul>
			                    <li><a href="#"><i class="icon-home"></i></a></li>
			                    <li>企业管理</li>
			                    <li><a href="${ctx }/company/listCompany.do">企业信息管理</a></li>
			                    <li>企业信息列表</li>
			                </ul>
			            </div>
			        </nav>
					<form id="searchForm" action="${ctx }/company/listCompany.do" class="form-inline" method="post">
						<input type="hidden" id="operStatus"  value="${operStatus }"/>
						<h3 class="heading">企业信息列表</h3>
						<div class="row-fluid" id="h_search">
							 <div class="span10">
		                       	<div class="input-prepend">
		           			   	   	<span class="add-on">企业名称</span><input id="name" name="name" type="text" class="input-medium" value="${companyInf.name }" />
		                       	</div>
		                       	<div class="input-prepend">
		           			   	   	<span class="add-on">交易开关状态</span>
		           			   	   	<select name="transFlag" id="transFlag" class="input-medium">
	                                    <option value="">--请选择--</option>
	                                    <option <c:if test="${companyInf.transFlag == '0' }">selected="selected"</c:if> value="0">开</option>
	                                    <option <c:if test="${companyInf.transFlag == '1' }">selected="selected"</c:if> value="1">关</option>
                                    </select>
		                       	</div>
		                       	<div class="input-prepend">
		           			   	   	<span class="add-on">联系人</span><input id="contacts" name="contacts" type="text" class="input-medium" value="${companyInf.contacts }" />
		                       	</div>
							</div>
							<div class="pull-right">
								<button type="submit" class="btn btn-search">查 询</button>
								<button type="reset" class="btn btn-inverse btn-reset">重 置</button>
								<sec:authorize access="hasRole('ROLE_COMPANY_INTOADD')">
								<button type="button" class="btn btn-primary btn-add">新增企业</button>
								</sec:authorize>
							</div>
						</div>
						
				         </br >       
				         <table class="table table-striped table-bordered dTableR table-hover" id="dt_gal">
				             <thead>
				             <tr>
				               <th>企业名称</th>
				               <th>统一社会信用代码</th>
				               <th>地址</th>
				               <th>联系人</th>
				               <th>联系电话</th>
								<th>平台标识</th>
				               <th>交易开关状态</th>
				               <th>开户状态</th>
				               <th>备注</th>
				               <th>操作</th>
				             </tr>
				             </thead>
				             <tbody>
					             <c:forEach var="company" items="${pageInfo.list}" varStatus="st">
					                 <tr>
					                 	<td>${company.name}</td>
					                    <td>${company.lawCode}</td>
					                    <td>${company.address}</td>
					                    <td>${company.contacts}</td>
					                    <td>${company.phoneNo}</td>
										 <td>
											 <c:if test="${company.isPlatform=='1'}">是</c:if>
											 <c:if test="${company.isPlatform=='0'}">否</c:if>
										 </td>
										 <td>
					                    	<c:if test="${company.transFlag=='1'}">关</c:if>
					                    	<c:if test="${company.transFlag=='0'}">开</c:if>
					                    </td>	
					                    <td>
					                    	<c:if test="${company.isOpen=='1'}">已开户</c:if>
					                    	<c:if test="${company.isOpen=='0'}">未开户</c:if>
					                    </td>			                 
					                    <td>${company.remarks}</td>
					                    <td>
					                    	<c:if test="${company.isOpen=='0'}">
					                    	<sec:authorize access="hasRole('ROLE_COMPANY_OPENACCOUNT')">
												<a companyId="${company.companyId}" title="开户" class="btn-mini btn-open" href="#"><i class="icon-pencil"></i></a>
					                    	</sec:authorize>
					                    	</c:if>
					                    	<sec:authorize access="hasRole('ROLE_COMPANY_INTOEDIT')">
												<a companyId="${company.companyId}" title="编辑" class="btn-mini btn-edit" href="#"><i class="icon-edit"></i></a>
											</sec:authorize>
											<sec:authorize access="hasRole('ROLE_COMPANY_DELETE')">	
												<a companyId="${company.companyId}" title="删除" class="btn-mini btn-delete" href="#"><i class="icon-remove"></i></a>
					                    	</sec:authorize>
											<c:if test="${company.isOpen=='1'} && ${company.isPlatform=='1'}">
												<a companyId="${company.companyId}" title="收款" class="btn-mini btn-tansfer" href="#"><i class="icon-pencil"></i></a>
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
		        <input type="hidden" id="companyId" name="companyId"/>
		        <span>你确定对该企业开户吗？</span>
		    </div>
		</form>
		<div class="modal-footer" style="text-align: center;">
		    <button class="btn btn-primary btn-open-submit">确 定  </button>
		    <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
		</div>
	</div>
	
	<div id="msg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
              <div class="modal-header">
                    
                    <h3 id="commodityInfModal_h2">温馨提示</h3>
                </div>
                <br/><br/><br/>
              <h3 align="center">信息正在处理......</h3>
        </div>
</body>
</html>
