$(document).ready(function() {
	CartAccount.init();
})
var CartAccount = {
		init : function() {
			CartAccount.initEvent();
		},
		initEvent:function(){
			$('.take_address').on('click', CartAccount.getMemberAddressList);	//地址列表
			$('.hkbstore_alignment_center').on('click', CartAccount.productAddress);	//点击选择地址
			$('#editMemberAddressBtn').on('click', CartAccount.editMemberAddress);	//保存地址
			$("#payOrderBtn").on('click', CartAccount.unifiedOrder); //去付款
		},
		unifiedOrder:function(){
			$('#payOrderBtn').attr("disabled",true);
			loadingChange.showLoading();
			var addrId=$("#memberAddressId").val();
			if(addrId==null || addrId==""){
				loadingChange.hideLoading();
				$('#payOrderBtn').attr("disabled",false); 
				jfShowTips.toastShow({'text' : '请先选择收货地址'});
				return false;
			}
			$("#AccountForm").submit();
		},
		
		getMemberAddressList:function(){
			$.ajax({
				type : 'POST',
				url : Helper.getRootPath() + '/member/memberAddress/getMemberAddressListJson',
				data : {},
				cache : false,
				dataType : 'json',
				success : function(data) {
					
					if (data.code == '00') {
						var addrs=data.object; //地址列表对象
						
						var addsHtml="<div class=\"container_main\">"
						
								for(var i in addrs){
									if(addrs[i].defAddr=="0"){
										checkeded="  <div class=\"default_font\">默认</div>";
									}else{
										checkeded="";
									}
									addsHtml+="<div class=\"delivery_address_list\" id=\"addr_"+addrs[i].addrId+"\">"
									addsHtml+="<div onclick=\"CartAccount.addressSelectChange('"+addrs[i].addrId+"')\">"
									addsHtml+="<div class=\"address_top_plate \">"
						            addsHtml+="<div class=\"add_address_left \">"
						            addsHtml+="  <p>"+addrs[i].userName+"</p>"
						            addsHtml+=	checkeded
					                addsHtml+="</div>"
					                addsHtml+="<div class=\"add_address_right\">"
					                addsHtml+="  <p>"+addrs[i].mobile+"</p>"
					                addsHtml+="  <p>"+addrs[i].province +addrs[i].city + addrs[i].region +addrs[i].addrDetail+"</p>"
					                addsHtml+="</div>"
				                	addsHtml+="</div>"
				                	addsHtml+="</div>"
				                	addsHtml+="<div class=\"operation_area hkbstore_alignment_center\">"
									addsHtml+="  <a class=\"edit\" href=\"javascript:void(0);\" onclick=\"CartAccount.getMemberAddressById('"+addrs[i].addrId+"')\" >"
									addsHtml+="      <span><img src=\""+Helper.getRootPath()+"/front/images/hkbstore/icons/delivery_address_1_edit.png\"></span> 编辑</a>"
									addsHtml+="<div class=\"delete\" onclick=\"CartAccount.addressDeletShow('"+addrs[i].addrId+"')\" >"
									addsHtml+="<span><img src=\""+Helper.getRootPath()+"/front/images/hkbstore/icons/delivery_address_2_del.png\"></span> 删除"
									addsHtml+="</div>"
									addsHtml+="</div>"
								    addsHtml+="</div>"
								}
							addsHtml+="</div>";

							addsHtml+="<div class=\"bottom_button\">"
							addsHtml+="<div class=\"tab_box\"></div>"
							addsHtml+="<a  class=\"button_contain\" href=\"javascript:void(0);\" onclick=\"CartAccount.intoAddMemberAddress()\">"
							addsHtml+=" 添加新地址"
							addsHtml+="</a>"
							addsHtml+="</div>"
							$("#data-href-1").html(addsHtml);
							window.location.href="#1";
					} else {
						jfShowTips.toastShow({'text' : data.msg});
						return false;
					}
				},
				error : function() {
					jfShowTips.toastShow({'text' : '系统请求超时，请稍后再试~'})
				}
			});
		},
		intoAddMemberAddress:function(){
			$.ajax({
				type : 'POST',
				url : Helper.getRootPath() + '/member/memberAddress/getCheckMemberAddressCount',
				cache : false,
				dataType : 'json',
				success : function(data) {
					if (data.code == '00') {
						$("#flag").val("0");
						$("#userName").val("");
						$("#mobile").val("");
						$("#address_show").text("");
						$("#addressId").val("");
						$("#addrDetail").val("");
						$("#addrId").val("");
						$("#defAddrDiv").hide();
						window.location.href="#2";
					}else{
						jfShowTips.toastShow({'text' : data.msg});
					}
				},
				error : function() {
					jfShowTips.toastShow({'text' : '系统请求超时，请稍后再试~'})
				}
			});
			
		},
		getMemberAddressById:function(addrId){
			$.ajax({
				type : 'POST',
				url : Helper.getRootPath() + '/member/memberAddress/getMemberAddressByIdJson',
				data : {
					addrId:addrId
				},
				cache : false,
				dataType : 'json',
				success : function(data) {
					if (data.code == '00') {
						$("#addrId").val(data.object.addrId);
						$("#userName").val(data.object.userName);
						$("#mobile").val(data.object.mobile);
						$("#addressId").val(data.object.provinceId+","+data.object.cityId+","+data.object.regionId);
						$("#address_show").text(data.object.province+"，"+data.object.city+"，"+data.object.region);
						$("#addressId").val();
						$("#addrDetail").val(data.object.addrDetail);
						$("#flag").val("1");
						if(data.object.defAddr == '1'){
							$("#defAddr").prop("checked",false);
							$("#defAddrDiv").show();
						}else{
							$("#defAddr").prop("checked",true);
							$("#defAddrDiv").hide();
						}
						window.location.href="#2";
					}else{
						jfShowTips.toastShow({'text' : data.msg});
					}
				},
				error : function() {
					jfShowTips.toastShow({'text' : '系统请求超时，请稍后再试~'})
				}
			});
		},
		productAddress: function(){ //地区选择
			productAddress.show({fn:
				function(){
				     addressChoose.run({
					     yourUrl:Helper.getRootPath()+'/member/city/getArea?areaId=',
					     type: 'get',
					     targetDom:document.getElementById('address_show'),
					     fn:function() {
					    	 	$("#addressId").val(addressChoose.addressCityId);
					       		productAddress.hide();
					    	 }
					     });
					}
				});
		},
		editMemberAddress: function(){
			var flag= $("#flag").val();
			var userName = $("#userName").val();
			var mobile = $("#mobile").val();
			var address = $("#address_show").text();
			var addressId = $("#addressId").val();
			var addrDetail = $("#addrDetail").val();
			var defAddr = 1;
			if($("#defAddr").is(":checked") == true){
				defAddr =0;
			}
			var reg = /^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$/;	//验证手机号
			
			if(addressId == ''){
				pageChange.sectionBack();
				return false;
			}
			if(userName == ''){
				jfShowTips.toastShow({'text' :"请输入收货人姓名"});
				return false;
			}
			if(mobile == ''){
				jfShowTips.toastShow({'text' :"请输入收货人手机号"});
				return false;
			}
			if(address == '请选择' || address == '' ){
				jfShowTips.toastShow({'text' :"请选择所在区域"});
				return false;
			}
			if(addrDetail == ''){
				jfShowTips.toastShow({'text' : "请输入详细地址"});
				return false;
			}
			if(!reg.test(mobile)){
				jfShowTips.toastShow({'text' :"请输入有效的收货人手机号"});
				return false;
			}
			$.ajax({
				type : 'POST',
				url : Helper.getRootPath() + '/member/memberAddress/editMemberAddressAjax',
				data : {
					"flag":flag,
					"addrId": $("#addrId").val(),
					"userName" : userName,
					"mobile" : mobile,
					"address" : address,
					"addressId" : addressId,
					"addrDetail" : addrDetail,
					"defAddr" : defAddr
				},
				cache : false,
				dataType : 'json',
				success : function(data) {
//					$("#defAddr").prop("checked",false);
					if (data.code == '00') {
						CartAccount.getMemberAddressList();
					} else {
						jfShowTips.toastShow({'text' : data.msg});
						return false;
					}
				},
				error : function() {
					jfShowTips.toastShow({'text' : '系统请求超时，请稍后再试~'});
				}
			});
		},
		addressDeletShow:function(addrId){
            jfShowTips.dialogShow({
                'mainText': '友情提示',
                'minText': '确认要删除该地址吗？',
                'checkFn': function () {
                	CartAccount.addressDeleteCommit(addrId);
                	jfShowTips.dialogRemove()
                }
            })
		},
		addressDeleteCommit:function(addrId){
            //点击删除后做的事件
        	$.ajax({
    			type : 'POST',
    			url : Helper.getRootPath() + '/member/memberAddress/deleteMemberAddress?addrId='+addrId,
    			data : {
    				"addrId" : addrId
    			},
    			cache : false,
    			dataType : 'json',
    			success : function(data) {
    				
    				if (data.code == '00') {
    					$('#addr_'+addrId).remove();
    				} else {
    					jfShowTips.toastShow({'text' : data.msg});
    					return false;
    				}
    			},
    			error : function() {
    				jfShowTips.toastShow({'text' : '系统请求超时，请稍后再试~'});
    			}
    		});
		},
//		defaultMemberAddress: function(addrId,self){
//			$.ajax({
//				type : 'POST',
//				url : Helper.getRootPath() + '/member/memberAddress/updateMemberDefAddr',
//				data : {
//					"addrId" : addrId
//				},
//				cache : false,
//				dataType : 'json',
//				success : function(data) {
//					if (data.code == '00') {
//						jfShowTips.toastShow({'text' : "设置成功~"});
//						jdShoppingCart.checkBoxChoose(self);//样式选中
//					} else {
//						jfShowTips.toastShow({'text' : data.msg});
//						return false;
//					}
//				},
//				error : function() {
//					jfShowTips.toastShow({'text' : '系统请求超时，请稍后再试~'});
//				}
//			});
//		},
		addressSelectChange:function(addrId){
			$.ajax({
				type : 'POST',
				url : Helper.getRootPath() + '/member/memberAddress/getMemberAddressByIdJson',
				data : {
					addrId:addrId
				},
				cache : false,
				dataType : 'json',
				success : function(data) {
					if (data.code == '00') {
						$("#memberAddressId").val(data.object.addrId);
						$("#memberAddressName").text(data.object.userName);
						$("#memberAddressMobile").text(data.object.mobile);
						$("#memberAddressProvince").text(data.object.province);
						$("#memberAddressCity").text(data.object.city);
						$("#memberAddressRegion").text(data.object.region);
						$("#memberAddressDetail").text(data.object.addrDetail);
						
						$('.take_address').removeClass("no_address");
						$('.take_address').removeClass("display_address");
						$('#takeAddress').addClass("no_address");
						$('#takeAddress_no').addClass("display_address");
						window.location.href="#0";
					}else{
						jfShowTips.toastShow({'text' : data.msg});
					}
				},
				error : function() {
					jfShowTips.toastShow({'text' : '系统请求超时，请稍后再试~'})
				}
			});
		}
	};