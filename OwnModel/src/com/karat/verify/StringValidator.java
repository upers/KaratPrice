package com.karat.verify;

import java.util.regex.Pattern;

public class StringValidator {
	private static final Pattern loginPattern = Pattern.compile("^[a-zA-Z0-9_-]{3,16}$");
	private static final Pattern namePattern = Pattern.compile("^[a-zёЁA-Zа-яА-Я]{0,16}$");
	private static final Pattern passwordPattern = Pattern.compile("^[a-zёЁA-Zа-яА-Я0-9_-]{3,16}$");
	private static final Pattern emailPattern = Pattern.compile("^([a-z0-9_\\.-]+)@([a-z0-9_\\.-]+)\\.([a-z\\.]{2,6})$");
	private static final Pattern companyPattern = Pattern.compile("^[a-zёЁA-Zа-яА-Я\\s]{0,16}$");
	
	private StringValidator(){}
	
	public static boolean isValidLogin(String login) {
		if (login == null)
			return false;
		return loginPattern.matcher(login).matches();
	}
	
	public static boolean isValidName(String name) {
		if (name == null)
			return true;
		System.out.println("is valid name: " + namePattern.matcher(name).matches());
		return namePattern.matcher(name).matches();
	}
	
	public static boolean isValidPassword(String pass) {
		if (pass == null)
			return false;
		return passwordPattern.matcher(pass).matches();
	}
	
	public static boolean isValidEmail(String email) {
		if (email == null)
			return true;
		if (email.equals(""))
			return true;
		return emailPattern.matcher(email).matches();
	}
	
	public static boolean isValidCompany(String company) {
		if (company == null)
			return false;
		return companyPattern.matcher(company).matches();
	}
}