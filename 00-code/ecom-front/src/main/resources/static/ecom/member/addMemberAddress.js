$(document).ready(function() {
	addMemberAddress.init();
})
var addMemberAddress = {
	isSaveState:true,
	init : function() {
		$('.hkbstore_alignment_center').on('click', addMemberAddress.productAddress);	//点击选择地址
		$('.button_contain').on('click', addMemberAddress.addMemberAddress);	//点击保存触发事件
	},
	productAddress: function(){
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

			     })
			}
		})
	},
	addMemberAddress: function(){
		loadingChange.showLoading();
		var userName = $("#userName").val();
		var mobile = $("#mobile").val();
		var address = $("#address_show").text();
		var addressId = $("#addressId").val();
		var addrDetail = $("#addrDetail").val();
		var reg = /^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$/;	//验证手机号
		if(userName == ''){
			addMemberAddress.isSaveState = true;
			loadingChange.hideLoading();
			jfShowTips.toastShow({'text' :"请输入收货人姓名"});
			return false;
		}
		if(mobile == ''){
			addMemberAddress.isSaveState = true;
			loadingChange.hideLoading();
			jfShowTips.toastShow({'text' :"请输入收货人手机号"});
			return false;
		}
		if(address == '请选择'){
			addMemberAddress.isSaveState = true;
			loadingChange.hideLoading();
			jfShowTips.toastShow({'text' :"请选择所在区域"});
			return false;
		}
		if(addrDetail == ''){
			addMemberAddress.isSaveState = true;
			loadingChange.hideLoading();
			jfShowTips.toastShow({'text' :"请输入详细地址"});
			return false;
		}
		if(!reg.test(mobile)){
			addMemberAddress.isSaveState = true;
			loadingChange.hideLoading();
			jfShowTips.toastShow({'text' :"请输入有效的收货人手机号"});
			return false;
		}
		if(addMemberAddress.isSaveState){
			addMemberAddress.isSaveState = false;
			$.ajax({
				type : 'POST',
				url : Helper.getRootPath() + '/member/memberAddress/addMemberAddress',
				data : {
					"userName" : userName,
					"mobile" : mobile,
					"address" : address,
					"addressId" : addressId,
					"addrDetail" : addrDetail
				},
				cache : false,
				dataType : 'json',
				success : function(data) {
					loadingChange.hideLoading();
					$('.button_contain').attr("disabled",false); 
					if (data.code == '00') {
						Helper.post('/member/memberAddress/getMemberAddressListByMemberId');
					} else {
						addMemberAddress.isSaveState = true;
						jfShowTips.toastShow({'text' :data.msg});
						return false;
					}
				},
				error : function() {
					addMemberAddress.isSaveState = true;
					jfShowTips.toastShow({'text' :data.msg});
				}
			});
		}else{
			loadingChange.hideLoading();
		}
	}
};