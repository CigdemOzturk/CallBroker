package com.ica.android.ui.policy;

import java.util.HashMap;
import java.util.List;

import com.ica.R;
import com.ica.android.ui.policy.time.TimeTableManagerActivity;
import com.ica.model.contact.ContactGroup;
import com.ica.model.facade.ContactFacadeImpl;
import com.ica.model.facade.ContactFacadeInterface;
import com.ica.model.facade.PolicyFacadeImpl;
import com.ica.model.facade.PolicyFacadeInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author Cigdem
 *
 */
public abstract class PolicyActivity extends Activity
{
	protected EditText etName = null;
	protected PolicyFacadeInterface policyFacade;
	protected ContactFacadeInterface contactFacade;
	protected ContactGroup[] contacts;
	protected int lastCommand = -1;
	protected boolean[] checkedContacts;
	protected HashMap<Long, Integer> checkedPos;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		policyFacade = PolicyFacadeImpl.getInstance(this);
		contactFacade = ContactFacadeImpl.getInstance(this);
	}
	
	@Override
	public void onDestroy()
	{
		TimeTableManagerActivity.clearResult();
		super.onDestroy();
	}
	
	protected void init(int layout)
	{
		setContentView(layout);
		Button save = (Button) findViewById(R.id.bSavePolicy);
		etName = (EditText) findViewById(R.id.etPolicyName);
		save.setOnClickListener(onClickSave());
		List<ContactGroup> c = contactFacade.findAllContactGroups();
		checkedContacts = new boolean[c.size()];
		contacts = (ContactGroup[]) c.toArray(new ContactGroup[c.size()]);
		checkedPos = new HashMap<Long, Integer>(c.size());
		final String[] contactGroupNames = new String[contacts.length];
		for(int i = 0; i < contacts.length; i++)
		{
			contactGroupNames[i] = contacts[i].getName();
			checkedPos.put(contacts[i].getId(), i);
		}
		Button contactGroup = (Button) findViewById(R.id.bAddContactGroup);
		contactGroup.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(PolicyActivity.this);
				builder.setTitle("Pick a ContactGroup");
				builder.setMultiChoiceItems(contactGroupNames, checkedContacts, new DialogInterface.OnMultiChoiceClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton, boolean isChecked)
					{
						checkedContacts[whichButton] = isChecked;
					}
				}).setPositiveButton(R.string.save_name, null);
				AlertDialog dialog = builder.create();
				for(int i = 0; i < checkedContacts.length; i++)
					dialog.getListView().setItemChecked(i, checkedContacts[i]);
				dialog.show();
			}
		});
		Button bTimeFilter = (Button) findViewById(R.id.bAddTimeFilter);
		bTimeFilter.setOnClickListener(onClickTimeTable());
	}
	
	protected abstract OnClickListener onClickSave();
	protected abstract OnClickListener onClickTimeTable();
}
