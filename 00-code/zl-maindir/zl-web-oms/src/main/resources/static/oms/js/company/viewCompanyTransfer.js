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
        $('.btn-edit-inAmt').on('click', viewCompanyTransfer.intoEditCompanyInAmt);
        $('.btn-edit-inAmt-submit').on('click', viewCompanyTransfer.editCompanyInAmtCommit);
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
        var orderId = $('#orderId').val();
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
                    location.href=Helper.getRootPath() + '/company/viewCompanyTransferDetail.do?operStatus=1&companyId='+companyId+"&orderId="+orderId;
                }else{
                    $('#msg').modal('hide');
                    Helper.alert(data.msg);
                    return false;
                }
            },
            error:function(){
                $('#msg').modal('hide');
                Helper.alert("系统超时，请稍后再试");
                return false;
            }
        });
    },
    intoEditCompanyInAmt : function () {
        var orderListId = $(this).attr("orderListId");
        $('#orderListId_').val(orderListId);
        $.ajax({
            url: Helper.getRootPath() + '/inaccount/getInaccountByOrderListId.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderListId": orderListId
            },
            success: function (data) {
                if(data.status){
                    $("#bName_").val(data.msg.bname);
                    $("#transAmt_").val(data.msg.transAmt);
                    $("#inaccountAmt_").val(data.msg.inaccountAmt);
                    $("#platformInAmt_").val(data.msg.platformInAmt);
                    $("#companyInAmt_").val(data.msg.companyInAmt);
                    $("#isInvoice_").val(data.msg.isInvoice);
                    $("#invoiceInfo_").val(data.msg.invoiceInfo);
                    $('#editInAmtModal').modal({
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
    editCompanyInAmtCommit : function () {
        var orderListId = $('#orderListId_').val();
        var companyInAmt = $('#companyInAmt_').val();
        var companyId = $('#companyId').val();
        var orderId = $('#orderId').val();
        if(companyInAmt =='' || companyInAmt == '0'){
            Helper.alert("企业收款金额不能为空");
            return false;
        }
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/company/editCompanyInAmtCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderListId": orderListId,
                "companyInAmt": companyInAmt
            },
            success: function (data) {
                if(data.status){
                    location.href=Helper.getRootPath() + '/company/viewCompanyTransferDetail.do?operStatus=1&companyId='+companyId+"&orderId="+orderId;
                }else{
                    $('#msg').modal('hide');
                    Helper.alert(data.msg);
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

