<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<%@ include file="/WEB-INF/views/common/init.jsp"%>
		<%@ include file="/WEB-INF/views/common/head.jsp"%>
		<script src="${ctx}/static/oms/js/baseDict/listBaseDict.js"></script>
	</head>
	<body>
		<%@ include file="/WEB-INF/views/common/navbar.jsp"%>
		<div id="contentwrapper">
			<div class="main_content">
				<nav>
		             <div id="jCrumbs" class="breadCrumb module">
			                <ul>
			                    <li><a href="#"><i class="icon-home"></i></a></li>
			                    <li>系统管理</li>
			                    <li><a href="${ctx }/billingType/listBillingType.do">字典管理</a></li>
			                    <li>字典列表</li>
			                </ul>
			         </div>
		        </nav>
				<form id="mainForm" action="${ctx }/baseDict/listBaseDict.do" class="form-inline" method="post">
					<h3 class="heading">字典列表</h3>
					<div class="row-fluid" id="h_search">
						<div class="span10">
							<div class="input-prepend">
								<span class="add-on">字典名称</span> 
								<input id="dictName" name="dictName" type="text" class="input-small" value="${baseDict.dictName }" />
							</div>
							<div class="input-prepend">
								<span class="add-on">字典名称</span> 
								<input id="dictName" name="dictName" type="text" class="input-small" value="${baseDict.dictName }" />
							</div>
						</div>
						<div class="pull-right">
							<button type="submit" class="btn btn-search">查 询</button>
							<button type="button" class="btn btn-inverse btn-reset">重 置</button>
						</div>
					</div>
					</br>
					<table class="table table-striped table-bordered dTableR table-hover" id="dt_gal">
						<thead>
							<tr>
								<th>字典名称</th>
								<th>字典代码</th>
								<th>字典值</th>
								<th>字典序号</th>
								<th>是否默认</th>
								<th>备注</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="b" items="${pageInfo.list}" varStatus="st">
								<tr>
									<td>${b.dictName}</td>
									<td>${b.dictCode}</td>
									<td>${b.dictValue}</td>
									<td>${b.seq}</td>
									<td>
										<c:if test="${b.isdefault == '0'}">是</c:if>
										<c:if test="${b.isdefault == '1'}">否</c:if>
									</td>
									<td>${b.remarks}</td>
									<td>
									<sec:authorize access="hasRole('ROLE_SYS_DICT_INTOEDIT')">
										<a dictId="${b.dictId}" title="编辑" href="#" class="btn-mini btn-edit"><i class="icon-edit"></i></a>
									</sec:authorize>
										<%-- <a dictId="${b.dictId}" title="删除" href="#" class="btn-mini btn-delete"><i class="icon-remove"></i></a>  --%>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<%@ include file="/WEB-INF/views/common/pagination.jsp"%>
				</form>
			</div>
		</div>
	</body>
</html>
