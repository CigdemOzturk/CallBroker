package com.ica.model.contact;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.Log;

import com.ica.model.Identifiable;
import com.ica.model.command.Command;
import com.ica.util.contact.ContactUtils;

/**
 * 
 * @author Cigdem
 *
 */
public class ContactGroup extends Identifiable
{
	private String name = null;
	private List<Contact> contacts;
	private List<Command> commands = null;
	
	public ContactGroup(String name)
	{
		this.name = name;
		contacts = new ArrayList<Contact>();
		commands = new ArrayList<Command>();
	}
	
	// @return name
	public String getName()
	{
		return name;
	}
	
	// @param name
	public void setName(String Name)
	{
		this.name = Name;
	}
	
	// @return commands
	public List<Command> getCommands()
	{
		return commands;
	}
	
	public void setCommands(List<Command> commands)
	{
		this.commands.clear();
		this.commands.addAll(commands);
	}
	
	public void setContacts(List<Contact> selectedContacts)
	{
		contacts.clear();
		contacts.addAll(selectedContacts);
	}
	
	// @return contacts
	public List<Contact> getContacts()
	{
		return contacts;
	}
	
	public boolean contains(String phoneNumber)
	{
		for(Contact c : contacts)
		{
			Log.w("DEBUG", "Contact Group ["+name+"] -> ["+c.getPhoneNumber()+"] == ["+phoneNumber+"] ?");
			if(ContactUtils.comparePhoneNumbers(c.getPhoneNumber(), phoneNumber)) return true;
		}
		return false;
	}
	
	public void executeCommand(Context context)
	{
		for(Command c : commands)
			c.execute(context);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
