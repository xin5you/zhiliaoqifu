$(document).ready(function() {
	listPhoneRecharge.init();
})

var listPhoneRecharge = {
	init : function() {
		listPhoneRecharge.initEvent();
		listPhoneRecharge.showDatetimepicker();
	},

	initEvent:function(){
		$('.date-time-picker').datetimepicker({
	        format: 'yyyy-MM-dd hh:mm:ss',
	        language: 'zh-CN',
	        pickDate: true,
	        pickTime: true,
	        hourStep: 1,
	        minuteStep: 5,
	        secondStep: 10,
	        endDate: new Date(new Date() - 86400000),
	        initialDate: new Date(new Date() - 86400000),
	        inputMask: true
	      }).on('changeDate', function(ev) {
	    	  //alert(ev.date.valueOf());
	    });
		$('.btn-search').on('click', listPhoneRecharge.searchData);
		$('.btn-reset').on('click', listPhoneRecharge.searchReset);
		$('.btn-upload').on('click',listPhoneRecharge.uploadListPhoneRecharge);
		$('.btn-edit').on('click',listPhoneRecharge.refundPhoneRecharge);
		$('#queryType').on('change', listPhoneRecharge.showDatetimepicker);
	},

	searchData: function(){
		var sd = $('#startTime').val();
		var ed = $('#endTime').val();
		if(sd != '' || sd != null){
			if (sd != '' && ed != '' && sd.localeCompare(ed) > 0) {
				Helper.alert('开始时间不能大于结束时间');
				return false;
			}
		}
		document.forms['searchForm'].submit();
	},
	searchReset: function(){
		location = Helper.getRootPath() + '/phone/phoneRecharge/getPhoneRechargeList.do';
	},
	showDatetimepicker: function(){
		var queryType = $('#queryType').val();
		if (queryType == 'his') {
			$('#datetimepicker1').show();
			$('#datetimepicker2').show();
		} else {
			$('#datetimepicker1').hide();
			$('#datetimepicker2').hide();
			$('#startTime').val('');
			$('#endTime').val('');
		}
	},
	uploadListPhoneRecharge:function(){
		var rId = $("#rId").val();
		var supplierOrderNo = $("#supplierOrderNo").val();
		var channelOrderNo = $("#channelOrderNo").val();
		var personalName = $("#personalName").val();
		var mobilePhoneNo = $("#mobilePhoneNo").val();
		var phone = $("#phone").val();
		var transStat = $("#transStat").val();
		var orderType = $('#orderType').val();
		var reqChannel = $('#reqChannel').val();
		var sd = $('#startTime').val();
		var ed = $('#endTime').val();
		var queryType = $('#queryType').val();
		
		
		if (queryType == 'his') {
			if (ed == '' || ed == null) {
				Helper.alert('交易结束时间不能为空');
				return false;
			}
			var s_d = sd.replace(new RegExp("-","g"),"").substring(0, 8);
			var e_d = ed.replace(new RegExp("-","g"),"").substring(0, 8);
			var now = new Date();
			var year = now.getFullYear();
		    var month =(now.getMonth() + 1).toString();
		    var day = (now.getDate()).toString();
		    if (month.length == 1) {
		        month = "0" + month;
		    }
		    if (day.length == 1) {
		        day = "0" + day;
		    }
		    var c_d = year + month + day;
			if (s_d == c_d) {
				Helper.alert('交易开始时间不能为当天');
				return false;
			}
			if (e_d == c_d) {
				Helper.alert('交易结束时间不能为当天');
				return false;
			}
			if(sd !='' && ed !='' && sd.localeCompare(ed)>0){
				Helper.alert('交易开始时间不能大于结束时间');
				return false;
			}
			location = Helper.getRootPath() + '/phone/phoneRecharge/uploadListPhoneRecharge.do?rId='+rId+'&queryType='+queryType+'&supplierOrderNo='+supplierOrderNo+'&channelOrderNo='+channelOrderNo+'&personalName='+personalName+'&mobilePhoneNo='+mobilePhoneNo+'&phone='+phone+'&transStat='+transStat+'&orderType='+orderType+'&reqChannel='+reqChannel+'&startTime='+sd+'&endTime='+ed;
		} else {
			location = Helper.getRootPath() + '/phone/phoneRecharge/uploadListPhoneRecharge.do?rId='+rId+'&queryType='+queryType+'&supplierOrderNo='+supplierOrderNo+'&channelOrderNo='+channelOrderNo+'&personalName='+personalName+'&mobilePhoneNo='+mobilePhoneNo+'&phone='+phone+'&transStat='+transStat+'&orderType='+orderType+'&reqChannel='+reqChannel;
		}
		
	},
	refundPhoneRecharge:function(){
		var rId = $(this).attr('rId');
		
		Helper.confirm("该订单【"+rId+"】确定要退款吗？",function(){
		    $.ajax({								  
	            url: Helper.getRootPath() + '/phone/phoneRecharge/refundPhoneRecharge.do',
	            type: 'post',
	            dataType : "json",
	            data: {
	                "rId": rId
	            },
	            success: function (result) {
	            	if(result.status){
	            		location = Helper.getRootPath() + '/phone/phoneRecharge/getPhoneRechargeList.do';
	            	}else{
	            		Helper.alert(result.msg);
	            	}
	            },
	            error:function(){
	            	Helper.alert("系统故障，请稍后再试");
	            }
	      });
		});
	}
}
