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
	<script src="${ctx}/static/oms/js/company/viewCompanyTransfer.js"></script>
</head>
<body>
	   <%@ include file="/WEB-INF/views/common/navbar.jsp"%>
            <div id="contentwrapper">
                <div class="main_content">
                	<nav>
			            <div id="jCrumbs" class="breadCrumb module">
			                <ul>
								<li><a href="#"><i class="icon-home"></i></a></li>
								<li>
									<c:if test="${company.isPlatform=='0'}">企业管理</c:if>
									<c:if test="${company.isPlatform=='1'}">平台管理</c:if>
								</li>
								<li>
									<c:if test="${company.isPlatform=='0'}">
										<a href="${ctx }/company/listCompany.do?isPlatform=${company.isPlatform}">企业信息管理</a>
									</c:if>
									<c:if test="${company.isPlatform=='1'}">
										<a href="${ctx }/company/listCompany.do?isPlatform=${company.isPlatform}">平台信息管理</a>
									</c:if>
								</li>
								<li>
									<c:if test="${company.isPlatform=='0'}">企业信息列表</c:if>
									<c:if test="${company.isPlatform=='1'}">平台信息列表</c:if>
								</li>
								<li>打款管理</li>
								<li><a href="${ctx }/company/intoAddCompanyTransfer.do?companyId=${company.companyId}&orderType=300">打款信息列表</a></li>
								<li>打款明细列表</li>
			                </ul>
			            </div>
			        </nav>
					<form id="pageMainForm" class="form-inline form_validation_tip" method="post">
						<h3 class="heading">打款明细列表</h3>
						<input type="hidden" id="companyId" name="companyId" value="${company.companyId}"/>
						<input type="hidden" id="orderId" name="orderId" value="${order.orderId}"/>
						<input type="hidden" id="isPlatform" name="isPlatform" value="${company.isPlatform}"/>
						<div class="control-group formSep">
							<table cellpadding="5px" style="width: 100%">
								<tr>
									<td>
										<label class="control-label" style="font-weight: bold;">审核状态：</label>
										<label style="color: red;">${order.checkStatName }</label>
									</td>
									<td>
										<label class="control-label" style="font-weight: bold;">打款总金额(元)：</label>
										<label style="color: red;">${order.remitAmt }</label>
									</td>
									<td>
										<label class="control-label" style="font-weight: bold;">上账总金额(元)：</label>
										<label style="color: red;">${order.inaccountSumAmt }</label>
									</td>
									<td>
										<label class="control-label" style="font-weight: bold;">平台收款总金额(元)：</label>
										<label style="color: red;">${order.platformInSumAmt }</label>
									</td>
								</tr>
								<tr>
									<td>
										<label class="control-label" style="font-weight: bold;">企业收款总金额(元)：</label>
										<label style="color: red;">${order.companyInSumAmt }</label>
									</td>
									<td>
										<label class="control-label" style="font-weight: bold;">收款企业：</label>
										<label style="color: red;">${order.companyName }</label>
									</td>
									<td>
										<label class="control-label" style="font-weight: bold;">上账状态：</label>
										<label style="color: red;">${order.inaccountCheckName }</label>
									</td>
									<td>
										<label class="control-label" style="font-weight: bold;">平台收款状态：</label>
										<label style="color: red;">${order.platformReceiverCheckName }</label>
									</td>
								</tr>
								<tr>
									<td>
										<label class="control-label" style="font-weight: bold;">企业收款状态：</label>
										<label style="color: red;">${order.companyReceiverCheckName }</label>
									</td>
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
								 <th>上帐金额(元)</th>
                                 <th>平台收入金额(元)</th>
                                 <th>企业收入金额(元)</th>
                                 <th>开票状态</th>
                                 <th>开票信息</th>
								 <th>操作</th>
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
                                     <td>${entity.platformInAmt}</td>
                                     <td>${entity.companyInAmt}</td>
                                     <td>
                                         <c:if test="${entity.isInvoice == '0'}">未开票</c:if>
                                         <c:if test="${entity.isInvoice == '1'}">已开票</c:if>
                                     </td>
                                     <td>${entity.invoiceInfo}</td>
									 <td>
										<c:if test="${order.transferCheck == '1' && order.platformReceiverCheck == '1' && order.companyReceiverCheck == '0' && company.isPlatform == '1'}">
											 <sec:authorize access="hasRole('ROLE_PLATFORM_EDIT_INAMT')">
												 <a orderListId="${entity.orderListId}" title="编辑企业收款金额" class="btn-mini btn-edit-inAmt" href="#"><i class="icon-edit"></i></a>
											 </sec:authorize>
										 </c:if>
										<c:if test="${order.companyReceiverCheck == '1' && company.isPlatform == '0' && entity.isInvoice == '0'}">
											<sec:authorize access="hasRole('ROLE_COMPANY_INVOICE_INTO')">
												<a orderListId="${entity.orderListId}" title="开票" class="btn-mini btn-invoice" href="#"><i class="icon-pencil"></i></a>
											</sec:authorize>
										</c:if>
									 </td>
				                 </tr>
				             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
                      
                      <br/>
                      <a href="${ctx }/company/intoAddCompanyTransfer.do?companyId=${company.companyId}&orderType=300"><button class="btn btn-primary" type="button">返 回</button></a>
				      </form>
				      </div>
			   </div>

	   <div id="msg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
		   <div class="modal-header">

			   <h3 id="commodityInfModal_h">温馨提示</h3>
		   </div>
		   <br/><br/><br/>
		   <h3 align="center">信息正在处理......</h3>
	   </div>

	   <div id="editInAmtModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		   <form class="form-horizontal">
			   <div class="modal-header">
				   <button class="close" data-dismiss="modal">&times;</button>
				   <h3 id="commodityInfModal_h5">编辑企业收款金额</h3>
			   </div>
			   <div class="modal-body">
				   <fieldset>
					   <div class="control-group">
						   <label class="control-label">订单明细号：</label>
						   <div class="controls">
							   <input type="text" class="span3" id="orderListId_" name="orderListId_" readonly="readonly"/>
							   <span class="help-block"></span>
						   </div>
					   </div>
					   <div class="control-group">
						   <label class="control-label">账户名称：</label>
						   <div class="controls">
							   <input type="text" class="span3" id="bName_" name="bName_" readonly="readonly"/>
							   <span class="help-block"></span>
						   </div>
					   </div>
					   <div class="control-group">
						   <label class="control-label">交易金额(元)：</label>
						   <div class="controls">
							   <input type="text" class="span3" id="transAmt_" name="transAmt_" readonly="readonly"/>
							   <span class="help-block"></span>
						   </div>
					   </div>
					   <div class="control-group">
						   <label class="control-label">上帐金额(元)：</label>
						   <div class="controls">
							   <input type="text" class="span3" id="inaccountAmt_" name="inaccountAmt_" readonly="readonly"/>
							   <span class="help-block"></span>
						   </div>
					   </div>
					   <div class="control-group">
						   <label class="control-label">平台收入金额(元)：</label>
						   <div class="controls">
							   <input type="text" class="span3" id="platformInAmt_" name="platformInAmt_" readonly="readonly"/>
							   <span class="help-block"></span>
						   </div>
					   </div>
					   <div class="control-group">
						   <label class="control-label">企业收入金额(元)：</label>
						   <div class="controls">
							   <input type="text" class="span3" id="companyInAmt_" name="companyInAmt_" onkeyup="checkPrice(this)"/>
							   <span class="help-block"></span>
						   </div>
					   </div>
					   <div class="control-group">
						   <label class="control-label">开票状态：</label>
						   <div class="controls">
							   <input type="text" class="span3" id="isInvoice_" name="isInvoice_" readonly="readonly"/>
							   <span class="help-block"></span>
						   </div>
					   </div>
					   <div class="control-group">
						   <label class="control-label">开票附加信息：</label>
						   <div class="controls">
							   <input type="text" class="span3" id="invoiceInfo_" name="invoiceInfo_" readonly="readonly"/>
							   <span class="help-block"></span>
						   </div>
					   </div>
				   </fieldset>
			   </div>
		   </form>
		   <div class="modal-footer" style="text-align: center;">
			   <sec:authorize access="hasRole('ROLE_PLATFORM_EDIT_INAMT_COMMIT')">
				   <button class="btn btn-primary btn-edit-inAmt-submit">确 定  </button>
			   </sec:authorize>
			   <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
		   </div>
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
			<sec:authorize access="hasRole('ROLE_COMPANY_INVOICE_COMMIT')">
			   <button class="btn btn-primary btn-invoice-submit">确 定  </button>
			</sec:authorize>
			   <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
		   </div>
	   </div>
</body>
<script type="text/javascript">
    //验证价格（带有小数点，小数点最多是两位）
    function checkPrice(obj){
        obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字而不是
        obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符
        obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d\d).*$/,'$2$1.$2');//只能输入两个小数
        if(obj.value.indexOf(".")< 0 && obj.value !=""){//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
            obj.value= parseFloat(obj.value);
        }
    }
</script>
</html>
