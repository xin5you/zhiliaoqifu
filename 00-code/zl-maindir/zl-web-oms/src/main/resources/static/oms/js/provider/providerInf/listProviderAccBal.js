$(document).ready(function () {
    listProviderAccBal.init();
})

var listProviderAccBal = {
    init : function() {
        listProviderAccBal.initEvent();
    },

    initEvent:function(){
        $('.btn-accBal').on('click', listProviderAccBal.listProviderAccBalDetail);
    },
    listProviderAccBalDetail : function () {
        var bId = $(this).attr("bId");
        var providerId = $("#providerId").val();
        var url = Helper.getRootPath()+"/provider/providerInf/listProviderAccBalDetail.do?bId="+bId+"&providerId="+providerId;
        location.href=url;
    }
};

