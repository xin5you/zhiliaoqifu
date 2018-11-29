$(document).ready(function() {
	listNotSettingBanner.init();
})

var listNotSettingBanner = {
	init : function() {
		listNotSettingBanner.initEvent();
	},

	initEvent : function() {
		$('.btn-submit').on('click', listNotSettingBanner.addSettingBanners);
		$('#selectAll').on('click', listNotSettingBanner.selectAll);
		$('.btn-back').on('click', listNotSettingBanner.backSettingBannerList);
		$('.btn-reset').on('click', listNotSettingBanner.searchReset);
	},
	selectAll: function(){
		 if ($("#selectAll").attr("checked")) {
			 $(":checkbox").attr("checked", true);
		 } else {
			 $(":checkbox").attr("checked", false);
		 }
	 },
	 searchReset: function(){
		 var settingId = $("#settingId").val();
		 Helper.post('/eSetting/intoNotSettingBannerList?settingId='+settingId);
	 },
	 addSettingBanners : function() {
		var obj = [];
		$("input:checkbox[name='bannerId']:checked").each(function() {
			obj.push($(this).val());
		});
		var ids = obj.join(",");
		var settingId = $("#settingId").val();
		$(".btn-submit").attr('disabled',"true");
		$.ajax({
			url : Helper.getRootPath() + '/eSetting/addSettingBanner',
			type : 'post',
			dataType : "json",
			data : {
				settingId : settingId,
				ids : ids
			},
			success : function(data) {
				$(".btn-submit").removeAttr("disabled");
				if (data.code == '00') {
					Helper.confirm_one('添加商城服务banner成功', function() {
						Helper.post('/eSetting/intoAddBanner?settingId='+settingId);
					});
				} else {
					Helper.alert(data.msg);
					return false;
				}
			},
	        error : function(){
	        	Helper.alert(data.msg);
	        }
		});
	},
	backSettingBannerList : function() {
		var settingId = $("#settingId").val();
		Helper.post('/eSetting/intoAddBanner?settingId='+settingId);
	}
}
