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
import com.karat.jpamodel.Category;
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

	/**
	 * Default constructor.
	 */
	public ExcelController() {
		// TODO Auto-generated constructor stub
	}

	@Asynchronous
	public Future<Boolean> updateProductsPrice(File excelFile) {
		ImportProductPriceExcel imExcel = new ImportProductPriceExcel(excelFile);
		List<Product> products = imExcel.parceProducts();
		log.info("--------------Start update the products from excel file--------------------");
		for (Product p : products) {
			Double price = p.getPrice();
			if (price != null && price != 0.0)
				continue;

			String code = p.getCode();
			Product dbProd = em.createNamedQuery("Product.findByIdCode", Product.class).setParameter("code", code)
					.getSingleResult();
			if (dbProd != null) {
				if (price != null && price != 0.0)
					dbProd.setPrice(price);
				
				log.info("Updated product with name: " + p.getName());
			} else {
				String categName = p.getCategoryName();
				Category category = em.createNamedQuery("Category.findById", Category.class).setParameter("name", categName)
						.getSingleResult();
				if (category != null) {
					p.setCategory(category);
					em.merge(p);
					log.info("Created new product with name: " + p.getName() + "  added it self in category with name: " + categName);
				} else {
					category = new Category();
					category.setName(categName);
					category.addProduct(p);
					
					em.merge(category);
					log.info("Created new category with name: " + categName + "  added product in it self with name: " + p.getName());
				}
			}
		}

		excelFile.delete();
		
		log.info("--------------Completed the update products from excel file--------------------");
		return new AsyncResult<>(true);
	}

}
