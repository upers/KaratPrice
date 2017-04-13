package com.karat.sessionbeans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 * Session Bean implementation class TestConnection
 */
@Stateless
@LocalBean
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class TestConnection {

	@PersistenceContext(unitName = "JPAModel")
	EntityManager em;

	/**
	 * Default constructor.
	 */
	public TestConnection() {
		System.out.println("---------TestConnection Constructor-----------------");
	}

	public String[] testSelectFrom() {
		List resList = em.createNativeQuery("SELECT name FROM user").getResultList();
		String[] arr = new String[resList.size()];
		System.out.println(resList);
		int index = 0;
		for (Object e : resList)
			arr[index++] = e.toString();
		
 		return arr;
	}

}
