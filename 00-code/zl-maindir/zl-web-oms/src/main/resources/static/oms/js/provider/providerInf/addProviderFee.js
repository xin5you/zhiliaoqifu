$(document).ready(function() {
    addProviderFee.init();
})

var addProviderFee = {

    init : function() {
        addProviderFee.initEvent();
    },

    initEvent:function(){
        $('.btn-reset').on('click', addProviderFee.searchReset);
        $('.btn-add').on('click', addProviderFee.intoAddProviderBillingType);
        $('.btn-add-Submit').on('click', addProviderFee.addProviderBillingTypeCommit);
        $('.btn-edit').on('click', addProviderFee.intoEditProviderBillingType);
        $('.btn-edit-submit').on('click', addProviderFee.editProviderBillingTypeCommit);
        $('.btn-delete').on('click', addProviderFee.deleteProviderBillingTypeCommit);
    },
    searchReset:function(){
        var providerId = $("#providerId").val();
        location = Helper.getRootPath() + "/provider/providerInf/listProviderFee.do?providerId="+providerId;
    },
    intoAddProviderBillingType : function() {
        var providerBillingId = $(this).attr("providerBillingId");
        $("#providerBillingTypeId").val(providerBillingId);
        $("#btn-submit").addClass("btn-add-submit");
        $(".btn-add-submit").attr("onclick","addProviderFee.addProviderBillingTypeCommit();");
        $("#commodityInfModal_h").text("添加供应商专项类型费率信息");
        $('#addProviderFeeModal').modal({
            backdrop : "static"
        });
    },
    addProviderBillingTypeCommit : function () {
        $("#btn-submit").attr('disabled',"true");
        var providerId = $("#providerId").val();
        var bId = $("#bId").val();
        var fee = $("#fee").val();
        var remarks = $("#remarks").val();
        $.ajax({
            url: Helper.getRootPath() + '/provider/providerInf/addProviderFee.do',
            type: 'post',
            dataType : "json",
            data: {
                "providerId" : providerId,
                "bId" : bId,
                "fee" : fee,
                "remarks" : remarks
            },
            traditional:true,
            success: function (data) {
                $("#btn-submit").removeAttr("disabled");
                if(data.status) {
                    location = Helper.getRootPath() + '/provider/providerInf/listProviderFee.do?operStatus=1&providerId='+providerId;
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
    intoEditProviderBillingType:function(){
        var providerBillingId = $(this).attr("providerBillingId");
        $("#providerBillingTypeId").val(providerBillingId);
        $.ajax({
            url: Helper.getRootPath() + '/provider/providerInf/getProviderFee.do',
            type: 'post',
            dataType : "json",
            data: {
                "providerBillingId" : providerBillingId
            },
            traditional:true,
            success: function (data) {
                if (data == null && data == '') {
                    Helper.alert("系统故障，请稍后再试");
                    return false;
                }
                $("#bId").val(data.bid);
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
        $(".btn-edit-submit").attr("onclick","addProviderFee.editProviderBillingTypeCommit();");
        $("#commodityInfModal_h").text("编辑供应商专项类型费率信息");
        $('#addProviderFeeModal').modal({
            backdrop : "static"
        });
    },
    editProviderBillingTypeCommit : function () {
        $("#btn-submit").attr('disabled',"true");
        var providerId = $("#providerId").val();
        var providerBillingTypeId = $("#providerBillingTypeId").val();
        var fee = $("#fee").val();
        var remarks = $("#remarks").val();
        $.ajax({
            url: Helper.getRootPath() + '/provider/providerInf/editProviderFee.do',
            type: 'post',
            dataType : "json",
            data: {
                "providerId" : providerId,
                "providerBillingTypeId" : providerBillingTypeId,
                "fee" : fee,
                "remarks" : remarks
            },
            traditional:true,
            success: function (data) {
                $("#btn-submit").removeAttr("disabled");
                if(data.status) {
                    location = Helper.getRootPath() + '/provider/providerInf/listProviderFee.do?operStatus=2&providerId='+providerId;
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
    deleteProviderBillingTypeCommit:function(){
        var providerId = $("#providerId").val();
        var providerBillingId = $(this).attr('providerBillingId');
        Helper.confirm("您是否删除该供应商专项类型费率信息？",function(){
            $.ajax({
                url: Helper.getRootPath() + '/provider/providerInf/deleteProviderFee.do',
                type: 'post',
                dataType : "json",
                data: {
                    "providerBillingId": providerBillingId
                },
                success: function (data) {
                    if(data.status){
                        location.href=Helper.getRootPath() + '/provider/providerInf/listProviderFee.do?operStatus=4&providerId='+providerId;
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
