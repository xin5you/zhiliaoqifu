<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <script src="${ctx}/static/oms/js/retailChnl/retailChnlInf/listRetailChnlInf.js"></script>
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
			                </ul>
			            </div>
			        </nav>
					<form id="searchForm" action="${ctx }/retailChnl/retailChnlInf/listRetailChnlInf.do" class="form-inline" method="post">
						<input type="hidden" id="operStatus"  value="${operStatus }"/>
						<h3 class="heading">分销商信息列表</h3>
						<div class="row-fluid" id="h_search">
							 <div class="span10">
		                       	<div class="input-prepend">
		           			   	   	<span class="add-on">分销商名称</span>
									<input id="channelName" name="channelName" type="text" class="input-medium" value="${retailChnlInf.channelName }" />
		                       	</div>
							</div>
							<div class="pull-right">
								
								<button type="submit" class="btn btn-search"> 查 询 </button>
								<button type="reset" class="btn btn-inverse btn-reset">重 置</button>
								<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_INTOADD')">
								<button type="button" class="btn btn-primary btn-add">新增分销商</button>
								</sec:authorize>
							</div>
						</div>
				         </br >       
				         <table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				             <thead>
				             <tr>
				             	<th>分销商ID</th>
				                <th>分销商名称</th>
				               	<th>分销商编号</th>
								 <th>分销商代码</th>
				               	<!-- <th>分销商备付金额(元)</th>
				                <th>分销商预警金额(元)</th> -->
				                <th>管理员手机号</th>
				                <th>邮箱</th>
				                <th>是否开户</th>
				               	<th>操作</th>
				             </tr>
				             </thead>
				             <tbody>
				             <c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
				                 <tr>
				                 	<td>${entity.channelId}</td>
				                 	<td>${entity.channelName}</td>
				                 	<td>${entity.channelCode}</td>
									 <td>${entity.lawCode}</td>
									<%-- <td>${entity.channelReserveAmt}</td>
				                    <td>${entity.channelPrewarningAmt}</td> --%>
				                    <td>${entity.phoneNo}</td>
				                    <td>${entity.email}</td>
				                    <td>
				                    	<c:if test="${entity.isOpen == '0'}">未开户</c:if>
				                    	<c:if test="${entity.isOpen == '1'}">已开户</c:if>
				                    </td>
				                    <td>
									<%-- <a channelId="${entity.channelId}" title="添加产品折扣率" class="btn-mini btn-edit a" href="#"><i class="icon-pencil"></i></a> 
									<a channelId="${entity.channelId}" title="追加备付金" class="btn-grant-role"  href="#"><i class="icon-plus"></i></a>  --%>
									<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_INTOEDIT')">
										<a channelId="${entity.channelId}" title="编辑" class="btn-mini btn-edit" href="#"><i class="icon-edit"></i></a>
									</sec:authorize>
									<c:if test="${entity.isOpen == '0'}">
										<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_DELETE')">
											<a channelId="${entity.channelId}" title="删除" class="btn-mini btn-delete" href="#"><i class="icon-remove"></i></a>
										</sec:authorize>
										<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_OPENACCOUNT')">
											<a channelId="${entity.channelId}" title="开户" class="btn-mini btn-openAccount" href="#"><i class="icon-pencil"></i></a>
										</sec:authorize>
									</c:if>
									<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_VIEW')">
										<a channelId="${entity.channelId}" title="详情" class="btn-mini btn-view" href="#"><i class="icon-search"></i></a>
									</sec:authorize>
									<c:if test="${entity.isOpen == '1'}">
										<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_ACCBAL')">
											<a channelId="${entity.channelId}" title="账户余额" class="btn-mini btn-accbal" href="#"><i class="icon-search"></i></a>
										</sec:authorize>
										<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_TRANSFER_INTOADD')">
											<a channelId="${entity.channelId}" title="收款订单" class="btn-mini btn-transfer" href="#"><i class="icon-pencil"></i></a>
										</sec:authorize>
									</c:if>
				                    </td>
				                 </tr>
				             </c:forEach>
				             </tbody>
				         </table>
				         <%@ include file="/WEB-INF/views/common/pagination.jsp"%>
				      </form>
			   </div>
	    </div>
	    
	    <div id="addOpenAccountModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<form class="form-horizontal">
		    <div class="modal-header">
		        <button class="close" data-dismiss="modal">&times;</button>
		        <h3 id="commodityInfModal_h">开户</h3>
		    </div>
		    <div class="modal-body">
		        <input type="hidden" id="channelId" name="channelId"/>
				<input type="hidden" id="companyId" name="companyId"/>
				<input type="hidden" id="orderName" name="orderName"/>
		        <span>你确定对该分销商开户吗？</span>
		    </div>
		</form>
		<div class="modal-footer" style="text-align: center;">
		    <button class="btn btn-primary btn-openAccount-submit">确 定  </button>
		    <button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
		</div>
	</div>
	
	    <div id="msg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
              <div class="modal-header">
                    
                    <h3 id="commodityInfModal_h2">温馨提示</h3>
                </div>
                <br/><br/><br/>
              <h3 align="center">信息正在处理......</h3>
        </div>
</body>
</html>
