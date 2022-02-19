package com.ica.model.facade;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.ica.model.command.Command;
import com.ica.model.contact.Contact;
import com.ica.model.contact.ContactGroup;
import com.ica.persistence.dao.sqllite.DatabaseDefinition;
import com.ica.persistence.factory.AbstractDaoFactory;
import com.ica.persistence.factory.DaoType;
import com.ica.persistence.interfaces.CommandDao;
import com.ica.persistence.interfaces.ContactDao;
import com.ica.persistence.interfaces.ContactGroupDao;

/**
 * 
 * @author Cigdem
 *
 */
public class ContactFacadeImpl implements ContactFacadeInterface
{
	private static ContactFacadeInterface instance = null;
	
	private ContactGroupDao contactGroupDao = null;
	private ContactDao contactDao = null;
	private CommandDao commandDao = null;
	
	private ContactFacadeImpl(Context context)
	{
		AbstractDaoFactory factory = AbstractDaoFactory.getFactory(DaoType.SQL_LITE, context);
		factory.createConnection();
		contactGroupDao = factory.getContactGroupDao();
		contactDao = factory.getContactDao();
		commandDao = factory.getAbstractCommandDao();
	}
	
	//@return instance
	public static ContactFacadeInterface getInstance(Context context)
	{
		if(instance == null)
		{
			instance = new ContactFacadeImpl(context);
		}
		return instance;
	}
	
	public boolean insertContactGroup(ContactGroup contactGroup)
	{
		// ContactGroup Table
		ContentValues contactGroupValues = new ContentValues();
		
		contactGroupValues.put(DatabaseDefinition.CONTACT_GROUP_COLUMN_NAME, contactGroup.getName());
		
		// contactGroup.idPolicy is null
		long contactGroupId = contactGroupDao.insert(contactGroupValues);
		
		// Contact Table
		List<Contact> contacts = contactGroup.getContacts();
		for(Contact contact : contacts)
		{
			ContentValues contactValues = new ContentValues();
			contactValues.put(DatabaseDefinition.CONTACT_COLUMN_ID, contact.getId());
			contactValues.put(DatabaseDefinition.CONTACT_PHONE_NUMBER, contact.getPhoneNumber());
			contactDao.insert(contactValues, contactGroupId);
		}
		return true;
	}
	
	public void updateContactGroup(long id, String newName, List<Contact> selectedContacts)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseDefinition.CONTACT_GROUP_COLUMN_NAME, newName);
		contactGroupDao.update(id, values);
		contactDao.updateFromContactGroup(id, selectedContacts);
	}
	
	public boolean deleteContactGroup(long id)
	{
		contactGroupDao.delete(id);
		commandDao.deleteAllFromContactGroup(id);
		return true;
	}
	
   /* Every find* method will get the object in a "lazy" way. It means all its members will be set to null.
	* This will improve the performance due to the fact that, according to the activity which is running,
	* just some of its members will be retrieved. 
	*/
	
	public ContactGroup findContactGroup(long id)
	{
		return contactGroupDao.find(id);
	}
	
	public List<Contact> findContactsOfContactGroup(long id)
	{
		return contactDao.findAllInContactGroup(id);
	}
	
	public List<Command> findCommandsOfContactGroup(long id)
	{
		return commandDao.findAllInContactGroup(id);
	}
	
	public List<ContactGroup> findAllContactGroups()
	{
		return contactGroupDao.findAll();
	}
	
	public void updateCommandsOfCotactGroup(Long idContactGroup, List<Command> commands)
	{
		commandDao.updateFromContactGroup(idContactGroup, commands);
	}
}
