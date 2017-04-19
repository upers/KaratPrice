package com.karat.excel;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
	private static final Properties p = new Properties();
	/**
	 * load excel properties from file
	 */
	static {
		InputStream is = ImportProductPriceExcel.class.getClassLoader().getResourceAsStream("excel.properties");
		try {
			p.load(is);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
		}
	}

	private static final String CATEGORY_FLAG = p.getProperty("category_flag");
	private static final String SHEET_NAME = p.getProperty("sheet_name");
	private static final int NAME_COLUMN = Integer.valueOf(p.getProperty("name_column"));
	private static final int CODE_COLUMN = Integer.valueOf(p.getProperty("code_column"));
	private static final int PRICE_COLUMN = Integer.valueOf(p.getProperty("price_column"));

	private File excelFile;
	private DataFormatter formatter;

	public ImportProductPriceExcel(File excelFile) {
		this.excelFile = excelFile;
		formatter = new DataFormatter();
	}

	/**
	 * Read products codes and prices from excel file
	 */
	public List<Product> parceProducts() {
		Workbook wb = null;
		List<Product> products = new ArrayList<>();
		try {
			wb = WorkbookFactory.create(excelFile);
			Sheet productSheet = wb.getSheet(SHEET_NAME);
			String currCategoryName = null;

			for (int i = 1; i <= productSheet.getLastRowNum(); i++) {
				Row currentRow = productSheet.getRow(i);
				String code = null;
				Double price = null;
				String prodName = null;
				String categoryMarker = getCellStringValue(currentRow, CODE_COLUMN);
				if (categoryMarker.trim().equalsIgnoreCase(CATEGORY_FLAG)) {
					currCategoryName = getCellStringValue(currentRow, NAME_COLUMN);
				} else {
					try {
						prodName = getCellStringValue(currentRow, NAME_COLUMN);
						code = getCellStringValue(currentRow, CODE_COLUMN);
						price = currentRow.getCell(PRICE_COLUMN).getNumericCellValue();
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
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (wb != null)
				try {
					wb.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
		}

		return products;
	}

	private String getCellStringValue(Row row, int colIndex) {
		Cell cell = row.getCell(colIndex);
		return formatter.formatCellValue(cell);
	}

	public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
		ImportProductPriceExcel inst = new ImportProductPriceExcel(null);
		List<Product> products = new ArrayList<>();
		Workbook wb = WorkbookFactory.create(new File("для сайта.xlsx"));
		Sheet productSheet = wb.getSheet(SHEET_NAME);
		String currCategoryName = null;

		for (int i = 1; i <= productSheet.getLastRowNum(); i++) {
			Row currentRow = productSheet.getRow(i);
			String code = null;
			Double price = null;
			String prodName = null;
			String categoryMarker = inst.getCellStringValue(currentRow, CODE_COLUMN);
			if (categoryMarker.trim().equalsIgnoreCase(CATEGORY_FLAG)) {
				currCategoryName = inst.getCellStringValue(currentRow, NAME_COLUMN);
			} else { 
				try {
					prodName = inst.getCellStringValue(currentRow, NAME_COLUMN);
					code = inst.getCellStringValue(currentRow, CODE_COLUMN);
					price = currentRow.getCell(PRICE_COLUMN).getNumericCellValue();
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
