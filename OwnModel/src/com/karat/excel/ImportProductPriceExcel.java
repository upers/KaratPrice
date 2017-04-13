package com.karat.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jboss.logging.Logger;

import com.karat.jpamodel.Product;

public class ImportProductPriceExcel {
	private static final Logger log = Logger.getLogger(ImportProductPriceExcel.class);
	private static final String categoryFlag = "ГРУППА";
	private File excelFile;
	private DataFormatter formatter;

	public ImportProductPriceExcel(File excelFile) {
		this.excelFile = excelFile;
		formatter = new DataFormatter();
	}

	/**
	 * Read products codes and prices from excel file
	 * 
	 * @param sheetName
	 * @param codeColumn
	 * @param priceColumn
	 * @return
	 */
	public Map<Integer, Double> getProductPrice(String sheetName, int codeColumn, int priceColumn) {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(excelFile);
			Sheet prodSheet = wb.getSheet(sheetName);
			return getProductPrice(prodSheet, codeColumn, priceColumn);
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}

		return null;
	}

	/**
	 * Read products codes and prices from excel file
	 * 
	 * @param sheetIndex
	 * @param codeColumn
	 * @param priceColumn
	 * @return
	 */
	public Map<Integer, Double> getProductPrice(Integer sheetIndex, int codeColumn,
			int priceColumn) {
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(excelFile);

			return getProductPrice(wb.getSheetAt(sheetIndex), codeColumn, priceColumn);
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}

		return null;
	}

	private Map<Integer, Double> getProductPrice(Sheet productSheet, int codeColumn,
			int priceColumn) {
		Map<Integer, Double> productPrices = new HashMap<>();
		for (int i = 0; i <= productSheet.getLastRowNum(); i++) {
			Row currentRow = productSheet.getRow(i);
			Double code = null;
			Double price = null;
			try {
				code = currentRow.getCell(codeColumn).getNumericCellValue();
				price = currentRow.getCell(priceColumn).getNumericCellValue();
			} catch (Exception e) {
				log.error(e);
			}
			if (code != null && price != null)
				productPrices.put(code.intValue(), price);
		}

		return productPrices;
	}
	
	private String getCellStringValue(Row row, int colIndex) {
		Cell cell = row.getCell(colIndex);
		return formatter.formatCellValue(cell);
	}

	public static void main(String[] args)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		ImportProductPriceExcel inst = new ImportProductPriceExcel(null);
		List<Product> products = new ArrayList<>();
		Workbook wb = WorkbookFactory.create(new File("для сайта.xlsx"));
		Sheet productSheet = wb.getSheet("Лист1");
		int nameColumn = 0;
		int codeColumn = 1;
		int priceColumn = 2;
		String currCategoryName = null;
		
		for (int i = 1; i <= productSheet.getLastRowNum(); i++) {
			Row currentRow = productSheet.getRow(i);
			String code = null;
			Double price = null;
			String prodName = null;
			String categoryMarker = inst.getCellStringValue(currentRow, codeColumn);
			if (categoryMarker.trim().equalsIgnoreCase(categoryFlag)) {
				currCategoryName = inst.getCellStringValue(currentRow, nameColumn);
			} else {
				try {
					prodName = inst.getCellStringValue(currentRow, nameColumn);
					code = inst.getCellStringValue(currentRow, codeColumn);
					price = currentRow.getCell(priceColumn).getNumericCellValue();
					Product product = new Product();
					product.setName(prodName);
					product.setPrice(price);
					product.setCode(code);
					product.setCategoryName(currCategoryName);
					if (code != null && price != null)
						products.add(product);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}

		for (Product p : products)
			log.info(p);
	}
}
