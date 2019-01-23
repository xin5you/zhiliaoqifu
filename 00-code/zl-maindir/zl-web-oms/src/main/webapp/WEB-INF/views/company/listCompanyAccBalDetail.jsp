<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
	<script src="${ctx}/static/oms/js/company/listCompanyAccBalDetail.js"></script>
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
								<li><a href="${ctx }/company/listCompanyAccBal.do?companyId=${companyId}">企业账户余额列表</a></li>
								<li>企业账户余额明细列表</li>
			                </ul>
			            </div>
			        </nav>
					<form id="searchForm" action="${ctx }/company/listCompanyAccBalDetail.do" class="form-inline" method="post">
						<h3 class="heading">企业账户余额明细列表</h3>
						<input type="hidden" id="companyId" name="companyId" value="${companyId}"/>

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
								   <th>交易日志id</th>
								   <th>交易日期</th>
									<th>交易时间</th>
								   <th>当前交易类型</th>
									<th>交易金额(元)</th>
									 <th>交易后余额(元)</th>
									 <th>交易描述</th>
									 <th>交易数量</th>
									 <th>交易类型</th>
									 <th>专项类型</th>
									 <th>交易渠道</th>
								   <th>交易流水号</th>
								   <th>专项类型分类</th>
									<%--<th>操作</th>--%>
					             </tr>
				             </thead>
				             <tbody>
				             <c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
				                 <tr>
				                 	<td>${entity.txnPrimaryKey}</td>
				                 	<td>${entity.txnDate}</td>
									<td>${entity.txnTime}</td>
									<td>${entity.accType}</td>
				                    <td>${entity.txnAmt}</td>
									 <td>${entity.accTotalBal}</td>
									 <td>${entity.transDesc}</td>
									 <td>${entity.transNumber}</td>
									 <td>${entity.transId}</td>
									 <td>${entity.priBId}</td>
									 <td>${entity.transChnl}</td>
									 <td>${entity.itfPrimaryKey}</td>
									 <td>${entity.code}类</td>
									 <%--<td>
										 <a bId="${entity.priBId}" title="账单详情" class="btn-mini btn-accBal-detail" href="#"><i class="icon-search"></i></a>
									 </td>--%>
				                 </tr>
				             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
						<a href="${ctx }/company/listCompanyAccBal.do?companyId=${companyId}"><button class="btn btn-primary" type="button">返 回</button></a>
				      </form>
			   </div>
	    </div>
</body>
</html>
