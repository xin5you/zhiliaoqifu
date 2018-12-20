<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <link rel="stylesheet" href="${ctx}/static/datetimepicker/css/bootstrap-datetimepicker.0.0.11.min.css" />
    <script src="${ctx}/static/datetimepicker/js/bootstrap-datetimepicker.0.0.11.min.js"></script>
    <script src="${ctx}/static/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="${ctx}/static/oms/js/provider/providerInf/addProviderTransfer.js"></script>
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
			                    <li>供应商管理</li>
			                    <li><a href="${ctx }/provider/providerInf/listProviderInf.do">供应商信息管理</a></li>
                                <li><a href="${ctx }/provider/providerInf/listProviderInf.do">供应商信息列表</a></li>
                                <li>入账管理</li>
			                </ul>
			            </div>
			        </nav>
					<form id="pageMainForm" action="${ctx}/batch/openAccount/intoAddOpenAccount.do" class="form-inline form_validation_tip" method="post">
						<input type="hidden" id="operStatus"  value="${operStatus }"/>
                        <input type="hidden" id="providerId"  value="${providerId }"/>
						<h3 class="heading">入账信息列表</h3>
						
				         <div>
				         	<button class="btn btn-primary btn-addTransfer" type="button"> 添 加 </button>
				         </div>
				         <br/>
				         
				         <table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				             <thead>
				             <tr>
				               <th>序号</th>
				               <th>订单号</th>
				               <th>审核状态</th>
				               <th>打款金额</th>
                               <th>上账金额</th>
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
									<td>${entity.remit_amt}</td>
                                     <td>${entity.inaccount_amt}</td>
                                     <td>${entity.companyId}</td>
                                     <td>
                                         <c:if test="${entity.inaccountStat == '0'}">未上账</c:if>
                                         <c:if test="${entity.inaccountStat == '1'}">已上账</c:if>
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
                                        <c:if test="${entity.checkStat == '0'}">
                                            <a orderId="${entity.orderId}" title="编辑" class="btn-mini btn-edit" href="#"><i class="icon-edit"></i></a>
                                            <a orderId="${entity.orderId}" title="审核" class="btn-mini btn-check" href="#"><i class="icon-pencil"></i></a>
                                        </c:if>
                                        <c:if test="${entity.checkStat == '1'}">
                                            <a orderId="${entity.orderId}" title="提交" class="btn-mini btn-addTransferSubmit" href="#"><i class="icon-ok"></i></a>
                                        </c:if>
                                         <a orderId="${entity.orderId}" title="上账明细" class="btn-mini btn-view" href="#"><i class="icon-search"></i></a>
				                    </td>
				                 </tr>
				             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
                      
                      <br/>
                      <a href="${ctx }/batch/openAccount/listOpenAccount.do"><button class="btn btn-primary" type="button">返 回</button></a>
				      </form>
				      </div>
			   </div>

	    <div id="addTransferModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <form class="form-horizontal" enctype="multipart/form-data">
                <div class="modal-header">
                    <button class="close" data-dismiss="modal">&times;</button>
                    <h3 id="commodityInfModal_h">添加入账信息</h3>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="orderId" />
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label">打款金额：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="remitAmt" name="remitAmt"/>
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">请选择打款凭证：</label>
                            <div class="controls">
                                <input id="evidenceUrl" class="span3" readonly type="text">
                                <input type="file" class="span3" id="evidenceUrlFile"  name = "evidenceUrlFile"/>
                                <span class="help-block"></span>
                            </div>  
                        </div>
                        <div class="control-group">
                            <label class="control-label">企业识别码：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="companyCode" name ="companyCode" />
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">上账金额：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="inaccountAmt" name ="inaccountAmt" />
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">通用账户金额：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="A00" name ="A00" />
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">办公用品账户金额：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="B01" name ="B01" />
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">差旅专用账户金额：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="B02" name ="B02" />
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">体检专用账户金额：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="B03" name ="B03" />
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">培训专用账户金额：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="B04" name ="B04" />
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">食品专用账户金额：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="B05" name ="B05" />
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">通讯专用账户金额：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="B06" name ="B06" />
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">保险专用账户金额：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="B07" name ="B07" />
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">交通专用账户金额：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="B08" name ="B08" />
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

                <button class="btn btn-primary btn-submit">确 定  </button>

                <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
            </div>
        </div>
        <div id="imorptMsg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
              <div class="modal-header">
                    
                    <h3 id="commodityInfModal_h1">温馨提示</h3>
                </div>
                <br/><br/><br/>
              <h3 align="center">文件上传中......</h3>
        </div>
        
        <div id="msg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
              <div class="modal-header">
                    
                    <h3 id="commodityInfModal_h2">温馨提示</h3>
                </div>
                <br/><br/><br/>
              <h3 align="center">信息正在处理......</h3>
        </div>

       <div id="updateCheckStatModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
           <form class="form-horizontal">
               <div class="modal-header">
                   <button class="close" data-dismiss="modal">&times;</button>
                   <h3 id="commodityInfModal_h3">审核</h3>
               </div>
               <div class="modal-body">
                   <span>标记该条上账记录为已审核状态？</span>
               </div>
           </form>
           <div class="modal-footer" style="text-align: center;">
               <button class="btn btn-primary btn-checkStat-submit">确 定  </button>
               <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
           </div>
       </div>
</body>
</html>
