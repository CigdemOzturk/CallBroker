package com.ica.model.facade;

import java.util.List;

import com.ica.android.service.Caller;
import com.ica.model.command.Command;
import com.ica.model.contact.Contact;
import com.ica.model.contact.ContactGroup;

/**
 * 
 * @author Cigdem
 *
 */
public interface CallerInterface 
{
	public List<Caller> findAllMatching(String Number);
	public boolean insertCaller(Caller caller);
	public boolean deleteContact(long id);
	
}
