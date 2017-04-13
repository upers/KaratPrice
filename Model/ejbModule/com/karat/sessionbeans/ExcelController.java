package com.karat.sessionbeans;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import com.karat.excel.ImportProductPriceExcel;
import com.karat.jpamodel.PricePolicy;
import com.karat.jpamodel.Product;

/**
 * Session Bean implementation class ExcelController
 */
@Stateless
@LocalBean
public class ExcelController {
	private static final Logger log = Logger.getLogger(ExcelController.class);
	@PersistenceContext(unitName = "JPAModel")
	private EntityManager em;
	
	private static final String productSheetName = "product";
	private static final Integer productSheetIndex = 0;
	private static final Integer productPriceColumnIndex = 0;
	private static final Integer productCodeColumnIndex = 1;

    /**
     * Default constructor. 
     */
    public ExcelController() {
        // TODO Auto-generated constructor stub
    }

	@Asynchronous
	public Future<Boolean> updateProductsPrice(File excelFile) {
		ImportProductPriceExcel imExcel = new ImportProductPriceExcel(excelFile);
		Map<Integer, Double> productsPrice = imExcel.getProductPrice(productSheetName, productCodeColumnIndex, productPriceColumnIndex);
		for (Integer key : productsPrice.keySet()) {
			log.info(key + "     " + productsPrice.get(key) + "<<<<<<<<<<<");
		}
		List<Product> products = em.createNamedQuery("Product.findByIdCodes", Product.class)
			.setParameter("codes", productsPrice.keySet())
			.getResultList();
		
		for (Product p : products) {
			log.info(p.getName() + "-=-=-=-=-=-=-=-=");
			Double price = productsPrice.get(p.getCode());
			if (price != null)
				p.setPrice(price);
		}
		
		excelFile.delete();
		return new AsyncResult<>(true);
	}

}
