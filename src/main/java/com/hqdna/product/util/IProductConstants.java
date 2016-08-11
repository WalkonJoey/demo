package com.hqdna.product.util;

/**
 * 产品常量
 * 
 *
 * @author Joey
 *
 */
public interface IProductConstants {
	public static final String BASE_PRODUCT = "BASE_PRODUCT";
	public static final String FREIGHT_PRODUCT = "9999999999";//在应付的时候需要一个sku分录，但表示的意思是运费，这个SKU也会创建到NC
	public static final String DISCOUNT_PRODUCT = "9999999998";//在应付的时候需要一个sku分录，但表示的意思是折扣，这个SKU也会创建到NC
	public static final String OTHER_TRANSACTION_FEES_PRODUCT = "9999999997";//FBA退货的其他业务费用，没有在sku级别，要为它创建一个sku
}
