package com.qing.exception;

public class AnimalException extends Exception{
	public AnimalException(String message)	{
		super("AnimalException- "+message);
	}
	public AnimalException(String message, Throwable cause){
		super("AnimalException- "+message, cause);
	}
}
