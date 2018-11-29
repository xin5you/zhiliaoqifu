(function(global, context, factory){
    var Cart = factory.call(global, context);
    if(!context.Modules)context.Modules = {};
    if(!context.Modules.Cart)context.Modules.Cart = {};
    return context.extend(context.Modules.Cart, Cart).init();
})(window, Marklbp, function(context){
    return {
        cartContainers: $("#cart_container,#cart_edit_container"),
        context: context,
        updateCartCallbacks: [],
        updateCartBefore: [],
        init: function(){
            //初始化购物车
            this.initCartContainer();
            // 绑定checkbox的change事件
            this.bindCheckboxChange();
            // 激活左滑删除操作
            this.slider()
            // 加减
            this.volumeChange(false);
            return this;
        },
        initCartContainer: function(){
            this.cartContainers[0].innerHTML = context.template(this.cartTemplate, {isEdit: false});
            return this;
        },
        slider: function(){
            var that = this;
            var touchStart = function (ev){
                ev= ev || event
                //tounches类数组，等于1时表示此时有只有一只手指在触摸屏幕
                if(ev.touches.length == 1){
                    // 记录开始位置
                    this.startX = ev.touches[0].clientX;
                }
            },
            touchMove = function(ev, remove){
                ev = ev || event;
                //获取删除按钮的宽度，此宽度为滑块左滑的最大距离
                var wd = remove.offsetWidth;
                if(ev.touches.length == 1) {
                    // 滑动时距离浏览器左侧实时距离
                    this.moveX = ev.touches[0].clientX
                      //起始位置减去 实时的滑动的距离，得到手指实时偏移距离
                    this.disX = this.startX - this.moveX;
                      //console.log(this.disX)
                      // 如果是向右滑动或者不滑动，不改变滑块的位置
                    if(this.disX < 0 || this.disX == 0) {
                        this.style.transform = "translateX(0px)";
                      // 大于0，表示左滑了，此时滑块开始滑动 
                    }else if (this.disX > 0) {
                        //具体滑动距离我取的是 手指偏移距离*5。
                        this.style.transform = "translateX(-" + this.disX*5 + "px)";
                         
                        // 最大也只能等于删除按钮宽度 
                        if (this.disX*5 >=wd) {
                          this.style.transform = "translateX(-" +wd+ "px)";
                        }
                    }
                }
            },
            touchEnd = function(ev, remove){
                 ev = ev || event;
                 var wd = remove.offsetWidth;
                 if (ev.changedTouches.length == 1) {

                    var endX = ev.changedTouches[0].clientX;
                      
                    this.disX = this.startX - endX;
                    //console.log(this.disX)
                    //如果距离小于删除按钮一半,强行回到起点
                   
                    if ((this.disX) < (wd/2)) {
                    
                      this.style.transform = "translateX(0px)";
                    }else{
                      //大于一半 滑动到最大值
                      //this.deleteSlider = "transform:translateX(-"+wd+ "px)";
                      this.style.transform = "translateX(-"+wd+ "px)";
                    }
                }
            },
            removeFn = function(e, isEditDelete){
                var cartSlider = $(this).parents(".cart-slider");
                var spuid = this.getAttribute("data-spuid");
                var cartSliderParent = cartSlider.parent();
                var cartSliderParentSiblings = cartSliderParent.siblings(".module_first");
                var cartSliderSiblings = cartSlider.siblings(".cart-slider");
                var callback =  that.updateCartTotalPrice;
                if(cartSliderSiblings.length <= 0){
                    cartSliderParent.remove();
                    if(cartSliderParentSiblings.length <= 0){
                        //清空购物车后无需异步更新页面，直接刷新页面
                        callback = function(){};
                        //当清空了购物车时重载页面
                        window.location.reload();
                    }
                }else{
                	cartSlider.remove();
                }
                /*
                 *预留更新购物车操作，非编辑删除情形更新购物车
                */
//               if(!isEditDelete){
//                    that.updateCart([spuid], 0, callback,"DEL");
//                }
            }
            
            $(document).on("touchstart touchmove touchend", ".cart-slider>.cart-slider-content", function(e){
                var remove;
                switch(e.type){
                    case "touchstart":
                        touchStart.call(this, e);
                    break;
                    case "touchmove":
                        remove = $(this).next().find(".remove")[0];
                        touchMove.call(this, e, remove);
                    break;
                    case "touchend":
                        remove = $(this).next().find(".remove")[0];
                        touchEnd.call(this, e, remove);
                    break;
                }
            })
            .on("click", ".cart-slider>.cart-slider-action>.remove", function(e, isDelAll){
                var isEditDelete = this.getAttribute("data-edit") == "true";
                var that = this;
                if(!isEditDelete||!isDelAll){
                	Marklbp.Modules.Cart.updateCartBefore.push(function(){
                		//菊花展示
                	})
                	Marklbp.Modules.Cart.updateCart([this.getAttribute("data-spuid")], 0, Marklbp.Modules.Cart.updateCartTotalPrice,"DEL");
                	Marklbp.Modules.Cart.updateCartCallbacks.push(function(){
                		removeFn.call(that, e, isEditDelete)
                	})
                }else{
                	/*总删除*/
                	removeFn.call(that, e, isEditDelete)
                }
            })
            return this;  
        },

        bindCheckboxChange: function(){
            var that = this;
            this.cartContainers.each(function(){
                $(this).on("change", ".check-all, .check-single", function(){
                    if(this.className.indexOf("check-all") > -1){
                        // 绑定全选事件
                        that.selectAll(this);
                      
                    }else{
                        // 绑定单选事件
                        that.selectSingle(this)
                    }
                    return false;
                })
            })
            return this;
        },

        getCheckSingles: function(checkbox){ //单选
            return $(checkbox).parents(".cart-container").find(".check-single");
        },

        selectAll: function (checkbox) { //选中所有
            var checkboxSingle = this.getCheckSingles(checkbox);
            var spuIds=[];
            for (var i=0, length = checkboxSingle.length; i < length; i++) {
            	spuIds.push(checkboxSingle[i].getAttribute("data-spuid"));
                checkboxSingle[i].checked = checkbox.checked;
            }
            $(document.getElementById("go2order"))[checkbox.checked ? "removeClass" : "addClass"]("disable");
            this.updateCart(spuIds, checkbox.checked, this.updateCartTotalPrice,"AC");
            return this;
        },

        selectSingle: function(checkbox){
            var checkboxAll = $(checkbox).parents(".cart-container").find(".check-all")[0];
            var uncheckbox = this.hasOthersNoChecked(checkbox);
            checkboxAll.checked = uncheckbox ? uncheckbox.checked : checkbox.checked;
            /*if(!uncheckbox){
                $(document.getElementById("go2order"))[checkbox.checked ? "addClass" : "removeClass"]("disable");
            }else{
                if(checkbox.checked){
                    $(document.getElementById("go2order"))[checkbox.checked ? "addClass" : "removeClass"]("disable");
                }else{
                    if(this.hasOhtersOneChecked(checkbox)){
                        $(document.getElementById("go2order")).addClass("disable");
                    }else{
                        $(document.getElementById("go2order")).removeClass("disable");
                    }
                }
            }*/
            var hasOhtersOneChecked = this.hasOhtersOneChecked(checkbox);
            $(document.getElementById("go2order"))[hasOhtersOneChecked || checkbox.checked ? "removeClass" : "addClass"]("disable");
            var spuids=$(checkbox).attr("data-spuid");
            this.updateCart(spuids, checkbox.checked, this.updateCartTotalPrice,"SC");
            return this;
        },
        hasOhtersOneChecked: function(checkbox){
            //至少有一个选中
            var checkboxSingle = this.getCheckSingles(checkbox);
            for(var i = 0, length = checkboxSingle.length; i < length; i++){
                if(checkboxSingle[i] != checkbox){
                    if(checkboxSingle[i].checked)return checkboxSingle[i];
                }
            }
            return false;
        },
        hasOthersNoChecked: function(checkbox){
            // 至少有一个没选中
            var checkboxSingle = this.getCheckSingles(checkbox);
            for(var i = 0, length = checkboxSingle.length; i < length; i++){
                if(checkboxSingle[i] != checkbox){
                    if(!checkboxSingle[i].checked)return checkboxSingle[i];
                }
            }
            return false;
        },

        showEdit: function(){
            var CartEditDom = this.cartContainers[1];
            var template = this.context.template(this.editTemplate, {isEdit: true})
            document.getElementById("cart_edit_container_content").innerHTML = template;
            CartEditDom.style.display = "block";
            document.body.style.overflow = "hidden";
            return this;
        },

        hideEdit: function(){
            document.body.style.overflow = "auto";
            this.cartContainers[1].style.display = "none";
        },
        // 编辑总删除按钮事件
        editRemove: function(){
            var checkeds = this.cartContainers.last().find(".check-single:checked");
            var spuids = [];
            if(checkeds.length <= 0)return Marklbp.modal.tip("您还没有选择商品哦～", 500, true);
            checkeds.each(function(){
                spuids.push(this.getAttribute("data-spuid"));
                var checkbox = this;
                // 编辑删除只删除dom节点
                Marklbp.Modules.Cart.updateCartCallbacks.push(function(){
                	$(checkbox).parents(".cart-slider").find(".cart-slider-action>.remove").trigger("click", true)
                })
            })
            this.updateCartBefore.push(function(){
            	//菊花展示
            })
            this.updateCart(spuids, 0, this.updateCartDom,"DEL");
            //  编辑删除处理
        },
        //
        volumeChange: function(e){
            var that = this;
            function t() {
                var e = $(this).siblings(".volume_input")[0],
                      t = parseInt(e.value),
                      value = 0;
                if(this.className.indexOf("reduce") > -1) {
                	value = a(t - 1, e);
                }else{
                	 value = a(t + 1, e);
                }
                e.value = value
                return value;
            }

            function a(e, input) {
                var v = e <= 1 || !e ? 1 : e;
                var existValue = parseInt( input.getAttribute("data-isstore"));
                if(existValue<v){
                	jfShowTips.toastShow({'text' : '库存不足'});
                	v = Math.min(v, existValue);
                }
                if(v>200){
                	jfShowTips.toastShow({'text' : '最大购买200件商品'});
                	v = Math.min(v, 200);
                }
                return v;
            }

            function n() {
                window.scrollTo(0, i), s.call(this)
            }

            function s() {
                this.value = a(this.value, this)
            }

            function o() {
                i = document.body.scrollTop, setTimeout(function() {
                    window.scrollTo(0, document.body.scrollHeight)
                }, 300)
            }

            $(document).on("touchstart", ".volume_btn>.reduce, .volume_btn>.add", function(e){
                var value = t.call(this, e);
                var spuids = [this.getAttribute("data-spuid")];
                var isEdit = this.getAttribute("data-edit") == "true"
                that.updateCart(spuids, value, isEdit ? that.updateCartDom : that.updateCartTotalPrice,"VOL");
            }).on("blur", ".volume_btn>.volume_input", function(e){
                s.call(this, e);
                that.updateCart([this.getAttribute("data-spuid")], this.value, that.updateCartTotalPrice,"VOL");
            });

            if(!!navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/) && e){
                $(document).on("focus blur", ".volume_btn>.volume_input", function(e){
                    if(e.type == 'focus'){
                        o.call(this, e)
                    }else{
                        n.call(this, e);
                        that.updateCart([this.getAttribute("data-spuid")], this.value, that.updateCartTotalPrice,"VOL");
                    }
                })
            }
        },
        // 更新购物车
        updateCart: function(spuids, amount, ajaxcallback,action){
            if(!this.updateCart.cached)this.updateCart.cached = {};
            var lastAmount = 0;
            var checkVal=false;
            if(  action == "VOL"){   // 追加数量操作
                lastAmount = this.updateCart.cached[spuids[0]];
                if(lastAmount<=0){
                	return false;
                }
                if(lastAmount == amount && this.updateCart.cached.hasOwnProperty(spuids[0])){
                	return;
                }
                this.updateCart.cached[spuids[0]] = amount;
            }else if(  action == "DEL"){  // 删除操作
                for (var i = 0; i < spuids.length; i++) {
                    if(this.updateCart.cached.hasOwnProperty(spuids[i]))delete this.updateCart.cached[spuids[i]]
                }
            }else if(action == "SC" || action == "AC"  ){ //选择事件
            	checkVal=amount;
            }
            //预留ajax更新购物车数据
            //alert("预留ajax更新购物车数据" + "\n" + action + "\n" + spuids.join(",") + "\n" + "数量：" + (amount||0));
            this.cartOperCommit(action,checkVal,spuids.toString(),amount, ajaxcallback);
        },
        updateCartTotalPrice: function(){
            //在购物车里直接删除，需要更新总价
            $("#total_price").html(Marklbp.mock.cart.amount)
        },
        // 更新购物车dom节点
        updateCartDom: function(){
            //这里更新整个购物车
            this.initCartContainer();
        },
        //更新购物车
    	cartOperCommit:function(checkType,checkVal,cartId,productNum, callback){
    		var delLoading = [];
    		$.ajax({
                type: 'POST',
                url: Helper.getRootPath() + '/ecom/cart/cartOperCommit',
                data: {
                	checkType:checkType,
            		checkVal: checkVal,
            		cartId:cartId,
            		productNum:productNum
                },
                cache:false,
                dataType: 'json',
                beforeSend: function(){
                	var cb;
                	while(Marklbp.Modules.Cart.updateCartBefore.length > 0){
                		cb = Marklbp.Modules.Cart.updateCartBefore.shift();
                		delLoading.push(cb && cb());
                	}
                },
                complete: function(){
                	var cb;
                	while(delLoading.length > 0){
                		cb = delLoading.shift();
                		cb && cb();
                	}
                },
                success: function(data){
                    	if(data.code=='00'){
                    		for(var i = 0, cb; i < Marklbp.Modules.Cart.updateCartCallbacks.length; i++){
                    			cb = Marklbp.Modules.Cart.updateCartCallbacks[i];
                    			'function' == typeof cb && cb();
                    		}
                    		
                    		/*Marklbp.mock.cart.amount =data.object.cartsPrice; //总额
                    		Marklbp.mock.cart.list = data.object.cartList; //列表list*/
                            Marklbp.extend(Marklbp.mock.cart, {
                                amount: data.object.cartsPrice,
                                list: data.object.cartList
                            })
                    		callback && callback.call(Marklbp.Modules.Cart);
                    	}else{
                    		Marklbp.modal.tip(data.msg,   1000,    true)
                    	}
                    	Marklbp.Modules.Cart.updateCartCallbacks = [];
                },
    			error : function(){
    				Mark.Modules.Cart.updateCartCallbacks = [];
    				Marklbp.modal.tip('系统超时，请稍后再试',   1000,    true);
    			}
            });
    	}
        ,go2order: function(btn){
            // /ecom-front/ecom/cart/cartAccounts
            if(btn.className.indexOf('disable') > -1)return false;
        
            var checkeds = this.cartContainers.first().find(".check-single:checked");
            var spuids = [];
            if(checkeds.length <= 0)return Marklbp.modal.tip("您还没有选择商品哦～", 500, true);
            checkeds.each(function(){
                spuids.push(this.getAttribute("data-spuid"));
            })
           $("#cartIds").val(spuids.toString());
           if(spuids=="" ||spuids.length <= 0){
        	   Marklbp.modal.tip('购物车还没有选中的货品',   1000,    true);
        	   return false;
           }
            $("#cartForm").submit();
        }
    }
})