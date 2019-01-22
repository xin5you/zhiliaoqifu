<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/common/init.jsp"%>
	<%@ include file="/WEB-INF/views/common/head.jsp"%>
	<link rel="stylesheet" href="${ctx}/static/datetimepicker/css/bootstrap-datetimepicker.0.0.11.min.css" />
	<script src="${ctx}/static/datetimepicker/js/bootstrap-datetimepicker.0.0.11.min.js"></script>
	<script src="${ctx}/static/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
	<script src="${ctx}/static/oms/js/retailChnl/retailChnlInf/addRetailChnlTransfer.js"></script>
	<script src="${ctx}/static/jquery/jquery.form.js"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/common/navbar.jsp"%>
<div id="contentwrapper">
	<div class="main_content">
		<nav>
			<div id="jCrumbs" class="breadCrumb module">
				<ul>
					<li><a href="#"><i class="icon-home"></i></a></li>
					<li>分销商管理</li>
					<li><a href="${ctx }/retailChnl/retailChnlInf/listRetailChnlInf.do">分销商信息管理</a></li>
					<li>分销商信息列表</li>
					<li>上账管理</li>
					<li>上账信息列表</li>
				</ul>
			</div>
		</nav>
		<form id="pageMainForm" action="${ctx}/retailChnl/retailChnlInf/intoAddRetailChnlTransfer.do?channelId=${channelId }&orderType=${orderType }" class="form-inline form_validation_tip" method="post">
			<h3 class="heading">上账信息列表</h3>

			<table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				<thead>
				<tr>
					<th>序号</th>
					<th>订单号</th>
					<th>审核状态</th>
					<th>打款金额(元)</th>
					<th>上账金额(元)</th>
					<th>收款分销商</th>
					<th>上账状态</th>
					<th>平台收款状态</th>
                    <th>分销商收款状态</th>
					<th>打款凭证</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
					<tr>
						<td>${st.index+1 }</td>
						<td>${entity.orderId}</td>
						<td>
							<c:if test="${entity.checkStat == '0'}">未审核</c:if>
							<c:if test="${entity.checkStat == '1'}">已审核</c:if>
						</td>
						<td>${entity.remitAmt}</td>
						<td>${entity.inaccountSumAmt}</td>
						<td>${entity.companyName}</td>
						<td>
							<c:if test="${entity.inaccountCheck == '0'}">未上账</c:if>
							<c:if test="${entity.inaccountCheck == '1'}">已上账</c:if>
						</td>
						<td>
							<c:if test="${entity.platformReceiverCheck == '0'}">未收款</c:if>
							<c:if test="${entity.platformReceiverCheck == '1'}">已收款</c:if>
						</td>
						<td>
							<c:if test="${entity.companyReceiverCheck == '0'}">未收款</c:if>
							<c:if test="${entity.companyReceiverCheck == '1'}">已收款</c:if>
						</td>
						<td>
							<c:if test="${entity.evidenceUrl != null && entity.evidenceUrl != ''}">
								<div id="evidenceUrlDiv" evidenceImage="${entity.evidenceUrl}" style="height: 200px; width: 200px">
									<img style="height: 100%; width: 100%" src="data:image/jpg;base64,${entity.evidenceUrl}" />
								</div>
							</c:if>
						</td>
						<td>
							<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_TRANSFER_DETAIL')">
								<a orderId="${entity.orderId}" title="上账明细" class="btn-mini btn-view" href="#"><i class="icon-search"></i></a>
							</sec:authorize>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<%@ include file="/WEB-INF/views/common/pagination.jsp"%>

			<br/>
			<a href="${ctx }/retailChnl/retailChnlInf/listRetailChnlInf.do"><button class="btn btn-primary" type="button">返 回</button></a>
		</form>
	</div>
</div>

</body>
</html>
