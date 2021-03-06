<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/init.jsp"%>
    <%@ include file="/WEB-INF/views/common/head.jsp"%>
    <script src="${ctx}/static/oms/js/retailChnl/retailChnlInf/editRetailChnlInf.js"></script>
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
			                     <li>编辑分销商信息</li>
			                </ul>
			            </div>
			        </nav>
					 <div class="row-fluid">
					 	<div class="span12">
							<form id="mainForm" action="${ctx}/retailChnl/retailChnlInf/editRetailChnlInfCommit.do" class="form-horizontal form_validation_tip" method="post" enctype="multipart/form-data">
								 <h3 class="heading">编辑分销商信息</h3>
							     		<input type="hidden" id="channelId" name="channelId" value="${retailChnlInf.channelId }" />
							     		<input type="hidden" id="lockVersion" name="lockVersion" value="${retailChnlInf.lockVersion }" />
							     		<div class="control-group">
							             <label class="control-label">分销商名称<span style="color:red">*</span></label>
							             <div class="controls">
											 <input type="text" class="span6" id="channelName" name="channelName" value="${retailChnlInf.channelName }" maxlength="32" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" <c:if test="${'1'==retailChnlInf.isOpen }">readonly="readonly"</c:if>/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group">
							             <label class="control-label">分销商编号<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="channelCode" name="channelCode" value="${retailChnlInf.channelCode }" maxlength="18" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')" <c:if test="${'1'==retailChnlInf.isOpen }">readonly="readonly"</c:if>/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>

										<div class="control-group">
											<label class="control-label">分销商代码<span style="color:red">*</span></label>
											<div class="controls">
												<input type="text" class="span6" id="lawCode" name="lawCode" value="${retailChnlInf.lawCode }" maxlength="18" <c:if test="${'1'==retailChnlInf.isOpen }">readonly="readonly"</c:if>/>
												<span class="help-block"></span>
											</div>
										</div>
							     		
							     		<div class="control-group">
							             <label class="control-label">分销商KEY<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="channelKey" name="channelKey" value="${retailChnlInf.channelKey }" maxlength="32" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<%-- <div class="control-group">
							             <label class="control-label">分销商备付金额(元)</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="channelReserveAmt" name="channelReserveAmt" onkeyup="checkPrice(this)" value="${retailChnlInf.channelReserveAmt }" maxlength="12"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div> --%>
							     		
							     		<%-- <div class="control-group">
							             <label class="control-label">分销商预警金额(元)</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="channelPrewarningAmt" name="channelPrewarningAmt" onkeyup="checkPrice(this)" value="${retailChnlInf.channelPrewarningAmt }" maxlength="12"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div> --%>
							     		
							     		
							     		<div class="control-group">
							             <label class="control-label">管理员手机号<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="phoneNo" name="phoneNo" value="${retailChnlInf.phoneNo }" onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="11"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>
							     		
							     		<div class="control-group">
							             <label class="control-label">邮箱</label>
							             <div class="controls">
							                 <input type="text" class="span6" id="email" name="email" value="${retailChnlInf.email }" maxlength="32" onkeyup="this.value=this.value.replace(/^ +| +$/g,'')"/>
							                 <span class="help-block"></span>
							             </div>
							     		</div>

                                        <div class="control-group">
                                             <label class="control-label">备注</label>
                                             <div class="controls">
                                                  <textarea  rows="4" class="span6" name="remarks" maxlength="125" id="remarks">${retailChnlInf.remarks }</textarea>
                                             </div>
                                        </div>
							     		
							       <div class="control-group ">
				                            <div class="controls">
				                            <sec:authorize access="hasRole('ROLE_RETAIL_CHNL_EDITCOMMIT')">
				                                <button class="btn btn-primary" type="button" id="addSubmitBtn" >保存</button>
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
<script type="text/javascript">
//验证价格
function checkPrice(obj){
      obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字而不是
	  obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符   
	  obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的   
	  obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");  
	  obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d\d).*$/,'$1$2.$3');//只能输入三个小数   
	  if(obj.value.indexOf(".")< 0 && obj.value !=""){//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额  
	   obj.value= parseFloat(obj.value);  
	  }  
}
</script>
</html>