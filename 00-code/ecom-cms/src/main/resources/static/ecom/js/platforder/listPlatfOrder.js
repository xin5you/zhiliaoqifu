$(document).ready(function() {
	listPlatfOrder.init();
})

var listPlatfOrder = {
	init : function() {
		listPlatfOrder.initEvent();
//		listPlatfOrder.showDatetimepicker();
	},
	initEvent : function() {
		$('.date-time-picker').datetimepicker({
			format : 'yyyy-MM-dd hh:mm:ss',
			language : 'zh-CN',
			pickDate : true,
			pickTime : true,
			hourStep : 1,
			minuteStep : 5,
			secondStep : 10,
			endDate : new Date(new Date() - 86400000),
			initialDate : new Date(new Date() - 86400000),
			inputMask : true
		}).on('changeDate', function(ev) {
			// alert(ev.date.valueOf());
		});
		$('.btn-search').on('click', listPlatfOrder.searchData);
		$('.btn-upload').on('click', listPlatfOrder.uploadListPlatfOrder);
		$('.btn-reset').on('click', listPlatfOrder.searchReset);
		$('.btn-edit').on('click', listPlatfOrder.getPlatfShopOrderList);
	},
	getPlatfShopOrderList: function(){
		var orderId = $(this).attr('orderId');
		Helper.post('/platforder/platforder/getPlatfShopOrderList?orderId='+orderId);
	},
	searchReset : function() {
		Helper.post('/platforder/platforder/getPlatfOrderList');
	},
	searchData : function() {
		var sd = $('#beginTime').val();
		var ed = $('#endTime').val();
		if(sd != '' || sd != null){
			if (sd != '' && ed != '' && sd.localeCompare(ed) > 0) {
				Helper.alert('开始时间不能大于结束时间');
				return false;
			}
		}
		document.forms['searchForm'].submit();
	},
	uploadListPlatfOrder: function(){
		var orderId = $("#orderId").val();
		var dmsRelatedKey = $("#dmsRelatedKey").val();
		var personalName = $("#personalName").val();
		var mobilePhoneNo = $("#mobilePhoneNo").val();
		var payStatus = $("#payStatus").val();
		var beginTime = $("#beginTime").val();
		var endTime = $("#endTime").val();
		Helper.post('/platforder/platforder/uploadListPlatfOrder' + '?orderId='+orderId+'&dmsRelatedKey='+dmsRelatedKey+'&personalName='+personalName+'&mobilePhoneNo='+mobilePhoneNo+'&payStatus='+payStatus+'&beginTime='+beginTime+'&endTime='+endTime);
	}
};