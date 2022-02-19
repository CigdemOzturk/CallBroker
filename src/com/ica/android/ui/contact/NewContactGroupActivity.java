package com.ica.android.ui.contact;

import java.util.List;

import com.ica.R;
import com.ica.android.ui.contact.util.AndroidContact;
import com.ica.android.ui.contact.util.ContactConversor;
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
public class NewContactGroupActivity extends ContactGroupActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		init(R.layout.new_contact_group);
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
					Toast.makeText(NewContactGroupActivity.this, "The Contacts Group's name is required", Toast.LENGTH_SHORT).show();
				}
				else
				{
					ContactGroup contactGroup = new ContactGroup(etName.getText().toString().trim());
					List<AndroidContact> selectedContacts = getSelectedContacts();
					contactGroup.setContacts(ContactConversor.toContact(selectedContacts));
					contactFacade.insertContactGroup(contactGroup);
					setResult(RESULT_OK, null);
					finish();
				}
			}
		};
		return listener;
	}
}
