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
	<%--<script src="${ctx}/static/oms/js/company/viewPlatformTransfer.js"></script>--%>
</head>
<body>
	   <%@ include file="/WEB-INF/views/common/navbar.jsp"%>
            <div id="contentwrapper">
                <div class="main_content">
                	<nav>
			            <div id="jCrumbs" class="breadCrumb module">
			                <ul>
								<li><a href="#"><i class="icon-home"></i></a></li>
								<li>企业管理</li>
								<li><a href="${ctx }/company/listCompany.do">企业信息管理</a></li>
								<li>企业信息列表</li>
								<li>上账管理</li>
								<li><a href="${ctx }/company/intoAddCompanyTransfer.do?companyId=${order.companyId}&orderType=200">上账信息列表</a></li>
								<li>上账明细列表</li>
			                </ul>
			            </div>
			        </nav>
					<form id="pageMainForm" class="form-inline form_validation_tip" method="post">
						<h3 class="heading">上账明细列表</h3>
						<input type="hidden" id="companyId" name="companyId" value="${companyId}"/>
						<input type="hidden" id="orderId" name="orderId" value="${order.orderId}"/>
						<div class="control-group formSep">
							<table cellpadding="5px" style="width: 100%">
								<tr>
									<td>
										<label class="control-label" style="font-weight: bold;">审核状态：</label>
										<label style="color: red;">${order.checkStatName }</label>
									</td>
									<td>
										<label class="control-label" style="font-weight: bold;">上账状态：</label>
										<label style="color: red;">${order.inaccountCheckName }</label>
									</td>
									<td>
										<label class="control-label" style="font-weight: bold;">分销商收款状态：</label>
										<label style="color: red;">${order.companyReceiverCheckName }</label>
									</td>
									<td>
										<label class="control-label" style="font-weight: bold;">上账总金额(元)：</label>
										<label style="color: red;">${order.inaccountSumAmt }</label>
									</td>
								</tr>
								<tr>
									<td>
										<label class="control-label" style="font-weight: bold;">收款分销商：</label>
										<label style="color: red;">${order.companyName }</label>
									</td>
									<td>
										<label class="control-label" style="font-weight: bold;">分销商收款总金额(元)：</label>
										<label style="color: red;">${order.companyInSumAmt }</label>
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
                                 <th>分销商收款金额(元)</th>
                                 <%--<th>开票状态</th>
                                 <th>开票信息</th>--%>
								 <%--<th>操作</th>--%>
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
                                     <td>${entity.companyInAmt}</td>
                                     <%--<td>
                                         <c:if test="${entity.isInvoice == '0'}">未开票</c:if>
                                         <c:if test="${entity.isInvoice == '1'}">已开票</c:if>
                                     </td>
                                     <td>${entity.invoiceInfo}</td>--%>
									 <%--<td>
										<c:if test="${order.companyReceiverCheck == '1' && company.isPlatform == '0' && entity.isInvoice == '0'}">
											<sec:authorize access="hasRole('ROLE_COMPANY_INVOICE_INTO')">
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
                      <a href="${ctx }/company/intoAddCompanyTransfer.do?companyId=${order.companyId}&orderType=200"><button class="btn btn-primary" type="button">返 回</button></a>
				      </form>
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
