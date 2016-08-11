package demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class TestUser {
	@Test
	public void testSchamExport(){
		// 1).创建Configuration对象：对应hibernate的配置信息和对象关系映射信息
		Configuration config = new Configuration().configure();
		// 2).创建一个Serviceregistry对象：hibernate4.x新添加的对象
		// hibernate中的任何配置和服务都需要在此对象中注册
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
		.applySettings(config.getProperties()).build();
		// 3).
//		SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
		SchemaExport schemaExport = new SchemaExport(config);
		schemaExport.create(true, true);
		//		// 2.创建一个Session对象
//		Session session = sessionFactory.openSession();
//		// 3.开启事务
//		Transaction transaction = session.beginTransaction();
	}
	@Test
	public void testAmazonReview(){

		Document doc;
		try {
			
//			String url = "https://www.amazon.com/product-reviews/B01F79IFF0/ref=pd_cp_0_cr_1?ie=UTF8&refRID=P25QB2BE7DQG35ZGK944";
			String url = "https://www.amazon.com/dp/B016A6IKKY?pf_rd_r=ZASB6Q1RTWJ98DFN2CF2&pf_rd_m=ATVPDKIKX0DER&pf_rd_t=Landing&pf_rd_i=172541&pf_rd_p=2409125322&pf_rd_s=merchandised-search-grid-t1-r1-c1";
			Map<String, String> cookies = new HashMap<String,String>();
//			Response res = Jsoup.connect(url).timeout(30000).execute();
//			cookies = res.cookies();
			cookies.put("session-id", "151-7087514-4431312");
			cookies.put("session-id-time", "2082787201l");
			cookies.put("session-token", "PIcqkfBQGLZY8ODprEDOA6ayKJRBBDvv7fMGIWTckeeTr+wsclmB+5B2LqfjraX4bFu4f9nqnLTip6inZUgLgwWRtYgtX5Wcbzc7o01DtxCJwIKcXOP2oaouBUK+BC0J2LnOdilQlG7+/kyHjFTEtCb/F36G4eM9rDRRirHzvUUgPuaAHtdWAXvnXUXn+cRr5ZtTm43rFhUb/ZW8dMAy9uOA7C6KXWAz2S7d768s0B974Jr+gu/BEW99PbwIA2W1");
			cookies.put("skin", "noskin");
			cookies.put("ubid-main", "161-1763656-8359019");
			cookies.put("x-wl-uid", "1E1tZYXbdGFvRCRlBSyIipT7JFt8BIcte+Rokg84TL9nPzwfv0ArG3nanJXGHdYSo3mf9Hc04s7A=");
			
			doc = Jsoup.connect(url).timeout(30000).cookies(cookies).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
			Element content = doc.getElementById("cm_cr-review_list");
			if(content!=null){
				Elements directChilds = content.select(".a-section.review");
				for (Element childDiv: directChilds) {
					Element reviewDivWap = childDiv.getElementsByAttributeValue("class", "a-row review-data").first();
					Element starDivWap = childDiv.getElementsByAttributeValue("class", "a-row").first();
					Element starIWap = starDivWap.getElementsByTag("a").first().getElementsByTag("i").first();
					Element reviewSpan = reviewDivWap.select("span").first();
					String review = reviewSpan.text().trim();
					String i_class = starIWap.attr("class");//a-icon a-icon-star a-star-5 review-rating
					String starCount = i_class.substring(i_class.indexOf("a-star-")+7, i_class.indexOf("a-star-")+8);
					System.out.println("i_class:"+starCount);
					System.out.println("review:"+review+"");
				}
			}else if(doc.getElementById("revMHRL")!=null){
				throw new RuntimeException("请点击下面的See all xx customer reviews (newest first)链接进入评论详情页面");
			}else{
				throw new NullPointerException();
			}
		}catch (NullPointerException e){
			e.printStackTrace();
			throw new RuntimeException("解析内容出错，请查看url是amazon的评论页面"+e.getMessage());
		} 
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("连接超时"+e.getMessage());
		} 
	}
}
