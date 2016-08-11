package com.hqdna.common.commonTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class ExcelUtil {
	/**
	 * 对外提供读取excel 的方法
	 * */
	public static List<List<Object>> readExcel(HttpServletRequest request) throws IOException {
		
		try {
			// 转型为MultipartHttpRequest
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
			// 获得文件 
			MultipartFile file = multipartRequest.getFile("file");
			//获取要导数据到数据库的excel文件的名字
			String filename=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1).toLowerCase();
			//判断excel文件后缀名xls和xlsx
			InputStream input = file.getInputStream();
			if("xls".equals(filename)){
				return read2003Excel(input);
			}else if("xlsx".equals(filename)){
				return read2007Excel(input);
			}else{
				throw new IOException("不支持的文件类型");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取 office 2003 excel
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static List<List<Object>> read2003Excel(InputStream input)
			throws IOException {
		List<List<Object>> list = new LinkedList<List<Object>>();
		HSSFWorkbook hwb = new HSSFWorkbook(input);
		HSSFSheet sheet = hwb.getSheetAt(0);
		Object value = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		int counter = 0;
		for (int i = sheet.getFirstRowNum(); counter < sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			} else {
				counter++;
			}
			List<Object> linked = new LinkedList<Object>();
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number String
															// 字符
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					System.out.println(i + "行" + j + " 列 is String type");
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					System.out.println(i + "行" + j
							+ " 列 is Number type ; DateFormt:"
							+ cell.getCellStyle().getDataFormatString());
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle()
							.getDataFormatString())) {
						value = nf.format(cell.getNumericCellValue());
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell
								.getNumericCellValue()));
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					System.out.println(i + "行" + j + " 列 is Boolean type");
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					System.out.println(i + "行" + j + " 列 is Blank type");
					value = "";
					break;
				default:
					System.out.println(i + "行" + j + " 列 is default type");
					value = cell.toString();
				}
				if (value == null || "".equals(value)) {
					continue;
				}
				linked.add(value);
			}
			list.add(linked);
		}
		return list;
	}

	/**
	 * 读取Office 2007 excel
	 * */
	private static List<List<Object>> read2007Excel(InputStream input) throws Exception {
		List<List<Object>> list = new LinkedList<List<Object>>();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		XSSFWorkbook xwb = new XSSFWorkbook(input);
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(0);
		Object value = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		int counter = 0;
		for (int i = sheet.getFirstRowNum(); counter < sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			} else {
				counter++;
			}
			List<Object> linked = new LinkedList<Object>();
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number String
															// 字符
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0");// 格式化数字
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					System.out.println(i + "行" + j + " 列 is String type");
					value = cell.getStringCellValue();
					System.out.println(value);
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					System.out.println(i + "行" + j
							+ " 列 is Number type ; DateFormt:"
							+ cell.getCellStyle().getDataFormatString());
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());
						System.out.println(value);
					} else if ("General".equals(cell.getCellStyle()
							.getDataFormatString())) {
						value = nf.format(cell.getNumericCellValue());
						System.out.println(value);
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell
								.getNumericCellValue()));
						System.out.println(value);
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					System.out.println(i + "行" + j + " 列 is Boolean type");
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					System.out.println(i + "行" + j + " 列 is Blank type");
					value = "";
					break;
				default:
					System.out.println(i + "行" + j + " 列 is default type");
					value = cell.toString();
				}
				if (value == null || "".equals(value)) {
					continue;
				}
				linked.add(value);
			}
			list.add(linked);
		}
		return list;
	}
	
	
	
	
	/**
	 * 判断单元格类型，返回  对应的Java类型  的值
	 * 
	 * @param classType  Java中的类型
	 * @param cell   Excel单元格
	 * @return  单元格类型对应的Java类型
	 */
	public static Object judgeCellType(String classType, Cell cell) throws RuntimeException{
		int cellType = cell.getCellType();
		Object value = null;
		try {
			if (cellType == Cell.CELL_TYPE_NUMERIC) {
				if (classType.equals("int")
						|| classType.equals("class java.lang.Integer")) {
					value = (int) cell.getNumericCellValue();
				} else if (classType.equals("long")
						|| classType.equals("class java.lang.Long")) {
					value = (long) cell.getNumericCellValue();
				} else if (classType.equals("boolean")
						|| classType.equals("class java.lang.Boolean")) {
					int intValue = (int) cell.getNumericCellValue();
					if(intValue==1){
						value = true;
					}else if(intValue==0){
						value = false;
					}else{
						throw new RuntimeException("boolean 类型导入填写数字只能为0 或1");
					}
				} else if (classType.equals("class java.lang.String")) {
					DecimalFormat df = new DecimalFormat("0");//转换成整型     可以有效防止科学计数法   这样做虽然防止了科学计数法，但是当数字是小数时就会把小数点抹掉
			        value = df.format(cell.getNumericCellValue());
				} else if (classType.equals("byte")
						|| classType.equals("class java.lang.Byte")) {
					value = (byte) cell.getNumericCellValue();
				} else if (classType.equals("class java.math.BigDecimal")) {
					/* DecimalFormat df = new DecimalFormat("0"); */
					value = new BigDecimal(cell.toString());
				} else if (classType.equals("float")
						|| classType.equals("class java.lang.Float")) {
					value = Float.parseFloat(cell.toString());
				} else if (classType.equals("double")
						|| classType.equals("class java.lang.Double")) {
					value = Double.parseDouble(cell.toString());
				} else if (classType.equals("class java.sql.Timestamp")) {
					Date date = cell.getDateCellValue();
					value = new Timestamp(date.getTime());
				} else {
					value = Double.valueOf(cell.getNumericCellValue())
							.toString();
				}
			} else if (cellType == Cell.CELL_TYPE_STRING) {
				String tempValue = cell.getStringCellValue().trim();
				if (classType.equals("int")
						|| classType.equals("class java.lang.Integer")) {
					value = Integer.valueOf(tempValue);
				} else if (classType.equals("boolean")
						|| classType.equals("class java.lang.Boolean")) {
					if("1".equals(tempValue)){
						value = true;
					}else if("0".equals(tempValue)){
						value = false;
					}else{
						throw new RuntimeException("boolean 类型导入填写数字只能为0 或1");
					}
				} else if (classType.equals("long")
						|| classType.equals("class java.lang.Long")) {
					value = Long.valueOf(tempValue);
				} else if (classType.equals("byte")
						|| classType.equals("class java.lang.Byte")) {
					value = Byte.valueOf(tempValue);
				} else if (classType.equals("float")
						|| classType.equals("class java.lang.Float")) {
					value = Float.valueOf(tempValue);
				} else if (classType.equals("double")
						|| classType.equals("class java.lang.Double")) {
					value = Double.valueOf(tempValue);
				} else if (classType.equals("class java.lang.String")) {
					value = tempValue;
				} else if (classType.equals("class java.math.BigDecimal")) {
					value = new BigDecimal(tempValue);
				} else if (classType.equals("class java.sql.Timestamp")) {
					try {
						if(tempValue.length()==10){
							if(tempValue.contains("/")){
								Date date = DateUtil.parseOnlyDate02(tempValue);
								value = new Timestamp(date.getTime());
							}else if(tempValue.contains("-")){
								Date date = DateUtil.parseOnlyDate(tempValue);
								value = new Timestamp(date.getTime());
							}else{
								value = DateUtil.nowTs();
							}
						}else if(tempValue.contains(":")){
							value = Timestamp.valueOf(tempValue);
						}else{
							value = DateUtil.nowTs();
						}
					} catch (NullPointerException npe) {
						throw npe;
					} catch (IllegalArgumentException iae) {
						throw iae;
					}
				} else {
					value = tempValue;
				}
				
				
			} else if (cellType == Cell.CELL_TYPE_BOOLEAN) {
				value = cell.getBooleanCellValue();
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				if (classType.equals("int")
						|| classType.equals("class java.lang.Integer")) {
					value = -1;
				} else if (classType.equals("long")
						|| classType.equals("class java.lang.Long")) {
					value = -1;
				} else if (classType.equals("class java.lang.String")) {
					value = "";
				} else if (classType.equals("byte")
						|| classType.equals("class java.lang.Byte")) {
					value = -1;
				} else if (classType.equals("class java.math.BigDecimal")) {
					value = new BigDecimal(0);
				} else if (classType.equals("float")
						|| classType.equals("class java.lang.Float")) {
					value = -1.0;
				} else if (classType.equals("double")
						|| classType.equals("class java.lang.Double")) {
					value = -1.0;
				} else if (classType.equals("class java.sql.Timestamp")) {
					value = null;
				} else {
					value = "";
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
		return value;
	}

	
	
	/** 
     * 复制一个单元格样式到目的单元格样式 
	 * @param wb 
	 * @param wb 
     * @param fromStyle 
     * @param toStyle 
     */  
    public static void copyCellStyle(XSSFWorkbook wb, XSSFCellStyle fromStyle,  
    		XSSFCellStyle toStyle) {  
        toStyle.setAlignment(fromStyle.getAlignment());  
        //边框和边框颜色  
        toStyle.setBorderBottom(fromStyle.getBorderBottom());  
        toStyle.setBorderLeft(fromStyle.getBorderLeft());  
        toStyle.setBorderRight(fromStyle.getBorderRight());  
        toStyle.setBorderTop(fromStyle.getBorderTop());  
        toStyle.setTopBorderColor(fromStyle.getTopBorderColor());  
        toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());  
        toStyle.setRightBorderColor(fromStyle.getRightBorderColor());  
        toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());  
          
        //背景和前景  
        //toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
        toStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        if(fromStyle.getFillBackgroundXSSFColor()!=null){
        	toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundXSSFColor());
        }
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());
        if(fromStyle.getFillForegroundXSSFColor()!=null){
        	toStyle.setFillForegroundColor(fromStyle.getFillForegroundXSSFColor());
        }
        toStyle.setDataFormat(fromStyle.getDataFormat());  
        toStyle.setFillPattern(fromStyle.getFillPattern());  
        toStyle.setHidden(fromStyle.getHidden());  
        toStyle.setIndention(fromStyle.getIndention());//首行缩进  
        toStyle.setLocked(fromStyle.getLocked());  
        toStyle.setRotation(fromStyle.getRotation());//旋转  
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());  
        toStyle.setWrapText(fromStyle.getWrapText()); 
        //字体
        XSSFFont font = fromStyle.getFont();
        XSSFFont newFont = wb.createFont();
        newFont.setBoldweight(font.getBoldweight());
        newFont.setFontName(font.getFontName());
        newFont.setFontHeight(font.getFontHeight());
        newFont.setCharSet(font.getCharSet());
        newFont.setColor(font.getColor());
        if(font.getXSSFColor()!=null){
        	newFont.setColor(font.getXSSFColor());
        }
        newFont.setUnderline(font.getUnderline());
        toStyle.setFont(newFont);
        
    }  
    
    
    /** 
     * Sheet复制 
     * @param fromSheet 
     * @param toSheet 
     * @param copyValueFlag 
     */  
    public static void copySheet(XSSFWorkbook wb,XSSFSheet fromSheet, XSSFSheet toSheet,  
            boolean copyValueFlag) {  
        //合并区域处理  
        mergerRegion(fromSheet, toSheet);
        int colNum = 0;
        for (Iterator<Row> rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {  
            XSSFRow tmpRow = (XSSFRow) rowIt.next();  
            XSSFRow newRow = toSheet.createRow(tmpRow.getRowNum());  
            //行复制  
            copyRow(wb,tmpRow,newRow,copyValueFlag);
            colNum = tmpRow.getLastCellNum();
        }
        for(int i=0;i<colNum;i++){
        	toSheet.setColumnWidth(i, fromSheet.getColumnWidth(i));
        }
    }
    
    /** 
     * 行复制功能 
     * @param fromRow 
     * @param toRow 
     */  
    public static void copyRow(XSSFWorkbook wb,XSSFRow fromRow,XSSFRow toRow,boolean copyValueFlag){  
        for (Iterator<Cell> cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
            XSSFCell tmpCell = (XSSFCell) cellIt.next();  
            XSSFCell newCell = toRow.createCell(tmpCell.getColumnIndex());
            toRow.setHeight(fromRow.getHeight());
           /* toRow.setRowStyle(wb.createCellStyle());
            if(fromRow.getRowStyle()!=null){
            	toRow.getRowStyle().cloneStyleFrom(fromRow.getRowStyle());
            }*/
            
            toRow.setRowStyle(fromRow.getRowStyle());
            copyCell(wb,tmpCell, newCell, copyValueFlag);  
        }  
    }  
    /** 
    * 复制原有sheet的合并单元格到新创建的sheet 
    *  
    * @param sheetCreat 新创建sheet 
    * @param sheet      原有的sheet 
    */  
    public static void mergerRegion(XSSFSheet fromSheet, XSSFSheet toSheet) {  
       int sheetMergerCount = fromSheet.getNumMergedRegions();
       for (int i = 0; i < sheetMergerCount; i++) {  
    	CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i); 
        toSheet.addMergedRegion(mergedRegionAt);  
       }  
    }  
    
    /** 
     * 复制单元格 
     *  
     * @param srcCell 
     * @param distCell 
     * @param copyValueFlag 
     *            true则连同cell的内容一起复制 
     */  
    public static void copyCell(XSSFWorkbook wb,XSSFCell srcCell, XSSFCell distCell,  
            boolean copyValueFlag) {  
        XSSFCellStyle newstyle=wb.createCellStyle();
        XSSFCellStyle oldStyle = srcCell.getCellStyle();
        // 不同数据类型处理  
        int srcCellType = srcCell.getCellType();  
        distCell.setCellType(srcCellType);  
        copyCellStyle(wb,oldStyle, newstyle);  
        //样式  
        distCell.setCellStyle(newstyle);
      /*  if(oldStyle!=null){
        	distCell.getCellStyle().cloneStyleFrom(oldStyle);
        	if(oldStyle.getFillBackgroundXSSFColor()!=null){
        		srcCell.getCellStyle().setFillBackgroundColor(oldStyle.getFillBackgroundColor());
        		srcCell.getCellStyle().setFillBackgroundColor(oldStyle.getFillBackgroundXSSFColor());
        	}
        }*/
        //评论  
        if (srcCell.getCellComment() != null) {  
            distCell.setCellComment(srcCell.getCellComment());  
        }  
        if (copyValueFlag) {  
            if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) { 
                distCell.setCellValue(srcCell.getNumericCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {  
                distCell.setCellValue(srcCell.getRichStringCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {  
                // nothing21  
            } else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {  
                distCell.setCellValue(srcCell.getBooleanCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {  
                distCell.setCellErrorValue(srcCell.getErrorCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {  
                distCell.setCellFormula(srcCell.getCellFormula());  
            } else { // nothing29  
            }  
        }  
    }
    
    
    
    
    /** 
     * Sheet复制 
     * @param fromSheet 
     * @param toSheet 
     * @param copyValueFlag 
     */  
    public static void newCopySheet(XSSFWorkbook wb,XSSFSheet fromSheet, XSSFSheet toSheet,  
            boolean copyValueFlag) {  
        //合并区域处理  
        mergerRegion(fromSheet, toSheet);
        int colNum = 0;
        for (Iterator<Row> rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {  
            XSSFRow tmpRow = (XSSFRow) rowIt.next();  
            XSSFRow newRow = toSheet.createRow(tmpRow.getRowNum());  
            //行复制  
            newCopyRow(wb,tmpRow,newRow,copyValueFlag);
            colNum = tmpRow.getLastCellNum();
        }
        for(int i=0;i<colNum;i++){
        	toSheet.setColumnWidth(i, fromSheet.getColumnWidth(i));
        }
    }
    
    /** 
     * 行复制功能 
     * @param fromRow 
     * @param toRow 
     */  
    public static void newCopyRow(XSSFWorkbook wb,XSSFRow fromRow,XSSFRow toRow,boolean copyValueFlag){  
        for (Iterator<Cell> cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
            XSSFCell tmpCell = (XSSFCell) cellIt.next();  
            XSSFCell newCell = toRow.createCell(tmpCell.getColumnIndex());
            toRow.setHeight(fromRow.getHeight());
            toRow.setRowStyle(wb.createCellStyle());
            if(fromRow.getRowStyle()!=null){
            	toRow.getRowStyle().cloneStyleFrom(fromRow.getRowStyle());
            }
            newCopyCell(wb,tmpCell, newCell, copyValueFlag);
        }  
    }  
    /** 
    * 复制原有sheet的合并单元格到新创建的sheet 
    *  
    * @param sheetCreat 新创建sheet 
    * @param sheet      原有的sheet 
    */  
    public static void newMergerRegion(XSSFSheet fromSheet, XSSFSheet toSheet) {  
       int sheetMergerCount = fromSheet.getNumMergedRegions();
       for (int i = 0; i < sheetMergerCount; i++) {  
    	CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i); 
        toSheet.addMergedRegion(mergedRegionAt);  
       }  
    }  
    
    /** 
     * 复制单元格 
     *  
     * @param srcCell 
     * @param distCell 
     * @param copyValueFlag 
     *            true则连同cell的内容一起复制 
     */  
    public static void newCopyCell(XSSFWorkbook wb,XSSFCell srcCell, XSSFCell distCell,  
            boolean copyValueFlag) {  
        XSSFCellStyle newstyle=wb.createCellStyle();
        XSSFCellStyle oldStyle = srcCell.getCellStyle();
        // 不同数据类型处理  
        int srcCellType = srcCell.getCellType();  
        distCell.setCellType(srcCellType);  
        copyCellStyle(wb,oldStyle, newstyle);  
        //样式  
        distCell.setCellStyle(newstyle);
        //评论  
        if (srcCell.getCellComment() != null) {  
            distCell.setCellComment(srcCell.getCellComment());  
        }  
        if (copyValueFlag) {  
            if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) { 
                distCell.setCellValue(srcCell.getNumericCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {  
                distCell.setCellValue(srcCell.getRichStringCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {  
                // nothing21  
            } else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {  
                distCell.setCellValue(srcCell.getBooleanCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {  
                distCell.setCellErrorValue(srcCell.getErrorCellValue());  
            } else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {  
                distCell.setCellFormula(srcCell.getCellFormula());  
            } else { // nothing29  
            }  
        }  
    }
    
    /**
     * 将list里面的值设置到sheet中
     * @param startIndex 写入开始的行
     * @param listData
     * @param sheet
     * @param cHelp
     */
    public static void setSpecialValueToExcel(int startRow,List<Object[]> listData,Sheet sheet,CreationHelper cHelp){
    	int i = startRow;
		for (Object[] objs : listData) {
			Row dataRow = sheet.createRow(i++);
			dataRow.setHeight((short) 300);
			int cellIndex = 0;
			for (Object object : objs) {
				Cell cell = dataRow.createCell(cellIndex);
				if(object instanceof Integer){
					cell.setCellValue(((Integer)object).intValue());
				}else if(object instanceof String){
					cell.setCellValue(object.toString());
				}else if(object instanceof Float){
					cell.setCellValue(((Float)object).floatValue());
				}else if(object instanceof java.lang.Byte){
					cell.setCellValue(((Byte)object).byteValue());
				}else if(object instanceof java.sql.Timestamp){
					Timestamp tp = (Timestamp)object;
					RichTextString text = cHelp.createRichTextString(DateUtil.formatDateTime(tp));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(text);
				}else if(object instanceof java.util.Date){
					Date tp = (Date)object;
					RichTextString text = cHelp.createRichTextString(DateUtil.formatDateTime(tp));
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(text);
				}else if(object instanceof java.math.BigDecimal){
					BigDecimal bD = (BigDecimal)object;
					cell.setCellValue(bD.doubleValue());
				}else if(object instanceof java.lang.Double){
					cell.setCellValue(((Double)object).doubleValue());
				}else{
					if(object!=null){
						RichTextString text = cHelp.createRichTextString(object.toString());
						cell.setCellValue(text);
					}else{
						cell.setCellValue("");
					}
				}
				cellIndex++;
			}
		}
    }
    
    
    public static void saveWorkbook(String pathName,Workbook workbook){
    	FileOutputStream output = null;
    	try {
    		output = new FileOutputStream(new File(pathName));
    		workbook.write(output);
			//output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    }

	public static CellStyle getDefaultTitleStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 粗体显示
		style.setFont(font);
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setAlignment(CellStyle.ALIGN_CENTER);

		return style;
		/*// 生成并设置另一个样式
		CellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(CellStyle.BORDER_THIN);
		style2.setBorderLeft(CellStyle.BORDER_THIN);
		style2.setBorderRight(CellStyle.BORDER_THIN);
		style2.setBorderTop(CellStyle.BORDER_THIN);
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		Font font2 = workbook.createFont();
		font2.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		//font2.setFontHeight((short) 250);
		// 把字体应用到当前的样式
		style2.setFont(font2);*/
	}

	public static void fillTitleInfo(Sheet sheet, CellStyle titleStyle,
			String[] titles, CreationHelper cHelp) {
		Row titleRow = sheet.createRow(0);
		int index = 0;
		// 写入表头
		for (String str : titles) {
			RichTextString text = cHelp.createRichTextString(str);
			Cell cell = titleRow.createCell(index);
			cell.setCellStyle(titleStyle);
			cell.setCellValue(text);
			index++;
		}
	}
}
