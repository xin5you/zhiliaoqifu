<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>

      <link rel="stylesheet" href="${ctx}/resource/chosen/chosen.css" />
      <script src="${ctx}/static/chosen/chosen.jquery.js"></script>
      <script src="${ctx}/static/oms/js/company/addCompany.js"></script>
    
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
			                    <li><a href="${ctx }/company/listCompany.do">企业管理</a></li>
			                    <li>新增企业</li>
			                </ul>
			            </div>
			        </nav>
					 <div class="row-fluid">
					 	<div class="span12">
							<form id="mainForm1" class="form-horizontal" method="post">
								 <h3 class="heading">新增企业</h3>
								 
								       <div class="control-group formSep">
							             <label class="control-label">企业名称<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="name" name="name" />
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		<div class="control-group formSep">
							             <label class="control-label">统一社会信用代码<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="lawCode" name="lawCode" />
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">地址</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="address" name="address" />
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">联系人</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="contacts" name="contacts" />
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">联系电话</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="phoneNo" name="phoneNo" />
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group formSep">
							             <label class="control-label">交易开关</label>
							             <div class="controls">
							             	<select id="transFlag" name="transFlag" class="chzn_a span6">
							             		<option value="" >---请选择---</option>
												<option value="0" >开</option>
												<option value="1" >关</option>
							                 </select>
							                 <span class="help-block"></span>
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