package com.qing.exception;

public class AdoptionRecordException extends Exception {
	public AdoptionRecordException(String message)	{
		super("AdoptionRecordException- "+message);
	}
	public AdoptionRecordException(String message, Throwable cause){
		super("AdoptionRecordException- "+message, cause);
	}
}
