package com.karat.sessionbeans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.logging.Logger;

import com.karat.jpamodel.PricePolicy;
import com.karat.jpamodel.Role;
import com.karat.jpamodel.User;
import com.karat.verify.ErrorMessage;
import com.karat.verify.HashCreater;
import com.karat.verify.StringValidator;

/**
 * Session Bean implementation class UserController
 */
@Stateless
@LocalBean
public class UserController {
	private static final Logger log = Logger.getLogger(UserController.class);
	@PersistenceContext(unitName = "JPAModel")
	EntityManager em;

	/**
	 * Default constructor.
	 */
	public UserController() {
		log.info("-------------UserController----Constructor----------");
	}

	public boolean verify(User user, ErrorMessage errorMessage) {
		if (user.getLogin() == null || user.getPassword() == null) {
			if (errorMessage != null)
				errorMessage.setMsg("Логин либо пароль не могут быть пустыми");
			return false;
		}
		//is user present
		User checkUser  = getUserByLogin(user.getLogin(), errorMessage);
		if (checkUser == null)
			return false;
		
		String usrHashPassword = new HashCreater().getPasswordHash(user.getPassword());
		 System.out.println("user hash password='" +usrHashPassword + "'");
		if (!usrHashPassword.equals(checkUser.getPassword())) {
			if (errorMessage != null)
				errorMessage.setMsg("Не верный пароль");

			return false;
		} else {
			if (errorMessage != null)
				errorMessage.setMsg(null);

			return true;
		}
	}
	
	public User getUserByLogin(String login, ErrorMessage errorMessage) {
		try {
			String escapedLogin = StringEscapeUtils.escapeSql(login);
			User user = em.createNamedQuery("User.findByLogin", User.class).setParameter("p", escapedLogin)
					.getSingleResult();
			return user;
		} catch (Exception e) {
			if (errorMessage != null)
				errorMessage.setMsg("Не верный логин");
		}
		
		return null;
	}
	
	public List<User> getAllUsers() {
		return em.createNamedQuery("User.findAll", User.class).getResultList();
	}

	public List<User> getAllUsersWithDependecies() {
		List<User> list = em.createNamedQuery("User.findAll", User.class).getResultList();
		for (User curU : list) {
			curU.getRole().getId();
			for ( PricePolicy pp : curU.getPricePolicies())  {
				pp.getCategory().getId();
				pp.getProduct().getId();
			}
		}
		return list;
	}

	public User getUserById(int id) {
		return em.find(User.class, id);
	}

	public User getUserByIdWithDependencies(int id) {
		User user = em.find(User.class, id);
		if (user == null)
			return null;
		
		user.getRole().getId();
		for ( PricePolicy pp : user.getPricePolicies())  {
			if (pp.getCategory() != null)
				pp.getCategory().getId();
			if (pp.getProduct() != null)
				pp.getProduct().getId();
		}
		return user;
	}

	public List<User> getUsersByRoleId(int id) {
		return em.createNamedQuery("User.findByRoleId", User.class).getResultList();
	}

	public List<User> getUsersByRoleIdWithDependencies(int id) {
		List<User> list = em.createNamedQuery("User.findByRoleId", User.class).getResultList();
		for (User curU : list) {
			curU.getRole().getId();
			for ( PricePolicy pp : curU.getPricePolicies())  {
				pp.getCategory().getId();
				pp.getProduct().getId();
			}
		}
		return list;
	}

	public boolean updateUser(User user) {
		if (user.getId() == null)
			return false;
		em.merge(user);
		return true;
	}

	public boolean insertUser(User user) {
		if (user.getId() != null)
			return false;
		if (user.getRole() == null)
			return false;
		
		em.persist(user);
		return true;
	}
	
	public boolean regestrationUser(User user, ErrorMessage errMsg) {
		if (user.getId() != null)
			return false;
		
		if (user.getRole() == null) {
			Role role = em.createNamedQuery("Role.findById", Role.class)
				.setParameter("p", Role.USER_ID)
				.getSingleResult();
			user.setRole(role);
		}
		User checkUser = getUserByLogin(user.getLogin(), null);
		if ( checkUser != null ) {
			errMsg.setMsg("Пользователь с таким логином присудствует");
			return false;
		}
		if ( !isUserDataValid(user, errMsg))
			return false;
		
		String pasHash = new HashCreater().getPasswordHash(user.getPassword());
		user.setPassword(pasHash);
		
		em.persist(user);
		return true;
	}
	
	public boolean isUserDataValid(User user) {
		return isUserDataValid(user, null);
	}
	
	public boolean isUserDataValid(User user, ErrorMessage errorMessage) {
		boolean isErrorMsgPresent = (errorMessage == null) ? false : true;
		
		String login = user.getLogin();
		String name = user.getName();
		String surname = user.getSurname();
		String password = user.getPassword();
		String company = user.getCompany();
		
		if ( !StringValidator.isValidLogin(login)) {
			if (isErrorMsgPresent)
				errorMessage.setMsg("Логин лалалалал может содержать латинские буквы, цыфры, знак подчеркивания и дефис и должен быть не менее 4 символов");
			
			return false;
		}
		
		if ( !StringValidator.isValidPassword(password)) {
			if (isErrorMsgPresent)
				errorMessage.setMsg("Пароль может содержать латинские и кириллические буквы, цыфры, знак подчеркивания и дефис и должен быть не менее 4 символов");
			
			return false;
		}
		
		if ( !StringValidator.isValidName(name)) {
			if (isErrorMsgPresent)
				errorMessage.setMsg("Имя может содержать латинские и кириллические буквы: " + name);
			
			return false;
		}
		
		if ( !StringValidator.isValidName(surname)) {
			if (isErrorMsgPresent)
				errorMessage.setMsg("Фамилия может содержать латинские и кириллические буквы");
			
			return false;
		}
		
		if ( !StringValidator.isValidCompany(company)) {
			if (isErrorMsgPresent)
				errorMessage.setMsg("Компания может содержать латинские и кириллические буквы и должен быть не менее 4 символов");
			
			return false;
		}
		
		return true;
	}
	
	public boolean isAdmin(User user) {
		user = getUserByLogin(user.getLogin(), null);
		
		return (user.getRole().getType().equals("Admin")) ? true : false; 
	}
}

