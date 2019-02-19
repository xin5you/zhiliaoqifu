$(document).ready(function() {
    listRetailChnlCoupon.init();
})

var listRetailChnlCoupon = {

    init : function() {
        listRetailChnlCoupon.initEvent();
    },

    initEvent:function(){
        $('.btn-reset').on('click', listRetailChnlCoupon.searchReset);
        $('.btn-buy').on('click', listRetailChnlCoupon.intoBuyCoupon);
        $('.btn-back').on('click', listRetailChnlCoupon.backRetailChnlInf);
        $('.btn-submit').on('click', listRetailChnlCoupon.buyCouponCommit);
    },
    searchReset:function(){
        var channelId = $("#channelId").val();
        var url = Helper.getRootPath()+"/retailChnl/retailChnlInf/listRetailChnlCoupon.do?transStat=1&channelId="+channelId;
        location.href = url;
    },
    intoBuyCoupon : function () {
        $("#buyCouponFrom").get(0).reset();
        var couponType = $("#couponType").val();
        if (couponType != null && couponType != '') {
            $.ajax({
                url: Helper.getRootPath() + '/retailChnl/retailChnlInf/toBuyCoupon',
                type: 'post',
                dataType : "json",
                data: {
                    couponType: couponType
                },
                success: function (result) {
                    if(result.status){
                        var html = "";
                        var couponProductList = result.couponProductList;
                        var totalNum = result.totalNum;
                        var totalAmount = result.totalAmount;
                        var couponType = result.couponType;
                        for(var i = 0; i < couponProductList.length; i++){
                            html += "<tr>" +
                                "<td>" +
                                "<img th:src=\"${couponProductList[i].iconImage}\" style=\"height: 50px; width: 50px;\"/>" +
                                "</td>" +
                                "<td>￥couponProductList[i].tagAmount</td>" +
                                "<td>" +
                                "<input type=\"text\" value=\"couponProductList[i].totalNum\" class=\"span2\"/>" +
                                "库存剩余couponProductList[i].totalNum张" +
                                "</td>" +
                                "<td>￥couponProductList[i].totalAmount(￥couponProductList[i].price*couponProductList[i].totalNum张)</td>" +
                                "</tr>";
                        }
                        html += "<tr>" +
                            "<th>合计</th>" +
                            "<th colspan=\"2\">totalNum张</th>" +
                            "<th>￥totalAmount</th>" +
                            "</tr>";
                        $("#couponTbody").html(html);
                        $("#couponType").val(couponType);
                    }else{
                        Helper.alert(result.msg);
                        return false;
                    }
                },
                error:function(){
                    Helper.alert("网络异常，请稍后再试");
                    return false;
                }
            });
        }
        $('#buyCouponModal').modal({
            backdrop : "static"
        });
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
