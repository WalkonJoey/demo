package com.hqdna.common.mv.report;

import java.util.HashMap;
import java.util.Map;


/**
 * 导出Excel表格参数封装类
 * 
 *
 */
public class ExcelParam {

	public static String PARAM="PARAM";
	//文件名称
	private String fileName;
	//表格中的列标题<列索引,列标题>
	private Map<Integer,String> titleMap=new HashMap<Integer,String>();
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Map<Integer, String> getTitleMap() {
		return titleMap;
	}
	
	
}
