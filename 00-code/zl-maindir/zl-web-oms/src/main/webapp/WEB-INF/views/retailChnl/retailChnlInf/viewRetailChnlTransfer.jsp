<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <link rel="stylesheet" href="${ctx}/static/datetimepicker/css/bootstrap-datetimepicker.0.0.11.min.css" />
    <script src="${ctx}/static/datetimepicker/js/bootstrap-datetimepicker.0.0.11.min.js"></script>
    <script src="${ctx}/static/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
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
                                <li><a href="${ctx }/retailChnl/retailChnlInf/intoAddRetailChnlTransfer.do?channelId=${order.providerId }">上账信息列表</a></li>
                                <li>上账明细列表</li>
			                </ul>
			            </div>
			        </nav>
					<form id="pageMainForm" class="form-inline form_validation_tip" method="post">
						<h3 class="heading">上账明细列表</h3>

						<div class="control-group formSep">
							<table cellpadding="5px" style="width: 100%">
								<tr>
									<td>
										<label class="control-label" style="font-weight: bold;">审核状态：</label>
										<label style="color: red;">${order.checkStat }</label>
									</td>
									<td>
										<label class="control-label" style="font-weight: bold;">上账状态：</label>
										<label style="color: red;">${order.inaccountCheck }</label>
									</td>
									<%--<td>
										<label class="control-label" style="font-weight: bold;">平台收款状态：</label>
										<label style="color: red;">${order.platformReceiverCheck }</label>
									</td>--%>
									<td>
										<label class="control-label" style="font-weight: bold;">上账分销商：</label>
										<label style="color: red;">${order.companyName }</label>
									</td>
									<%--<td>
										<label class="control-label" style="font-weight: bold;">分销商收款状态：</label>
										<label style="color: red;">${order.companyReceiverCheckName }</label>
									</td>--%>
									<td>
										<label class="control-label" style="font-weight: bold;">打款总金额(元)：</label>
										<label style="color: red;">${order.remitAmt }</label>
									</td>
								</tr>
								<tr>
									<td>
										<label class="control-label" style="font-weight: bold;">上账总金额(元)：</label>
										<label style="color: red;">${order.inaccountSumAmt }</label>
									</td>
									<%--<td>
										<label class="control-label" style="font-weight: bold;">分销商收款总金额(元)：</label>
										<label style="color: red;">${order.companyInSumAmt }</label>
									</td>--%>
								</tr>
							</table>
						</div>
						<br/>
				         <table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				             <thead>
				             <tr>
				               <th>序号</th>
				               <th>订单明细号</th>
                                 <th>账户名称</th>
								 <th>交易金额(元)</th>
				               	<th>上账金额(元)</th>
                                 <%--<th>平台收款金额(元)</th>--%>
                                 <%--<th>分销商收款金额(元)</th>--%>
                                 <%--<th>开票状态</th>
                                 <th>开票信息</th>--%>
				             </tr>
				             </thead>
				             <tbody>
				             <c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
				                 <tr>
				                 	<td>${st.index+1 }</td>
				                 	<td>${entity.orderListId}</td>
									<td>${entity.BName}</td>
									 <td>${entity.transAmt}</td>
									<td>${entity.inaccountAmt}</td>
                                     <%--<td>${entity.platformInAmt}</td>--%>
                                     <%--<td>${entity.companyInAmt}</td>--%>
                                     <%--<td>
                                         <c:if test="${entity.isInvoice == '0'}">未开票</c:if>
                                         <c:if test="${entity.isInvoice == '1'}">已开票</c:if>
                                     </td>
                                     <td>${entity.invoiceInfo}</td>--%>
									 <%--<td>
										 <c:if test="${order.companyReceiverCheck == '1' && company.isPlatform == '0' && entity.isInvoice == '0'}">
											 <sec:authorize access="hasRole('ROLE_RETAIL_CHNL_INVOICE_INTOADD')">
											 	<a orderListId="${entity.orderListId}" title="开票" class="btn-mini btn-invoice" href="#"><i class="icon-pencil"></i></a>
										 	</sec:authorize>
										 </c:if>
									 </td>--%>
				                 </tr>
				             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
                      
                      <br/>
                      <a href="${ctx }/retailChnl/retailChnlInf/intoAddRetailChnlTransfer.do?channelId=${order.providerId }"><button class="btn btn-primary" type="button">返 回</button></a>
				      </form>
				      </div>

				<div id="msg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
					<div class="modal-header">

						<h3 id="commodityInfModal_h">温馨提示</h3>
					</div>
					<br/><br/><br/>
					<h3 align="center">信息正在处理......</h3>
				</div>

				<div id="addInvoiceModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<form class="form-horizontal">
						<input type="hidden" id="orderListId" name="orderListId"/>
						<div class="modal-header">
							<button class="close" data-dismiss="modal">&times;</button>
							<h3 id="commodityInfModal_h4">确认开票</h3>
						</div>
						<div class="modal-body">
							<fieldset>
								<div class="control-group">
									<label class="control-label">开票账户：</label>
									<div class="controls">
										<input type="text" class="span3" id="bName" name="bName" readonly="readonly"/>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">开票金额(元)：</label>
									<div class="controls">
										<input type="text" class="span3" id="companyInAmt" name="companyInAmt" readonly="readonly"/>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">开票附加信息：</label>
									<div class="controls">
										<input type="text" class="span3" id="invoiceInfo" name="invoiceInfo"/>
										<span class="help-block"></span>
									</div>
								</div>
							</fieldset>
						</div>
					</form>
					<div class="modal-footer" style="text-align: center;">
					<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_INVOICE_ADDCOMMIT')">
						<button class="btn btn-primary btn-invoice-submit">确 定  </button>
					</sec:authorize>
					<button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
					</div>
				</div>
			   </div>
</body>
</html>
