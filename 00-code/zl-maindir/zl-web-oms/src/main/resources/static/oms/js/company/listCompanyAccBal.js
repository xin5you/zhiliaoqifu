$(document).ready(function() {
    listCompanyAccBal.init();
})

var listCompanyAccBal = {

    init : function() {
        listCompanyAccBal.initEvent();
    },

    initEvent:function(){
        $('.btn-accBal-detail').on('click', listCompanyAccBal.listCompanyAccBalDetail);
    },
    listCompanyAccBalDetail : function () {
        var bId = $(this).attr('bId');
        var companyId = $("#companyId").val();
        var url = Helper.getRootPath()+"/company/listCompanyAccBalDetail.do?companyId=" + companyId + "&bId=" + bId;
        location.href = url;
    }
}
