$(document).ready(function() {
    listCompany.init();
})

var listCompany = {

    init : function() {
        listCompany.initEvent();
        var operStatus=$("#operStatus").val();
        Helper.operTip(operStatus);
    },

    initEvent:function(){
        $('.btn-edit').on('click', listCompany.intoEditcompany);
        $('.btn-delete').on('click', listCompany.deleteCompanyCommit);
        $('.btn-add').on('click', listCompany.intoAddcompany);
        $('.btn-reset').on('click', listCompany.searchReset);
        $('.btn-open').on('click', listCompany.loadAddOpenAccountModal);
        $('.btn-open-submit').on('click', listCompany.companyOpenAccount);
        $('.btn-platform-tansfer').on('click', listCompany.intoAddCompanyTransfer);
        $('.btn-company-tansfer').on('click', listCompany.intoAddCompanyTransfer);
        $('.btn-accbal').on('click', listCompany.listCompanyAccBal);
        $('.btn-invoice').on('click', listCompany.intoAddCompanyTransfer);
        $('.btn-add-fee').on('click', listCompany.intoAddCompanyFee);
        $('.btn-platform-inAccount').on('click', listCompany.intoAddPlatformTransfer);
        $('.btn-platform-coupon').on('click', listCompany.intoPlatformCoupon);
    },
    searchReset:function(){
        var isPlatform = $("#isPlatform").val();
        location = Helper.getRootPath() + "/company/listCompany.do?isPlatform="+isPlatform;
    },
    intoAddcompany:function(){
        var isPlatform = $("#isPlatform").val();
        var url = Helper.getRootPath()+"/company/intoAddCompany.do?isPlatform="+isPlatform;
        location.href=url;
    },
    intoEditcompany:function(){
        var companyId = $(this).attr('companyId');
        var isPlatform = $("#isPlatform").val();
        var url = Helper.getRootPath()+"/company/intoEditCompany.do?companyId="+companyId+"&isPlatform="+isPlatform;
        location.href=url;
    },
    deleteCompanyCommit:function(){
        var companyId = $(this).attr('companyId');
        var isPlatform = $("#isPlatform").val();
        Helper.confirm("您是否删除该企业？",function(){
            $.ajax({
                url: Helper.getRootPath() + '/company/deleteCompany.do',
                type: 'post',
                dataType : "json",
                data: {
                    "companyId": companyId
                },
                success: function (data) {
                    if(data.status){
                        location.href=Helper.getRootPath() + "/company/listCompany.do?operStatus=4&isPlatform="+isPlatform;
                    }else{
                        Helper.alter(data.msg);
                        return false;
                    }
                },
                error:function(){
                    Helper.alert("系统超时，请稍微再试试");
                    return false;
                }
            });
        });
    },
    loadAddOpenAccountModal:function(){
        var companyId = $(this).attr('companyId');
        $('#companyId').val(companyId);
        $('#addOpenAccountModal').modal({
            backdrop : "static"
        });
    },
    companyOpenAccount : function() {
        var companyId = $('#companyId').val();
        var isPlatform = $("#isPlatform").val();
        $('#msg').modal({
            backdrop : "static"
        });

        $.ajax({
            url: Helper.getRootPath() + '/company/openAccountCompany.do',
            type: 'post',
            dataType : "json",
            data: {
                "companyId": companyId,
                "orderName": "企业"+companyId+"开户"
            },
            traditional:true,
            success: function (data) {
                if(data.status){
                    location.href=Helper.getRootPath() + "/company/listCompany.do?operStatus=1&isPlatform="+isPlatform;
                }else{
                    $('#msg').modal('hide');
                    Helper.alter(data.msg);
                    return false;
                }
            },
            error:function(){
                $('#msg').modal('hide');
                Helper.alert("系统超时，请稍微再试试");
                return false;
            }
        });
    },
    intoAddCompanyTransfer:function(){
        var companyId = $(this).attr('companyId');
        var isPlatform = $("#isPlatform").val();
        var url = Helper.getRootPath()+"/company/intoAddCompanyTransfer.do?companyId="+companyId+"&orderType=300";
        location.href=url;
    },
    listCompanyAccBal : function () {
        var isPlatform = $("#isPlatform").val();
        var companyId = $(this).attr('companyId');
        var url = Helper.getRootPath()+"/company/listCompanyAccBal.do?companyId="+companyId+"&isPlatform="+isPlatform;
        location.href=url;
    },
    intoAddCompanyFee : function () {
        var companyId = $(this).attr('companyId');
        var isPlatform = $("#isPlatform").val();
        var url = Helper.getRootPath()+"/company/listCompanyFee.do?companyId="+companyId+"&isPlatform="+isPlatform;
        location.href=url;
    },
    intoAddPlatformTransfer : function () {
        var companyId = $(this).attr('companyId');
        var url = Helper.getRootPath()+"/company/intoAddCompanyTransfer.do?companyId="+companyId+"&orderType=200";
        location.href=url;
    },
    intoPlatformCoupon : function () {
        var url = Helper.getRootPath()+"/company/listPlatformCoupon.do";
        location.href = url;
    }
}
