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
			<%--<input id="channelId" name="channelId" type="hidden" value="${channelId }" />
			<input id="transStat" name="transStat" type="hidden" value="1" />--%>

			<br/>
			<table class="table table-striped table-bordered dTableR table-hover">
				<thead>
					<tr>
						<th colspan="2">代金券剩余总值：${totalAmount}万元</th>
					</tr>
					<tr>
						<th>专项类型</th>
						<th>代金券总值</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="product" items="${couponProductList}" varStatus="st">
					<tr>
						<td>${product.BName}</td>
						<td>${product.tagAmount}元</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>

			<br/>
			<div class="row-fluid" id="h_search">
				<div class="span12">
					<div class="input-prepend">
						<span class="add-on">订单号</span>
						<input id="orderId" name="orderId" type="text" class="input-medium" value="" />
					</div>
					<div class="input-prepend">
						<span class="add-on">卡券类型:</span>
						<select name="bId" id="bId" class="input-medium">
							<option value="">--请选择--</option>
							<c:forEach var="b" items="${billingTypeList}" varStatus="bStat">
								<option value="${b.bId}" <c:if test="${b.bId==couponOrder.couponBid}">selected</c:if>   >${b.name }</option>
							</c:forEach>
						</select>
					</div>

					<div class="pull-right">
						<button type="submit" class="btn btn-search">查 询</button>
						<button type="reset" class="btn btn-inverse btn-reset">重 置</button>
						<button type="button" class="btn btn-primary btn-buy">购买代金券</button>
					</div>
				</div>
			</div>

			<br/>
			<table class="table table-striped table-bordered dTableR table-hover" id="dt_gal">
				<thead>
				<tr>
					<th>订单号</th>
					<th>卡券类型</th>
					<th>面额总值</th>
					<th>卡券面额(张数)</th>
					<th>托管支出</th>
					<th>交易状态</th>
					<th>创建时间</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="entity" items="${couponOrderPageInfo.list}" varStatus="st">
					<tr>
						<td>${entity.id}</td>
						<td>${entity.couponBid}</td>
						<td>${entity.couponAmt}元</td>
						<td>${entity.totalNum}</td>
						<td>${entity.totalAmt}元</td>
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
			<%@ include file="/WEB-INF/views/common/pagination.jsp"%>
		</form>
	</div>
</div>

<div id="buyCouponModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<form id="buyCouponFrom" class="form-horizontal" enctype="multipart/form-data" action="${ctx }/retailChnl/retailChnlInf/addRetailChnlTransfer.do" method="post">
		<div class="modal-header">
			<button class="close" data-dismiss="modal">&times;</button>
			<h3 id="commodityInfModal_h">购买代金券</h3>
		</div>
		<div class="modal-body">
			<fieldset>
				<table>
					<thead>
						<tr style="height: 50px">
							<td>
								选择卡券类型：
								<select name="couponType" id="couponType" class="span2">
									<option value="">---请选择---</option>
									<c:forEach var="b" items="${billingTypeList}" varStatus="bStatus">
										<option value="${b.bId}" <c:if test="${b.bId==couponType}">selected</c:if>>${b.name}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td>填写需要购买的张数</td>
						</tr>
					</thead>
				</table>
				<br/>
				<table class="table table-striped table-bordered dTableR table-hover">
					<thead>
						<tr>
							<th>代金券面额</th>
							<th>代金券面额</th>
							<th>购买张数(张)</th>
							<th>需支付金额</th>
						</tr>
					</thead>
					<tbody id="couponTbody">
						<%--<tr>
							<td>
								<c:if test="${entity.iconImage != null && entity.iconImage != ''}">
									<img th:src="${entity.iconImage}" style="height: 50px; width: 50px;"/>
								</c:if>
							</td>
							<td>￥100</td>
							<td>
								<input type="text" value="10" class="span2"/>
								库存剩余500张
							</td>
							<td>￥1000(￥100*10张)</td>
						</tr>
						<tr>
							<th>合计</th>
							<th colspan="2">20张</th>
							<th>￥3000</th>
						</tr>--%>
					</tbody>
				</table>
			</fieldset>
		</div>
	</form>
	<div class="modal-footer" style="text-align: center;">
		<%--<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_TRANSFER_ADD')">--%>
			<button id="btn-submit" class="btn btn-primary btn-submit">确 定  </button>
		<%--</sec:authorize>--%>
		<button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
	</div>
</div>
<style type="text/css">
	#buyCouponModal {
		width: 900px;
		margin: -250px 0 0 -450px;
	}
</style>
</body>
</html>
