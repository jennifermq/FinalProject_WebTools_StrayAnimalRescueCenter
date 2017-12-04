package com.qing.exception;

public class AdopterException extends Exception{
	public AdopterException(String message)	{
		super("AdopterException- "+message);
	}
	public AdopterException(String message, Throwable cause){
		super("AdopterException- "+message, cause);
	}
}
