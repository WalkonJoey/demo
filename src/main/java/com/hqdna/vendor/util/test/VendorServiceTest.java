package com.hqdna.vendor.util.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hqdna.vendor.dao.IVendorDao;
import com.hqdna.vendor.service.IVendorService;
import com.hqdna.vendor.vo.VendorVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-config.xml")
public class VendorServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Resource(name = "vendorService")
	private IVendorService vendorService;

	@Resource(name = "vendorDao")
	private IVendorDao vendorDao;

	@Test
	public void testQueryAllVendors() {
		List<VendorVo> list = vendorService.getVendorList(null).getResultlist();
		Assert.assertNotNull(list);
		Assert.assertEquals(2, list.size());
	}
	
	@Test
	public void testFindAlls(){
		List<VendorVo> list = vendorService.getVendorList(null, null, null, null)
				.getResultlist();
		Assert.assertNotNull(list);
		Assert.assertEquals(3, list.size());
	}
	
	@Test
	public void testFindEntityById(){
		System.out.println("aaaaa"+vendorDao.getAllLastSN("119"));
	}
	
	@Test
	public void testSaveOrUpdateEntity(){
		
	}
	
	@Test
	public void testDeleteById(){
		
	}
}
