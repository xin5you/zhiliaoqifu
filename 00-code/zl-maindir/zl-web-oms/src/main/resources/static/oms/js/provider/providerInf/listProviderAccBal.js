$(document).ready(function () {
    listProviderAccBalDetail.init();
})

var listProviderAccBalDetail = {
    init : function() {
        listProviderAccBalDetail.initEvent();
    },

    initEvent:function(){
        $('.btn-accBal').on('click', listProviderAccBalDetail.listProviderAccBalDetail);
    },
    listProviderAccBalDetail : function () {
        var bId = $(this).attr("bId");
        var providerId = $("#providerId").val();
        var url = Helper.getRootPath()+"/provider/providerInf/listProviderAccBalDetail.do?bId="+bId+"&providerId="+providerId;
        location.href=url;
    }
};

