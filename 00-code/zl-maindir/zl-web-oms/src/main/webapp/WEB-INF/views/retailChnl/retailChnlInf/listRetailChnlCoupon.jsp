<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/common/init.jsp"%>
	<%@ include file="/WEB-INF/views/common/head.jsp"%>
	<script src="${ctx}/static/oms/js/retailChnl/retailChnlInf/listRetailChnlCoupon.js"></script>
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
					<li>卡券购买列表</li>
				</ul>
			</div>
		</nav>
		<form id="searchForm" action="#" class="form-inline" method="post">
			<h3 class="heading">卡券购买列表</h3>
			<input id="channelId" name="channelId" type="hidden" value="${channelId }" />
			<input id="transStat" name="transStat" type="hidden" value="1" />

			<div class="row-fluid" id="h_search">
				<div class="span12">
					<div class="input-prepend">
						<span class="add-on">卡券名称</span>
						<input id="couponName" name="couponName" type="text" class="input-medium" value="${coupon.couponName }" />
					</div>
					<div class="input-prepend">
						<span class="add-on">卡券类型:</span>
						<select name="bId" id="bId" class="input-medium">
							<option value="">--请选择--</option>
							<c:forEach var="b" items="${billingTypeList}" varStatus="bStat">
								<option value="${b.bId}"  <c:if test="${b.bId==coupon.BId}">selected</c:if>   >${b.name }</option>
							</c:forEach>
						</select>
					</div>
					<%--<div class="input-prepend">
						<span class="add-on">交易状态:</span>
						<select name="transStat" id="transStat" class="input-medium">
							<option value="">--请选择--</option>
							<c:forEach var="t" items="${transStatList}" varStatus="sStat">
								<option value="${t.code}"  <c:if test="${t.code==coupon.transStat}">selected</c:if>   >${t.name }</option>
							</c:forEach>
						</select>
					</div>--%>
					<div class="pull-right">
						<button type="submit" class="btn btn-search">查 询</button>
						<button type="reset" class="btn btn-inverse btn-reset">重 置</button>
					</div>
				</div>
			</div>

			</br >
			<table class="table table-striped table-bordered dTableR table-hover" id="dt_gal">
				<thead>
				<tr>
					<th><input type="checkbox" id="selectAll" title="全选" class="checkbox" /></th>
					<th>卡券名称</th>
					<th>卡券类型</th>
					<th>价格(元)</th>
					<th>交易状态</th>
					<th>创建时间</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
					<tr>
						<td><input type="checkbox" name="couponId" value="${entity.couponId}" class="checkbox couponId"/></td>
						<td>${entity.couponName}</td>
						<td>${entity.BId}</td>
						<td>${entity.price / 100}</td>
						<td>${entity.transStat}</td>
						<td>
							<jsp:useBean id="createTime" class="java.util.Date"/>
							<jsp:setProperty name="createTime" property="time" value="${entity.createTime}"/>
							<fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<div style="text-align: center;">
				<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_BUY_COUPON_COMMIT')">
					<button type="button" class="btn btn-primary btn-submit">提 交</button>
				</sec:authorize>
					<button type="button" class="btn btn-back">返回</button>
			</div>
			<%@ include file="/WEB-INF/views/common/pagination.jsp"%>
			<%--<a href="${ctx }/retailChnl/retailChnlInf/listRetailChnlInf.do"><button class="btn btn-primary" type="button">返 回</button></a>--%>
		</form>
	</div>
</div>

</body>
</html>
