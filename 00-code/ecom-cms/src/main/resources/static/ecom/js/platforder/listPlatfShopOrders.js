$(document).ready(function() {
	listPlatfShopOrders.init();
})

var listPlatfShopOrders = {
	init : function() {
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
		$('.btn-search').on('click', listPlatfShopOrders.searchData);
		$('.btn-reset').on('click', listPlatfShopOrders.searchReset);
		$('.btn-again-submit').on('click', listPlatfShopOrders.placeOrder);
		$('.btn-edit').on('click', listPlatfShopOrders.cancellationOrder);
		$('.btn-upload').on('click', listPlatfShopOrders.uploadListPlatfShopOrders);
	},
	searchReset : function() {
		Helper.post('/platforder/platforder/getPlatfShopOrderLists');
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
	placeOrder: function(){
		var sOrderId = $(this).attr('sOrderId');
		if (sOrderId == null || sOrderId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定提交该订单吗？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/platforder/platforder/placeOrder',
				type : 'post',
				dataType : "json",
				data : {
					"sOrderId" : sOrderId
				},
				success : function(data) {
					if (data.code == '00') {
						Helper.post('/platforder/platforder/getPlatfShopOrderLists');
					}  else {
						Helper.alert(data.msg);
						return false;
					}
				}
			});
		});
	},
	cancellationOrder: function(){
		var sOrderId = $(this).attr('sOrderId');
		if (sOrderId == null || sOrderId == '') {
			Helper.alert("系统故障，请稍后再试");
			return false;
		}
		Helper.confirm("确定作废该订单吗？", function() {
			$.ajax({
				url : Helper.getRootPath() + '/platforder/platforder/cancellationOrder',
				type : 'post',
				dataType : "json",
				data : {
					"sOrderId" : sOrderId
				},
				success : function(data) {
					if (data.code == '00') {
						Helper.post('/platforder/platforder/getPlatfShopOrderLists');
					}  else {
						Helper.alert(data.msg);
						return false;
					}
				}
			});
		});
	},
	uploadListPlatfShopOrders: function(){
		var sOrderId = $("#sOrderId").val();
		var orderId = $("#orderId").val();
		var personalName = $("#personalName").val();
		var mobilePhoneNo = $("#mobilePhoneNo").val();
		var ecomCode = $("#ecomCode").val();
		var subOrderStatus = $("#subOrderStatus").val();
		var payStatus = $("#payStatus").val();
		var beginTime = $("#beginTime").val();
		var endTime = $("#endTime").val();
		Helper.post('/platforder/platforder/uploadListPlatfShopOrders' + '?sOrderId='+sOrderId+'&orderId='+orderId+'&personalName='+personalName+'&mobilePhoneNo='+mobilePhoneNo+'&ecomCode='+ecomCode+'&subOrderStatus='+subOrderStatus+'&payStatus='+payStatus+'&beginTime='+beginTime+'&endTime='+endTime);
	}
};