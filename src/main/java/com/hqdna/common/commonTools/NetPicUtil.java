package com.hqdna.common.commonTools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NetPicUtil {
	
	public static byte[] getNetPic(String urlStr){
		//从网络上获取图片
    	//new一个URL对象
		URL url;
		byte[] data = null;
		try {
			//url = new URL("https://www.myled.com/image/cache/product/"+urlStr.substring(1,2)+"/"+urlStr.substring(2,3)+urlStr.substring(0,urlStr.lastIndexOf("."))+"-59x59.jpg");
			url = new URL(urlStr);//logo
			//打开链接
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//设置请求方式为"GET"
			conn.setRequestMethod("GET");
			//超时响应时间为5秒
			conn.setConnectTimeout(5 * 1000);
			//通过输入流获取图片数据
			InputStream inStream = conn.getInputStream();
			//得到图片的二进制数据，以二进制封装得到数据，具有通用性
			data = readInputStream(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	public static byte[] getNetPic(byte bool,String urlStr){
		//从网络上获取图片
    	//new一个URL对象
		URL url;
		byte[] data = null;
		try {
			//url = new URL("https://www.myled.com/image/cache/product/"+urlStr.substring(1,2)+"/"+urlStr.substring(2,3)+urlStr.substring(0,urlStr.lastIndexOf("."))+"-59x59.jpg");
			//url = new URL("https://www.myled.com/image/cache/"+urlStr.substring(0,urlStr.lastIndexOf("."))+"-170x170.jpg");
			if(bool==1){
				url = new URL("https://www.myled.com/catalog/view/theme/default/images/logo.png");//logo
			}else if(bool ==2){
				url = new URL("https://www.myled.com/catalog/view/theme/default/images/default.jpg");//默认产品图片
			}else{
				url = new URL("https://www.myled.com/image/cache/"+urlStr.substring(0,urlStr.lastIndexOf("."))+"-170x170"+urlStr.substring(urlStr.lastIndexOf("."),urlStr.length()));
			}
			//打开链接
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//设置请求方式为"GET"
			conn.setRequestMethod("GET");
			//超时响应时间为5秒
			conn.setConnectTimeout(5 * 1000);
			//通过输入流获取图片数据
			InputStream inStream = conn.getInputStream();
			//得到图片的二进制数据，以二进制封装得到数据，具有通用性
			data = readInputStream(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	public static byte[] readInputStream(InputStream inStream) throws IOException{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		//创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		//每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		//使用一个输入流从buffer里把数据读取出来
		while( (len=inStream.read(buffer)) != -1 ){
			//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		//关闭输入流
		inStream.close();
		//把outStream里的数据写入内存
		return outStream.toByteArray();
	}
	
	/**
	 * 是否是获取产品默认图片还是logo图片
	 * @param isDefaultJPG
	 * @return
	 */
	public static byte[] getLocalPic(boolean isDefaultJPG){
		String imagePath = "";
		if(isDefaultJPG){
			imagePath = File.separator+"root"+File.separator+"wgerp"+File.separator+"skupicture"+File.separator+"default.jpg";
		}else{
			imagePath = File.separator+"root"+File.separator+"wgerp"+File.separator+"skupicture"+File.separator+"logo.png";
		}
        //通过输入流获取图片数据
        InputStream inStream = null;
        byte[] data = null;
		try {
			inStream = new FileInputStream(new File(imagePath));
			data = readInputStream(inStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	public static byte[] getLocalPic(String vendorCode,String skuNo,String picPath){
		
		String imagePath = "";
		if(vendorCode!=null){
			imagePath = File.separator+"root"+File.separator+"wgerp"+File.separator+"skupicture"+File.separator+vendorCode+File.separator+skuNo+picPath.substring(picPath.lastIndexOf("."),picPath.length());
		}else{
			imagePath = File.separator+"root"+File.separator+"wgerp"+File.separator+"skupicture"+File.separator+"logo.png";
		}
		
		//通过输入流获取图片数据
        InputStream inStream = null;
        byte[] data = null;
		try {
			inStream = new FileInputStream(new File(imagePath));
			if(inStream!=null){
				data = readInputStream(inStream);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	public static Document getDocumentByUrl(String url) throws IOException{
		Document doc =null;
		Map<String, String> cookies = new HashMap<String,String>();
//		Response res = Jsoup.connect(amazonReviewUrl).timeout(30000).execute();
//		cookies = res.cookies();
		cookies.put("session-id", "151-7087514-4431312");
		cookies.put("session-id-time", "2082787201l");
		cookies.put("session-token", "PIcqkfBQGLZY8ODprEDOA6ayKJRBBDvv7fMGIWTckeeTr+wsclmB+5B2LqfjraX4bFu4f9nqnLTip6inZUgLgwWRtYgtX5Wcbzc7o01DtxCJwIKcXOP2oaouBUK+BC0J2LnOdilQlG7+/kyHjFTEtCb/F36G4eM9rDRRirHzvUUgPuaAHtdWAXvnXUXn+cRr5ZtTm43rFhUb/ZW8dMAy9uOA7C6KXWAz2S7d768s0B974Jr+gu/BEW99PbwIA2W1");
		cookies.put("skin", "noskin");
		cookies.put("ubid-main", "161-1763656-8359019");
		cookies.put("x-wl-uid", "1E1tZYXbdGFvRCRlBSyIipT7JFt8BIcte+Rokg84TL9nPzwfv0ArG3nanJXGHdYSo3mf9Hc04s7A=");

		try {
			doc = Jsoup.connect(url).timeout(30000).cookies(cookies).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return doc;
	}
	
	
	public static List<Map<String,String>> getAmazonReviewList(Document doc){
		try {
			Element content = doc.getElementById("cm_cr-review_list");
			if(content!=null){
				List<Map<String,String>> reviewStarMapList = new ArrayList<Map<String,String>>();
				Elements directChilds = content.select(".a-section.review");
				for (Element childDiv: directChilds) {
					Element reviewDivWap = childDiv.getElementsByAttributeValue("class", "a-row review-data").first();
					Element starDivWap = childDiv.getElementsByAttributeValue("class", "a-row").first();
					Element starIWap = starDivWap.getElementsByTag("a").first().getElementsByTag("i").first();
					Element reviewSpan = reviewDivWap.select("span").first();
					String review = reviewSpan.text().trim();
					String i_class = starIWap.attr("class");//a-icon a-icon-star a-star-5 review-rating
					String starCount = i_class.substring(i_class.indexOf("a-star-")+7, i_class.indexOf("a-star-")+8);
					Element nameWap = childDiv.select("a.author").first();
					String name = nameWap.text();
					Element dateWap = childDiv.select("span.review-date").first();
					String date = dateWap.text();
					Map<String,String> reviewStarMap = new HashMap<String,String>();
					reviewStarMap.put("review", review);
					reviewStarMap.put("star", starCount);
					reviewStarMap.put("name", name);
					reviewStarMap.put("date", date.substring(date.indexOf("on ")+3));
					reviewStarMapList.add(reviewStarMap);
				}
				return reviewStarMapList;
				
			}else if(doc.getElementById("revMHRL")!=null){
				throw new RuntimeException("请点击下面的See all xx customer reviews (newest first)链接进入评论详情页面");
			}else{
				throw new NullPointerException("访问的页面没有找到评论");
			}
		}catch (NullPointerException e){
			throw new RuntimeException("解析内容出错，请查看url是amazon的评论页面"+e.getMessage());
		}catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}
	public static Map<String,String> getAmazonProductInfo(Document doc){
		try {
			Element content = doc.getElementById("cm_cr-product_info");
			if(content!=null){
				Element imgElement = content.select(".product-image").first().getElementsByTag("img").first();
				Map<String,String> reviewStarMap = new HashMap<String,String>();
				String imageUrl = imgElement.attr("src");
				reviewStarMap.put("imageUrl", imageUrl);
				Element productInfoEle = content.select(".product-info").first();
				
				Element productTitleEle = productInfoEle.select(".product-title").first().getElementsByTag("a").first();
				String productTitle = productTitleEle.text();
				reviewStarMap.put("productTitle", productTitle);
				Element productByLineEle = productInfoEle.select(".product-by-line").first().getElementsByTag("a").first();
				String productByLine = productByLineEle.text();
				reviewStarMap.put("productByLine", productByLine);
				Elements productVariationStripEles = productInfoEle.select(".product-variation-strip");
				if(productVariationStripEles!=null){
					Element productVariationStripEle = productVariationStripEles.first();
					if(productVariationStripEle!=null){
						String productVariationStrip = productVariationStripEle.select("span.a-color-secondary").first().text();
						reviewStarMap.put("productVariationStrip", productVariationStrip);
					}
				}
				Elements productPriceLineEles =  productInfoEle.select(".product-price-line");
				if(productPriceLineEles!=null){
					Element productPriceLineEle = productPriceLineEles.first();
					if(productPriceLineEle!=null){
						String productPriceLine = productPriceLineEle.select("span.a-color-price").first().text();
						if(productPriceLineEle.select("span.arp-shipping-stripe")!=null){
							productPriceLine += productPriceLineEle.select("span.arp-shipping-stripe").first().text();
						}
						reviewStarMap.put("productPriceLine", productPriceLine);
					}
					
				}
				return reviewStarMap;
				
			}else if(doc.getElementById("revMHRL")!=null){
				throw new RuntimeException("请点击下面的See all xx customer reviews (newest first)链接进入评论详情页面");
			}else{
				throw new NullPointerException("访问的页面没有找到评论");
			}
		}catch (NullPointerException e){
			throw new RuntimeException("解析内容出错，请查看url是amazon的评论页面"+e.getMessage());
		}catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}
	public static List<Map<String,String>> getAmazonReviewList(String url){
		Document doc;
		try {
			
			Map<String, String> cookies = new HashMap<String,String>();
//			Response res = Jsoup.connect(amazonReviewUrl).timeout(30000).execute();
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
				List<Map<String,String>> reviewStarMapList = new ArrayList<Map<String,String>>();
				Elements directChilds = content.select(".a-section.review");
				for (Element childDiv: directChilds) {
					Element reviewDivWap = childDiv.getElementsByAttributeValue("class", "a-row review-data").first();
					Element starDivWap = childDiv.getElementsByAttributeValue("class", "a-row").first();
					Element starIWap = starDivWap.getElementsByTag("a").first().getElementsByTag("i").first();
					Element reviewSpan = reviewDivWap.select("span").first();
					String review = reviewSpan.text().trim();
					String i_class = starIWap.attr("class");//a-icon a-icon-star a-star-5 review-rating
					String starCount = i_class.substring(i_class.indexOf("a-star-")+7, i_class.indexOf("a-star-")+8);
					Element nameWap = childDiv.select("a.author").first();
					String name = nameWap.text();
					Element dateWap = childDiv.select("span.review-date").first();
					String date = dateWap.text();
					Map<String,String> reviewStarMap = new HashMap<String,String>();
					reviewStarMap.put("review", review);
					reviewStarMap.put("star", starCount);
					reviewStarMap.put("name", name);
					reviewStarMap.put("date", date.substring(date.indexOf("on ")+3));
					reviewStarMapList.add(reviewStarMap);
				}
				return reviewStarMapList;
				
			}else if(doc.getElementById("revMHRL")!=null){
				throw new RuntimeException("请点击下面的See all xx customer reviews (newest first)链接进入评论详情页面");
			}else{
				throw new NullPointerException("访问的页面没有找到评论");
			}
		}catch (NullPointerException e){
			throw new RuntimeException("解析内容出错，请查看url是amazon的评论页面"+e.getMessage());
		}catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("连接超时"+e.getMessage());
		}catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}
}
