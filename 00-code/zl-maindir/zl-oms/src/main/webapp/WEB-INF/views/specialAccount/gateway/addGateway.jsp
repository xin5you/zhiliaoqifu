<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>

      <link rel="stylesheet" href="${ctx}/resource/chosen/chosen.css" />
      <script src="${ctx}/static/chosen/chosen.jquery.js"></script>
      <script src="${ctx}/static/oms/js/specialAccount/gateway/addGateway.js"></script>
</head>

<body>
	   <%@ include file="/WEB-INF/views/common/navbar.jsp"%>
          <!-- main content -->
            <div id="contentwrapper">
                <div class="main_content">
                	<nav>
			            <div id="jCrumbs" class="breadCrumb module">
			                <ul>
			                    <li><a href="#"><i class="icon-home"></i></a></li>
			                    <li>专用账户管理</li>
			                    <li><a href="${ctx }/specialAccount/gateway/listGateway.do">网关管理</a></li>
			                    <li>网关企业</li>
			                </ul>
			            </div>
			        </nav>
					 <div class="row-fluid">
					 	<div class="span12">
							<form id="mainForm" class="form-horizontal" method="post">
								 <h3 class="heading">新增网关</h3>
								 
								       <div class="control-group formSep">
							             <label class="control-label">网关名称<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="name" name="name" />
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">网关代码<span style="color:red">*</span></label>
							             <div class="controls">
							                  <select id="code" name="code" class="chzn_a span6">
							                      <option value="">---请选择代码---</option>
							                      <c:forEach var="g" items="${gatewayCodes}" varStatus="st">
												  <option value="${g.getCode()}">${g.getCode() }(${g.getValue() })</option>
											      </c:forEach>
							                  </select>
							                <!--  <input type="text" class="span6" id="code" name="code" /> -->
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">描述</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="desc" name="desc" />
							                 <span class="help-block"></span>
							             </div>
							     		</div>

				                 	  <div class="control-group formSep">
							             <label class="control-label">专用账户类型</label>
							             <div class="controls"">
							             <br>A类：<br>
												 <c:forEach var="b" items="${bList}" varStatus="st">
												 <label
												  <c:if test="${b.type=='B'}">
						                                     style="display: none;"
												  </c:if>
												   class="checkbox">
												            <input
												            <c:if test="${b.type=='B'}">
						                                     disabled="disabled"
												            </c:if>
												            <c:if test="${b.type=='A'}">
						                                     checked="checked"
												            </c:if>
												            type="checkbox" name="bId" value="${b.bId }"/>${b.name }
												 </label>
												 </c:forEach>
										 B类：<br>
												 <c:forEach var="b" items="${bList}" varStatus="st">
												 <label
												  <c:if test="${b.type=='A'}">
						                                     style="display: none;"
												  </c:if>
												  class="checkbox">
												            <input
												            <c:if test="${b.type=='A'}">
						                                     disabled="disabled"
												            </c:if>
												            <c:if test="${b.type=='B'}">
						                                     checked="checked"
												            </c:if>
												            type="checkbox" name="bId" value="${b.bId }"/>${b.name }
												 </label>
												 </c:forEach>
							             </div>
							     		</div>
							     		<div class="control-group">
							             <label class="control-label">备注</label>
							             <div class="controls">
							                  <textarea  rows="6" class="span6" id="remarks" name="remarks"></textarea>
							                 <span class="help-block"></span>
							             </div>
							     	</div>
							     		
								        <div class="control-group">
					                            <div class="controls">
					                                <button class="btn btn-primary btn-submit" type="button">保存</button>
					                                <button class="btn btn-inverse btn-reset" type="reset">重 置</button>
					                            </div>
					                  	</div>
					                 </div>
						     </form>
					     </div>
				     </div>
				</div>
		</div>
</body>

</html>