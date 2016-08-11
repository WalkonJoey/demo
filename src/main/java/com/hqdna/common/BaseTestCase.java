
package com.hqdna.common;

import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试基础类
 */
public class BaseTestCase{
	protected static ApplicationContext cxt;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			cxt = new ClassPathXmlApplicationContext(IGlobalConstants.SPRING_CFG);
		} catch (Exception e) {
			System.out.println("====初始化spring config file失败====");
			e.printStackTrace();
		}
	}

}
