$(document).ready(function() {
    listPlatformCoupon.init();
})

var listPlatformCoupon = {

    init : function() {
        listPlatformCoupon.initEvent();
    },

    initEvent:function(){
        $('.btn-reset').on('click', listPlatformCoupon.searchReset);
    },
    searchReset:function(){
        location = Helper.getRootPath() + "/company/listPlatformCoupon.do";
    }
}
