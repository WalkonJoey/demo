package com.hqdna.common.baseVo;

/**
 * PO、VO转换接口
 * 
 * @param VO类型
 *
 */
public interface ITransfer<T> {
	/**
	 * PO,VO相互转换，只是转换其中的原始类型或普通类型的属性
	 * @param vo
	 * @param direction true:PO->VO;false:VO-->PO
	 */
	public void transferPo2Vo(T vo, boolean direction);
}
