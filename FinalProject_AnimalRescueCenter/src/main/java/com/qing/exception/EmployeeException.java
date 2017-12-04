package com.qing.exception;

public class EmployeeException extends Exception{
	public EmployeeException(String message)	{
		super("EmployeeException- "+message);
	}
	public EmployeeException(String message, Throwable cause){
		super("EmployeeException- "+message, cause);
	}
}
