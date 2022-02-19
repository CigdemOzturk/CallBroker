package com.ica.android.ui.policy;

import java.util.ArrayList;
import java.util.List;

import com.ica.R;
import com.ica.model.facade.PolicyFacadeImpl;
import com.ica.model.facade.PolicyFacadeInterface;
import com.ica.model.policy.Policy;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * @author Cigdem
 *
 */
public class PolicyManagementActivity extends ListActivity
{
	private PolicyFacadeInterface policyFacade;
	private List<Policy> policies = null;
	private ArrayAdapter<Policy> arrayAdapter = null;
	static final int EDIT_POLICY = 0;
	static final int NEW_POLICY = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		policyFacade = PolicyFacadeImpl.getInstance(this);
		setContentView(R.layout.policy_tab);
		policies = new ArrayList<Policy>(policyFacade.findAllPolicies());
		arrayAdapter = new ArrayAdapter<Policy>(this, android.R.layout.simple_list_item_single_choice, policies);
		setListAdapter(arrayAdapter);
		final ListView lv = getListView();
		for(Policy policy : policies)
			if(policy.isActive())
				lv.setItemChecked(arrayAdapter.getPosition(policy), true);
				lv.setOnItemClickListener(new OnItemClickListener ()
				{
					public void onItemClick(AdapterView<?> parent, View view, int pos, long rowId)
					{
						Policy activePolicy = (Policy) lv.getAdapter().getItem(pos);
						policyFacade.setActivePolicy(activePolicy.getId());
					}
				});
				registerForContextMenu(lv);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if((requestCode == EDIT_POLICY) || requestCode == NEW_POLICY)
		{
			if(resultCode == RESULT_OK)
			{
				policies.clear();
				policies.addAll(policyFacade.findAllPolicies());
				arrayAdapter.notifyDataSetChanged();
				Toast.makeText(this, "Policy saved", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.policy_menu,  menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch(item.getItemId())
		{
		case R.id.addPolicy:
			Intent intent = new Intent (this, NewPolicyActivity.class);
			startActivityForResult(intent, NEW_POLICY);
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
		inflater.inflate(R.menu.policy_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Policy selectedPolicy = (Policy) getListAdapter().getItem(info.position);
		switch(item.getItemId())
		{
		case R.id.editPolicy:
			Intent intent = new Intent(PolicyManagementActivity.this, EditPolicyActivity.class);
			intent.putExtra("policyId", selectedPolicy.getId());
			startActivityForResult(intent, EDIT_POLICY);
			return true;
		case R.id.deletePolicy:
			AlertDialog alert = createDeletePolicyDialog(selectedPolicy);
			alert.show();return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	private AlertDialog createDeletePolicyDialog(final Policy policy)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.delete_policy_dialog_name).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				PolicyManagementActivity.this.policyFacade.deletePolicy(policy.getId());
				PolicyManagementActivity.this.policies.remove(policy);
				PolicyManagementActivity.this.arrayAdapter.notifyDataSetChanged();
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
