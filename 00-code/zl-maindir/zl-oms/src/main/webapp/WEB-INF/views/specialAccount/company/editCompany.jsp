<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>

      <link rel="stylesheet" href="${ctx}/resource/chosen/chosen.css" />
      <script src="${ctx}/static/chosen/chosen.jquery.js"></script>
      <script src="${ctx}/static/oms/js//specialAccount/company/editCompany.js"></script>
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
			                    <li><a href="${ctx }/specialAccount/company/listCompany.do">企业管理</a></li>
			                    <li>修改企业</li>
			                </ul>
			            </div>
			        </nav>
					 <div class="row-fluid">
					 	<div class="span12">
							<form id="mainForm" class="form-horizontal form_validation_tip" method="post">
								 <h3 class="heading">修改企业</h3>
								 
								       <div class="control-group formSep">
							             <label class="control-label">企业名称<span style="color:red">*</span></label>
							             <input type="hidden" name="cId" id="cId" value="${companyInf.cId }">
							             <div class="controls">
							                 <input type="text" class="span6" id="name" name="name" value="${companyInf.name }"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<%-- <div class="control-group formSep">
							             <label class="control-label">企业类型<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="type" name="type" value="${companyInf.type }"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div> --%>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">企业代码<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="comCode" name="comCode" value="${companyInf.comCode }" readonly="readonly"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">统一社会信用代码<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="lawCode" name="lawCode" value="${companyInf.lawCode }"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">地址</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="address" name="address" value="${companyInf.address }"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">联系人</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="contacts" name="contacts" value="${companyInf.contacts }"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">联系电话</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="phoneNO" name="phoneNO" value="${companyInf.phoneNO }"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">交易开关</label>
							             <div class="controls">
							             <input type="radio" <c:if test="${companyInf.flag=='0' }">checked="checked"</c:if> name="flag" value="0">开
							             <input type="radio" <c:if test="${companyInf.flag=='1' }">checked="checked"</c:if> name="flag" value="1">关
							                 <span class="help-block"></span>
							             </div>
							             </div>

				                 	   <div class="control-group formSep">
							             <label class="control-label">专用账户类型</label>
							             <div class="controls">
							             <br>A类：<br>
												 <c:forEach var="b" items="${bList}" varStatus="st">
												 <label
												  <c:if test="${b.type=='B'}">
						                                     style="display: none;"
												  </c:if>
												   class="checkbox">
												            <input
												            <c:forEach var="cb" items="${cbList}">
												            <c:if test="${b.bId==cb.bId && b.type=='A'}">
						                                     checked="checked"
												            </c:if>
												            </c:forEach>
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
												            <c:forEach var="cb" items="${cbList}">
												            <c:if test="${b.bId==cb.bId && b.type=='B'}">
						                                     checked="checked"
												            </c:if>
												            </c:forEach>
												            type="checkbox" name="bId" value="${b.bId }"/>${b.name }
												 </label>
												 </c:forEach>
							             </div>
							     		</div>
							     		<div class="control-group">
							             <label class="control-label">备注</label>
							             <div class="controls">
							                  <textarea  rows="6" class="span6" id="remarks" name="remarks">${companyInf.remarks }</textarea>
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