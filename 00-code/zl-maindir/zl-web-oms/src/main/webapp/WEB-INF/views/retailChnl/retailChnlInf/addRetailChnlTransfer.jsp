<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/common/init.jsp"%>
	<%@ include file="/WEB-INF/views/common/head.jsp"%>
	<link rel="stylesheet" href="${ctx}/static/datetimepicker/css/bootstrap-datetimepicker.0.0.11.min.css" />
	<script src="${ctx}/static/datetimepicker/js/bootstrap-datetimepicker.0.0.11.min.js"></script>
	<script src="${ctx}/static/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
	<script src="${ctx}/static/oms/js/retailChnl/retailChnlInf/addRetailChnlTransfer.js"></script>
	<script src="${ctx}/static/jquery/jquery.form.js"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/common/navbar.jsp"%>
<div id="contentwrapper">
	<div class="main_content">
		<nav>
			<div id="jCrumbs" class="breadCrumb module">
				<ul>
					<li><a href="#"><i class="icon-home"></i></a></li>
					<li>分销商管理</li>
					<li><a href="${ctx }/retailChnl/retailChnlInf/listRetailChnlInf.do">分销商信息管理</a></li>
					<li>分销商信息列表</li>
					<li>上账管理</li>
					<li>上账信息列表</li>
				</ul>
			</div>
		</nav>
		<form id="pageMainForm" action="${ctx}/retailChnl/retailChnlInf/intoAddRetailChnlTransfer.do?channelId=${channelId }&orderType=${orderType }" class="form-inline form_validation_tip" method="post">
			<h3 class="heading">上账信息列表</h3>
			<div>
				<button class="btn btn-primary btn-addTransfer" type="button"> 添 加 </button>
			</div>
			<br/>
			<table class="table table-striped table-bordered dTableR table-hover" id="dt_gal" >
				<thead>
				<tr>
					<th>序号</th>
					<th>订单号</th>
					<th>审核状态</th>
					<th>打款金额(元)</th>
					<th>上账金额(元)</th>
					<th>上账分销商</th>
					<th>上账状态</th>
					<%--<th>平台收款状态</th>--%>
                    <%--<th>分销商收款状态</th>--%>
					<th>打款凭证</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="entity" items="${pageInfo.list}" varStatus="st">
					<tr>
						<td>${st.index+1 }</td>
						<td>${entity.orderId}</td>
						<td>
							<c:if test="${entity.checkStat == '0'}">未审核</c:if>
							<c:if test="${entity.checkStat == '1'}">已审核</c:if>
						</td>
						<td>${entity.remitAmt}</td>
						<td>${entity.inaccountSumAmt}</td>
						<td>${entity.companyName}</td>
						<td>
							<c:if test="${entity.inaccountCheck == '0'}">未上账</c:if>
							<c:if test="${entity.inaccountCheck == '1'}">已上账</c:if>
						</td>
						<%--<td>
							<c:if test="${entity.platformReceiverCheck == '0'}">未收款</c:if>
							<c:if test="${entity.platformReceiverCheck == '1'}">已收款</c:if>
						</td>--%>
						<%--<td>
							<c:if test="${entity.companyReceiverCheck == '0'}">未收款</c:if>
							<c:if test="${entity.companyReceiverCheck == '1'}">已收款</c:if>
						</td>--%>
						<td>
							<c:if test="${entity.evidenceUrl != null && entity.evidenceUrl != ''}">
								<div class="evidenceUrlDiv" evidenceImage="${entity.evidenceUrl}" style="height: 200px; width: 200px; margin-left: auto; margin-right: auto;">
									<img style="height: 100%; width: 100%" src="data:image/jpg;base64,${entity.evidenceUrl}" />
								</div>
							</c:if>
						</td>
						<td>
							<c:if test="${entity.checkStat == '0'}">
								<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_TRANSFER_INTOEDIT')">
									<a orderId="${entity.orderId}" title="编辑" class="btn-mini btn-edit" href="#"><i class="icon-edit"></i></a>
								</sec:authorize>
								<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_TRANSFER_INTODELETE')">
									<a orderId="${entity.orderId}" title="删除" class="btn-mini btn-delete" href="#"><i class="icon-remove"></i></a>
								</sec:authorize>
								<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_TRANSFER_INTOCHECK')">
									<a orderId="${entity.orderId}" title="审核" class="btn-mini btn-check" href="#"><i class="icon-pencil"></i></a>
								</sec:authorize>
							</c:if>
							<c:if test="${entity.checkStat == '1'&& entity.inaccountCheck == '0'}">
								<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_TRANSFER_ADDCOMMIT')">
									<a orderId="${entity.orderId}" title="提交" class="btn-mini btn-addTransferSubmit" href="#"><i class="icon-ok"></i></a>
								</sec:authorize>
							</c:if>
							<sec:authorize access="hasRole('ROLE_RETAIL_TRANSFER_ORDER_DETAIL')">
								<a orderId="${entity.orderId}" title="订单明细" class="btn-mini btn-view" href="#"><i class="icon-search"></i></a>
							</sec:authorize>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<%@ include file="/WEB-INF/views/common/pagination.jsp"%>

			<br/>
			<a href="${ctx }/retailChnl/retailChnlInf/listRetailChnlInf.do"><button class="btn btn-primary" type="button">返 回</button></a>
		</form>
	</div>
