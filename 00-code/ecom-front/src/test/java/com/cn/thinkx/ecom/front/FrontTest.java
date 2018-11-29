package com.cn.thinkx.ecom.front;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cn.thinkx.ecom.FrontApp;
import com.cn.thinkx.ecom.basics.goods.service.ApiSpuService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=FrontApp.class)// 指定spring-boot的启动类
public class FrontTest {

	@Autowired
	private ApiSpuService apiSpuService;
	
	
//    @Test
//    public void insert() throws Exception  {
//    	ApiSpu apiSku=new ApiSpu();
//    	apiSku.setSpuCode("10002");
//    	apiSku.setEcomCode("40054006");
//    	apiSpuService.insert(apiSku);
//        System.out.println(apiSku.getId());
//    }
	
//    @Test
//    public void find() throws Exception  {
//    	ApiSpu apiSku=apiSpuService.selectByPrimaryKey("100004");
//        System.out.println(apiSku.toString());
//    }
    
//    
//    @Test
//    public void update() throws Exception  {
//    
//    	ApiSpu apiSku=apiSpuService.selectByPrimaryKey("100004");
//    	apiSku.setSpuCode("10003");
//    	apiSpuService.updateByPrimaryKey(apiSku);
//        System.out.println(apiSku.toString());
//    }
	
//	@Test
//	public void delete() throws Exception  {
//		apiSpuService.deleteByPrimaryKey("100004");
//	}
//         
}
