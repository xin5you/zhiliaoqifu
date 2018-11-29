$(function(){
    var loginUser = {
        login: function(){
            var messageLabel = $("#errorcontent");
            messageLabel.html("");
            var obj = {username:$("#username").val(), password:$("#password").val(), authCode:$("#authCode").val()};
            if(obj.username==''){
                messageLabel.html("<span style='color:red'>账号不能为空</span>");
                return false;
            }
            if(obj.password==''){
                messageLabel.html("<span style='color:red'>密码不能为空</span>");
                return false;
            }
            if(obj.authCode==''){
                messageLabel.html("<span style='color:red'>验证码不能为空</span>");
                return false;
            }

    		var md5_val = $.md5($.md5(obj.password) + obj.authCode);
    		$("#password").val(md5_val)
            $.ajax({
                url: Helper.getRootPath() + '/doLogin',
                type: 'post',
                dataType : "json",
                data: {
                    userName: obj.username,
                    md5Code: md5_val,
                    authCode: obj.authCode
                },
                success: function (data) {
                    if (data.code == '05'){
                    	Helper.confirm_one('无权限', function(){
                    		document.location.href = Helper.getRootPath() + "/login";
                        });
                    } else if (data.code == '00') {
                    	messageLabel.html("<span style='color:limegreen'>" + data.msg + "</span>");
                        document.location.href = Helper.getRootPath() + "/main";
                    } else if (data.code == '01') {
                    	loginUser.genAuthCodeImg();
                        $(".btn-submit").removeAttr('disabled');
                        messageLabel.html("<span style='color:red'>" + data.msg + "</span>");
                    } else if (data.code == '02') {
                    	loginUser.genAuthCodeImg();
                        $(".btn-submit").removeAttr('disabled');
                        messageLabel.html("<span style='color:red'>" + data.msg + "</span>");
                    } else if (data.code == '03') {
                    	loginUser.genAuthCodeImg();
                        $(".btn-submit").removeAttr('disabled');
                        messageLabel.html("<span style='color:red'>" + data.msg + "</span>");
                    }else if (data.code == '04') {
                    	loginUser.genAuthCodeImg();
                        $(".btn-submit").removeAttr('disabled');
                        messageLabel.html("<span style='color:red'>" + data.msg + "</span>");
                    } else{
                    	loginUser.genAuthCodeImg();
                        $(".btn-submit").removeAttr('disabled');
                        messageLabel.html("<span style='color:red'>" + data.msg + "</span>");
                    }
                },
                error : function(){
					Helper.alert(data.msg);
				}
            });
        },
         genAuthCodeImg:function(){
        	 var loginImg = Helper.genAuthCode(1);
 			 $("#authCodeImg").attr('src', loginImg);
         }
    }

    var form_wrapper = $('.login_box');

    function boxHeight() {
        form_wrapper.animate({
            marginTop : (-(form_wrapper.height() / 2) - 24)
        }, 400);
    }

    form_wrapper.css({
        marginTop : (-(form_wrapper.height() / 2) - 24)
    });

    $('.btn-submit').on('click', loginUser.login);
    
    $('.text-login').keyup(function(event){
    	  if(event.keyCode ==13){
    		  loginUser.login();
    	  }
    });
    
    $('.auth-code-img').on('click', loginUser.genAuthCodeImg);
    loginUser.genAuthCodeImg();
});