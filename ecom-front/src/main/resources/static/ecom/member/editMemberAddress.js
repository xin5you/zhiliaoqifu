$(document).ready(function() {
	addMemberAddress.init();
})

var addMemberAddress = {
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
		var userName = $("#userName").val();
		var mobile = $("#mobile").val();
		var address = $("#address_show").text();
		var addressId = $("#addressId").val();
		var addrDetail = $("#addrDetail").val();
		var defAddr = '1';
		if($("#defAddr").is(":checked") == true){
			defAddr ='0';
		}
		var reg = /^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$/;	//验证手机号
		if(userName == ''){
			jfShowTips.toastShow({'text' :"请输入收货人姓名"});
			return false;
		}
		if(mobile == ''){
			jfShowTips.toastShow({'text' :"请输入收货人手机号"});
			return false;
		}
		if(address == '请选择'){
			jfShowTips.toastShow({'text' :"请选择所在区域"});
			return false;
		}
		if(addrDetail == ''){
			jfShowTips.toastShow({'text' :"请输入详细地址"});
			return false;
		}
		if(!reg.test(mobile)){
			jfShowTips.toastShow({'text' :"请输入有效的收货人手机号"});
			return false;
		}
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/member/memberAddress/updateMemberAddress',
			data : {
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
				if (data.code == '00') {
					Helper.post('/member/memberAddress/getMemberAddressListByMemberId');
				} else {
					jfShowTips.toastShow({'text' :data.msg});
					return false;
				}
			},
			error : function() {
				jfShowTips.toastShow({'text' :data.msg});
			}
		});
	}
};