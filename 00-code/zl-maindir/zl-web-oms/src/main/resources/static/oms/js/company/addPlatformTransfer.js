$(document).ready(function () {
    addPlatformTransfer.init();
})

var addPlatformTransfer = {

    init : function() {
        addPlatformTransfer.initEvent();
    },

    initEvent:function(){
        $('.btn-addTransfer').on('click', addPlatformTransfer.intoAddPlatformTransfer);
        $('.btn-add-submit').on('click', addPlatformTransfer.addPlatformTransfer);
        $('#evidenceUrlFile').on('change', addPlatformTransfer.fileUpload);
        $('.btn-addTransferSubmit').on('click', addPlatformTransfer.addPlatformTransferCommit);
        $('.btn-check').on('click', addPlatformTransfer.intoUpdatePlatformCheckStat);
        $('.btn-checkStat-submit').on('click', addPlatformTransfer.updatePlatformCheckStatCommit);
        $('.btn-remit').on('click', addPlatformTransfer.intoAddPlatformRemit);
        $('.btn-remit-submit').on('click', addPlatformTransfer.addPlatformRemitCommit);
        $('.btn-view').on('click', addPlatformTransfer.viewPlatformTransferDetail);
        $('.btn-edit').on('click', addPlatformTransfer.intoEditPlatformTransfer);
        $('.btn-edit-submit').on('click', addPlatformTransfer.editPlatformTransferCommit);
        $('.btn-delete').on('click', addPlatformTransfer.intoDeletePlatformTransfer);
        $('.btn-delete-submit').on('click', addPlatformTransfer.deletePlatformTransferCommit);
        $(".evidenceUrlDiv").on('click', addPlatformTransfer.showEvidenceUrlDiv);
        $("#evidenceUrlDiv").on('click', addPlatformTransfer.showEvidenceUrlDiv);
    },
    intoAddPlatformTransfer : function() {
        $("#addTransferFrom").get(0).reset();
        $("#btn-submit").addClass("btn-add-submit");
        $(".btn-add-submit").attr("onclick","addPlatformTransfer.addPlatformTransfer();");
        $("#commodityInfModal_h").text("添加入账信息");
        $('#addTransferModal').modal({
            backdrop : "static"
        });
        $("#evidenceUrlDiv").hide();
    },
    fileUpload : function () {
        var imgUrl = $("#evidenceUrlFile").val();
        $("#evidenceUrl").val(imgUrl);
    },
    addPlatformTransfer:function(){
        var orderType = $("#orderType").val();
        var companyId = $("#companyId").val();
        var evidenceUrl = $("#evidenceUrl").val();
        var channelId = $("#channelId").val();
        var inaccountAmt = $("#inaccountAmt").val();
        var remarks = $("#remarks").val();
        /*var formula = $("#formula").val();*/
        var A00 = $("#A00").val();
        var B01 = $("#B01").val();
        var B02 = $("#B02").val();
        var B03 = $("#B03").val();
        var B04 = $("#B04").val();
        var B05 = $("#B05").val();
        var B06 = $("#B06").val();
        var B07 = $("#B07").val();
        var B08 = $("#B08").val();
        /*if (formula == '' || formula == null) {
            Helper.alert("请选择计算方式");
            return false;
        }*/
        /*if(remitAmt=='' || remitAmt == '0'){
            Helper.alert("打款金额不能为空");
            return false;
        }*/
        if(evidenceUrl=='' || evidenceUrl == '0'){
            Helper.alert("打款凭证不能为空");
            return false;
        }
        if(channelId=='' || channelId == '0'){
            Helper.alert("请选择收款分销商");
            return false;
        }
        if(inaccountAmt == '' || inaccountAmt == '0'){
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
        if ((sum - 0) != (inaccountAmt - 0)) {
            Helper.alert("所有专项金额总和必须等于上账金额");
            return false;
        }
        /*if ((sum - 0) != (remitAmt - 0)) {
            Helper.alert("所有专项金额总和必须等于打款金额");
            return false;
        }*/
        $.ajax({
            url: Helper.getRootPath() + '/company/addPlatformTransfer.do',
            type: 'post',
            data: new FormData($("#addTransferFrom")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType : "json",
            success: function (result) {
                if(result.status) {
                    location = Helper.getRootPath() + '/company/intoAddCompanyTransfer.do?operStatus=1&companyId='+companyId+"&orderType="+orderType;
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
    addPlatformTransferCommit : function () {
        var orderId = $(this).attr("orderId");
        var companyId = $('#companyId').val();
        var orderType = $('#orderType').val();
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/company/addPlatformTransferCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId,
                "companyId": companyId
            },
            success: function (data) {
                if(data.status){
                    location.href=Helper.getRootPath() + '/company/intoAddCompanyTransfer.do?operStatus=1&companyId='+companyId+"&orderType="+orderType;
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
    intoUpdatePlatformCheckStat : function () {
        var orderId = $(this).attr("orderId");
        $("#checkStatOrderId").val(orderId);
        $('#updateCheckStatModal').modal({
            backdrop : "static"
        });
    },
    updatePlatformCheckStatCommit : function () {
        var orderId = $("#checkStatOrderId").val();
        var companyId = $('#companyId').val();
        var orderType = $('#orderType').val();
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/company/updatePlatformCheckStatCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId,
                "companyId": companyId
            },
            success: function (data) {
                if(data.status){
                    location.href=Helper.getRootPath() + '/company/intoAddCompanyTransfer.do?operStatus=1&companyId='+companyId+"&orderType="+orderType;
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
    intoAddPlatformRemit : function () {
        var orderId = $(this).attr("orderId");
        $('#order_id').val(orderId);
        $('#addRemitModal').modal({
            backdrop : "static"
        });
    },
    addPlatformRemitCommit : function () {
        var orderId = $('#order_id').val();
        var companyId = $('#companyId').val();
        var orderType = $('#orderType').val();
        $('#msg').modal({
            backdrop : "static"
        });
        $.ajax({
            url: Helper.getRootPath() + '/company/updatePlatformRemitStatCommit.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId,
                "companyId": companyId
            },
            success: function (data) {
                if(data.status){
                    location.href=Helper.getRootPath() + '/company/intoAddCompanyTransfer.do?operStatus=1&companyId='+companyId+"&orderType="+orderType;
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
    viewPlatformTransferDetail : function () {
        var orderId = $(this).attr("orderId");
        var url = Helper.getRootPath()+"/company/viewPlatformTransferDetail.do?orderId="+orderId;
        location.href=url;
    },
    intoEditPlatformTransfer : function () {
        $("#addTransferFrom").get(0).reset();
        $("#evidenceUrlDiv").show();
        var orderId = $(this).attr("orderId");
        $("#orderId").val(orderId);
        $.ajax({
            url: Helper.getRootPath() + '/company/intoEditPlatformTransfer.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId
            },
            success: function (data) {
                if(data.status){
                    /*$("#remitAmt").val(data.order.remitAmt);*/
                    $("#inaccountAmt").val(data.order.inaccountSumAmt);
                    $("#channelId").val(data.order.providerId);
                    $("#evidenceUrlImg").attr("src", "data:image/jpg;base64,"+data.order.evidenceUrl);
                    $("#evidenceUrlDiv").attr("evidenceImage", data.order.evidenceUrl);
                    $("#evidenceUrl").val(data.order.evidenceUrl);
                    $("#remarks").val(data.order.remarks);
                    $.each(data.orderDetail, function (i, item) {
                        $('.span3[id=' + item.bid + ']').val(item.transAmt);
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
        $(".btn-edit-submit").attr("onclick","addPlatformTransfer.editPlatformTransferCommit();");
        $("#commodityInfModal_h").text("编辑入账信息");
        $('#addTransferModal').modal({
            backdrop : "static"
        });
    },
    editPlatformTransferCommit : function () {
        var orderType = $("#orderType").val();
        var orderId = $("#orderId").val();
        var companyId = $("#companyId").val();
        var evidenceUrl = $("#evidenceUrl").val();
        var channelId = $("#channelId").val();
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
        if(evidenceUrl==''){
            Helper.alert("打款凭证不能为空");
            return false;
        }
        if(channelId==''){
            Helper.alert("请选择收款分销商");
            return false;
        }
        if(inaccountAmt == '' || inaccountAmt == '0'){
            Helper.alert("上账金额不能为空");
            return false;
        }
        if (A00 == '' && B01 == '' && B02 == '' && B03 == '' && B04 == '' && B05 == '' && B06 == '' && B07 == '' && B08 == '') {
            Helper.alert("必须有一个专项账户金额不能为空");
            return false;
        }
        var sum = (A00 - 0) + (B01 - 0) + (B02 - 0) + (B03 - 0) + (B04 - 0) + (B05 - 0) + (B06 - 0) + (B07 - 0) + (B08 - 0);
        if ((sum - 0) != (inaccountAmt - 0)) {
            Helper.alert("所有专项金额总和必须等于上账金额");
            return false;
        }
        $.ajax({
            url: Helper.getRootPath() + '/company/editPlatformTransfer.do',
            type: 'post',
            data: new FormData($("#addTransferFrom")[0]),
            processData: false,
            contentType: false,
            async: false,
            cache: false,
            dataType : "json",
            success: function (result) {
                if(result.status) {
                    location.href=Helper.getRootPath() + '/company/intoAddCompanyTransfer.do?operStatus=1&companyId='+companyId+"&orderType="+orderType;
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
    intoDeletePlatformTransfer : function () {
        var orderId = $(this).attr("orderId");
        $("#transferOrderId").val(orderId);
        $('#deleteTransferModal').modal({
            backdrop : "static"
        });
    },
    deletePlatformTransferCommit : function () {
        var orderId = $("#transferOrderId").val();
        var companyId = $("#companyId").val();
        var orderType = $("#orderType").val();
        $.ajax({
            url: Helper.getRootPath() + '/company/deletePlatformTransfer.do',
            type: 'post',
            dataType : "json",
            data: {
                "orderId": orderId
            },
            success: function (result) {
                if(result.status) {
                    location.href=Helper.getRootPath() + '/company/intoAddCompanyTransfer.do?operStatus=1&companyId='+companyId+"&orderType="+orderType;
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
    showEvidenceUrlDiv : function () {
       var image = $(this).attr("evidenceImage");
       $("#bigImage").attr("src","data:image/jpg;base64,"+image);
       $('#imageModal').modal({
           backdrop : "static"
       });
    }
};

