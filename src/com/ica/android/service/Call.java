package com.ica.android.service;

/**
 * 
 * @author Cigdem
 *
 */

public class Call
{
	private long id;
	private String phoneNumber = null;
	private int fifteen,thirty;
	private String daypart = "";
	
	public Call(long id, String phoneNumber, int fifteen, int thirty, String daypart)
	{
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.fifteen = fifteen;
		this.thirty = thirty;
		this.daypart = daypart;
	}
	
	// @return id 
	public long getId()
	{
		return id;
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
	public String getDayPart()
	{
		return daypart;
	}
	
	//@param phoneNumber
	public void setDayPart(String daypart)
	{
		this.daypart = daypart;
	}
	
	// @return phoneNumber
	public int getFifteen()
	{
		return fifteen;
	}
	
	//@param phoneNumber
	public void setFifteen(int fifteen)
	{
		this.fifteen = fifteen;
	}
	
	// @return phoneNumber
	public int getThirty()
	{
		return thirty;
	}
	
	//@param phoneNumber
	public void setThiry(int thirty)
	{
		this.thirty = thirty;
	}
}
