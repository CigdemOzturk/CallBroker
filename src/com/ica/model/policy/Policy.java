package com.ica.model.policy;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.ica.model.Identifiable;
import com.ica.model.contact.ContactGroup;
import com.ica.model.policy.time.TimeStamp;
import com.ica.model.policy.time.TimeTableManager;
import com.ica.util.time.TimeUtils.DAYS;

/**
 * 
 * @author Cigdem
 *
 */
public class Policy extends Identifiable 
{
	private String name = null;
	private TimeTableManager timeTableManager = null;
	private List<ContactGroup> contacts = null;
	private boolean active;
	
	public Policy(String name)
	{
		this.setName(name);
		timeTableManager = new TimeTableManager();
		active = false;
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
	
	//@return contacts
	public List<ContactGroup> getContacts()
	{
		return contacts;
	}
	
	// @param contacts
	public void setContacts(List<ContactGroup> contacts)
	{
		this.contacts = contacts;
	}
	
	public TimeTableManager getTimeTableManager()
	{
		return timeTableManager;
	}
	
	public List<TimeStamp> getTimeStamps(DAYS day)
	{
		return timeTableManager.getTimeStamps(day);
	}
	
	public void setTimeStamps(HashMap<DAYS, List<TimeStamp>> timeTable)
	{
		timeTableManager.setTimeTable(timeTable);
	}
	
	public List<ContactGroup> test(String phoneNumber)
	{
		List<ContactGroup> matched = new Vector<ContactGroup>();
		if(contacts == null)
		{
			return matched;
		}
		if(!timeTableManager.contains(Calendar.getInstance())) 
		{
			return matched;
		}
		for(ContactGroup contactGroup : contacts)
		{
			if(contactGroup.contains(phoneNumber))
			{
				matched.add(contactGroup);
			}
		}
		return matched;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
