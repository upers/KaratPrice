package com.karat.sessionbeans;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.karat.jpamodel.Category;
import com.karat.jpamodel.Product;
import com.karat.models.ProductMarkup;

@Local
public interface ProductionControllerLocal {
	 void removeProduct(int productId);
	 List<Product> getProductsByCategoryId(int categoryId);
	 List<Product> getAllProducts();
	 List<ProductMarkup> getProductsWithMurkupByUserAndCategId(int user_id, int categ_id);
	 Product getProductById(int productId);
	 Category getCategoryByProductId(int productId);
	 void updateProduct(Product product);
	 void mergeProduct(Product p);
	 /**
	 * Method forms products list and count them murkup
	 * 
	 */
	 Map<String, Double> getProductsForUser(int user_id, int ctegory_id);
}
