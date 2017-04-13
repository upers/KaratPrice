package com.karat.sessionbeans;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import com.karat.jpamodel.Category;
import com.karat.jpamodel.PricePolicy;
import com.karat.jpamodel.Product;
import com.karat.jpamodel.User;
import com.karat.models.ProductMarkup;

/**
 * Session Bean implementation class ProductionController
 */
@Stateless
@LocalBean
public class ProductionController {
	private static final Logger log = Logger.getLogger(ProductionController.class);
	@PersistenceContext(unitName="JPAModel")
	private EntityManager em;
	@EJB
	private CategoryController categoryController;
    /**
     * Default constructor. 
     */
    public ProductionController() {
        // TODO Auto-generated constructor stub
    }
    
    
    public List<Product> getAllProducts() {
    	return em.createNamedQuery("Product.findAll", Product.class).getResultList();
    }
    
    
    public List<Product> getProductsByCategoryId(int categoryId) {
    	return em.createNamedQuery("Product.findByCategoryId", Product.class)
    		.setParameter("id", categoryId)
    		.getResultList();
    }
    
    
    public void removeProduct(int productId) {
    	List<PricePolicy> ppList = em.createNamedQuery("PricePolicy.getPricePolByProductId", PricePolicy.class)
    		.setParameter("id", productId)
    		.getResultList();
    	
    	for (PricePolicy pp : ppList)
    		em.remove(pp);
    	
    	Product p = em.find(Product.class, productId);
    	em.remove(p);
    }
    
    
    public List<ProductMarkup> getProductsWithMurkupByUserAndCategId(int user_id, int categ_id) {
    	List<Product> childProducts = em.createNamedQuery("Product.findByCategoryId", Product.class)
    		.setParameter("id", categ_id)
    		.getResultList();
    	
    	Map<Integer, ProductMarkup> prodMurkups = new HashMap<>();
    	for (Product p : childProducts) {
    		ProductMarkup productMarkup = new ProductMarkup();
    		productMarkup.setProductId(p.getId());
    		productMarkup.setUserId(user_id);
    		productMarkup.setProductName(p.getName());
    		productMarkup.setMarkup(0.0);
    		
    		prodMurkups.put(p.getId(), productMarkup);
    	}
    	
    	User user = em.find(User.class, user_id);
    	for (PricePolicy userPP : user.getPricePolicies()) {
    		Integer prodId = null;
    		if (userPP.getProduct() != null)
    			prodId = userPP.getProduct().getId();
    	
    		if ( prodMurkups.containsKey(prodId) ) 
    			prodMurkups.get(prodId).setMarkup( (userPP.getProductMarkup__productMarckupPercent() != null) ? userPP.getProductMarkup__productMarckupPercent() : 0.0 );
    	}
    	
    	return new ArrayList<>(prodMurkups.values());
    }
    
    
    public Map<String, Double> getProductsForUser(int user_id, int ctegory_id) {
    	List<Product> childProducts = em.createNamedQuery("Product.findByCategoryId", Product.class)
        		.setParameter("id", ctegory_id)
        		.getResultList();
    	User user = em.find(User.class, user_id);
    	
    	Map<String, Double> result = new TreeMap<>();
    	for (Product product : childProducts) {
    		Double murkup = countMurkup(product, user);
    		Double price = product.getPrice();
    		log.info("product price: " + price + "    Product murkup:  " + murkup + "   user_id: " + user.getId());
    		if (murkup != 0.0)
    			price += price / 100 * murkup;
    		
    		price = new BigDecimal(price).setScale(2, RoundingMode.UP).doubleValue();
    		result.put(product.getName(), price);
    	}
    	
    	return result;
    }
    
    
    private Double countMurkup(Product product, User user) {
    	Double murkup = 0.0;
    	for (PricePolicy pp : product.getPricePolicies()) {
    		if (pp.getUser().getId() == user.getId()) {
    			murkup = pp.getProductMarkup__productMarckupPercent();
    		}
    	}
    	if (murkup == 0.0)
    		murkup = getNearestCategoryMurkup(product.getCategory(), user);
    	
    	return murkup;
    }
    
    private Double getNearestCategoryMurkup(Category category, User user) {
    	for (PricePolicy pp : category.getPricePolicies() ) {
    		if (pp.getUser().getId() == user.getId()) {
    			return pp.getCategoryMurkupPercent();
    		}
    	}
    	
    	if ( category.getParentId() != null ) {
    		Category parentCat = categoryController.getCategoryById(category.getParentId());
    		return getNearestCategoryMurkup(parentCat, user);
    	}
    	
    	return 0.0;
    }
    
    
    public Product getProductById(int productId) {
    	try {
    		return em.createNamedQuery("Product.findById", Product.class)
        			.setParameter("id", productId)
        			.getSingleResult();
    	}catch (NoResultException e) {
    		log.error(e);
    		return null;
    	}
    }
    
    
    public Category getCategoryByProductId(int productId) {
    	Product p = null;
    	try {
    		p = em.createNamedQuery("Product.findById", Product.class)
        			.setParameter("id", productId)
        			.getSingleResult();
    	} catch (NoResultException e) {
    		log.error(e);
    	}
    	if (p == null)
    		return null;
    	
    	return p.getCategory();
    }

	
	public void updateProduct(Product product) {
		if (product.getId() == null)
			return;
		
		em.merge(product);
	}
	
	public void mergeProduct(Product p ) {
		em.merge(p);
	}
}
