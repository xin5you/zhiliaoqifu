<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/common/init.jsp"%>
	<%@ include file="/WEB-INF/views/common/head.jsp"%>
	<script src="${ctx}/static/oms/js/billingType/listBillingType.js"></script>
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
					<li><a href="${ctx }/billingType/listBillingType.do">账户类型管理</a></li>
					<li>账户类型列表</li>
				</ul>
			</div>
		</nav>
		<form id="mainForm" action="${ctx }/billingType/listBillingType.do" class="form-inline" method="post">
			<h3 class="heading">账户类型列表</h3>
			<div class="row-fluid" id="h_search">
				<div class="span10">
					<div class="input-prepend">
						<span class="add-on">账户类型名称</span>
						<input id="bName" name="bName" type="text" class="input-small" value="${billingTypeInf.BName }" />
					</div>
				</div>
				<div class="pull-right">
					<button type="submit" class="btn btn-search">查 询</button>
					<button type="button" class="btn btn-inverse btn-reset">重 置</button>
					<!-- <button type="button" class="btn btn-primary btn-add">新 增</button> -->
				</div>
			</div>
			</br>
			<table class="table table-striped table-bordered dTableR table-hover" id="dt_gal">
				<thead>
				<tr>
					<th>账户类型ID</th>
					<th>账户类型名称</th>
					<th>账户类型代码</th>
					<th>折损率</th>
					<th>可购率</th>
					<th>备注</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="b" items="${pageInfo.list}" varStatus="st">
					<tr>
						<td>${b.BId}</td>
						<td>${b.BName}</td>
						<td>${b.code}类</td>
						<td>${b.loseFee}</td>
						<td>${b.buyFee}</td>
						<td style="width: 30%;">${b.remarks}</td>
						<td>
							<sec:authorize access="hasRole('ROLE_BILLING_TYPE_INTOEDIT')">
								<a bId="${b.BId}" title="编辑" href="#" class="btn-mini btn-edit"><i class="icon-edit"></i></a>
							</sec:authorize>
								<%-- <a bId="${b.bId}" title="删除" href="#" class="btn-mini btn-delete"><i class="icon-remove"></i></a>  --%>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<%@ include file="/WEB-INF/views/common/pagination.jsp"%>
		</form>
	</div>
</div>
<!-- <div id="billingTypeModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <form class="form-horizontal">
        <div class="modal-header">
            <button class="close" data-dismiss="modal">&times;</button>
            <h3 id="billingTypeModal_h">账户类型编辑</h3>
        </div>
        <div class="modal-body">
            <input type="hidden" id="billingTypeInf_bId" />
            <fieldset>
                <div class="control-group">
                    <label class="control-label">账户类型名称<span style="color:red">*</span></label>
                    <div class="controls">
                        <input type="text" class="span3" id="billingTypeInf_name" />
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">账户类型代码<span style="color:red">*</span></label>
                    <div class="controls">
                        <input type="text" class="span3" id="billingTypeInf_code" />
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">备注：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="billingTypeInf_remarks" />
                        <span class="help-block"></span>
                    </div>
                </div>
            </fieldset>
        </div>
    </form>
    <div class="modal-footer" style="text-align: center;">
        <button class="btn btn-primary btn-submit">提 交  </button>
        <button class="btn" data-dismiss="modal" aria-hidden="true">关 闭</button>
    </div>
</div> -->
</body>
</html>
