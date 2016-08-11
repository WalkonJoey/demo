package com.hqdna.common.mv.excel;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.commonTools.NetPicUtil;
import com.hqdna.common.page.Json;

public class AmazonReviewExcel extends AbstractExcelHandler{
	
	private Json json; // 数据
	private String sheetName;// excel sheet名
	private String fileName;// excel文件名
	private String titles[];// excel表头名 （sheet内容的第一行）


	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public Json getJson() {
		return json;
	}

	public void setJson(Json json) {
		this.json = json;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public AmazonReviewExcel(){}
	public AmazonReviewExcel(Json json,String sheetName,String fileName,String[] titles){
		this.setJson(json);
		this.sheetName = sheetName;
		this.fileName = fileName;
		this.titles = titles;
	}

	public List<Object[]> readExcel(InputStream inputStream) {
		return null;
	}

	public void writeExcel(Map<String, Object> model, Workbook workbook) {
		long start = DateUtil.nowMilli();
		try {
			
			this.json = (Json)model.get("json"); 
			titles = (String[]) model.get("titles");
			sheetName = (String)model.get("sheetName");
			createExcel(model, workbook, sheetName.toString(),json);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
		if(logger.isDebugEnabled()){
			logger.debug("导出采购单用时："+DateUtil.cal(start, DateUtil.nowMilli()));
		}
	}
	
	private void createExcel(Map<String, Object> model,Workbook workbook,String sheetName,Json json) throws Exception{
		XSSFSheet sheet = (XSSFSheet) workbook.createSheet(sheetName);
		
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		Font font2 = workbook.createFont();
		font2.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		// font2.setFontHeight((short) 250);
		// 把字体应用到当前的样式
		cellStyle.setFont(font2);

		
		@SuppressWarnings("unchecked")
		Map<String,Object> objMap = (Map<String,Object>)json.getObj();
		@SuppressWarnings("unchecked")
		Map<String,String> productInfo = (Map<String,String>)objMap.get("productInfo");
		Row infoTitleRow = sheet.createRow(0);
		Row infoRow = sheet.createRow(1);
		infoRow.setHeight((short) 870);
		int col = 0;
		for (String key : productInfo.keySet()) {
			if(key.equals("imageUrl")){
				Cell cell = infoTitleRow.createCell(col);
				cell.setCellValue(key);
				Cell cell1 = infoRow.createCell(col);
				createPic(workbook, sheet, productInfo.get(key), 1, col);
			}else{
				Cell cell = infoTitleRow.createCell(col);
				cell.setCellValue(key);
				Cell cell1 = infoRow.createCell(col);
				cell1.setCellValue(productInfo.get(key));
			}
			col++;
		}
		
		
		Row titleRow = sheet.createRow(2);
		for (int i=0;i<titles.length;i++) {
			Cell cell = titleRow.createCell(i);
			cell.setCellValue(titles[i]);
		}
		int noInitIndex = 3;
		if(json.isSuccess()){
			@SuppressWarnings("unchecked")
			List<Map<String,String>> reviewMapList = (List<Map<String,String>>)objMap.get("productReview");
			int n = 1;
			for (Map<String, String> map : reviewMapList) {
				Row dataRow = sheet.createRow(noInitIndex);
				// 添加分录图片

				Cell cell01 = dataRow.createCell(0);
				cell01.setCellValue(n++);
				Cell cell02 = dataRow.createCell(1);
				cell02.setCellValue(map.get("star"));
				Cell cell03 = dataRow.createCell(2);
				cell03.setCellValue(map.get("name"));
				Cell cell04 = dataRow.createCell(3);
				cell04.setCellValue(map.get("date"));
				Cell cell05 = dataRow.createCell(4);
				cell05.setCellValue(map.get("review"));
				noInitIndex++;
			}
		}else{
			
		}
	}
	
	private void createPic(Workbook workbook,XSSFSheet sheet,String urlStr,int row,int column){
		byte[] picData = NetPicUtil.getNetPic(urlStr);
		if(picData!=null){
			XSSFDrawing prodPicDrawing = sheet.createDrawingPatriarch();
			XSSFClientAnchor prodPicAnchor = new XSSFClientAnchor();
			CTMarker ctmFrom  = prodPicAnchor.getFrom();
			ctmFrom.setCol(column);
			ctmFrom.setColOff(100);
			ctmFrom.setRow(row);
			ctmFrom.setRowOff(100);
			CTMarker ctmTo  = prodPicAnchor.getTo();
			ctmTo.setCol(column+1);
			ctmTo.setColOff((long)200);
			ctmTo.setRow(row+1);
			ctmTo.setRowOff((long)200);
			prodPicAnchor.setAnchorType(XSSFClientAnchor.MOVE_AND_RESIZE);
			prodPicDrawing.createPicture(prodPicAnchor, workbook.addPicture(picData,XSSFWorkbook.PICTURE_TYPE_JPEG));
		}
	}
}
