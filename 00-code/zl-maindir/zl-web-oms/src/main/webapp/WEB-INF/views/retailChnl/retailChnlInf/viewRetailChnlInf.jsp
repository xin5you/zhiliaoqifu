<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>

      <link href="${ctx }/static/bootstrap-fileinput/css/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
      <link rel="stylesheet" href="${ctx}/static/chosen/chosen.css" />
      
      
      <script src="${ctx}/static/jQueryDistpicker/js/distpicker.data.js"></script>
	  <script src="${ctx}//static/jQueryDistpicker/js/distpicker.js"></script>
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
			                    <li>分销商管理</li>
			                    <li><a href="${ctx }/retailChnl/retailChnlInf/listRetailChnlInf.do">分销商信息管理</a></li>
			                     <li>分销商信息详情</li>
			                </ul>
			            </div>
			        </nav>
					 <div class="row-fluid">
					 	<div class="span12">
							<form id="mainForm" action="" class="form-horizontal form_validation_tip" method="post" enctype="multipart/form-data">
								 <h3 class="heading">分销商信息详情</h3>
							     		<div class="control-group">
							             <label class="control-label">分销商名称</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="channelName" name="channelName" value="${retailChnlInf.channelName }" readonly="readonly"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group">
							             <label class="control-label">分销商编号</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="channelCode" name="channelCode" value="${retailChnlInf.channelCode }" readonly="readonly"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group">
							             <label class="control-label">分销商KEY</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="channelKey" name="channelKey" value="${retailChnlInf.channelKey }" readonly="readonly"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<%-- <div class="control-group">
							             <label class="control-label">分销商备付金额(元)</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="channelReserveAmt" name="channelReserveAmt" value="${retailChnlInf.channelReserveAmt }" readonly="readonly"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div> --%>
							     		
							     		<%-- <div class="control-group">
							             <label class="control-label">分销商预警金额(元)</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="channelPrewarningAmt" name="channelPrewarningAmt" value="${retailChnlInf.channelPrewarningAmt }" readonly="readonly"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div> --%>
							     		
							     		
							     		<div class="control-group">
							             <label class="control-label">管理员手机号</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="phoneNo" name="phoneNo" value="${retailChnlInf.phoneNo }" readonly="readonly"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group">
							             <label class="control-label">邮箱</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="email" name="email" value="${retailChnlInf.email }" readonly="readonly"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
                                        <div class="control-group">
                                             <label class="control-label">是否开户</label>
                                             <div class="controls">
                                                  <input type="text" class="span6" id="email" name="email" value="<c:if test="${retailChnlInf.isOpen == '0' }">未开户</c:if><c:if test="${retailChnlInf.isOpen == '1' }">已开户</c:if>" readonly="readonly"/>
                                             </div>
                                        </div>
                                        
                                        <div class="control-group">
                                             <label class="control-label">备注</label>
                                             <div class="controls">
                                                  <textarea  rows="4" class="span6" name="remarks"  id="remarks" readonly="readonly">${retailChnlInf.remarks }</textarea>
                                             </div>
                                        </div>
							     		
						     </form>
					     </div>
				     </div>
				</div>
		</div>
</body>
<script>
</script>
</html>