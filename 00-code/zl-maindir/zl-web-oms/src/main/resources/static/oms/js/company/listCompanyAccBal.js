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
        var isPlatform = $("#isPlatform").val();
        var url = Helper.getRootPath()+"/company/listCompanyAccBalDetail.do?companyId=" + companyId + "&bId=" + bId + "&isPlatform=" + isPlatform;
        location.href = url;
    }
}
