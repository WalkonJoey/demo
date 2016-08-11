package com.hqdna.product.util.test;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hqdna.product.dao.IProductDao;
import com.hqdna.product.service.ICategoryService;
import com.hqdna.product.service.IProductService;
import com.hqdna.product.vo.ProductVo;
import com.hqdna.user.service.IUserService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config.xml")
public class ProductServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	@Resource(name = "userService")
	private IUserService userService;
	@Resource(name = "productService")//产品
	private IProductService productService;
	/*@Resource(name = "remoteService")
	private IRemoteFacade remoteService;*/
	@Resource(name = "productDao")
	private IProductDao productDao;
	@Resource(name = "categoryService")
	private ICategoryService categoryService;
	//@Test
	//public void testPoVo(){
		//获取发货单billStatus为已发货，关联的不相同的产品skuNo    
				/*List<String> deliveredSkus = deliveryService.getDeliveredDiffSkus();
				//比较库存里面那些skuNo是存在的，那些是不存在的，将不存在的创建一个新的库存
				List<String> stockedSkus = productStockService.getStockDiffSkus();
				List<String> needInStockSkus = new ArrayList<String>();
				for (String sku : deliveredSkus) {
					if(!stockedSkus.contains(sku)){
						needInStockSkus.add(sku);
					}
				}
				for (String sku : needInStockSkus) {
					int count = deliveryService.getSpecSkuNoSum(sku);
					System.out.println(count);
				}*/
		/*ExpressVo expressVo = expressService.getExpressVoByNo("YD21212121");
		System.out.println(expressVo.getExpressID());
		DeliveryVo deVo01 = deliveryService.getDeliveryByExpressID(expressVo.getExpressID());
		
		System.out.println(deVo01.getDeliveryNo());
		List<WareHouseEntryVo> list01 = deVo01.getWhEntryList();
		DeliveryVo deVo = deliveryService.getDeliveryByExpressID(10048);
		List<PurchaseOrderVo> list = deVo.getPoList();
		WareHouseEntryVo poVo = whEntryService.getWhEntryByNo("1415757768660");
		List<DeliveryVo> deList = poVo.getDeliveryVoList();
		if(poVo!=null)
			System.out.println();*/
		/*ProductVo productVo = remoteService.getProductInfo("1171200007");*/
		/*ProductVo productVo = productService.getProductBySkuNo("1421000005");
		remoteService.createProduct(productVo);
		System.out.println(productVo.getSkuNo());*/
		/*ProductVo rVo = remoteService.getProductInfo("1200700001");
		System.out.println(rVo.getSkuNo());
		ProductVo rVo01 = remoteService.getProductInfo("1171200007");
		System.out.println(rVo01.getSkuNo());
		ProductVo rVo02 = remoteService.getProductInfo("341050001");//1110300008
		System.out.println(rVo02.getSkuNo());*/
		/*ProductVo pVo = productService.getProductBySkuNo("1200700001");
		float b = (float)12.0;
		Object[] arr = {pVo.getAfterPackSize(),pVo.getBillStatus(),pVo.getCategoryID(),pVo.getCreateDt(),pVo.getDeclarePrice(),b};
		for (Object object : arr) {
			if(object instanceof Integer){
				System.out.println("int:"+object);
			}else if(object instanceof String){
				System.out.println("String:"+object);
			}else if(object instanceof Float){
				System.out.println("java.lang.Float:"+object);
			}else if(object instanceof java.lang.Byte){
				System.out.println("java.lang.Byte:"+object);
			}else if(object instanceof java.sql.Timestamp){
				System.out.println("java.sql.Timestamp:"+object);
			}else if(object instanceof java.math.BigDecimal){
				System.out.println("java.math.BigDecimal:"+object);
			}else{
				System.out.println("xxxx:"+object);
			}
			
		}*/
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = sdf.parse("2008-08-08");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*EntityManagerFactory factory = Persistence.createEntityManagerFactory("default_unit");
		factory.close();*/
		
	//}
	@Test
	public void testQueryProductBySkuNo() {
		
		/*String skuNo = "1000710042";
		ProductVo product = productService.getProductBySkuNo(skuNo);
		System.out.println();*/
		/*product.setSkuNo("123456789");
		product.setProductID(-1);
		UserInfo currentUser=new UserInfo();
		currentUser.setUserID("user00000000-0000-0000-0000-0000000aaron");
		int a = productService.addProduct(product, currentUser);
		System.out.println(a);
		ProductVo productVo = productService.getProductByID(a);
		Assert.assertNotNull(product);*/
		//productStockDao.getOmsTemplateStock();
		
		/*String html = "<html><head><title>First parse</title></head>"
				  + "<body id='content'><p>Parsed HTML into a doc.<a href='www.baidu.com'>baidu</a></p></body></html>";
		Document doc = Jsoup.parse(html);
		Element content = doc.getElementById("content");
		Elements links = content.getElementsByTag("a");
		for (Element link : links) {
		  String linkHref = link.attr("href");
		  System.out.println(linkHref);
		  String linkText = link.text();
		  System.out.println(linkText);
		}*/
		/*Document doc;
		try {
			
			String url = "https://www.amazon.com/Buck-BuckLite-Max-Folding-Knife/product-reviews/B003XI0TGQ/ref=pd_sim_200_cr_2?ie=UTF8&refRID=9XXV3K33VZ6NZ3SCRV6R";
			doc = Jsoup.connect(url).timeout(5000).get();
			
			Element content = doc.getElementById("cm_cr-review_list");
			Elements directChilds = content.select(".a-section.review");
			for (Element childDiv: directChilds) {
				Element listDiv = childDiv.getElementsByAttributeValue("class", "a-row review-data").first();
				Element span = listDiv.select("span").first();
	            String linkText = span.text().trim(); 
	            System.out.println(linkText);  
	        } 
			
		} catch (IOException e) {
			e.printStackTrace();
		} */
		String i_class = "on July 13, 2016";
        String starCount = i_class.substring(i_class.indexOf("on ")+3);
        System.out.println(starCount);
		
	}
	@Test
	public void testGetAllProducts() {
		List<ProductVo> list = productService.getProductList(null, null).getResultlist();
		Assert.assertNotNull(list);
		Assert.assertEquals(1000, list.size());
	}
	
	/*@Test
	public void testAddProduct() {
		ProductVo  vo=new ProductVo();
		vo.setProductName("myled");
		productService.addProduct(vo,null);
	}

	

	@Test
	public void testGetProductCategory() {
		Map<Integer, List<CategoryVo>> cats = productService
				.getProductCategory();
		Assert.assertNotNull(cats);
		Assert.assertEquals(2, cats.size());
	}

	@Test
	public void testQueryAttrs4Category() {
		Map<AttrNameVo, List<AttrValueVo>> cats = productDao
				.queryAttrs4Category(1);
		Assert.assertNotNull(cats);
		Assert.assertEquals(3, cats.size());
	}

	@Test
	public void testDeleteCategoryByID() {
		productDao.deleteCategoryByID(1);
	}
	
	@Test
	public void testAddCategory() {
		CategoryVo category=new CategoryVo();
		category.setCategoryParentID(1);
		category.setCategoryCode(5);
		category.setCategoryCnName("测试用分类");
		UserInfo creator=new UserInfo();
		creator.setUserID("user00000000-0000-0000-0000-0000000aaron");
		UserInfo operator=new UserInfo();
		operator.setUserID("user00000000-0000-0000-0000-0000000aaron");
		productDao.addCategory(creator,category);
	}
	
	@Test
	public void testAddAttrGroup() {
		AttrGroupVo ag=new AttrGroupVo();
		ag.setAttrGroupCode("codetest");
		ag.setAttrGroupName("测试");
		ag.setOperateDt(new Timestamp(System.currentTimeMillis()));
		UserInfo currentUser=new UserInfo();
		currentUser.setUserID("user00000000-0000-0000-0000-0000000aaron");
		
		AttrNameVo an=new AttrNameVo();
		an.setAttrName("name1");
		an.setAttrCode("attrCode");
		an.setAttrID(5);
		
		productDao.addAttrGroup(currentUser, ag);
	}
	
	@Test
	public void testDeleteAttrGroupByID() {
		int attrGroupID=1;
		productDao.deleteAttrGroupByID(null, attrGroupID);
	}
	
	
	@Test
	public void testAddAttr4AttrGroup() {
		int attrGroupID=1;
		int attrNames=1;
		productDao.addAttr4AttrGroup(null, attrGroupID, attrNames);
	}
	
	
	@Test
	public void testUpdateCategoryByID() {
		UserInfo userInfo=new UserInfo();
		userInfo.setUserID("user00000000-0000-0000-0000-0000000aaron");
		
		CategoryVo category=new CategoryVo();
		category.setCategoryID(1);
		category.setCategoryCnName("更改后LED类");
		productDao.updateCategoryByID(userInfo, category);
	}
	
	
	@Test
	public void testAssignAttr4AttrGroup() {
		UserInfo currentUser=new UserInfo();
		currentUser.setUserID("user00000000-0000-0000-0000-0000000aaron");
		
		productDao.addAttr4AttrGroup(currentUser, 1, 1);
	}*/
}
