$(document).ready(function() {
    addCompanyFee.init();
})

var addCompanyFee = {

    init : function() {
        addCompanyFee.initEvent();
        var operStatus=$("#operStatus").val();
        Helper.operTip(operStatus);
    },

    initEvent:function(){
        $('.btn-reset').on('click', addCompanyFee.searchReset);
        $('.btn-add').on('click', addCompanyFee.intoAddCompanyBillingType);
        $('.btn-add-Submit').on('click', addCompanyFee.addCompanyBillingTypeCommit);
        $('.btn-edit').on('click', addCompanyFee.intoEditCompanyBillingType);
        $('.btn-edit-submit').on('click', addCompanyFee.editCompanyBillingTypeCommit);
        $('.btn-delete').on('click', addCompanyFee.deleteCompanyBillingTypeCommit);
    },
    searchReset:function(){
        location = Helper.getRootPath() + '/company/listCompanyFee.do';
    },
    intoAddCompanyBillingType : function() {
        var companyBillingId = $(this).attr("companyBillingId");
        $("#companyBillingTypeId").val(companyBillingId);
        $("#btn-submit").addClass("btn-add-submit");
        $(".btn-add-submit").attr("onclick","addCompanyFee.addCompanyBillingTypeCommit();");
        $("#commodityInfModal_h").text("添加企业专项类型费率信息");
        $('#addCompanyFeeModal').modal({
            backdrop : "static"
        });
    },
    addCompanyBillingTypeCommit : function () {
        $("#btn-submit").attr('disabled',"true");
        var companyId = $("#companyId").val();
        var bId = $("#bId").val();
        var fee = $("#fee").val();
        var remarks = $("#remarks").val();
        $.ajax({
            url: Helper.getRootPath() + '/company/addCompanyFee.do',
            type: 'post',
            dataType : "json",
            data: {
                "companyId" : companyId,
                "bId" : bId,
                "fee" : fee,
                "remarks" : remarks
            },
            traditional:true,
            success: function (data) {
                $("#btn-submit").removeAttr("disabled");
                if(data.status) {
                    location = Helper.getRootPath() + '/company/listCompanyFee.do';
                }else {
                    Helper.alert(data.msg);
                    return false;
                }
            },
            error:function(){
                $("#btn-submit").removeAttr("disabled");
                Helper.alert("系统故障，请稍后再试");
                return false;
            }
        });
    },
    intoEditCompanyBillingType:function(){
        var companyBillingId = $(this).attr("companyBillingId");
        $("#companyBillingTypeId").val(companyBillingId);
        $.ajax({
            url: Helper.getRootPath() + '/company/getCompanyFee.do',
            type: 'post',
            dataType : "json",
            data: {
                "companyBillingId" : companyBillingId
            },
            traditional:true,
            success: function (data) {
                if (data == null && data == '') {
                    Helper.alert("系统故障，请稍后再试");
                    return false;
                }
                $("#bId").val(data.bId);
                $("#fee").val(data.fee);
                $("#remarks").val(data.remarks);
            },
            error:function(){
                Helper.alert("系统故障，请稍后再试");
                return false;
            }
        });
        $("#bId").attr('disabled',"true");
        $("#btn-submit").addClass("btn-edit-submit");
        $(".btn-edit-submit").attr("onclick","addCompanyFee.editCompanyBillingTypeCommit();");
        $("#commodityInfModal_h").text("编辑企业专项类型费率信息");
        $('#addCompanyFeeModal').modal({
            backdrop : "static"
        });
    },
    editCompanyBillingTypeCommit : function () {
        $("#btn-submit").attr('disabled',"true");
        var companyBillingTypeId = $("#companyBillingTypeId").val();
        var fee = $("#fee").val();
        var remarks = $("#remarks").val();
        $.ajax({
            url: Helper.getRootPath() + '/company/editCompanyFee.do',
            type: 'post',
            dataType : "json",
            data: {
                "companyBillingTypeId" : companyBillingTypeId,
                "fee" : fee,
                "remarks" : remarks
            },
            traditional:true,
            success: function (data) {
                $("#btn-submit").removeAttr("disabled");
                if(data.status) {
                    location = Helper.getRootPath() + '/company/listCompanyFee.do';
                }else {
                    Helper.alert(data.msg);
                    return false;
                }
            },
            error:function(){
                $("#btn-submit").removeAttr("disabled");
                Helper.alert("系统故障，请稍后再试");
                return false;
            }
        });
    },
    deleteCompanyBillingTypeCommit:function(){
        var companyBillingId = $(this).attr('companyBillingId');
        Helper.confirm("您是否删除该企业专项类型费率信息？",function(){
            $.ajax({
                url: Helper.getRootPath() + '/company/deleteCompanyFee.do',
                type: 'post',
                dataType : "json",
                data: {
                    "companyBillingId": companyBillingId
                },
                success: function (data) {
                    if(data.status){
                        location.href=Helper.getRootPath() + '/company/listCompanyFee.do?operStatus=4';
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
    }
}
