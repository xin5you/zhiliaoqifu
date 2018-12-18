<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <!-- header -->
 <div id="loading_layer" style="display:none"><img src="${ctx}/static/gebo/img/ajax_loader.gif" alt="" /></div>
   <header>
             <div class="navbar navbar-fixed-top">
                 <div class="navbar-inner">
                     <div class="container-fluid">
                         <a class="brand" href="#"><i class="icon-home icon-white"></i>知了企服运营管理平台</a>
                         <ul class="nav user_menu pull-right">
                             <li class="divider-vertical hidden-phone hidden-tablet"></li>
                             <li class="dropdown">
                             	
                                 <a href="#" class="dropdown-toggle" data-toggle="dropdown">${user.username } <b class="caret"></b></a>
                                 <ul class="dropdown-menu">
                                 	<li><a href="${ctx}/login/logout.do">退出登陆</a></li>
                                 	<li><a href="#" id="updatePwdBtn" onclick="PayCommon.loadUpdatePwdModal();" >重置密码</a></li>
                                 </ul>
                             </li>
                         </ul>
                         <ul class="nav" id="mobile-nav">
                             <li class="dropdown">
                                 <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="icon-cog icon-white"></i>分销商管理<b class="caret"></b></a>
                                 <ul class="dropdown-menu">
                                     	<li><a href="${ctx }/retailChnl/retailChnlInf/listRetailChnlInf.do">分销商信息管理</a></li>
                                     	<%-- <li><a href="${ctx }/retailChnl/reserve/listRetailChnlReserve.do">分销商备付金管理</a></li> --%>
										<li><a href="${ctx }/retailChnl/retailChnlOrder/listRetailChnlOrderInf.do">分销商订单</a></li>
										<%-- <li><a href="${ctx }/retailChnl/retailChnlProduct/listRetailChnlProduct.do">分销商充值产品管理</a></li> --%>
										<%-- <li><a href="${ctx }/retailChnl/areaInf/listRetailChnlAreaInf.do">分销商话费地区维护</a></li>
										<li><a href="${ctx }/retailChnl/item/listRetailChnlItem.do">分销商产品折扣率管理</a></li> --%>
                                 </ul>
                             </li>
                             
                             <sec:authorize access="hasRole('ROLE_PHONE_RECHARGE')">
                             <li class="dropdown">
                                 <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="icon-cog icon-white"></i>供应商管理<b class="caret"></b></a>
                                 <ul class="dropdown-menu">
                                   		<li><a href="${ctx }/provider/providerInf/listProviderInf.do">供应商信息管理</a></li>
										<li><a href="${ctx }/provider/providerOrder/listProviderOrderInf.do">供应商订单</a></li>
                                 </ul>
                             </li>
                             </sec:authorize>
                             
                             <sec:authorize access="hasRole('ROLE_SPECIAL_ACCOUNT_MANAGE')">
                             <li class="dropdown">
                             	<a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="icon-bookmark icon-white"></i>企业管理 <b class="caret"></b></a>
                                 <ul class="dropdown-menu">
                                 	<sec:authorize access="hasRole('ROLE_COMPANY_INFO')">
                                         <li><a href="${ctx }/company/listCompany.do">企业信息管理</a></li>
                                     </sec:authorize>
                                 </ul>
                             </li>
                             </sec:authorize>
                             
                             <sec:authorize access="hasRole('ROLE_DIY_MANAGER')">
                             <li class="dropdown">
                                 <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="icon-cog icon-white"></i>商户自助平台<b class="caret"></b></a>
                                 <ul class="dropdown-menu">
                                 	<sec:authorize access="hasRole('ROLE_DIY_USER')">
                                     	<li><a href="${ctx }/diy/diyUser/listDiyUser.do">商户用户管理</a></li>
                                     </sec:authorize>
                                     <sec:authorize access="hasRole('ROLE_DIY_ROLE')">
                                     	<li><a href="${ctx }/diy/diyRole/listDiyRole.do">商户角色管理</a></li>
                                     </sec:authorize>
                                     <sec:authorize access="hasRole('ROLE_DIY_RESORCE')">
                                     	<li><a href="${ctx }/diy/diyResource/listDiyResource.do">商户资源管理</a></li>
                                     </sec:authorize>
                                 </ul>
                             </li>
                             </sec:authorize>
                             
                             <sec:authorize access="hasRole('ROLE_SPECIAL_ACCOUNT_MANAGE')">
                             <li class="dropdown">
                                 <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="icon-bookmark icon-white"></i>账户管理 <b class="caret"></b></a>
                                 <ul class="dropdown-menu">
                                     <sec:authorize access="hasRole('ROLE_BILLING_TYPE')">
                                        <li><a href="${ctx }/billingType/listBillingType.do">账户类型管理</a></li>
                                     </sec:authorize>
                                     <sec:authorize access="hasRole('ROLE_SPE_BATCH_OPEN_ACCOUNT')">
                                     	<li><a href="${ctx }/batch/openAccount/listOpenAccount.do">批量开户</a></li>
                                     	<li><a href="${ctx }/batch/recharge/listRecharge.do">批量充值</a></li>
                                     </sec:authorize>
                                     <!-- <sec:authorize access="hasRole('ROLE_SPE_BATCH_RECHARGE')"> -->
                                     	
                                     <!-- </sec:authorize> -->
                                 </ul>
                             </li>
                             </sec:authorize>
                             
                             <sec:authorize access="hasRole('ROLE_SYS_MANAGER')">
                             <li class="dropdown">
                                 <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="icon-wrench icon-white"></i>系统管理<b class="caret"></b></a>
                                 <ul class="dropdown-menu">
                                 	<sec:authorize access="hasRole('ROLE_SYS_USER')">
                                     	<li><a href="${ctx }/sys/user/listUser.do">用户管理</a></li>
                                     </sec:authorize>
                                     <sec:authorize access="hasRole('ROLE_SYS_ROLE')">
                                     	<li><a href="${ctx }/sys/role/listRole.do">角色管理</a></li>
                                     </sec:authorize>
                                     <sec:authorize access="hasRole('ROLE_SYS_RESORCE')">
                                     	<li><a href="${ctx }/sys/resource/listResource.do">资源管理</a></li>
                                     </sec:authorize>
                                     <sec:authorize access="hasRole('ROLE_SYS_ORGANIZATION')">
                                     	<li><a href="${ctx }/sys/organization/listOrganization.do">部门管理</a></li>
                                     </sec:authorize>
                                     <li><a href="${ctx }/baseDict/listBaseDict.do">字典管理</a></li>
                                 </ul>
                             </li>
                             </sec:authorize>
                         </ul>
                     </div>
                 </div>
             </div>
  </header>
  
  <div id="updatePWDModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<form class="form-horizontal">
			<div class="modal-header">
				<button class="close" data-dismiss="modal">&times;</button>
				<h3>修改密码</h3>
			</div>
			<div class="modal-body">
				<fieldset>
					<div class="control-group">
						<label class="control-label">旧密码：</label>
						<div class="controls">
							<input type="password" class="span3" id="oldPasswrodPage" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">新密码：</label>
						<div class="controls">
							<input type="password" class="span3" id="newPasswordPage"/>
						</div>	
					</div>
					<div class="control-group">
						<label class="control-label">二次确认密码：</label>
						<div class="controls">
							<input type="password" class="span3" id="newPassword2Page"/>
						</div>
					</div>
				</fieldset>
			</div>
		</form>
		<div class="modal-footer" style="text-align: center;">
            <button class="btn btn-primary btn-submit" id="userPWdSubmitBtn">提 交  </button>
            <button class="btn" data-dismiss="modal" aria-hidden="true">关 闭</button>
        </div> 
	</div>
