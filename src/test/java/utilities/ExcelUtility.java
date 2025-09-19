package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

		public class ExcelUtility {
			
			private FileInputStream fi;
			private FileOutputStream fo;
			private XSSFWorkbook workbook;
			private XSSFSheet sheet;
			private XSSFRow row;
			private XSSFCell cell;
			private CellStyle style;
			private String path;
			
			// Constructor
			public ExcelUtility(String path) {
				this.path = path;
			}

			// ✅ Create sheet if not exists
			public void createSheetIfNotExists(String sheetName) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				sheet = workbook.getSheet(sheetName);

				if (sheet == null) {
					sheet = workbook.createSheet(sheetName);
				}

				fo = new FileOutputStream(path);
				workbook.write(fo);
				workbook.close();
				fi.close();
				fo.close();
			}

			// ✅ Delete sheet
			public void deleteSheet(String sheetName) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				int index = workbook.getSheetIndex(sheetName);
				if (index != -1) {
					workbook.removeSheetAt(index);
				}
				fo = new FileOutputStream(path);
				workbook.write(fo);
				workbook.close();
				fi.close();
				fo.close();
			}

			// ✅ Check if sheet exists
			public boolean isSheetExist(String sheetName) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				int index = workbook.getSheetIndex(sheetName);
				workbook.close();
				fi.close();
				return index != -1;
			}

			// ✅ Clear entire sheet
			public void clearSheet(String sheetName) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				sheet = workbook.getSheet(sheetName);
				if (sheet != null) {
					int lastRow = sheet.getLastRowNum();
					for (int i = lastRow; i >= 0; i--) {
						Row r = sheet.getRow(i);
						if (r != null) {
							sheet.removeRow(r);
						}
					}
				}
				fo = new FileOutputStream(path);
				workbook.write(fo);
				workbook.close();
				fi.close();
				fo.close();
			}

			// ✅ Auto-size all columns
			public void autoSizeColumns(String sheetName) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				sheet = workbook.getSheet(sheetName);
				if (sheet != null && sheet.getRow(0) != null) {
					int columnCount = sheet.getRow(0).getLastCellNum();
					for (int i = 0; i < columnCount; i++) {
						sheet.autoSizeColumn(i);
					}
				}
				fo = new FileOutputStream(path);
				workbook.write(fo);
				workbook.close();
				fi.close();
				fo.close();
			}

			// ✅ Get row count
			public int getRowCount(String sheetName) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				sheet = workbook.getSheet(sheetName);
				int rowCount = sheet.getLastRowNum();
				workbook.close();
				fi.close();
				return rowCount;
			}

			// ✅ Get cell count
			public int getCellCount(String sheetName, int rowNum) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				sheet = workbook.getSheet(sheetName);
				row = sheet.getRow(rowNum);
				int cellCount = (row == null) ? 0 : row.getLastCellNum();
				workbook.close();
				fi.close();
				return cellCount;
			}

			// ✅ Read cell data
			public String getCellData(String sheetName, int rowNum, int colNum) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				sheet = workbook.getSheet(sheetName);
				row = sheet.getRow(rowNum);
				cell = (row == null) ? null : row.getCell(colNum);

				DataFormatter formatter = new DataFormatter();
				String data;
				try {
					data = (cell == null) ? "" : formatter.formatCellValue(cell);
				} catch (Exception e) {
					data = "";
				}

				workbook.close();
				fi.close();
				return data;
			}

			// ✅ Write cell data
			public void setCellData(String sheetName, int rowNum, int colNum, String data) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				sheet = workbook.getSheet(sheetName);

				if (sheet == null) sheet = workbook.createSheet(sheetName);

				row = sheet.getRow(rowNum);
				if (row == null) row = sheet.createRow(rowNum);

				cell = row.getCell(colNum);
				if (cell == null) cell = row.createCell(colNum);

				cell.setCellValue(data);

				fo = new FileOutputStream(path);
				workbook.write(fo);
				workbook.close();
				fi.close();
				fo.close();
			}

			// ✅ Set cell background + font color
			public void fillCellColor(String sheetName, int rowNum, int colNum, IndexedColors bgColor, IndexedColors fontColor) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				sheet = workbook.getSheet(sheetName);

				if (sheet == null) sheet = workbook.createSheet(sheetName);

				row = sheet.getRow(rowNum);
				if (row == null) row = sheet.createRow(rowNum);

				cell = row.getCell(colNum);
				if (cell == null) cell = row.createCell(colNum);

				style = workbook.createCellStyle();
				Font font = workbook.createFont();

				// Background color
				style.setFillForegroundColor(bgColor.getIndex());
				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				// Font color + bold
				font.setColor(fontColor.getIndex());
				font.setBold(true);
				style.setFont(font);

				// Border
				style.setBorderBottom(BorderStyle.THIN);
				style.setBorderTop(BorderStyle.THIN);
				style.setBorderLeft(BorderStyle.THIN);
				style.setBorderRight(BorderStyle.THIN);

				cell.setCellStyle(style);

				fo = new FileOutputStream(path);
				workbook.write(fo);
				workbook.close();
				fi.close();
				fo.close();
			}

			// ✅ Mark cell as PASS
			public void setPass(String sheetName, int rowNum, int colNum) throws IOException {
				setCellData(sheetName, rowNum, colNum, "PASS");
				fillCellColor(sheetName, rowNum, colNum, IndexedColors.LIGHT_GREEN, IndexedColors.BLACK);
			}

			// ✅ Mark cell as FAIL
			public void setFail(String sheetName, int rowNum, int colNum) throws IOException {
				setCellData(sheetName, rowNum, colNum, "FAIL");
				fillCellColor(sheetName, rowNum, colNum, IndexedColors.RED, IndexedColors.WHITE);
			}

			// ✅ Mark cell as SKIPPED
			public void setSkip(String sheetName, int rowNum, int colNum) throws IOException {
				setCellData(sheetName, rowNum, colNum, "SKIPPED");
				fillCellColor(sheetName, rowNum, colNum, IndexedColors.YELLOW, IndexedColors.BLACK);
			}

			// ✅ Set timestamp in cell
			public void setTimestamp(String sheetName, int rowNum, int colNum) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				sheet = workbook.getSheet(sheetName);

				if (sheet == null) sheet = workbook.createSheet(sheetName);

				row = sheet.getRow(rowNum);
				if (row == null) row = sheet.createRow(rowNum);

				cell = row.getCell(colNum);
				if (cell == null) cell = row.createCell(colNum);

				// Current date & time
				String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				cell.setCellValue(timestamp);

				// Apply style
				style = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				style.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));
				cell.setCellStyle(style);

				fo = new FileOutputStream(path);
				workbook.write(fo);
				workbook.close();
				fi.close();
				fo.close();
			}

			// ✅ Append new result at next empty row
			public int appendResult(String sheetName, String testCase, String status) throws IOException {
				fi = new FileInputStream(path);
				workbook = new XSSFWorkbook(fi);
				sheet = workbook.getSheet(sheetName);

				if (sheet == null) sheet = workbook.createSheet(sheetName);

				int rowNum = sheet.getLastRowNum() + 1;
				row = sheet.createRow(rowNum);

				// TestCase Name
				cell = row.createCell(0);
				cell.setCellValue(testCase);

				// Status
				cell = row.createCell(1);
				cell.setCellValue(status);

				// Timestamp
				cell = row.createCell(2);
				String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				cell.setCellValue(timestamp);

				fo = new FileOutputStream(path);
				workbook.write(fo);
				workbook.close();
				fi.close();
				fo.close();

				return rowNum;
			}

			public static Object[][] getExcelData(String path2, String string) {
				// TODO Auto-generated method stub
				return null;
			}
		}

	
		

