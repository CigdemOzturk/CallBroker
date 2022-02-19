package com.ica.android.ui.contact;

import java.util.List;
import java.util.Vector;

import com.ica.R;
import com.ica.android.ui.command.CommandActivity;
import com.ica.model.contact.ContactGroup;
import com.ica.model.facade.ContactFacadeImpl;
import com.ica.model.facade.ContactFacadeInterface;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * 
 * @author Cigdem
 *
 */
public class ContactsGroupManagementActivity extends ListActivity
{
	private ContactFacadeInterface contactFacade;
	private List<ContactGroup> contactGroups = null;
	private ArrayAdapter<ContactGroup> arrayAdapter = null;
	static final int EDIT_CONTACT_GROUP = 1;
	static final int NEW_CONTACT_GROUP = 2;
	private static final int ASSIGN_COMMAND = 3;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		contactFacade = ContactFacadeImpl.getInstance(this);
		setContentView(R.layout.contact_group_tab);
		contactGroups = new Vector<ContactGroup>();
		contactGroups.addAll(contactFacade.findAllContactGroups());
		arrayAdapter = new ArrayAdapter<ContactGroup>(this, android.R.layout.simple_list_item_1, contactGroups); // android.R.layout
		setListAdapter(arrayAdapter);
		ListView lv = getListView();
		registerForContextMenu(lv);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if((requestCode == EDIT_CONTACT_GROUP) || (requestCode == NEW_CONTACT_GROUP))
		{
			if(resultCode == RESULT_OK)
			{
				contactGroups.clear();
				contactGroups.addAll(contactFacade.findAllContactGroups());
				arrayAdapter.notifyDataSetChanged();
				Toast.makeText(this, "Contact Group saved", Toast.LENGTH_SHORT).show();
			}
		}
		else if(requestCode == ASSIGN_COMMAND)
		{
			if(resultCode == RESULT_OK)
			{
				contactGroups.clear();
				contactGroups.addAll(contactFacade.findAllContactGroups());
				arrayAdapter.notifyDataSetChanged();
				Toast.makeText(this, "Command assigned", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contact_group_menu, menu);
		return true;		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch(item.getItemId())
		{
		case R.id.addContactGroup:
			Intent intent = new Intent(this, NewContactGroupActivity.class);
			startActivityForResult(intent, NEW_CONTACT_GROUP);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contact_group_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		ContactGroup selectedContactGroup = (ContactGroup) getListAdapter().getItem(info.position);
		switch(item.getItemId())
		{
		case R.id.assignCommand:
			Intent intent1 = new Intent(this, CommandActivity.class);
			intent1.putExtra("contactGroupId", selectedContactGroup.getId());
			startActivityForResult(intent1, ASSIGN_COMMAND);
			return true;
		case R.id.editContactGroup:
			Intent intent2 = new Intent(this, EditContactGroupActivity.class);
			intent2.putExtra("contactGroupId", selectedContactGroup.getId());
			startActivityForResult(intent2, EDIT_CONTACT_GROUP);
			return true;
		case R.id.deleteContactGroup:
			AlertDialog alert = createDeleteContactGroupDialog(selectedContactGroup);
			alert.show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	private AlertDialog createDeleteContactGroupDialog(final ContactGroup contactGroup)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_contact_group_dialog_name).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				ContactsGroupManagementActivity.this.contactFacade.deleteContactGroup(contactGroup.getId());
				ContactsGroupManagementActivity.this.contactGroups.remove(contactGroup);
				ContactsGroupManagementActivity.this.arrayAdapter.notifyDataSetChanged();
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
		return builder.create();
	}
}
