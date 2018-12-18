<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>

      <link rel="stylesheet" href="${ctx}/resource/chosen/chosen.css" />
      <script src="${ctx}/static/chosen/chosen.jquery.js"></script>
      <script src="${ctx}/static/oms/js/baseDict/editBaseDict.js"></script>
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
			                    <li><a href="${ctx }/baseDict/listBaseDict.do">字典管理</a></li>
			                    <li>编辑字典信息</li>
			                </ul>
			            </div>
			        </nav>
					 <div class="row-fluid">
					 	<div class="span12">
							<form id="mainForm" class="form-horizontal form_validation_tip" method="post">
								 <h3 class="heading">编辑字典信息</h3>
								  <input type="hidden" name="dictId" id="dictId" value="${baseDict.dictId }">
								       <div class="control-group formSep">
							             <label class="control-label">字典名称<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="dictName" name="dictName" value="${baseDict.dictName }"/>
							                 <span class="help-block"></span>
							             </div>
							             </div>
							     		<div class="control-group formSep">
							             <label class="control-label">字典代码<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="dictCode" name="dictCode" value="${baseDict.dictCode }"/>
							                 <span class="help-block"></span>
							             </div>
							             </div>
							             <div class="control-group formSep">
							             <label class="control-label">字典值<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="dictValue" name="dictValue" value="${baseDict.dictValue }"/>
							                 <span class="help-block"></span>
							             </div>
							             </div>
							             <div class="control-group formSep">
							             	<label class="control-label">是否默认</label>
							             <div class="controls">
							             	<select id="isdefault" name="isdefault" class="chzn_a span6">
												<option value="0" <c:if test="${baseDict.isdefault == '0'}">selected="selected"</c:if>>是</option>
												<option value="1" <c:if test="${baseDict.isdefault == '1'}">selected="selected"</c:if>>否</option>
							                 </select>
							                 <span class="help-block"></span>
							             </div>
							             </div>
							     		<div class="control-group">
							             <label class="control-label">备注</label>
							             <div class="controls">
							                  <textarea  rows="6" class="span6" id="remarks" name="remarks">${baseDict.remarks }</textarea>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
								        <div class="control-group">
					                            <div class="controls">
					                            <sec:authorize access="hasRole('ROLE_SYS_DICT_EDITCOMMIT')">
					                                <button class="btn btn-primary btn-submit" type="button">保存</button>
					                            </sec:authorize>
					                                <button class="btn btn-inverse btn-reset" type="reset">重 置</button>
					                            </div>
					                  	</div>
					                 </div>
						     </form>
					     </div>
				     </div>
				</div>
</body>

</html>