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
					<li>供应商管理</li>
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
					<sec:authorize access="hasRole('ROLE_PROVIDER_INTOADD')">
						<button type="button" class="btn btn-primary btn-add">新增供应商</button>
					</sec:authorize>
				</div>
			</div>

			</br >
			<table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				<thead>
				<tr>
					<th>供应商名称</th>
					<th>供应商代码</th>
					<%--<th>app_url</th>
					<th>app_Secret</th>
					<th>access_token</th>--%>
					<th>默认路由标识</th>
					<%--<th>供应商折扣</th>--%>
					<th>操作顺序</th>
					<th>是否开户</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
					<tr>
						<td>${entity.providerName}</td>
						<td>${entity.lawCode}</td>
						<%--<td>${entity.appUrl}</td>
						<td>${entity.appSecret}</td>
						<td>${entity.accessToken}</td>--%>
						<td>${entity.defaultRoute}</td>
						<%--<td>${entity.providerRate}</td>--%>
						<td>${entity.operSolr}</td>
						<td>
							<c:if test="${entity.isOpen == '0'}">未开户</c:if>
							<c:if test="${entity.isOpen == '1'}">已开户</c:if>
						</td>
						<td>
							<c:if test="${entity.isOpen=='0'}">
								<sec:authorize access="hasRole('ROLE_PROVIDER_OPENACCOUNT')">
									<a providerId="${entity.providerId}" title="开户" class="btn-mini btn-openAccount" href="#"><i class="icon-pencil"></i></a>
								</sec:authorize>
								<sec:authorize access="hasRole('ROLE_PROVIDER_DELETE')">
									<a providerId="${entity.providerId}" title="删除" class="btn-mini btn-delete" href="#"><i class="icon-remove"></i></a>
								</sec:authorize>
							</c:if>
							<sec:authorize access="hasRole('ROLE_PROVIDER_INTOEDIT')">
								<a providerId="${entity.providerId}" title="编辑" class="btn-mini btn-edit" href="#"><i class="icon-edit"></i></a>
							</sec:authorize>
							<sec:authorize access="hasRole('ROLE_PROVIDER_VIEW')">
								<a providerId="${entity.providerId}" title="详情" class="btn-mini btn-view" href="#"><i class="icon-search"></i></a>
							</sec:authorize>
							<c:if test="${entity.isOpen=='1'}">
								<sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER')">
									<a providerId="${entity.providerId}" title="上账" class="btn-mini btn-transfer" href="#"><i class="icon-pencil"></i></a>
								</sec:authorize>
								<sec:authorize access="hasRole('ROLE_PROVIDER_ACCBAL')">
									<a providerId="${entity.providerId}" title="账户余额" class="btn-mini btn-accbal" href="#"><i class="icon-search"></i></a>
								</sec:authorize>
							</c:if>
							<sec:authorize access="hasRole('ROLE_PROVIDER_FEE_LIST')">
								<a providerId="${entity.providerId}" title="添加费率" class="btn-mini btn-add-fee" href="#"><i class="icon-plus"></i></a>
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

<div id="addOpenAccountModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<form class="form-horizontal">
		<div class="modal-header">
		<sec:authorize access="hasRole('ROLE_PROVIDER_OPENACCOUNT_COMMIT')">
			<button class="close" data-dismiss="modal">&times;</button>
		</sec:authorize>
			<h3 id="commodityInfModal_h1">开户</h3>
		</div>
		<div class="modal-body">
			<input type="hidden" id="providerId" name="providerId"/>
			<span>你确定对该供应商开户吗？</span>
		</div>
	</form>
	<div class="modal-footer" style="text-align: center;">
	<sec:authorize access="hasRole('ROLE_PROVIDER_OPENACCOUNT_COMMIT')">
		<button class="btn btn-primary btn-openAccount-submit">确 定  </button>
	</sec:authorize>
		<button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
	</div>
</div>

<div id="msg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
	<div class="modal-header">

		<h3 id="commodityInfModal_h3">温馨提示</h3>
	</div>
	<br/><br/><br/>
	<h3 align="center">信息正在处理......</h3>
</div>
</body>
</html>
