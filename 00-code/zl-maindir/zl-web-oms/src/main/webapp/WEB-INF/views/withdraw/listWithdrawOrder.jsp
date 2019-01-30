<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/common/init.jsp"%>
	<%@ include file="/WEB-INF/views/common/head.jsp"%>
	<script src="${ctx}/static/oms/js/withdraw/listWithdrawOrder.js"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/common/navbar.jsp"%>
<div id="contentwrapper">
	<div class="main_content">
		<nav>
			<div id="jCrumbs" class="breadCrumb module">
				<ul>
					<li><a href="#"><i class="icon-home"></i></a></li>
					<li>提现管理</li>
					<li><a href="${ctx }/withdraw/listWithdrawOrder.do">提现订单管理</a></li>
					<li>提现订单信息列表</li>
				</ul>
			</div>
		</nav>
		<form id="searchForm" action="${ctx }/withdraw/listWithdrawOrder.do" class="form-inline" method="post">
			<h3 class="heading">提现订单信息列表</h3>
			<%--<div class="row-fluid" id="h_search">
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
			</div>--%>

			</br >
			<table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				<thead>
				<tr>
					<th>批次号</th>
					<th>交易日志id</th>
					<th>交易笔数</th>
					<th>交易总金额(元)</th>
					<th>错误码</th>
					<th>交易状态</th>
					<th>创建时间</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
					<tr>
						<td>${entity.batchNo}</td>
						<td>${entity.txnPrimaryKey}</td>
						<td>${entity.totalNum}</td>
						<td>${entity.totalAmount}</td>
						<td>${entity.errorCode}</td>
						<td>${entity.status}</td>
						<td>
							<jsp:useBean id="createTime" class="java.util.Date"/>
							<jsp:setProperty name="createTime" property="time" value="${entity.createTime}"/>
							<fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>
							<%--<sec:authorize access="hasRole('ROLE_WITHDRAW_VIEW')">
								<a batchNo="${entity.batchNo}" title="详情" class="btn-mini btn-view" href="#"><i class="icon-search"></i></a>
							</sec:authorize>--%>
							<sec:authorize access="hasRole('ROLE_WITHDRAW_DETAIL_LIST')">
								<a batchNo="${entity.batchNo}" title="订单明细" class="btn-mini btn-detail" href="#"><i class="icon-search"></i></a>
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

<div id="viewWithOrderModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<form id="addWithOrderFrom" class="form-horizontal" enctype="multipart/form-data" action="" method="post">
		<div class="modal-header">
			<button class="close" data-dismiss="modal">&times;</button>
			<h3 id="commodityInfModal_h">提现订单信息详情</h3>
		</div>
		<div class="modal-body">
			<fieldset>
				<div class="control-group">
					<label class="control-label">批次号：</label>
					<div class="controls">
						<input type="text" class="span3" id="batchNo" name ="batchNo" readonly="readonly"/>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">交易日志id：</label>
					<div class="controls">
						<input type="text" class="span3" id="txnPrimaryKey" name ="txnPrimaryKey" readonly="readonly"/>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">备注：</label>
					<div class="controls">
						<input type="text" class="span3" id="remarks" name ="remarks" />
						<span class="help-block"></span>
					</div>
				</div>
			</fieldset>
		</div>
	</form>
	<div class="modal-footer" style="text-align: center;">
		<button class="btn" data-dismiss="modal" aria-hidden="true">关 闭</button>
	</div>
</div>

</body>
</html>
