$(document).ready(function() {
	addGoodsDetailIntro.init();
    var ue = UE.getEditor('container');
    ue.ready(function() {
        //设置编辑器的内容
        var intro = $('#intro').val();
        if (intro == null || intro == '') {
            ue.setContent('请在此处输入内容');
        } else {
            ue.setContent(intro);
        }
        //获取html内容，返回: <p>hello</p>
        //var html = ue.getContent();
        //获取纯文本内容，返回: 请在此处输入内容
        //var txt = ue.getContentTxt();
    });
    UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
    UE.Editor.prototype.getActionUrl = function(action){
        if (action == 'uploadImage' ||action== 'uploadscrawl' || action == 'uploadimage') {
            var detailId = $("#detailId").val();
            return Helper.getRootPath() + '/ueditor/uploadImage?detailId='+ detailId;
        } else if(action =='uploadvideo') {
            return "${basePath}/upload";
        } else if(action == 'listimage'){
            return "${basePath}/download";
        } else{
            return this._bkGetActionUrl.call(this, action);
        }
    }
})

var addGoodsDetailIntro = {
	init : function() {
        addGoodsDetailIntro.initEvent();
	},

	initEvent:function(){
		$('.btn-add-detailIntro').on('click', addGoodsDetailIntro.addGoodsDetailIntro);
        $('.btn-backGoodsDetail').on('click',addGoodsDetailIntro.intoBackGoodsDetail);
	},
    addGoodsDetailIntro:function(){
        var goodsId = $("#goodsId").val();
        var detailId = $("#detailId").val();
        var intro = UE.getEditor('container').getContent();
        $.ajax({
            type: 'POST',
            url: Helper.getRootPath() + '/goodsManage/goodsInf/addGoodsDetailIntro',
            dataType: 'json',
            data : {
                "detailId" : detailId,
                "intro" : intro
            },
            success: function(data) {
                $(".btn-submit").removeAttr("disabled");
                if (data.code == '00') {
                    Helper.confirm_one('新增商品详情富文本信息成功', function() {
                        Helper.post('/goodsManage/goodsInf/getGoodsDetailList?goodsId=' + goodsId);
                    });
                } else {
                    Helper.alert(data.msg);
                    return false;
                }
            },
            error : function() {
                Helper.alert("网络异常，请稍后再试");
            }
        });
    },
    intoBackGoodsDetail : function () {
        var goodsId = $('#goodsId').val();
        Helper.post('/goodsManage/goodsInf/getGoodsDetailList?goodsId=' + goodsId);
    }
}
