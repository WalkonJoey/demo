package com.hqdna.common.mv.excel;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel操作接口
 * 
 *
 */
public interface IExcelHandler {

	/**
	 * 读取excel文件
	 * 
	 * @param path
	 * @param fileName
	 * @throws Exception 
	 */
	List<Object[]> readExcel(InputStream inputP) throws Exception;

	/**
	 * 写excel文件
	 */
	void writeExcel(Map<String, Object> model, Workbook workbook);
}
