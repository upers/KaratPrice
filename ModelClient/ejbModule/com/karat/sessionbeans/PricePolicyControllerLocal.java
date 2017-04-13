package com.karat.sessionbeans;

import javax.ejb.Local;

import com.karat.jpamodel.PricePolicy;

@Local
public interface PricePolicyControllerLocal {
	PricePolicy getPricePolByCategUserId(Integer userId, Integer categId);
	void mergePricePol (PricePolicy pp);
	PricePolicy getPricePolByProductId(Integer userId, Integer productId);
}
