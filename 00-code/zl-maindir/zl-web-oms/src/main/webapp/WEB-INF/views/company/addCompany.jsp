<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/common/init.jsp"%>
	<%@ include file="/WEB-INF/views/common/head.jsp"%>

	<link rel="stylesheet" href="${ctx}/resource/chosen/chosen.css" />
	<script src="${ctx}/static/chosen/chosen.jquery.js"></script>
	<script src="${ctx}/static/oms/js/company/addCompany.js"></script>

	<link rel="stylesheet" href="${ctx}/static/css/select2.css" />
	<script src="${ctx}/static/select2.js"></script>

</head>

<body>
<%@ include file="/WEB-INF/views/common/navbar.jsp"%>
<!-- main content -->
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
						<c:if test="${isPlatform=='0'}">新增企业信息</c:if>
						<c:if test="${isPlatform=='1'}">新增平台信息</c:if>
					</li>
				</ul>
			</div>
		</nav>
		<div class="row-fluid">
			<div class="span12">
				<form id="mainForm1" class="form-horizontal" method="post">
					<h3 class="heading">
						<c:if test="${isPlatform=='0'}">新增企业信息</c:if>
						<c:if test="${isPlatform=='1'}">新增平台信息</c:if>
					</h3>
					<input type="hidden" id="isPlatform" name="isPlatform" value="${isPlatform}"/>
					<div class="control-group formSep">
						<label class="control-label">
							<c:if test="${isPlatform=='0'}">企业名称</c:if>
							<c:if test="${isPlatform=='1'}">平台名称</c:if>
							<span style="color:red">*</span>
						</label>
						<div class="controls">
							<input type="text" class="span6" id="name" name="name" maxlength="64" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="control-group formSep">
						<label class="control-label">统一社会信用代码<span style="color:red">*</span></label>
						<div class="controls">
							<input type="text" class="span6" id="lawCode" name="lawCode" maxlength="18" onkeyup="this.value=this.value.replace(/[^0-9a-z]/g,'')"/>
							<span class="help-block"></span>
						</div>
					</div>

					<div class="control-group formSep">
						<label class="control-label">地址<span style="color:red">*</span></label>
						<div class="controls">
							<input type="text" class="span6" id="address" name="address" maxlength="123" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
							<span class="help-block"></span>
						</div>
					</div>

					<div class="control-group formSep">
						<label class="control-label">联系人<span style="color:red">*</span></label>
						<div class="controls">
							<input type="text" class="span6" id="contacts" name="contacts" maxlength="64" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
							<span class="help-block"></span>
						</div>
					</div>

					<div class="control-group formSep">
						<label class="control-label">联系电话<span style="color:red">*</span></label>
						<div class="controls">
							<input type="text" class="span6" id="phoneNo" name="phoneNo" maxlength="11" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
							<span class="help-block"></span>
						</div>
					</div>

					<%--<div class="control-group formSep">
						<label class="control-label">平台标识<span style="color:red">*</span></label>
						<div class="controls">
							<select id="isPlatform" name="isPlatform" class="chzn_a span6">
								<option value="" >---请选择---</option>
								<option value="0" >否</option>
								<option value="1" >是</option>
							</select>
							<span class="help-block"></span>
						</div>
					</div>--%>

					<%--<div class="control-group formSep">
						<label class="control-label">交易开关<span style="color:red">*</span></label>
						<div class="controls">
							<select id="transFlag" name="transFlag" class="chzn_a span6">
								<option value="" >---请选择---</option>
								<option value="0" >开</option>
								<option value="1" >关</option>
							</select>
							<span class="help-block"></span>
						</div>
					</div>--%>

					<div class="control-group">
						<label class="control-label">备注</label>
						<div class="controls">
							<textarea  rows="6" class="span6" id="remarks" name="remarks" maxlength="123" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"></textarea>
							<span class="help-block"></span>
						</div>
					</div>

					<div class="control-group">
						<div class="controls">
							<sec:authorize access="hasRole('ROLE_COMPANY_ADDCOMMIT')">
								<button class="btn btn-primary btn-submit" type="button">保存</button>
							</sec:authorize>
							<button class="btn btn-inverse btn-reset" type="reset">重 置</button>
						</div>
					</div>
			</form>
		</div>
	</div>
</div>
</div>
</body>
</html>