</div>

<div id="addTransferModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<form id="addTransferFrom" class="form-horizontal" enctype="multipart/form-data" action="${ctx }/retailChnl/retailChnlInf/addRetailChnlTransfer.do" method="post">
		<div class="modal-header">
			<button class="close" data-dismiss="modal">&times;</button>
			<h3 id="commodityInfModal_h">添加入账信息</h3>
		</div>
		<div class="modal-body">
			<input type="hidden" id="orderId" name="orderId"/>
			<input type="hidden" id="channelId" name="channelId"  value="${channelId }"/>
			<fieldset>
				<%--<div class="control-group">
					<label class="control-label">打款金额(元)：</label>
					<div class="controls">
						<input type="text" class="span3" id="remitAmt" name="remitAmt" onkeyup="checkPrice(this)"/>
						<span class="help-block"></span>
					</div>
				</div>--%>
				<div class="control-group">
					<label class="control-label">请选择打款凭证：</label>
					<div class="controls">
						<div id="evidenceUrlDiv" style="height: 200px; width: 200px">
							<img id="evidenceUrlImg" name="evidenceUrl" style="height: 100%; width: 100%"/>
						</div>
						<input id="evidenceUrl" name="evidenceUrl" class="span3" type="hidden">
						<input type="file" class="span3" id="evidenceUrlFile"  name="evidenceUrlFile" multiple/>
						<span class="help-block"></span>
					</div>
				</div>
				<%--<div class="control-group">
                    <label class="control-label">企业识别码：</label>
                    <div class="controls">
                        <input type="text" class="span3" id="companyCode" name ="companyCode" />
                        <span class="help-block"></span>
                    </div>
                </div>--%>
				<div class="control-group">
					<label class="control-label">上账金额(元)：</label>
					<div class="controls">
						<input type="text" class="span3" id="inaccountAmt" name ="inaccountAmt" onkeyup="checkPrice(this)"/>
						<span class="help-block"></span>
					</div>
				</div>
					<div class="control-group">
						<label class="control-label">托管账户金额(元)：</label>
						<div class="controls">
							<input type="text" class="span3" id="A01" name ="A01" onkeyup="checkPrice(this)"/>
							<span class="help-block"></span>
						</div>
					</div>
				<%--<div class="control-group">
					<label class="control-label">通用账户金额(元)：</label>
					<div class="controls">
						<input type="text" class="span3" id="A00" name ="A00" onkeyup="checkPrice(this)"/>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">办公用品账户金额(元)：</label>
					<div class="controls">
						<input type="text" class="span3" id="B01" name ="B01" onkeyup="checkPrice(this)"/>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">差旅专用账户金额(元)：</label>
					<div class="controls">
						<input type="text" class="span3" id="B02" name ="B02" onkeyup="checkPrice(this)"/>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">体检专用账户金额(元)：</label>
					<div class="controls">
						<input type="text" class="span3" id="B03" name ="B03" onkeyup="checkPrice(this)"/>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">培训专用账户金额(元)：</label>
					<div class="controls">
						<input type="text" class="span3" id="B04" name ="B04" onkeyup="checkPrice(this)"/>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">食品专用账户金额(元)：</label>
					<div class="controls">
						<input type="text" class="span3" id="B05" name ="B05" onkeyup="checkPrice(this)"/>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">通讯专用账户金额(元)：</label>
					<div class="controls">
						<input type="text" class="span3" id="B06" name ="B06" onkeyup="checkPrice(this)"/>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">保险专用账户金额(元)：</label>
					<div class="controls">
						<input type="text" class="span3" id="B07" name ="B07" onkeyup="checkPrice(this)"/>
						<span class="help-block"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">交通专用账户金额(元)：</label>
					<div class="controls">
						<input type="text" class="span3" id="B08" name ="B08" onkeyup="checkPrice(this)"/>
						<span class="help-block"></span>
					</div>
				</div>--%>
				<div class="control-group">
					<label class="control-label">备注：</label>
					<div class="controls">
						<input type="text" class="span3" id="remarks" name ="remarks" />
						<span class="help-block"></span>
					</div>
				</div>
			</fieldset>
		</div>
	</form>
	<div class="modal-footer" style="text-align: center;">
		<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_TRANSFER_ADD')">
			<button id="btn-submit" class="btn btn-primary btn-submit">确 定  </button>
		</sec:authorize>
		<button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
	</div>
