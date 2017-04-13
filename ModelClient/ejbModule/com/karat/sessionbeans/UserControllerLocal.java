package com.karat.sessionbeans;

import java.util.List;

import javax.ejb.Local;

import com.karat.jpamodel.User;
import com.karat.verify.ErrorMessage;

@Local
public interface UserControllerLocal {
	boolean verify(User user, ErrorMessage errorMessage);
	List<User> getAllUsers();
	List<User> getAllUsersWithDependecies();
	User getUserById(int id);
	User getUserByIdWithDependencies(int id);
	List<User> getUsersByRoleId(int id);
	List<User> getUsersByRoleIdWithDependencies(int id);
	boolean updateUser(User user);
	boolean insertUser(User user);
	User getUserByLogin(String login, ErrorMessage errorMessage);
	boolean regestrationUser(User user, ErrorMessage errMsg);
	boolean isUserDataValid(User user, ErrorMessage errorMessage);
	boolean isUserDataValid(User user);
	boolean isAdmin(User user);
}

