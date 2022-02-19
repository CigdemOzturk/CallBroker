package com.ica.android.ui.contact.util;

import java.util.ArrayList;
import java.util.List;

import com.ica.model.contact.Contact;

/**
 * 
 * @author Cigdem
 *
 */
public abstract class ContactConversor 
{
	public static Contact toContact(AndroidContact androidContact)
	{
		return new Contact(androidContact.getId(), androidContact.getName(), androidContact.getPhoneNumber());
	}
	
	public static List<Contact> toContact(List<AndroidContact> androidContacts)
	{
		ArrayList<Contact> res = new ArrayList<Contact>(androidContacts.size());
		for(AndroidContact contact : androidContacts)
			res.add(toContact(contact));
		return res;
	}
}
