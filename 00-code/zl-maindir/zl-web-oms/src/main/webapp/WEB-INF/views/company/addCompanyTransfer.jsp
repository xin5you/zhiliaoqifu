<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <link rel="stylesheet" href="${ctx}/static/datetimepicker/css/bootstrap-datetimepicker.0.0.11.min.css" />
    <script src="${ctx}/static/datetimepicker/js/bootstrap-datetimepicker.0.0.11.min.js"></script>
    <script src="${ctx}/static/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="${ctx}/static/oms/js/company/addCompanyTransfer.js"></script>
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
			                    <li>企业管理</li>
                                <li><a href="${ctx }/company/listCompany.do">企业信息管理</a></li>
                                <li>企业信息列表</li>
                                <li>打款管理</li>
                                <li>打款信息列表</li>
			                </ul>
			            </div>
			        </nav>
					<form id="pageMainForm" action="${ctx }/company/intoAddCompanyTransfer.do?companyId=${company.companyId}" class="form-inline form_validation_tip" method="post">
						<h3 class="heading">打款信息列表</h3>
						
				         <table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				             <thead>
				             <tr>
				               <th>序号</th>
				               <th>订单号</th>
				               <th>审核状态</th>
				               <th>打款金额(元)</th>
                               <th>上账金额(元)</th>
                               <th>收款企业</th>
                                 <th>上账状态</th>
                                 <th>平台收款状态</th>
                                 <th>企业收款状态</th>
                                 <th>打款凭证</th>
				               <th>操作</th>
				             </tr>
				             </thead>
				             <tbody>
				             <c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
				                 <tr>
				                 	<td>${st.index+1 }</td>
				                 	<td>${entity.orderId}</td>
									<td>
                                        <c:if test="${entity.checkStat == '0'}">未审核</c:if>
                                        <c:if test="${entity.checkStat == '1'}">已审核</c:if>
                                    </td>
									<td>${entity.remitAmt}</td>
                                     <td>${entity.inaccountSumAmt}</td>
                                     <td>${entity.companyName}</td>
                                     <td>
                                         <c:if test="${entity.inaccountCheck == '0'}">未上账</c:if>
                                         <c:if test="${entity.inaccountCheck == '1'}">已上账</c:if>
                                     </td>
                                     <td>
                                         <c:if test="${entity.platformReceiverCheck == '0'}">未到账</c:if>
                                         <c:if test="${entity.platformReceiverCheck == '1'}">已到账</c:if>
                                     </td>
                                     <td>
                                         <c:if test="${entity.companyReceiverCheck == '0'}">未到账</c:if>
                                         <c:if test="${entity.companyReceiverCheck == '1'}">已到账</c:if>
                                     <td>
                                        <c:if test="${entity.evidenceUrl != null && entity.evidenceUrl != ''}">
                                            <div id="evidenceUrlDiv" evidenceImage="${entity.evidenceUrl}" style="height: 200px; width: 200px">
                                                <img style="height: 100%; width: 100%" src="data:image/jpg;base64,${entity.evidenceUrl}" />
                                            </div>
                                        </c:if>
                                     </td>
                                     <td>
                                         <c:if test="${company.isPlatform == '1'}">
                                             <sec:authorize access="hasRole('ROLE_COMPANY_IN_REMIT_DETAIL')">
                                                 <a orderId="${entity.orderId}" title="订单明细" class="btn-mini btn-view" href="#"><i class="icon-search"></i></a>
                                             </sec:authorize>
                                         </c:if>
                                         <c:if test="${entity.transferCheck == '1' && entity.platformReceiverCheck == '1' && entity.companyReceiverCheck == '0' && company.isPlatform == '1'}">
                                             <sec:authorize access="hasRole('ROLE_PLATFORM_IN_REMIT_INTO')">
                                                 <a orderId="${entity.orderId}" title="打款至企业" class="btn-mini btn-platform-ok" href="#"><i class="icon-pencil"></i></a>
                                             </sec:authorize>
                                         </c:if>
                                         <c:if test="${entity.transferCheck == '1' && entity.platformReceiverCheck == '1' && entity.companyReceiverCheck == '1' && company.isPlatform == '0'}">
                                             <sec:authorize access="hasRole('ROLE_COMPANY_IN_REMIT_DETAIL')">
                                                 <a orderId="${entity.orderId}" title="开票订单" class="btn-mini btn-invoice-order" href="#"><i class="icon-search"></i></a>
                                             </sec:authorize>
                                         </c:if>
                                        <%--<c:if test="${entity.transferCheck == '1' && entity.platformReceiverCheck == '0' && company.isPlatform == '1'}">
                                            <sec:authorize access="hasRole('ROLE_PLATFORM_IN_REMIT_INTO')">
                                                <a orderId="${entity.orderId}" title="平台收款" class="btn-mini btn-platform-ok" href="#"><i class="icon-ok"></i></a>
                                            </sec:authorize>
                                        </c:if>--%>
                                        <%--<c:if test="${entity.platformReceiverCheck == '1' && entity.companyReceiverCheck == '0' && company.isPlatform == '0'}">
                                            <sec:authorize access="hasRole('ROLE_COMPANY_IN_REMIT_INTO')">
                                                <a orderId="${entity.orderId}" title="企业收款" class="btn-mini btn-company-ok" href="#"><i class="icon-ok"></i></a>
                                            </sec:authorize>
                                        </c:if>--%>
				                    </td>
				                 </tr>
				             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
                      
                      <br/>
                      <a href="${ctx }/company/listCompany.do"><button class="btn btn-primary" type="button">返 回</button></a>
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

       <div id="addRemitModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
           <form class="form-horizontal">
               <input type="hidden" id="order_id" name="order_id"/>
               <input type="hidden" id="companyId" name="companyId"  value="${company.companyId }"/>
               <div class="modal-header">
                   <button class="close" data-dismiss="modal">&times;</button>
                   <h3 id="commodityInfModal_h1">打款</h3>
               </div>
               <div class="modal-body">
                   <span id="company_name">确认打款至企业？</span>
               </div>
           </form>
           <div class="modal-footer" style="text-align: center;">
            <sec:authorize access="hasRole('ROLE_COMPANY_IN_REMIT_COMMIT')">
               <button class="btn btn-primary btn-remit-submit">确 定  </button>
            </sec:authorize>
               <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
           </div>
       </div>

       <div id="imageModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
           <form class="form-horizontal">
               <div class="modal-header">
                   <button class="close" data-dismiss="modal">&times;</button>
                   <h3 id="commodityInfModal_h6">上账凭证图片</h3>
               </div>
               <div class="modal-body">
                   <div style="width: 100%; height: 100%; overflow-x: scroll">
                       <img id="bigImage" style="height: 500px; max-width:initial"/>
                   </div>
               </div>
           </form>
       </div>
</body>
</html>
