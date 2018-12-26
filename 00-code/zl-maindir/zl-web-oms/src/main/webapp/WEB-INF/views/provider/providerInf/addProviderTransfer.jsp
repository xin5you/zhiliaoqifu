<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <link rel="stylesheet" href="${ctx}/static/datetimepicker/css/bootstrap-datetimepicker.0.0.11.min.css" />
    <script src="${ctx}/static/datetimepicker/js/bootstrap-datetimepicker.0.0.11.min.js"></script>
    <script src="${ctx}/static/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="${ctx}/static/oms/js/provider/providerInf/addProviderInfTransfer.js"></script>
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
                    <li>供应商信息列表</li>
                    <li>上账管理</li>
                    <li>上账信息列表</li>
                </ul>
            </div>
        </nav>
        <form id="pageMainForm" action="${ctx}/provider/providerInf/intoAddProviderTransfer.do" class="form-inline form_validation_tip" method="post">
            <h3 class="heading">上账信息列表</h3>

            <div>
                <sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER_ORDER_INTOADD')">
                    <button class="btn btn-primary btn-addTransfer" type="button"> 添 加 </button>
                </sec:authorize>
            </div>
            <br/>

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
                            <c:if test="${entity.platformReceiverCheck == '0'}">未到账</c:if>
                            <c:if test="${entity.platformReceiverCheck == '1'}">已到账</c:if>
                        </td>
                        <td>
                            <c:if test="${entity.companyReceiverCheck == '0'}">未到账</c:if>
                            <c:if test="${entity.companyReceiverCheck == '1'}">已到账</c:if>
                        <td>
                            <c:if test="${entity.evidenceUrl != null && entity.evidenceUrl != ''}">
                                <a href="${entity.evidenceUrl}">${entity.evidenceUrl}</a>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${entity.checkStat == '0'}">
                                <sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER_INTOEDIT')">
                                    <a orderId="${entity.orderId}" title="编辑" class="btn-mini btn-edit" href="#"><i class="icon-edit"></i></a>
                                </sec:authorize>
                                <sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER_INTODELETE')">
                                    <a orderId="${entity.orderId}" title="删除" class="btn-mini btn-delete" href="#"><i class="icon-remove"></i></a>
                                </sec:authorize>
                                <sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER_CHECK_INTO')">
                                    <a orderId="${entity.orderId}" title="审核" class="btn-mini btn-check" href="#"><i class="icon-pencil"></i></a>
                                </sec:authorize>
                            </c:if>
                            <c:if test="${entity.checkStat == '1'&& entity.inaccountCheck == '0'}">
                                <sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER_ADDCOMMIT')">
                                    <a orderId="${entity.orderId}" title="提交" class="btn-mini btn-addTransferSubmit" href="#"><i class="icon-ok"></i></a>
                                </sec:authorize>
                            </c:if>
                            <sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER_DETAIL')">
                                <a orderId="${entity.orderId}" title="上账明细" class="btn-mini btn-view" href="#"><i class="icon-search"></i></a>
                            </sec:authorize>
                            <c:if test="${entity.inaccountCheck == '1'&& entity.transferCheck == '0'}">
                                <sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER_INTO_COMPANY')">
                                    <a orderId="${entity.orderId}" title="打款至平台" class="btn-mini btn-remit" href="#"><i class="icon-pencil"></i></a>
                                </sec:authorize>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <%@ include file="/WEB-INF/views/common/pagination.jsp"%>

            <br/>
            <a href="${ctx }/provider/providerInf/listProviderInf.do"><button class="btn btn-primary" type="button">返 回</button></a>
        </form>
    </div>
</div>

<div id="addTransferModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <form id="addTransferFrom" class="form-horizontal" enctype="multipart/form-data" action="${ctx }/provider/providerInf/addProviderTransfer.do" method="post">
        <div class="modal-header">
            <button class="close" data-dismiss="modal">&times;</button>
            <h3 id="commodityInfModal_h">添加入账信息</h3>
        </div>
        <div class="modal-body">
            <input type="hidden" id="orderId" name="orderId"/>
            <input type="hidden" id="providerId" name="providerId"  value="${providerId }"/>
            <fieldset>
                <div class="control-group">
                    <label class="control-label">打款金额(元)：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="remitAmt" name="remitAmt" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">请选择打款凭证：</label>
                    <div class="controls">
                        <input id="evidenceUrl" name="evidenceUrl" class="span3" readonly type="text">
                        <input type="file" class="span3" id="evidenceUrlFile"  name="evidenceUrlFile" multiple/>
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
                    <label class="control-label">上账金额(元)：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="inaccountAmt" name ="inaccountAmt" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">通用账户金额(元)：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="A00" name ="A00" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">办公用品账户金额(元)：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="B01" name ="B01" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">差旅专用账户金额(元)：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="B02" name ="B02" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">体检专用账户金额(元)：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="B03" name ="B03" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">培训专用账户金额(元)：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="B04" name ="B04" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">食品专用账户金额(元)：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="B05" name ="B05" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">通讯专用账户金额(元)：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="B06" name ="B06" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">保险专用账户金额(元)：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="B07" name ="B07" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
                        <span class="help-block"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">交通专用账户金额(元)：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="B08" name ="B08" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
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
        <sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER_ORDER_ADDCOMMIT')">
            <button id="btn-submit" class="btn btn-primary btn-submit">确 定  </button>
        </sec:authorize>
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
            <input type="hidden" id="checkStatOrderId" name="checkStatOrderId"/>
            <h3 id="commodityInfModal_h3">审核</h3>
        </div>
        <div class="modal-body">
            <span>标记该条上账记录为已审核状态？</span>
        </div>
    </form>
    <div class="modal-footer" style="text-align: center;">
        <sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER_CHECK_COMMIT')">
            <button class="btn btn-primary btn-checkStat-submit">确 定  </button>
        </sec:authorize>
        <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
    </div>
</div>

<div id="addRemitModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <form class="form-horizontal">
        <input type="hidden" id="order_id" name="order_id"/>
        <input type="hidden" id="company_id" name="company_id"/>
        <div class="modal-header">
            <button class="close" data-dismiss="modal">&times;</button>
            <h3 id="commodityInfModal_h4">打款至平台</h3>
        </div>
        <div class="modal-body">
            <span id="company_name">确认打款至平台账户吗？</span>
        </div>
    </form>
    <div class="modal-footer" style="text-align: center;">
        <sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER_INTO_COMPANY_COMMIT')">
            <button class="btn btn-primary btn-remit-submit">确 定  </button>
        </sec:authorize>
        <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
    </div>
</div>

<div id="deleteTransferModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <form class="form-horizontal">
        <input type="hidden" id="transferOrderId" name="transferOrderId"/>
        <div class="modal-header">
            <button class="close" data-dismiss="modal">&times;</button>
            <h3 id="commodityInfModal_h5">删除上账记录</h3>
        </div>
        <div class="modal-body">
            <span>确认删除该条上账记录吗？</span>
        </div>
    </form>
    <div class="modal-footer" style="text-align: center;">
        <sec:authorize access="hasRole('ROLE_PROVIDER_TRANSFER_DELETE_COMMIT')">
            <button class="btn btn-primary btn-delete-submit">确 定  </button>
        </sec:authorize>
        <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
    </div>
</div>
</body>
</html>
