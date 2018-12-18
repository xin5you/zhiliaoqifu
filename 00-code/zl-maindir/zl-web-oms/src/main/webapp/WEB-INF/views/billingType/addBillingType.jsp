<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>

      <link rel="stylesheet" href="${ctx}/resource/chosen/chosen.css" />
      <script src="${ctx}/static/chosen/chosen.jquery.js"></script>
      <script src="${ctx}/static/oms/js/billingType/addBillingType.js"></script>
    
      <link rel="stylesheet" href="${ctx}/static/css/select2.css" />
      <script src="${ctx}/static/select2.js"></script>
      
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
			                    <li><a href="${ctx }/billingType/listBillingType.do">账户类型管理</a></li>
			                    <li>新增账户类型</li>
			                </ul>
			            </div>
			        </nav>
					 <div class="row-fluid">
					 	<div class="span12">
							<form id="mainForm1" class="form-horizontal" method="post">
								 <h3 class="heading">新增账户类型</h3>
								 
								       <div class="control-group formSep">
							             <label class="control-label">账户类型名称<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="bName" name="bName" maxlength="20"/>
							                 <span class="help-block"></span>
							             </div>
							             </div>
							     		<div class="control-group formSep">
							             <label class="control-label">代码</label>
							             <div class="controls">
							             	<select id="code" name="code" class="chzn_a span6">
								                 <c:forEach var="c" items="${billingTypeCodeList}" varStatus="st">
													<option value="${c.bId}" >${c.name }</option>
												 </c:forEach>
							                 </select>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		<div class="control-group formSep">
							             <label class="control-label">折损率<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="loseFee" name="loseFee" maxlength="6"/>
							                 <span class="help-block"></span>
							             </div>
							             </div>
							             <div class="control-group formSep">
							             <label class="control-label">可购率<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="buyFee" name="buyFee" maxlength="6"/>
							                 <span class="help-block"></span>
							             </div>
							             </div>
							     		<div class="control-group">
							             <label class="control-label">备注</label>
							             <div class="controls">
							                  <textarea  rows="6" class="span6" id="remarks" name="remarks" maxlength="255"></textarea>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
								        <div class="control-group">
					                            <div class="controls">
					                                <button class="btn btn-primary btn-submit" type="button">保存</button>
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