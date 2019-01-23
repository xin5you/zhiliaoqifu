<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
     <script src="${ctx}/static/oms/js/sys/organization/addOrganization.js"></script>
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
			                    <li>系统管理</li>
			                    <li><a href="${ctx }/sys/organization/listOrganization.do">部门管理</a></li>
			                     <li>新增部门</li>
			                </ul>
			            </div>
			        </nav>
					 <div class="row-fluid">
					 	<div class="span12">
							<form id="mainForm" class="form-horizontal form_validation_tip" method="post">
								 <h3 class="heading">新增部门</h3>
						         <div class="control-group">
						             <label class="control-label">部门名称<span style="color:red">*</span></label>
						             <div class="controls">
						                 <input type="text" class="span6" id="name" name="name" maxlength="125"/>
						                 <span class="help-block"></span>
						             </div>
						         </div>
						         <div class="control-group">
						             <label class="control-label">部门编码</label>
						             <div class="controls">
						                 <input type="text" class="span6" id="code" name="code" maxlength="125"/>
						                 <span class="help-block"></span>
						             </div>
						         </div>
						         <div class="control-group">
						             <label class="control-label">排序<span style="color:red">*</span></label>
						             <div class="controls">
						                 <input type="text" class="span6" id="seq" name="seq" maxlength="5" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
						                 <span class="help-block"></span>
						             </div>
						         </div>
						         
						         
						         <div class="control-group">
						             <label class="control-label">上级类型</label>
						             <div class="controls">
						                  <select class="form-control span6" id="pid" name="pid">
						                 	 <option value="0" selected="selected">请选择所属部门</option>
											 <c:forEach var="dl" items="${entityList}" varStatus="st">
											 		<option value="${dl.id}">${dl.name }</option>
											 </c:forEach>
										</select>
						                 <span class="help-block"></span>
						             </div>
						         </div>
						       
						         <div class="control-group ">
				                            <div class="controls">
				                             <sec:authorize access="hasRole('ROLE_SYS_ORGAN_ADDCOMMIT')">
				                                <button class="btn btn-primary btn-submit" type="submit">提 交</button>
				                                </sec:authorize>
				                                <button class="btn btn-inverse btn-reset" type="reset">重 置</button>
				                            </div>
				                  </div>
						     </form>
					     </div>
				     </div>
				</div>
		</div>
</body>
</html>