package com.ica.android.service;

/**
 * 
 * @author Cigdem
 *
 */

public class Caller 
{
	private long id;
	private String name = null;
	private String phoneNumber = null;
	private String callTimePeriod = null;
	private String callDate = null;
	
	public Caller(long id, String name, String phoneNumber, String callTimePeriod, String callDate)
	{
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.callTimePeriod = callTimePeriod;
		this.callDate = callDate;
	}
	
	// @return id 
	public long getId()
	{
		return id;
	}
	
	// @return name
	public String getName()
	{
		return name;
	}
	
	//@param name
	public void setName(String name)
	{
		this.name = name;
	}
	
	// @return phoneNumber
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	
	//@param phoneNumber
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	// @return phoneNumber
	public String getCallTimePeriod()
	{
		return callTimePeriod;
	}
	
	//@param phoneNumber
	public void setCallTimePeriod(String callTimePeriod)
	{
		this.callTimePeriod = callTimePeriod;
	}
	
	// @return phoneNumber
	public String getCallDate()
	{
		return callDate;
	}
	
	//@param phoneNumber
	public void setCallTDate(String callDate)
	{
		this.callDate = callDate;
	}
}
