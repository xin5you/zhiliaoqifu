$(document).ready(function() {
	memberAddress.init();
	memberAddress.deleteAddress();
})

var memberAddress = {
	init : function() {
//		$('.select_key').on('click', memberAddress.defaultMemberAddress);	//修改默认地址
		$('.button_contain').on('click', memberAddress.intoAddMemberAddress);
	},
	
	deleteAddress: function() {
        var deleteCheckbox = document.getElementsByClassName('delete');
        /* <![CDATA[ */
        for (var i = 0; i < deleteCheckbox.length; i++) {
            deleteCheckbox[i].addEventListener('click', function () {
            	var addrId = $(this).attr('addrId');
                jfShowTips.dialogShow({
                    'mainText': '友情提示',
                    'minText': '确认要删除该地址吗？',
                    'checkFn': function () {
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
                })
            })
        }
        /*]]>*/
    },
	defaultMemberAddress: function(){
		jdShoppingCart.checkBoxChoose(this);
		var addrId = $(this).attr('addrId');
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/member/memberAddress/updateMemberDefAddr',
			data : {
				"addrId" : addrId
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
	},
	intoAddMemberAddress: function(){
		$.ajax({
			type : 'POST',
			url : Helper.getRootPath() + '/member/memberAddress/getCheckMemberAddressCount',
			cache : false,
			dataType : 'json',
			success : function(data) {
				if (data.code == '00') {
					window.location.href= Helper.getRootPath()+"/member/memberAddress/intoAddMemberAddress";
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