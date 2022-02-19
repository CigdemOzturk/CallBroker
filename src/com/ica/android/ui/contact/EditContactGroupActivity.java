package com.ica.android.ui.contact;

import java.util.ArrayList;
import java.util.List;

import com.ica.R;
import com.ica.android.ui.contact.util.AndroidContact;
import com.ica.android.ui.contact.util.ContactArrayAdapter;
import com.ica.android.ui.contact.util.ContactConversor;
import com.ica.model.contact.Contact;
import com.ica.model.contact.ContactGroup;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 
 * @author Cigdem
 *
 */
public class EditContactGroupActivity extends ContactGroupActivity
{
	private ContactGroup contactGroup = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		init(R.layout.edit_contact_group);
		Bundle extras = getIntent().getExtras();
		long idContactGroup = (Long) extras.get("contactGroupId");
		contactGroup = contactFacade.findContactGroup(idContactGroup);
		contactGroup.setContacts(contactFacade.findContactsOfContactGroup(idContactGroup));
		etName.setText(contactGroup.getName());
	}
	
	private AndroidContact getAndroidContactById(ArrayList<AndroidContact> temp, long id)
	{
		for(AndroidContact contact : temp)
		{
			if(contact.getId() == id)
				return contact;
		}
		return null;
	}
	
	@Override
	protected Runnable getAfterFillList(final ArrayList<AndroidContact> temp)
	{
		return new Runnable()
		{
			public void run()
			{
				if(temp != null && temp.size() > 0)
				{
					contacts.addAll(temp);
					adapter = new ContactArrayAdapter(EditContactGroupActivity.this, R.layout.contact_list_view, contacts);
					lv.setAdapter(adapter);
					List<Contact> currentContacts = contactGroup.getContacts();
					for(Contact current : currentContacts)
					{
						AndroidContact inList = getAndroidContactById(contacts, current.getId());
						if(inList != null)
						{
							inList.setChecked(true);
						}
					}
					adapter.notifyDataSetChanged();
				}
				progressDialog.dismiss();
			}
		};
	}
	
	@Override
	protected OnClickListener onClickSave()
	{
		OnClickListener listener = new OnClickListener()
		{
			public void onClick(View v)
			{

				if(etName.getText().toString().equals("")) 
				{
					Toast.makeText(EditContactGroupActivity.this, "The Contacts Group's name is require",  Toast.LENGTH_SHORT).show();
				}
				else
				{
					String newName = etName.getText().toString().trim();
					List<Contact> selectedContacts = ContactConversor.toContact(getSelectedContacts());
					ContactConversor.toContact(getSelectedContacts());
					contactFacade.updateContactGroup(contactGroup.getId(), newName, selectedContacts);
					setResult(RESULT_OK, null);
					finish();
				}
				
			}
		};
		return listener;
	}
}
