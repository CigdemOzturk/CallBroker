package com.ica.model.contact;

/**
 * 
 * @author Cigdem
 *
 */

/* This class does not extend IdentifiableModelObject because
 * an id is provided whenever the contact is created
 */

public class Contact 
{
	private long id;
	private String name = null;
	private String phoneNumber = null;
	
	public Contact(long id, String name, String phoneNumber)
	{
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
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
}
