package com.ica.android.ui.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.ica.R;
import com.ica.android.ui.contact.util.AndroidContact;
import com.ica.android.ui.contact.util.ContactArrayAdapter;
import com.ica.model.facade.ContactFacadeImpl;
import com.ica.model.facade.ContactFacadeInterface;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author Cigdem
 *
 */
public abstract class ContactGroupActivity extends Activity
{
	protected ListView lv = null;
	protected EditText etName = null;
	protected ArrayList<AndroidContact> contacts = null;
	protected boolean[] checkedContacts;
	protected ContactArrayAdapter adapter = null;
	protected Runnable viewContent = null;
	protected ProgressDialog progressDialog = null;
	protected ContactFacadeInterface contactFacade;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		contactFacade = ContactFacadeImpl.getInstance(this);
	}
	
	protected void init(int layout)
	{
		setContentView(layout);
		Button save = (Button) findViewById(R.id.bSaveContactGroup);
		save.setOnClickListener(onClickSave());
		contacts = new ArrayList<AndroidContact>();
		etName = (EditText) findViewById(R.id.etContactGroupName);
		lv = (ListView) findViewById(R.id.lvContacts);
		viewContent = new Runnable()
		{
			public void run()
			{
				populateContactList();
			}
		};
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
			{
				AndroidContact c = contacts.get(pos);
				
				if (!c.getChecked())
				{
					view.setBackgroundColor(Color.DKGRAY);
				}
				else
				{
					view.setBackgroundColor(Color.TRANSPARENT);
				}
					
				c.setChecked(!c.getChecked());
				
				
				
//				parent.notifyDataSetChanged();
			}
		});
		Thread thread = new Thread(null, viewContent, "LoadContacts");
		thread.start();
		progressDialog = ProgressDialog.show(this, "Wait", "Loading contacts...", true);
	}
	protected abstract OnClickListener onClickSave();
	
	protected ArrayList<AndroidContact> getSelectedContacts()
	{
		ArrayList<AndroidContact> res = new ArrayList<AndroidContact>();
		for(int i = 0; i < contacts.size(); i++)
		{
			if(contacts.get(i).getChecked())
				res.add(contacts.get(i));
		}
		return res;
	}
	
	private String removeChar(String s, char c)
	{
		String r = "";
		for(int i = 0; i < s.length(); i++)
		{
			if(s.charAt(i) != c)
				r += s.charAt(i);
		}
		return r;
	}
	
	protected void populateContactList()
	{
		Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		ArrayList<AndroidContact> temp = new ArrayList<AndroidContact>();
		while(phones.moveToNext())
		{
			long id = phones.getLong(phones.getColumnIndex(ContactsContract.Contacts._ID));
			String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			AndroidContact contact = new AndroidContact(id, name, removeChar(phoneNumber, '-'));
			temp.add(contact);
		}
		phones.close();
		checkedContacts = new boolean[contacts.size()];
		Collections.sort(temp, new Comparator<AndroidContact>(){
			public int compare(AndroidContact o1, AndroidContact o2)
			{
				return o1.getName().compareTo(o2.getName());
			}
		});
		runOnUiThread(getAfterFillList(temp));
	}
	
	protected Runnable getAfterFillList(final ArrayList<AndroidContact> temp)
	{
		return new Runnable()
		{
			public void run()
			{
				if(temp != null && temp.size() > 0)
				{
					contacts.addAll(temp);
					adapter = new ContactArrayAdapter(ContactGroupActivity.this, R.layout.contact_list_view, contacts);
					lv.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
				progressDialog.dismiss();
			}
		};
	}
}
