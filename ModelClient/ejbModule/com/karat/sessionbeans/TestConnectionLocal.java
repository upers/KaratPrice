package com.karat.sessionbeans;

import javax.ejb.Local;

@Local
public interface TestConnectionLocal {
	String[] testSelectFrom();
}
