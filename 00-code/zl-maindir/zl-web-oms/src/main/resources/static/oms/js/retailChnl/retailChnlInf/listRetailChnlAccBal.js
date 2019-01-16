$(document).ready(function () {
    listRetailChnlAccBal.init();
})

var listRetailChnlAccBal = {
    init : function() {
        listRetailChnlAccBal.initEvent();
    },

    initEvent:function(){
        $('.btn-accBal').on('click', listRetailChnlAccBal.listRetailChnlAccBalDetail);
    },
    listRetailChnlAccBalDetail : function () {
        var bId = $(this).attr("bId");
        var channelId = $("#channelId").val();
        var url = Helper.getRootPath()+"/retailChnl/retailChnlInf/listRetailChnlAccBalDetail.do?bId="+bId+"&channelId="+channelId;
        location.href=url;
    }
};

