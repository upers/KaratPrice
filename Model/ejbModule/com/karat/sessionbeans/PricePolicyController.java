package com.karat.sessionbeans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import com.karat.jpamodel.PricePolicy;

/**
 * Session Bean implementation class PricePolicyController
 */
@Stateless
@LocalBean
public class PricePolicyController {
	
	@PersistenceContext(unitName="JPAModel")
	private EntityManager em;
    /**
     * Default constructor. 
     */
    public PricePolicyController() {
    	System.out.println("------PricePolicyController constructor-----------");
    }
    
    public PricePolicy getPricePolByCategUserId(Integer userId, Integer categId) {
    	if (userId == null || categId == null)
    		return null;
		try {
			PricePolicy pp = em.createNamedQuery("PricePolicy.getPricePolByCategUserId", PricePolicy.class)
		    		.setParameter("user_id", userId)
		    		.setParameter("category_id", categId)
		    		.getSingleResult();
			
			return pp;
		} catch (Exception e) {
			return null;
		}
    }
    
    public PricePolicy getPricePolByProductId(Integer userId, Integer productId) {
    	if (userId == null || productId == null)
    		return null;
		try {
			PricePolicy pp = em.createNamedQuery("PricePolicy.getPricePolByProductAndUserId", PricePolicy.class)
		    		.setParameter("user_id", userId)
		    		.setParameter("product_id", productId)
		    		.getSingleResult();
			
			return pp;
		} catch (Exception e) {
			return null;
		}
    }
    
    public void mergePricePol(PricePolicy pp) {
    	em.merge(pp);
    }

}
