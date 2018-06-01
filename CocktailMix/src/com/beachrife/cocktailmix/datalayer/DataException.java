
package com.beachrife.cocktailmix.datalayer;

public class DataException extends Exception 
{
	private static final long serialVersionUID = -6512846383834624898L;

	public DataException() 
	{
		super();
	}
	
	public DataException(String message) 
	{
		super(message);
	}

	public DataException(Throwable throwable) 
	{
		super(throwable);
	}
	
	public DataException(String message, Throwable throwable) 
	{
		super(message, throwable);
	}
}
