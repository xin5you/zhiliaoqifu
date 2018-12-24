$(document).ready(function () {
    viewCompanyTransfer.init();
})

var viewCompanyTransfer = {
    init: function () {
        viewCompanyTransfer.initEvent();
    },
    initEvent:function(){
		$('.btn-invoice').on('click', viewCompanyTransfer.intoAddCompanyInvoice);
        $('.btn-invoice-submit').on('click', viewCompanyTransfer.addCompanyInvoiceCommit);
	},
    intoAddCompanyInvoice: function(){
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
                    $("#bName").val(data.msg.bname);
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
    addCompanyInvoiceCommit : function () {
       var orderListId = $('#orderListId').val();
       var companyId = $('#companyId').val();
        var invoiceInfo = $('#invoiceInfo').val();
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/company/addCompanyInvoiceCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderListId": orderListId,
                "invoiceInfo": invoiceInfo
            },
            success: function (data) {
                if(data.status){
                    location.href=Helper.getRootPath() + '/company/intoAddCompanyTransfer.do?operStatus=1&companyId='+companyId;
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

