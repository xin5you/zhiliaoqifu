$(document).ready(function () {
    addTelProviderInfTransfer.init();
})

var addTelProviderInfTransfer = {

    init : function() {
        addTelProviderInfTransfer.initEvent();
        var operStatus=$("#operStatus").val();
        Helper.operTip(operStatus);
    },

    initEvent:function(){
        $('.btn-addTransfer').on('click', addTelProviderInfTransfer.addProviderTransfer);
        $('.btn-addTransferSubmit').on('click', addTelProviderInfTransfer.addProviderTransferCommit);
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
        if (A00 == '' && B01 == '' && B02 == '' && B03 == '' && B04 == '' && B05 == '' && B06 == '') {
            Helper.alert("必须有一个专项账户金额不能为空");
            return false;
        }
        $.ajax({
            url: Helper.getRootPath() + '/provider/providerInf/addProviderTransfer.do',
            type: 'post',
            dataType : "json",
            data: {
                "providerId" : providerId,
                "remitAmt" : remitAmt,
                "evidenceUrl" : evidenceUrl,
                "companyCode" : companyCode,
                "inaccountAmt" : inaccountAmt,
                "remarks" : remarks,
                "A00" : A00,
                "B01" : B01,
                "B02" : B02,
                "B03" : B03,
                "B04" : B04,
                "B05" : B05,
                "B06" : B06
            },
            success: function (result) {
                if(result.status) {
                    location = Helper.getRootPath() + '/provider/providerInf/intoAddProviderTransfer.do';
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
        
    }
};

