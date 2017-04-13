package com.karat.sessionbeans;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.karat.models.CategoryMarkup;

/**
 * Session Bean implementation class CategoryController
 */
@Stateless
@LocalBean
public class CategoryController {
	private static final Logger log = Logger.getLogger(CategoryController.class);
	@PersistenceContext(unitName = "JPAModel")
	private EntityManager em;
	@EJB
	private ProductionController productController;
    /**
     * Default constructor. 
     */
    public CategoryController() {
        // TODO Auto-generated constructor stub
    }

	
	public List<Category> getAllCategories() {
		return em.createNamedQuery("Category.findAll", Category.class).getResultList();// TODO Auto-generated method stub
	}
	
	
	public List<Category> getAllMainCategories() {
		return em.createNamedQuery("Category.findAllMainCategories", Category.class).getResultList();// TODO Auto-generated method stub
	}

	
	public Category getCategoryById(int id) {
		return em.createNamedQuery("Category.findById", Category.class)
			.setParameter("id", id)
			.getSingleResult();
	}

	
	public boolean updateCategory(Category category) {
		if (category.getId() == 0)
			return false;
		em.merge(category);
		return true;
	}

	
	public boolean insertCategory(Category category) {
		if (category == null)
			return false;
		if (category.getId() != null)
			return false;
		
		em.persist(category);
		
		return true;
	}
	
	
	public List<CategoryMarkup> getMainCategoriesMarkupByUserId(Integer userId) {
		List<CategoryMarkup> result = new ArrayList<>();
		List<Category> allMainCategories = em.createNamedQuery("Category.findAllMainCategories", Category.class).getResultList();
		User user = em.find(User.class, userId);
		
		Map<Integer, Double> categsMarkupsByUser = new HashMap<>();
		if (user.getPricePolicies() != null ) {
			for (PricePolicy pp : user.getPricePolicies()) {
				if (pp.getCategory() != null)
					categsMarkupsByUser.put(pp.getCategory().getId(), pp.getCategoryMurkupPercent());
			}
		}
		
		for ( Category c : allMainCategories) {
			CategoryMarkup categoryMarkup = new CategoryMarkup();
			categoryMarkup.setCategoryId(c.getId());
			categoryMarkup.setCategoryName(c.getName());
			if (user != null && categsMarkupsByUser.containsKey(c.getId())) {
				categoryMarkup.setUserId(userId);
				Double murkup = ( categsMarkupsByUser.get(c.getId()) != null ) ? categsMarkupsByUser.get(c.getId()) : 0.0; 
				categoryMarkup.setMarkup(murkup);
			} else {
				categoryMarkup.setMarkup(0.0);
			}
			result.add(categoryMarkup);
		}
		
		return result;
	}
	
	public boolean doHaveChildCateg(int categoryId) {
		List<Category> list = em.createNamedQuery("Category.findByParentId", Category.class)
				.setParameter("id", categoryId)
				.getResultList();
		
		if (list.size() > 0)
			return true;
		else 
			return false;
	}
	
	public void removeCategory(int categoryId) {
		Category cat = em.find(Category.class, categoryId);
		List<Category> childCateg = em.createNamedQuery("Category.findByParentId", Category.class)
				.setParameter("id", categoryId)
				.getResultList();
		
		if (childCateg.size() > 0) {
			for (Category c : childCateg)
				removeCategory(c.getId());
		}
		
		List<PricePolicy> ppList = em.createNamedQuery("PricePolicy.getPricePolByCategoryId", PricePolicy.class)
	    		.setParameter("id", categoryId)
	    		.getResultList();
	    	
	    for (PricePolicy pp : ppList)
	    	em.remove(pp);
	    
	    List<Product> prodList = cat.getProducts();
	    for (Product p : prodList)
	    	productController.removeProduct(p.getId());
	    
		em.remove(cat);
	}
	
	
	public List<Category> getChildCategories(int parentId) {
		return em.createNamedQuery("Category.findByParentId", Category.class)
				.setParameter("id", parentId)
				.getResultList();
	}
	
	public List<CategoryMarkup> getChildCategoriesMarkup(int parentId, int userId) {
		List<Category> allChildCateg = em.createNamedQuery("Category.findByParentId", Category.class)
				.setParameter("id", parentId)
				.getResultList();
		User user = em.find(User.class, userId);
		
		Map<Integer, CategoryMarkup> catMurckups = new HashMap<>();
		for (Category c : allChildCateg) {
			CategoryMarkup currCatMurk = new CategoryMarkup();
			currCatMurk.setCategoryId(c.getId());
			currCatMurk.setUserId(userId);
			currCatMurk.setCategoryName(c.getName());
			
			catMurckups.put(c.getId(), currCatMurk);
		}
		
		for (PricePolicy userPP : user.getPricePolicies()) {
			Integer userPPCatId = null;
			if (userPP.getCategory() != null)
				userPPCatId = userPP.getCategory().getId();
			
			if (catMurckups.containsKey(userPPCatId)) {
				Double murkup = (userPP.getCategoryMurkupPercent() != null) ? userPP.getCategoryMurkupPercent() : 0.0;
				catMurckups.get(userPPCatId).setMarkup(murkup);
			}
		}
		
		return new ArrayList<CategoryMarkup>(catMurckups.values());
	}
	
	
	public Category getParentCategoryById(int categoryId) {
		try {
			return em.createNamedQuery("Category.findParentById", Category.class)
			.setParameter("id", categoryId)
			.getSingleResult();
		} catch(NoResultException e) {
			log.error(e);
		}
		return  null;
	}

}
