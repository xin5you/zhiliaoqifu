<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/common/init.jsp"%>
	<%@ include file="/WEB-INF/views/common/head.jsp"%>
	<script src="${ctx}/static/oms/js/provider/providerInf/addProviderFee.js"></script>
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
					<li>供应商专项费率管理</li>
				</ul>
			</div>
		</nav>
		<form id="searchForm" action="${ctx }/provider/providerInf/listProviderFee.do" class="form-inline" method="post">
			<input type="hidden" id="providerId" name="providerId" value="${providerId }"/>
			<h3 class="heading">供应商专项费率列表</h3>
			<div class="row-fluid" id="h_search">
				<div class="span10">
					<div class="input-prepend">
						<span class="add-on">专项类型名称</span><input id="bName" name="bName" type="text" class="input-medium" value="${providerBillingTypeInf.BName }" />
					</div>
				</div>
				<div class="pull-right">
					<button type="submit" class="btn btn-search">查 询</button>
					<button type="reset" class="btn btn-inverse btn-reset">重 置</button>
					<sec:authorize access="hasRole('ROLE_PROVIDER_FEE_INTOADD')">
						<button type="button" class="btn btn-primary btn-add">新增专项费率</button>
					</sec:authorize>
				</div>
			</div>

			</br >
			<table class="table table-striped table-bordered dTableR table-hover" id="dt_gal">
				<thead>
				<tr>
					<th>供应商名称</th>
					<th>专项类型名称</th>
					<th>费率</th>
					<th>备注</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="providerBilling" items="${pageInfo.list}" varStatus="st">
					<tr>
						<td>${providerBilling.providerName}</td>
						<td>${providerBilling.BName}</td>
						<td>${providerBilling.fee}</td>
						<td>${providerBilling.remarks}</td>
						<td>
							<sec:authorize access="hasRole('ROLE_PROVIDER_FEE_INTOEDIT')">
								<a providerBillingId="${providerBilling.id}" title="编辑" class="btn-mini btn-edit" href="#"><i class="icon-edit"></i></a>
							</sec:authorize>
							<sec:authorize access="hasRole('ROLE_PROVIDER_FEE_DELETE')">
								<a providerBillingId="${providerBilling.id}" title="删除" class="btn-mini btn-delete" href="#"><i class="icon-remove"></i></a>
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

<div id="addProviderFeeModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<form id="addTransferFrom" class="form-horizontal" enctype="multipart/form-data" method="post">
		<div class="modal-header">
			<button class="close" data-dismiss="modal">&times;</button>
			<h3 id="commodityInfModal_h">添加供应商专项类型费率信息</h3>
		</div>
		<div class="modal-body">
			<input type="hidden" id="providerBillingTypeId" name="providerBillingTypeId"/>
			<fieldset>
				<div class="control-group">
					<label class="control-label">专项类型：</label>
					<div class="controls">
						<select name="bId" id="bId" class="span3">
							<c:forEach var="b" items="${billingTypeList}" varStatus="st">
								<option value="${b.bId}">${b.name}</option>
							</c:forEach>
						</select>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">费率：</label>
					<div class="controls">
						<input type="text" class="span3" id="fee" name ="fee" onkeyup="checkPrice(this)" />
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
		<sec:authorize access="hasRole('ROLE_PROVIDER_FEE_COMMIT')">
			<button id="btn-submit" class="btn btn-primary btn-submit">确 定  </button>
		</sec:authorize>
		<button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
	</div>
</div>
</body>

<script type="text/javascript">
    //验证价格（带有小数点，小数点最多是三位）
    function checkPrice(obj){
        obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字而不是
        obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符
        obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d\d).*$/,'$1$2.$3');//只能输入三个小数
        if(obj.value.indexOf(".")< 0 && obj.value !=""){//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
            obj.value= parseFloat(obj.value);
        }
    }
</script>
</html>
