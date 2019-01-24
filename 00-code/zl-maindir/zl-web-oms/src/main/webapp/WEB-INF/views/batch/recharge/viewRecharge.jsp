<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <link rel="stylesheet" href="${ctx}/static/datetimepicker/css/bootstrap-datetimepicker.0.0.11.min.css" />
    <script src="${ctx}/static/datetimepicker/js/bootstrap-datetimepicker.0.0.11.min.js"></script>
    <script src="${ctx}/static/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="${ctx}/static/oms/js/batch/recharge/viewRecharge.js"></script>
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
			                    <li><a href="${ctx }/batch/recharge/listRecharge.do">企业员工批量充值</a></li>
			                     <li>充值订单详情</li>
			                </ul>
			            </div>
			        </nav>
                    <input type="hidden" id="operStatus"  value="${operStatus }"/>
					<form id="searchForm" action="${ctx }/batch/recharge/intoViewRecharge.do" class="form-inline" method="post">
						<h3 class="heading">充值订单详情</h3>
						
						<div class="row-fluid" >
							 <div class="span12">
		                       	<div style="display: flex;justify-content:left;">
                                    <table cellpadding="5px" style="width: 80%">
                                         <tr>
                                             <td>
                                                 <span class="fontBold">订单号:</span>
                                                 <span class="fontColor">${order.orderId }</span>
                                             </td>
                                             <td>
                                                 <span class="fontBold">公司名称:</span>
                                                 <span class="fontColor">${order.companyName }</span>
                                             </td>
                                             <td>
                                                <span class="fontBold">订单名称:</span>
                                                 <span class="fontColor">${order.orderName }</span>
                                             </td>
                                             <td>
                                                 <span class="fontBold">订单状态:</span>
                                                 <span class="fontColor">${order.orderStat}</span>
                                             </td>
                                         </tr>
                                         <tr>
                                            <td>
                                                 <span class="fontBold">充值总金额:</span>
                                                 <span class="fontColor">${order.sumAmount }</span>
                                             </td>
                                             <td>
                                                 <span class="fontBold">订单总量:</span>
                                                 <span class="fontColor">${order.orderCount }</span>
                                             </td>
                                             <td>
                                                 <span class="fontBold">未 处 理:</span>
                                                 <span class="fontColor">${order.disposeWait }</span>
                                             </td>
                                             <td>
                                                 <span class="fontBold">处理成功:</span>
                                                 <span class="fontColor">${order.disposeSuccess }</span>
                                             </td>
                                             <td>
                                                 <span class="fontBold">处理失败:</span>
                                                 <span class="fontColor">${order.disposeFail }</span>
                                             </td>
                                         </tr>
                                        <tr>
                                            <td>
                                                <span class="fontBold">订单状态:</span>
                                                <input type="hidden" id="orderId" name="orderId" value="${order.orderId}"/>
                                                <select id="orderStatus" name="orderStatus">
                                                    <option value="">--请选择--</option>
                                                    <c:forEach var="mapStat" items="${mapOrderStat}" varStatus="sta">
                                                        <option value="${mapStat.code}"  <c:if test="${mapStat.code==order.orderStatus}">selected</c:if>   >${mapStat.stat }</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td>
                                                <button type="submit" class="btn btn-search"> 查 询 </button>
                                                <button type="reset" class="btn btn-inverse btn-reset">重 置</button>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
						  </div>
						</div>
				         </br >       
				         <table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				             <thead>
				             <tr>
				               <th>序号</th>
				               <th>订单号</th>
				               <th>姓名</th>
				               <th>身份证号码</th>
				               <th>手机号</th>
				               <th>充值金额(元)</th>
				               <th>充值账户</th>
				               <th>状态</th>
				               <th>备注</th>
				             </tr>
				             </thead>
				             <tbody>
				             <c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
                                 <tr>
                                    <td>${st.index+1 }</td>
                                    <td>${entity.orderListId}</td>
                                    <td>${entity.userName}</td>
                                    <td>${entity.userCardNo}</td>
                                    <td>${entity.phoneNo}</td>
                                    <td>${entity.amount}</td>
                                    <td>${entity.bizTypeName}</td>
                                    <td>${entity.orderStat}</td>
                                    <td>${entity.remarks}</td>
                                 </tr>
                             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
				      </form>
				      <br/>
                       <a href="${ctx }/batch/recharge/listRecharge.do"><button class="btn btn-primary" type="button">返回</button></a>
			   </div>
	    </div>
</body>
</html>
