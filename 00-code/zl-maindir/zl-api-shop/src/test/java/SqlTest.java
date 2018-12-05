import com.ebeijia.zl.shop.ShopApp;
import com.ebeijia.zl.shop.core.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.core.goods.service.ITbEcomGoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApp.class)
public class SqlTest {
    @Autowired
    ITbEcomGoodsService goodsService;

    @Test
    public void sqlQuery(){
        System.out.println(goodsService.count());
        TbEcomGoods goods = new TbEcomGoods();
        goods.setGoodsId(UUID.randomUUID().toString());
        goods.setEcomCode(UUID.randomUUID().toString());
        goodsService.save(goods);
    }
}
