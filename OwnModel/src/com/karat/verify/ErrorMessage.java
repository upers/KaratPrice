package com.karat.verify;

import java.io.Serializable;

import org.jboss.logging.Logger;


public class ErrorMessage implements Serializable {
	private static final Logger log = Logger.getLogger(ErrorMessage.class);
	private String msg;
	
	public ErrorMessage() {}
	
	public ErrorMessage(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		log.info("--set--ErrorMessage: " + msg + "-------------------");
		this.msg = msg;
	}
	
}
