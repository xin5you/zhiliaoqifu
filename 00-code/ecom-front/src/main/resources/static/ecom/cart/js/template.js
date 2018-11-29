(function(global, context, factory){
    if(!context.Modules)context.Modules = {};
    if(!context.Modules.Cart)context.Modules.Cart = {};
    return context.extend(context.Modules.Cart, factory.call(global));
})(window, Marklbp, function (){

    var editTemplate = '\
        <%\
           $.each(Marklbp.mock.cart.list, function(i, ecom){\
            var icon = ecom.esetting.ecomName;\
            var type2cn = ecom.esetting.shopDesc;\
            var carts = ecom.ecomCarts;\
        %>\
        <div class="module_first">\
            <!--品牌及包邮信息-->\
            <div class="cost_express">\
    	 		<span class="express_state"><%=icon%></span>\
                <span class="express_state"><%=type2cn%></span>\
            </div>\
        <%\
            $.each(carts, function(j, cart){\
                var spuid = cart.cartId; \
                var main_image = cart.picurl;\
                var skus = cart.pageTitle;\
                var name = cart.goodsName;\
                var unit ="￥" ;\
                var price = cart.goodsPrice;\
    			var isCheck = cart.isCheck;\
    			var isValid = cart.marketEnable == 1;\
    			var isStore=cart.isStore;\
    			if(isCheck ==1){\
    				Marklbp.mock.cart.checkedAll = false;\
    			}else{\
                    Marklbp.mock.cart.hasChecked = true;\
                }\
                var amount = cart.productNum;\
                var url = "/ecom-front/ecom/goods/detail?goodsId=&productId="+cart.productId || "#"\
        %>\
            <div class="cart-slider <%=!isValid&&!isEdit ? \"disable\" : \"\"%>">\
                <div class="cart-slider-content">\
                    <div class="edit_product">\
    					<%if(!(!isValid&&!isEdit)){%>\
                        <label class="checkbox-radio">\
                            <input type="checkbox" name="cartChecked" data-edit="<%=isEdit%>" class="check-single" <%=!isEdit && isCheck =="0" ? "checked" : ""%> data-spuid="<%=spuid%>">\
                            <i></i>\
                        </label><%}%>\
        <%  if(isEdit){%>\
                        <a class="product_minute" onclick="void(0)">\
        <%}else{%>\
                        <a class="product_minute"  href="<%=url%>">\
        <%}%>\
                            <div class="product_img">\
                                <img src="<%=main_image%>" class="product_small_img">\
                            </div>\
                            <div class="minute_text">\
                                <!--商品名称-->\
                                <div class="title"><%=name%></div>\
                                <!--SKU信息-->\
                                <div class="sku_minute"><%=skus %></div>\
                                <!--价格及加减数量-->\
                                <div class="adjust_number">\
                                    <div class="price"><span><%=unit%></span><%=price%></div>\
                                </div>\
                            </div>\
                        </a>\
    	<%if(isValid){%>\
                        <div class="volume_btn">\
                            <span class="reduce <%=!isValid&&!isEdit ? \"disable\" : \"\"%>" data-spuid="<%=spuid%>" data-edit="<%=isEdit%>">-</span>\
                            <input type="number" pattern="[0-9]*" <%=!isValid&&!isEdit ? \"disable\" : \"\"%> data-spuid="<%=spuid%>" max="<%=isStore%>" data-isStore="<%=isStore%>" class="volume_input" value="<%=amount%>" data-edit="<%=isEdit%>">\
                            <span class="add <%=!isValid&&!isEdit ? \"disable\" : \"\"%>" data-spuid="<%=spuid%>" data-edit="<%=isEdit%>">+</span>\
                        </div><%}%>\
                    </div>\
                </div>\
                <div class="cart-slider-action">\
                  <div class="xicon remove" data-spuid="<%=spuid%>" data-edit="<%=isEdit%>">删除</div>\
                </div>\
            </div>\
        <%  }) %>\
        </div>\
        <%})%>';

    var cartTemplate = '\
    	<!-- 顶部导航 -->\
        <nav class="hkb_top_bar">\
            <div class="hkb_top_contain">\
                <div class="content">\
                    <a class="left" onclick="window.history.go(-1)">\
                        <p class="back"><img src="/ecom-front/front/images/hkbstore/icons/nav_go_back.png"></p>\
                    </a>\
                    <div class="center">\
                        购物车\
                    </div>\
                    <div class="right">\
        <%\
           if(Marklbp.mock.cart.list.length > 0){\
        %>\
                        <span ontouchstart="Marklbp.Modules.Cart.showEdit()">编辑</span>\
        <%}%>\
                    </div>\
                </div>\
            </div>\
        </nav>\
        <!--模块（1）-->\
        <% \
           if(Marklbp.mock.cart.list.length > 0){\
        %>\
        <!--注释-->\
        <a class="hkb_hint">\
            <div class="annotation">\
                <img src="" alt="">\
                <span>\
                    <p>此商品信息均为不含邮费信息，不同商城运费不一致，\
                        每个商城的信息以商城免邮规则计算邮费\
                    </p>\
                </span>\
            </div>\
        </a>\
        '
        + editTemplate +
        '<!-- 编辑 -->\
        <div class="operation_tab_text">\
            <div class="hkb_alignment_left">\
                <label class="checkbox-radio">\
                    <input type="checkbox" class="check-all" <%=Marklbp.mock.cart.checkedAll ? "checked" :  ""%>>\
                    <i></i>\
                    <span>全选</span>\
                </label>\
                <div class="hkb_price">合计:\
                    <span class="font_red">￥<strong id="total_price"><%=Marklbp.mock.cart.amount%></strong></span>\
                    <p>不含运费</p>\
                </div>\
            </div>\
            <a id="go2order" class="operation_btn<%=!Marklbp.mock.cart.hasChecked ? \" disable\" : \"\"%>" onclick="Marklbp.Modules.Cart.go2order(this)">结算</a>\
        </div>\
        <%}else{%>\
        <div class="hkb_card_blank">\
            <p class="default-text"><i class="fa fa-shopping-cart"></i>购物车是空的</p>\
        </div>\
        <%}%>';

    return {
        cartTemplate: cartTemplate,
        editTemplate: editTemplate
    }
})
