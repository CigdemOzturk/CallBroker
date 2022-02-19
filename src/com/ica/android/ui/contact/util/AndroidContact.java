package com.ica.android.ui.contact.util;

/**
 * 
 * @author Cigdem
 *
 */
public class AndroidContact 
{
	private long id;
	private String name;
	private String phoneNumber;
	private boolean checked;
	
	public AndroidContact(long id, String name, String phoneNumber)
	{
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		checked = false;
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
	
	// @param name
	public void setName(String name)
	{
		this.name = name;
	}
	
	// @return phoneNumber
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	
	// @param phoneNumber
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}
	
	// @return checked
	public boolean getChecked()
	{
		return checked;
	}
	
	// @param checked
	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}
}
