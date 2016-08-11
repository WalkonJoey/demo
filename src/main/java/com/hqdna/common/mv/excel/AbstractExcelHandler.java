package com.hqdna.common.mv.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.hqdna.common.base.AbstractBaseLogProvider;
import com.hqdna.common.mv.report.ExcelParam;

/**
 * Excel操作抽象类（公共部分）
 * 
 *
 */
public abstract class AbstractExcelHandler extends AbstractBaseLogProvider implements IExcelHandler {

	/**
	 * 构建标题
	 * 
	 * @param model
	 */
	protected void buildTitles(Map<String, Object> model, Workbook workbook) {
		ExcelParam param = (ExcelParam) model.get(ExcelParam.PARAM);
		Sheet sheet = workbook.getSheetAt(0);
		for (Integer index : param.getTitleMap().keySet()) {
			setText(getCell(sheet, 0, index), param.getTitleMap().get(index));
		}

	}

	protected Cell getCell(Sheet sheet, int row, int col) {
		Row sheetRow = sheet.getRow(row);
		if (sheetRow == null) {
			sheetRow = sheet.createRow(row);
		}
		Cell cell = sheetRow.getCell(col);
		if (cell == null) {
			cell = sheetRow.createCell(col);
		}
		return cell;
	}
	protected Cell getCell(Row row, int col) {
		Cell cell = row.getCell(col);
		if (cell == null) {
			cell = row.createCell(col);
		}
		return cell;
	}
	protected void setText(Cell cell, String text) {
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(text);
	}

}
