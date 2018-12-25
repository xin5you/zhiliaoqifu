$(document).ready(function () {
    addRetailChnlInfTransfer.init();
})

var addRetailChnlInfTransfer = {

    init : function() {
        addRetailChnlInfTransfer.initEvent();
    },

    initEvent:function(){
        $('.btn-addTransfer').on('click', addRetailChnlInfTransfer.intoAddRetailChnlTransfer);
        $('.btn-add-submit').on('click', addRetailChnlInfTransfer.addRetailChnlTransfer);
        $('#evidenceUrlFile').on('change', addRetailChnlInfTransfer.fileUpload);
        $('.btn-addTransferSubmit').on('click', addRetailChnlInfTransfer.addRetailChnlTransferCommit);
        $('.btn-check').on('click', addRetailChnlInfTransfer.intoUpdateRetailChnlCheckStat);
        $('.btn-checkStat-submit').on('click', addRetailChnlInfTransfer.updateRetailChnlCheckStatCommit);
        $('.btn-view').on('click', addRetailChnlInfTransfer.viewRetailChnlTransferDetail);
        $('.btn-edit').on('click', addRetailChnlInfTransfer.intoEditRetailChnlTransfer);
        $('.btn-edit-submit').on('click', addRetailChnlInfTransfer.editRetailChnlTransferCommit);
        $('.btn-delete').on('click', addRetailChnlInfTransfer.intoDeleteRetailChnlTransfer);
        $('.btn-delete-submit').on('click', addRetailChnlInfTransfer.deleteRetailChnlTransferCommit);
    },
    intoAddRetailChnlTransfer : function() {
        var orderId = $(this).attr("orderId");
        $("#orderId").val(orderId);
        $("#btn-submit").addClass("btn-add-submit");
        $(".btn-add-submit").attr("onclick","addRetailChnlInfTransfer.addRetailChnlTransfer();");
        $("#commodityInfModal_h").text("添加入账信息");
        $('#addTransferModal').modal({
            backdrop : "static"
        });
    },
    fileUpload : function () {
        var imgUrl = $("#evidenceUrlFile").val();
        $("#evidenceUrl").val(imgUrl);
    },
    addRetailChnlTransfer:function(){
        var channelId = $("#channelId").val();
       /* var remitAmt = $("#remitAmt").val();
        var evidenceUrl = $("#evidenceUrl").val();*/
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
        /*if(remitAmt=='' || remitAmt == '0'){
            Helper.alert("打款金额不能为空");
            return false;
        }*/
        /*if(evidenceUrl=='' || evidenceUrl == '0'){
            Helper.alert("打款凭证不能为空");
            return false;
        }*/
        if(inaccountAmt=='' || inaccountAmt == '0'){
            Helper.alert("上账金额不能为空");
            return false;
        }
        if (A00 == '' && B01 == '' && B02 == '' && B03 == '' && B04 == '' && B05 == '' && B06 == '' && B07 == '' && B08 == '') {
            Helper.alert("必须有一个专项账户金额不能为空");
            return false;
        }
        /*if ((inaccountAmt - 0) - (remitAmt - 0) > 0) {
            Helper.alert("上账金额不能大于打款金额");
            return false;
        }*/
        var sum = (A00 - 0) + (B01 - 0) + (B02 - 0) + (B03 - 0) + (B04 - 0) + (B05 - 0) + (B06 - 0) + (B07 - 0) + (B08 - 0);
        if ((sum - 0) - (inaccountAmt - 0)  > 0) {
            Helper.alert("所有专项金额总和不能大于上账金额");
            return false;
        }
        $.ajax({
            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/addRetailChnlTransfer.do',
            type: 'post',
            data: new FormData($("#addTransferFrom")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType : "json",
            success: function (result) {
                if(result.status) {
                    location = Helper.getRootPath() + '/retailChnl/retailChnlInf/intoAddRetailChnlTransfer.do?operStatus=1&channelId='+channelId;
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
    addRetailChnlTransferCommit : function () {
        var orderId = $(this).attr("orderId");
        var channelId = $('#channelId').val();
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/addRetailChnlTransferCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId,
                "channelId": channelId
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
    },
    intoUpdateRetailChnlCheckStat : function () {
        var orderId = $(this).attr("orderId");
        $("#checkStatOrderId").val(orderId);
        $('#updateCheckStatModal').modal({
            backdrop : "static"
        });
    },
    updateRetailChnlCheckStatCommit : function () {
        var orderId = $("#checkStatOrderId").val();
        var channelId = $('#channelId').val();
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/updateRetailChnlCheckStatCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId,
                "channelId": channelId
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
    },
    viewRetailChnlTransferDetail : function () {
        var orderId = $(this).attr("orderId");
        var url = Helper.getRootPath()+"/retailChnl/retailChnlInf/viewRetailChnlTransferDetail.do?orderId="+orderId;
        location.href=url;
    },
    intoEditRetailChnlTransfer : function () {
        var orderId = $(this).attr("orderId");
        $("#orderId").val(orderId);
        $.ajax({
            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/intoEditRetailChnlTransfer.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId
            },
            success: function (data) {
                if(data.status){
                    /*$("#remitAmt").val(data.order.remitAmt);*/
                    $("#inaccountAmt").val(data.order.inaccountAmt);
                    /*$("#evidenceUrl").val(data.order.evidenceUrl);*/
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
        $(".btn-edit-submit").attr("onclick","addRetailChnlInfTransfer.editRetailChnlTransferCommit();");
        $("#commodityInfModal_h").text("编辑入账信息");
        $('#addTransferModal').modal({
            backdrop : "static"
        });
    },
    editRetailChnlTransferCommit : function () {
        var orderId = $("#orderId").val();
        var chnannelId = $("#chnannelId").val();
        /*var remitAmt = $("#remitAmt").val();
        var evidenceUrl = $("#evidenceUrl").val();*/
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
        /*if(remitAmt=='' || remitAmt == '0'){
            Helper.alert("打款金额不能为空");
            return false;
        }
        if(evidenceUrl==''){
            Helper.alert("打款凭证不能为空");
            return false;
        }*/
        if(inaccountAmt=='' || inaccountAmt == '0'){
            Helper.alert("上账金额不能为空");
            return false;
        }
        if (A00 == '' && B01 == '' && B02 == '' && B03 == '' && B04 == '' && B05 == '' && B06 == '' && B07 == '' && B08 == '') {
            Helper.alert("必须有一个专项账户金额不能为空");
            return false;
        }
        /*if ((inaccountAmt - 0) - (remitAmt - 0) > 0) {
            Helper.alert("上账金额不能大于打款金额");
            return false;
        }*/
        var sum = (A00 - 0) + (B01 - 0) + (B02 - 0) + (B03 - 0) + (B04 - 0) + (B05 - 0) + (B06 - 0) + (B07 - 0) + (B08 - 0);
        if ((sum - 0) - (inaccountAmt - 0)  > 0) {
            Helper.alert("所有专项金额总和不能大于上账金额");
            return false;
        }
        $.ajax({
            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/editRetailChnlTransfer.do',
            type: 'post',
            data: new FormData($("#addTransferFrom")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType : "json",

            success: function (result) {
                if(result.status) {
                    location = Helper.getRootPath() + '/retailChnl/retailChnlInf/intoAddRetailChnlTransfer.do?operStatus=1&channelId='+channelId;
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
    intoDeleteRetailChnlTransfer : function () {
        var orderId = $(this).attr("orderId");
        $("#transferOrderId").val(orderId);
        $('#deleteTransferModal').modal({
            backdrop : "static"
        });
    },
    deleteRetailChnlTransferCommit : function () {
        var orderId = $("#transferOrderId").val();
        var providerId = $("#providerId").val();
        $.ajax({
            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/deleteRetailChnlTransfer.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId
            },
            success: function (result) {
                if(result.status) {
                    location = Helper.getRootPath() + '/retailChnl/retailChnlInf/intoAddRetailChnlTransfer.do?operStatus=4&channelId='+channelId;
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

