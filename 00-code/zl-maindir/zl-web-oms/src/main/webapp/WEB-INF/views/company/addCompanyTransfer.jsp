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
                                <li>转账管理</li>
			                </ul>
			            </div>
			        </nav>
					<form id="pageMainForm" action="${ctx }/company/intoAddCompanyTransfer.do?companyId=${company.companyId}" class="form-inline form_validation_tip" method="post">
						<h3 class="heading">转账信息列表</h3>
						
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
                                     <td>${entity.inaccountAmt}</td>
                                     <td>${entity.companyName}</td>
                                     <td>
                                         <c:if test="${entity.inaccountCheck == '0'}">未上账</c:if>
                                         <c:if test="${entity.inaccountCheck == '1'}">已上账</c:if>
                                     </td>
                                     <td>
                                         <c:if test="${entity.platformReceiverCheck == '0'}">未收款</c:if>
                                         <c:if test="${entity.platformReceiverCheck == '1'}">已收款</c:if>
                                     </td>
                                     <td>
                                         <c:if test="${entity.companyReceiverCheck == '0'}">未收款</c:if>
                                         <c:if test="${entity.companyReceiverCheck == '1'}">已收款</c:if>
                                     <td>
                                        <c:if test="${entity.evidenceUrl != null && entity.evidenceUrl != ''}">
                                            <a href="${entity.evidenceUrl}">${entity.evidenceUrl}</a>
                                        </c:if>
                                     </td>
				                    <td>
                                        <a orderId="${entity.orderId}" title="订单明细" class="btn-mini btn-view" href="#"><i class="icon-search"></i></a>
                                        <c:if test="${entity.transferCheck == '1' && entity.platformReceiverCheck == '0' && company.isPlatform == '1'}">
                                         <a orderId="${entity.orderId}" title="平台收款" class="btn-mini btn-platform-ok" href="#"><i class="icon-ok"></i></a>
                                        </c:if>
                                        <c:if test="${entity.platformReceiverCheck == '1' && entity.companyReceiverCheck == '0' && company.isPlatform == '0'}">
                                            <a orderId="${entity.orderId}" title="企业收款" class="btn-mini btn-company-ok" href="#"><i class="icon-ok"></i></a>
                                        </c:if>
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
                   <h3 id="commodityInfModal_h1">收款</h3>
               </div>
               <div class="modal-body">
                   <span id="company_name">是否确认收款？</span>
               </div>
           </form>
           <div class="modal-footer" style="text-align: center;">
               <button class="btn btn-primary btn-remit-submit">确 定  </button>
               <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
           </div>
       </div>
</body>
</html>
