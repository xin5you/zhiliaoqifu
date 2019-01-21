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
							             <label class="control-label">账户类型代码<span style="color:red">*</span></label>
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
							                 <input type="text" class="span6" id="loseFee" name="loseFee" maxlength="10"onkeyup="checkPrice(this)" />
							                 <span class="help-block"></span>
							             </div>
							             </div>
							             <div class="control-group formSep">
							             <label class="control-label">可购率<span style="color:red">*</span></label>
							             <div class="controls">
							                 <input type="text" class="span6" id="buyFee" name="buyFee" maxlength="10" onkeyup="checkPrice(this)"/>
							                 <span class="help-block"></span>
							             </div>
							             </div>
							     		<div class="control-group">
							             <label class="control-label">备注</label>
							             <div class="controls">
							                  <textarea  rows="6" class="span6" id="remarks" name="remarks" maxlength="125"></textarea>
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
<script type="text/javascript">
    //验证价格（带有小数点，小数点最多是三位）
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