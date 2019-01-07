$(document).ready(function () {
    addTelProviderInfTransfer.init();
})

var addTelProviderInfTransfer = {

    init : function() {
        addTelProviderInfTransfer.initEvent();
    },

    initEvent:function(){
        $('.btn-addTransfer').on('click', addTelProviderInfTransfer.intoAddProviderTransfer);
        $('.btn-add-submit').on('click', addTelProviderInfTransfer.addProviderTransfer);
        $('#evidenceUrlFile').on('change', addTelProviderInfTransfer.fileUpload);
        $('.btn-addTransferSubmit').on('click', addTelProviderInfTransfer.addProviderTransferCommit);
        $('.btn-check').on('click', addTelProviderInfTransfer.intoUpdateProviderCheckStat);
        $('.btn-checkStat-submit').on('click', addTelProviderInfTransfer.updateProviderCheckStatCommit);
        $('.btn-remit').on('click', addTelProviderInfTransfer.intoAddProviderRemit);
        $('.btn-remit-submit').on('click', addTelProviderInfTransfer.addProviderRemitCommit);
        $('.btn-view').on('click', addTelProviderInfTransfer.viewProviderTransferDetail);
        $('.btn-edit').on('click', addTelProviderInfTransfer.intoEditProviderTransfer);
        $('.btn-edit-submit').on('click', addTelProviderInfTransfer.editProviderTransferCommit);
        $('.btn-delete').on('click', addTelProviderInfTransfer.intoDeleteProviderTransfer);
        $('.btn-delete-submit').on('click', addTelProviderInfTransfer.deleteProviderTransferCommit);
    },
    intoAddProviderTransfer : function() {
        var orderId = $(this).attr("orderId");
        $("#orderId").val(orderId);
        $("#btn-submit").addClass("btn-add-submit");
        $(".btn-add-submit").attr("onclick","addTelProviderInfTransfer.addProviderTransfer();");
        $("#commodityInfModal_h").text("添加入账信息");
        $('#addTransferModal').modal({
            backdrop : "static"
        });
    },
    fileUpload : function () {
        var imgUrl = $("#evidenceUrlFile").val();
        $("#evidenceUrl").val(imgUrl);
    },
    addProviderTransfer:function(){
        var providerId = $("#providerId").val();
        var remitAmt = $("#remitAmt").val();
        var evidenceUrl = $("#evidenceUrl").val();
        var companyCode = $("#companyCode").val();
        var inaccountAmt = $("#inaccountAmt").val();
        var remarks = $("#remarks").val();
        var A00 = $("#A00").val();
        var B01 = $("#B01").val();
        var B02 = $("#B02").val();
        var B03 = $("#B03").val();
        var B04 = $("#B04").val();
        var B05 = $("#B05").val();
        var B06 = $("#B06").val();
        var B07 = $("#B07").val();
        var B08 = $("#B08").val();
        if(remitAmt=='' || remitAmt == '0'){
            Helper.alert("打款金额不能为空");
            return false;
        }
        if(evidenceUrl=='' || evidenceUrl == '0'){
            Helper.alert("打款凭证不能为空");
            return false;
        }
        if(companyCode=='' || companyCode == '0'){
            Helper.alert("企业识别为空");
            return false;
        }
        if(inaccountAmt=='' || inaccountAmt == '0'){
            Helper.alert("上账金额不能为空");
            return false;
        }
        if (A00 == '' && B01 == '' && B02 == '' && B03 == '' && B04 == '' && B05 == '' && B06 == '' && B07 == '' && B08 == '') {
            Helper.alert("必须有一个专项账户金额不能为空");
            return false;
        }
        if ((inaccountAmt - 0) - (remitAmt - 0) > 0) {
            Helper.alert("上账金额不能大于打款金额");
            return false;
        }
        var sum = (A00 - 0) + (B01 - 0) + (B02 - 0) + (B03 - 0) + (B04 - 0) + (B05 - 0) + (B06 - 0) + (B07 - 0) + (B08 - 0);
        if ((sum - 0) != (inaccountAmt - 0)) {
            Helper.alert("所有专项金额总和必须等于上账金额");
            return false;
        }
        $.ajax({
            url: Helper.getRootPath() + '/provider/providerInf/addProviderTransfer.do',
            type: 'post',
            data: new FormData($("#addTransferFrom")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType : "json",
            success: function (result) {
                if(result.status) {
                    location = Helper.getRootPath() + '/provider/providerInf/intoAddProviderTransfer.do?operStatus=1&providerId='+providerId;
                } else {
                    Helper.alert(result.msg);
                    return false;
                }
            },
            error:function(){
                Helper.alert("系统故障，请稍后再试");
            }
        });
    },
    addProviderTransferCommit : function () {
        var orderId = $(this).attr("orderId");
        var providerId = $('#providerId').val();
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/provider/providerInf/addProviderTransferCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId,
                "providerId": providerId
            },
            success: function (data) {
                if(data.status){
                    location.href=Helper.getRootPath() + '/provider/providerInf/intoAddProviderTransfer.do?operStatus=1&providerId='+providerId;
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
    intoUpdateProviderCheckStat : function () {
        var orderId = $(this).attr("orderId");
        $("#checkStatOrderId").val(orderId);
        $('#updateCheckStatModal').modal({
            backdrop : "static"
        });
    },
    updateProviderCheckStatCommit : function () {
        var orderId = $("#checkStatOrderId").val();
        var providerId = $('#providerId').val();
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/provider/providerInf/updateProviderCheckStatCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId,
                "providerId": providerId
            },
            success: function (data) {
                if(data.status){
                    location.href=Helper.getRootPath() + '/provider/providerInf/intoAddProviderTransfer.do?operStatus=1&providerId='+providerId;
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
    intoAddProviderRemit : function () {
        var orderId = $(this).attr("orderId");
        $('#order_id').val(orderId);
        $('#addRemitModal').modal({
            backdrop : "static"
        });
    },
    addProviderRemitCommit : function () {
        var orderId = $('#order_id').val();
        var providerId = $('#providerId').val();
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/provider/providerInf/updateProviderRemitStatCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId,
                "providerId": providerId
            },
            success: function (data) {
                if(data.status){
                    location.href=Helper.getRootPath() + '/provider/providerInf/intoAddProviderTransfer.do?operStatus=1&providerId='+providerId;
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
    viewProviderTransferDetail : function () {
        var orderId = $(this).attr("orderId");
        var url = Helper.getRootPath()+"/provider/providerInf/viewProviderTransferDetail.do?orderId="+orderId;
        location.href=url;
    },
    intoEditProviderTransfer : function () {
        var orderId = $(this).attr("orderId");
        $("#orderId").val(orderId);
        $.ajax({
            url: Helper.getRootPath() + '/provider/providerInf/intoEditProviderTransfer.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId
            },
            success: function (data) {
                if(data.status){
                    $("#remitAmt").val(data.order.remitAmt);
                    $("#inaccountAmt").val(data.order.inaccountAmt);
                    $("#companyCode").val(data.order.companyCode);
                    $("#evidenceUrl").attr("src", "data:image/jpg;base64,"+data.order.evidenceUrl);
                    //$("#evidenceUrl").val(data.order.evidenceUrl);
                    $("#remarks").val(data.order.remarks);
                    $.each(data.orderDetail, function (i, item) {
                        $('.span3[id=' + item.bid + ']').attr('value',item.transAmt);
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
        $("#btn-submit").addClass("btn-edit-submit");
        $(".btn-edit-submit").attr("onclick","addTelProviderInfTransfer.editProviderTransferCommit();");
        $("#commodityInfModal_h").text("编辑入账信息");
        $('#addTransferModal').modal({
            backdrop : "static"
        });
    },
    editProviderTransferCommit : function () {
        var orderId = $("#orderId").val();
        var providerId = $("#providerId").val();
        var remitAmt = $("#remitAmt").val();
        var evidenceUrl = $("#evidenceUrl").val();
        var companyCode = $("#companyCode").val();
        var inaccountAmt = $("#inaccountAmt").val();
        var remarks = $("#remarks").val();
        var A00 = $("#A00").val();
        var B01 = $("#B01").val();
        var B02 = $("#B02").val();
        var B03 = $("#B03").val();
        var B04 = $("#B04").val();
        var B05 = $("#B05").val();
        var B06 = $("#B06").val();
        var B07 = $("#B07").val();
        var B08 = $("#B08").val();
        if(remitAmt=='' || remitAmt == '0'){
            Helper.alert("打款金额不能为空");
            return false;
        }
        if(evidenceUrl==''){
            Helper.alert("打款凭证不能为空");
            return false;
        }
        if(companyCode==''){
            Helper.alert("企业识别为空");
            return false;
        }
        if(inaccountAmt=='' || inaccountAmt == '0'){
            Helper.alert("上账金额不能为空");
            return false;
        }
        if (A00 == '' && B01 == '' && B02 == '' && B03 == '' && B04 == '' && B05 == '' && B06 == '' && B07 == '' && B08 == '') {
            Helper.alert("必须有一个专项账户金额不能为空");
            return false;
        }
        if ((inaccountAmt - 0) - (remitAmt - 0) > 0) {
            Helper.alert("上账金额不能大于打款金额");
            return false;
        }
        var sum = (A00 - 0) + (B01 - 0) + (B02 - 0) + (B03 - 0) + (B04 - 0) + (B05 - 0) + (B06 - 0) + (B07 - 0) + (B08 - 0);
        if ((sum - 0) != (inaccountAmt - 0)) {
            Helper.alert("所有专项金额总和必须等于上账金额");
            return false;
        }
        $.ajax({
            url: Helper.getRootPath() + '/provider/providerInf/editProviderTransfer.do',
            type: 'post',
            data: new FormData($("#addTransferFrom")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType : "json",

            success: function (result) {
                if(result.status) {
                    location = Helper.getRootPath() + '/provider/providerInf/intoAddProviderTransfer.do?operStatus=1&providerId='+providerId;
                } else {
                    Helper.alert(result.msg);
                    return false;
                }
            },
            error:function(){
                Helper.alert("系统故障，请稍后再试");
            }
        });
    },
    intoDeleteProviderTransfer : function () {
        var orderId = $(this).attr("orderId");
        $("#transferOrderId").val(orderId);
        $('#deleteTransferModal').modal({
            backdrop : "static"
        });
    },
    deleteProviderTransferCommit : function () {
        var orderId = $("#transferOrderId").val();
        var providerId = $("#providerId").val();
        $.ajax({
            url: Helper.getRootPath() + '/provider/providerInf/deleteProviderTransfer.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId
            },
            success: function (result) {
                if(result.status) {
                    location = Helper.getRootPath() + '/provider/providerInf/intoAddProviderTransfer.do?operStatus=4&providerId='+providerId;
                } else {
                    Helper.alert(result.msg);
                    return false;
                }
            },
            error:function(){
                Helper.alert("系统故障，请稍后再试");
            }
        });
    }
};

