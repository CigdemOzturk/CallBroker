package com.ica.model.facade;

import java.util.List;

import com.ica.model.command.Command;
import com.ica.model.contact.Contact;
import com.ica.model.contact.ContactGroup;

/**
 * 
 * @author Cigdem
 *
 */
public interface ContactFacadeInterface 
{
	public boolean insertContactGroup(ContactGroup contactGroup);
	public boolean deleteContactGroup(long id);
	public ContactGroup findContactGroup(long id);
	public List<Contact> findContactsOfContactGroup(long id);
	public List<Command> findCommandsOfContactGroup(long id);
	public List<ContactGroup> findAllContactGroups();
	
	public void updateContactGroup(long id, String newName, List<Contact> selectedContacts);
	public void updateCommandsOfCotactGroup(Long idContactGroup, List<Command> commands);
}
