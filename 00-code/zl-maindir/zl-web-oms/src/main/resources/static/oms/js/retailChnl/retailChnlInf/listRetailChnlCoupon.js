$(document).ready(function() {
    listRetailChnlCoupon.init();
})

var listRetailChnlCoupon = {

    init : function() {
        listRetailChnlCoupon.initEvent();
    },

    initEvent:function(){
        $('.btn-reset').on('click', listRetailChnlCoupon.searchReset);
        $('#selectAll').on('click', listRetailChnlCoupon.selectAll);
       /* $('.couponId').on('click', listRetailChnlCoupon.selectFirst);*/
        $('.btn-back').on('click', listRetailChnlCoupon.backRetailChnlInf);
        $('.btn-submit').on('click', listRetailChnlCoupon.buyCouponCommit);
    },
    searchReset:function(){
        var channelId = $("#channelId").val();
        var url = Helper.getRootPath()+"/retailChnl/retailChnlInf/listRetailChnlCoupon.do?transStat=1&channelId="+channelId;
        location.href = url;
    },
    selectAll : function () {
        if ($("#selectAll").attr("checked")) {
            $(":checkbox").attr("checked", true);
        } else {
            $(":checkbox").attr("checked", false);
        }
    },
    backRetailChnlInf : function () {
        var url = Helper.getRootPath()+"/retailChnl/retailChnlInf/listRetailChnlInf.do";
        location.href = url;
    },
    buyCouponCommit : function () {
        $(".btn-submit").attr('disabled',"true");
        var channelId = $("#channelId").val();
        var couponIds = new Array();
        $("input:checkbox[name='couponId']:checked").each(function() {
            console.log($(this).val());
            couponIds.push($(this).val());
        });
        $.ajax({
            url: Helper.getRootPath() + '/retailChnl/retailChnlInf/buyCouponCommit',
            type: 'post',
            dataType : "json",
            data: {
                channelId: channelId,
                couponIds: couponIds
            },
            success: function (result) {
                $(".btn-submit").removeAttr("disabled");
                if(result.status){
                    location.href = Helper.getRootPath() + "/retailChnl/retailChnlInf/listRetailChnlCoupon.do?transStat=1&channelId="+channelId;
                }else{
                    Helper.alert(result.msg);
                    return false;
                }
            },
            error:function(){
                $(".btn-submit").removeAttr("disabled");
                Helper.alert("网络异常，请稍后再试");
                return false;
            }
        });
    }
}
