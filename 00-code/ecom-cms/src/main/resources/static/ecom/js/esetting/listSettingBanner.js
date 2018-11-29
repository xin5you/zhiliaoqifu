$(document).ready(function() {
	listSettingBanner.init();
})

var listSettingBanner = {
	init : function() {
		listSettingBanner.initEvent();
	},

	initEvent : function() {
		$('.btn-reset').on('click', listSettingBanner.searchReset);
		$('.btn-add').on('click', listSettingBanner.intoNotSettingBannerList);
		$('.btn-delete').on('click', listSettingBanner.deleteSettingBanner);
	},
	deleteSettingBanner: function(){
		var bannerId = $(this).attr('bannerId');
		var settingId = $("#settingId").val();
		if (bannerId == null || bannerId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		if (settingId == null || settingId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定移除该商品？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/eSetting/deleteSettingBanner',
				type : 'post',
				dataType : "json",
				data : {
					"settingId" : settingId,
					"bannerId" : bannerId
				},
				success : function(data) {
					if (data.code == '00') {
						Helper.confirm_one('删除楼层信息成功', function() {
							listSettingBanner.searchReset();
						});
					}  else {
						Helper.alert(data.msg);
						return false;
					}
				},
				error : function() {
					Helper.alert(data.msg);
				}
			});
		});
	},
	intoNotSettingBannerList: function(){
		var settingId = $("#settingId").val();
		Helper.post('/eSetting/intoNotSettingBannerList?settingId='+settingId);
	},
	searchReset: function(){
		var settingId = $("#settingId").val();
		Helper.post('/eSetting/intoAddBanner?settingId='+settingId);
	},
}
