package com.ica.persistence.interfaces;

import java.util.List;

import android.content.ContentValues;

import com.ica.android.service.Caller;
import com.ica.model.contact.Contact;

/**
 * 
 * @author Cigdem
 *
 */
public interface CallerDao extends GenericDao<Caller>
{
	List<Caller> findAllMatching(String number);
	long insert(ContentValues values);
	void updateContact(long id);
}
