package com.lucianoortizsilva.crud.exception;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8979247949102244261L;

	public ObjectNotFoundException(String msg) {
		super(msg);
	}

	public ObjectNotFoundException(String msg, Throwable e) {
		super(msg);
	}
}
