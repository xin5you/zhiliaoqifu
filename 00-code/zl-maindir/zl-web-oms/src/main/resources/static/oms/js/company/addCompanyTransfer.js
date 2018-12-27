$(document).ready(function () {
    addCompanyTransfer.init();
})

var addCompanyTransfer = {
    init: function () {
        addCompanyTransfer.initEvent();
    },
    initEvent:function(){
		$('.btn-platform-ok').on('click', addCompanyTransfer.intoAddCompanyPlatformTransfer);
        $('.btn-company-ok').on('click', addCompanyTransfer.intoAddCompanyTransfer);
        $('.btn-remit-submit').on('click', addCompanyTransfer.addCompanyTransferCommit);
        $('.btn-view').on('click', addCompanyTransfer.viewCompanyTransferDetail);
        $('.btn-invoice-order').on('click', addCompanyTransfer.viewCompanyTransferDetail);
	},
    intoAddCompanyPlatformTransfer: function(){
        var orderId = $(this).attr("orderId");
        $('#order_id').val(orderId);
        $('#addRemitModal').modal({
            backdrop : "static"
        });
	},
    intoAddCompanyTransfer: function(){
        var orderId = $(this).attr("orderId");
        $('#order_id').val(orderId);
        $('#addRemitModal').modal({
            backdrop : "static"
        });
    },
    addCompanyTransferCommit : function () {
       var orderId = $('#order_id').val();
       var companyId = $('#companyId').val();
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/company/addCompanyTransferCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId,
                "companyId": companyId
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
    },
    viewCompanyTransferDetail : function () {
        var orderId = $(this).attr("orderId");
        var companyId = $("#companyId").val();
        var url = Helper.getRootPath()+"/company/viewCompanyTransferDetail.do?orderId="+orderId+"&companyId="+companyId;
        location.href=url;
    }
};

