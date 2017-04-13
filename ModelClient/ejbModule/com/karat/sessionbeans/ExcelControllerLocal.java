package com.karat.sessionbeans;

import java.io.File;
import java.util.concurrent.Future;

import javax.ejb.Local;

@Local
public interface ExcelControllerLocal {
	Future<Boolean> updateProductsPrice(File excelFile);
}
