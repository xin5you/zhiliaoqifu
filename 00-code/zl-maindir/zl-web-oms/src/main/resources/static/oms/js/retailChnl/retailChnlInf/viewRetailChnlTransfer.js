$(document).ready(function () {
    viewRetailChnlTransfer.init();
})

var viewRetailChnlTransfer = {
    init: function () {
        viewRetailChnlTransfer.initEvent();
    },
    initEvent:function(){
		$('.btn-invoice').on('click', viewRetailChnlTransfer.intoAddRetailChnlInvoice);
        $('.btn-invoice-submit').on('click', viewRetailChnlTransfer.addRetailChnlInvoiceCommit);
	},
    intoAddRetailChnlInvoice: function(){
        var orderListId = $(this).attr("orderListId");
        $('#orderListId').val(orderListId);
        $.ajax({
            url: Helper.getRootPath() + '/inaccount/getInaccountByOrderListId.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderListId": orderListId
            },
            success: function (data) {
                if(data.status){
                    $("#bName").val(data.msg.bName);
                    $("#companyInAmt").val(data.msg.companyInAmt);
                    $('#addInvoiceModal').modal({
                        backdrop : "static"
                    });
                }else{
                    Helper.alter(data.msg);
                    return false;
                }
            },
            error:function(){
                Helper.alert("系统超时，请稍后再试");
                return false;
            }
        });
	},
    addRetailChnlInvoiceCommit : function () {
       var orderListId = $('#orderListId').val();
       var channelId = $('#channelId').val();
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/addRetailChnlInvoiceCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderListId": orderListId
            },
            success: function (data) {
                if(data.status){
                    location.href=Helper.getRootPath() + '/retailChnl/retailChnlInf/intoAddRetailChnlTransfer.do?operStatus=1&channelId='+channelId;
                }else{
                    $('#msg').modal('hide');
                    Helper.alter(data.msg);
                    return false;
                }
            },
            error:function(){
                $('#msg').modal('hide');
                Helper.alert("系统超时，请稍后再试");
                return false;
            }
        });
    }
};

