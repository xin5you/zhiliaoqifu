<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
	<script src="${ctx}/static/oms/js/company/listCompanyAccBal.js"></script>
</head>
<body>
	   <%@ include file="/WEB-INF/views/common/navbar.jsp"%>
            <div id="contentwrapper">
                <div class="main_content">
                	<nav>
			            <div id="jCrumbs" class="breadCrumb module">
			                <ul>
								<li><a href="#"><i class="icon-home"></i></a></li>
								<li>
									<c:if test="${isPlatform=='0'}">企业管理</c:if>
									<c:if test="${isPlatform=='1'}">平台管理</c:if>
								</li>
								<li>
									<c:if test="${isPlatform=='0'}">
										<a href="${ctx }/company/listCompany.do?isPlatform=0">企业信息管理</a>
									</c:if>
									<c:if test="${isPlatform=='1'}">
										<a href="${ctx }/company/listCompany.do?isPlatform=1">平台信息管理</a>
									</c:if>
								</li>
								<li>
									<c:if test="${isPlatform=='0'}">企业信息列表</c:if>
									<c:if test="${isPlatform=='1'}">平台信息列表</c:if>
								</li>
								<li>
									<c:if test="${isPlatform=='0'}">企业账户余额列表</c:if>
									<c:if test="${isPlatform=='1'}">平台账户余额列表</c:if>
								</li>
			                </ul>
			            </div>
			        </nav>
					<form id="searchForm" action="${ctx }/company/listCompanyAccBal.do" class="form-inline" method="post">
						<h3 class="heading">
							<c:if test="${isPlatform=='0'}">企业账户余额列表</c:if>
							<c:if test="${isPlatform=='1'}">平台账户余额列表</c:if>
						</h3>
						<input type="hidden" id="companyId" name="companyId" value="${companyId}"/>
						<input type="hidden" id="isPlatform" name="isPlatform" value="${isPlatform}"/>
					<%--<div class="row-fluid" id="h_search">
							 <div class="span10">
		                       	<div class="input-prepend">
		           			   	   	<span class="add-on">供应商名称</span><input id="providerName" name="providerName" type="text" class="input-medium" maxlength="32" value="${providerInf.providerName }" />
		                       	</div>
							</div>
							<div class="pull-right">
								<button type="submit" class="btn btn-search"> 查 询 </button>
								<button type="reset" class="btn btn-inverse btn-reset">重 置</button>
							</div>
						</div>
				         </br >  --%>
				         <table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				             <thead>
					             <tr>
					              <%-- <th>账户号</th>
					               <th>企业ID</th>--%>
					               <th>
									   <c:if test="${isPlatform=='0'}">企业名称</c:if>
									   <c:if test="${isPlatform=='1'}">平台名称</c:if>
								   </th>
					               <th>账户名称</th>
					               <th>账户余额(元)</th>
									<th>操作</th>
					             </tr>
				             </thead>
				             <tbody>
				             <c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
				                 <tr>
				                 	<%--<td>${entity.accountNo}</td>
				                 	<td>${entity.userId}</td>--%>
									<td>${entity.personalName}</td>
									<td>${entity.BId}</td>
				                    <td>${entity.accBal}</td>
									 <td>
										 <a bId="${entity.BId}" title="账单明细" class="btn-mini btn-accBal-detail" href="#"><i class="icon-search"></i></a>
									 </td>
				                 </tr>
				             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
						<a href="${ctx }/company/listCompany.do?isPlatform=${isPlatform}"><button class="btn btn-primary" type="button">返 回</button></a>
				      </form>
			   </div>
	    </div>
</body>
</html>
