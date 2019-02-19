<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
</head>
<body>
	   <%@ include file="/WEB-INF/views/common/navbar.jsp"%>
          <-- main content -->
            <div id="contentwrapper">
                <div class="main_content">
                <nav>
			            <div id="jCrumbs" class="breadCrumb module">
			                <ul>
			                    <li><a href="#"><i class="icon-home"></i></a></li>
			                    <li>供应商管理</li>
			                    <li><a href="${ctx }/provider/providerInf/listProviderInf.do">供应商信息管理</a></li>
			                    <li>供应商信息详情</li>
			                </ul>
			            </div>
			        </nav>
					 <div class="row-fluid">
					 	<div class="span12">
							<h3 class="heading">供应商信息详情</h3>
							<form id="mainForm" action="#" class="form-horizontal form_validation_tip">
					     		<div class="control-group formSep">
					             	<label class="control-label">供应商名称</label>
						             <div class="controls">
											<input type="text" class="span6" id="providerName" name="providerName" maxlength="32" value="${providerInf.providerName}" readonly="readonly"/>
						                 	<span class="help-block"></span>
						             </div>
					     		</div>
								<div class="control-group formSep">
									<label class="control-label">是否开户</label>
									<div class="controls">
										<input type="text" class="span6" id="isOpen" name="isOpen" maxlength="32" value="${providerInf.isOpen}" readonly="readonly"/>
										<span class="help-block"></span>
									</div>
								</div>
								<div class="control-group formSep">
									<label class="control-label">供应商代码</label>
									<div class="controls">
										<input type="text" class="span6" id="lawCode" name="lawCode" maxlength="32" value="${providerInf.lawCode}" readonly="readonly"/>
										<span class="help-block"></span>
									</div>
								</div>
					     		<div class="control-group formSep">
						             <label class="control-label">app_url</label>
						             <div class="controls">
											<input type="text" class="span6" name="appUrl" id="appUrl" maxlength="128" value="${providerInf.appUrl}" readonly="readonly"/>
						                 	<span class="help-block"></span>
						             </div>
					     		</div>
					     		<div class="control-group formSep">
						             <label class="control-label">app_Secret</label>
						             <div class="controls">
											<input type="text" class="span6" name="appSecret" id="appSecret" maxlength="64" value="${providerInf.appSecret}" readonly="readonly"/>
						                 	<span class="help-block"></span>
						             </div>
					     		</div>
					     		<div class="control-group formSep">
						             <label class="control-label">access_token</label>
						             <div class="controls">
											<input type="text" class="span6" name="accessToken" id="accessToken" maxlength="64" value="${providerInf.accessToken}" readonly="readonly"/>
						                 	<span class="help-block"></span>
						             </div>
					     		</div>
					     		<div class="control-group formSep">
						             <label class="control-label">默认路由标识</label>
						             <div class="controls">
						               		<select name="defaultRoute" id="defaultRoute" class="span6" readonly="readonly" disabled="disabled">
											 <c:forEach var="drList" items="${defaultRouteList}" varStatus="st">
											 		<option value="${drList.code}" <c:if test="${drList.code == providerInf.defaultRoute }">selected="selected""</c:if> >${drList.value}</option>
											 </c:forEach>
			                                </select>
						             </div>
					     		</div>
					     		<div class="control-group formSep">
	                                  <label class="control-label">供应商折扣</label>
	                                  <div class="controls">
	                                      <input type="text" class="span6" id="providerRate" name="providerRate" maxlength="8" value="${providerInf.providerRate}" readonly="readonly"/>
	                                      <span class="help-block"></span>
	                                  </div>
                                 </div>
								<%-- <div class="control-group formSep">
	                                  <label class="control-label">专项类型</label>
	                                  <div class="controls">
	                                      <input type="text" class="span6" id="bType" name="bType" maxlength="8" value="${providerInf.bName}" readonly="readonly"/>
	                                      <span class="help-block"></span>
	                                  </div>
                                 </div> --%>
                                <%--<div class="control-group formSep">
                                    <label class="control-label">操作顺序</label>
                                    <div class="controls">
                                          <input type="text" class="span6" id="operSolr" name="operSolr" value="${providerInf.operSolr}" onkeyup="this.value=this.value.replace(/\D/g,'')" readonly="readonly"/>
                                          <span class="help-block"></span>
                                    </div>
                                </div>--%>
                                
                                <div class="control-group">
                                     <label class="control-label">备注</label>
                                     <div class="controls">
                                          <textarea  rows="4" class="span6" name="remarks" readonly="readonly" id = "remarks" maxlength="256">${providerInf.remarks}</textarea>
                                     </div>
                                </div>
                            </form>	
					     </div>
				     </div>
				</div>
		</div>
</body>
</html>