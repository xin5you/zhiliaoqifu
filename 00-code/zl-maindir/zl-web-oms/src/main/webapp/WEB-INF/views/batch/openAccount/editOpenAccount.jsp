<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <link rel="stylesheet" href="${ctx}/static/datetimepicker/css/bootstrap-datetimepicker.0.0.11.min.css" />
    <script src="${ctx}/static/datetimepicker/js/bootstrap-datetimepicker.0.0.11.min.js"></script>
    <script src="${ctx}/static/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="${ctx}/static/oms/js/batch/openAccount/editOpenAccount.js"></script>
</head>
<body>
	   <%@ include file="/WEB-INF/views/common/navbar.jsp"%>
            <div id="contentwrapper">
                <div class="main_content">
                	<nav>
			            <div id="jCrumbs" class="breadCrumb module">
			                <ul>
			                    <li><a href="#"><i class="icon-home"></i></a></li>
			                    <li>账户管理</li>
			                    <li><a href="${ctx }/batch/openAccount/listOpenAccount.do">企业员工批量开户</a></li>
			                     <li>订单编辑</li>
			                </ul>
			            </div>
			        </nav>
					<form id="mainForm" action="${ctx }/batch/openAccount/intoEditOpenAccount.do?orderId=${order.orderId}" class="form-inline" method="post">
						<input type="hidden" id="operStatus"  value="${operStatus }"/>
						<input type="hidden" id="companyId" name="companyId"  value="${order.companyId }"/>
						<input type="hidden" id="accountType" name="accountType"  value="${accountType }"/>
						<h3 class="heading">订单编辑</h3>
						<div>
						<sec:authorize access="hasRole('ROLE_BATCH_OPEN_ACCOUNT_ORDERLISTADD')">
						<button class="btn btn-primary btn-editAddAccount" type="button"> 添 加 </button>
						</sec:authorize>
						</div><br/>
						<div class="row-fluid" >
							 <div class="span12">
							 <div class="control-group formSep">
		                       	<div style="display: flex;justify-content:left;">
									<table cellpadding="5px" style="width: 80%">
										<tr>
											<td>
												<span class="fontBold">订单号:</span>
												<span class="fontColor">${order.orderId }</span>
											</td>
											<td>
												<span class="fontBold">订单名称:</span>
												<span class="fontColor">${order.orderName }</span>
											</td>
											<td>
												<span class="fontBold">订单总量:</span>
												<span class="fontColor">${order.orderCount }</span>
											</td>
										</tr>
										<tr>
											<td>
												<span class="fontBold">账户类型:</span>
												<span class="fontColor">${accountTypeName }</span>
											</td>
											<td>
												<span class="fontBold">所属企业:</span>
												<span class="fontColor">${order.companyName }</span>
											</td>
											<%-- <td>
												<span class="fontBold">开户类型:</span>
												<span class="fontColor">
													<select multiple name="billingTypes" id="billingTypes" class="form-control span8">
														<c:forEach var="bList" items="${billingTypeList}" varStatus="st">
															<optgroup label="${bList }"></optgroup>
															<option value="${bList.bId }">${bList.name }</option>
														</c:forEach>
													</select>
												</span>
											</td> --%>
										</tr>
									</table>
								</div>
							</div>
						  </div>
						</div>
				         </br >       
				         <table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				             <thead>
				             <tr>
				               <th>序号</th>
				               <th>姓名</th>
				               <th>身份证号码</th>
				               <th>手机号</th>
				               <th>操作</th>
				             </tr>
				             </thead>
				             <tbody>
				             <c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
				                 <tr>
				                 	<td>${st.index +1}</td>
                                    <td>${entity.userName}</td>
                                    <td>${entity.userCardNo}</td>
                                    <td>${entity.phoneNo}</td>
                                    <td>${entity.bizTypeName}</td>
				                    <td>
                                   <sec:authorize access="hasRole('ROLE_BATCH_OPEN_ACCOUNT_ORDERLISTDELETE')">
                                    	<a orderListId="${entity.orderListId }" title="删除" class="btn-mini btn-delete" href="#"><i class="icon-remove"></i></a>
                                   </sec:authorize>
				                    </td>
				                 </tr>
				             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
				      </form>
				      <br/>
                      <a href="${ctx }/batch/openAccount/listOpenAccount.do"><button class="btn btn-primary" type="button">返回</button></a>
			   </div>
	    </div>
	    
	    <div id="editAddAccountModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <form class="form-horizontal">
                <div class="modal-header">
                    <button class="close" data-dismiss="modal">&times;</button>
                    <h3 id="commodityInfModal_h">添加名单</h3>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="orderId" value="${order.orderId }" />
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label">姓名：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="name" name ="name"/>
                                <span class="help-block"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">手机号码：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="phone"  name="phone"/>
                                <span class="help-block"></span>
                            </div>  
                        </div>
                        <div class="control-group">
                            <label class="control-label">身份证号码：</label>
                            <div class="controls">
                                <input type="text" class="span3" id="card"  name="card"/>
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </fieldset>
                </div>
            </form>
            <div class="modal-footer" style="text-align: center;">
                <sec:authorize access="hasRole('ROLE_BATCH_OPEN_ACCOUNT_ORDERLISTADDCOMMIT')">
                	<button class="btn btn-primary btn-submit">确 定  </button>
                </sec:authorize>
                <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
            </div>
        </div>   
	    
	    
</body>
</html>