</div>
<div id="imorptMsg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
	<div class="modal-header">

		<h3 id="commodityInfModal_h1">温馨提示</h3>
	</div>
	<br/><br/><br/>
	<h3 align="center">文件上传中......</h3>
</div>

<div id="msg" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="height: 200px;">
	<div class="modal-header">

		<h3 id="commodityInfModal_h2">温馨提示</h3>
	</div>
	<br/><br/><br/>
	<h3 align="center">信息正在处理......</h3>
</div>

<div id="updateCheckStatModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<form class="form-horizontal">
		<div class="modal-header">
			<button class="close" data-dismiss="modal">&times;</button>
			<input type="hidden" id="checkStatOrderId" name="checkStatOrderId"/>
			<h3 id="commodityInfModal_h3">审核</h3>
		</div>
		<div class="modal-body">
			<span>标记该条上账记录为已审核状态？</span>
		</div>
	</form>
	<div class="modal-footer" style="text-align: center;">
		<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_CHECK_COMMIT')">
			<button class="btn btn-primary btn-checkStat-submit">确 定  </button>
		</sec:authorize>
		<button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
	</div>
</div>

<div id="deleteTransferModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<form class="form-horizontal">
		<input type="hidden" id="transferOrderId" name="transferOrderId"/>
		<div class="modal-header">
			<button class="close" data-dismiss="modal">&times;</button>
			<h3 id="commodityInfModal_h5">删除上账记录</h3>
		</div>
		<div class="modal-body">
			<span>确认删除该条上账记录吗？</span>
		</div>
	</form>
	<div class="modal-footer" style="text-align: center;">
		<sec:authorize access="hasRole('ROLE_RETAIL_CHNL_TRANSFER_DELETE_COMMIT')">
			<button class="btn btn-primary btn-delete-submit">确 定  </button>
		</sec:authorize>
		<button class="btn" data-dismiss="modal" aria-hidden="true">取 消</button>
	</div>
</div>

<div id="imageModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<form class="form-horizontal">
		<div class="modal-header">
			<button class="close" data-dismiss="modal">&times;</button>
			<h3 id="commodityInfModal_h6">上账凭证图片</h3>
		</div>
		<div class="modal-body" >
			<div style="width: 100%; height: 100%; overflow-x: scroll">
				<img id="bigImage" style="height: 500px; max-width:initial"/>
			</div>
		</div>
	</form>
</div>

</body>
<script type="text/javascript">
    //验证价格（带有小数点，小数点最多是两位）
    function checkPrice(obj){
        obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字而不是
        obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符
        obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d\d).*$/,'$2$1.$2');//只能输入两个小数
        if(obj.value.indexOf(".")< 0 && obj.value !=""){//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
            obj.value= parseFloat(obj.value);
        }
    }
</script>
</html>
