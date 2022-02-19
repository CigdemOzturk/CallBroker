package com.ica.persistence.interfaces;

import java.util.List;

import android.content.ContentValues;

import com.ica.model.contact.Contact;

/**
 * 
 * @author Cigdem
 *
 */
public interface ContactDao extends GenericDao<Contact>
{
	List<Contact> findAllInContactGroup(long id);
	long insert(ContentValues values, long idContactGroup);
	void updateFromContactGroup(long id, List<Contact> selectedContacts);
}
